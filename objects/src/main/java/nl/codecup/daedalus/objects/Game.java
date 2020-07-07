package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.FileFilter;

public class Game{

	private final File file;

	public Game(File gameDirectory){
		this.file = gameDirectory;
	}

	public String getName(){
		return this.file.getName();
	}

	public static Game[] getGames(File gamesDirectory){
		File[] gameDirectories = gamesDirectory.listFiles(new FileFilter(){

			@Override
			public boolean accept(File file){
				return file.isDirectory();
			}

		});
		if(gameDirectories==null){
			return new Game[0];
		}
		Game[] games = new Game[gameDirectories.length];
		for(int i=0;i<games.length;i++){
			games[i] = new Game(gameDirectories[i]);
		}
		return games;
	}

}