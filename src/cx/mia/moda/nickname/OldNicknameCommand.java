//package moda.plugin.module.nickname;
//
//import java.lang.String;
//
//import moda.plugin.moda.module.LangFile;
//import moda.plugin.moda.util.UuidFetcher;
//import moda.plugin.module.nickname.storage.NicknameStorageHandler;
//import moda.plugin.module.nickname.utils.Colors;
//import org.bukkit.Bukkit;
//import org.bukkit.OfflinePlayer;
//import org.bukkit.command.*;
//import org.bukkit.entity.Player;
//import org.bukkit.util.StringUtil;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//public class OldNicknameCommand implements CommandExecutor, TabCompleter {
//
//    private Nickname module;
//    private LangFile lang;
//    private NicknameStorageHandler storage;
//
//
//    public OldNicknameCommand(Nickname module) {
//        this.module = module;
//        this.lang = module.getLang();
//        this.storage = module.getStorage();
//    }
//
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//
//        if (args.length == 0) {
//            sendUsage(sender);
//        }
//
//        if (!sender.hasPermission("moda.module.nickname.set")) {
//            lang.send(sender, NicknameMessage.COMMAND_NICKNAME_ERROR_NO_PERMISSION);
//            lang.send(sender, NicknameMessage.COMMAND_NICKNAME_DESCRIPTION);
//            lang.send(sender, NicknameMessage.COMMAND_NICKNAME_USAGE);
//            return true;
//        }
//
//        if (args.length > 2) {
//            lang.send(sender, NicknameMessage.COMMAND_NICKNAME_ERROR_TOO_MANY_ARGUMENTS);
//            lang.send(sender, NicknameMessage.COMMAND_NICKNAME_DESCRIPTION);
//            lang.send(sender, NicknameMessage.COMMAND_NICKNAME_USAGE);
//            return true;
//        }
//
//        String nickname = args[args.length - 1];
//
//        checkFormatting(sender, nickname);
//
//        if (args.length == 1) {
//            if (sender instanceof Player) {
//                OfflinePlayer target = (Player) sender;
//
//                if (!checkPermissions(sender, target)) {
//                    return true;
//                }
//
//                return setNickname(sender, target, nickname);
//
//            } else {
//                sendUsage(sender);
//                return true;
//            }
//        }
//
//        if (args.length == 1) {
//
//            setOwnNickname(sender, nickname);
//
//        }
//
//        if (args.length == 2) {
//
//            String targetName = args[0];
//
//            storage.getPlayersByNickname(targetName)
//                    .onComplete(targets -> {
//
//                        OfflinePlayer target;
//
//                        if (targets.size() > 1) {
//                            target = targets.iterator().next();
//                        }
//                        else {
//                            try {
//                                Optional<OfflinePlayer> opt = UuidFetcher.getOfflinePlayer(targetName).callBlocking();
//                                if (opt.isPresent()) {
//                                    target = opt.get();
//                                }
//                                else {
//                                    // TODO
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    })
//                    .onException(e -> {
//
//                    }).callAsync();
//
//            String senderName;
//
//            if (sender instanceof Player) {
//                Player player = (Player) sender;
//                try {
//                    Optional<String> opt = module.getStorage().getNickname(player.getUniqueId()).callBlocking();
//                    senderName = player.getDisplayName();
//
//                    if (opt.isPresent()) {
//                        senderName = xyz.derkades.derkutils.bukkit.Colors.parseColors(opt.get());
//                    }
//
//                } catch (Exception e) {
//                    senderName = player.getDisplayName();
//                    module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_UNKNOWN,
//                            "TARGET", targetName);
//                    e.printStackTrace();
//                }
//            }
//
//            if ()
//
//            if (sender.hasPermission("moda.module.nickname.set.others")) {
//                String finalNickname = nickname;
//                UuidFetcher.getOfflinePlayer(targetName)
//                        .onComplete(opt -> {
//                            if (targetName.equals(finalNickname)) {
//                                opt.ifPresent(target -> {
//                                    module.getStorage().removeNickname(target.getUniqueId())
//                                            .onComplete(var -> {
//                                                module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_REMOVE,
//                                                        "TARGET", target.getName());
//
//                                                if (target.isOnline() && !target.equals(sender)) {
//                                                    target.getPlayer().sendMessage(module.getLang().getMessage(NicknameMessage.UPDATE_NOTIFY,
//                                                            "NICKNAME", finalNickname));
//                                                }
//                                            })
//                                            .onException(e -> {
//                                                module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_UNKNOWN,
//                                                        "TARGET", targetName);
//                                                e.printStackTrace();
//                                            }).callAsync();
//                                });
//                            } else {
//                                if (!sender.hasPermission("moda.module.nickname.set.taken")) {
//                                    module.getStorage().nicknameExists(finalNickname)
//                                            .onComplete(nicknameExists -> {
//                                                if (nicknameExists) {
//                                                    module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_WARN_TAKEN,
//                                                            "NICKNAME", finalNickname);
//                                                } else {
//                                                    setNickname(sender, opt, finalNickname);
//                                                }
//                                            })
//                                            .onException(e -> {
//                                                module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_UNKNOWN,
//                                                        "TARGET", targetName);
//                                                e.printStackTrace();
//                                            }).callAsync();
//                                } else {
//                                    setNickname(sender, opt, finalNickname);
//                                }
//                            }
//                        })
//                        .onException(e -> {
//                            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_INVALID_TARGET,
//                                    "TARGET", targetName);
//                            e.printStackTrace();
//                        }).callAsync();
//                return true;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
//
//        List<String> completions = new ArrayList<>();
//        List<String> players = new ArrayList<>();
//
//        if (args.length == 1) {
//            if (
//                    sender instanceof Player
//                            && sender.hasPermission("moda.module.nickname.set.others")
//                            && args[0].length() > 2
//            ) {
//
//                StringUtil.copyPartialMatches(args[0], players, completions);
//
//                Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName);
//
//            }
//        }
//
//        return completions;
//    }
//
//    private boolean sendUsage(CommandSender sender) {
//        if (sender instanceof Player) {
//            module.getStorage().getNickname(((Player) sender).getUniqueId())
//                    .onComplete(nickname -> {
//
//                        if (nickname.isPresent()) {
//                            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_SELF_GET,
//                                    "NICKNAME", nickname.get());
//                        } else {
//                            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_SELF_GET,
//                                    "NICKNAME", sender.getName());
//                        }
//                    })
//                    .onException(e -> {
//
//                        e.printStackTrace();
//                    }).callAsync();
//        }
//        module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_DESCRIPTION);
//        module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_USAGE);
//        return true;
//    }
//
//    private String checkFormatting(CommandSender sender, String nickname) {
//
//        if (sender.hasPermission("moda.module.nickname.set.color")) {
//            nickname = Colors.parseColors(sender, nickname);
//        } else {
//            nickname = Colors.stripColors(nickname);
//            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_WARN_COLOR_COLOR);
//        }
//
//        if (sender.hasPermission("moda.module.nickname.set.format")) {
//            nickname = Colors.parseColors(sender, nickname);
//        } else {
//            nickname = Colors.stripColors(nickname);
//            module.getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_WARN_COLOR_FORMAT);
//        }
//
//        return nickname;
//
//    }
//
//    private boolean setNickname(CommandSender sender, OfflinePlayer target, String nickname) {
//
//    }
//}
