package eu.greev.worldapi;

import eu.greev.worldapi.api.API;
import eu.greev.worldapi.commands.DeleteWorldCommand;
import eu.greev.worldapi.commands.LoadWorldCommand;
import eu.greev.worldapi.commands.TeleportWorldCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldAPI extends JavaPlugin {

    private static API worldAPI = new API();

    @Override
    public void onEnable() {
        getCommand("loadworld").setExecutor(new LoadWorldCommand());
        getCommand("deleteworld").setExecutor(new DeleteWorldCommand());
        getCommand("teleportworld").setExecutor(new TeleportWorldCommand());
        System.out.println("WorldAPI loaded");
    }

    @Override
    public void onDisable() {
        System.out.println("WorldAPI unloaded");
    }

    public static API getAPI() {
        return worldAPI;
    }
}
