package cx.moda.module.nickname;

import cx.moda.moda.module.LangFile;
import cx.moda.moda.module.command.ModuleCommandExecutor;
import cx.moda.moda.util.BukkitFuture;
import cx.moda.moda.util.UuidFetcher;
import cx.moda.module.nickname.storage.NicknameStorageHandler;
import cx.moda.module.nickname.utils.Colors;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class NicknameCommand extends ModuleCommandExecutor<Nickname> implements TabCompleter {

    public NicknameCommand(Nickname module) {
        super(module);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // send own nickname
        if (args.length == 0) {
            if (sender instanceof Player) {
                sendNickname((Player) sender)
                        .onComplete(var -> {
                            sendUsage(sender);
                        }).callAsync();
                return true;
            }
            sendUsage(sender);
            return true;
        }

        // too many arguments
        else if (args.length > 2) {
            tooManyArguments(sender);
            return true;
        }

        BukkitFuture<OfflinePlayer> future = getTarget(sender, args);

        future.onComplete(target -> {
                    if (checkPermission(sender, target)) {

                        String nickname = args[args.length -1];

                        setNickname(sender, target, nickname, sender == target ? sender.getName() : args[0]);
                    } else {
                        if (sender == target) {
                            getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_ERROR_NO_PERMISSION_SELF,
                                    "PERMISSION", "moda.module.nickname.set");
                        } else {
                            try {
                                getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_ERROR_NO_PERMISSION_OTHER,
                                        "TARGET", getStorage().getNickname(target.getUniqueId()).callBlocking(),
                                        "PERMISSION", "moda.module.nickname.set.others");
                            } catch (Exception e) {
                                getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_ERROR_UNKNOWN);
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .onException(e -> {
                    errorUnknown(sender);
                    e.printStackTrace();
                }).callAsync();
        return true;
    }

    /**
     *
     * @param sender
     * @param args
     * @return
     */
    private BukkitFuture<OfflinePlayer> getTarget(CommandSender sender, String[] args) {

        // no provided target, target = sender
        if (args.length == 1) {
            if (sender instanceof Player) {
                return new BukkitFuture<>(() -> (OfflinePlayer) sender);
            } else {
                invalidSender(sender);
                return new BukkitFuture<>(() -> null);
            }
        }

        // get target from provided name
        else {
            return new BukkitFuture<OfflinePlayer>(() -> {

                Set<OfflinePlayer> players = getStorage().getPlayersByNickname(args[0]).callBlocking();

                if (players.size() < 1) {
                    invalidTarget(sender, args[0]);
                    return null;
                } else if (players.size() > 1) {
                    return UuidFetcher.getOfflinePlayer(args[0]).callBlocking().orElse(null);
                } else {
                    return players.iterator().next();
                }
            });
        }
    }

    private boolean checkPermission(CommandSender sender, OfflinePlayer target) {
        if (sender == target) {
            return sender.hasPermission("moda.module.nickname.set");
        } else {
            return sender.hasPermission("moda.module.nickname.set.others");
        }
    }

    // TODO dont check dupe nickname when player changing own nickname

    private void setNickname(CommandSender sender, OfflinePlayer target, String nickname, String targetName) {
        boolean nicknameExists = true;

        try {
            nicknameExists = getStorage().nicknameExists(nickname).callBlocking();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO optimize nested if statements
        if (nicknameExists) {
            if (target != sender) {
                getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_WARN_TAKEN,
                        "NICKNAME", nickname);
                if (!sender.hasPermission("moda.module.nickname.set.taken")) {
                    return;
                }
            }
        }

        if (nickname.contains(ChatColor.COLOR_CHAR + "")) nickname = Colors.strip(nickname);

        if (sender.hasPermission("moda.module.nickname.set.color")) {
            nickname = Colors.parseColors(sender, nickname);
        } else {
            nickname = Colors.stripColors(nickname);
            getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_WARN_COLOR_COLOR);
        }

        if (sender.hasPermission("moda.module.nickname.set.format")) {
            nickname = Colors.parseColors(sender, nickname);
        } else {
            nickname = Colors.stripFormatting(nickname);
            getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_WARN_COLOR_FORMAT);
        }

        if (targetName.equals(nickname)) {
            String finalNickname = nickname;
            getStorage().removeNickname(target.getUniqueId())
                    .onComplete(var -> {
                        if (target == sender) {
                            getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_SELF_REMOVE,
                                    "NICKNAME", finalNickname);
                        }
                        else {
                            getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_REMOVE,
                                    "TARGET", target.getName(),
                                    "NICKNAME", finalNickname);
                            if (target.isOnline()) {
                                getLang().send((Player) target, NicknameMessage.UPDATE_REMOVE);
                            }
                        }
                    })
                    .onException(e -> {
                        getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_ERROR_UNKNOWN);
                        e.printStackTrace();
                    }).callAsync();
        } else {
            String finalNickname = nickname;
            getStorage().setNickname(target.getUniqueId(), nickname)
                    .onComplete(var -> {
                        if (target == sender) {
                            getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_SELF_UPDATE,
                                    "NICKNAME", finalNickname);
                        }
                        else {
                            getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_UPDATE,
                                    "TARGET", target.getName(),
                                    "NICKNAME", finalNickname);
                            if (target.isOnline()) {
                                getLang().send((Player) target, NicknameMessage.UPDATE_NOTIFY,
                                        "NICKNAME", finalNickname);
                            }
                        }
                    })
                    .onException(e -> {
                        getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_ERROR_UNKNOWN);
                        e.printStackTrace();
                    }).callAsync();
        }
    }

    private BukkitFuture<Void> sendNickname(Player sender) {
        return new BukkitFuture<>(() -> {

            Optional<String> opt = getStorage().getNickname(sender.getUniqueId()).callBlocking();
            String nickname = sender.getDisplayName();

            if (opt.isPresent()) {
                nickname = opt.get();
            }

            getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_SELF_GET,
                    "NICKNAME", nickname);
            return null;
        });
    }

    private void invalidSender(CommandSender sender) {
        getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_ERROR_INVALID_SENDER);
        sendUsage(sender);
    }

    private void invalidTarget(CommandSender sender, String targetName) {
        getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_OTHER_ERROR_INVALID_TARGET,
                "TARGET", targetName);
        sendUsage(sender);
    }

    private void tooManyArguments(CommandSender sender) {
        getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_ERROR_TOO_MANY_ARGUMENTS);
        sendUsage(sender);
    }

    private void sendUsage(CommandSender sender) {
        getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_DESCRIPTION);
        getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_USAGE);
    }

    private void errorUnknown(CommandSender sender) {
        getLang().send(sender, NicknameMessage.COMMAND_NICKNAME_ERROR_UNKNOWN);
    }

    private LangFile getLang() {
        return getModule().getLang();
    }

    private NicknameStorageHandler getStorage() {
        return getModule().getStorage();
    }

    // TODO setting to only do tab complete after a given amount of characters has been typed (tabcomplete-min-char)
    // TODO make tab completer not list duplicate usernames.
    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {

        List<String> players = new ArrayList<>();
        List<String> completions = new ArrayList<>();

        if (s.hasPermission("moda.module.nickname.set.others")) {
            players.addAll(Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName).collect(Collectors.toList()));

//            getStorage().getStoredNicknames()
//                    .onComplete(players::addAll)
//                    .onException(Throwable::printStackTrace)
//                    .callAsync();

            try {
                players.addAll(getStorage().getStoredNicknames().callBlocking().stream().map(Colors::strip).collect(Collectors.toList()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], players, completions);
        }
        if (args.length == 2) {
            completions.clear();
            try {
                completions.addAll(getStorage().getPlayersByNickname(args[0]).callBlocking().stream().map(p -> {
                    return Colors.strip(p.getName());
                }).collect(Collectors.toList()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        Collections.sort(completions);
        return completions;
    }
}
