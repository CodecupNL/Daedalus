package nl.codecup.daedalus.protocol;

import java.nio.ByteBuffer;

import nl.codecup.daedalus.objects.Game;
import nl.codecup.daedalus.objects.Player;
import nl.codecup.daedalus.objects.Referee;

public class Protocol{
	
	//Addresses
	public static byte ADDRESS_DAEDALUS = 0x00;
	public static byte ADDRESS_MANAGER = 0x01;
	public static byte ADDRESS_REFEREE = 0x02;
	public static byte ADDRESS_PLAYER = 0x03;
	
	//Actions
	public static byte ACTION_MANAGER_START = 0x00;
	public static byte ACTION_REFEREE_START = 0x01;
	public static byte ACTION_PLAYER_START = 0x02;
	
	public static byte ACTION_MANAGER_STOP = 0x03;
	public static byte ACTION_REFEREE_STOP = 0x04;
	public static byte ACTION_PLAYER_STOP = 0x05;
	
	public static byte ACTION_BATTLE_CREATE = 0x06;
	public static byte ACTION_BATTLE_GAME_LIST = 0x07;
	public static byte ACTION_BATTLE_GAME_SET = 0x08;
	public static byte ACTION_BATTLE_TIME_SET = 0x09;
	public static byte ACTION_BATTLE_REFEREE_LIST = 0x0A;
	public static byte ACTION_BATTLE_REFEREE_SET = 0x0B;
	public static byte ACTION_BATTLE_PLAYER_LIST = 0x0C;
	public static byte ACTION_BATTLE_PLAYER_ADD = 0x0D;
	public static byte ACTION_BATTLE_START = 0x0E;
	public static byte ACTION_BATTLE_STOP = 0x0F;
	
	public static byte ACTION_REFEREE_STEP = 0x10;
	public static byte ACTION_REFEREE_LISTEN = 0x11;
	
