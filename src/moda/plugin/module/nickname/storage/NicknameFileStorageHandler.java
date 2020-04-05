package moda.plugin.module.nickname.storage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import moda.plugin.moda.modules.Module;
import moda.plugin.moda.utils.BukkitFuture;
import moda.plugin.moda.utils.storage.JsonStorageHandler;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.derkades.derkutils.caching.Cache;

import java.io.IOException;
import java.util.*;

public class NicknameFileStorageHandler extends JsonStorageHandler implements NicknameStorageHandler {

    public NicknameFileStorageHandler(Module<? extends ModuleStorageHandler> module) throws IOException {
        super(module);
    }

    public BukkitFuture<Optional<String>> getNickname(OfflinePlayer player) {
        return new BukkitFuture<>(() -> {

            String uuid = player.getUniqueId().toString();
            Optional<String> nickname;

            // get nickname from cache
            Optional<Optional<String>> cache = Cache.get("nickname." + player.getUniqueId());

            // if nickname doesn't exist in cache
            if (!cache.isPresent()) {

                // if player exists in json
                if (getJson().has(uuid)) {
                    JsonObject playerData = getJson().get(uuid).getAsJsonObject();

                    // if player has nickname
                    if (playerData.has("nickname")) {
                        nickname = Optional.of(playerData.get("nickname").getAsString());
                        Cache.set("nickname." + player.getUniqueId(), nickname);
                        return nickname;
                    }
                }

                nickname = Optional.empty();

                // add nickname to cache
                Cache.set("nickname." + player.getUniqueId(), nickname);
                return nickname;
            }

            nickname = cache.get();
            return nickname;
        });
    }

    @Override
    public BukkitFuture<Boolean> setNickname(OfflinePlayer player, String nickname) {
        return new BukkitFuture<>(() -> {

            String uuid = player.getUniqueId().toString();

            // if nickname = username, remove nickname
            if (nickname.equals(player.getName())) {
                Cache.remove(uuid + ".nickname");
            }

            // update cache


            return false;
        });
    }

    @Override
    public BukkitFuture<Boolean> nicknameExists(String nickname) {
        return null;
    }

    @Override
    public BukkitFuture<Set<OfflinePlayer>> getPlayersByNickname(String nickname) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BukkitFuture<HashMap<String, String>> getAllPlayerData() {

        return new BukkitFuture<>(() -> {

            HashMap<String, String> playerdata = new HashMap<>();

            JsonObject obj = getJson().getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> players = obj.entrySet();

            for (Map.Entry<String, JsonElement> player : players) {
                playerdata.put(player.toString(), player.getValue().getAsJsonObject().get("nickname").getAsString());
            }

            return playerdata;
        });
    }
}
