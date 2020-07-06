package nl.codecup.daedalus.runner.util;

import joptsimple.BuiltinHelpFormatter;
import joptsimple.internal.Strings;

public class DaedalusHelpFormatter extends BuiltinHelpFormatter{

	public DaedalusHelpFormatter(){
		this(80,2);
	}

	public DaedalusHelpFormatter(int desiredOverallWidth,int desiredColumnSeparatorWidth){
		super(desiredOverallWidth,desiredColumnSeparatorWidth);
	}

	protected void appendTypeIndicator(StringBuilder buffer,String typeIndicator,String description,char start,char end){
		if(!Strings.isNullOrEmpty(description)){
			buffer.append(' ').append(start);
			buffer.append(description);
		}
		buffer.append(end);
	}

}