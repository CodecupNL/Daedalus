package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.IOException;

//import nl.codecup.daedalus.io.ExecutableThread;
//import nl.codecup.daedalus.io.ExecutableWrapper;

public class Manager extends Executable{

	public Manager(File name) throws IOException{
		super(name.getName());
	}

	public Manager(String name){
		super(name);
	}
	
//	public ExecutableThread getThread(){
//		try{
//			return new ExecutableThread(Runtime.getRuntime().exec(ExecutableWrapper.getCommand(new File("managers/"+this.getName()))));
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		return null;
//	}

}