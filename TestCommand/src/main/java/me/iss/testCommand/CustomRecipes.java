package me.iss.testCommand;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static me.iss.testCommand.CustomKeys.MELTED_DIAMOND;
import static me.iss.testCommand.CustomKeys.SPECIAL_DIAMOND;

public final class CustomRecipes implements Listener {

    public static void initializeRecipes() {
        ItemStack SpecialDiamond = ItemFactory.createSpecialDiamond();
        ItemStack olympusSword = ItemFactory.createOlympusSword();
        ItemStack MeltedDiamond = ItemFactory.createMeltedDiamond();

        FurnaceRecipe meltingRecipe = new FurnaceRecipe(
                new NamespacedKey(main.getInstance(), "MeltedDiamondRecipe"),
                MeltedDiamond,
                new RecipeChoice.MaterialChoice(Material.DIAMOND) {
                    @Override
                    public boolean test(ItemStack itemStack) {
                        return super.test(itemStack) && !isSpecialDiamond(itemStack) && !isMeltedDiamond(itemStack);
                    }
                },
                2.5f,
                400
        );
        Bukkit.addRecipe(meltingRecipe);

        ShapelessRecipe SpecialDiamondRecipe = new ShapelessRecipe(new NamespacedKey(main.getInstance(), "SpecialDiamondRecipe"), SpecialDiamond);
        for (int i = 0; i < 9; i++) {
            SpecialDiamondRecipe.addIngredient(new RecipeChoice.ExactChoice(MeltedDiamond));
        }
        Bukkit.addRecipe(SpecialDiamondRecipe);

        ShapedRecipe recipeOlympus = new ShapedRecipe(CustomKeys.OLYMPUS_SWORD, olympusSword);
        recipeOlympus.shape(" D ", " D ", " S ");

        RecipeChoice specialDiamondChoice = new RecipeChoice.MaterialChoice(Material.DIAMOND) {
            @Override
            public boolean test(ItemStack itemStack) {
                return itemStack != null &&
                        itemStack.getType() == Material.DIAMOND &&
                        isSpecialDiamond(itemStack);
            }
        };

        recipeOlympus.setIngredient('D', specialDiamondChoice);
        recipeOlympus.setIngredient('S', Material.STICK);
        Bukkit.addRecipe(recipeOlympus);

        Bukkit.getPluginManager().registerEvents(new CustomRecipes(), main.getInstance());
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        Recipe recipe = event.getRecipe();

        if (recipe != null && isCustomRecipe(recipe)) {
            return;
        }

        ItemStack[] matrix = inventory.getMatrix();

        for (ItemStack item : matrix) {
            if (isSpecialDiamond(item)) {
                inventory.setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        CraftingInventory inventory = event.getInventory();
        Recipe recipe = event.getRecipe();

        // Skip if it's one of our custom recipes
        if (recipe != null && isCustomRecipe(recipe)) {
            return;
        }

        ItemStack[] matrix = inventory.getMatrix();

        for (ItemStack item : matrix) {
            if (isSpecialDiamond(item)) {
                event.setCancelled(true);
                if (event.getWhoClicked() instanceof Player) {
                    Player player = (Player) event.getWhoClicked();
                    player.sendMessage(ChatColor.RED + "Special Diamonds can only be used in custom recipes!");
                }
                return;
            }
        }
    }

    private boolean isCustomRecipe(Recipe recipe) {
        if (!(recipe instanceof Keyed)) {
            return false;
        }

        NamespacedKey key = ((Keyed) recipe).getKey();

        return key.equals(CustomKeys.OLYMPUS_SWORD) ||
                key.equals(new NamespacedKey(main.getInstance(), "SpecialDiamondRecipe")) ||
                key.equals(new NamespacedKey(main.getInstance(), "MeltedDiamondRecipe"));
    }

    public static boolean isSpecialDiamond(ItemStack item) {
        if (item == null || item.getType() != Material.DIAMOND) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        return meta.getPersistentDataContainer().has(SPECIAL_DIAMOND, PersistentDataType.BOOLEAN);
    }

    public static boolean isMeltedDiamond(ItemStack item) {
        if (item == null || item.getType() != Material.DIAMOND) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        return meta.getPersistentDataContainer().has(MELTED_DIAMOND, PersistentDataType.BOOLEAN);
    }
}