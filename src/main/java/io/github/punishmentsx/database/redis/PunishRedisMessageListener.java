package io.github.punishmentsx.database.redis;

import com.google.gson.JsonObject;
import io.github.punishmentsx.ConfigValues;
import io.github.punishmentsx.Locale;
import io.github.punishmentsx.PunishmentsX;
import io.github.punishmentsx.profiles.Profile;
import io.github.punishmentsx.utils.ClickableMessage;
import io.github.punishmentsx.utils.Colors;
import org.bukkit.entity.Player;

public class PunishRedisMessageListener implements RedisMessageListener {

    private final PunishmentsX plugin;

    public PunishRedisMessageListener(PunishmentsX plugin) {
        this.plugin = plugin;
        plugin.getRedisSubscriber().getListeners().add(this);
    }

    @Override
    public void onReceive(RedisMessage redisMessage) {
        JsonObject json = redisMessage.getElements();
        if(redisMessage.getInternalChannel().equals(ConfigValues.REDIS_CHANNEL.format(plugin))) {
            RedisAction action = RedisAction.valueOf(json.get("action").getAsString());

            switch(action) {
                case PUNISHMENT:
                    for (Profile profile : plugin.getProfileManager().getProfiles().values()) {
                        Player player = profile.getPlayer();
                        if (player != null && player.isOnline() && player.hasPermission(Locale.SILENT_PERMISSION.format(plugin))) {
                            ClickableMessage message = new ClickableMessage(Colors.convertLegacyColors(json.get("message").getAsString()))
                                    .hover(Colors.convertLegacyColors(json.get("hover").getAsString()));
                            message.sendToPlayer(player);
                        } else if (player != null) {
                            player.sendMessage(Colors.convertLegacyColors(json.get("message").getAsString()));
                        }
                    }
                    break;
                case PUNISHMENT_SILENT:
                    for (Profile profile : plugin.getProfileManager().getProfiles().values()) {
                        Player player = profile.getPlayer();
                        if (player != null && player.isOnline() && player.hasPermission(Locale.SILENT_PERMISSION.format(plugin))) {
                            ClickableMessage message = new ClickableMessage(Colors.convertLegacyColors(json.get("message").getAsString()))
                                    .hover(Colors.convertLegacyColors(json.get("hover").getAsString()));
                            message.sendToPlayer(player);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void close() {
        plugin.getRedisSubscriber().getListeners().remove(this);
    }
}
