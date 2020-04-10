package moda.plugin.module.nickname.storage;

import moda.plugin.moda.module.Module;
import moda.plugin.moda.module.storage.DatabaseStorageHandler;
import moda.plugin.moda.util.BukkitFuture;
import moda.plugin.module.nickname.Nickname;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import xyz.derkades.derkutils.bukkit.Colors;
import java.sql.SQLException;
import java.util.*;

public class NicknameDatabaseStorageHandler extends DatabaseStorageHandler implements NicknameStorageHandler {

    private static final String PROPERTY_NICKNAME = "nickname";

    public NicknameDatabaseStorageHandler(Module<?> module) {
        super(module);
    }

    public void setup() throws SQLException {
        createTableIfNonexistent("module_nickname", "CREATE TABLE `module_nickname` (`uuid` VARCHAR(100) NOT NULL, `nickname` VARCHAR(" + Nickname.NICKNAME_MAX_LENGTH + ") NOT NULL, PRIMARY KEY (`uuid`)) ENGINE = InnoDB");
    }

    public BukkitFuture<Optional<String>> getNickname(UUID uuid) {
        return new BukkitFuture<>(() -> {

            Optional<String> nickname;

            nickname = getProperty(uuid, PROPERTY_NICKNAME);

            return nickname;

        });
    }

    @Override
    public BukkitFuture<Void> setNickname(UUID uuid, String nickname) {
        return new BukkitFuture<>(() -> {

            setProperty(uuid, PROPERTY_NICKNAME, nickname);

            return null;
        });
    }

    @Override
    public BukkitFuture<Void> removeNickname(UUID uuid) {
        return new BukkitFuture<>(() -> {

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
