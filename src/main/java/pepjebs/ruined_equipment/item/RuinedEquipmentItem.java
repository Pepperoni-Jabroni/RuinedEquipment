package pepjebs.ruined_equipment.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.recipe.RuinedEquipmentSmithingEmpowerRecipe;
import pepjebs.ruined_equipment.utils.RuinedEquipmentUtils;

import java.util.List;
import java.util.Map;

public class RuinedEquipmentItem extends Item {

    public RuinedEquipmentItem(Settings settings) {
        super(settings);
    }

    @Override
    public String getTranslationKey() {
        return "item.ruined_equipment.ruined_prefix";
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (stack.getTag() == null) return;
        String tagString = stack.getTag().getString(RuinedEquipmentUtils.RUINED_ENCHTS_TAG);
        Map<Enchantment, Integer> enchantMap = RuinedEquipmentUtils.processEncodedEnchantments(tagString);
        if (enchantMap != null) {
            MutableText newT0 = tooltip.get(0).shallowCopy();
            if (stack.hasGlint()) {
                newT0 = new LiteralText(newT0.getString()).formatted(Formatting.AQUA);
                if (stack.hasCustomName()) {
                    newT0 = newT0.formatted(Formatting.ITALIC);
                }
                tooltip.set(0, newT0);
            }

            for (Map.Entry<Enchantment, Integer> enchant : enchantMap.entrySet()) {
                tooltip.add(new LiteralText(
                        enchant.getKey().getName(enchant.getValue()).getString()).formatted(Formatting.GRAY));
            }
        }
        if (stack.getTag() != null && stack.getTag().contains(RuinedEquipmentSmithingEmpowerRecipe.RUINED_MAX_ENCHT_TAG)
                && stack.getTag().getBoolean(RuinedEquipmentSmithingEmpowerRecipe.RUINED_MAX_ENCHT_TAG)) {
            tooltip.add(new TranslatableText("item.ruined_equipment.ruined_upgrading")
                    .formatted(Formatting.GRAY));
        }
        if (this == Registry.ITEM.get(new Identifier(RuinedEquipmentMod.MOD_ID, "ruined_shield"))
                && stack.getTag().contains("BlockEntityTag")) {
            tooltip.add(new TranslatableText("item.ruined_equipment.ruined_shield.banner")
                    .formatted(Formatting.GRAY)
                    .formatted(Formatting.ITALIC));
        }
    }

    @Override
    public Text getName() {
        // Get existing text
        MutableText supered = super.getName().shallowCopy();
        // Append vanilla item's name
        Item vanillaItem = RuinedEquipmentItems.getVanillaItemMap().get(this);
        supered = supered.append(new TranslatableText(vanillaItem.getTranslationKey()));
        return supered;
    }

    @Override
    public Text getName(ItemStack stack) {
        // Get existing text
        MutableText supered = super.getName().shallowCopy();
        // Append vanilla item's name
        Item vanillaItem = RuinedEquipmentItems.getVanillaItemMap().get(this);
        supered = supered.append(new TranslatableText(vanillaItem.getTranslationKey()));
        // Add the Aqua text if it has a glint
        if (hasGlint(stack)) {
            supered = supered.formatted(Formatting.AQUA);
        }
        return supered;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack != null && stack.getTag() != null
                && stack.getTag().getString(RuinedEquipmentUtils.RUINED_ENCHTS_TAG) != null
                && !stack.getTag().getString(RuinedEquipmentUtils.RUINED_ENCHTS_TAG).isEmpty();
    }
}
