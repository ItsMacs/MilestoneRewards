package eu.maxpi.fiverr.milestonerewards.utils;

import eu.maxpi.fiverr.milestonerewards.MilestoneRewards;
import eu.maxpi.fiverr.milestonerewards.milestones.Milestone;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PluginLoader {

    public static HashMap<String, String> lang = new HashMap<>();

    public static long spamM;
    public static long afkThreshold;

    public static void load(){
        MilestoneRewards.getInstance().saveResource("config.yml", false);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(MilestoneRewards.getInstance().getDataFolder() + "/config.yml"));
        config.getConfigurationSection("lang").getKeys(false).forEach(s -> lang.put(s, ColorTranslator.translate(config.getString("lang." + s))));

        spamM = config.getLong("spam-interval");
        afkThreshold = config.getLong("afk-threshold");

        YamlConfiguration storage = YamlConfiguration.loadConfiguration(new File(MilestoneRewards.getInstance().getDataFolder() + "/storage.yml"));
        storage.getKeys(false).forEach(s -> {
            if(s.equalsIgnoreCase("leftover")) return;
            if(s.equalsIgnoreCase("playtime")) return;

            Milestone m = new Milestone(s);
            if(storage.contains(s + ".req")) storage.getConfigurationSection(s + ".req").getKeys(false).forEach(re -> {
                m.requirements.add(MilestoneRewards.availableRequirements.get(storage.getString(s + ".req." + re + ".type")).fromData(storage, s + ".req." + re));
            });

            if(storage.contains(s + ".items")) storage.getConfigurationSection(s + ".items").getKeys(false).forEach(i -> {
                m.rewards.add(storage.getItemStack(s + ".items." + i));
            });

            if(storage.contains(s + ".players")) storage.getConfigurationSection(s + ".players").getKeys(false).forEach(i -> {
                m.completed.put(i, storage.getInt(s + ".players." + i));
            });

            m.commands = storage.getStringList(s + ".commands");
            MilestoneRewards.milestones.put(s, m);
        });

        if(storage.contains("leftover")) storage.getConfigurationSection("leftover").getKeys(false).forEach(s -> {
            List<ItemStack> items = new ArrayList<>();
            storage.getConfigurationSection("leftover." + s).getKeys(false).forEach(item -> {
                items.add(storage.getItemStack("leftover." + s + "." + item));
            });

            MilestoneRewards.awaiting.put(s, items);
        });

        if(storage.contains("playtime")) storage.getConfigurationSection("playtime").getKeys(false).forEach(s -> {
            PlaytimeManager.playTime.put(s, storage.getLong("playtime." + s));
        });
    }

    public static void save(){
        YamlConfiguration storage = new YamlConfiguration();

        PlaytimeManager.playTime.forEach((s, l) -> storage.set("playtime." + s, l));

        MilestoneRewards.awaiting.forEach((s, l) -> {
            AtomicInteger i = new AtomicInteger(0);
            l.forEach(item -> storage.set("leftover." + s + "." + i.getAndIncrement(), item));
        });

        MilestoneRewards.milestones.values().forEach(m -> {
            AtomicInteger i = new AtomicInteger(0);
            m.requirements.forEach(req -> req.saveData(storage, m.name + ".req." + i.getAndIncrement()));

            i.set(0);
            m.rewards.forEach(item -> storage.set(m.name + ".items." + i.getAndIncrement(), item));

            m.completed.forEach((p, c) -> storage.set(m.name + ".players." + p, c));
            storage.set(m.name + ".commands", m.commands);
        });

        try{
            storage.save(new File(MilestoneRewards.getInstance().getDataFolder() + "/storage.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
