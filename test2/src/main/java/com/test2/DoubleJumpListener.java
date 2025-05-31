package com.test2;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;

public final class DoubleJumpListener extends JavaPlugin implements Listener {


    private final HashMap<UUID, Long> lastJumpTime = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent playerJoinEvent)
    {
        playerJoinEvent.getPlayer().setAllowFlight(true);
    }

    @EventHandler
    public void onPlayerToggleFlight (PlayerToggleFlightEvent playerToggleFlightEvent)
    {

        var player = playerToggleFlightEvent.getPlayer();


        if (player.getGameMode() != GameMode.ADVENTURE && player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        if (!player.isFlying()){


            playerToggleFlightEvent.setCancelled(true);


            if (lastJumpTime.containsKey(player.getUniqueId())){
                long timeElapsed = System.currentTimeMillis() - lastJumpTime.get(player.getUniqueId());
                if (timeElapsed < 5000 ){
                    double remainingSeconds = Math.ceil((5000 - timeElapsed) / 100.0);
                    player.sendMessage(ChatColor.RED + "The ability is in cooldown for" + ChatColor.DARK_RED + remainingSeconds + ChatColor.RED + "!!!");
                    return;
                }
            }



            player.setAllowFlight(false);

            player.setFlying(false);

            player.setVelocity(player.getLocation().getDirection().multiply(1.2).setY(0.8));

            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.5, 0.1, 0.5, 0.1);

            // Add sound effect
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, 1.0f);


            lastJumpTime.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onPlayerLanding(PlayerMoveEvent playerMoveEvent)
    {
        Player player = playerMoveEvent.getPlayer();

        if (((Entity) player).isOnGround()){
            player.setAllowFlight(true);
        }
    }
}