package moda.plugin.module.nickname;

import java.io.IOException;
import java.lang.String;

import moda.plugin.moda.util.UuidFetcher;
import moda.plugin.module.nickname.utils.Colors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
            module.getStorage().getNickname(((Player) sender).getUniqueId())
                    .onComplete(nickname -> {

                        if (nickname.isPresent()) {
                            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_SELF_GET,
                                    "NICKNAME", nickname.get());
                        } else {
                            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_SELF_GET,
                                    "NICKNAME", sender.getName());
                        }
                        module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_DESCRIPTION);
                        module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_USAGE);
                    })
                    .onException(e -> {
                        e.printStackTrace();
                    }).retrieveAsync();
            return true;
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

        if (sender.hasPermission("moda.module.nickname.set.color")) {
            nickname = Colors.parseColors(sender, nickname);
        } else {
            nickname = Colors.stripColors(nickname);
            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_WARN_COLOR);
        }

        if (sender.hasPermission("moda.module.nickname.set.format")) {
            nickname = Colors.parseColors(sender, nickname);
        } else {
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
                            if (targetName.equals(finalNickname)) {
                                opt.ifPresent(target -> {
                                    module.getStorage().removeNickname(target.getUniqueId())
                                            .onComplete(var -> {
                                                module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_REMOVE,
                                                        "TARGET", target.getName());

                                                if (target.isOnline() && !target.equals(sender)) {
                                                    target.getPlayer().sendMessage(module.getLang().getMessage(NicknameMessage.UPDATE_NOTIFY,
                                                            "NICKNAME", finalNickname));
                                                }
                                            })
                                            .onException(e -> {
                                                module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_UNKNOWN,
                                                        "TARGET", targetName);
                                                e.printStackTrace();
                                            }).retrieveAsync();
                                });
                            } else {
                                if (!sender.hasPermission("moda.module.nickname.set.taken")) {
                                    module.getStorage().nicknameExists(finalNickname)
                                            .onComplete(nicknameExists -> {
                                                if (nicknameExists) {
                                                    module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_WARN_TAKEN,
                                                            "NICKNAME", finalNickname);
                                                } else {
                                                    setNickname(sender, opt, finalNickname);
                                                }
                                            })
                                            .onException(e -> {
                                                module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_UNKNOWN,
                                                        "TARGET", targetName);
                                                e.printStackTrace();
                                            }).retrieveAsync();
                                } else {
                                    setNickname(sender, opt, finalNickname);
                                }
                            }
                        })
                        .onException(e -> {
                            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_INVALID_PLAYER,
                                    "TARGET", targetName);
                            e.printStackTrace();
                        }).retrieveAsync();
                return true;
            }
        }
        return true;
    }

    private void setNickname(CommandSender sender, Optional<OfflinePlayer> opt, String nickname) {
        opt.ifPresent(target -> {
            try {
                module.getFileStorageHandler().setProperty(target.getUniqueId(), "debug", "debug");
            } catch (IOException e) {
                e.printStackTrace();
            }
            module.getStorage().setNickname(target.getUniqueId(), nickname)
                    .onComplete(var -> {
                        module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_UPDATE,
                                "TARGET", target.getName(),
                                "NICKNAME", nickname);

                        if (target.isOnline() && !target.equals(sender)) {
                            target.getPlayer().sendMessage(module.getLang().getMessage(NicknameMessage.UPDATE_NOTIFY,
                                    "NICKNAME", nickname));
                        }
                    })
                    .onException(e -> {
                        module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_UNKNOWN,
                                "TARGET", nickname);
                        e.printStackTrace();
                    }).retrieveAsync();
        });
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
