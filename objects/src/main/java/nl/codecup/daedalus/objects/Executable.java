package nl.codecup.daedalus.objects;

import java.io.File;

public abstract class Executable{

	private final File file;

	public Executable(File executableFile){
		this.file = executableFile;
	}

	public String getName(){
		return this.file.getName();
	}






















//	public Manager(File name) throws IOException{
//		super(name.getName());
//	}
//
//	public Manager(String name){
//		super(name);
//	}

//	public ExecutableThread getThread(){
//		try{
//			return new ExecutableThread(Runtime.getRuntime().exec(ExecutableWrapper.getCommand(new File("managers/"+this.getName()))));
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		return null;
//	}

	//	public ExecutableThread getThread(Game game){
//		try{
//			return new ExecutableThread(Runtime.getRuntime().exec(ExecutableWrapper.getCommand(new File("games/"+game.getName()+"/players/"+this.getName()))));
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		return null;
//	}

	//	public Referee(String name){
//		super(name);
//	}

//	public ExecutableThread getThread(Game game){
//		try{
//			return new ExecutableThread(Runtime.getRuntime().exec(ExecutableWrapper.getCommand(new File("games/"+game.getName()+"/referees/"+this.getName()))));
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		return null;
//	}

}