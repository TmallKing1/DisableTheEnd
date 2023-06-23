package top.pigest.disabletheend;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import top.pigest.disabletheend.command.*;
import top.pigest.disabletheend.config.ConfigManager;
import top.pigest.disabletheend.config.DisableTheEndConfig;
import top.pigest.disabletheend.util.Mute;

public class DisableTheEnd implements DedicatedServerModInitializer {
    public static final String MODID = "disabletheend";
    public static MinecraftServer minecraftServer;
    private static DisableTheEndConfig CONFIG;

    @Override
    public void onInitializeServer() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            minecraftServer = server;
            ConfigManager.DISABLE_THE_END_CONFIG_FOLDER = server.getRunDirectory().toPath().resolve("config").resolve(DisableTheEnd.MODID);
            Mute.MUTELIST_FILE = server.getRunDirectory().toPath().resolve("muted-players.json");
            setConfig(ConfigManager.getDisableTheEndConfig());
            Mute.muteList = Mute.getMuteList();
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if(environment.dedicated) {
                ForceSpawnCommand.register(dispatcher);
                MuteCommand.register(dispatcher);
                UnmuteCommand.register(dispatcher);
                ScoreboardCopyCommand.register(dispatcher);
                DTECommand.register(dispatcher);
            }
        });
    }

    public static DisableTheEndConfig getConfig() {
        return CONFIG;
    }

    public static void setConfig(DisableTheEndConfig config) {
        DisableTheEnd.CONFIG = config;
    }
}
