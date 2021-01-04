package pepjebs.ruined_equipment.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import pepjebs.ruined_equipment.item.RuinedEquipmentItem;
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;

import java.util.ArrayList;

// I understand that this class is an abomination. But the lack of NBT Crafting is the real issue.
public class RuinedEquipmentSmithingEmpowerRecipe extends SmithingRecipe {

    public static final Item REPAIR_MATERIAL = Items.NETHERITE_SCRAP;
    public static final String RUINED_MAX_ENCHT_TAG = "IsUpgrading";

    public RuinedEquipmentSmithingEmpowerRecipe(Identifier id) {
        super(id, Ingredient.ofItems(RuinedEquipmentItems.RUINED_BOW), Ingredient.ofItems(REPAIR_MATERIAL), ItemStack.EMPTY);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
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
    public ItemStack craft(Inventory inv) {
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

    public static class Serializer implements RecipeSerializer<RuinedEquipmentSmithingEmpowerRecipe> {

        @Override
        public RuinedEquipmentSmithingEmpowerRecipe read(Identifier id, JsonObject json) {
            return new RuinedEquipmentSmithingEmpowerRecipe(id);
        }

        @Override
        public RuinedEquipmentSmithingEmpowerRecipe read(Identifier id, PacketByteBuf buf) {
            return new RuinedEquipmentSmithingEmpowerRecipe(id);
        }

        @Override
        public void write(PacketByteBuf buf, RuinedEquipmentSmithingEmpowerRecipe recipe) {}
    }
}
