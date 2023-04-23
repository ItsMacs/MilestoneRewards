package eu.maxpi.fiverr.milestonerewards.commands;

import eu.maxpi.fiverr.milestonerewards.MilestoneRewards;
import eu.maxpi.fiverr.milestonerewards.milestones.Milestone;
import eu.maxpi.fiverr.milestonerewards.milestones.requirements.MilestoneRequirement;
import eu.maxpi.fiverr.milestonerewards.utils.PlaytimeManager;
import eu.maxpi.fiverr.milestonerewards.utils.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MilestoneCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("milestonerewards.admin")){
            sender.sendMessage(PluginLoader.lang.get("no-permission"));
            return true;
        }

        if(args.length == 0){
            sender.sendMessage(PluginLoader.lang.get("milestone-usage"));
            return true;
        }

        switch (args[0].toLowerCase()){

            case "create" -> { // /milestone create <name> <repeating true/false>
                if(args.length != 3){
                    sender.sendMessage(PluginLoader.lang.get("milestone-usage"));
                    return true;
                }

                if(MilestoneRewards.milestones.containsKey(args[1].toLowerCase())){
                    sender.sendMessage(PluginLoader.lang.get("milestone-already-exists"));
                    return true;
                }

                boolean repeating = args[2].equalsIgnoreCase("true");

                MilestoneRewards.milestones.put(args[1].toLowerCase(), new Milestone(args[1], repeating));
                sender.sendMessage(PluginLoader.lang.get("milestone-created"));
            }

            case "reload" -> {
                PlaytimeManager.afkCountdown.clear();
                PlaytimeManager.playTime.clear();
                PlaytimeManager.prevLoc.clear();

                MilestoneRewards.milestones.clear();
                MilestoneRewards.awaiting.clear();


                PluginLoader.lang.clear();
                PluginLoader.load();

                sender.sendMessage(PluginLoader.lang.get("reloaded"));
            }

            case "setplaytime" -> {
                if(args.length != 3){
                    sender.sendMessage(PluginLoader.lang.get("milestone-usage"));
                    return true;
                }

                Player p = Bukkit.getPlayer(args[1]);
                if(p == null){
                    sender.sendMessage(PluginLoader.lang.get("no-player"));
                    return true;
                }

                long l;
                try{
                    l = Long.parseLong(args[2]);
                }catch (Exception e){
                    sender.sendMessage(PluginLoader.lang.get("milestone-usage"));
                    return true;
                }

                PlaytimeManager.playTime.put(p.getUniqueId().toString(), l);
                sender.sendMessage(PluginLoader.lang.get("playtime-set"));
            }

            case "delete" -> {
                if(args.length != 2){
                    sender.sendMessage(PluginLoader.lang.get("milestone-usage"));
                    return true;
                }

                if(!MilestoneRewards.milestones.containsKey(args[1].toLowerCase())){
                    sender.sendMessage(PluginLoader.lang.get("no-milestone"));
                    return true;
                }

                MilestoneRewards.milestones.remove(args[1].toLowerCase());
                sender.sendMessage(PluginLoader.lang.get("milestone-removed"));
            }

            case "manage" -> {
                if(args.length < 3){
                    sender.sendMessage(PluginLoader.lang.get("milestone-manage-usage"));
                    return true;
                }

                if(!MilestoneRewards.milestones.containsKey(args[1].toLowerCase())){
                    sender.sendMessage(PluginLoader.lang.get("no-milestone"));
                    return true;
                }

                Milestone m = MilestoneRewards.milestones.get(args[1].toLowerCase());

                switch (args[2].toLowerCase()){
                    case "rewards" -> {
                        if(args.length == 3){
                            sender.sendMessage(PluginLoader.lang.get("milestone-rewards-usage"));
                            return true;
                        }

                        switch (args[3].toLowerCase()){
                            case "command" -> {
                                if(args.length == 4){
                                    sender.sendMessage(PluginLoader.lang.get("milestone-rewards-usage"));
                                    return true;
                                }

                                StringBuilder cmd = new StringBuilder();
                                for(int i = 4; i < args.length; i++) cmd.append(args[i]).append(" ");
                                m.commands.add(cmd.toString());
                                sender.sendMessage(PluginLoader.lang.get("reward-added"));
                                return true;
                            }

                            case "item" -> {
                                m.rewards.add(((Player)sender).getInventory().getItemInMainHand().clone());
                                sender.sendMessage(PluginLoader.lang.get("reward-added"));
                                return true;
                            }
                        }
                    }

                    case "requirements" -> {
                        if(args.length == 3){
                            sender.sendMessage(PluginLoader.lang.get("milestone-requirements-usage"));
                            return true;
                        }

                        if(!MilestoneRewards.availableRequirements.containsKey(args[3].toLowerCase())){
                            sender.sendMessage(PluginLoader.lang.get("no-requirement"));
                            return true;
                        }

                        MilestoneRequirement req = MilestoneRewards.availableRequirements.get(args[3].toLowerCase()).create((Player)sender, args);
                        if(req == null) return true;

                        m.requirements.add(req);
                        sender.sendMessage(PluginLoader.lang.get("requirement-added"));
                    }
                }
            }
        }
        return true;
    }
}
