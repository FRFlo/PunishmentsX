package io.github.punishmentsx.listeners;

import io.github.punishmentsx.Locale;
import io.github.punishmentsx.PunishmentsX;
import io.github.punishmentsx.profiles.Profile;
import io.github.punishmentsx.punishments.Punishment;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JoinListener implements Listener {
    private final PunishmentsX plugin;

    public JoinListener(PunishmentsX plugin) {
        this.plugin = plugin;
        plugin.registerListener(this);
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        Profile profile;

        if (plugin.getProfileManager().getProfiles().containsKey(uuid)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Votre profil est déjà chargé, veuillez vous reconnecter !");

            profile = plugin.getProfileManager().get(uuid);

            if (profile == null) {
                plugin.getProfileManager().getProfiles().remove(uuid);
            } else {
                plugin.getProfileManager().push(false, profile, true);
            }
        } else {
            profile = plugin.getProfileManager().find(uuid, true);
            if (profile == null) {
                profile = plugin.getProfileManager().createProfile(uuid);
            }

            Punishment blacklist = profile.getActivePunishment(Punishment.Type.BLACKLIST);
            Punishment ban = profile.getActivePunishment(Punishment.Type.BAN);

            List<String> list = new ArrayList<>();
            if (blacklist != null) {
                for (String string : Locale.BLACKLIST_MESSAGE.formatLines(plugin)) {
                    list.add(string.replace("%reason%", blacklist.getIssueReason()));
                }

                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, String.join("\n", list));
                profile.addIp(event.getAddress().getHostAddress());
                plugin.getProfileManager().push(true, profile, true);
            }

            if (ban != null) {
                for (String string : Locale.BAN_MESSAGE.formatLines(plugin)) {
                    list.add(string
                            .replace("%expirationDate%", ban.expiry())
                            .replace("%expiry%", ban.duration())
                            .replace("%reason%", ban.getIssueReason()));
                }

                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, String.join("\n", list));
                profile.addIp(event.getAddress().getHostAddress());
                plugin.getProfileManager().push(true, profile, true);
            }

            if (ban == null && blacklist == null) {
                profile.addIp(event.getAddress().getHostAddress());
                event.allow();
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().get(player.getUniqueId());

        if(profile == null) {
            player.kickPlayer(ChatColor.RED + "Votre profil ne s'est pas chargé correctement, veuillez vous reconnecter.");
            return;
        }

        profile.setName(player.getName());

        profile.update();
    }
}
