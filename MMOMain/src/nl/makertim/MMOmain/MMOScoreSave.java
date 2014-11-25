package nl.makertim.MMOmain;

import java.util.HashMap;

import nl.makertim.MMOmain.lib.MMOScoreBoard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class MMOScoreSave implements Listener{
	private static HashMap<PlayerStats, MMOScoreBoard> mapping = new HashMap<PlayerStats, MMOScoreBoard>();
	
	public static MMOScoreBoard getScoreBoard(PlayerStats pls){
		if(!mapping.containsKey(pls)){
			mapping.put(pls, new MMOScoreBoard(pls));
		}
		return mapping.get(pls);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onLogin(PlayerLoginEvent e){
		Bukkit.getScheduler().runTaskLater(Refrence.main, new Runnable(){
			Player pl;
			public Runnable setPlayer(Player newPL){
				pl = newPL;
				return this;
			}
			@Override
			public void run(){
				PlayerStats pls = PlayerStats.getPlayerStats(pl);
				if(!mapping.containsKey(pls)){
					mapping.put(pls, new MMOScoreBoard(pls.pl.playerName));
				}
				pls.resetScoreBoard();
			}
		}.setPlayer(e.getPlayer()), 10);
		
	}
}
