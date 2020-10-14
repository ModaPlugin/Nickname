package cx.moda.module.nickname.utils;

import cx.moda.module.nickname.Nickname;
import cx.moda.module.nickname.NicknameMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.List;

public class Colors {

    public static String parseColors(CommandSender c, String s) {

        Nickname n = Nickname.instance;

        for (ChatColor color : ChatColor.values()) {
            try {
                Field field = ChatColor.class.getDeclaredField("code");
                field.setAccessible(true);
                char code = (char) field.get(color);
                if (!s.contains("&" + code)) continue;

                if (c.hasPermission("moda.plugin.module.nickname.set.color." + color.getName()) && !isBlacklisted(color)) {
                    s = s.replace("&" + code, ChatColor.COLOR_CHAR + "" + code);
                } else if (isBlacklisted(color)) {
                    if (!c.hasPermission("moda.module.nickname.set.color.blacklist.bypass")) {
                        n.getLang().send(c, NicknameMessage.COMMAND_NICKNAME_WARN_COLOR_BLACKLISTED,
                                "PERMISSION", "moda.module.nickname.set.color.blacklist.bypass",
                                "CODE", "&" + code + ChatColor.getByChar(code).getName());
                        s = s.replace("&" + code, "");
                    }
                } else {
                    n.getLang().send(c, NicknameMessage.COMMAND_NICKNAME_WARN_COLOR_CODE,
                            "PERMISSION", "moda.module.nickname.set.color." + ChatColor.getByChar(code).getName(),
                            "CODE", "&" + code + ChatColor.getByChar(code).getName());
                    s = s.replace("&" + code, "");
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

        boolean isBlacklisted = false;
        List<String> blacklistedColors = n.getConfig().getStringList("blacklisted-colors");

        if (blacklistedColors.stream().anyMatch(s -> s.trim().equalsIgnoreCase(c.name()))) {
            isBlacklisted = true;
        }

        return isBlacklisted;
//        return n.getConfig().getStringList("blacklisted-colors").stream().anyMatch(s -> s.equalsIgnoreCase(c.name()));
    }

    public static String stripColors(String s) {
        return xyz.derkades.derkutils.bukkit.Colors.stripColors(s);
    }

    public static String stripFormatting(String s) {
        return xyz.derkades.derkutils.bukkit.Colors.stripColors(s);
    }

    public static String strip(String s) {
        s = stripColors(s);
        s = stripFormatting(s);
        return s;
    }
}
