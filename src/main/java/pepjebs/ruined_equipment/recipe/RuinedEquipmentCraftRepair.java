package pepjebs.ruined_equipment.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.item.RuinedEquipmentItem;
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;
import pepjebs.ruined_equipment.utils.RuinedEquipmentUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RuinedEquipmentCraftRepair extends SpecialCraftingRecipe {

    // Default bonus is 0.05
    public static final double REPAIR_MODIFIER = 0.07;

    public RuinedEquipmentCraftRepair(Identifier id) {
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
            Set<Item> items = craftingStacks.stream().map(ItemStack::getItem).collect(Collectors.toSet());
            if (items.size() == 1) {
                for (Map.Entry<Item, Item> itemMap : RuinedEquipmentItems.getVanillaItemMap().entrySet()) {
                    if (items.contains(itemMap.getKey())) return true;
                }
                if (items.toArray()[0] == RuinedEquipmentMod.RUINED_ASHES_ITEM) {
                    Identifier one = RuinedEquipmentUtils.getItemKeyIdFromItemStack(craftingStacks.get(0));
                    Identifier two = RuinedEquipmentUtils.getItemKeyIdFromItemStack(craftingStacks.get(1));
                    return one != null && one.equals(two);
                }
            } else {
                for (Map.Entry<Item, Item> itemMap : RuinedEquipmentItems.getVanillaItemMap().entrySet()) {
                    if (items.contains(itemMap.getKey()) && items.contains(itemMap.getValue())) return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack vanillaItem = ItemStack.EMPTY;
        ItemStack ruinedItem = ItemStack.EMPTY;
        ItemStack t;
        for(int i = 0; i < inv.size(); i++) {
            t = inv.getStack(i);
            if (!t.isEmpty()) {
                if (t.getItem() instanceof RuinedEquipmentItem) {
                    ruinedItem = t;
                } else {
                    vanillaItem = t;
                }
            }
        }
        ItemStack newStack;
        int targetDamage;
        if (vanillaItem.getItem() == RuinedEquipmentMod.RUINED_ASHES_ITEM) {
            Item item = Registry.ITEM.get(RuinedEquipmentUtils.getItemKeyIdFromItemStack(vanillaItem));
            newStack = new ItemStack(item);
            targetDamage = (int) ((1.0 - REPAIR_MODIFIER) * newStack.getMaxDamage());
        } else if (vanillaItem != ItemStack.EMPTY) {
            newStack = vanillaItem.copy();
            if (newStack.getNbt() != null) newStack.getNbt().remove("Enchantments");
            targetDamage =
                    newStack.getDamage() - (int)(REPAIR_MODIFIER * newStack.getMaxDamage());
        } else {
            newStack =
                    new ItemStack(RuinedEquipmentItems.getVanillaItemMap().get(ruinedItem.getItem()));
            targetDamage = (int) ((1.0 - REPAIR_MODIFIER) * newStack.getMaxDamage());
        }
        newStack.setDamage(Math.max(targetDamage, 0));
        return newStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RuinedEquipmentMod.RUINED_CRAFT_REPAIR_RECIPE;
    }
}
