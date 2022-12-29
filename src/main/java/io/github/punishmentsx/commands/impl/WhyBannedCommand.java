package io.github.punishmentsx.commands.impl;

import io.github.punishmentsx.Locale;
import io.github.punishmentsx.PunishmentsX;
import io.github.punishmentsx.commands.BaseCommand;
import io.github.punishmentsx.evasion.EvasionCheck;
import io.github.punishmentsx.profiles.Profile;
import io.github.punishmentsx.utils.ThreadUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class WhyBannedCommand extends BaseCommand {

    private final PunishmentsX plugin;

    public WhyBannedCommand(PunishmentsX plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args, String alias) {
        if (!sender.hasPermission(Locale.HISTORY_PERMISSION.format(plugin))) {
            sender.sendMessage(Locale.NO_PERMISSION.format(plugin));
            return;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + String.format("Usage: /%s <joueur>", this.getLabel()));
            return;
        }

        ThreadUtil.runTask(true, plugin, () -> {
            Profile targetProfile = getProfile(sender, plugin, args[0]);
            if (targetProfile == null) {
                return;
            }

            EvasionCheck evasionCheck = new EvasionCheck(targetProfile.getUuid(), targetProfile.getCurrentIp(), plugin);
            String message = evasionCheck.getWhy();

            if (message == null) {
                sender.sendMessage("Le joueur cible devrait pouvoir se joindre à nous !");
            } else {
                sender.sendMessage(message);
            }
        });
    }
}