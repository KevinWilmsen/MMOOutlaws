package nl.makertim.MMOmain.lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import nl.makertim.MMOmain.Lang;
import nl.makertim.MMOmain.MKTEventHandler;
import nl.makertim.MMOmain.PlayerStats;
import nl.makertim.MMOmain.GameWorld;
import nl.makertim.MMOmain.Refrence;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public abstract class Mission implements Listener{
	protected ArrayList<Player> teamA = new ArrayList<Player>();
	protected ArrayList<Player> teamB = new ArrayList<Player>();
	
	protected boolean isLobby = false, inGame = false;
	protected int state = -1, minPlayers = 1, maxPlayers = 10, timer = 0;
	
	
	public static Mission getMissionFromPlayer(Player pl){
		Mission ret = null;
		if(PlayerStats.getPlayerStats(pl).isInMission){
			for(Mission mission : GameWorld.getGameWorld().missions){
				for(Player plm : mission.getAllPlayers()){
					if(plm.equals(pl)){
						ret = mission;
					}
				}
			}
		}
		return ret;
	}
	
	
	public boolean canJoin(PlayerStats pls){
		if(pls.isOutlaw && teamA.size() >= (maxPlayers/2)){
			return false;
		}
		if(!pls.isOutlaw && teamB.size() >= (maxPlayers/2)){
			return false;
		}
		return getAllPlayers().size() < maxPlayers;
	}
	
	public List<Player> getAllPlayers(){
		ArrayList<Player> all = new ArrayList<Player>();
		all.addAll(teamA);
		all.addAll(teamB);
		return all;
	}
	
	public MissionLocation getLocation(boolean isOutlaw){
		return new MissionLocation(new Location(Refrence.theWorld, 486, 70, 55), 4);
	}
	
	public String getName(){
		return Lang.missionNotNamed;
	}
	
	public boolean inLobby(){
		return isLobby;
	}
	
	public void joinPlayer(PlayerStats pls){
		pls.isInMission = true;
		pls.save();
		Player pl = pls.getPlayer();
		MKTEventHandler.cleanupPlayerInventory(pl);
		pl.getInventory().setItem(8, Refrence.slot8i);
		if(pls.isOutlaw){
			teamA.add(pl);
		}else{
			teamB.add(pl);
		}
		showMissionPeople();
		if(pls.isOutlaw){
			pls.getPlayer().getInventory().setHelmet(Refrence.customIS(Material.JACK_O_LANTERN, 1, "Outlaw Helmet", null, null));
		}else{
			pls.getPlayer().getInventory().setHelmet(Refrence.customIS(Material.PUMPKIN, 1, "Police Helmet", null, null));
		}
	}
	
	public void leavePlayer(PlayerStats pls){
		pls.isInMission = false;
		pls.save();
		Player pl = pls.getPlayer();
		teamA.remove(pl);
		teamB.remove(pl);
		pls.resetScoreBoard();
		MKTEventHandler.cleanupPlayerInventory(pl);
		showPeople(pl);
	}
	
	public Material getIcon(){
		return Material.SPONGE;
	}
	
	public void registerEvents(PluginManager pm, Plugin pl){
		pm.registerEvents(this, pl);
	}
	
	public void reward(Player pl, int xp){
		PlayerStats pls = PlayerStats.getPlayerStats(pl);
		pls.addXP(xp);
		pls.save();
	}
	
	protected void sendMessage(String message){
		sendMessage(true, message);
		sendMessage(false, message);
	}
	
	protected void sendMessage(boolean isOutlaw, String message){
		if(isOutlaw){
			for(Player pl : this.teamA){
				MMOOutlaws.sendActionMessage(pl, ChatColor.GOLD + message);
			}
		}else{
			for(Player pl : this.teamB){
				MMOOutlaws.sendActionMessage(pl, ChatColor.GOLD + message);
			}
		}
	}
	
	protected void showMissionPeople(){
		ArrayList<Player> allNotIngame = new ArrayList<Player>(Refrence.getAllPlayers());
		allNotIngame.removeAll(getAllPlayers());
		
		for(Player pl : getAllPlayers()){
			for(Player npl : allNotIngame){
				pl.hidePlayer(npl);
			}
			for(Player ipl : getAllPlayers()){
				pl.showPlayer(ipl);
			}
		}
	}
	
	protected void showPeople(Player pl){
		for(Player apl : Refrence.getAllPlayers()){
			pl.showPlayer(apl);
		}
	}
	
	public void stop(){
		for(Player inGame : new ArrayList<Player>(getAllPlayers())){
			leavePlayer(PlayerStats.getPlayerStats(inGame));
		}
		isLobby = false;
		inGame = false;
		state = -1;
		timer = 0;
	}
	
	public void tick(int i){ //TODO zet dit in title
		for(Player pl : getAllPlayers()){
			PlayerStats pls = PlayerStats.getPlayerStats(pl);
			ArrayList<String> score = new ArrayList<String>();
			score.add(ChatColor.RED.toString() + ChatColor.BOLD + Lang.outlaws); 
			for(Player outlaw : teamA){
				score.add(outlaw.getName());
			}
			for(int j=0; j<minPlayers-teamA.size(); j++){
				score.add(Lang.empty + StringUtils.repeat(" ", j));
			}
			score.add(" ");
			score.add(ChatColor.BLUE.toString() + ChatColor.BOLD + Lang.sheriffs);
			for(Player popo : teamB){
				score.add(popo.getName());
			}
			for(int j=0; j<minPlayers-teamB.size(); j++){
				score.add(Lang.empty + StringUtils.repeat(" ", minPlayers+j));
			}
			score.add("  ");
			score.add(ChatColor.GREEN.toString() + ChatColor.BOLD + Lang.time + " " + ChatColor.RESET + Integer.toString(timer));
			pls.getScoreBoard().setStatistics(score.toArray(new String[score.size()])).setTitle(ChatColor.GOLD + ChatColor.GOLD.toString() + this.getName());
		}
	}
	
	public void updateServerJoinEvent(Player pl){
		for(Player ipl : getAllPlayers()){
			ipl.hidePlayer(pl);
		}
	}
}
