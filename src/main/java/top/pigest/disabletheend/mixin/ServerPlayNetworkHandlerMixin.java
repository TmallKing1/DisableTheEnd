package top.pigest.disabletheend.mixin;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.pigest.disabletheend.util.Mute;
import top.pigest.disabletheend.util.TimeUtil;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onChatMessage",at = @At(value = "HEAD"), cancellable = true)
    private void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        if(Mute.isMutedWithExpiry(this.player.getGameProfile())) {
            player.sendMessage(Text.literal("你已被禁言")
                    .append(Mute.getMuteReason(player.getGameProfile()) == null ? Text.empty() : Text.literal("，原因：" + Mute.getMuteReason(player.getGameProfile())))
                    .append(Mute.getMuteRemainingSeconds(player.getGameProfile()) == -1 ? Text.empty() : Text.literal("，将在" + TimeUtil.formatTime(Mute.getMuteRemainingSeconds(player.getGameProfile())) + "后解禁")));
            ci.cancel();
        }
    }
}
