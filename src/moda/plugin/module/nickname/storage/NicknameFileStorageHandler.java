package moda.plugin.module.nickname.storage;

import moda.plugin.moda.modules.Module;
import moda.plugin.moda.utils.BukkitFuture;
import moda.plugin.moda.utils.storage.FileStorageHandler;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;
import org.bukkit.entity.Player;

import java.util.Collection;

public class NicknameFileStorageHandler extends FileStorageHandler implements NicknameStorageHandler {
    public NicknameFileStorageHandler(Module<? extends ModuleStorageHandler> module) {
        super(module);
    }

    public BukkitFuture<Boolean> hasNickname(Player player) {
        return null;
    }

    public BukkitFuture<String> getNickname(Player player) {
        return null;
    }

    public BukkitFuture<Void> setNickname(Player player, String nickname) {
        return null;
    }

    public BukkitFuture<Collection<Player>> getPlayersByNickname(String nickname) {
        return null;
    }
}
