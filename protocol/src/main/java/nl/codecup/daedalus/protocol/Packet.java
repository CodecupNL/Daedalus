package nl.codecup.daedalus.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Packet{
	
	private final byte from;
	private final byte to;
	private final byte action;
	private final short length;
	private final byte[] data;
	
	public Packet(DataInputStream dis) throws IOException{		
		this.from = dis.readByte();
		this.to = dis.readByte();
		this.action = dis.readByte();
		this.length = dis.readShort();
		this.data = new byte[this.length];
		dis.read(this.data);
	}
	
	public Packet(byte[] rawData){
		ByteBuffer bb = ByteBuffer.wrap(rawData);
		this.from = bb.get();
		this.to = bb.get();
		this.action = bb.get();
		this.length = bb.getShort();
		this.data = new byte[this.length];
		bb.get(this.data);
	}
	
	public Packet(byte from,byte to,byte action){
		this(from,to,action,new byte[0]);
	}
	
	public Packet(byte from,byte to,byte action,byte[] data){
		this.from = from;
		this.to = to;
		this.action = action;
		this.length = (short) data.length;
		this.data = data;
	}
	
	public byte getFrom(){
		return this.from;
	}
	
	public byte getTo(){
		return this.to;
	}
	
	public byte getAction(){
		return this.action;
	}
	
	public short getLength(){
		return this.length;
	}
	
	public byte[] getData(){
		return this.data;
	}
	
	public byte[] toRaw(){
		ByteBuffer bb = ByteBuffer.allocate(5+this.data.length);
		bb.put(this.from);
		bb.put(this.to);
		bb.put(this.action);
		bb.putShort(this.length);
		bb.put(this.data);
		return bb.array();
	}
	
	public void toStream(DataOutputStream dos) throws IOException{
		dos.writeByte(this.from);
		dos.writeByte(this.to);
		dos.writeByte(this.action);
		dos.writeShort(this.length);
		dos.write(this.data);
		dos.flush();
	}

}