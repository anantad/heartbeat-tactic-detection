package ast;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

import junit.framework.Assert;

public class JavaProjectParserTest {

	@Test
	public void test() {
		try {
			assertTrue(JavaProjectParser.testIt());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fail("Not yet implemented");
	}

}
