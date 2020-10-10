package moda.plugin.module.nickname.storage;

import moda.plugin.moda.module.storage.StorageMigrator;

@Deprecated
public class NicknameStorageMigrator implements StorageMigrator<NicknameStorageHandler> {


    @Override
    public void migrate(NicknameStorageHandler from, NicknameStorageHandler to) throws Exception {
//        from.getAllPlayerData().get().forEach((uuid, nickname) -> to.setNickname(Bukkit.getOfflinePlayer(UUID.fromString(uuid)), nickname));
    }
}
