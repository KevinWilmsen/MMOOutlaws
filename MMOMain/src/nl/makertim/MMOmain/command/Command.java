package nl.makertim.MMOmain.command;

import org.bukkit.entity.Player;

public abstract class Command {
	public String commandName;
	
	public Command(String name){
		commandName = name;
	}
	
	public abstract void onCommand(Player sender, String command, String[] args);
	
}
