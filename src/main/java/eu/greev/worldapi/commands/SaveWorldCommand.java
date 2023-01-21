package eu.greev.worldapi.commands;

import eu.greev.worldapi.WorldAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.time.Instant;

public class SaveWorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("worldapi.saveworld")) {
            sender.sendMessage("You dont have the permissions for that command");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("Please use the following syntax: /saveworld <world> <path> (with .zip)");
            return true;
        }

        if(Bukkit.getWorld(args[0]) == null) {
            sender.sendMessage("This world does not exist!");
            return true;
        }

        Instant start = Instant.now();

        Bukkit.getWorld(args[0]).save();
        boolean success = WorldAPI.getAPI().saveMap(args[0], new File(args[1]), true);
        if (success) {
            sender.sendMessage(String.format("Saved %s as %s in %d milliseconds", args[0], args[1], Instant.now().minusMillis(start.toEpochMilli()).toEpochMilli()));
        } else {
            sender.sendMessage("The world could not be saved!");
        }
        return true;
    }
}
