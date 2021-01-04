package pepjebs.ruined_equipment.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.*;
import net.minecraft.tag.ItemTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.ruined_equipment.item.RuinedEquipmentItem;
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;
import pepjebs.ruined_equipment.recipe.RuinedEquipmentSmithingEmpowerRecipe;
import pepjebs.ruined_equipment.utils.RuinedEquipmentUtils;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    private static final double REPAIR_MODIFIER = 0.18;

    @Shadow
    @Final
    private Property levelCost;

    @Shadow
    private int repairItemUsage;


    public AnvilScreenHandlerMixin(
            @Nullable ScreenHandlerType<?> type,
            int syncId,
            PlayerInventory playerInventory,
            ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "updateResult", at = @At(value = "RETURN"))
    private void updateRuinedRepair(CallbackInfo ci) {
        ItemStack leftStack = this.input.getStack(0).copy();
        ItemStack rightStack = this.input.getStack(1).copy();
        if (leftStack.getItem() instanceof RuinedEquipmentItem) {
            RuinedEquipmentItem ruinedItem = (RuinedEquipmentItem) leftStack.getItem();
            boolean isMaxEnch = leftStack.getTag() != null &&
                    leftStack.getTag().contains(RuinedEquipmentSmithingEmpowerRecipe.RUINED_MAX_ENCHT_TAG)
                    && leftStack.getTag().getBoolean(RuinedEquipmentSmithingEmpowerRecipe.RUINED_MAX_ENCHT_TAG);
            Item vanillaItem = RuinedEquipmentItems.VANILLA_ITEM_MAP.get(ruinedItem);
            int vanillaMaxDamage = vanillaItem.getMaxDamage();
            // Check right stack for matching repair item
            Ingredient repairIngredient = null;
            if(vanillaItem instanceof ArmorItem) {
                repairIngredient = ((ArmorItem) vanillaItem).getMaterial().getRepairIngredient();
            } else if (vanillaItem instanceof ToolItem) {
                repairIngredient = ((ToolItem) vanillaItem).getMaterial().getRepairIngredient();
            } else if (vanillaItem == Items.SHIELD) {
                repairIngredient = Ingredient.fromTag(ItemTags.PLANKS);
            }
            if (repairIngredient != null && repairIngredient.test(rightStack)) {
                double targetFraction = 1.0 - (rightStack.getCount() * REPAIR_MODIFIER);
                ItemStack repaired = RuinedEquipmentUtils.generateRepairedItemForAnvilByFraction(
                        leftStack,
                        Math.min(targetFraction, 1.0),
                        isMaxEnch);
                this.output.setStack(0, repaired);
                this.levelCost.set(RuinedEquipmentUtils.generateRepairLevelCost(repaired));
                this.repairItemUsage = 6;
                this.sendContentUpdates();
                return;
            }
            // Check right stack for corresponding vanilla item
            if (rightStack.getItem() == vanillaItem) {
                int targetDamage = rightStack.getDamage() - (int)(REPAIR_MODIFIER * rightStack.getMaxDamage());
                ItemStack repaired = RuinedEquipmentUtils.generateRepairedItemForAnvilByDamage(
                        leftStack,
                        Math.min(targetDamage, vanillaMaxDamage),
                        isMaxEnch);
                this.output.setStack(0, repaired);
                this.levelCost.set(RuinedEquipmentUtils.generateRepairLevelCost(repaired));
                this.repairItemUsage = 0;
                this.sendContentUpdates();
            }
        }
    }
}
