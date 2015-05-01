import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestResult;
import org.testng.Reporter;

import org.testng.TestListenerAdapter;

public class AutomationLogger extends TestListenerAdapter {


	@Override
	public void onConfigurationFailure(ITestResult tr){
		super.onConfigurationFailure( tr );
		_print( tr.getThrowable() );
	}

	@Override
	public void onTestStart(ITestResult tr) {
		System.out.println("STARTED - " + tr.getName() + " - " + tr.getTestContext().getSuite().getName() + " @ " + getCurrentTime());
		Reporter.log("<p>" + "STARTED - " + tr.getName() + " - " + tr.getTestContext().getSuite().getName() + " @ " + getCurrentTime() + "</p>");
		System.out.println(".....");
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		System.out.println("PASSED - " + tr.getName() + " - " + tr.getTestContext().getSuite().getName() + " @ " + getCurrentTime());
		Reporter.log("<p>" + "PASSED - " + tr.getName() + " - " + tr.getTestContext().getSuite().getName() + " @ " + getCurrentTime() + "</p>");
		printTestResults(tr);
		System.out.println(".....");
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		System.out.println("FAILED - " + tr.getName() + " - " + tr.getTestContext().getSuite().getName() + " @ " + getCurrentTime());
		Reporter.log("<p>" + "FAILED - " + tr.getName() + " - " + tr.getTestContext().getSuite().getName() + " @ " + getCurrentTime() + "</p>");
		printTestResults(tr);
		_print( tr.getThrowable() );
		System.out.println(".....");		
	}

	@Override
	public void onTestSkipped(ITestResult tr) {	
		System.out.println("SKIPPED - " + tr.getName() + " - " + tr.getTestContext().getSuite().getName() + " @ " + getCurrentTime());
		Reporter.log("<p>" + "SKIPPED - " + tr.getName() + " - " + tr.getTestContext().getSuite().getName() + " @ " + getCurrentTime() + "</p>");
		printTestResults(tr);
		_print( tr.getThrowable() );
		System.out.println(".....");
	}
	
	// This is the method which will be executed in case of test pass or fail

	// This will provide the information on the test

	private void printTestResults(ITestResult result) { 

		String status = null;        

		switch (result.getStatus()) {

		case ITestResult.SUCCESS:

			status = "Passed";

			break;

		case ITestResult.FAILURE:

			status = "Failed";

			break;

		case ITestResult.SKIP:

			status = "Skipped";

			break;

		default:

			status = "Unknown";

			break;

		}

		System.out.println(result.getTestContext().getSuite().getName() + " test status: " + status);

	}

	//Returns the current time when the method is called
	public String getCurrentTime(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
		Date dt = new Date();
		return dateFormat.format(dt);    
	}

	private void _print( Throwable t ){

		int otcount = 0;
		int jlrcount = 0;

		if (t == null) {
			return;
		}

		Reporter.log("<p>" + t.toString() + "</p>");
		System.out.println("-" + t.toString()+ "-");

		for ( StackTraceElement e : t.getStackTrace() ){
			if ( e.getClassName().startsWith( "org.testng.")) {
				if (otcount++ == 0) {
					Reporter.log("<p>" + "  " + e + " (with others of org.testng.* omitted)" + "</p>");
					System.out.println("  " + e + " (with others of org.testng.* omitted)");
				}
			}
			else if (e.getClassName().startsWith( "java.lang.reflect.") || e.getClassName().startsWith("sun.reflect.") ) {
				if (jlrcount++ == 0) {
					Reporter.log("<p>" + "  " + e  + " (with others of java.lang.reflect.* or sun.reflect.* omitted)" + "</p>");
					System.out.println("  " + e  + " (with others of java.lang.reflect.* or sun.reflect.* omitted)");
				}
			}
			else {
				Reporter.log("<p>" + "  " +  e + "</p>");
				System.out.println("  " +  e );
			}
		}

		if (t.getCause() != null) {
			Reporter.log("<p>" + "Caused By : " + "</p>");
			System.out.println("Caused By : ");

			_print(t.getCause());
		}
		System.out.println();
	}
}
