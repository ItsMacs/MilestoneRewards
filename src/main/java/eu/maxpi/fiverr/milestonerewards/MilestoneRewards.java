package eu.maxpi.fiverr.milestonerewards;

import eu.maxpi.fiverr.milestonerewards.commands.MilestoneCMD;
import eu.maxpi.fiverr.milestonerewards.commands.RedeemCMD;
import eu.maxpi.fiverr.milestonerewards.events.onJoin;
import eu.maxpi.fiverr.milestonerewards.milestones.Milestone;
import eu.maxpi.fiverr.milestonerewards.milestones.requirements.GetItemRequirement;
import eu.maxpi.fiverr.milestonerewards.milestones.requirements.MilestoneRequirement;
import eu.maxpi.fiverr.milestonerewards.milestones.requirements.PlaytimeRequirement;
import eu.maxpi.fiverr.milestonerewards.utils.PlaytimeManager;
import eu.maxpi.fiverr.milestonerewards.utils.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class MilestoneRewards extends JavaPlugin {

    private static MilestoneRewards instance = null;
    public static MilestoneRewards getInstance() { return MilestoneRewards.instance; }
    private static void setInstance(MilestoneRewards in) { MilestoneRewards.instance = in; }

    public static HashMap<String, Milestone> milestones = new HashMap<>();

    public static HashMap<String, MilestoneRequirement> availableRequirements = new HashMap<>();


    public static HashMap<Player, Long> lastSpam = new HashMap<>();

    public static HashMap<String, List<ItemStack>> awaiting = new HashMap<>();

    @Override
    public void onEnable() {
        setInstance(this);

        PluginLoader.load();

        loadObjects();

        loadTasks();
        loadEvents();
        loadCommands();

        Bukkit.getLogger().info("MilestoneRewards by fiverr.com/macslolz was enabled successfully!");
    }

    private void loadObjects(){
        availableRequirements.put("playtime", new PlaytimeRequirement());
        availableRequirements.put("getitem", new GetItemRequirement());
    }

    private void loadTasks(){
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            milestones.values().forEach(Milestone::tryGive);
            milestones.values().forEach(Milestone::handout);

            milestones.values().stream().filter(m -> m.isRepeating).forEach(m -> m.completed.values().removeIf(i -> i == 2));
        }, 0L, 10L);

        Bukkit.getScheduler().runTaskTimer(this, PlaytimeManager::manage, 0L, 20L);
    }

    private void loadEvents(){
        Bukkit.getPluginManager().registerEvents(new onJoin(), this);
    }

    private void loadCommands(){
        Objects.requireNonNull(getCommand("milestone")).setExecutor(new MilestoneCMD());
        Objects.requireNonNull(getCommand("redeem")).setExecutor(new RedeemCMD());
    }

    public static void send(Player p){
        if(lastSpam.getOrDefault(p, 0L) + PluginLoader.spamM > ZonedDateTime.now().toEpochSecond()) return;
        if(!lastSpam.containsKey(p)) lastSpam.put(p, ZonedDateTime.now().toEpochSecond());

        if(awaiting.getOrDefault(p.getUniqueId().toString(), new ArrayList<>()).size() == 0){
            lastSpam.remove(p);
            return;
        }

        p.sendMessage(PluginLoader.lang.get("missing-out"));
        p.sendTitle(PluginLoader.lang.get("missing-out-title"), PluginLoader.lang.get("missing-out-subtitle"), 15, 60, 15);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1, 1);
    }

    public static int getEmptySlots(Player p) {
        int i = 0;
        for(ItemStack item : p.getInventory().getContents()) if(item != null && item.getType() != Material.AIR) i++;
        return 36 - i;
    }

    @Override
    public void onDisable() {
        PluginLoader.save();

        Bukkit.getLogger().info("MilestoneRewards by fiverr.com/macslolz was disabled successfully!");
    }
}
