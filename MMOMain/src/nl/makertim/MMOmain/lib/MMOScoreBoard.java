package nl.makertim.MMOmain.lib;

import nl.makertim.MMOmain.PlayerStats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class MMOScoreBoard{
	private Team redTeam, blueTeam;
	public Scoreboard theBoard = Bukkit.getScoreboardManager().getNewScoreboard();
	private Objective obj;
	private String[] oldArray;
	
	public MMOScoreBoard(String naam){
		obj = theBoard.registerNewObjective(naam, "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public MMOScoreBoard(PlayerStats player){
		obj = theBoard.registerNewObjective(player.pl.playerName, "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		addPlayer(player.getPlayer());
	}
	
	public MMOScoreBoard setStatistics(String name){
		setStatistics(new String[]{ name });
		return this;
	}
	
	public MMOScoreBoard setTitle(String name){
		obj.setDisplayName(name);
		return this;
	}
	
	public MMOScoreBoard setStatistics(String[] names){
		if(names == null){
			throw new IllegalArgumentException();
		}
		if(oldArray != null){
			if(names.length == oldArray.length){
				boolean b = true;
				for(int i=0; i<names.length; i++){
					if(names[i] != oldArray[i]){
						b = false;
					}
				}
				if(b){
					return this;
				}
			}for(String str : oldArray){
				theBoard.resetScores(str);
			}
		}
		oldArray = names;
		int j=names.length;
		for(String name : names){
			Score score = obj.getScore(name);
			score.setScore(j--);
		}
		return this;
	}
	
	public MMOScoreBoard clearStatistics(){
		if(oldArray != null){
			for(int i=0; i<oldArray.length; i++){
				theBoard.resetScores(oldArray[i]);
			}
		}
		oldArray = new String[]{};
		return this;
	}
	
	public void addPlayer(Player pl){
		if(pl.getScoreboard() != theBoard){
			pl.setScoreboard(theBoard);
		}
	}
	
	public static void clearPlayer(Player pl){
		pl.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	public MMOScoreBoard setTeam(Boolean isRed, Player pl){
		redTeam = theBoard.getTeam("MMOred");
		blueTeam = theBoard.getTeam("MMOblue");
		if(redTeam == null){
			redTeam = theBoard.registerNewTeam("MMOred");
			redTeam.setPrefix("TEST_");
		}else if(blueTeam == null){
			blueTeam = theBoard.registerNewTeam("MMOblue");
			blueTeam.setPrefix("TEST_");
		}
		if(isRed == null){
			blueTeam.removePlayer(pl);
			redTeam.removePlayer(pl);
		}else if(isRed){
			redTeam.addPlayer(pl);
		}else{
			blueTeam.addPlayer(pl);
		}
		return this;
	}
}
