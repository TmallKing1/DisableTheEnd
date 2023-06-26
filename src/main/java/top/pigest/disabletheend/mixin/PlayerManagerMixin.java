package top.pigest.disabletheend.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.pigest.disabletheend.DisableTheEnd;
import top.pigest.disabletheend.config.DisableTheEndConfig;
import top.pigest.disabletheend.config.ForceSpawnConfig;
import top.pigest.disabletheend.util.Mute;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setServerWorld(Lnet/minecraft/server/world/ServerWorld;)V"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        DisableTheEndConfig dConfig = DisableTheEnd.getConfig();
        ForceSpawnConfig config = dConfig.getForceSpawnConfig();
        if(config.isEnabled()) {
            player.setPos(config.getSpawnPos().getX(),config.getSpawnPos().getY(),config.getSpawnPos().getZ());
            player.setPitch(config.getSpawnRotation().getX());
            player.setYaw(config.getSpawnRotation().getY());
        }
    }

    @Inject(method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/network/message/MessageType$Parameters;)V", at= @At(value = "HEAD"), cancellable = true)
    private void broadcast(SignedMessage message, ServerCommandSource source, MessageType.Parameters params, CallbackInfo ci) throws CommandSyntaxException {
        Registry<MessageType> registry = source.getRegistryManager().get(RegistryKeys.MESSAGE_TYPE);
        if(Mute.isMutedWithExpiry(source.getPlayerOrThrow().getGameProfile()) && (registry.get(MessageType.SAY_COMMAND) == params.type() || registry.get(MessageType.EMOTE_COMMAND) == params.type())) {
            Mute.sendMuteMessage(source.getPlayerOrThrow());
            ci.cancel();
        }
    }
}
