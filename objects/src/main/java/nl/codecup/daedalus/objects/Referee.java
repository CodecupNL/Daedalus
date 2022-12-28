package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.FileFilter;

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

	@Override
	public String toString() {
		return "Referee{" +
				"file=" + file +
				'}';
	}

}