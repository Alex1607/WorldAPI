package eu.greev.worldapi;

import eu.greev.worldapi.api.Api;
import eu.greev.worldapi.api.impl.ApiImpl;
import eu.greev.worldapi.commands.DeleteWorldCommand;
import eu.greev.worldapi.commands.LoadWorldCommand;
import eu.greev.worldapi.commands.SaveWorldCommand;
import eu.greev.worldapi.commands.TeleportWorldCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class WorldAPI extends JavaPlugin {

    private static Api worldApi = new ApiImpl();

    @Override
    public void onEnable() {
        getCommand("loadworld").setExecutor(new LoadWorldCommand());
        getCommand("deleteworld").setExecutor(new DeleteWorldCommand());
        getCommand("teleportworld").setExecutor(new TeleportWorldCommand());
        getCommand("saveworld").setExecutor(new SaveWorldCommand());

        Bukkit.getLogger().log(Level.INFO, "WorldAPI loaded");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().log(Level.INFO, "WorldAPI unloaded");
    }

    public static Api getAPI() {
        return worldApi;
    }
}
