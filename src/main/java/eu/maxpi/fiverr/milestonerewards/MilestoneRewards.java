package eu.maxpi.fiverr.milestonerewards;

import eu.maxpi.fiverr.milestonerewards.commands.MilestoneCMD;
import eu.maxpi.fiverr.milestonerewards.utils.Milestone;
import eu.maxpi.fiverr.milestonerewards.utils.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MilestoneRewards extends JavaPlugin {

    private static MilestoneRewards instance = null;
    public static MilestoneRewards getInstance() { return MilestoneRewards.instance; }
    private static void setInstance(MilestoneRewards in) { MilestoneRewards.instance = in; }

    public static List<Milestone> milestones = new ArrayList<>();

    @Override
    public void onEnable() {
        setInstance(this);

        PluginLoader.load();

        loadTasks();
        loadEvents();
        loadCommands();

        Bukkit.getLogger().info("MilestoneRewards by fiverr.com/macslolz was enabled successfully!");
    }

    private void loadTasks(){
        Bukkit.getScheduler().runTaskTimer(this, () -> milestones.forEach(Milestone::tryGive), 0L, 20L);
    }

    private void loadEvents(){

    }

    private void loadCommands(){
        Objects.requireNonNull(getCommand("milestone")).setExecutor(new MilestoneCMD());
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("MilestoneRewards by fiverr.com/macslolz was disabled successfully!");
    }
}
