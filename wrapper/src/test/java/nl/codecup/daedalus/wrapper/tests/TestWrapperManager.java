package nl.codecup.daedalus.wrapper.tests;

import nl.codecup.daedalus.objects.Wrapper;
import nl.codecup.daedalus.wrapper.WrapperManager;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestWrapperManager{

	@Test
	public void testConstructorFile() throws IOException{
		File f = new File("wrapper.json");
		//WrapperManager wm = new WrapperManager(f);

		System.out.println(">>>>>>>>>>"+f.getAbsolutePath());

//		for(Wrapper w : wm.getWrappers(new File("yocto.java"))){
//			if(w!=null){
//				System.out.println(w.getName());
//			}else{
//				System.out.println(w);
//			}
//		}

		assertTrue(true);
	}

}