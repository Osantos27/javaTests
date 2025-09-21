package me.iss.testCommand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

public class KitsCommand implements CommandExecutor, TabExecutor {


    private JavaPlugin plugin;

    public KitsCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (args.length == 0) {
            ConfigurationSection kitsSection = plugin.getConfig().getConfigurationSection("kits");

            if (kitsSection == null) {
                player.sendMessage(ChatColor.RED + "DEBUG: kitsSection is null!");
                player.sendMessage(ChatColor.RED + "DEBUG: Trying to reload config...");

                // Attempt to reload the config
                plugin.reloadConfig();

                if (kitsSection == null) {
                    player.sendMessage(ChatColor.RED + "ERROR: Config section 'kits' not found!");
                    player.sendMessage(ChatColor.RED + "Please check your config.yml file structure.");
                    return true;
                } else {
                    player.sendMessage(ChatColor.GREEN + "Config reloaded successfully!");
                }
            }

            Set<String> kits = kitsSection.getKeys(false);

            if (kits.isEmpty()) {
                player.sendMessage(ChatColor.GOLD + "Available Kits");
                for (String kitName : kits) {
                    player.sendMessage(ChatColor.GREEN + "• " + ChatColor.WHITE + kitName);
                }
                return true;
            }
            return true;
        } else {

            String kitName = args[0];


            if (!plugin.getConfig().contains("kits." + kitName)) {
                player.sendMessage(ChatColor.RED + "Kit '" + kitName + "' does not exist.");
                return true;
            }
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(ChatColor.RED + "Your inventory is full.");
                return true;
            }
            List<String> items = plugin.getConfig().getStringList("kits." + kitName + ".items");

            for (String item : items) {
                String[] parts = item.split(":");
                Material material = Material.matchMaterial(parts[0]);
                int quantidade = Integer.parseInt(parts[1]);

                ItemStack itemStack = new ItemStack(material, quantidade);

                player.getInventory().addItem(itemStack);
                //player.sendMessage(ChatColor.GREEN + "You received: " + item);
            }
            player.sendMessage(ChatColor.GREEN + "You have received the kit: " + kitName);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("tools", "armor");
        }
        return List.of();
    }
}
