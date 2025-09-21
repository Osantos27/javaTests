package me.iss.testCommand;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class main extends JavaPlugin {


    @Override
    public void onEnable() {
        CustomRecipes.initializeRecipes();

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new RightClickListener(), this);

        Objects.requireNonNull(getCommand("Test")).setExecutor(new TestCommand());
        Objects.requireNonNull(getCommand("Heal")).setExecutor(new HealCommand());
        Objects.requireNonNull(getCommand("Test")).setTabCompleter(new TestCommand());
        Objects.requireNonNull(getCommand("Heal")).setTabCompleter(new HealCommand());
        Objects.requireNonNull(getCommand("Kits")).setExecutor(new KitsCommand(this));
        Objects.requireNonNull(getCommand("Kits")).setTabCompleter(new KitsCommand(this));
        //*********//
        Objects.requireNonNull(getCommand("GiveCustomItem")).setExecutor(new GiveCustomItemCommand());


    }

    @Override
    public void onDisable() {
        //onDisable exists?
    }

    public static main getInstance() {
        return getPlugin(main.class);
    }
}