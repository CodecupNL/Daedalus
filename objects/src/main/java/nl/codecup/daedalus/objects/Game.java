package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.FileFilter;

public class Game{

	private final File file;
	private final File DIRECTORY_PLAYERS;
	private final File DIRECTORY_REFEREES;

	public Game(File gameDirectory){
		this.file = gameDirectory;
		this.DIRECTORY_PLAYERS = new File(this.file,"players");
		this.DIRECTORY_REFEREES = new File(this.file,"referees");
	}

	public String getName(){
		return this.file.getName();
	}

	public Player[] getPlayers(){
		return Player.getPlayers(this.DIRECTORY_PLAYERS);
	}

	public Referee[] getReferees(){
		return Referee.getReferees(this.DIRECTORY_REFEREES);
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

	@Override
	public String toString() {
		return "Game{" +
				"file=" + file +
				'}';
	}

}