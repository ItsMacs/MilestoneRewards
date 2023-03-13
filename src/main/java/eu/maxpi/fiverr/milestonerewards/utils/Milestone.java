package eu.maxpi.fiverr.milestonerewards.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Milestone {

    public String name;
    public String description;
    public List<ItemStack> rewards = new ArrayList<>();

    //K: Player UUID
    //V: State (0 = Not completed, shouldn't be set; 1 = Completed but objects not redeemed; 2 = Completed and redeemed
    public HashMap<String, Integer> completed = new HashMap<>();


    public Milestone(String name, String description){
        this.name = name;
        this.description = description;
    }

    public void tryGive(){
        Bukkit.getOnlinePlayers().stream().filter(p -> completed.getOrDefault(p.getUniqueId().toString(), 0) == 0).forEach(p -> {

        });
    }

}
