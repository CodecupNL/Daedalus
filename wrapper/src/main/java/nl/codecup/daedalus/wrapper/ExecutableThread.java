package nl.codecup.daedalus.wrapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;

//import nl.codecup.daedalus.ShutdownHook;

public class ExecutableThread{
	
	private Process process;
	
	public final DataInputStream STDOUT;
	public final DataInputStream STDERR;
	public final DataOutputStream STDIN;
	
	public final PrintStream PRINT;
	
	public ExecutableThread(Process process){
		this.process = process;
//		ShutdownHook.addProcess(this.process);
				
		this.STDOUT = new DataInputStream(this.process.getInputStream());
		this.STDERR = new DataInputStream(this.process.getErrorStream());
		this.STDIN = new DataOutputStream(this.process.getOutputStream());
		
		this.PRINT = new PrintStream(this.process.getOutputStream());
	}




//
//
//
//	//TODO Temporary
//	public ExecutableThread getThread(){
//		try{
//			String command = ExecutableWrapper.getCommand(this.file);
//			System.err.println("COMMAND = "+command);
//			return new ExecutableThread(Runtime.getRuntime().exec(command));
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		return null;
//	}
//














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

//	public ExecutableThread getThread(Game game){
//		try{
//			return new ExecutableThread(Runtime.getRuntime().exec(ExecutableWrapper.getCommand(new File("games/"+game.getName()+"/referees/"+this.getName()))));
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		return null;
//	}

}