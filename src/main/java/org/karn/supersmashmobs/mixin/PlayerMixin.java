package org.karn.supersmashmobs.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.karn.supersmashmobs.api.HudApi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerMixin {
    private final PlayerEntity player = (PlayerEntity) (Object) this;
    @Inject(method = "collideWithEntity", at = @At("HEAD"), cancellable = true)
    private void SSM$Pickup(Entity entity, CallbackInfo ci) {
        if(entity instanceof ItemEntity && !player.getWorld().isClient){
            ItemEntity itemEntity = (ItemEntity) entity;
            if(itemEntity.getStack().isOf(Items.NETHER_STAR)){
                var glow = new StatusEffectInstance(StatusEffects.GLOWING, 60, 0, false, false);
                player.addStatusEffect(glow);
                player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.MASTER, 3.0F, 1.0F);
                ((HudApi) player).setFinalSmash(true);
                entity.kill();
                ci.cancel();
            }
        }
    }
}
