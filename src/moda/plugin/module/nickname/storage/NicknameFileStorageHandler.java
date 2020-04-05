package moda.plugin.module.nickname.storage;

import moda.plugin.moda.module.Module;
import moda.plugin.moda.module.storage.JsonStorageHandler;
import moda.plugin.moda.module.storage.ModuleStorageHandler;
import moda.plugin.moda.util.BukkitFuture;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import xyz.derkades.derkutils.bukkit.Colors;

import java.io.IOException;
import java.util.*;

public class NicknameFileStorageHandler extends JsonStorageHandler implements NicknameStorageHandler {

    private static final String PROPERTY_NICKNAME = "nickname";

    public NicknameFileStorageHandler(Module<? extends ModuleStorageHandler> module) throws IOException {
        super(module);
    }

    public BukkitFuture<Optional<String>> getNickname(OfflinePlayer player) {
        return new BukkitFuture<>(() -> {

            Optional<String> nickname;
            UUID uuid = player.getUniqueId();

            nickname = getProperty(uuid, PROPERTY_NICKNAME);

            return nickname;

        });
    }

    @Override
    public BukkitFuture<Void> setNickname(OfflinePlayer player, String nickname) {
        return new BukkitFuture<>(() -> {

            UUID uuid = player.getUniqueId();

            setProperty(uuid, PROPERTY_NICKNAME, nickname);

            return null;
        });
    }

    @Override
    public BukkitFuture<Void> removeNickname(OfflinePlayer player) {
        return new BukkitFuture<>(() -> {

            UUID uuid = player.getUniqueId();

            removeProperty(uuid, PROPERTY_NICKNAME);

            return null;
        });
    }

    @Override
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

    @Override
    public BukkitFuture<Set<OfflinePlayer>> getPlayersByNickname(String nickname) {
        return new BukkitFuture<>(() -> {

            Set<OfflinePlayer> players = new HashSet<>();

            for (UUID uuid : getUuids()) {

                Optional<String> storedNickname = getProperty(uuid, PROPERTY_NICKNAME);

                if (storedNickname.isPresent() && storedNickname.equals(nickname)) {
                    players.add(Bukkit.getOfflinePlayer(uuid));
                }
            }
            return players;
        });
    }
}
