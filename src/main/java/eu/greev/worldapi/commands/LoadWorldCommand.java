package eu.greev.worldapi.commands;

import eu.greev.worldapi.WorldAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.Instant;
import java.util.Optional;

public class LoadWorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("worldapi.loadworld")) {
            sender.sendMessage("You dont have the permissions for that command");
            return true;
        }

        if (args.length != 1 && args.length != 2) {
            sender.sendMessage("Please use the following syntax: /loadworld <path> <name>");
            return true;
        }

        Instant start = Instant.now();

        if (args.length == 2) {
            boolean success = WorldAPI.getAPI().loadMap(new File(args[0]), args[1]);
            if (success) {
                sender.sendMessage(String.format("Loaded %s as %s in %d milliseconds", args[0], args[1], Instant.now().minusMillis(start.toEpochMilli()).toEpochMilli()));
            } else {
                sender.sendMessage("The world could not be loaded! Please check if the file exists.");
            }
        } else {
            Optional<String> worldname = WorldAPI.getAPI().loadMap(new File(args[0]));
            if (worldname.isPresent()) {
                if (sender instanceof Player) {
                    TextComponent message = new TextComponent(String.format("Loaded %s as %s in %d milliseconds", args[0], worldname, Instant.now().minusMillis(start.toEpochMilli()).toEpochMilli()));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, worldname.get()));
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to copy the uuid in your chat.").create()));
                    ((Player) sender).spigot().sendMessage(message);
                } else {
                    sender.sendMessage(String.format("Loaded %s as %s in %d milliseconds", args[0], worldname, Instant.now().minusMillis(start.toEpochMilli()).toEpochMilli()));
                }
            } else {
                sender.sendMessage("The world could not be loaded! Please check if the file exists.");
            }
        }
        return true;
    }
}
