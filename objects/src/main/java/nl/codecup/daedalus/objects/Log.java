package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.FileFilter;

public class Log{

	private static boolean debug;

	private final File file;

	public Log(File logFile){
		this.file = logFile;
	}

	public String getName(){
		return this.file.getName();
	}

	public static Log[] getLogs(File logsDirectory){
		File[] logFiles = logsDirectory.listFiles(new FileFilter(){

			@Override
			public boolean accept(File file){
				return file.isFile();
			}

		});
		if(logFiles==null){
			return new Log[0];
		}
		Log[] logs = new Log[logFiles.length];
		for(int i=0;i<logs.length;i++){
			logs[i] = new Log(logFiles[i]);
		}
		return logs;
	}

	public static void setDebug(boolean debug){
		Log.debug = debug;
	}

}