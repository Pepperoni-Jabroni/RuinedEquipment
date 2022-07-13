package pepjebs.ruined_equipment.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.utils.RuinedEquipmentUtils;

import java.util.ArrayList;

// I understand that this class is an abomination. But the lack of NBT Crafting is the real issue.
public class RuinedEquipmentSmithingEmpowerRecipe extends SmithingRecipe {

    public static final String RUINED_MAX_ENCHT_TAG = "IsUpgrading";

    public RuinedEquipmentSmithingEmpowerRecipe(Identifier id) {
        super(
                id,
                Ingredient.ofItems(Registry.ITEM.get(new Identifier(RuinedEquipmentMod.MOD_ID, "ruined_bow"))),
                Ingredient.ofItems(Items.NETHERITE_SCRAP),
                ItemStack.EMPTY
        );
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        if (RuinedEquipmentMod.CONFIG != null && !RuinedEquipmentMod.CONFIG.enableSmithingRuinedEmpowered)
            return false;
        Item empowermentItem = RuinedEquipmentUtils.getEmpowermentApplicationItem();
        ArrayList<ItemStack> craftingStacks = new ArrayList<>();
        for(int i = 0; i < inv.size(); i++) {
            if (!inv.getStack(i).isEmpty()) {
                craftingStacks.add(inv.getStack(i));
            }
        }
        if (craftingStacks.size() == 2) {
            ItemStack otherStack = ItemStack.EMPTY;
            if (craftingStacks.get(0).getItem() == empowermentItem) {
                otherStack = craftingStacks.get(1).copy();
            } else if (craftingStacks.get(1).getItem() == empowermentItem) {
                otherStack = craftingStacks.get(0).copy();
            }
            if (otherStack == ItemStack.EMPTY) return false;
            return RuinedEquipmentUtils.isRuinedItem(otherStack.getItem());
        }
        return false;
    }

    @Override
    public ItemStack craft(Inventory inv) {
        ItemStack ruinedItem = ItemStack.EMPTY;
        for(int i = 0; i < inv.size(); i++) {
            if (RuinedEquipmentUtils.isRuinedItem(inv.getStack(i).getItem())) {
                ruinedItem = inv.getStack(i).copy();
            }
        }
        NbtCompound tag = ruinedItem.getNbt();
        if (tag == null) tag = new NbtCompound();
        if (tag.contains(RUINED_MAX_ENCHT_TAG)) tag.remove(RUINED_MAX_ENCHT_TAG);
        tag.putBoolean(RUINED_MAX_ENCHT_TAG, true);
        ruinedItem.setNbt(tag);
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
