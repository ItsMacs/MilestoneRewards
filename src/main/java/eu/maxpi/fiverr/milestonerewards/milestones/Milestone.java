package eu.maxpi.fiverr.milestonerewards.milestones;

import eu.maxpi.fiverr.milestonerewards.MilestoneRewards;
import eu.maxpi.fiverr.milestonerewards.milestones.requirements.MilestoneRequirement;
import eu.maxpi.fiverr.milestonerewards.utils.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Milestone {

    public String name;
    public List<ItemStack> rewards = new ArrayList<>();
    public List<String> commands = new ArrayList<>();

    //K: Player UUID
    //V: State (0 = Not completed, shouldn't be set; 1 = Completed but objects not redeemed; 2 = Completed and redeemed
    public HashMap<String, Integer> completed = new HashMap<>();

    public List<MilestoneRequirement> requirements = new ArrayList<>();

    public Milestone(String name){
        this.name = name;
    }

    public void tryGive(){
        if(requirements.size() == 0) return;

        Bukkit.getOnlinePlayers().stream().filter(p -> completed.getOrDefault(p.getUniqueId().toString(), 0) == 0).forEach(p -> {
            if(requirements.stream().anyMatch(req -> !req.isComplete(p))) return;

            completed.put(p.getUniqueId().toString(), 1);
            p.sendMessage(PluginLoader.lang.get("milestone-reached").replace("%milestone%", name));
            p.sendTitle(PluginLoader.lang.get("milestone-reached-title"), PluginLoader.lang.get("milestone-reached-subtitle"), 15, 60, 15);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        });
    }

    public void handout(){
        Bukkit.getOnlinePlayers().stream().filter(p -> completed.getOrDefault(p.getUniqueId().toString(), 0) == 1).forEach(p -> {
            completed.put(p.getUniqueId().toString(), 2);

            commands.forEach(s -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", p.getName()).replace("%uuid%", p.getUniqueId().toString()));
            });

            if(MilestoneRewards.getEmptySlots(p) < rewards.size()) {
                MilestoneRewards.send(p);

                List<ItemStack> l = MilestoneRewards.awaiting.getOrDefault(p.getUniqueId().toString(), new ArrayList<>());
                l.addAll(rewards);

                MilestoneRewards.awaiting.put(p.getUniqueId().toString(), l);
                return;
            }
            rewards.forEach(i -> p.getInventory().addItem(i));
        });
    }

}
