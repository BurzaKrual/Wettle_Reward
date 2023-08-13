package com.github.burzakrual.minecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("Usage: /link <twitch_username>");
            return true;
        }

        String twitchUsername = args[0];
        String minecraftUsername = player.getName();

        AccountLinkDatabase.linkAccounts(minecraftUsername, twitchUsername);
        player.sendMessage("Your Minecraft account has been linked with your Twitch account: " + twitchUsername);

        return true;
    }
}
