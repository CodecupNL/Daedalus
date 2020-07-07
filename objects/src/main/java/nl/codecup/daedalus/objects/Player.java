package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.FileFilter;

public class Player extends Executable{

	public Player(File playerFile){
		super(playerFile);
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

}