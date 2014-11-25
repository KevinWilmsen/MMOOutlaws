package nl.makertim.MMOmain;

import java.io.File;

import nl.makertim.MMOmain.PlayerUUID.PlayerUUIDLookup;
import nl.makertim.MMOmain.command.RegisterCommands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class bukkitStarter extends JavaPlugin{
	
	@Override
	public void onEnable(){
		Refrence.main = this;
		Refrence.theWorld = Bukkit.getWorlds().get(0);
		Refrence.mainDir = new File(Refrence.main.getDataFolder().toString() + File.separatorChar + "OutlawDB");
		new File(Refrence.main.getDataFolder().toString() + File.separatorChar + "OutlawDB" + File.separatorChar + "playerData").mkdirs();
		Bukkit.getServer().getPluginManager().registerEvents(new MKTEventHandler(), Refrence.main);
		Bukkit.getServer().getPluginManager().registerEvents(new MMOScoreSave(), Refrence.main);
		new GameWorld();
		PlayerUUIDLookup.load();
		new RegisterCommands();
	}
	
	@Override
	public void onDisable(){
		for(PlayerStats pls : Refrence.playerStats){
			pls.isInMission = false;
			pls.save();
			System.out.println(pls.pl.playerName + " Saved");
		}
		PlayerUUIDLookup.save();
		Refrence.commands.clear();
	}
}
