package top.pigest.disabletheend.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.pigest.disabletheend.DisableTheEnd;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {

    /**
     * @author xiaozhu_zhizui
     * @reason Disable The End
     */
    @Inject(method = "onEntityCollision",at = @At("HEAD"), cancellable = true)
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if(DisableTheEnd.getConfig().isDisableEndPortal()) {
            ci.cancel();
        }
    }
}
