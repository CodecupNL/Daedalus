package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.fusesource.jansi.Ansi;

public class Log{

	private static boolean DEBUG;
	private static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
		Log.DEBUG = debug;
	}

	public static void debug(String tag,String message){
		System.err.println(Ansi.ansi().fgBlue().a(Log.time()+" ["+tag+"] [DEBUG]: "+message).reset().toString());
	}

	public static void error(String tag,String message){
		System.err.println(Ansi.ansi().fgRed().a(Log.time()+" ["+tag+"] [ERROR]: "+message).reset().toString());
	}

	public static void info(String tag,String message){
		System.err.println(Ansi.ansi().fgBrightGreen().a(Log.time()+" ["+tag+"] [INFO]: "+message).reset().toString());
	}

	public static void warning(String tag,String message){
		System.err.println(Ansi.ansi().fgYellow().a(Log.time()+" ["+tag+"] [WARNING]: "+message).reset().toString());
	}

	private static String time(){
		return Log.FORMAT.format(new Date());
	}

}