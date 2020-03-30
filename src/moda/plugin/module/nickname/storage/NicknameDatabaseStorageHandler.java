package moda.plugin.module.nickname.storage;

import moda.plugin.moda.modules.Module;
import moda.plugin.moda.utils.BukkitFuture;
import moda.plugin.moda.utils.storage.DatabaseStorageHandler;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Collection;

public class NicknameDatabaseStorageHandler extends DatabaseStorageHandler implements NicknameStorageHandler {
    public NicknameDatabaseStorageHandler(Module<?> module) {
        super(module);
    }

    public void setup() throws SQLException {
        createTableIfNonexistent("module_nickname", "CREATE TABLE `module_nickname` ");
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
