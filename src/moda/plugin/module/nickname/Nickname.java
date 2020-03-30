package moda.plugin.module.nickname;

import moda.plugin.moda.modules.Module;
import moda.plugin.moda.utils.placeholders.ModaPlaceholderAPI;
import moda.plugin.moda.utils.storage.DatabaseStorageHandler;
import moda.plugin.moda.utils.storage.FileStorageHandler;
import moda.plugin.module.nickname.storage.NicknameDatabaseStorageHandler;
import moda.plugin.module.nickname.storage.NicknameFileStorageHandler;
import moda.plugin.module.nickname.storage.NicknameStorageHandler;

public class Nickname extends Module<NicknameStorageHandler> {

    /* TODO
    - nickname storage methods
    - caching
    - persistence save method for onDisable()
    - permissions
    - nickname command
    - nickname GUI
     */

    public String getName() {
        return null;
    }

    @Override
    public DatabaseStorageHandler getDatabaseStorageHandler() {
        return new NicknameDatabaseStorageHandler(this);
    }

    @Override
    public FileStorageHandler getFileStorageHandler() {
        return new NicknameFileStorageHandler(this);
    }

    @Override
    public void onEnable() {
        ModaPlaceholderAPI.addPlaceholder("NICKNAME", player -> this.getStorage().getNickname(player));
        getLogger().info("Nickname module enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Nickname module disabled.");
    }
}
