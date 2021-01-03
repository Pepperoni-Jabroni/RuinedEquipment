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
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;
import pepjebs.ruined_equipment.recipe.RuinedEquipmentCraftRepair;

import java.util.Map;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    @Shadow
    @Final
    private Property levelCost;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "updateResult", at = @At(value = "HEAD"))
    private void onUpdateResult(CallbackInfo ci) {
        ItemStack leftStack = this.input.getStack(0);
        ItemStack rightStack = this.input.getStack(1);
        for (Map.Entry<Item, Item> item : RuinedEquipmentItems.VANILLA_ITEM_MAP.entrySet()) {
            if (leftStack.getItem() == item.getKey()) {
                // Check right stack for matching repair item
                if(item.getValue() instanceof ArmorItem) {
                    ArmorItem armor = (ArmorItem) item.getValue();
                } else if (item.getValue() instanceof ToolItem) {
                    ToolItem tool = (ToolItem) item.getValue();
                    Ingredient ing = tool.getMaterial().getRepairIngredient();
                    if (ing.test(rightStack)) {
                        ItemStack repaired = new ItemStack(item.getValue());
                        int targetDamage = (int) ((1.0 - (rightStack.getCount() * RuinedEquipmentCraftRepair.REPAIR_MODIFIER)) * repaired.getMaxDamage());
                        repaired.setDamage(targetDamage);
                        if (leftStack.hasCustomName()) {
                            repaired.setCustomName(leftStack.getName());
                        }
                        this.output.setStack(0, repaired);
                        this.sendContentUpdates();
                    }
                }
                // Check right stack for corresponding vanilla item
                if (rightStack.getItem() == item.getValue()) {

                }
                // Discontinue loop
                break;
            }
        }
    }
}
