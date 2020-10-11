package cx.mia.moda.nickname.storage;

import moda.plugin.moda.module.storage.ModuleStorageHandler;
import moda.plugin.moda.util.BukkitFuture;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface NicknameStorageHandler extends ModuleStorageHandler {

//    default String getIdentifier(OfflinePlayer player, DataType dataType) {
//        return player.getUniqueId() + "." + dataType.getIdentifier();
//    }

    /**
     * gets the provided player's nickname.
     * @param uuid
     * @return the player's nickname
     */
    BukkitFuture<Optional<String>> getNickname(UUID uuid);

    /**
     * sets the nickname for the provided player to the provided nickname.
     * @param uuid
     * @param nickname
     * @return boolean indicating whether it succeeded
     */
    BukkitFuture<Void> setNickname(UUID uuid, String nickname);

    /**
     * removes the nickname from the provided player.
     * @param uuid
     * @return boolean indicating whether it succeeded
     */
    BukkitFuture<Void> removeNickname(UUID uuid);

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

    /**
     * gets all stored nicknames (nicknames that are currently set), may be empty
     * @return a set of stored nicknames
     */

    BukkitFuture<Set<String>> getStoredNicknames();

//    /**
//     * gets all the player data as a HashMap
//     * @return a HashMap containing playerdata (uuid, data)
//     */
//    BukkitFuture<HashMap<String, String>> getAllPlayerData();

}
