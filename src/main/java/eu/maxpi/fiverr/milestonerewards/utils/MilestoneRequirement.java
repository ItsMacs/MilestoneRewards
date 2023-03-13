package eu.maxpi.fiverr.milestonerewards.utils;

import org.bukkit.entity.Player;

public abstract class MilestoneRequirement {

    public String type;

    public abstract boolean isComplete(Player p);

    public abstract void create(Player p, String[] args);

}
