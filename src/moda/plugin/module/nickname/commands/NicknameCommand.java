package moda.plugin.module.nickname.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class NicknameCommand extends Command {
    public NicknameCommand() {
        super("nickname", "opens a GUI to set a nickname.", "/nickname", Arrays.asList("nick"));
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        return false;
    }
}
