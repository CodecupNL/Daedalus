package nl.codecup.daedalus.runner;

import nl.codecup.daedalus.objects.Battle;
import nl.codecup.daedalus.objects.Executable;
import nl.codecup.daedalus.objects.Log;
import nl.codecup.daedalus.protocol.LineInputStream;
import nl.codecup.daedalus.protocol.Packet;
import nl.codecup.daedalus.wrapper.ExecutableThread;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DaedalusBattle implements Runnable{

	private Battle battle;

	private ExecutableThread refereeExe;
	private ExecutableThread[] playersExe;

	public DaedalusBattle(Battle battle){
		this.battle = battle;
	}

	public Battle getBattle() {
		return this.battle;
	}

	private boolean isRunning = true;

	@Override
	public void run(){
		System.err.println("Start the loop of battle "+battle.getId());
		try{
			this.refereeExe = ExecutableThread.start(this.battle.getReferee());
		}catch(IOException e){
			e.printStackTrace();
		}
		this.playersExe = new ExecutableThread[this.battle.getPlayers().length];
		for(int i=0;i<this.battle.getPlayers().length;i++){
			try{
				this.playersExe[i] = ExecutableThread.start(this.battle.getPlayers()[i]);
			}catch(IOException e){
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while(this.isRunning){
			try{
				String line = new String(this.refereeExe.STDOUT.readLine(true,true,true,false)).trim();
				char ident = line.charAt(0);
				Log.error("BATTLE/"+battle.getId(),"Line = "+line);
				if(ident=='1' || ident=='2'){
					System.err.println("To Player: "+ident);
					this.playersExe[Integer.parseInt(ident+"")-1].STDIN.write((line.substring(2)+"\n").getBytes());
					try {
						this.playersExe[Integer.parseInt(ident + "") - 1].STDIN.flush();
					}catch(Exception e){
						Log.warning("BATTLE/"+battle.getId(),"FLUSH ERROR 1");
						//e.printStackTrace();
					}
					continue;
				}
				if(ident=='M'){
					String[] parts = line.split(" ");
					String last = parts[parts.length-1];
					String[] parts2 = last.split(";");
					int[] ranks = new int[2];
					if(parts2[0].endsWith("_LOSE")){
						ranks[0] = 1;
					}
					if(parts2[0].endsWith("_WIN")){
						ranks[0] = 1;
					}
					if(parts2[1].endsWith("_LOSE")){
						ranks[1] = 1;
					}
					if(parts2[1].endsWith("_WIN")){
						ranks[1] = 1;
					}
					int[] points = new int[2];
					points[0] = Integer.parseInt(parts[2]);
					points[1] = Integer.parseInt(parts[3]);


					Map<String,Object> data = new HashMap<>();


					data.put("id",battle.getId());
					data.put("ranks",ranks);
					data.put("points",points);
					new Packet(Packet.COMMAND_BATTLE_RESULT,data).toStream(Daedalus.getInstance().managerThread.STDIN);
					break;
				}
				if(ident=='I' && line.substring(2).equals("lock")){
					System.err.println("LOCK OK");
					this.refereeExe.STDIN.write("lock_ok\n".getBytes());
					try{
						this.refereeExe.STDIN.flush();
					}catch(Exception e){

						Log.warning("BATTLE/"+battle.getId(),"FLUSH ERROR 2");
						//e.printStackTrace();
					}
					continue;
				}
				if(ident=='I' && line.substring(2).equals("unlock")){
					System.err.println("UNLOCK OK");
					continue;
				}
				if(ident=='I' && line.substring(2,8).equals("listen")){
					char ident2 = line.charAt(9);
					System.err.println("LISTEN OK = "+ident2);
					String line2 = new String(this.playersExe[Integer.parseInt(ident2+"")-1].STDOUT.readLine(true,true,true,false)).trim();

					this.refereeExe.STDIN.write((line2+"\n").getBytes());
					try{
						this.refereeExe.STDIN.flush();
					}catch(Exception e){
						Log.warning("BATTLE/"+battle.getId(),"FLUSH ERROR 3");
						//e.printStackTrace();
					}
					continue;
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}


}
