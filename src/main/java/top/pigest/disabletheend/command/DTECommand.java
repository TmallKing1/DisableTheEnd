package top.pigest.disabletheend.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import top.pigest.disabletheend.DisableTheEnd;
import top.pigest.disabletheend.config.ConfigManager;

public class DTECommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("dte").requires(source -> source.hasPermissionLevel(4)).then(CommandManager.literal("reload").executes(context -> executeReload(context.getSource())));
        dispatcher.register(literalArgumentBuilder);
    }

    private static int executeReload(ServerCommandSource source) {
        DisableTheEnd.setConfig(ConfigManager.getDisableTheEndConfig());
        source.sendFeedback(() -> Text.literal("重载 Disable The End 配置文件完成！"), true);
        return 1;
    }
}
