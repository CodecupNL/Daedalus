package nl.codecup.daedalus.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.json.JSONObject;

public class Packet{

	public static String COMMAND_LOG = "LOG";

	public static String COMMAND_MANAGER_LIST = "MANAGER_LIST";

	//Actions
	public static String COMMAND_DAEDALUS_START = "DAEDALUS_START";
	public static String COMMAND_MANAGER_START = "MANAGER_START";
	public static String COMMAND_REFEREE_START = "REFEREE_START";
	public static String COMMAND_PLAYER_START = "PLAYER_START";

	public static String COMMAND_MANAGER_STOP = "MANAGER_STOP";
	public static String COMMAND_REFEREE_STOP = "REFEREE_STOP";
	public static String COMMAND_PLAYER_STOP = "PLAYER_STOP";

	public static String COMMAND_BATTLE_CREATE = "BATTLE_CREATE";
	public static String COMMAND_BATTLE_GAME_LIST = "BATTLE_GAME_LIST";
	public static String COMMAND_BATTLE_GAME_SET = "BATTLE_GAME_SET";
	public static String COMMAND_BATTLE_TIME_SET = "BATTLE_TIME_SET";
	public static String COMMAND_BATTLE_REFEREE_LIST = "BATTLE_REFEREE_LIST";
	public static String COMMAND_BATTLE_REFEREE_SET = "BATTLE_REFEREE_SET";
	public static String COMMAND_BATTLE_PLAYER_LIST = "BATTLE_PLAYER_LIST";
	public static String COMMAND_BATTLE_PLAYER_ADD = "BATTLE_PLAYER_ADD";
	public static String COMMAND_BATTLE_START = "BATTLE_START";
	public static String COMMAND_BATTLE_STOP = "BATTLE_STOP";

	public static String COMMAND_BATTLE_RESULT = "BATTLE_RESULT";

	public static String COMMAND_REFEREE_STEP = "REFEREE_STEP";
	public static String COMMAND_REFEREE_LISTEN = "REFEREE_LISTEN";

	private static final String KEY_COMMAND = "command";
	private static final String KEY_DATA = "data";
	private static final String KEY_SUCCESS = "success";

	private final String command;
	private final Map<String,Object> data;
	private final Boolean success;

	public Packet(String command){
		this(command,null,null);
	}

	public Packet(String command,Map<String,Object> data){
		this(command,data,null);
	}

	public Packet(String command,Boolean success){
		this(command,null,success);
	}

	public Packet(String command,Map<String,Object> data,Boolean success){
		this.command = command;
		this.data = data;
		this.success = success;
	}

	public String getCommand(){
		return this.command;
	}

	public Map<String,Object> getData(){
		return this.data;
	}

	public boolean hasData(){
		return this.data!=null;
	}

	public boolean isResponse(){
		return this.success!=null;
	}

	public boolean isSuccess(){
		return this.success;
	}

	public boolean isFailure(){
		return !this.isSuccess();
	}

	public JSONObject toJSON(){
		JSONObject obj = new JSONObject();
		obj.put(Packet.KEY_COMMAND,this.command);
		if(this.hasData()){
			JSONObject data = new JSONObject();
			for(Map.Entry<String,Object> entry : this.data.entrySet()){
				data.put(entry.getKey(),entry.getValue());
			}
			obj.put(Packet.KEY_DATA,data);
		}
		if(this.isResponse()){
			obj.put(Packet.KEY_SUCCESS,this.isSuccess());
		}
		return obj;
	}

	public String toJSONString(){
		return this.toJSON().toString();
	}

	public void toStream(OutputStream out) throws IOException{
		String str = this.toJSONString()+"\r\n";
		out.write(str.getBytes());
		out.flush();
	}

	@Override
	public String toString() {
		return "Packet{" +
				"command='" + command + '\'' +
				", data=" + data +
				", success=" + success +
				'}';
	}

	public static Packet fromJSON(JSONObject obj){
		String command = null;
		Map<String,Object> data = null;
		Boolean success = null;
		if(obj.has(Packet.KEY_COMMAND)){
			command = obj.getString(Packet.KEY_COMMAND);
		}
		if(obj.has(Packet.KEY_DATA)){
			JSONObject dataObj = obj.getJSONObject(Packet.KEY_DATA);
			data = new HashMap<>();
			for(String key : dataObj.keySet()){
				data.put(key,dataObj.get(key));
			}
		}
		if(obj.has(Packet.KEY_SUCCESS)){
			success = obj.getBoolean(Packet.KEY_SUCCESS);
		}
		if(command==null){
			throw new RuntimeException("Missing command");
		}
		if(data!=null && success!=null){
			return new Packet(command,data,success);
		}
		if(data!=null){
			return new Packet(command,data);
		}
		if(success!=null){
			return new Packet(command,success);
		}
		return new Packet(command);
	}

	public static Packet fromJSONString(String str){
		JSONObject obj = new JSONObject(str);
		return Packet.fromJSON(obj);
	}

	public static Packet fromStream(LineInputStream in) throws IOException{
		byte[] bytes = in.readLine(true,true,true,false);
		String str = new String(bytes).trim();
		return Packet.fromJSONString(str);
	}

}