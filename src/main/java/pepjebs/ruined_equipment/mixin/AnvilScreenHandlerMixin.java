package pepjebs.ruined_equipment.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.*;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.item.RuinedEquipmentItem;
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;
import pepjebs.ruined_equipment.recipe.RuinedEquipmentCraftRepair;

import java.util.Map;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    private static final double REPAIR_MODIFIER = 0.18;

    @Shadow
    @Final
    private Property levelCost;

    @Shadow
    private int repairItemUsage;


    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "updateResult", at = @At(value = "RETURN"))
    private void onUpdateResult(CallbackInfo ci) {
        ItemStack leftStack = this.input.getStack(0);
        ItemStack rightStack = this.input.getStack(1);
        if (leftStack.getItem() instanceof RuinedEquipmentItem) {
            RuinedEquipmentItem ruinedItem = (RuinedEquipmentItem) leftStack.getItem();
            Item vanillaItem = RuinedEquipmentItems.VANILLA_ITEM_MAP.get(ruinedItem);
            // Check right stack for matching repair item
            Ingredient repairIngredient = null;
            if(vanillaItem instanceof ArmorItem) {
                repairIngredient = ((ArmorItem) vanillaItem).getMaterial().getRepairIngredient();
            } else if (vanillaItem instanceof ToolItem) {
                repairIngredient = ((ToolItem) vanillaItem).getMaterial().getRepairIngredient();
            }
            if (repairIngredient != null && repairIngredient.test(rightStack)) {
                ItemStack repaired = new ItemStack(vanillaItem);
                int targetDamage = (int) ((1.0 - (rightStack.getCount() * REPAIR_MODIFIER)) * repaired.getMaxDamage());
                repaired.setDamage(targetDamage);
                if (leftStack.hasCustomName()) {
                    repaired.setCustomName(leftStack.getName());
                }
                this.output.setStack(0, repaired);
                this.levelCost.set(15);
                this.repairItemUsage = 5;
                this.sendContentUpdates();
                return;
            }
            // Check right stack for corresponding vanilla item
            if (rightStack.getItem() == vanillaItem) {

            }
        }
    }
}
