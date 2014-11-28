package nl.makertim.MMOmain.lib;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.makertim.MMOmain.Refrence;
import nl.makertim.MMOmain.GameWorld;

public class MMOOutlaws{
	private static MMOOutlaws instance;
	public ArrayList<String> que = new ArrayList<String>();
	public static Random random = new Random(2304);
	
	public static MMOOutlaws getInstance(){
		if(instance == null){
			instance = new MMOOutlaws();
		}
		return instance;
	}
		
	public void addMission(Class<? extends Mission> missionClass){
		Refrence.missionTypes.add(missionClass);
		try{
			ArrayList<Class<? extends Mission>> missionTypes = new ArrayList<Class<? extends Mission>>();
			missionTypes.add(missionClass);
			GameWorld.getGameWorld().addClasses(missionTypes);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void removeMission(Class<? extends Mission> missionClass){
		Refrence.missionTypes.remove(missionClass);
		try{
			ArrayList<Mission> missions = new ArrayList<Mission>();
			for(Mission mission : GameWorld.getGameWorld().missions){
				if(!mission.getClass().equals(missionClass)){
					missions.add(mission);
				}else{
					mission.stop();
				}
			}
			GameWorld.getGameWorld().missions = missions;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addSkillHandler(Class<? extends SkillAction> skillHandler){
		try{
			SkillAction sa = skillHandler.newInstance();
			sa.registerEvents(Bukkit.getPluginManager(), Refrence.main);
			Refrence.handlers.add(sa);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sendTitleMessage(Player pl, String title, String subtitle, int duratio){
		//1.8 only
		try{
			throw new Exception();
			/*CraftPlayer player = ((CraftPlayer)pl);
			player.getHandle().playerConnection.sendPacket(null);
			if(title != null){
				title = TitleStringObject.convert(title);
				player.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(Action.TITLE, ChatSerializer.a(title)));
			}
			if(subtitle != null){
				subtitle = TitleStringObject.convert(subtitle);
				player.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(Action.SUBTITLE, ChatSerializer.a(subtitle)));
			}*/
		}catch(Exception ex){
			if(title != null){
				pl.sendMessage(title);
			}
			if(subtitle != null){
				pl.sendMessage(subtitle);
			}
		}
	}
	
	public static int sendActionMessage(Player pl, String message){
		//1.8 only
		
		int protocol = -1;
		try{
			throw new Exception();/*
			protocol = ((CraftPlayer)pl).getHandle().playerConnection.networkManager.getVersion();
			IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
			PacketPlayOutChat ppoc = new PacketPlayOutChat(icbc, 2);
			((CraftPlayer)pl).getHandle().playerConnection.sendPacket(ppoc);*/
		}catch(Exception ex){
			pl.sendMessage(message);
		}
		return protocol;
	}
	
	public static boolean isAdmin(Player pl){
		return pl.isOp() || pl.hasPermission("outlaws.admin");
	}
}
