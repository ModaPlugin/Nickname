package moda.plugin.module.nickname.storage;

import moda.plugin.moda.modules.Module;
import moda.plugin.moda.utils.BukkitFuture;
import moda.plugin.moda.utils.storage.DatabaseStorageHandler;
import moda.plugin.module.nickname.Nickname;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import xyz.derkades.derkutils.caching.Cache;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class NicknameDatabaseStorageHandler extends DatabaseStorageHandler implements NicknameStorageHandler {

    public NicknameDatabaseStorageHandler(Module<?> module) {
        super(module);
    }

    public void setup() throws SQLException {
        createTableIfNonexistent("module_nickname", "CREATE TABLE `module_nickname` (`uuid` VARCHAR(100) NOT NULL, `nickname` VARCHAR(" + Nickname.NICKNAME_MAX_LENGTH + ") NOT NULL, PRIMARY KEY (`uuid`)) ENGINE = InnoDB");
    }

    public BukkitFuture<Optional<String>> getNickname(OfflinePlayer player) {
        return new BukkitFuture<>(() -> {

            // if player has never joined the server
            if (!player.hasPlayedBefore()) {
                return Optional.empty(); // return empty optional
            }

            // get nickname from cache
            Optional<Optional<String>> cache = Cache.get(getIdentifier(player, DataType.NICKNAME));

            // if nickname doesn't exist in cache
            if (!cache.isPresent()) {

                // get from database
                final PreparedStatement statement = this.db.prepareStatement("SELECT nickname FROM module_nickname WHERE uuid=?", player.getUniqueId());
                final ResultSet result = statement.executeQuery();

                // if exists in database
                if (result.next()) {
                    Optional<String> nickname;
                    nickname = Optional.of(result.getString(0));
                    Cache.set(getIdentifier(player, DataType.NICKNAME), nickname); // add to cache
                    return nickname; // return nickname
                } else {
                    Cache.set(getIdentifier(player, DataType.NICKNAME), Optional.ofNullable(player.getName())); // update cache to the player's DisplayName
                    return Optional.ofNullable(player.getName()); // return the player's DisplayName
                }
            }

            // if nickname does exist in cache
            else {
                return cache.get(); // return nickname
            }
        });
    }

    public BukkitFuture<Boolean> setNickname(OfflinePlayer player, String nickname) {
        return new BukkitFuture<>(() -> {

            String uuid = player.getUniqueId().toString();

            // if nickname = username, remove nickname
            if (nickname.equals(player.getName())) {
                Cache.remove(getIdentifier(player, DataType.NICKNAME));
                this.db.prepareStatement("UPDATE module_nickname SET nickname=NULL WHERE uuid=? AND nickname IS NOT NULL", uuid);
            }

            // update cache
            Cache.set("nickname." + uuid, Optional.of(nickname));

            // save to database
            final PreparedStatement statement = this.db.prepareStatement("INSERT INTO module_nickname (uuid, nickname) VALUES (?, ?) ON DUPLICATE KEY UPDATE nickname=?", uuid, nickname, nickname);
            statement.execute();

            return true;
        });
    }

    @Override
    public BukkitFuture<Boolean> nicknameExists(String nickname) {
        return new BukkitFuture<>(() -> {

            boolean nicknameExists = false;

            // get from database at nickname
            final PreparedStatement statement = this.db.prepareStatement("SELECT FROM module_nickname WHERE nickname=?", nickname);
            final ResultSet result = statement.executeQuery();

            // if exists in database
            if (result.next()) {
                nicknameExists = true;
            }

            return nicknameExists;
        });
    }

    public BukkitFuture<Set<OfflinePlayer>> getPlayersByNickname(String nickname) {
        return new BukkitFuture<>(() -> {


            // get uuid's that use @nickname
            final PreparedStatement statement = this.db.prepareStatement("SELECT FROM module_nickname WHERE nickname=?", nickname);
            final ResultSet result = statement.executeQuery();

            Set<OfflinePlayer> players = new HashSet<>();

            // add all players to set
            while (result.next()) {
                players.add(Bukkit.getOfflinePlayer(UUID.fromString(result.getString(0))));
            }

            // return set
            return players;
        });
    }

    @Override
    public BukkitFuture<HashMap<String, String>> getAllPlayerData() {
        return new BukkitFuture<>(() -> {

            HashMap<String, String> nicknames = new HashMap<>();

            final PreparedStatement statement = this.db.prepareStatement("SELECT FROM module_nickname");
            final ResultSet result = statement.executeQuery();

            while (result.next()) {
                String uuidString = result.getString(0);;
                String nickname = result.getString(1);;

                nicknames.put(uuidString, nickname);
            }

            return nicknames;
        });
    }
}
