package moda.plugin.module.nickname;

import moda.plugin.moda.module.IMessage;

import java.util.logging.Level;

public enum NicknameMessage implements IMessage {
    UPDATE_NOTIFY(
            // Level.INFO,
            "update.notify",
            "&7Your nickname was set to &f{NICKNAME}&7."),
    UPDATE_REMOVE(
            // Level.INFO,
            "update.remove",
            "&7Your nickname was removed."),
    COMMAND_NICKNAME_DESCRIPTION(
            // Level.INFO,
            "commands.nickname.description",
            "&f/nickname&7 is used to set nicknames that can be seen in chat messages and the tab-list"),
    COMMAND_NICKNAME_USAGE(
            // Level.INFO,
            "commands.nickname.usage",
            "&f/nickname &7[nickname|&8(target <nickname>)]"),
    COMMAND_NICKNAME_SELF_GET(
            // Level.INFO,
            "commands.nickname.self.get",
            "&7Your nickname is currently set to &f{NICKNAME}&7."),
    COMMAND_NICKNAME_SELF_UPDATE(
            // Level.INFO,
            "commands.nickname.self.update",
            "&7You've set your nickname to &f{NICKNAME}&7."),
    COMMAND_NICKNAME_SELF_REMOVE(
            // Level.INFO,
            "commands.nickname.self.reset",
            "&7You've reset &8(removed)&7 your nickname."),
    COMMAND_NICKNAME_SELF_ERROR_UNKNOWN(
            // Level.SEVERE,
            "commands.nickname.self.error.unknown",
            "&cAn unknown error has occurred while setting your nickname, please check the console"),
    COMMAND_NICKNAME_OTHER_UPDATE(
            // Level.INFO,
            "commands.nickname.other.update",
            "&7You've set {TARGET}&7's nickname to &a{NICKNAME}&7."),
    COMMAND_NICKNAME_OTHER_REMOVE(
            // Level.INFO,
            "commands.nickname.other.reset",
            "&7You've reset &8(removed)&7 {TARGET}&7's nickname."),
    COMMAND_NICKNAME_OTHER_ERROR_UNKNOWN(
            // Level.SEVERE,
            "commands.nickname.other.error.unknown",
            "&cAn unknown error has occurred while setting &f{TARGET}&c's nickname, please check the console"),
    COMMAND_NICKNAME_OTHER_ERROR_INVALID_PLAYER(
            // Level.SEVERE,
            "commands.nickname.other.error.player-not-found",
            "&cThe target &f{TARGET}&c was not found, is &7{TARGET}&c a valid minecraft user?"),
    COMMAND_NICKNAME_WARN_COLOR(
            // Level.WARNING,
            "commands.nickname.warn.color",
            "&6You don't have permission to use colors in nicknames, the nickname will be set without colors"),
    COMMAND_NICKNAME_WARN_FORMAT(
            // Level.WARNING,
            "commands.nickname.warn.format",
            "&6You don't have permission to use formatting in nicknames, the nickname will be set without formatting"),
    COMMAND_NICKNAME_WARN_TAKEN(
            // Level.WARNING,
            "commands.nickname.warn.taken",
            "&6The nickname &f{NICKNAME}&6 is already in use."),
    COMMAND_NICKNAME_ERROR_NO_PERMISSION(
            // Level.SEVERE,
            "commands.nickname.error.no-permission",
            "&cYou do not have permission to set nicknames."),
    COMMAND_NICKNAME_ERROR_ILLEGAL_CHARACTER(
            // Level.SEVERE,
            "commands.nickname.error.illegal-character",
            "&cThe nickname you've provided contains illegal characters, nicknames cannot contain &f{ILLEGAL-CHAR}&c."),
    COMMAND_NICKNAME_ERROR_SENDER_NOT_PLAYER(
            // Level.SEVERE,
            "commands.nickname.error.sender-not-player",
            "&cYou can only set your own nickname as a player."),
    COMMAND_NICKNAME_ERROR_TOO_MANY_ARGUMENTS(
            // Level.SEVERE,
            "commands.nickname.error.too-many-arguments",
            "&cYou've provided too many arguments. Nicknames cannot contain &fspaces&c.")
    ;

//    private Level level;
    private String path;
    private String defaultMessage;

    NicknameMessage( // final Level level,
                    final String path,
                    final String defaultMessage) {
//        this.level = level;
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getDefault() {
        return this.defaultMessage;
    }
}
