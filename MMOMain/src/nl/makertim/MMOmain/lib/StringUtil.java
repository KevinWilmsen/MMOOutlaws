package nl.makertim.MMOmain.lib;

public class StringUtil{
	
	public static String repeat(String str, int amount){
		String ret = "";
		for(int i=0; i<amount; i++){
			ret+=str;
		}
		return ret;
	}
	
}
