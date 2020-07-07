package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.FileFilter;

//import nl.codecup.daedalus.io.ExecutableThread;
//import nl.codecup.daedalus.io.ExecutableWrapper;

public class Player extends Executable{

	private final File file;

	public Player(File playerFile){
		this.file = playerFile;
	}

	public String getName(){
		return this.file.getName();
	}

	public static Player[] getPlayers(File playersDirectory){
		File[] playerFiles = playersDirectory.listFiles(new FileFilter(){

			@Override
			public boolean accept(File file){
				return file.isFile();
			}

		});
		if(playerFiles==null){
			return new Player[0];
		}
		Player[] players = new Player[playerFiles.length];
		for(int i=0;i<players.length;i++){
			players[i] = new Player(playerFiles[i]);
		}
		return players;
	}
	
//	public ExecutableThread getThread(Game game){
//		try{
//			return new ExecutableThread(Runtime.getRuntime().exec(ExecutableWrapper.getCommand(new File("games/"+game.getName()+"/players/"+this.getName()))));
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		return null;
//	}

}