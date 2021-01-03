package pepjebs.ruined_equipment.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
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

import java.util.List;

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
        String tagString = stack.getTag().getString("enchantments");
        String[] tagStrings = tagString.split(",");
        if (tagStrings.length == 0) return;

        MutableText newT0 = tooltip.get(0).shallowCopy();
        if (stack.hasGlint()) {
            newT0 = new LiteralText(newT0.getString()).formatted(Formatting.AQUA);
            if (stack.hasCustomName()) {
                newT0 = newT0.formatted(Formatting.ITALIC);
            }
            tooltip.set(0, newT0);
        }

        for (String encodedEnch : tagStrings) {
            String[] enchantParts = encodedEnch.split("_");
            String[] enchantKey = enchantParts[0].split(":");
            int enchantLevel = Integer.parseInt(enchantParts[1]);
            Enchantment e = Registry.ENCHANTMENT.get(new Identifier(enchantKey[0], enchantKey[1]));
            if (e == null) continue;
            tooltip.add(new LiteralText(e.getName(enchantLevel).getString()).formatted(Formatting.GRAY));
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        // Get existing text
        MutableText supered = super.getName().shallowCopy();
        // Append vanilla item's name
        Item vanillaItem = RuinedEquipmentItems.VANILLA_ITEM_MAP.get(this);
        supered = supered.append(new TranslatableText(vanillaItem.getTranslationKey()));
        // Add the Aqua text if it has a glint
        if (hasGlint(stack)) {
            supered = supered.formatted(Formatting.AQUA);
        }
        return supered;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack != null && stack.getTag() != null && stack.getTag().getString("enchantments") != null
                && !stack.getTag().getString("enchantments").isEmpty();
    }
}
