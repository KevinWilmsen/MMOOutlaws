package nl.makertim.MMOmain.lib;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
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
	
	public static int sendActionMessage(Player pl, String message){
		//1.8 only
		int protocol = -1;
		try{
			protocol = ((CraftPlayer)pl).getHandle().playerConnection.networkManager.getVersion();
			IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
			PacketPlayOutChat ppoc = new PacketPlayOutChat(icbc, 2);
			((CraftPlayer)pl).getHandle().playerConnection.sendPacket(ppoc);
		}catch(Exception ex){
			pl.sendMessage(message);
			System.out.println("THERE WAS AN ERROR - PLAYER " + pl.getName() + " PROTOCOL " + protocol);
			ex.printStackTrace();
		}
		return protocol;
	}
	
	public static boolean isAdmin(Player pl){
		return pl.isOp() || pl.hasPermission("outlaws.admin");
	}
}
