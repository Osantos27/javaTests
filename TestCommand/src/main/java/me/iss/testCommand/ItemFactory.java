package me.iss.testCommand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

import static me.iss.testCommand.CustomKeys.OLYMPUS_SWORD;
import static me.iss.testCommand.CustomKeys.SPECIAL_DIAMOND;

public class ItemFactory {

    public static ItemStack createOlympusSword() {
        ItemStack olympusSword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = olympusSword.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + "Olympus Sword");
        meta.setLore(List.of("A legendary sword forged in the fires of Olympus."));
        meta.setUnbreakable(true);
        meta.getPersistentDataContainer().set(OLYMPUS_SWORD, PersistentDataType.BOOLEAN, true);

        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);

        AttributeModifier damageModifier = new AttributeModifier(
                UUID.randomUUID(),
                "generic.attackDamage",
                15.0,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlot.HAND
        );
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);
        olympusSword.setItemMeta(meta);

        return olympusSword;
    }

    public static ItemStack createSpecialDiamond() {
        ItemStack SpecialDiamond = new ItemStack(Material.DIAMOND, 1);
        ItemMeta SpecialDiamondMeta = SpecialDiamond.getItemMeta();
        SpecialDiamondMeta.setDisplayName(ChatColor.AQUA + "Special Diamond");
        SpecialDiamondMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        SpecialDiamondMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        SpecialDiamondMeta.getPersistentDataContainer().set(SPECIAL_DIAMOND, PersistentDataType.BOOLEAN, true);

        SpecialDiamond.setItemMeta(SpecialDiamondMeta);
        return SpecialDiamond;
    }

    public static ItemStack createSMT() {
        ItemStack SMT = new ItemStack(Material.DIAMOND, 1);
        ItemMeta SMTMeta = SMT.getItemMeta();
        SMTMeta.setDisplayName(ChatColor.AQUA + "SMT");

        SMT.setItemMeta(SMTMeta);
        return SMT;
    }

    public static ItemStack createMeltedDiamond() {
        ItemStack MeltedDiamond = new ItemStack(Material.DIAMOND, 1);
        ItemMeta MeltedDiamondMeta = MeltedDiamond.getItemMeta();
        MeltedDiamondMeta.setDisplayName(ChatColor.GRAY + "Melted Diamond");

        NamespacedKey meltedKey = new NamespacedKey(main.getInstance(), "melted_diamond");
        MeltedDiamondMeta.getPersistentDataContainer().set(meltedKey, PersistentDataType.BOOLEAN, true);

        MeltedDiamond.setItemMeta(MeltedDiamondMeta);
        return MeltedDiamond;
    }

}
