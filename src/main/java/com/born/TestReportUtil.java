package com.born;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Date;
import java.util.Properties;
public class TestReportUtil {

	public static String result_FolderName = null;
	static Integer PASS_TEST_CASE_COUNT = 0;
	static Integer FAIL_TEST_CASE_COUNT = 0;
	static Integer TOTAL_TEST_CASES_COUNT = 0;
	static Integer TOTAL_PASS = 0;
	static Integer TOTAL_FAIL = 0;
	
	
	public static void main(String[] args) throws Exception {
		
		
		Date d = new Date();
		String date = d.toString().replaceAll(" ", "_");
		date = date.replaceAll(":", "_");
		date = date.replaceAll("\\+", "_");
		//System.out.println(date);
	try	{
		FileInputStream fstr = new FileInputStream(System.getProperty("user.dir")+ "/build_info.properties");
		Properties CONF = new Properties();
		CONF.load(fstr);
		
		String BuildVer=CONF.getProperty("build.revision.number");
		result_FolderName = "TestReport" +"_"+"Build_Ver"+BuildVer;
		new File(result_FolderName).mkdirs();
   
	    
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir")
				+ "/src/test/java/com/born/config/config.properties");
		Properties CONFIG = new Properties();
		CONFIG.load(fs);
		String environment = CONFIG.getProperty("environment");
		String logo        = CONFIG.getProperty("BornLogoPath");
		String watermark = CONFIG.getProperty("reportbg");
		String release     = CONFIG.getProperty("release");
		String project     = CONFIG.getProperty("project");
		String browser     = CONFIG.getProperty("browserType");

		Xls_Reader suiteXLS = new Xls_Reader(System.getProperty("user.dir")
				+ "//src//test//java//com//born//xls//Suite.xlsx");

		String indexHtmlPath = result_FolderName + "//index.html";
		new File(indexHtmlPath).createNewFile();
		//System.out.println("Automation Test Execution Report"+project);
		FileWriter fstream = new FileWriter(indexHtmlPath);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write("<html><HEAD> <TITLE>BEATS 2.0 TEST RESULTS</TITLE></HEAD><body><style><!--body {background-image: url("+ watermark +");background-repeat: no-repeat; background-color: rgb(216, 227, 186);width: 900px;background-position: 196px 113px; } //--></style> <h4 align=center><FONT COLOR=#000000 FACE=AriaL SIZE=6><b><u>BEATS 2.0 TEST RESULTS</u></b></h4><table  border=1 cellspacing=1 cellpadding=1 ><tr><h4> <FONT COLOR=660000 FACE=Arial SIZE=4.5> "+project+" Test Environment</h4><td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Test Execution Date</b></td><td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>");
		out.write(d.toString());
		// "for logo"after body before h4 tag copy -----<img src=\""+ logo +"\" />
		out.write("</b></td></tr><tr><td width=150 align=left bgcolor=#153e7e><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Run Environment</b></td><td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>");
		out.write(environment);
		//out.write("+ environment +">" + environment + "</a>");
		out.write("</b></td></tr><tr><td width=150 align= left  bgcolor=#153e7e><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>Release</b></td><td width=150 align= left ><FONT COLOR=#153E7E FACE= Arial  SIZE=2.75><b>");
		out.write(release);
		out.write("</b></td></tr><tr><td width=150 align= left  bgcolor=#153e7e><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>BrowserType/OS </b></td><td width=150 align= left ><FONT COLOR=#153E7E FACE= Arial  SIZE=2.75><b>");
		out.write(browser);
		out.write("</b></td></tr></table>");
		
		out.write("<h4> <FONT COLOR=660000 FACE= Arial  SIZE=4.5>" + project + " Test Suites</h4><table  border=1 cellspacing=1 cellpadding=1 width=100%><tr><td width=20% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>SUITE NAME</b></td><td width=40% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>DESCRIPTION</b></td>");
		
		Integer totalTestSuites = suiteXLS.getRowCount(Constants.TEST_SUITE_SHEET);
		//System.out.println("total test suits :: " + totalTestSuites);
		
		String currentTestSuite = null;
		Xls_Reader current_suite_xls = null;
		String suite_result = "";
		Integer passedScenarios = 0;
		Integer failedScenarios = 0;
		
		/**
		 * This loops helps us to traverse all rows of Suite.xlsx file, get the data from file and form the html file in table fashion
		 */
		for (int currentSuiteID = 2; currentSuiteID <= totalTestSuites; currentSuiteID++) {
			
			currentTestSuite = suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.SUITE_ID,currentSuiteID);
			
			current_suite_xls = new Xls_Reader(System.getProperty("user.dir")
							+ "//src//test//java//com//born//xls//"+ currentTestSuite + ".xlsx");
			
			String currentTestName = null;
			String testCasePageName = null;
			
			testCasePageName = currentTestSuite+"_"+Constants.TEST_CASES_SHEET;
			String testCaseFile = result_FolderName + "\\"+ testCasePageName + "_steps.html";
			new File(testCaseFile).createNewFile();
			FileWriter fstream_test_cases = new FileWriter(testCaseFile);
			BufferedWriter out_test_cases = new BufferedWriter(fstream_test_cases);
			out_test_cases.write("<html><HEAD> <TITLE>"+ currentTestSuite
					+ " Test Results</TITLE></HEAD><body><style><!--body {background-image: url("+ watermark +");background-repeat: no-repeat;background-position: 390px 150px;background-color: rgb(236, 227, 186);background-attachment: fixed;} //--></style><h4 align=center><FONT COLOR=660066 FACE=AriaL SIZE=6><b><u> "
					+ currentTestSuite+ " Detailed Test Results</u></b></h4>");
			out_test_cases.write("<tr>");
			
			int rowsOfCases = current_suite_xls.getRowCount(Constants.TEST_CASES_SHEET);
			int colsOfCases = current_suite_xls.getColumnCount(Constants.TEST_CASES_SHEET);
			out_test_cases.write("<h4 align=left><FONT COLOR=660066 FACE=AriaL SIZE=4.5><b> Total Test Scenario :"+ (rowsOfCases-1) +"</b></h4>");
			out_test_cases.write("<table width=100% border=1 cellspacing=1 cellpadding=1 >");
			TOTAL_TEST_CASES_COUNT = TOTAL_TEST_CASES_COUNT + (rowsOfCases-1);
			/**
			 * This loop is for forming the TEST_CASES_SHEET html file table header
			 */
			for (int colNum = 0; colNum < colsOfCases; colNum++) {
				out_test_cases
						.write("<td width=150 align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2>");
				out_test_cases.write(current_suite_xls.getCellData(
						Constants.TEST_CASES_SHEET, colNum, 1));
			}
			
			out_test_cases.write("</b></tr>");
			String passOfFail="PASS";
			
			/**
			 * This loops helps us to traverse all rows of TEST_CASES_SHEET file, get the data from file and form the html file in table fashion
			 */
			for (int currentTestCaseID = 2; currentTestCaseID <= current_suite_xls.getRowCount(Constants.TEST_CASES_SHEET); currentTestCaseID++) {
				
				currentTestName = current_suite_xls.getCellData(Constants.TEST_CASES_SHEET, Constants.TCID,currentTestCaseID);
				//System.out.println("currentTestName :: "+currentTestName);
				
				/**
				 * From TEST_CASES_SHEET file will get Test case steps file names {@currentTestName} which in first coloumn of TEST_CASES_SHEET file
				 * based on this currentTestName file names we will generate html files and get the data from respective .xslx files and
				 * forming the table structure with this data in html file.  
				 */
				String currentTestStepsFileName = currentTestSuite+"_"+Constants.TEST_STEPS_SHEET;
				String testCaseStepsFile = result_FolderName + "\\"+currentTestStepsFileName+ "_steps.html";
				new File(Constants.TEST_STEPS_SHEET).createNewFile();
				int rows = current_suite_xls.getRowCount(Constants.TEST_STEPS_SHEET);
				int cols = current_suite_xls.getColumnCount(Constants.TEST_STEPS_SHEET);
				FileWriter fstream_test_steps = new FileWriter(testCaseStepsFile);
				BufferedWriter out_test_steps = new BufferedWriter(fstream_test_steps);
				out_test_steps.write("<html><HEAD> <TITLE>"+ currentTestSuite
						+ " Test Results</TITLE></HEAD><body><style><!--body {background-image: url("+ watermark +");background-repeat: no-repeat;background-color: rgb(236, 227, 186);background-attachment: fixed;} //--></style><h4 align=center><FONT COLOR=660066 FACE=AriaL SIZE=6><b><u> "
						+ Constants.TEST_STEPS_SHEET+ " Detailed Test Results</u></b></h4><table width=100% border=1 cellspacing=1 cellpadding=1 >");
				
				out_test_steps.write("<tr>");
				for (int colNum = 0; colNum < cols; colNum++) {
					out_test_steps
							.write("<td align= center bgcolor=#153E7E><FONT COLOR=#000000 FACE= Arial  SIZE=2><b>");        //COLOR=#ffffff
					out_test_steps.write(current_suite_xls.getCellData(Constants.TEST_STEPS_SHEET, colNum, 1));
				}
				out_test_steps.write("</b></tr>");

				boolean result_col = false;
				//System.out.println("ROWS COUNT :: " + rows);
				String testCaseName = "";
				passOfFail="PASS";
				for (int rowNum = 2; rowNum <= rows; rowNum++) {
					out_test_steps.write("<tr>");
					for (int colNum = 0; colNum < cols; colNum++) {
						if(colNum == 0){
							testCaseName = current_suite_xls.getCellData(Constants.TEST_STEPS_SHEET, colNum, rowNum);
						}
						String data = current_suite_xls.getCellData(Constants.TEST_STEPS_SHEET, colNum, rowNum);
						//System.out.println(" cell data :: " + data);
						result_col = current_suite_xls.getCellData(Constants.TEST_STEPS_SHEET, colNum, 1).startsWith(Constants.RESULT);
						if (data.isEmpty()) {
							if (result_col)
								data = "---";  // SKIP Changed ---
							else
								data = " ";
						}
						//if(testCaseName.equals(currentTestName)){
						if ((data.startsWith("Pass") || data.startsWith("PASS")) && result_col){
							out_test_steps.write("<td align=center bgcolor=green><FONT COLOR=#000000 FACE= Arial  SIZE=1>");
							PASS_TEST_CASE_COUNT++;
						}else if ((data.startsWith("Fail") || data.startsWith("FAIL")) && result_col) {
							out_test_steps.write("<td align=center bgcolor=red><FONT COLOR=#000000 FACE= Arial  SIZE=1>");
							FAIL_TEST_CASE_COUNT++;
							if(testCaseName.equals(currentTestName))
								passOfFail="FAIL";
							if (suite_result.equals(""))
								suite_result = "FAIL";
						} else if ((data.startsWith("Skip") || data.startsWith("SKIP")) && result_col){
							out_test_steps.write("<td align=center bgcolor=yellow><FONT COLOR=#d7c797 FACE= Arial  SIZE=1>");
						}else
							out_test_steps.write("<td align= center bgcolor=#ffffff><FONT COLOR=#000000 FACE= Arial  SIZE=1>");
						out_test_steps.write(data);
						//}
					}
					out_test_steps.write("</tr>");
					//System.out.println("iNSIDE CHILD XLS :: " + passOfFail);
				}
				out_test_steps.write("</tr>");
				out_test_steps.write("</table>");
				out_test_steps.close();
				//System.out.println("OUTSIDE LOOP OF CHILD XLS :: " + passOfFail);
				if(passOfFail.equalsIgnoreCase("PASS"))
					passedScenarios +=1;
				else
					failedScenarios +=1;
				
				/**
				 * completed the child .xlsx file reading and generating html files
				 */
				
				String currentTestDescription = current_suite_xls.getCellData(Constants.TEST_CASES_SHEET, Constants.DESCRIPTION,currentTestCaseID);
				String currentTestRunmode = current_suite_xls.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE,currentTestCaseID);
				//System.out.println(currentTestName+"==="+currentTestDescription+"==="+currentTestRunmode);
				
				out_test_cases.write("<tr><td width=20% align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>");
				out_test_cases.write("<a href=" + currentTestStepsFileName.replace(" ", "%20")+ "_steps.html>" + currentTestName + "</a>");
				out_test_cases.write("</b></td><td width=40% align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>");
				out_test_cases.write(currentTestDescription);
				out_test_cases.write("</b></td><td width=40% align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>");
				out_test_cases.write(currentTestRunmode);
				out_test_cases.write("</b></td><td width=40% align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>");
				
				if(passOfFail == "FAIL")
				{
					out_test_cases.write("FAIL");
				}
				else
					
					out_test_cases.write("PASS");
					
				//out_test_cases.write("</b></tr>");
				//System.out.println("suit result >>>>>>>>>>>>>> "+suite_result);
			}
			//System.out.println(">>>>>>>> :: " +PASS_TEST_CASE_COUNT);
			TOTAL_PASS = TOTAL_PASS + PASS_TEST_CASE_COUNT/(rowsOfCases-1);
			PASS_TEST_CASE_COUNT=0;
			//System.out.println("<<<<<<<<< :: " +TOTAL_PASS+ " :::: "+ rowsOfCases);
			TOTAL_FAIL = TOTAL_FAIL + FAIL_TEST_CASE_COUNT/(rowsOfCases-1);
			FAIL_TEST_CASE_COUNT=0;
			out_test_cases.write("</table>");
			out_test_cases.close();
			out.write("<tr><td width=20% align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>");
			out.write("<a href=" + testCasePageName.replace(" ", "%20")+ "_steps.html>" + currentTestSuite + "</a>");
			out.write("</b></td><td width=40% align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>");
			out.write(suiteXLS.getCellData(Constants.TEST_SUITE_SHEET,
					Constants.DESCRIPTION, currentSuiteID));
			out.write("</b></td></tr>");
			
