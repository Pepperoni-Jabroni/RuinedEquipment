package pepjebs.ruined_equipment.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;
import pepjebs.ruined_equipment.recipe.RuinedEquipmentSmithingEmpowerRecipe;

import java.util.*;

public class RuinedEquipmentUtils {

    public static boolean ruinedItemHasEnchantment(ItemStack ruinedItem, Enchantment enchantment) {
        if (ruinedItem.getTag() == null) return false;
        String tagString = ruinedItem.getTag().getString("enchantments");
        Map<Enchantment, Integer> enchantMap = RuinedEquipmentUtils.processEncodedEnchantments(tagString);
        if (enchantMap == null) return false;
        for (Enchantment e : enchantMap.keySet()) {
            if (e == enchantment) return true;
        }
        return false;
    }

    public static Item getEmpowermentApplicationItem() {
        if (RuinedEquipmentMod.CONFIG != null) {
            try {
                String[] itemId = RuinedEquipmentMod.CONFIG.empowermentSmithingItem.split(":");
                return Registry.ITEM.get(new Identifier(itemId[0], itemId[1]));
            } catch (Exception e) {
                RuinedEquipmentMod.LOGGER.warn(e.getMessage());
            }
        }
        return Items.NETHERITE_SCRAP;
    }

    public static int compareItemsById(Item i1, Item i2) {
        return Registry.ITEM.getId(i1).compareTo(Registry.ITEM.getId(i2));
    }

    public static int generateRepairLevelCost(ItemStack repaired, int maxLevel) {
        int targetLevel = maxLevel * (repaired.getMaxDamage() - repaired.getDamage()) / repaired.getMaxDamage();
        return Math.max(targetLevel, 1);
    }

    public static ItemStack generateRepairedItemForAnvilByFraction(
            ItemStack leftStack,
            double damageFraction,
            boolean isMaxEnchant) {
        int maxDamage = new ItemStack(RuinedEquipmentItems.getVanillaItemMap().get(leftStack.getItem())).getMaxDamage();
        return generateRepairedItemForAnvilByDamage(leftStack, (int) (damageFraction * (double) maxDamage), isMaxEnchant);
    }

    public static ItemStack generateRepairedItemForAnvilByDamage(
            ItemStack leftStack,
            int targetDamage,
            boolean isMaxEnchant){
        ItemStack repaired = new ItemStack(RuinedEquipmentItems.getVanillaItemMap().get(leftStack.getItem()));
        repaired.setDamage(targetDamage);
        if (leftStack.hasCustomName()) {
            repaired.setCustomName(leftStack.getName());
        }
        CompoundTag tag = leftStack.getTag();
        if (tag != null) {
            if (isMaxEnchant) {
                tag.remove(RuinedEquipmentSmithingEmpowerRecipe.RUINED_MAX_ENCHT_TAG);
            }
            if (leftStack.getItem() instanceof DyeableItem) {
                ((DyeableItem) repaired.getItem()).setColor(repaired,
                        ((DyeableItem) leftStack.getItem()).getColor(leftStack));
            }
            if (leftStack.getItem() ==
                    Registry.ITEM.get(new Identifier(RuinedEquipmentMod.MOD_ID, "ruined_shield"))) {
                CompoundTag newTag = repaired.getTag();
                if (newTag == null) newTag = new CompoundTag();
                if (tag.contains("BlockEntityTag")) {
                    newTag.put("BlockEntityTag", tag.getCompound("BlockEntityTag"));
                    repaired.setTag(newTag);
                }
            }
            String encodedEnch = tag.getString("enchantments");
            Map<Enchantment, Integer> enchantMap = RuinedEquipmentUtils.processEncodedEnchantments(encodedEnch);
            if (enchantMap != null) {
                for (Map.Entry<Enchantment, Integer> enchant : enchantMap.entrySet()) {
                    if (isMaxEnchant) {
                        repaired.addEnchantment(enchant.getKey(), enchant.getKey().getMaxLevel());
                    } else {
                        repaired.addEnchantment(enchant.getKey(), enchant.getValue());
                    }
                }
            }
        }
        return repaired;
    }

