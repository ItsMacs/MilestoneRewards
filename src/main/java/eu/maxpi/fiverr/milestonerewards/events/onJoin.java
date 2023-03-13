package eu.maxpi.fiverr.milestonerewards.events;

import eu.maxpi.fiverr.milestonerewards.MilestoneRewards;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoin implements Listener {

    @EventHandler
    public void joinEvent(PlayerJoinEvent event){
        MilestoneRewards.send(event.getPlayer());
    }

}
