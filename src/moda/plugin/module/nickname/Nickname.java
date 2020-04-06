package moda.plugin.module.nickname;

import moda.plugin.moda.module.IMessage;
import moda.plugin.moda.module.LangFile;
import moda.plugin.moda.module.Module;
import moda.plugin.moda.module.command.ModuleCommandBuilder;
import moda.plugin.moda.module.storage.DatabaseStorageHandler;
import moda.plugin.moda.module.storage.FileStorageHandler;
import moda.plugin.moda.module.storage.StorageMigrator;
import moda.plugin.moda.placeholder.ModaPlaceholderAPI;
import moda.plugin.module.nickname.storage.NicknameDatabaseStorageHandler;
import moda.plugin.module.nickname.storage.NicknameFileStorageHandler;
import moda.plugin.module.nickname.storage.NicknameStorageHandler;
import moda.plugin.module.nickname.storage.NicknameStorageMigrator;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class Nickname extends Module<NicknameStorageHandler> {

    public static final int NICKNAME_MAX_LENGTH = 128;

    public String getName() {
        return "Nickname";
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
    public IMessage[] getMessages() {
        return NicknameMessage.values();
    }

    @Override
    public void onEnable() throws InvalidConfigurationException {
        if (getConfig().getInt("max-length", NICKNAME_MAX_LENGTH) > 128) {
            throw new InvalidConfigurationException("Maximum nickname length cannot exceed 128 characters. " + getConfig().getInt("nickname.max-length") + " > " + NICKNAME_MAX_LENGTH);
        }
        ModaPlaceholderAPI.addPlaceholder("NICKNAME", player -> this.getStorage().getNickname(player));
        this.registerCommand(
                new ModuleCommandBuilder("nickname")
                        .withExecutor(new NicknameCommand(this))
                        .withTabCompleter(new NicknameCommand(this))
                        .withDescription("opens a GUI to set a nickname.")
                        .withPermission("moda.module.nickname.set")
                        .withUsage("/nickname")
                        .withAlias("nick")
                        .create());
    }
}
