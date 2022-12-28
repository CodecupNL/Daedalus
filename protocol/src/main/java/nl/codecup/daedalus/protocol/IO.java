package nl.codecup.daedalus.protocol;

import java.io.DataOutputStream;

public class IO{
	
	public static final DataOutputStream STDERR = new DataOutputStream(System.err);
	public static final LineInputStream STDIN = new LineInputStream(System.in);
	public static final DataOutputStream STDOUT = new DataOutputStream(System.out);

}