package top.pigest.disabletheend.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.RotationArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import top.pigest.disabletheend.DisableTheEnd;

public class ForceSpawnCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder<ServerCommandSource>)((LiteralArgumentBuilder) CommandManager.literal("forcespawn").requires((source) -> source.hasPermissionLevel(2)))
                .then((CommandManager.literal("enable").executes((context) -> executeEnable(context.getSource()))))
                .then((CommandManager.literal("disable").executes((context) -> executeDisable(context.getSource()))))
                .then(CommandManager.literal("setpos").then(CommandManager.argument("position", Vec3ArgumentType.vec3()).executes(context -> setPos(context.getSource(), Vec3ArgumentType.getVec3(context, "position")))))
                .then(CommandManager.literal("setrotation").then(CommandManager.argument("rotation", RotationArgumentType.rotation()).executes(context -> setRotation(context.getSource(), RotationArgumentType.getRotation(context, "rotation"))))));
    }


    private static int executeEnable(ServerCommandSource source) {
        DisableTheEnd.getConfig().setEnabled(true);
        source.sendFeedback(Text.literal("已启用强制进服出生点"), true);
        return 1;
    }

    private static int executeDisable(ServerCommandSource source) {
        DisableTheEnd.getConfig().setEnabled(false);
        source.sendFeedback(Text.literal("已禁用强制进服出生点"), true);
        return 0;
    }

    private static int setPos(ServerCommandSource source, Vec3d vec3d) {
        DisableTheEnd.getConfig().setSpawnPos(vec3d);
        source.sendFeedback(Text.literal("强制进服出生点位置已设定为 " + vec3d), true);
        return 1;
    }

    private static int setRotation(ServerCommandSource source, PosArgument posArgument) {
        Vec2f vec2f = posArgument.toAbsoluteRotation(source);
        DisableTheEnd.getConfig().setSpawnRotation(vec2f);
        source.sendFeedback(Text.literal("强制进服出生点朝向已设定为 (" + vec2f.x + "," + vec2f.y + ")"), true);
        return 1;
    }
}
