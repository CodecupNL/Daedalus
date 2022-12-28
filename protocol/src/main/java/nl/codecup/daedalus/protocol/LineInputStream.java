package nl.codecup.daedalus.protocol;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LineInputStream extends BufferedInputStream {

	public LineInputStream(InputStream in){
		super(in);
	}

	public byte[] readLine(boolean cr, boolean lf, boolean crlf, boolean lfcr) throws IOException {
		if(!this.markSupported()){
			throw new IOException("The input stream doesn't support resetting.");
		}
		if(!cr && !lf && !crlf && !lfcr){
			throw new IOException("At least one line-end should be used.");
		}
		List<Byte> bytes = new ArrayList<>();
		boolean breakByEOS = false;
		while(true){
			int b = this.read();
			if(b==-1){
				breakByEOS = true;
				break;
			}
			if(b!='\r' && b!='\n'){
				bytes.add((byte) b);
				continue;
			}
			boolean firstCR = b=='\r';
			boolean firstLF = b=='\n';
			this.mark(1);
			int b2 = -1;
			if(this.available()>0 || (firstCR && !cr) || (firstLF && !lf)){
				b2 = this.read();
			}
			boolean useSecond = b2!=-1;
			boolean secondCR = b2=='\r';
			boolean secondLF = b2=='\n';

			if(useSecond){
				if(crlf && firstCR && secondLF){
					bytes.add((byte) b);
					bytes.add((byte) b2);
					break;
				}
				if(lfcr && firstLF && secondCR){
					bytes.add((byte) b);
					bytes.add((byte) b2);
					break;
				}
			}

			if(cr && firstCR){
				bytes.add((byte) b);
				this.reset();
				break;
			}
			if(lf && firstLF){
				bytes.add((byte) b);
				this.reset();
				break;
			}
		}
		if(bytes.size()==0 && breakByEOS){
			return null;
		}
		byte[] ba = new byte[bytes.size()];
		for(int i=0;i<ba.length;i++){
			ba[i] = bytes.get(i);
		}
		return ba;
	}

}