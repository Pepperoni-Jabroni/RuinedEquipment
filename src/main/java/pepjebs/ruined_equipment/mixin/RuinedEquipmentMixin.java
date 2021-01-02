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
public class RuinedEquipmentMixin {

    private LivingEntity livingEntity = ((LivingEntity) (Object) this);

    @Inject(method = "sendEquipmentBreakStatus", at = @At("RETURN"))
    private void onSendEquipmentBreakStatus(EquipmentSlot slot, CallbackInfo ci) {
        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) livingEntity;
            ItemStack breakingItemStack;
            switch(slot) {
                case MAINHAND:
                    breakingItemStack = serverPlayer.getMainHandStack();
                    RuinedEquipmentUtils.onSendEquipmentBreakStatusImpl(serverPlayer, breakingItemStack, true);
                    break;
                case OFFHAND:
                    breakingItemStack = serverPlayer.getOffHandStack();
                    RuinedEquipmentUtils.onSendEquipmentBreakStatusImpl(serverPlayer, breakingItemStack, true);
                    break;
                case HEAD:
                    breakingItemStack = serverPlayer.getEquippedStack(EquipmentSlot.HEAD);
                    RuinedEquipmentUtils.onSendEquipmentBreakStatusImpl(serverPlayer, breakingItemStack, false);
                    break;
                default:
                    RuinedEquipmentMod.LOGGER.warn("No valid slot found in 'onSendEquipmentBreakStatus'.");
            }
        }
    }
}
