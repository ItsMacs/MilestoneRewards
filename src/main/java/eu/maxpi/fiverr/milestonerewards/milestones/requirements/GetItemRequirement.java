package eu.maxpi.fiverr.milestonerewards.milestones.requirements;

import eu.maxpi.fiverr.milestonerewards.milestones.Milestone;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GetItemRequirement extends MilestoneRequirement{

    public ItemStack item;

    public GetItemRequirement() {
        super("getitem");
    }

    @Override
    public boolean isComplete(Player p, Milestone m) {
        return Arrays.stream(p.getInventory().getContents()).anyMatch(i -> i.isSimilar(item));
    }

    @Override
    public MilestoneRequirement create(Player p, String[] args) {
        GetItemRequirement req = new GetItemRequirement();
        req.item = p.getInventory().getItemInMainHand().clone();
        return req;
    }

    @Override
    public void saveData(YamlConfiguration config, String root) {
        config.set(root + ".item", item);
        config.set(root + ".type", type);
    }

    @Override
    public MilestoneRequirement fromData(YamlConfiguration config, String root){
        GetItemRequirement req = new GetItemRequirement();
        req.item = config.getItemStack(root + ".item");
        return req;
    }
}
