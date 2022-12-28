package nl.codecup.daedalus.objects;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

import nl.codecup.daedalus.protocol.IO;
import nl.codecup.daedalus.protocol.Protocol;
import org.fusesource.jansi.Ansi;

public class Log{

	public static final byte LEVEL_INFO = 0x01;
	public static final byte LEVEL_DEBUG = 0x02;
	public static final byte LEVEL_WARNING = 0x03;
	public static final byte LEVEL_ERROR = 0x04;

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
		Date d = new Date();
		try{
//			Protocol.log(d,tag,Log.LEVEL_DEBUG,message).toStream(IO.STDOUT);
			IO.STDERR.write((Ansi.ansi().fgBlue().a(Log.time(d)+" ["+tag+"] [DEBUG]: "+message).reset().toString()+"\n").getBytes());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void error(String tag,String message){
		Date d = new Date();
		try{
//			Protocol.log(d,tag,Log.LEVEL_ERROR,message).toStream(IO.STDOUT);
			IO.STDERR.write((Ansi.ansi().fgRed().a(Log.time(d)+" ["+tag+"] [ERROR]: "+message).reset().toString()+"\n").getBytes());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void info(String tag,String message){
		Date d = new Date();
		try{
//			Protocol.log(d,tag,Log.LEVEL_INFO,message).toStream(IO.STDOUT);
			IO.STDERR.write((Ansi.ansi().fgBrightGreen().a(Log.time(d)+" ["+tag+"] [INFO]: "+message).reset().toString()+"\n").getBytes());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void warning(String tag,String message){
		Date d = new Date();
		try{
//			Protocol.log(d,tag,Log.LEVEL_WARNING,message).toStream(IO.STDOUT);
			IO.STDERR.write((Ansi.ansi().fgYellow().a(Log.time(d)+" ["+tag+"] [WARNING]: "+message).reset().toString()+"\n").getBytes());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static String time(Date d){
		return Log.FORMAT.format(d);
	}

}