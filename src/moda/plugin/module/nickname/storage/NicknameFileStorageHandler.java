package moda.plugin.module.nickname.storage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import moda.plugin.moda.modules.Module;
import moda.plugin.moda.utils.BukkitFuture;
import moda.plugin.moda.utils.storage.JsonStorageHandler;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.derkades.derkutils.caching.Cache;

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
    public BukkitFuture<Boolean> setNickname(OfflinePlayer player, String nickname) {
        return new BukkitFuture<>(() -> {

            UUID uuid = player.getUniqueId();

            // if nickname = username, remove nickname
            if (nickname.equals(player.getName())) {
                removeProperty(uuid, PROPERTY_NICKNAME);
            }

            setProperty(uuid, PROPERTY_NICKNAME, nickname);

            return true;

        });
    }

    @Override
    public BukkitFuture<Boolean> nicknameExists(String nickname) {
        return new BukkitFuture<>(() -> {

            for (UUID uuid : getUuids()) {

                Optional<String> storedNickname = getProperty(uuid, PROPERTY_NICKNAME);

                if (storedNickname.isPresent() && storedNickname.equals(nickname)) {
                    return true;
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

//    @Override
//    public BukkitFuture<HashMap<String, String>> getAllPlayerData() {
//
//        return new BukkitFuture<>(() -> {
//
//            HashMap<String, String> playerdata = new HashMap<>();
//
//            JsonObject obj = getJson().getAsJsonObject();
//            Set<Map.Entry<String, JsonElement>> players = obj.entrySet();
//
//            for (Map.Entry<String, JsonElement> player : players) {
//                playerdata.put(player.toString(), player.getValue().getAsJsonObject().get("nickname").getAsString());
//            }
//
//            return playerdata;
//        });
//    }
}
