package nl.makertim.MMOmain.command;

import nl.makertim.MMOmain.MKTEventHandler;
import nl.makertim.MMOmain.PlayerStats;
import nl.makertim.MMOmain.Refrence;
import nl.makertim.MMOmain.GameWorld;
import nl.makertim.MMOmain.lib.MMOOutlaws;
import nl.makertim.MMOmain.lib.Mission;

import org.bukkit.entity.Player;

public class RegisterCommands{
	
	public RegisterCommands(){
		registerCommand(new Command("Test"){
			@Override
			public void onCommand(Player sender, String command, String[] args) {
				PlayerStats.getPlayerStats(sender).getScoreBoard().setTeam(Refrence.random.nextBoolean(), sender);
			}
		});
		registerCommand(new Command("ReJoin"){
			@Override
			public void onCommand(Player sender, String command, String[] args) {
				if(MMOOutlaws.isAdmin(sender) && Mission.getMissionFromPlayer(sender)==null){
					sender.sendMessage("Cleand up your inventory!");
					MKTEventHandler.cleanupPlayerInventory(sender);
				}
			}
		});
		registerCommand(new Command("ReStats"){
			@Override
			public void onCommand(Player sender, String command, String[] args) {
				if(MMOOutlaws.isAdmin(sender)){
					PlayerStats pls = PlayerStats.getPlayerStats(sender);
					pls.xp = 0;
					pls.theTree.Reset();
					pls.resetScoreBoard();
				}
			}
		});
		registerCommand(new Command("ReScore"){
			@Override
			public void onCommand(Player sender, String command, String[] args) {
				PlayerStats.getPlayerStats(sender).resetScoreBoard();
			}
		});
		registerCommand(new Command("ReMission"){
			@Override
			public void onCommand(Player sender, String command, String[] args) {
				Mission ms = GameWorld.getGameWorld().getMissionPlayer(sender);
				if(ms != null){
					sender.sendMessage(ms.toString());
				}
			}
		});
		registerCommand(new Command("PlayerStats"){
			@Override
			public void onCommand(Player sender, String command, String[] args) {
				if(MMOOutlaws.isAdmin(sender)){
					sender.sendMessage(PlayerStats.getPlayerStats(sender).toString());
					MKTEventHandler.cleanupPlayerInventory(sender);
				}
			}
		});
		registerCommand(new Command("LevelUp"){
			@Override
			public void onCommand(Player sender, String command, String[] args){
				if(MMOOutlaws.isAdmin(sender)){
					PlayerStats pls = PlayerStats.getPlayerStats(sender);
					pls.theTree.addLevel();
					sender.sendMessage("Level added! Level " + pls.theTree.getTotalLVL());
					pls.save();
				}
			}
		});
		registerCommand(new Command("MMOVersion"){
			@Override
			public void onCommand(Player sender, String command, String[] args){
				sender.sendMessage("Name    - " + Refrence.main.getName());
				sender.sendMessage("Version - " + Refrence.main.getDescription().getVersion());
				sender.sendMessage("Plugin Made By - MakerTim");
				sender.sendMessage(" ");
				sender.sendMessage("Concept Made By - iJordiii");
			}
		});
	}
	
	public static void registerCommand(Command cmd){
		CommandHandler.getInstance().addCommand(cmd);
	}
}
