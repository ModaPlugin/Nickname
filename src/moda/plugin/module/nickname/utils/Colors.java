package moda.plugin.module.nickname.utils;

import moda.plugin.module.nickname.Nickname;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;

public class Colors {

    public static String parseColors(CommandSender c, String s) {
        Nickname n = Nickname.instance;

        for (ChatColor color : ChatColor.values()) {
            try {
                Field field = ChatColor.class.getField("code");
                field.setAccessible(true);
                char code = (char) field.get(color);
                if (c.hasPermission("moda.plugin.module.nickname.set.color." + color.getName())) {
                    s = s.replace("&" + code, ChatColor.COLOR_CHAR + "" + code);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                s = stripColors(s);
                e.printStackTrace();
                break;
            }
        }
        return s;
    }

    public static boolean isBlacklisted(ChatColor c) {
        Nickname n = Nickname.instance;
        return n.getConfig().getStringList("blacklisted-colors").stream().anyMatch(s -> s.equalsIgnoreCase(c.name()));
    }

    public static String stripColors(String s) {
        return xyz.derkades.derkutils.bukkit.Colors.stripColors(s);
    }
}
