package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.FileFilter;

//import nl.codecup.daedalus.io.ExecutableThread;
//import nl.codecup.daedalus.io.ExecutableWrapper;

public class Referee extends Executable{

	public Referee(File refereeFile){
		super(refereeFile);
	}

	public static Referee[] getReferees(File refereesDirectory){
		File[] refereeFiles = refereesDirectory.listFiles(new FileFilter(){

			@Override
			public boolean accept(File file){
				return file.isFile();
			}

		});
		if(refereeFiles==null){
			return new Referee[0];
		}
		Referee[] referee = new Referee[refereeFiles.length];
		for(int i=0;i<referee.length;i++){
			referee[i] = new Referee(refereeFiles[i]);
		}
		return referee;
	}

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