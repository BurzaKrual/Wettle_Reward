package com.github.burzakrual.minecraft;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.TwitchHelix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import com.github.twitch4j.helix.domain.UserList;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RewardCommandExecutor implements CommandExecutor {
    private final TwitchClient twitchClient;

    public RewardCommandExecutor(TwitchClient twitchClient) {
        this.twitchClient = twitchClient;
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

        rewardRandomViewer(player);

        return true;
    }



    private void rewardRandomViewer(Player sender) {
        // Fetch the list of viewers from Twitch
        List<String> viewers = fetchViewersFromTwitch();

        if (viewers.isEmpty()) {
            sender.sendMessage("No viewers are currently available.");
            return;
        }

        // Randomly select a viewer
        String randomViewer = viewers.get(new Random().nextInt(viewers.size()));

        Player selectedPlayer = sender.getServer().getPlayer(randomViewer);
        if (selectedPlayer != null) {
            selectedPlayer.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
            selectedPlayer.sendMessage("Congratulations! You've received a diamond sword reward!");
        }
    }
    private List<String> fetchViewersFromTwitch() {
        List<String> viewers = new ArrayList<>();

        // Initialize a TwitchHelix instance
        TwitchHelix helix = twitchClient.getHelix();

        // Replace "YOUR_CHANNEL_NAME" with your Twitch channel name
        String channelName = "YOUR_CHANNEL_NAME";

        // Fetch the viewer list for your channel
        ViewerList viewerList = helix.getChatters(channelName);

        // Extract the viewers from the ViewerList
        viewers.addAll(viewerList.getChatters().getViewers());

        return viewers;
    }


