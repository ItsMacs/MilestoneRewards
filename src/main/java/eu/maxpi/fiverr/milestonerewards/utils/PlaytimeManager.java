package eu.maxpi.fiverr.milestonerewards.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlaytimeManager {

    public static HashMap<String, Long> playTime = new HashMap<>();
    public static HashMap<String, Long> afkCountdown = new HashMap<>();
    public static HashMap<Player, Location> prevLoc = new HashMap<>();


    public static void manage(){
        Bukkit.getOnlinePlayers().forEach(p -> {
            if(!prevLoc.containsKey(p) || prevLoc.get(p).getWorld() != p.getWorld()){
                prevLoc.put(p, p.getLocation());
                return;
            }

            if(prevLoc.get(p).distance(p.getLocation()) < 1){
                if(afkCountdown.getOrDefault(p.getUniqueId().toString(), 0L) >= PluginLoader.afkThreshold) return;
                afkCountdown.put(p.getUniqueId().toString(), afkCountdown.getOrDefault(p.getUniqueId().toString(), 0L) + 1);
            }else{
                afkCountdown.put(p.getUniqueId().toString(), 0L);
                prevLoc.put(p, p.getLocation());
            }

            playTime.put(p.getUniqueId().toString(), playTime.getOrDefault(p.getUniqueId().toString(), 0L) + 1);
        });
    }

    public static long getTime(Player p){
        return playTime.getOrDefault(p.getUniqueId().toString(), 0L);
    }

}
