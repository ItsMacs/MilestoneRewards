package eu.maxpi.fiverr.milestonerewards.milestones.requirements;

import eu.maxpi.fiverr.milestonerewards.milestones.Milestone;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class MilestoneRequirement {

    public String type;

    public MilestoneRequirement(String s){
        this.type = s;
    }

    public abstract boolean isComplete(Player p, Milestone m);

    public abstract MilestoneRequirement create(Player p, String[] args);

    public abstract void saveData(YamlConfiguration config, String root);

    public abstract MilestoneRequirement fromData(YamlConfiguration config, String root);

}
