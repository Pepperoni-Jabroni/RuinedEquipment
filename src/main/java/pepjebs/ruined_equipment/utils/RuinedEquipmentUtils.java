package pepjebs.ruined_equipment.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.item.RuinedAshesItem;
import pepjebs.ruined_equipment.item.RuinedEquipmentItem;
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;
import pepjebs.ruined_equipment.recipe.RuinedEquipmentSmithingEmpowerRecipe;

import java.util.*;
import java.util.stream.Collectors;

public class RuinedEquipmentUtils {

    public static String RUINED_ENCHTS_TAG = "RuinedEnchantments";
    public static String RUINED_ITEM_KEY_TAG = "ItemKey";

    public static boolean ruinedItemHasEnchantment(ItemStack ruinedItem, Enchantment enchantment) {
        if (ruinedItem.getNbt() == null) return false;
        String tagString = ruinedItem.getNbt().getString(RUINED_ENCHTS_TAG);
        Map<Enchantment, Integer> enchantMap = RuinedEquipmentUtils.processEncodedEnchantments(tagString);
        if (enchantMap == null) return false;
        for (Enchantment e : enchantMap.keySet()) {
            if (e == enchantment) return true;
        }
        return false;
    }

    public static boolean isRuinedItem(Item item) {
        return Registries.ITEM.getId(item).getNamespace().compareTo(RuinedEquipmentMod.MOD_ID) == 0;
    }

    public static Item getEmpowermentApplicationItem() {
        if (RuinedEquipmentMod.CONFIG != null) {
            try {
                String[] itemId = RuinedEquipmentMod.CONFIG.empowermentSmithingItem.split(":");
                return Registries.ITEM.get(new Identifier(itemId[0], itemId[1]));
            } catch (Exception e) {
                RuinedEquipmentMod.LOGGER.warn(e.getMessage());
            }
        }
        return Items.NETHERITE_SCRAP;
    }

    public static int compareItemsById(Item i1, Item i2) {
        return Registries.ITEM.getId(i1).compareTo(Registries.ITEM.getId(i2));
    }

    public static int generateRepairLevelCost(ItemStack repaired, int maxLevel) {
        int targetLevel = maxLevel * (repaired.getMaxDamage() - repaired.getDamage()) / repaired.getMaxDamage();
        return Math.max(targetLevel, 1);
    }

    public static Item getRepairItemForItemStack(ItemStack stack) {
        Item vanillaItem = RuinedEquipmentItems.getVanillaItemMap().get(stack.getItem());
        if (vanillaItem == null) {
            Identifier modItemId = RuinedEquipmentUtils.getItemKeyIdFromItemStack(stack);
            vanillaItem = Registries.ITEM.get(modItemId);
        }
        return vanillaItem;
    }

    public static ItemStack generateRepairedItemForAnvilByFraction(
            ItemStack leftStack,
            double damageFraction) {
        Item vanillaItem = getRepairItemForItemStack(leftStack);
        int maxDamage = new ItemStack(vanillaItem).getMaxDamage();
        return generateRepairedItemForAnvilByDamage(leftStack, (int) (damageFraction * (double) maxDamage));
    }

    public static ItemStack generateRepairedItemForAnvilByDamage(
            ItemStack leftStack,
            int targetDamage){
        boolean isMaxEnchant = leftStack.getNbt() != null &&
                leftStack.getNbt().contains(RuinedEquipmentSmithingEmpowerRecipe.RUINED_MAX_ENCHT_TAG)
                && leftStack.getNbt().getBoolean(RuinedEquipmentSmithingEmpowerRecipe.RUINED_MAX_ENCHT_TAG);

        Item vanillaItem = getRepairItemForItemStack(leftStack);
        ItemStack repaired = new ItemStack(vanillaItem);
        NbtCompound tag = leftStack.getOrCreateNbt();
        String encodedEnch = tag.getString(RUINED_ENCHTS_TAG);
        if (!encodedEnch.isEmpty()) tag.remove(RUINED_ENCHTS_TAG);
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
        repaired.setNbt(repaired.getOrCreateNbt().copyFrom(tag));
        repaired.setDamage(targetDamage);
        return repaired;
    }

    public static Map<Enchantment, Integer> processEncodedEnchantments(String encodedEnchants) {
        if (encodedEnchants.isEmpty()) return null;
        Map<Enchantment, Integer> enchants = new HashMap<>();
        for (String encodedEnchant : encodedEnchants.split(",")) {
            String[] enchantItem = encodedEnchant.split(">");
            String[] enchantKey = enchantItem[0].split(":");
            int enchantLevel = Integer.parseInt(enchantItem[1]);
            enchants.put(Registries.ENCHANTMENT.get(new Identifier(enchantKey[0], enchantKey[1])), enchantLevel);
        }
        return enchants.isEmpty() ? null : enchants;
    }

    public static List<Identifier> getParsedBlocklistForRuinedAshesItems() {
        if (RuinedEquipmentMod.CONFIG == null || RuinedEquipmentMod.CONFIG.blocklistForRuinedAshesItems == null
                || RuinedEquipmentMod.CONFIG.blocklistForRuinedAshesItems.isEmpty())
            return new ArrayList<>();
        return Arrays.stream(RuinedEquipmentMod.CONFIG.blocklistForRuinedAshesItems
                .split(";"))
                .map(p -> {
                    String[] idParts = p.split(":");
                    return new Identifier(idParts[0], idParts[1]);
                })
                .toList();
    }

