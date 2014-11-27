package nl.makertim.MMOmain.command;

import nl.makertim.MMOmain.Refrence;

import org.bukkit.entity.Player;

public class CommandHandler {
	
	private static CommandHandler instance;
	
	public static CommandHandler getInstance(){
		if(instance == null){
			instance = new CommandHandler();
		}
		return instance;
	}	
	private CommandHandler(){}
	
	public boolean triggerCommand(String command, String[] args, Player cmdSender){
		boolean b = false;
		for(Command cmd : Refrence.commands){
			if(cmd.commandName.equalsIgnoreCase(command)){
				b = cmd.onCommand(cmdSender, command, args);
			}
		}
		return b;
	}
	
	public void addCommand(Command cmd){
		Refrence.commands.add(cmd);
	}
}
