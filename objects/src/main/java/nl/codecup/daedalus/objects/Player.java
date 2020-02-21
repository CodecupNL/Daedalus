package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.IOException;

//import nl.codecup.daedalus.io.ExecutableThread;
//import nl.codecup.daedalus.io.ExecutableWrapper;

public class Player extends Executable{

	public Player(String name){
		super(name);
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