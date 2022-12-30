package io.github.punishmentsx.utils;

import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClickableMessage {
    private final List<TextComponent> components = new ArrayList<>();
    private TextComponent current;

    public ClickableMessage(final String msg) {
        this.add(msg);
    }

    public ClickableMessage add(final String msg) {
        final TextComponent component = new TextComponent(msg);
        this.components.add(component);
        this.current = component;
        return this;
    }

    private void hover(final TextComponent component, final String msg) {
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(msg).create()));
    }

    public ClickableMessage hover(final String msg) {
        this.hover(this.current, msg);
        return this;
    }

    public ClickableMessage hoverAll(final String msg) {
        this.components.forEach(component -> this.hover(component, msg));
        return this;
    }

    private void command(final TextComponent component, final String command) {
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
    }

    private void link(TextComponent component, String url) {
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
    }

    public ClickableMessage command(final String command) {
        this.command(this.current, command);
        return this;
    }

    public ClickableMessage commandAll(final String command) {
        this.components.forEach(component -> this.command(component, command));
        return this;
    }

    public ClickableMessage color(final String color) {
        this.current.setColor(net.md_5.bungee.api.ChatColor.getByChar(color.charAt(1)));
        return this;
    }

    public ClickableMessage color(final ChatColor color) {
        this.current.setColor(color.asBungee());
        return this;
    }

    public ClickableMessage style(final ChatColor color) {
        switch (color) {
            case UNDERLINE:
                this.current.setUnderlined(true);
                break;
            case BOLD:
                this.current.setBold(true);
                break;
            case ITALIC:
                this.current.setItalic(true);
                break;
            case MAGIC:
                this.current.setObfuscated(true);
                break;
            case AQUA:
                this.current.setColor(ChatColor.AQUA.asBungee());
                break;
            case BLACK:
                this.current.setColor(ChatColor.BLACK.asBungee());
                break;
            case BLUE:
                this.current.setColor(ChatColor.BLUE.asBungee());
                break;
            case DARK_AQUA:
                this.current.setColor(ChatColor.DARK_AQUA.asBungee());
                break;
            case DARK_BLUE:
                this.current.setColor(ChatColor.DARK_BLUE.asBungee());
                break;
            case DARK_GRAY:
                this.current.setColor(ChatColor.DARK_GRAY.asBungee());
                break;
            case DARK_GREEN:
                this.current.setColor(ChatColor.DARK_GREEN.asBungee());
                break;
            case DARK_PURPLE:
                this.current.setColor(ChatColor.DARK_PURPLE.asBungee());
                break;
            case DARK_RED:
                this.current.setColor(ChatColor.DARK_RED.asBungee());
                break;
            case GOLD:
                this.current.setColor(ChatColor.GOLD.asBungee());
                break;
            case GRAY:
                this.current.setColor(ChatColor.GRAY.asBungee());
                break;
            case GREEN:
                this.current.setColor(ChatColor.GREEN.asBungee());
                break;
            case LIGHT_PURPLE:
                this.current.setColor(ChatColor.LIGHT_PURPLE.asBungee());
                break;
            case RED:
                this.current.setColor(ChatColor.RED.asBungee());
                break;
            case RESET:
                this.current.setColor(ChatColor.WHITE.asBungee());
                break;
            case STRIKETHROUGH:
                this.current.setStrikethrough(true);
                break;
            case WHITE:
                this.current.setColor(ChatColor.WHITE.asBungee());
                break;
            case YELLOW:
                this.current.setColor(ChatColor.YELLOW.asBungee());
                break;
            default:
                break;
        }
        return this;
    }

    public void sendToPlayer(final Player player) {
        player.spigot().sendMessage(this.components.toArray(new BaseComponent[0]));
    }
}
