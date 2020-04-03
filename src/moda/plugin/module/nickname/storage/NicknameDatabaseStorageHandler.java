package moda.plugin.module.nickname.storage;

import moda.plugin.moda.modules.Module;
import moda.plugin.moda.utils.BukkitFuture;
import moda.plugin.moda.utils.storage.DatabaseStorageHandler;
import moda.plugin.module.nickname.Nickname;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.derkades.derkutils.caching.Cache;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class NicknameDatabaseStorageHandler extends DatabaseStorageHandler implements NicknameStorageHandler {

    public NicknameDatabaseStorageHandler(Module<?> module) {
        super(module);
    }

    public void setup() throws SQLException {
        createTableIfNonexistent("module_nickname", "CREATE TABLE `module_nickname` (`uuid` VARCHAR(100) NOT NULL, `nickname` VARCHAR(" + Nickname.NICKNAME_MAX_LENGTH + ") NOT NULL, PRIMARY KEY (`uuid`)) ENGINE = InnoDB");
    }

    public BukkitFuture<Optional<String>> getNickname(Player player) {
        return new BukkitFuture<Optional<String>>(() -> {

            // get nickname from cache
            Object cache = Cache.getCachedObject("nick." + player.getUniqueId());

            // if nickname doesn't exist in cache
            if (cache == null) {

                // get from database
                final PreparedStatement statement = this.db.prepareStatement("SELECT nickname FROM module_nickname WHERE uuid=?", player.getUniqueId());
                final ResultSet result = statement.executeQuery();

                // if exists in database
                if (result.next()) {
                    Optional<String> nickname;
                    nickname = Optional.of(result.getString(0));
                    Cache.addCachedObject("nick." + player.getUniqueId(), nickname); // add to cache
                    return nickname; // return nickname
                } else {
                    Cache.addCachedObject("nick." + player.getUniqueId(), Optional.of(player.getDisplayName())); // add empty optional to cache
                    return Optional.of(player.getDisplayName()); // return empty optional
                }
            }

            // if nickname does exist in cache
            else {
                return (Optional<String>) cache; // return nickname
            }
        });
    }

    public BukkitFuture<Boolean> setNickname(Player player, String nickname) {
        return new BukkitFuture<Boolean>(() -> {

            // if nickname = username, remove nickname
            if (nickname.equals(player.getName())) {
                Cache.removeCachedObject("nick." + player.getUniqueId());
            }

            // update cache
            Cache.addCachedObject("nick." + player.getUniqueId(), Optional.of(nickname));

            // save to database
            final PreparedStatement statement = this.db.prepareStatement("INSERT INTO module_nickname (uuid, nickname) VALUES (?, ?) ON DUPLICATE KEY UPDATE nickname=?", player.getUniqueId(), nickname, nickname);
            statement.execute();

            return true;
        });
    }

    @Override
    public BukkitFuture<Boolean> nicknameExists(String nickname) {
        return new BukkitFuture<Boolean>(() -> {

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
        return new BukkitFuture<Set<OfflinePlayer>>(() -> {


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
}
