package pepjebs.ruined_equipment.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.utils.RuinedEquipmentUtils;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    private static final double REPAIR_MODIFIER = 0.25;

    @Shadow
    @Final
    private Property levelCost;

    @Shadow
    private int repairItemUsage;

    @Shadow
    private String newItemName;


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
        if (RuinedEquipmentUtils.isRuinedItem(leftStack.getItem())) {
            if (RuinedEquipmentMod.CONFIG != null &&
                    !RuinedEquipmentMod.CONFIG.enableAnvilRuinedRepair) return;
            Item vanillaItem = RuinedEquipmentUtils.getRepairItemForItemStack(leftStack);
            Identifier vanillaItemId = Registries.ITEM.getId(vanillaItem);
            int vanillaMaxDamage = vanillaItem.getMaxDamage() - 1;
            // Check right stack for matching repair item
            Ingredient repairIngredient = null;
            var ashesRepairItems = RuinedEquipmentUtils.getParsedRuinedItemsAshesRepairItems();
            if (vanillaItemId.getNamespace().compareTo("minecraft") != 0 && ashesRepairItems != null) {
                Identifier repairingItemId = ashesRepairItems.get(vanillaItemId);
                if (repairingItemId == null) return;
                repairIngredient = Ingredient.ofItems(Registries.ITEM.get(repairingItemId));
            } else if(vanillaItem instanceof ArmorItem) {
                repairIngredient = ((ArmorItem) vanillaItem).getMaterial().getRepairIngredient();
            } else if (vanillaItem instanceof ToolItem) {
                repairIngredient = ((ToolItem) vanillaItem).getMaterial().getRepairIngredient();
            } else if (vanillaItem == Items.SHIELD) {
                repairIngredient = Ingredient.fromTag(ItemTags.PLANKS);
            }

            ItemStack repaired = ItemStack.EMPTY;
            int maxLevel = 4;
            if (repairIngredient != null && repairIngredient.test(rightStack)) {
                double targetFraction = 1.0 - (rightStack.getCount() * REPAIR_MODIFIER);
                repaired = RuinedEquipmentUtils.generateRepairedItemForAnvilByFraction(
                        leftStack,
                        Math.min(targetFraction, 1.0));
                this.repairItemUsage = 4;
            } else if (rightStack.getItem() == vanillaItem) {
                // Check right stack for corresponding vanilla item
                int targetDamage = rightStack.getDamage() - (int)(REPAIR_MODIFIER * rightStack.getMaxDamage());
                repaired = RuinedEquipmentUtils.generateRepairedItemForAnvilByDamage(
                        leftStack,
                        Math.min(targetDamage, vanillaMaxDamage));
                maxLevel = 2;
                this.repairItemUsage = 0;
            }
            // Set the output
            if (!repaired.isEmpty()) {
                int levelCost = RuinedEquipmentUtils.generateRepairLevelCost(repaired, maxLevel);
                if (this.newItemName.compareTo(leftStack.getName().getString()) != 0) {
                    if (StringUtils.isBlank(this.newItemName)) {
                        repaired.removeCustomName();
                    } else {
                        repaired.setCustomName(Text.literal(this.newItemName));
                        levelCost++;
                    }
                }
                this.levelCost.set(levelCost);
            }
            this.output.setStack(0, repaired);
            this.sendContentUpdates();
        }
    }
}
