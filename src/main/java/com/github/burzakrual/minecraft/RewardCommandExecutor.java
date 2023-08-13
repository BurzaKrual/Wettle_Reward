package com.github.burzakrual.minecraft;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelJoinEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.ChannelLeaveEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelJoinEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.ChannelLeaveEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RewardCommandExecutor implements CommandExecutor {
    private final TwitchClient twitchClient;
    private final List<String> activeChatters;

    public RewardCommandExecutor(TwitchClient twitchClient) {
        this.twitchClient = twitchClient;
        this.activeChatters = new ArrayList<>();

        // Register event listeners
        twitchClient.getChat().getEventManager().onEvent(ChannelJoinEvent.class, this::handleJoinEvent);
        twitchClient.getChat().getEventManager().onEvent(ChannelLeaveEvent.class, this::handleLeaveEvent);
        twitchClient.getChat().getEventManager().onEvent(ChannelMessageEvent.class, this::handleMessageEvent);
    }

    private void handleJoinEvent(ChannelJoinEvent event) {
        activeChatters.add(event.getUser().getName());
    }

    private void handleLeaveEvent(ChannelLeaveEvent event) {
        activeChatters.remove(event.getUser().getName());
    }

    private void handleMessageEvent(ChannelMessageEvent event) {
        String username = event.getUser().getName();
        if (!activeChatters.contains(username)) {
            activeChatters.add(username);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.isOp()) {
            player.sendMessage("You must be an op to use this command.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("rewarduser")) {
            rewardRandomViewer(player);
            return true;
        }

        player.sendMessage("Usage: /rewarduser");
        return true;
    }

    private void rewardRandomViewer(Player sender) {
        if (activeChatters.isEmpty()) {
            sender.sendMessage("No active chatters to reward.");
            return;
        }

        String randomViewer = activeChatters.get(new Random().nextInt(activeChatters.size()));
        Player selectedPlayer = sender.getServer().getPlayer(randomViewer);

        if (selectedPlayer != null) {
            String twitchUsername = AccountLinkDatabase.getTwitchUsername(selectedPlayer.getName());
            if (twitchUsername != null) {
                selectedPlayer.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
                selectedPlayer.sendMessage("Congratulations! You've received a diamond sword reward!");
            } else {
                selectedPlayer.sendMessage("Sorry, you are not linked to a Twitch account.");
            }
        }
    }
}
