package nl.makertim.MMOmain;

import java.io.File;

import nl.makertim.MMOmain.lib.MMOScoreBoard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;

public class PlayerStats{
	public PlayerUUID pl;
	public SkillTree theTree = new SkillTree();
	public double xp = 0, won = 0, rounds = 0;
	public int money = 100;
	public boolean isInMission = false, isOutlaw = false;
	
	public PlayerStats(PlayerUUID player){
		pl = player;
		PlayerStats pls = load(player);
		if(pls != null){
			addToRefrence(pls);
		}else{
			addToRefrence(this);
			save();
		}
	}
	
	public static PlayerStats getPlayerStats(Player pl){
		PlayerStats ret = null;
		for(PlayerStats pls : Refrence.playerStats){
			if(pls.equals(pl)){
				ret = pls;
			}
		}
		if(ret == null){
			ret = new PlayerStats(new PlayerUUID(pl));
		}
		return ret;
	}
	
	public MMOScoreBoard getScoreBoard(){
		return MMOScoreSave.getScoreBoard(this);
	}
	
	public void resetScoreBoard(){
		try{
			MMOScoreBoard score = getScoreBoard();
			score.addPlayer(this.getPlayer());
			score.setStatistics(new String[]{
					ChatColor.RED.toString() + ChatColor.BOLD + "Wanted Level",
					"★☆☆☆☆",
					"",
					ChatColor.GOLD.toString() + ChatColor.BOLD + "Money",
					"€ " + money,
					" ",
					ChatColor.GREEN.toString() + ChatColor.BOLD + "Status",
					(isOutlaw ? "Outlaw" : "Sherrif") + " Level " + this.theTree.getTotalLVL(),
					"XP " + Math.round(xpProcent()*10)/10 + "%",
					
			}).setTitle(pl.playerName);
		}catch(Exception ex){}
	}

	public double xpProcent(){
		return (this.xp / (theTree.getTotalLVL() * 1024)) * 100;
	}
	
	public void addXP(int xp){
		this.xp += xp;
		if(this.xp >= (theTree.getTotalLVL() * 1024)){
			this.xp -= (theTree.getTotalLVL() * 1024);
			theTree.addLevel();
		}
	}
	
	public Player getPlayer(){
		return Bukkit.getPlayer(pl.playerUUID);
	}
	
	public void addToRefrence(PlayerStats pls){
		if(!Refrence.playerStats.contains(pls)){
			Refrence.playerStats.add(pls);
		}
	}
	
	public void removeFromRefrence(){
		if(Refrence.playerStats.contains(this)){
			Refrence.playerStats.remove(this);
		}
	}
	
	public void save(){
		File uuidDB = new File(Refrence.mainDir.toString() + File.separatorChar + "playerData" + File.separatorChar + this.pl.playerUUID + ".json");
		uuidDB.delete();
		try{
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(this);
			Utils.saveToFile(uuidDB, new String[]{json});
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void quickSave(){
		File uuidDB = new File(Refrence.mainDir.toString() + File.separatorChar + "playerData" + File.separatorChar + this.pl.playerUUID + ".json");
		uuidDB.delete();
		try{
			Gson gson = new GsonBuilder().create();
			String json = gson.toJson(this);
			Utils.saveToFile(uuidDB, new String[]{json});
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static PlayerStats load(PlayerUUID pl){
		PlayerStats pls = null;
		File uuidDB = new File(Refrence.mainDir.toString() + File.separatorChar + "playerData" + File.separatorChar + pl.playerUUID + ".json");
		try{
			String json = Utils.readFromFile(uuidDB);
			Gson gson = new GsonBuilder().create();
			pls = gson.fromJson(json, PlayerStats.class);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return pls;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Player){
			return pl.playerUUID.equals(((Player)obj).getUniqueId());
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString(){
		return new GsonBuilder().setPrettyPrinting().create().toJson(this);
	}
}
