package pepjebs.ruined_equipment.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.utils.RuinedEquipmentUtils;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    private LivingEntity livingEntity = ((LivingEntity) (Object) this);

    @Inject(method = "sendEquipmentBreakStatus", at = @At("RETURN"))
    private void onSendEquipmentBreakStatus(EquipmentSlot slot, CallbackInfo ci) {
        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) livingEntity;
            ItemStack breakingItemStack = serverPlayer.getEquippedStack(slot);
            boolean forceSet = slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND;
            if (RuinedEquipmentMod.CONFIG != null && !RuinedEquipmentMod.CONFIG.enableSetRuinedItemInHand) {
                forceSet = false;
            }
            RuinedEquipmentUtils.onSendEquipmentBreakStatusImpl(serverPlayer, breakingItemStack, forceSet);
        }
    }
}
