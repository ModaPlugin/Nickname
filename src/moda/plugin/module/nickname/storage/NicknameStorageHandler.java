package moda.plugin.module.nickname.storage;

import moda.plugin.moda.module.storage.ModuleStorageHandler;
import moda.plugin.moda.util.BukkitFuture;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.Set;

public interface NicknameStorageHandler extends ModuleStorageHandler {

    default String getIdentifier(OfflinePlayer player, DataType dataType) {
        return player.getUniqueId() + "." + dataType.getIdentifier();
    }

    /**
     * gets the provided player's nickname.
     * @param player
     * @return the player's nickname
     */
    BukkitFuture<Optional<String>> getNickname(OfflinePlayer player);

    /**
     * sets the nickname for the provided player to the provided nickname.
     * @param player
     * @param nickname
     * @return boolean indicating whether it succeeded
     */
    BukkitFuture<Void> setNickname(OfflinePlayer player, String nickname);

    /**
     * removes the nickname from the provided player.
     * @param player
     * @return boolean indicating whether it succeeded
     */
    BukkitFuture<Void> removeNickname(OfflinePlayer player);

    /**
     * checks if a nickname already exists in the storage.
     * @param nickname
     * @return whether a nickname is used
     */
    BukkitFuture<Boolean> nicknameExists(String nickname);

    /**
     * gets all the Player that use the given nickname, may be empty.
     * @param nickname
     * @return a set of players that use the provided nickname
     */
    BukkitFuture<Set<OfflinePlayer>> getPlayersByNickname(String nickname);

//    /**
//     * gets all the player data as a HashMap
//     * @return a HashMap containing playerdata (uuid, data)
//     */
//    BukkitFuture<HashMap<String, String>> getAllPlayerData();

}
