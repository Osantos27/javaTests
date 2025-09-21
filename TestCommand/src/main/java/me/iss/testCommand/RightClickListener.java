package me.iss.testCommand;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.iss.testCommand.CustomKeys.OLYMPUS_SWORD;

public class RightClickListener implements Listener {

    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final long cooldownMillis = 10 * 1000; // 10 seconds in milliseconds

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if (itemStack == null || !itemStack.hasItemMeta()) {
            return;
        }
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(OLYMPUS_SWORD, PersistentDataType.BOOLEAN)) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (event.getClickedBlock() != null && event.getClickedBlock().getType().isInteractable()) {
                return;
            } else {
                event.setCancelled(true);
            }
            long now = System.currentTimeMillis();
            if (cooldowns.containsKey(player.getUniqueId())) {
                long lastUse = cooldowns.get(player.getUniqueId());
                if (now - lastUse < cooldownMillis) {
                    long cooldown = (cooldownMillis - (now - lastUse)) / 1000;
                    player.sendMessage(ChatColor.RED + "Ability on cooldown! Wait " + cooldown + " secs.");
                    return;
                }
            }
            Location loc = player.getLocation();
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
                if (entity instanceof Monster) {
                    Monster monster = (Monster) entity;

                    entity.getWorld().strikeLightningEffect(entity.getLocation());
                    monster.damage(25.0, player);
                    monster.setFireTicks(100); //20 ticks = 1s 100 ticks = 5s

                    Location groundLoc = entity.getLocation().clone();
                    Location fireLoc = groundLoc.clone().add(0, 1, 0);

                    if (fireLoc.getBlock().getType() == Material.AIR &&
                            groundLoc.getBlock().getType().isSolid()) {
                        fireLoc.getBlock().setType(Material.FIRE);
                    }
                }
            }
            cooldowns.put(player.getUniqueId(), now);
        }
    }
}