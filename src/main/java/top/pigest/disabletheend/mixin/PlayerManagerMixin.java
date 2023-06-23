package top.pigest.disabletheend.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.pigest.disabletheend.DisableTheEnd;
import top.pigest.disabletheend.config.DisableTheEndConfig;
import top.pigest.disabletheend.config.ForceSpawnConfig;

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
}
