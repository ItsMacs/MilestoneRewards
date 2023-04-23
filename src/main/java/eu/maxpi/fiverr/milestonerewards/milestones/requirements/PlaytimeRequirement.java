package eu.maxpi.fiverr.milestonerewards.milestones.requirements;

import eu.maxpi.fiverr.milestonerewards.milestones.Milestone;
import eu.maxpi.fiverr.milestonerewards.utils.PlaytimeManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlaytimeRequirement extends MilestoneRequirement {

    public long playTimeRequired;

    public PlaytimeRequirement() {
        super("playtime");
    }

    @Override
    public boolean isComplete(Player p, Milestone m) {
        return PlaytimeManager.getTime(p) >= (playTimeRequired * (m.timesCompleted.getOrDefault(p.getName(), 0) + 1));
    }

    @Override
    public MilestoneRequirement create(Player p, String[] args) {
        if(args.length < 5) return null;

        long l;
        try{
            l = Long.parseLong(args[4]);
        }catch (Exception e){
            return null;
        }

        PlaytimeRequirement req = new PlaytimeRequirement();
        req.playTimeRequired = l;
        return req;
    }

    @Override
    public void saveData(YamlConfiguration config, String root) {
        config.set(root + ".time", playTimeRequired);
        config.set(root + ".type", type);
    }

    @Override
    public MilestoneRequirement fromData(YamlConfiguration config, String root){
        PlaytimeRequirement req = new PlaytimeRequirement();
        req.playTimeRequired = config.getLong("root" + ".time");
        return req;
    }
}
