package com.test2;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerJoinEvent;

public final class DoubleJumpListener extends JavaPlugin implements Listener {


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
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setVelocity(player.getLocation().getDirection().multiply(1.2).setY(0.8));
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