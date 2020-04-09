package moda.plugin.module.nickname.storage;

import moda.plugin.moda.module.Module;
import moda.plugin.moda.module.storage.ModuleStorageHandler;
import moda.plugin.moda.module.storage.YamlStorageHandler;
import moda.plugin.moda.util.BukkitFuture;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import xyz.derkades.derkutils.bukkit.Colors;

import java.io.IOException;
import java.util.*;

public class NicknameFileStorageHandler extends YamlStorageHandler implements NicknameStorageHandler {

    private static final String PROPERTY_NICKNAME = "nickname";

    public NicknameFileStorageHandler(Module<? extends ModuleStorageHandler> module) throws IOException {
        super(module);
    }

    public BukkitFuture<Optional<String>> getNickname(UUID uuid) {
        return new BukkitFuture<>(() -> {

            Optional<String> nickname;

            nickname = getProperty(uuid, PROPERTY_NICKNAME);

            return nickname;

        });
    }

    public BukkitFuture<Void> setNickname(UUID uuid, String nickname) {
        return new BukkitFuture<>(() -> {

            setProperty(uuid, PROPERTY_NICKNAME, nickname);

            return null;
        });
    }

    public BukkitFuture<Void> removeNickname(UUID uuid) {
        return new BukkitFuture<>(() -> {

            removeProperty(uuid, PROPERTY_NICKNAME);

            return null;
        });
    }

    public BukkitFuture<Boolean> nicknameExists(String nickname) {
        return new BukkitFuture<>(() -> {

            for (UUID uuid : getUuids()) {

                Optional<String> storedNickname = getProperty(uuid, PROPERTY_NICKNAME);

                if (storedNickname.isPresent()) {
                    if (Colors.stripColors(storedNickname.get()).equals(Colors.stripColors(nickname))) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

    public BukkitFuture<Set<OfflinePlayer>> getPlayersByNickname(String nickname) {
        return new BukkitFuture<>(() -> {
            String finalNickname = Colors.stripColors(nickname);

            Set<OfflinePlayer> players = new HashSet<>();

            for (UUID uuid : getUuids()) {

                Optional<String> opt = getProperty(uuid, PROPERTY_NICKNAME);

                if (opt.isPresent()) {
                    String storedNickname = Colors.stripColors(opt.get());
                    if (storedNickname.equals(finalNickname)) {
                        players.add(Bukkit.getOfflinePlayer(uuid));
                    }
                }
            }

            for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                if (player.getName().equals(finalNickname)) {
                    players.add(player);
                }
            }

            return players;
        });
    }
}
