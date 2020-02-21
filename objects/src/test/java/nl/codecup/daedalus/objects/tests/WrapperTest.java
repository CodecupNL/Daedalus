package nl.codecup.daedalus.objects.tests;

import nl.codecup.daedalus.objects.Wrapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WrapperTest{

	@Test
	public void testGetName(){
		String NAME = "name-of-wrapper";

		Wrapper w = new Wrapper(NAME,null,null,null);

		assertEquals(NAME,w.getName());
	}

	@Test
	public void testGetCommands(){
		String[] COMMANDS = {"command-1-of-wrapper","command-2-of-wrapper"};

		Wrapper w = new Wrapper(null,COMMANDS,null,null);

		assertEquals(COMMANDS,w.getCommands());
	}

	@Test
	public void testGetPatterns(){
		String[] PATTERNS = {"pattern-1-of-wrapper","pattern-2-of-wrapper"};

		Wrapper w = new Wrapper(null,null,PATTERNS,null);

		assertEquals(PATTERNS,w.getPatterns());
	}

	@Test
	public void testWrapper(){
		Wrapper WRAPPER = new Wrapper(null,null,null,null);

		Wrapper w = new Wrapper(null,null,null,WRAPPER);

		assertEquals(WRAPPER,w.getWrapper());
	}

}