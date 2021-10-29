package eu.greev.worldapi.commands;

import eu.greev.worldapi.WorldAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DeleteWorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("worldapi.deleteworld")) {
            sender.sendMessage("You dont have the permissions for that command");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Please use the following syntax: /deleteworld <name>");
            return true;
        }

        if(Bukkit.getWorld(args[0]) == null) {
            sender.sendMessage("This world does not exist!");
            return true;
        }

        if(!Bukkit.getWorld(args[0]).getPlayers().isEmpty()) {
            sender.sendMessage("You cant delete a world that still has players in it.");
            return true;
        }

        long a = System.currentTimeMillis();

        boolean success = WorldAPI.getAPI().removeWorld(args[0]);
        if (success) {
            sender.sendMessage("Deleted " + args[0] + " in " + (System.currentTimeMillis() - a) + " milliseconds");
        } else {
            sender.sendMessage("The world could not be deleted! Please check if the folder exists.");
        }
        return true;
    }
}
