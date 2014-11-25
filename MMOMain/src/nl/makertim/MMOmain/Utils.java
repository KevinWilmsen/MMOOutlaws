package nl.makertim.MMOmain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class Utils{
	
	public static boolean saveToFile(File out, String[] toWrite){
		try{
			PrintWriter writer = new PrintWriter(out, "UTF-8");
			for(int i=0; i<toWrite.length; i++){
				writer.println(toWrite[i]);
			}
			writer.close();
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static String readFromFile(File in){
		String input = "";
		try{
			BufferedReader reader = new BufferedReader(new FileReader(in));
			String line;
			while((line = reader.readLine()) != null){
				input += line;
				input += System.lineSeparator();
			}
			reader.close();
		}catch(Exception ex){
			try{
				in.createNewFile();
			}catch(Exception ex2){}
		}
		return input;
	}
}
