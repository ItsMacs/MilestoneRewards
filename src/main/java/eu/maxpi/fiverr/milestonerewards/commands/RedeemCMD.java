package eu.maxpi.fiverr.milestonerewards.commands;

import eu.maxpi.fiverr.milestonerewards.MilestoneRewards;
import eu.maxpi.fiverr.milestonerewards.milestones.requirements.MilestoneRequirement;
import eu.maxpi.fiverr.milestonerewards.utils.PluginLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class RedeemCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<ItemStack> l = MilestoneRewards.awaiting.getOrDefault(((Player)sender).getUniqueId().toString(), new ArrayList<>());
        if(l.size() == 0) return true;

        for(int i = 0; i < MilestoneRewards.getEmptySlots((Player)sender); i++){
            if(i >= l.size()) break;

            ((Player)sender).getInventory().addItem(l.remove(i));
        }

        sender.sendMessage(PluginLoader.lang.get(l.size() == 0 ? "redeemed" : "redeemed-not-full"));
        return true;
    }

}
