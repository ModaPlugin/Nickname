package moda.plugin.module.nickname.storage;

import moda.plugin.moda.utils.BukkitFuture;
import moda.plugin.moda.utils.storage.ModuleStorageHandler;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface NicknameStorageHandler extends ModuleStorageHandler {

    public BukkitFuture<Boolean> hasNickname(Player player);

    public BukkitFuture<String> getNickname(Player player);

    public BukkitFuture<Void> setNickname(Player player, String nickname);

    public BukkitFuture<Collection<Player>> getPlayersByNickname(String nickname);

}
