package nl.codecup.daedalus.wrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.codecup.daedalus.objects.Wrapper;

import org.json.JSONArray;
import org.json.JSONObject;

public class WrapperManager{

	private Wrapper[] wrappers;

	public WrapperManager(File config) throws IOException{
		this(WrapperManager.fileToJSON(config));
	}

	public WrapperManager(JSONObject json){
		Map<String,Object> wrapperConverter = new HashMap<>();
		if(json.has("wrappers")){
			JSONObject wrappers = json.getJSONObject("wrappers");
			for(String key : wrappers.keySet()){
				wrapperConverter.put(key,wrappers.getJSONObject(key));
			}
			this.resolveWrappers(wrapperConverter,null);
			int i = 0;
			this.wrappers = new Wrapper[wrapperConverter.size()];
			for(String name : wrapperConverter.keySet()){
				Object o = wrapperConverter.get(name);
				if(o instanceof Wrapper){
					Wrapper w = (Wrapper) o;
					this.wrappers[i] = w;
					i++;
				}
			}
		}
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

	private void resolveWrappers(Map<String,Object> wrapperConverter,String wrapper){
		if(wrapper!=null){
			Object o = wrapperConverter.get(wrapper);
			if(o instanceof JSONObject){
				JSONObject json = (JSONObject) o;
				Wrapper w = null;
				if(json.has("wrapper")){
					String subWrapperName = json.getString("wrapper");
					Object subWrapper = wrapperConverter.get(subWrapperName);
					if(subWrapper instanceof JSONObject){
						this.resolveWrappers(wrapperConverter,subWrapperName);
						subWrapper = wrapperConverter.get(subWrapperName);
					}
					if(subWrapper instanceof Wrapper){
						Wrapper castedSubWrapper = (Wrapper) subWrapper;
						w = this.jsonToWrapper(wrapper,json,castedSubWrapper);
					}
				}else{
					w = this.jsonToWrapper(wrapper,json,null);
				}
				wrapperConverter.replace(wrapper,w);
			}
		}else{
			for(String name : wrapperConverter.keySet()){
				Object o = wrapperConverter.get(name);
				if(o instanceof JSONObject){
					this.resolveWrappers(wrapperConverter,name);
				}
			}
		}
	}

	private Wrapper jsonToWrapper(String name,JSONObject json,Wrapper wrapper){
		String[] commands = {};
		if(json.has("commands")){
			JSONArray commandArr = json.getJSONArray("commands");
			commands = new String[commandArr.length()];
			for(int i=0;i<commands.length;i++){
				commands[i] = commandArr.getString(i);
			}
		}

		String[] patterns = {};
		if(json.has("patterns")){
			JSONArray patternArr = json.getJSONArray("patterns");
			patterns = new String[patternArr.length()];
			for(int i=0;i<patterns.length;i++){
				patterns[i] = patternArr.getString(i);
			}
		}

		if(wrapper==null){
			return new Wrapper(name,commands,patterns);
		}else{
			return new Wrapper(name,commands,patterns,wrapper);
		}
	}

	public Wrapper[] getWrappers(){
		return wrappers;
	}

	public Wrapper[] getWrappers(File f){
		List<Wrapper> wrappers = new ArrayList<>();
		for(Wrapper w : this.wrappers){
			if(w.matches(f)){
				wrappers.add(w);
			}
		}
		return wrappers.toArray(new Wrapper[0]);
	}

	public Wrapper getWrapper(String name){
		for(Wrapper w : this.wrappers){
			if(w.getName().equals(name)){
				return w;
			}
		}
		return null;
	}

}