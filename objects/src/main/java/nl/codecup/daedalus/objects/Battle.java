package nl.codecup.daedalus.objects;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;

//import nl.codecup.daedalus.protocol.ExecutableThread;
//import nl.codecup.daedalus.protocol.Packet;
//import nl.codecup.daedalus.protocol.Protocol;
//import nl.codecup.daedalus.log.Log;

public class Battle /*implements Runnable*/{
	
//	enum Status{
//		CREATED,
//		STARTED,
//		STOPPED
//	}
//
//	private final int id;
//	private Game game;
//	private long time;
//	private Referee referee;
//	private Player[] players = new Player[0];
//	private Status status = Status.CREATED;
//
//	private ExecutableThread RT;
//	private ExecutableThread[] PT = new ExecutableThread[0];
//
//	private int currentPlayer = -1;
//
//	private long threadTime;
//
//	private long[] playerTime;
//
//	public Battle(int id){
//		this.id = id;
//	}
//
//	public int getBattleId(){
//		return this.id;
//	}
//
//	public void setGame(Game game){
//		this.game = game;
//	}
//
//	public void setTime(long time){
//		this.time = time;
//	}
//
//	public void setReferee(Referee referee){
//		this.referee = referee;
//		this.RT = this.referee.getThread(this.game);
//	}
//
//	public int addPlayer(Player player){
//		Player[] players2 = new Player[players.length+1];
//		ExecutableThread[] PT2 = new ExecutableThread[PT.length+1];
//
//		System.arraycopy(players,0,players2,0,players.length);
//		System.arraycopy(PT,0,PT2,0,PT.length);
//
//		PT2[PT.length] = player.getThread(this.game);
//
//		players = players2;
//		PT = PT2;
//
//		return players.length;
//	}
//
//	@Override
//	public void run(){
//		this.playerTime = new long[this.players.length];
//
//		try{
//			System.err.println("[Battle "+id+"] Starting referee");
//			Protocol.refereeStart(this.id).toStream(RT.STDIN);
//
//			while(this.status== Status.STARTED){
//				try{
//					if(RT.STDOUT.available()>0){
//						Packet p = new Packet(RT.STDOUT);
//						if(p.getAction()==Protocol.ACTION_REFEREE_START){
//							ByteBuffer bb = ByteBuffer.wrap(p.getData());
//							int id = bb.getInt();
//							Log.debug("Daedalus","[Battle "+id+"] Referee started ("+id+")");
//						}
//						if(p.getAction()==Protocol.ACTION_REFEREE_STEP){
//							ByteBuffer bb = ByteBuffer.wrap(p.getData());
//							int id = bb.getInt();
//							int toPlayer = bb.getInt();
//							short strLength = bb.getShort();
//							byte[] buf = new byte[strLength];
//							bb.get(buf);
//							String step = new String(buf);
//
//							Log.debug("Daedalus","[Battle "+id+"] Referee did check step: '"+step+"'");
//
//							PT[toPlayer].PRINT.println(step);
//							PT[toPlayer].PRINT.flush();
//							Log.debug("Daedalus","[Battle "+id+"] Flushed");
//						}
//						if(p.getAction()==Protocol.ACTION_REFEREE_LISTEN){
//							this.waitForPlayerResponse(p);
//						}
//						if(p.getAction()==Protocol.ACTION_REFEREE_STOP){
//							Log.debug("Daedalus","Referee want to stop");
//							this.status = Status.STOPPED;
//							//currentPlayer = -1;
//							break;
//						}
//					}
//
//					if(RT.STDERR.available()>0){
//						byte[] buf = new byte[RT.STDERR.available()];
//						RT.STDERR.read(buf);
//						String str = new String(buf);
//						Log.debug("Referee",str);
//					}
//
//
//
//
//
//				//long l = ManagementFactory.getThreadMXBean().getThreadCpuTime(this.getId());
//
//				//BigInteger z = new BigInteger("45465465545345564765754543434").remainder(new BigInteger("23234"));
//
//				//this.RT.STDOUT.writeBytes("Hoi");
//				//this.RT.STDOUT.flush();
//				//this.RT.STDIN.readInt();
//
//				//Thread.sleep(10);
//
//				//long l2 = ManagementFactory.getThreadMXBean().getThreadCpuTime(this.getId());
//
//				//System.out.println(z);
//
//				//System.out.println(l);
//				//System.out.println(l2);
//				//System.out.println("=======================");
//
//
//				//Thread.sleep(5000);
//			}catch(/*IO*/Exception e){
//				e.printStackTrace();
//			}
//		}
//
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	}
//
//	private void waitForPlayerResponse(Packet p) throws IOException{
//		Log.debug("Daedalus","[Battle "+id+"] Referee want Daedalus to listen");
//
//		ByteBuffer bb = ByteBuffer.wrap(p.getData());
//		int id = bb.getInt();
//		int toPlayer = bb.getInt();
//
//		currentPlayer = toPlayer;
//
//		Protocol.listened(id).toStream(RT.STDIN);
//
//		Log.debug("Daedalus","[Battle "+id+"] Listening to player "+currentPlayer);
//		this.threadTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
//
//		while(currentPlayer>=0){
//			ExecutableThread ET = PT[currentPlayer];
//
//			if(ET.STDERR.available()>0){
//				byte[] buf = new byte[ET.STDERR.available()];
//				ET.STDERR.read(buf);
//				String str = new String(buf);
//				Log.debugPlayer("Player "+(currentPlayer+1),str);
//			}
//
//			if(ET.STDOUT.available()>0){
//				long duration = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()-threadTime;
//				@SuppressWarnings("deprecation")
//				String step = ET.STDOUT.readLine();//TODO Is deprecated
//				//Log.debugPlayer("PlayerOUT "+(currentPlayer+1),"'"+step+"'");
//				Log.warn("Daedalus","[Player "+(currentPlayer+1)+"] Thread "+(duration/1000/1000)+" ms");
//				this.playerTime[currentPlayer] += duration;
//				Log.error("Daedalus","[Player "+(currentPlayer+1)+"] Total "+(this.playerTime[currentPlayer]/1000/1000)+" ms / "+(time/1000/1000)+" ms");
//				Protocol.step(this.id,currentPlayer,step).toStream(RT.STDIN);
//				currentPlayer = -1;
//			}
//		}
//	}
//
//	public void setStarted(){
//		this.status = Status.STARTED;
//	}
//
//	public void setStopped(){
//		this.status = Status.STOPPED;
//	}
	
}