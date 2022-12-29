package nl.codecup.daedalus.manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.codecup.daedalus.objects.Log;
import nl.codecup.daedalus.protocol.IO;
import nl.codecup.daedalus.protocol.LineInputStream;
import nl.codecup.daedalus.protocol.Packet;
import nl.codecup.daedalus.protocol.PacketListener;

public class DaedalusManager implements PacketListener,Runnable{

	private static final String TAG = "MANAGER";

	public static boolean started = false;

	private boolean isRunning = true;

	private List<String[]> battleList = new ArrayList<>();
	private int battleIndex = 0;

	@Override
	public void run(){
		this.loadManagerFile();
		this.loop(IO.STDIN);
	}

	private void loadManagerFile(){
		Log.debug(TAG,"Loading battle file");
		try{
			LineInputStream in = new LineInputStream(new FileInputStream("manager.txt"));
			byte[] line;
			while((line = in.readLine(false,false,true,false))!=null){
				String lineStr = new String(line).trim();
				if(lineStr.isEmpty()){
					continue;
				}
				if(lineStr.startsWith("#")){
					continue;
				}
				String[] items = lineStr.split("\t");
				List<String> item = new ArrayList<>();
				for(String str : items){
					if(!str.isEmpty()){
						item.add(str);
					}
				}
				battleList.add(item.toArray(new String[0]));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Log.debug(TAG,"Loaded battle file: "+this.battleList.size()+" battles");
	}

	private void loop(LineInputStream in){
		Log.debug(TAG,"Joining loop");
		while(this.isRunning){
			try{
				if(in.available()>0){
					Log.debug(DaedalusManager.TAG,"Packet received");
					Packet packet = null;
					try{
						packet = Packet.fromStream(in);
					}catch(Exception e){
						Log.error(DaedalusManager.TAG,"Invalid packet");
					}
					if(packet!=null){
						this.onReceivePacket(packet);
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		Log.debug(DaedalusManager.TAG,"Quiting loop");
	}

	@Override
	public void onReceivePacket(Packet packet){
		if(Packet.COMMAND_MANAGER_START.equals(packet.getCommand())){
			if(!packet.isResponse()){
				if(!DaedalusManager.started){
					Log.debug(DaedalusManager.TAG,"Start Daedalus");
					try{
						new Packet(Packet.COMMAND_DAEDALUS_START).toStream(IO.STDOUT);
					}catch(Exception e){
						e.printStackTrace();
					}
					Log.debug(DaedalusManager.TAG,"Starting manager");
					try{
						new Packet(Packet.COMMAND_MANAGER_START,true).toStream(IO.STDOUT);
					}catch(Exception e){
						e.printStackTrace();
					}
					DaedalusManager.started = true;
					return;
				}
				Log.info(DaedalusManager.TAG,"Manager already started. Ignoring this.");
				return;
			}
			Log.error(DaedalusManager.TAG,"The command '"+packet.getCommand()+"' should not be a response.");
			return;
		}
		if(Packet.COMMAND_DAEDALUS_START.equals(packet.getCommand())){
			if(packet.isResponse()){
				Log.info(DaedalusManager.TAG,"Started Daedalus");
				this.createGameBattle();
				return;
			}
			Log.error(DaedalusManager.TAG,"The command '"+packet.getCommand()+"' should be a response.");
			return;
		}
		if(Packet.COMMAND_BATTLE_CREATE.equals(packet.getCommand())){
			if(packet.isResponse()){
				Log.info(DaedalusManager.TAG,"Created battle");
				this.startGameBattle();
				return;
			}
			Log.error(DaedalusManager.TAG,"The command '"+packet.getCommand()+"' should be a response.");
			return;
		}
		if(Packet.COMMAND_BATTLE_START.equals(packet.getCommand())){
			if(packet.isResponse()){
				Log.info(DaedalusManager.TAG,"Started battle");
				return;
			}
			Log.error(DaedalusManager.TAG,"The command '"+packet.getCommand()+"' should be a response.");
			return;
		}
		if(Packet.COMMAND_BATTLE_RESULT.equals(packet.getCommand())){
			if(!packet.isResponse()){
				Log.info(DaedalusManager.TAG,"Result from battle");
				try{
					new Packet(Packet.COMMAND_BATTLE_RESULT,true).toStream(IO.STDOUT);
				}catch(Exception e){
					e.printStackTrace();
				}
				battleIndex++;
				this.createGameBattle();
				return;
			}
			Log.error(DaedalusManager.TAG,"The command '"+packet.getCommand()+"' should not be a response.");
			return;
		}
		if(Packet.COMMAND_DAEDALUS_STOP.equals(packet.getCommand())){
			if(packet.isResponse()){
				Log.info(DaedalusManager.TAG,"Stopped Daedalus");
				return;
			}
			Log.error(DaedalusManager.TAG,"The command '"+packet.getCommand()+"' should be a response.");
			return;
		}
		if(Packet.COMMAND_MANAGER_STOP.equals(packet.getCommand())){
			if(!packet.isResponse()){
				Log.debug(DaedalusManager.TAG,"Stopping manager");
				try{
					new Packet(Packet.COMMAND_MANAGER_STOP,true).toStream(IO.STDOUT);
					started = false;
					this.isRunning = false;
				}catch(Exception e){
					e.printStackTrace();
				}
				return;
			}
			Log.error(DaedalusManager.TAG,"The command '"+packet.getCommand()+"' should not be a response.");
			return;
		}
		Log.warning(DaedalusManager.TAG,"Unknown command '"+packet.getCommand()+"'");
	}

	private void createGameBattle(){
		if(battleList.size()>battleIndex){
			String[] battle = battleList.get(battleIndex);
			Log.debug(DaedalusManager.TAG,"Create battle "+battle[0]+" for game '"+battle[1]+"' with referee '"+battle[2]+"' and players: "+Arrays.toString(Arrays.copyOfRange(battle,3,battle.length)));
			try{
				Map<String,Object> data = new HashMap<>();
				data.put("id",battle[0]);
				data.put("game",battle[1]);
				data.put("referee",battle[2]);
				data.put("players",Arrays.copyOfRange(battle,3,battle.length));
				new Packet(Packet.COMMAND_BATTLE_CREATE,data).toStream(IO.STDOUT);
			}catch(Exception e){
				e.printStackTrace();
			}
			return;
		}
		Log.info(DaedalusManager.TAG,"No more battles to create. Ending.");
		try{
			new Packet(Packet.COMMAND_DAEDALUS_STOP).toStream(IO.STDOUT);
		}catch(Exception e){
			e.printStackTrace();
		}

		//		if(Packet.COMMAND_LOG.equals(packet.getCommand())){
//			try {
//				Files.write(Paths.get("log_test.txt"), (packet.getData()+"\n").getBytes(), StandardOpenOption.APPEND);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			System.err.println("Packet: "+packet.getData());
//		}
//		long start = System.currentTimeMillis();
//
//		try{
//			new Packet(Packet.COMMAND_MANAGER_LIST).toStream(IO.STDOUT);
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//
//		Log.debug(DaedalusManager.TAG,"Debug");
//		Log.error(DaedalusManager.TAG,"Error");
//		Log.info(DaedalusManager.TAG,"Info");
//		Log.warning(DaedalusManager.TAG,"Warning");
//		//System.out.println("Running the manager");
////			try {
////				Thread.sleep(1000);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
//
//		if(System.currentTimeMillis()-start>10000){
//			Log.info(DaedalusManager.TAG,"GO STOP!!!!!!!!!!!!!!!!!!!!");
//			System.err.println("GO STOP");
//			break;
//		}
//
//
	}

	private void startGameBattle(){
		if(battleList.size()>battleIndex){
			String[] battle = battleList.get(battleIndex);
			Log.debug(DaedalusManager.TAG,"Start battle");
			try{
				Map<String,Object> data = new HashMap<>();
				data.put("id",battle[0]);
				new Packet(Packet.COMMAND_BATTLE_START,data).toStream(IO.STDOUT);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		//		if(Packet.COMMAND_LOG.equals(packet.getCommand())){
//			try {
//				Files.write(Paths.get("log_test.txt"), (packet.getData()+"\n").getBytes(), StandardOpenOption.APPEND);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			System.err.println("Packet: "+packet.getData());
//		}
//		long start = System.currentTimeMillis();
//
//		try{
//			new Packet(Packet.COMMAND_MANAGER_LIST).toStream(IO.STDOUT);
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//
//		Log.debug(DaedalusManager.TAG,"Debug");
//		Log.error(DaedalusManager.TAG,"Error");
//		Log.info(DaedalusManager.TAG,"Info");
//		Log.warning(DaedalusManager.TAG,"Warning");
//		//System.out.println("Running the manager");
////			try {
////				Thread.sleep(1000);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
//
//		if(System.currentTimeMillis()-start>10000){
//			Log.info(DaedalusManager.TAG,"GO STOP!!!!!!!!!!!!!!!!!!!!");
//			System.err.println("GO STOP");
//			break;
//		}
//
//
	}

}