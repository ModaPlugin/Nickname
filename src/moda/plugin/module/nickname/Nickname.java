package moda.plugin.module.nickname;

import moda.plugin.moda.modules.Module;
import moda.plugin.moda.utils.placeholders.ModaPlaceholderAPI;
import moda.plugin.moda.utils.storage.DatabaseStorageHandler;
import moda.plugin.moda.utils.storage.FileStorageHandler;
import moda.plugin.moda.utils.storage.StorageMigrator;
import moda.plugin.module.nickname.commands.NicknameCommand;
import moda.plugin.module.nickname.storage.NicknameDatabaseStorageHandler;
import moda.plugin.module.nickname.storage.NicknameFileStorageHandler;
import moda.plugin.module.nickname.storage.NicknameStorageHandler;
import moda.plugin.module.nickname.storage.NicknameStorageMigrator;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class Nickname extends Module<NicknameStorageHandler> {

    /* TODO
    - nickname storage methods
    - caching
    - persistence save method for onDisable()
    - permissions
    - nickname command
    - nickname GUI
     */

    public static final int NICKNAME_MAX_LENGTH = 128;

    public String getName() {
        return null;
    }

    @Override
    public DatabaseStorageHandler getDatabaseStorageHandler() {
        return new NicknameDatabaseStorageHandler(this);
    }

    @Override
    public FileStorageHandler getFileStorageHandler() throws IOException {
        return new NicknameFileStorageHandler(this);
    }

    @Override
    public StorageMigrator<NicknameStorageHandler> getStorageMigrator() {
        return new NicknameStorageMigrator();
    }

    @Override
    public void onEnable() throws InvalidConfigurationException {
        if (getConfig().getInt("nickname.max-length", NICKNAME_MAX_LENGTH) > 128) {
            throw new InvalidConfigurationException("Maximum nickname length cannot exceed 128 characters. " + getConfig().getInt("nickname.max-length") + " > " + NICKNAME_MAX_LENGTH);
        }
        ModaPlaceholderAPI.addPlaceholder("NICKNAME", player -> this.getStorage().getNickname(player));
        getLogger().info("Nickname module enabled.");
        this.registerCommand(new NicknameCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("Nickname module disabled.");
    }
}
