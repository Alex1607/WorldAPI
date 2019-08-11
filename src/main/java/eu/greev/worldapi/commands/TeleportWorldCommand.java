package eu.greev.worldapi.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportWorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You need to be a player to run this command");
            return true;
        }

        if (!sender.hasPermission("worldapi.teleportworld")) {
            sender.sendMessage("You dont have the permissions for that command");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Please use the following syntax: /teleportworld <name>");
            return true;
        }

        if (Bukkit.getWorld(args[0]) == null) {
            sender.sendMessage("This world does not exist!");
            return true;
        }

        Player player = (Player) sender;

        player.teleport(new Location(Bukkit.getWorld(args[0]), 0, 100, 0));
        player.setGameMode(GameMode.SPECTATOR);

        return true;
    }
}
