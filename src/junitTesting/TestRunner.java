package junitTesting;

/**
 * @author MD AL MAMUNUR RASHID
 * This test run the Unit test and print the result
 */
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner 
{
	public static void main(String[] args)
	{
		Result result = JUnitCore.runClasses(CommunityDetectiionTesting.class);
		
		for(Failure failure : result.getFailures())
			System.out.println(failure);
		
		System.out.println(result.wasSuccessful());
	}
}
