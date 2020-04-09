package moda.plugin.module.nickname;

import moda.plugin.moda.module.IMessage;
import moda.plugin.moda.module.Module;
import moda.plugin.moda.module.command.ModuleCommandBuilder;
import moda.plugin.moda.module.storage.DatabaseStorageHandler;
import moda.plugin.moda.module.storage.FileStorageHandler;
import moda.plugin.moda.placeholder.ModaPlaceholderAPI;
import moda.plugin.module.nickname.storage.NicknameDatabaseStorageHandler;
import moda.plugin.module.nickname.storage.NicknameFileStorageHandler;
import moda.plugin.module.nickname.storage.NicknameStorageHandler;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import xyz.derkades.derkutils.bukkit.Colors;

import java.io.IOException;
import java.util.Optional;

public class Nickname extends Module<NicknameStorageHandler> {

    public static final int NICKNAME_MAX_LENGTH = 128;

    public static Nickname instance;

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

        instance = this;

        if (getConfig().getInt("max-length", NICKNAME_MAX_LENGTH) > 128) {
            throw new InvalidConfigurationException("Maximum nickname length cannot exceed 128 characters. " + getConfig().getInt("nickname.max-length") + " > " + NICKNAME_MAX_LENGTH);
        }
        ModaPlaceholderAPI.addPlaceholder("NICKNAME", player -> {
            String nickname;

            // TODO async papi handling
            try {
                Optional<String> opt = this.getStorage().getNickname(player.getUniqueId()).callBlocking();
                nickname = player.getDisplayName();

                if (opt.isPresent()) {
                    nickname = Colors.parseColors(opt.get());
                }

            } catch (Exception e) {
                nickname = ChatColor.RED + player.getDisplayName();
                e.printStackTrace();
            }

            return nickname;
        });
        this.registerCommand(
                new ModuleCommandBuilder("nickname")
                        .withExecutor(new NicknameCommand(this))
                        .withTabCompleter(new NicknameCommand(this))
                        .withDescription("opens a GUI to set a nickname.")
                        .withUsage("/nickname")
                        .withAlias("nick")
                        .create());
    }
}
