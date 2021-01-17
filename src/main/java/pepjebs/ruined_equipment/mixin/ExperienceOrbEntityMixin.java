package pepjebs.ruined_equipment.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.item.RuinedEquipmentItem;
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;
import pepjebs.ruined_equipment.utils.RuinedEquipmentUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {

    @Shadow
    private int pickupDelay;

    @Shadow
    private int amount;

    @Inject(method = "onPlayerCollision", at = @At("INVOKE"), cancellable = true)
    private void doRuinedRepairOnPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        if (!player.world.isClient) {
            if (RuinedEquipmentMod.CONFIG != null &&
                    !RuinedEquipmentMod.CONFIG.enableRuinedMendingRepair) return;
            if (this.pickupDelay == 0) {
                doPlayerHandRuinedMendingRepair(player, Hand.MAIN_HAND);
                doPlayerHandRuinedMendingRepair(player, Hand.OFF_HAND);
            }

        }
    }

    private void doPlayerHandRuinedMendingRepair(PlayerEntity player, Hand hand) {
        ItemStack handStack = player.getStackInHand(hand);
        if (handStack.getItem() instanceof RuinedEquipmentItem
                && RuinedEquipmentUtils.ruinedItemHasEnchantment(handStack, Enchantments.MENDING)) {
            player.experiencePickUpDelay = 2;
            player.sendPickup((ExperienceOrbEntity)(Object)this, 1);
            if (this.amount > 0) {
                Item vanillaItem = RuinedEquipmentItems.getVanillaItemMap().get(handStack.getItem());
                int repairAmount = getMendingRepairAmount(this.amount);
                ItemStack repaired = RuinedEquipmentUtils.generateRepairedItemForAnvilByDamage(
                        handStack, vanillaItem.getMaxDamage() - repairAmount, false);
                if (hand == Hand.MAIN_HAND) {
                    player.inventory.main.set(player.inventory.selectedSlot, repaired);
                } else {
                    player.inventory.offHand.set(0, repaired);
                }
                this.amount -= getMendingRepairCost(repairAmount);
            }
        }
    }

    @Shadow abstract int getMendingRepairCost(int repairAmount);

    @Shadow abstract int getMendingRepairAmount(int experienceAmount);
}
