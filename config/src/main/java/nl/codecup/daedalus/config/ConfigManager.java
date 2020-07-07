package nl.codecup.daedalus.config;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConfigManager{

	private JSONObject json;

	public ConfigManager(File config) throws IOException{
		this(ConfigManager.fileToJSON(config));
	}

	public ConfigManager(JSONObject json){
		this.json = json;
	}

	public boolean debug(){
		if(this.json.has("debug")){
			return this.json.getBoolean("debug");
		}
		return false;
	}

	public String manager(){
		if(this.json.has("manager")){
			return this.json.getString("manager");
		}
		return null;
	}

	private static JSONObject fileToJSON(File f) throws IOException{
		FileInputStream fis = new FileInputStream(f);
		byte[] buf = new byte[fis.available()];
		if(fis.read(buf)<0){
			return new JSONObject();
		}
		fis.close();
		return new JSONObject(new String(buf));
	}

}