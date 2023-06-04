package top.pigest.disabletheend;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import top.pigest.disabletheend.command.ForceSpawnCommand;
import top.pigest.disabletheend.config.ConfigManager;
import top.pigest.disabletheend.config.DisableTheEndConfig;

public class DisableTheEnd implements DedicatedServerModInitializer {
    public static final String MODID = "disabletheend";
    public static MinecraftServer minecraftServer;
    private static DisableTheEndConfig CONFIG;

    @Override
    public void onInitializeServer() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            minecraftServer = server;
            ConfigManager.CONFIG_FOLDER = server.getRunDirectory().toPath().resolve("config").resolve(DisableTheEnd.MODID);
            CONFIG = ConfigManager.getDisableTheEndConfig();
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if(environment.dedicated) {
                ForceSpawnCommand.register(dispatcher);
            }
        });
    }

    public static DisableTheEndConfig getConfig() {
        return CONFIG;
    }
}
