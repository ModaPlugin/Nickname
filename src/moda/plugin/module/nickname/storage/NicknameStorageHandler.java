package moda.plugin.module.nickname.storage;

import moda.plugin.moda.utils.BukkitFuture;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public interface NicknameStorageHandler extends ModuleStorageHandler {

    default String getIdentifier(OfflinePlayer player, DataType dataType) {
        return player.getUniqueId() + "." + dataType.getIdentifier();
    }

    /**
     * gets the provided player's nickname.
     * this check the cache first, if it doesn't exist in cache, it will update the cache from the storage.
     * @param player
     * @return the player's nickname
     */
    BukkitFuture<Optional<String>> getNickname(OfflinePlayer player);

    /**
     * sets the nickname for the provided player to the provided nickname.
     * this updates both the cache and the storage.
     * @param player
     * @param nickname
     */
    BukkitFuture<Boolean> setNickname(OfflinePlayer player, String nickname);

    /**
     * checks if a nickname already exists in the storage.
     * @param nickname
     * @return whether a nickname is used
     */
    BukkitFuture<Boolean> nicknameExists(String nickname);

    /**
     * gets all the Player that use the given nickname, maybe be empty.
     * @param nickname
     * @return a set of players that use the provided nickname
     */
    BukkitFuture<Set<OfflinePlayer>> getPlayersByNickname(String nickname);

    /**
     * gets all the player data as a HashMap
     * @return a HashMap containing playerdata (uuid, data)
     */
    BukkitFuture<HashMap<String, String>> getAllPlayerData();

}
