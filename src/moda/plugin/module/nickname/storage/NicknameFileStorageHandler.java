package moda.plugin.module.nickname.storage;

import moda.plugin.moda.modules.Module;
import moda.plugin.moda.utils.BukkitFuture;
import moda.plugin.moda.utils.storage.FileStorageHandler;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;

public class NicknameFileStorageHandler extends FileStorageHandler implements NicknameStorageHandler {
    public NicknameFileStorageHandler(Module<? extends ModuleStorageHandler> module) {
        super(module);
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

    public BukkitFuture<Collection<OfflinePlayer>> getPlayersByNickname(String nickname) {
        return null;
    }
}
