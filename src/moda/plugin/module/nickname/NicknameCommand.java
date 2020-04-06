package moda.plugin.module.nickname;

import java.lang.String;

import moda.plugin.moda.util.UuidFetcher;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import xyz.derkades.derkutils.bukkit.Colors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class NicknameCommand implements CommandExecutor, TabCompleter {

    private Nickname module;

    public NicknameCommand(Nickname module) {
        this.module = module;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        System.out.println("debug2");

        if (args.length == 0 && sender instanceof Player) {
            AtomicBoolean callback = new AtomicBoolean(false);
            module.getStorage().getNickname((OfflinePlayer) sender)
                    .onComplete(nickname -> {
                        module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_SELF_GET,
                                "NICKNAME", nickname);
                        module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_DESCRIPTION);
                        module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_USAGE);
                        callback.set(true);
                    })
                    .onException(e -> {
                        e.printStackTrace();
                    }).awaitCompletion();
            return callback.get();
        }

        if (!sender.hasPermission("moda.module.nickname.set")) {
            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_ERROR_NO_PERMISSION);
            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_DESCRIPTION);
            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_USAGE);
            return true;
        }

        if (args.length > 2) {
            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_ERROR_TOO_MANY_ARGUMENTS);
            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_DESCRIPTION);
            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_USAGE);
            return true;
        }

        String nickname = args[args.length - 1];

        if (!sender.hasPermission("moda.module.nickname.set.color")) {
            nickname = Colors.stripColors(nickname);
            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_WARN_COLOR);
        }

        if (!sender.hasPermission("moda.module.nickname.set.format")) {
            nickname = Colors.stripColors(nickname);
            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_WARN_FORMAT);
        }

        if (args.length == 1) {

            System.out.println("debug4");

        }

        if (args.length == 2) {

            System.out.println("debug3");

            String targetName = args[0];

            if (sender.hasPermission("moda.module.nickname.set.others")) {
                String finalNickname = nickname;
                UuidFetcher.getOfflinePlayer(targetName)
                        .onComplete(opt -> {
                            if (args[0].equals(finalNickname)) {
                                opt.ifPresent(target -> {
                                    module.getStorage().removeNickname(target)
                                            .onComplete(var -> {
                                                module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_REMOVE,
                                                        "TARGET", target.getName());

                                                if (target.isOnline()) {
                                                    target.getPlayer().sendMessage(module.getLang().getMessage(NicknameMessage.UPDATE_NOTIFY,
                                                            "NICKNAME", args[1]));
                                                }
                                            })
                                            .onException(e -> {
                                                module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_UNKNOWN,
                                                        "TARGET", args[0]);
                                                e.printStackTrace();
                                            });
                                });
                            } else {

                                module.getStorage().nicknameExists(finalNickname)
                                        .onComplete(nicknameExists -> {
                                            if (nicknameExists) {
                                                module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_WARN_TAKEN,
                                                        "NICKNAME", finalNickname);
                                            }
                                            if (sender.hasPermission("moda.module.nickname.set.taken")) {
                                                opt.ifPresent(target -> {
                                                    module.getStorage().setNickname(target, finalNickname)
                                                            .onComplete(var -> {
                                                                module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_UPDATE,
                                                                        "TARGET", target.getName(),
                                                                        "NICKNAME", finalNickname);

                                                                if (target.isOnline()) {
                                                                    target.getPlayer().sendMessage(module.getLang().getMessage(NicknameMessage.UPDATE_NOTIFY,
                                                                            "NICKNAME", args[1]));
                                                                }
                                                            })
                                                            .onException(e -> {
                                                                module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_UNKNOWN,
                                                                        "TARGET", args[0]);
                                                                e.printStackTrace();
                                                            });
                                                });
                                            }
                                        })
                                        .onException(e -> {
                                            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_UNKNOWN,
                                                    "TARGET", args[0]);
                                            e.printStackTrace();
                                        });
                            }
                        })
                        .onException(e -> {
                            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_INVALID_PLAYER,
                                    "TARGET", args[0]);
                            e.printStackTrace();
                        });
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        List<String> completions = new ArrayList<>();
        List<String> players = new ArrayList<>();

        if (args.length == 1) {
            if (
                    sender instanceof Player
                            && sender.hasPermission("moda.module.nickname.set.others")
                            && args[0].length() > 2
            ) {

                StringUtil.copyPartialMatches(args[0], players, completions);

                Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName);

            }
        }

        return completions;
    }
}
