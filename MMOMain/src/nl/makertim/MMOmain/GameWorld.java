package nl.makertim.MMOmain;

import java.util.ArrayList;

import nl.makertim.MMOmain.lib.Mission;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;

public class GameWorld implements Runnable{
	public ArrayList<Mission> missions = new ArrayList<Mission>();
	
	public int tick = 0, randomInt = 0;
	
	public GameWorld(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Refrence.main, this, 10L, 1L);
		addClasses(Refrence.missionTypes);
	}
	
	public void addClasses(ArrayList<Class<? extends Mission>> missionTypes){
		PluginManager pm = Bukkit.getPluginManager();
		for(Class<? extends Mission> missionClass : missionTypes){
			try{
				boolean DDouble = false;
				for(Mission mission : missions){
					if(mission.getClass().equals(missionClass)){
						DDouble = true;
					}
				}
				if(DDouble){
					continue;
				}
				Mission mission = missionClass.newInstance();
				mission.registerEvents(pm, Refrence.main);
				missions.add(mission);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<Mission> getJoinableMissions(PlayerStats pls){
		ArrayList<Mission> ret = new ArrayList<Mission>();
		for(Mission mission : missions){
			try{
				if(pls.getPlayer() == null){
					Refrence.playerStats.remove(pls);
					return ret;
				}
				if(mission.canJoin(pls)){
					if(mission.getLocation(pls.isOutlaw).isInsideRange(pls.getPlayer().getLocation())){
						ret.add(mission);
					}
				}
			}catch(Exception ex){
				if(mission == null){
					System.out.println("mission bug");
				}else if(pls == null){
					System.out.println("playerStats bug");
				}else if(pls.getPlayer()== null){
					System.out.println("player bug");
				}
			}
		}
		return ret;
	}
	
	public Mission getMissionPlayer(Player pl){
		Mission ret = null;
		for(Mission mission : missions){
			boolean hasPlayer = false;
			for(Player misPl : mission.getAllPlayers()){
				if(misPl.getName().equalsIgnoreCase(pl.getName())){
					hasPlayer = true;
				}
				if(hasPlayer){
					ret = mission;
				}
			}
		}
		return ret;
	}
	
	public static GameWorld getGameWorld(){
		return Refrence.getWorld();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run(){
		tick++;
		tick%=20;
		for(Mission mission : missions){
			try{
				if(tick == 0 || tick == 10){
					for(Player pl : Bukkit.getOnlinePlayers()){ //BUKKIT WHY
						PlayerInventory pli = pl.getInventory();
						PlayerStats pls = PlayerStats.getPlayerStats(pl);
						Material mat8 = null;
						if(pli.getItem(8) == null){
							 continue;
						}
						mat8 = pli.getItem(8).getType();
						if(mat8.equals(Material.COMPASS) && !pls.isInMission){
							if(randomInt++ % 20 == 0){
								randomInt = 0;
								pls.resetScoreBoard();
							}if(getJoinableMissions(pls).size() > 0){
								pli.setItem(8, Refrence.slot8S);
								pl.setCompassTarget(mission.getLocation(pls.isOutlaw).center);
								continue;
							}else{
								pli.setItem(8, Refrence.slot8);
								if(mission.canJoin(pls)){
									pl.setCompassTarget(mission.getLocation(pls.isOutlaw).center);
								}
							}
						}
					}
				}
				mission.tick(tick);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
