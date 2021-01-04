package pepjebs.ruined_equipment.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import pepjebs.ruined_equipment.item.RuinedEquipmentItem;

import java.util.ArrayList;

public class RuinedEquipmentSetUpgrading extends SpecialCraftingRecipe {

    public static final Item REPAIR_MATERIAL = Items.NETHERITE_SCRAP;
    public static final String RUINED_MAX_ENCHT_TAG = "IsUpgrading";

    public RuinedEquipmentSetUpgrading(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        ArrayList<ItemStack> craftingStacks = new ArrayList<>();
        for(int i = 0; i < inv.size(); i++) {
            if (!inv.getStack(i).isEmpty()) {
                craftingStacks.add(inv.getStack(i));
            }
        }
        if (craftingStacks.size() == 2) {
            ItemStack otherStack = ItemStack.EMPTY;
            if (craftingStacks.get(0).getItem() == REPAIR_MATERIAL) {
                otherStack = craftingStacks.get(1).copy();
            } else if (craftingStacks.get(1).getItem() == REPAIR_MATERIAL) {
                otherStack = craftingStacks.get(0).copy();
            }
            if (otherStack == ItemStack.EMPTY) return false;
            return !(!(otherStack.getItem() instanceof RuinedEquipmentItem) || (otherStack.getTag() != null
                    && otherStack.getTag().contains(RUINED_MAX_ENCHT_TAG)
                    && otherStack.getTag().getBoolean(RUINED_MAX_ENCHT_TAG)));
        }
        return false;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack ruinedItem = ItemStack.EMPTY;
        for(int i = 0; i < inv.size(); i++) {
            if (inv.getStack(i).getItem() instanceof RuinedEquipmentItem) {
                ruinedItem = inv.getStack(i).copy();
            }
        }
        CompoundTag tag = ruinedItem.getTag();
        if (tag == null) tag = new CompoundTag();
        if (tag.contains(RUINED_MAX_ENCHT_TAG)) tag.remove(RUINED_MAX_ENCHT_TAG);
        tag.putBoolean(RUINED_MAX_ENCHT_TAG, true);
        ruinedItem.setTag(tag);
        return ruinedItem;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}