	//Manager Start Request
	public static Packet managerStart(){
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_MANAGER,Protocol.ACTION_MANAGER_START);
	}
	
	//Manager Start Response
	public static Packet managerStarted(){
		return new Packet(Protocol.ADDRESS_MANAGER,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_MANAGER_START);
	}
	
	//Manager Stop Request
	public static Packet managerStop(){
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_MANAGER,Protocol.ACTION_MANAGER_STOP);
	}
	
	//Manager Stop Response
	public static Packet managerStopped(){
		return new Packet(Protocol.ADDRESS_MANAGER,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_MANAGER_STOP);
	}
	
	//Battle Create Request
	public static Packet battleCreate(){
		return new Packet(Protocol.ADDRESS_MANAGER,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_BATTLE_CREATE);
	}
	
	//Battle Create Response
	public static Packet battleCreated(int battleID){
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(battleID);
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_MANAGER,Protocol.ACTION_BATTLE_CREATE,bb.array());
	}
	
	//Game List Request
	public static Packet listGame(int battleID){
		return new Packet(Protocol.ADDRESS_MANAGER,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_BATTLE_GAME_LIST);
	}
	
	//Game List Response
	public static Packet listedGame(int battleID,Game[] games){
		int dataLength = 4;
		for(Game g : games){
			dataLength += 2+g.getName().length();
		}
		ByteBuffer bb = ByteBuffer.allocate(dataLength).putInt(battleID);
		for(Game g : games){
			String name = g.getName();
			bb.putShort((short) name.length());
			bb.put(name.getBytes());
		}
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_MANAGER,Protocol.ACTION_BATTLE_GAME_LIST,bb.array());
	}
	
	//Game Set Request
	public static Packet setGame(int battleID,Game game){
		int dataLength = 4+2+game.getName().length();
		ByteBuffer bb = ByteBuffer.allocate(dataLength).putInt(battleID);
		String name = game.getName();
		bb.putShort((short) name.length());
		bb.put(name.getBytes());
		return new Packet(Protocol.ADDRESS_MANAGER,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_BATTLE_GAME_SET,bb.array());
	}
	
	//Game Set Response
	public static Packet gameSet(int battleID){
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(battleID);
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_MANAGER,Protocol.ACTION_BATTLE_GAME_SET,bb.array());
	}
	
	//Time Set Request
	public static Packet setTime(int battleID,long time){
		ByteBuffer bb = ByteBuffer.allocate(4+8).putInt(battleID).putLong(time);
		return new Packet(Protocol.ADDRESS_MANAGER,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_BATTLE_TIME_SET,bb.array());
	}
	
	//Time Set Response
	public static Packet timeSet(int battleID){
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(battleID);
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_MANAGER,Protocol.ACTION_BATTLE_TIME_SET,bb.array());
	}
	
	//Referee List Request
	public static Packet listReferee(int battleID){
		return new Packet(Protocol.ADDRESS_MANAGER,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_BATTLE_REFEREE_LIST);
	}
	
	//Referee List Response
	public static Packet listedReferee(int battleID,Referee[] referees){
		int dataLength = 4;
		for(Referee r : referees){
			dataLength += 2+r.getName().length();
		}
		ByteBuffer bb = ByteBuffer.allocate(dataLength).putInt(battleID);
		for(Referee r : referees){
			String name = r.getName();
			bb.putShort((short) name.length());
			bb.put(name.getBytes());
		}
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_MANAGER,Protocol.ACTION_BATTLE_REFEREE_LIST,bb.array());
	}
	
	//Referee Set Request
	public static Packet setReferee(int battleID,Referee referee){
		int dataLength = 4+2+referee.getName().length();
		ByteBuffer bb = ByteBuffer.allocate(dataLength).putInt(battleID);
		String name = referee.getName();
		bb.putShort((short) name.length());
		bb.put(name.getBytes());
		return new Packet(Protocol.ADDRESS_MANAGER,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_BATTLE_REFEREE_SET,bb.array());
	}
	
	//Referee Set Response
	public static Packet refereeSet(int battleID){
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(battleID);
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_MANAGER,Protocol.ACTION_BATTLE_REFEREE_SET,bb.array());
	}
	
	//Player List Request
	public static Packet listPlayer(int battleID){
		return new Packet(Protocol.ADDRESS_MANAGER,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_BATTLE_PLAYER_LIST);
	}
	
	//Player List Response
	public static Packet listedPlayer(int battleID,Player[] players){
		int dataLength = 4;
		for(Player p : players){
			dataLength += 2+p.getName().length();
		}
		ByteBuffer bb = ByteBuffer.allocate(dataLength).putInt(battleID);
		for(Player p : players){
			String name = p.getName();
			bb.putShort((short) name.length());
			bb.put(name.getBytes());
		}
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_MANAGER,Protocol.ACTION_BATTLE_PLAYER_LIST,bb.array());
	}
	
	//Player Add Request
	public static Packet addPlayer(int battleID,Player player){
		int dataLength = 4+2+player.getName().length();
		ByteBuffer bb = ByteBuffer.allocate(dataLength).putInt(battleID);
		String name = player.getName();
		bb.putShort((short) name.length());
		bb.put(name.getBytes());
		return new Packet(Protocol.ADDRESS_MANAGER,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_BATTLE_PLAYER_ADD,bb.array());
	}
	
	//Player Add Response
	public static Packet addedPlayer(int battleID){
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(battleID);
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_MANAGER,Protocol.ACTION_BATTLE_PLAYER_ADD,bb.array());
	}
	
	//Referee Start Request
	public static Packet battleStart(int battleID){
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(battleID);
		return new Packet(Protocol.ADDRESS_MANAGER,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_BATTLE_START,bb.array());
	}
	
	//Referee Start Response
	public static Packet battleStarted(int battleID){
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(battleID);
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_MANAGER,Protocol.ACTION_BATTLE_START,bb.array());
	}
	
	//Referee Start Request
	public static Packet refereeStart(int battleID){
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(battleID);
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_REFEREE,Protocol.ACTION_REFEREE_START,bb.array());
	}
	
	//Referee Start Response
	public static Packet refereeStarted(int battleID){
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(battleID);
		return new Packet(Protocol.ADDRESS_REFEREE,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_REFEREE_START,bb.array());
	}
	
	//Step Request
	public static Packet step(int battleID,int fromPlayer,String step){
		ByteBuffer bb = ByteBuffer.allocate(4+4+2+step.length()).putInt(battleID);
		bb.putInt(fromPlayer);
		bb.putShort((short) step.length());
		bb.put(step.getBytes());
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_REFEREE,Protocol.ACTION_REFEREE_STEP,bb.array());
	}
	
	//Step Response
	public static Packet stepped(int battleID,int toPlayer,String step){
		ByteBuffer bb = ByteBuffer.allocate(4+4+2+step.length()).putInt(battleID);
		bb.putInt(toPlayer);
		bb.putShort((short) step.length());
		bb.put(step.getBytes());
		return new Packet(Protocol.ADDRESS_REFEREE,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_REFEREE_STEP,bb.array());
	}
	
	//Listen Request
	public static Packet listen(int battleID,int toPlayer){
		ByteBuffer bb = ByteBuffer.allocate(4+4).putInt(battleID);
		bb.putInt(toPlayer);
		return new Packet(Protocol.ADDRESS_REFEREE,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_REFEREE_LISTEN,bb.array());
	}
	
	//Listen Response
	public static Packet listened(int battleID){
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(battleID);
		return new Packet(Protocol.ADDRESS_REFEREE,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_REFEREE_LISTEN,bb.array());
	}
	
	//Manager Stop Request
	public static Packet refereeStop(int battleID){
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(battleID);
		return new Packet(Protocol.ADDRESS_DAEDALUS,Protocol.ADDRESS_REFEREE,Protocol.ACTION_REFEREE_STOP,bb.array());
	}
	
	//Manager Stop Response
	public static Packet refereeStopped(int battleID){
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(battleID);
		return new Packet(Protocol.ADDRESS_REFEREE,Protocol.ADDRESS_DAEDALUS,Protocol.ACTION_REFEREE_STOP,bb.array());
	}

}