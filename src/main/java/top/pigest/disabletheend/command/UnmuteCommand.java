package top.pigest.disabletheend.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import top.pigest.disabletheend.util.Mute;

import java.util.Collection;

public class UnmuteCommand {
    private static final SimpleCommandExceptionType NOT_MUTED_EXCEPTION = new SimpleCommandExceptionType(Text.literal("指定的玩家均未被禁言"));
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("unmute").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("targets", GameProfileArgumentType.gameProfile()).executes(context -> execute(context.getSource(), GameProfileArgumentType.getProfileArgument(context, "targets")))));
    }

    private static int execute(ServerCommandSource source, Collection<GameProfile> target) throws CommandSyntaxException {
        int k = 0;
        for(GameProfile gameProfile: target) {
            if(Mute.isMutedWithExpiry(gameProfile)) {
                Mute.unMute(gameProfile);
                k++;
            }
        }
        if(k == 0) {
            throw NOT_MUTED_EXCEPTION.create();
        }
        MutableText text;
        if(k == 1) {
            text = Text.literal("已取消禁言" + target.iterator().next().getName());
        } else {
            text = Text.literal("已取消禁言" + k + "个玩家");
        }
        source.sendFeedback(() -> text, true);
        return k;
    }
}
