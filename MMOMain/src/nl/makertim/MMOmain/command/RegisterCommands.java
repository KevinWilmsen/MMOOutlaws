package nl.makertim.MMOmain.command;

import nl.makertim.MMOmain.Lang;
import nl.makertim.MMOmain.MKTEventHandler;
import nl.makertim.MMOmain.PlayerStats;
import nl.makertim.MMOmain.Refrence;
import nl.makertim.MMOmain.GameWorld;
import nl.makertim.MMOmain.lib.MMOOutlaws;
import nl.makertim.MMOmain.lib.Mission;

import org.bukkit.entity.Player;

public class RegisterCommands{
	
	public RegisterCommands(){
		registerCommand(new Command(Lang.commandTest){
			@Override
			public boolean onCommand(Player sender, String command, String[] args){
				if(MMOOutlaws.isAdmin(sender)){
					sender.sendMessage("No Test Found Exception");
					return true;
				}
				return false;
			}
		});
		registerCommand(new Command(Lang.commandInv){
			@Override
			public boolean onCommand(Player sender, String command, String[] args) {
				if(MMOOutlaws.isAdmin(sender) && Mission.getMissionFromPlayer(sender)==null){
					sender.sendMessage("Cleand up your inventory!");
					MKTEventHandler.cleanupPlayerInventory(sender);
					return true;
				}
				return false;
			}
		});
		registerCommand(new Command(Lang.commandStats){
			@Override
			public boolean onCommand(Player sender, String command, String[] args) {
				if(MMOOutlaws.isAdmin(sender)){
					PlayerStats pls = PlayerStats.getPlayerStats(sender);
					pls.xp = 0;
					pls.theTree.Reset();
					pls.resetScoreBoard();
					return true;
				}
				return false;
			}
		});
		registerCommand(new Command(Lang.commandBoard){
			@Override
			public boolean onCommand(Player sender, String command, String[] args) {
				PlayerStats.getPlayerStats(sender).resetScoreBoard();
				return true;
			}
		});
		registerCommand(new Command(Lang.commandMission){
			@Override
			public boolean onCommand(Player sender, String command, String[] args) {
				Mission ms = GameWorld.getGameWorld().getMissionPlayer(sender);
				if(ms != null){
					sender.sendMessage(ms.toString());
					return true;
				}
				return false;
			}
		});
		registerCommand(new Command(Lang.commandPLStats){
			@Override
			public boolean onCommand(Player sender, String command, String[] args) {
				if(MMOOutlaws.isAdmin(sender)){
					sender.sendMessage(PlayerStats.getPlayerStats(sender).toString());
					return true;
				}
				return false;
			}
		});
		registerCommand(new Command(Lang.commandLVL){
			@Override
			public boolean onCommand(Player sender, String command, String[] args){
				if(MMOOutlaws.isAdmin(sender)){
					PlayerStats pls = PlayerStats.getPlayerStats(sender);
					pls.theTree.addLevel();
					sender.sendMessage("Level added! Level " + pls.theTree.getTotalLVL());
					pls.save();
					return true;
				}
				return false;
			}
		});
		registerCommand(new Command(Lang.commandVersion){
			@Override
			public boolean onCommand(Player sender, String command, String[] args){
				sender.sendMessage("Name    - " + Refrence.main.getName());
				sender.sendMessage("Version - " + Refrence.main.getDescription().getVersion());
				sender.sendMessage("Plugin Made By - MakerTim");
				sender.sendMessage(" ");
				sender.sendMessage("Concept Made By - iJordiii");
				return true;
			}
		});
	}
	
	public static void registerCommand(Command cmd){
		CommandHandler.getInstance().addCommand(cmd);
	}
}