    public static Map<Enchantment, Integer> processEncodedEnchantments(String encodedEnchants) {
        if (encodedEnchants.isEmpty()) return null;
        Map<Enchantment, Integer> enchants = new HashMap<>();
        for (String encodedEnchant : encodedEnchants.split(",")) {
            String[] enchantItem = encodedEnchant.split(">");
            String[] enchantKey = enchantItem[0].split(":");
            int enchantLevel = Integer.parseInt(enchantItem[1]);
            enchants.put(Registry.ENCHANTMENT.get(new Identifier(enchantKey[0], enchantKey[1])), enchantLevel);
        }
        return enchants.isEmpty() ? null : enchants;
    }

    public static void onSendEquipmentBreakStatusImpl(
            ServerPlayerEntity serverPlayer,
            ItemStack breakingStack,
            boolean forceSet) {
        for (Map.Entry<Item, Item> itemMap : RuinedEquipmentItems.getVanillaItemMap().entrySet()) {
            if (isVanillaItemStackBreaking(breakingStack, itemMap.getValue())) {
                ItemStack ruinedStack = new ItemStack(itemMap.getKey());
                // Set enchantment NBT data
                CompoundTag enchantTag = getTagForEnchantments(breakingStack, ruinedStack);
                if (enchantTag != null) ruinedStack.setTag(enchantTag);
                // Handle Ruined item name
                MutableText breakingToolName = new LiteralText(breakingStack.getName().getString());
                if (breakingStack.hasCustomName()) {
                    if (ruinedStack.hasGlint()) {
                        breakingToolName = breakingToolName.formatted(Formatting.AQUA);
                    }
                    ruinedStack.setCustomName(breakingToolName);
                }
                // Handle Leather Armors color
                if (breakingStack.getItem() instanceof DyeableItem) {
                    int breakingColor = ((DyeableItem) breakingStack.getItem()).getColor(breakingStack);
                    ((DyeableItem) ruinedStack.getItem()).setColor(ruinedStack, breakingColor);
                    DyeableItem.blendAndSetColor(ruinedStack, new LinkedList<>());
                }
                // Handle Shield banners
                if (breakingStack.getItem() == Items.SHIELD && breakingStack.getTag() != null
                        && breakingStack.getTag().contains("BlockEntityTag")) {
                    CompoundTag tag = ruinedStack.getTag();
                    if (tag == null) tag = new CompoundTag();
                    tag.put("BlockEntityTag", breakingStack.getTag().getCompound("BlockEntityTag"));
                }
                // Force set will place the Ruined item in hand
                if (forceSet) {
                    serverPlayer.inventory.setStack(serverPlayer.inventory.selectedSlot, ruinedStack);
                } else {
                    serverPlayer.inventory.insertStack(ruinedStack);
                }
            }
        }
    }

    public static CompoundTag getTagForEnchantments(ItemStack breakingStack, ItemStack ruinedStack) {
        Set<String> enchantmentStrings = new HashSet<>();
        for (Map.Entry<Enchantment, Integer> ench : EnchantmentHelper.get(breakingStack).entrySet()) {
            String enchantString = Registry.ENCHANTMENT.getId(ench.getKey())+">"+ench.getValue();
            enchantmentStrings.add(enchantString);
        }
        if (!enchantmentStrings.isEmpty()) {
            CompoundTag tag = ruinedStack.getTag();
            if (tag == null) tag = new CompoundTag();
            tag.putString("enchantments", String.join(",", enchantmentStrings));
            return tag;
        }
        return null;
    }

    public static boolean isVanillaItemStackBreaking(ItemStack breakingStack, Item vanillaItem) {
        return breakingStack.isItemEqualIgnoreDamage(new ItemStack(vanillaItem))
            && breakingStack.getMaxDamage() - breakingStack.getDamage() <= 0;
    }
}
