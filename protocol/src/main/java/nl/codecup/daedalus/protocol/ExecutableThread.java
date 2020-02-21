package nl.codecup.daedalus.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

}