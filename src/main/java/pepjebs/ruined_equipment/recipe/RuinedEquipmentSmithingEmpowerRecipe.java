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
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.utils.RuinedEquipmentUtils;

import java.util.ArrayList;

// I understand that this class is an abomination. But the lack of NBT Crafting is the real issue.
public class RuinedEquipmentSmithingEmpowerRecipe implements SmithingRecipe {

    public static final String RUINED_MAX_ENCHT_TAG = "IsUpgrading";

    private final Identifier id;
    final Ingredient template;
    final Ingredient base;
    final Ingredient addition;

    public RuinedEquipmentSmithingEmpowerRecipe() {
        this.id = new Identifier(RuinedEquipmentMod.MOD_ID, "ruined_empowerment");
        this.template = Ingredient.ofStacks(Registries.ITEM.stream()
                .filter(i -> Registries.ITEM.getId(i).getNamespace().compareTo(RuinedEquipmentMod.MOD_ID) == 0)
                .map(ItemStack::new));
        this.base = Ingredient.ofItems(Items.NETHERITE_SCRAP);
        this.addition = Ingredient.ofItems(Items.GOLD_INGOT);
    }


    @Override
    public boolean matches(Inventory inv, World world) {
        if (RuinedEquipmentMod.CONFIG != null && !RuinedEquipmentMod.CONFIG.enableSmithingRuinedEmpowered) {
            RuinedEquipmentMod.LOGGER.info("RuinedEquipmentSmithingEmpowerRecipe off");
            return false;
        }
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
            if (otherStack == ItemStack.EMPTY) {
                RuinedEquipmentMod.LOGGER.info("RuinedEquipmentSmithingEmpowerRecipe empty otherStack");
                return false;
            }
            RuinedEquipmentMod.LOGGER.info("RuinedEquipmentSmithingEmpowerRecipe isRuinedItem "
                    +RuinedEquipmentUtils.isRuinedItem(otherStack.getItem()));
            return RuinedEquipmentUtils.isRuinedItem(otherStack.getItem());
        }
        return false;
    }

    @Override
    public ItemStack craft(Inventory inv, DynamicRegistryManager registryManager) {
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
        RuinedEquipmentMod.LOGGER.info("RuinedEquipmentSmithingEmpowerRecipe craft "+ruinedItem.getItem());
        return ruinedItem;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return null;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return new Serializer();
    }

    @Override
    public boolean testTemplate(ItemStack stack) {
        return this.template.test(stack);
    }

    @Override
    public boolean testBase(ItemStack stack) {
        return this.base.test(stack);
    }

    @Override
    public boolean testAddition(ItemStack stack) {
        return this.addition.test(stack);
    }

    public static class Serializer implements RecipeSerializer<RuinedEquipmentSmithingEmpowerRecipe> {

        @Override
        public RuinedEquipmentSmithingEmpowerRecipe read(Identifier id, JsonObject json) {
            return new RuinedEquipmentSmithingEmpowerRecipe();
        }

        @Override
        public RuinedEquipmentSmithingEmpowerRecipe read(Identifier id, PacketByteBuf buf) {
            return new RuinedEquipmentSmithingEmpowerRecipe();
        }

        @Override
        public void write(PacketByteBuf buf, RuinedEquipmentSmithingEmpowerRecipe recipe) {}
    }
}
