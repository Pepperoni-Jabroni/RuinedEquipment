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
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;

import java.util.*;

public class RuinedEquipmentUtils {

    public static int generateRepairLevelCost(ItemStack repaired) {
        return (int) 30.0 * (repaired.getMaxDamage() - repaired.getDamage()) / repaired.getMaxDamage();
    }

    public static ItemStack generateRepairedItemForAnvilByFraction(
            ItemStack leftStack,
            double damageFraction) {
        int maxDamage = new ItemStack(RuinedEquipmentItems.VANILLA_ITEM_MAP.get(leftStack.getItem())).getMaxDamage();
        return generateRepairedItemForAnvilByDamage(leftStack, (int) (damageFraction * (double) maxDamage));
    }

    public static ItemStack generateRepairedItemForAnvilByDamage(
            ItemStack leftStack,
            int targetDamage){
        ItemStack repaired = new ItemStack(RuinedEquipmentItems.VANILLA_ITEM_MAP.get(leftStack.getItem()));
        repaired.setDamage(targetDamage);
        if (leftStack.hasCustomName()) {
            repaired.setCustomName(leftStack.getName());
        }
        CompoundTag tag = leftStack.getTag();
        if (tag != null) {
            String encodedEnch = tag.getString("enchantments");
            Map<Enchantment, Integer> enchantMap = RuinedEquipmentUtils.processEncodedEnchantments(encodedEnch);
            if (enchantMap != null) {
                for (Map.Entry<Enchantment, Integer> enchant : enchantMap.entrySet()) {
                    repaired.addEnchantment(enchant.getKey(), enchant.getValue());
                }
            }
        }
        return repaired;
    }

    public static Map<Enchantment, Integer> processEncodedEnchantments(String encodedEnchants) {
        Map<Enchantment, Integer> enchants = new HashMap<>();
        for (String encodedEnchant : encodedEnchants.split(",")) {
            String[] enchantItem = encodedEnchant.split("_");
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
        for (Map.Entry<Item, Item> itemMap : RuinedEquipmentItems.VANILLA_ITEM_MAP.entrySet()) {
            if (isVanillaItemStackBreaking(breakingStack, itemMap.getValue())) {
                // Crossbows don't play nicely for some reason...
                if (breakingStack.isItemEqualIgnoreDamage(new ItemStack(Items.CROSSBOW))) forceSet = false;
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
            String enchantString = Registry.ENCHANTMENT.getId(ench.getKey())+"_"+ench.getValue();
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