    public static void onSendEquipmentBreakStatusImpl(
            ServerPlayerEntity serverPlayer,
            ItemStack breakingStack,
            boolean forceSet) {
        for (Map.Entry<Item, Item> itemMap : RuinedEquipmentItems.getVanillaItemMap().entrySet()) {
            if (isVanillaItemStackBreaking(breakingStack, itemMap.getValue())) {
                ItemStack ruinedStack = new ItemStack(itemMap.getKey());
                processBreakingEquipment(serverPlayer, breakingStack, forceSet, ruinedStack);
                return;
            }
        }
        if (RuinedEquipmentMod.CONFIG.enableRuinedItemsAshesGeneration) {
            ItemStack ruinedStack = new ItemStack(RuinedEquipmentMod.RUINED_ASHES_ITEM);
            if (getParsedBlocklistForRuinedAshesItems().stream()
                    .anyMatch(i -> i.compareTo(Registries.ITEM.getId(breakingStack.getItem())) == 0))
                return;
            processBreakingEquipment(serverPlayer, breakingStack, forceSet, ruinedStack);
        }
    }

    private static void processBreakingEquipment(
            ServerPlayerEntity serverPlayer,
            ItemStack breakingStack,
            boolean forceSet,
            ItemStack ruinedStack
            ) {
        NbtCompound breakingNBT = breakingStack.getOrCreateNbt();
        if (breakingNBT.contains("Damage")) breakingNBT.remove("Damage");
        if (breakingNBT.contains("RepairCost")) breakingNBT.remove("RepairCost");
        // Set enchantment NBT data
        NbtCompound enchantTag = getNbtForEnchantments(breakingStack, ruinedStack);
        if (enchantTag != null) breakingNBT.copyFrom(enchantTag);
        if (breakingNBT.contains("Enchantments")) breakingNBT.remove("Enchantments");
        if (ruinedStack.getItem() == RuinedEquipmentMod.RUINED_ASHES_ITEM) {
            Identifier breakingId = Registries.ITEM.getId(breakingStack.getItem());
            breakingNBT.putString(RUINED_ITEM_KEY_TAG, breakingId.toString());
        }
        ruinedStack.setNbt(breakingNBT);
        // Force set will place the Ruined item in hand
        if (forceSet) {
            int idx = 0;
            if (serverPlayer.getInventory().offHand.get(0).toString().compareTo(breakingStack.toString()) != 0)
                idx = serverPlayer.getInventory().selectedSlot + 1;
            RuinedEquipmentMod.ruinedEquipmentSetter.put(
                    serverPlayer.getName().getString(),
                    new Pair<>(idx, ruinedStack));
        } else {
            serverPlayer.getInventory().offerOrDrop(ruinedStack);
        }
    }

    public static Identifier getItemKeyIdFromItemStack(ItemStack ruinedItemAshes) {
        assert ruinedItemAshes.getItem() == RuinedEquipmentMod.RUINED_ASHES_ITEM;
        if (ruinedItemAshes.getNbt() == null || !ruinedItemAshes.getNbt().contains(RUINED_ITEM_KEY_TAG)) return null;
        return new Identifier(ruinedItemAshes.getNbt().getString(RUINED_ITEM_KEY_TAG));

    }

    public static Map<Identifier, Identifier> getParsedRuinedItemsAshesRepairItems() {
        if (RuinedEquipmentMod.CONFIG.ruinedItemsAshesRepairItems == null) return null;
        String ruinedItemsAshesRepairStr = RuinedEquipmentMod.CONFIG.ruinedItemsAshesRepairItems;
        return Arrays.stream(ruinedItemsAshesRepairStr.split(";"))
                .map(s -> {
                    String[] modItem = s.split("/")[0].split(":");
                    String[] modRepair = s.split("/")[1].split(":");
                    Identifier modItemId = new Identifier(modItem[0], modItem[1]);
                    Identifier modRepairId = new Identifier(modRepair[0], modRepair[1]);
                    return new Pair<>(modItemId, modRepairId);
                })
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

    public static NbtCompound getNbtForEnchantments(ItemStack breakingStack, ItemStack ruinedStack) {
        Set<String> enchantmentStrings = new HashSet<>();
        for (Map.Entry<Enchantment, Integer> ench : EnchantmentHelper.get(breakingStack).entrySet()) {
            String enchantString = Registries.ENCHANTMENT.getId(ench.getKey())+">"+ench.getValue();
            enchantmentStrings.add(enchantString);
        }
        if (!enchantmentStrings.isEmpty()) {
            NbtCompound tag = ruinedStack.getNbt();
            if (tag == null) tag = new NbtCompound();
            tag.putString(RUINED_ENCHTS_TAG, String.join(",", enchantmentStrings));
            return tag;
        }
        return null;
    }

    public static boolean isVanillaItemStackBreaking(ItemStack breakingStack, Item vanillaItem) {
        return breakingStack.isItemEqual(new ItemStack(vanillaItem))
            && breakingStack.getMaxDamage() - breakingStack.getDamage() <= 0;
    }
}
