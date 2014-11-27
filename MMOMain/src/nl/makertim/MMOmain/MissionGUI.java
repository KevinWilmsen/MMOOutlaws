package nl.makertim.MMOmain;

import nl.makertim.MMOmain.lib.MMOOutlaws;
import nl.makertim.MMOmain.lib.Mission;
import nl.makertim.MMOmain.lib.IconMenu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MissionGUI extends IconMenu{
	
	public MissionGUI(Player pl){
		super(Lang.mission, (int)(Refrence.missionTypes.size()/9)+1, Refrence.main);
		int i = 0;
		PlayerStats pls = PlayerStats.getPlayerStats(pl);
		if(pls.isInMission){
			Mission mission = GameWorld.getGameWorld().getMissionPlayer(pl);
			if(mission.inLobby()){
				this.addItem(new ItemHandler(i, Lang.leaveMission, null, new ItemStack(Material.WEB), true){
					Mission mission;
					@Override
					public void onClick(InventoryClickEvent e, IconMenu ic) {
						mission.leavePlayer(PlayerStats.getPlayerStats((Player)e.getWhoClicked()));
					}
					public ItemHandler setMission(Mission mission){
						this.mission = mission;
						return this;
					}
				}.setMission(mission));
			}
		}else{
			for(Mission mission : GameWorld.getGameWorld().getJoinableMissions(PlayerStats.getPlayerStats(pl))){
				this.addItem(new ItemHandler(i++, mission.getName(), null, new ItemStack(mission.getIcon()), true){
					Mission mission;
					@Override
					public void onClick(InventoryClickEvent e, IconMenu ic) {
						PlayerStats pls = PlayerStats.getPlayerStats((Player)e.getWhoClicked());
						if(!pls.isInMission){
							mission.joinPlayer(pls);
						}else{
							MMOOutlaws.sendActionMessage(pls.getPlayer(), ChatColor.RED + Lang.dubbleission);
						}
					}
					public ItemHandler setMission(Mission mission){
						this.mission = mission;
						return this;
					}
				}.setMission(mission));
			}
		}
	}
}
