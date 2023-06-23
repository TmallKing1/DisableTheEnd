package top.pigest.disabletheend.mixin;

import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.pigest.disabletheend.DisableTheEnd;

import java.util.List;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow private PlayerManager playerManager;

    @Shadow public abstract int getMaxPlayerCount();

    @Shadow public abstract boolean hideOnlinePlayers();

    @Shadow @Final private Random random;

    @Inject(method = "createMetadataPlayers",at = @At(value = "HEAD"), cancellable = true)
    private void injected(CallbackInfoReturnable<ServerMetadata.Players> cir) {
        if(DisableTheEnd.getConfig().isAntiAnonymous()) {
            List<ServerPlayerEntity> list = this.playerManager.getPlayerList();
            int i = this.getMaxPlayerCount();
            if (this.hideOnlinePlayers()) {
                cir.setReturnValue(new ServerMetadata.Players(i, list.size(), List.of()));
            } else {
                int j = Math.min(list.size(), 12);
                ObjectArrayList<GameProfile> objectArrayList = new ObjectArrayList<>(j);
                int k = MathHelper.nextInt(this.random, 0, list.size() - j);
                for(int l = 0; l < j; ++l) {
                    ServerPlayerEntity serverPlayerEntity = list.get(k + l);
                    objectArrayList.add(serverPlayerEntity.getGameProfile());
                }
                Util.shuffle(objectArrayList, this.random);
                cir.setReturnValue(new ServerMetadata.Players(i, list.size(), objectArrayList));
            }
        }
    }
}
