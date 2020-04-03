package moda.plugin.module.nickname.storage;

import moda.plugin.moda.modules.Module;
import moda.plugin.moda.utils.BukkitFuture;
import moda.plugin.moda.utils.storage.FileStorageHandler;
import moda.plugin.moda.utils.storage.JsonStorageHandler;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;
import moda.plugin.moda.utils.storage.YamlStorageHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class NicknameFileStorageHandler extends JsonStorageHandler implements NicknameStorageHandler {

    public NicknameFileStorageHandler(Module<? extends ModuleStorageHandler> module) throws IOException {
        super(module);
    }

    @Override
    public void save() {

    }

    public BukkitFuture<Boolean> hasNickname(Player player) {
        return null;
    }

    public BukkitFuture<Optional<String>> getNickname(Player player) {
        return null;
    }

    public BukkitFuture<Boolean> setNickname(Player player, String nickname) {
        return null;
    }

    @Override
    public BukkitFuture<Boolean> nicknameExists(String nickname) {
        return null;
    }

    @Override
    public BukkitFuture<Set<OfflinePlayer>> getPlayersByNickname(String nickname) {
        return null;
    }
}
