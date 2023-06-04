package top.pigest.disabletheend.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.pigest.disabletheend.DisableTheEnd;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setWorld(Lnet/minecraft/server/world/ServerWorld;)V"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        if(DisableTheEnd.getConfig().isEnabled()) {
            player.setPos(DisableTheEnd.getConfig().getSpawnPos().getX(),DisableTheEnd.getConfig().getSpawnPos().getY(),DisableTheEnd.getConfig().getSpawnPos().getZ());
            player.setPitch(DisableTheEnd.getConfig().getSpawnRotation().getX());
            player.setYaw(DisableTheEnd.getConfig().getSpawnRotation().getY());
        }
    }
}