			/**
			 * Pls enable this commnts, if u want to see executable result in suit file.
			 */
			/*out.write("<td width=10% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>EXECUTION RESULT</b></td></tr>");
			out.write("<td width=10% align=center  bgcolor=");
			if (suiteXLS.getCellData(Constants.TEST_SUITE_SHEET,
					Constants.RUNMODE, currentSuiteID).equalsIgnoreCase(
					Constants.RUNMODE_YES))
				if (suite_result.equals("FAIL"))
					out.write("red><FONT COLOR=153E7E FACE=Arial SIZE=2><b>FAIL</b></td></tr>");
				else
					out.write("green><FONT COLOR=153E7E FACE=Arial SIZE=2><b>PASS</b></td></tr>");
			else
				out.write("yellow><FONT COLOR=153E7E FACE=Arial SIZE=2><b>SKIP</b></td></tr>");*/
		}
		out.write("</table></br>");
		out.write("<h4> <FONT COLOR=660000 FACE= Arial  SIZE=4.5> "+ project +" Execution Summary </h4>");
		out.write("<table  border=1 cellspacing=1 cellpadding=1>");
		out.write("<tr><td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Total Suites</b></td><td width=150 align=left><FONT COLOR=#031e1b FACE=Arial SIZE=2.75><b>");
		Integer suitCount = totalTestSuites - 1;
		out.write(suitCount.toString());
		System.out.println("Total Suites  ------>>"+suitCount );
		out.write("</b></td></tr><tr><td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Total Test Scenarios</b></td><td width=150 align=left><FONT COLOR=#031e1b FACE=Arial SIZE=2.75><b>");
		out.write(TOTAL_TEST_CASES_COUNT.toString());
		System.out.println("Total Test Scenarios  ------>>"+TOTAL_TEST_CASES_COUNT );
		out.write("</b></td></tr><tr><td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Passed Test Scenarios</b></td><td width=150 align=left><FONT COLOR=#031e1b FACE=Arial SIZE=2.75><b>");
		out.write(passedScenarios.toString());
		System.out.println("Passed Test Scenarios  ------>>"+passedScenarios );
		out.write("</b></td></tr><tr><td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Failed Test Scenarios</b></td><td width=150 align=left><FONT COLOR=#ff0000 FACE=Arial SIZE=2.75><b>");
		out.write(failedScenarios.toString());
		System.out.println("Failed Test Scenarios  ------>>"+failedScenarios );
		out.write("</b></td></tr><tr><td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Total Executed Test Steps</b></td><td width=150 align=left><FONT COLOR=#031e1b FACE=Arial SIZE=2.75><b>");
		Integer countOfTestCases = TOTAL_PASS + TOTAL_FAIL; 
		out.write(countOfTestCases.toString());
		System.out.println("Total Test Steps  ------>>"+countOfTestCases );
		out.write("</b></td></tr><tr><td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Passed Test Steps</b></td><td width=150 align=left><FONT COLOR=#031e1b FACE=Arial SIZE=2.75><b>");
		out.write(TOTAL_PASS.toString());
		System.out.println("Passed Test Steps  ------>>"+TOTAL_PASS );
		out.write("</b></td></tr><tr><td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>Failed Test Steps</b></td><td width=150 align= left ><FONT COLOR=#ff0000 FACE= Arial  SIZE=2.75><b>");
		out.write(TOTAL_FAIL.toString());
		System.out.println("Failed Test Steps  ------>>"+TOTAL_FAIL );
		out.write("</b></td></tr></table>");
		out.close();
		
	} catch(Exception e){
		
		//System.out.println("Exception got :: " + e.getMessage());
	}
		
		
	}
}


