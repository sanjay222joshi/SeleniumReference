package com.sanjay.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.testng.log4testng.Logger;

import com.sanjay.base.DriverFactory;

public class Reportor {
	private static final Logger log = Logger.getLogger(Reportor.class);
	public static String SCREEN_SHOTS_LOCATION = System.getProperty("user.dir")
			+ File.separator + "test-output" + File.separator;
	public static Date startDate = new Date();
	public static Date endDate = new Date();
	public static int passCounter = 0;
	public static int failCounter = 0;
	public static int skipCounter = 0;
	public static List<ReportDetailStatus> reportDetailsStatus = new ArrayList<ReportDetailStatus>();
	public static List<ReportDetailStatus> reportDetailsStatusForIE = new ArrayList<ReportDetailStatus>();
	public static List<ReportDetailStatus> reportDetailsStatusForFireFox = new ArrayList<ReportDetailStatus>();
	public static List<ReportDetailStatus> reportDetailsStatusForChrome = new ArrayList<ReportDetailStatus>();
	public static List<ReportDetailStatus> reportDetailsStatusForUnKnownBrowser = new ArrayList<ReportDetailStatus>();
	// public TimeUnit TimeUnt;
	private static SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
	private static String browserName = "";
	public static String testName = System.getProperty("xml").toUpperCase();

	public static String resultsVar = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
			+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
			+ "<head> <meta http-equiv=\"content-type\" charset=\"ISO-8859-1\" content=\"text/html;\">"
			+ "<title>Test Report</title>"

			+ "</head>"
			+ "<style type=\"text/css\">"
			+ "body {"
			+ "background:none repeat scroll 0 0 #E6EAE9;"
			+ "color:#4F6B72;"
			+ "}"
			+ ""
			+ "a {"
			+ "color:#C75F3E;"
			+ "}"
			+ "#mytable {"
			+ "margin:0;"
			+ "padding:0;"
			+ "width:700px;"
			+ "}"
			+ "caption {"
			+ "font:italic 11px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "padding:0 0 5px;"
			+ "text-align:right;"
			+ "width:700px;"
			+ "}"
			+ ""
			+ "th {"
			+ "background:url(\"images/bg_header.jpg\") no-repeat scroll 0 0 #4F6B72;"
			+ "border:1px solid #C1DAD7;"
			+ "color:#FFFFFF;"
			+ "font:bold 11px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "letter-spacing:2px;"
			+ "padding:6px 6px 6px 12px;"
			+ "text-align:center;"
			+ "text-transform:uppercase;"
			+ "}"
			+ ""
			+ "th.header {"
			+ "font:bold 16px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "}"
			+ "th.nobg {"
			+ "background:none repeat scroll 0 0 transparent;"
			+ "}"
			+ "td {"
			+ "background:none repeat scroll 0 0 #FFFFFF;"
			+ "border:1px solid #C1DAD7;"
			+ "color:#4F6B72;"
			+ "padding:1px;"
			+ "}"
			+ "td.step {"
			+ "background:none repeat scroll 0 0 #FFFFFF;"
			+ "border:1px solid #C1DAD7;"
			+ "color:#000000;"
			+ "font:14px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "padding:1px;"
			+ "}"
			+ "td.result_text {"
			+ "background:none repeat scroll 0 0 #FFFFFF;"
			+ "border:1px solid #C1DAD7;"
			+ "color:#000000;"
			+ "font:14px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "padding:1px;"
			+ "text-align:center;"
			+ "}"
			+ ""
			+ "td.result_date {"
			+ "background:none repeat scroll 0 0 #FFFFFF;"
			+ "border:1px solid #C1DAD7;"
			+ "color:#000000;"
			+ "font:10px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "padding:1px;"
			+ "text-align:left;"
			+ "}"
			+ ""
			+ ""
			+ "td.result_pass {"
			+ "background:none repeat scroll 0 0 ;"
			+ "border-bottom:1px solid #C1DAD7;"
			+ "border-right:1px solid #C1DAD7;"
			+ "color:#088A4B;"
			+ "font:bold 12px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "padding:1px;"
			+ "text-align:left;"
			+ ""
			+ "}"
			+ ""
			+ "td.result_pper {"
			+ "background:none repeat scroll 0 0 #088A4B;"
			+ "border-bottom:1px solid #C1DAD7;"
			+ "border-right:1px solid #C1DAD7;"
			+ "color:#FFFFFF;"
			+ "font:bold 12px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "padding:1px;"
			+ "text-align:center;"
			+ "text-transform:uppercase;"
			+ "}"
			+ ""
			+ "td.result_skiper {"
			+ "background:none repeat scroll 0 0 #848484;"
			+ "border-bottom:1px solid #C1DAD7;"
			+ "border-right:1px solid #C1DAD7;"
			+ "color:#FFFFFF;"
			+ "font:bold 12px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "padding:1px;"
			+ "text-align:center;"
			+ "text-transform:uppercase;"
			+ "}"
			+ ""
			+ "td.result_fper {"
			+ "background:none repeat scroll 0 0 #9E1212;"
			+ "border-bottom:1px solid #C1DAD7;"
			+ "border-right:1px solid #C1DAD7;"
			+ "color:#FFFFFF;"
			+ "font:bold 12px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "padding:1px;"
			+ "text-align:center;"
			+ "text-transform:uppercase;"
			+ "} "
			+ ""
			+ ""
			+ "td.result_fail {"
			+ "background:none repeat scroll 0 0 ;"
			+ "border-bottom:1px solid #C1DAD7;"
			+ "border-right:1px solid #C1DAD7;"
			+ "color:#9E1212;"
			+ "font:bold 12px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "padding:1px;"
			+ "text-align:left;"
			+ ""
			+ "}"
			+ ""
			+ "td.result_skip {"
			+ "background:none repeat scroll 0 0 ;"
			+ "border-bottom:1px solid #C1DAD7;"
			+ "border-right:1px solid #C1DAD7;"
			+ "color:#2E9AFE;"
			+ "font:bold 12px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "padding:1px;"
			+ "text-align:left;"
			+ ""
			+ "}"
			+ ""
			+ "td.result_info {"
			+ "background:none repeat scroll 0 0 ;"
			+ "border-bottom:1px solid #C1DAD7;"
			+ "border-right:1px solid #C1DAD7;"
			+ "color:#2E2EFE;"
			+ "font:bold 12px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "padding:1px;"
			+ "text-align:left;"
			+ "}"
			+ ""
			+ ""
			+ "td.alt {"
			+ "background:none repeat scroll 0 0 #C1DAD7;"
			+ "color:#000000;"
			+ "font:bold 12px \"Trebuchet MS\",Verdana,Arial,Helvetica,sans-serif;"
			+ "text-align:center;"
			+ "text-transform:uppercase;"
			+ "}"
			+ ""
			+ "}"
			+ ".black_overlay{"
			+ "	display: none;"
			+ "	position: absolute;"
			+ "	top: 0%;"
			+ "	left: 0%;"
			+ "	width: 100%;"
			+ "	height: 100%;"
			+ "	background-color: black;"
			+ "	z-index:1001;"
			+ "	-moz-opacity: 0.8;"
			+ "	opacity:.80;"
			+ "	filter: alpha(opacity=80);"
			+ "}"
			+ ".white_content {"
			+ "	display: none;"
			+ "	position: absolute;"
			+ "	left: 25%;"
			+ "	width: 50%;"
			+ "	height: 30%;"
			+ "	padding: 16px;"
			+ "	border: 5px  solid orange;"
			+ "	background-color: white;"
			+ "	z-index:1002;"
			+ "	overflow: auto;"
			+ "}"
			+ "</style>"
			+ "<body>"
			+ "<script type=\"text/javascript\" language=\"JavaScript\">"

			+ "function toggle(element) {    if (document.getElementById(element).style.display == 'none') {"
			+ "document.getElementById(element).style.display = 'block';} else { document.getElementById(element).style.display = 'none';}}"
			+ "</script> "
			+ "<br>"
			+ "<center>"
			+ "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"800\"><tbody><tr><td><table cellspacing=\"2\" cellpadding=\"2\" border=\"0\" width=\"100%\">"
			+ "<tbody>"
			+ "<tr>"
			+ "<th align=\"right\" class=\"header\">Test Execution Report</th>"
			+ "</tr>"
			+ "</tbody>"
			+ "</table>"
			+ "<br>"
			+ "<center>"
			+ "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"95%\">"
			+ "<tbody>"
			+ "</tbody></table>"
			+ "<center>"
			+ "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"98%\">"
			+ "<tbody>"
			+ "      <tr>"
			+ "      <th width=\"15%\">Script Name:</th>"
			+ "      <td align=\"center\" width=\"35%\" colspan=\"1\">"
			+ testName
			+ "</td>"
			+ "<th width=\"15%\">Test Date:</th>"
			+ "<td align=\"center\" width=\"35%\" colspan=\"3\" id=\"testDate\"> "
			+ sdf.format(startDate)
			+ " </td>"
			+ "      </tr>"
			+ "      <tr>"
			+ "      <th width=\"19%\">Test Start Time:</th>"
			+ "      <td align=\"center\" width=\"38%\" id=\"testStartTime\">"
			+ CommonUtils.getTimeFromMilliSeconds(startDate.getTime())
			+ " </td>" + "      <th width=\"17%\">Test End Time:</th>";

	public static void addBrowserRelatedResults(
			List<ReportDetailStatus> rdetails, PrintWriter out) {
		Collections.sort(rdetails, new sortTestCase());

		for (ReportDetailStatus reportDetailStatus : rdetails) {

			out.print("<tr> <td width=\"35%\" class=\"result_date\" colspan=\"1\">"
					+ reportDetailStatus.getDescription());
			if (reportDetailStatus.getStatus().equalsIgnoreCase("Fail")) {
				out.print("<td width=\"10%\" class=\"result_fail\" colspan=\"1\">"
						+ "<center>"
						+ reportDetailStatus.getStatus()
						+ "</center> </b></td>");
			}

			if (reportDetailStatus.getStatus().equalsIgnoreCase("Pass")) {
				out.print("<td width=\"10%\" class=\"result_pass\" colspan=\"1\">"
						+ "<center>"
						+ reportDetailStatus.getStatus()
						+ "</center> </b></td>");
			}

			if (reportDetailStatus.getStatus().equalsIgnoreCase("Skipped")) {
				out.print("<td width=\"10%\" class=\"result_skip\" colspan=\"1\">"
						+ "<center>"
						+ reportDetailStatus.getStatus()
						+ "</center> </b></td>");
			}
			if (reportDetailStatus.getStatus().equalsIgnoreCase("Pass")) {
				out.print("<td width=\"35%\" class=\"result_pass\" colspan=\"6\">"
						+ reportDetailStatus.getReason()
						+ "</b> </font> </td></tr>");
			} else if (reportDetailStatus.getStatus().equalsIgnoreCase("Fail")
					|| reportDetailStatus.getStatus().equalsIgnoreCase(
							"Skipped")) {

				String randomValue1 = RandomValue.getRandomStringOfNumbers(5);
				String randomValue2 = RandomValue.getRandomStringOfNumbers(5);

				String filePath = "screenshots/"
						+ browserName
						+ "/"
						+ uniqueFileNameFrom(reportDetailStatus
								.getDescription()) + ".png";

				String reasonBox = "<a href = \"javascript:void(0)\" onclick = \"document.getElementById('"
						+ randomValue1
						+ "').style.display='block';document.getElementById('"
						+ randomValue2
						+ "').style.display='block'\">Reason</a>";
				reasonBox = reasonBox + "&nbsp; <a target=\"_blank\" href="
						+ filePath + ">ScreenShot</a>";
				reasonBox = reasonBox
						+ "<div id=\""
						+ randomValue1
						+ "\" class=\"white_content\" align=\"left\">"
						+ reportDetailStatus.getReason()
						+ " <a href = \"javascript:void(0)\" onclick = \"document.getElementById('"
						+ randomValue1
						+ "').style.display='none';document.getElementById('"
						+ randomValue2
						+ "').style.display='none'\"><b><font size=\"5\"><center>Close</center></font></a></div>";
				reasonBox = reasonBox + "<div id=\"" + randomValue2
						+ "\" class=\"black_overlay\"></div>";

				out.print("<td width=\"35%\" class=\"result_fail\" colspan=\"6\">"
						+ "<center>"
						+ reasonBox
						+ "</center> </b> </font> </td></tr>");
			} else if (reportDetailStatus.getStatus().equalsIgnoreCase(
					"Skipped")) {

				out.print("<td width=\"35%\" class=\"result_fail\" colspan=\"6\">"
						+ reportDetailStatus.getReason()
						+ "</b> </font> </td></tr>");
			}
		}
	}

	public static String stringToHTMLString(String string) {
		StringBuffer sb = new StringBuffer(string.length());
		// true if last char was blank
		boolean lastWasBlankChar = false;
		int len = string.length();
		char c;

		for (int i = 0; i < len; i++) {
			c = string.charAt(i);
			if (c == ' ') {
				// blank gets extra work,
				// this solves the problem you get if you replace all
				// blanks with &nbsp;, if you do that you loss
				// word breaking
				if (lastWasBlankChar) {
					lastWasBlankChar = false;
					sb.append("&nbsp;");
				} else {
					lastWasBlankChar = true;
					sb.append(' ');
				}
			} else {
				lastWasBlankChar = false;
				//
				// HTML Special Chars
				if (c == '"') {
					sb.append("&quot;");
				} else if (c == '&') {
					sb.append("&amp;");
				} else if (c == '<') {
					sb.append("&lt;");
				} else if (c == '>') {
					sb.append("&gt;");
				} else if (c == '\n') {
					// Handle Newline
					sb.append("&lt;br/&gt;");
				} else {
					int ci = 0xffff & c;
					if (ci < 160) {
						// nothing special only 7 Bit
						sb.append(c);
					} else {
						// Not 7 Bit use the unicode system
						sb.append("&#");
						sb.append(Integer.valueOf(ci).toString());
						sb.append(';');
					}
				}
			}
		}
		return sb.toString();
	}

	/*
	 * public void setResultsVar(String resultsVar) { this.resultsVar =
	 * resultsVar; }
	 */

	public static void addResults(PrintWriter out) throws IOException {
		out.println("</table> ");
		String table = "      <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"98%\">"
				+ "      <tbody>"
				+ "      <tr>"
				+ "      <th width=\"100%\" class=\"header\" colspan=\"8\">";

		if (!reportDetailsStatusForFireFox.isEmpty()) {
			// <table> <th> <a href=\"#\" onclick=\"toggle(event,
			// 'firefox');\">FireFox - show/hide</a> </th>
			out.println(table
					+ "<a href=\"#\" onclick=\"toggle('firefox');\">FireFox - show/hide</a> </th>"
					+ "      </tr> </table> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"98%\" id=\"firefox\" style=\"display:none;\">");
			browserName = "firefox";
			addBrowserRelatedResults(reportDetailsStatusForFireFox, out);

			out.println("</table>");
		}

		if (!reportDetailsStatusForIE.isEmpty()) {
			out.println(table
					+ " <a href=\"#\" onclick=\"toggle('ie');\">IE - show/hide</a> </tr> </th> </table> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"98%\"  id=\"ie\" style=\"display:none;\">");
			browserName = "iexplore";
			addBrowserRelatedResults(reportDetailsStatusForIE, out);
			out.println("</table>");
		}

		if (!reportDetailsStatusForChrome.isEmpty()) {
			out.println(table
					+ " <a href=\"#\" onclick=\"toggle('chrome');\">Chrome - show/hide</a> </tr> </th> </table> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"98%\"  id=\"chrome\" style=\"display:none;\">");
			browserName = "chrome";
			addBrowserRelatedResults(reportDetailsStatusForChrome, out);
			out.println("</table>");
		}

		if (!reportDetailsStatusForUnKnownBrowser.isEmpty()) {
			out.println(table
					+ " <a href=\"#\" onclick=\"toggle('unknownbrowser');\">UnKnown - show/hide</a> </tr> </th> </table> <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"98%\"  id=\"unknownbrowser\" style=\"display:none;\">");
			browserName = "iexplore";
			addBrowserRelatedResults(reportDetailsStatusForUnKnownBrowser, out);
			out.println("</table>");
		}

		//
		// for (ReportDetailStatus reportDetailStatus : reportDetailsStatus) {
		//
		// out.print("<tr> <td width=\"15%\" class=\"result_date\"
		// colspan=\"1\">"
		// + reportDetailStatus.getDescription());
		// if (reportDetailStatus.getStatus().equalsIgnoreCase("Fail")) {
		// out.print("<td width=\"30%\" class=\"result_fail\" colspan=\"1\">" +
		// "<center>"
		// + reportDetailStatus.getStatus() + " </b></td>");
		// }
		//
		// if (reportDetailStatus.getStatus().equalsIgnoreCase("Pass")) {
		// out.print("<td width=\"30%\" class=\"result_pass\" colspan=\"1\">" +
		// "<center>"
		// + reportDetailStatus.getStatus() + " </b></td>");
		// }
		//
		// if (reportDetailStatus.getStatus().equalsIgnoreCase("Skipped")) {
		// out.print("<td width=\"30%\" class=\"result_skip\" colspan=\"1\">" +
		// "<center>"
		// + reportDetailStatus.getStatus() + " </b></td>");
		// }
		// if (reportDetailStatus.getStatus().equalsIgnoreCase("Pass")) {
		// out.print("<td width=\"55%\" class=\"result_pass\" colspan=\"6\">" +
		// reportDetailStatus.getReason()
		// + "</b> </font> </td></tr>");
		// } else if (reportDetailStatus.getStatus().equalsIgnoreCase("Fail")) {
		// out.print("<td width=\"55%\" class=\"result_fail\" colspan=\"6\">" +
		// reportDetailStatus.getReason()
		// + "</b> </font> </td></tr>");
		// } else if
		// (reportDetailStatus.getStatus().equalsIgnoreCase("Skipped")) {
		// out.print("<td width=\"55%\" class=\"result_fail\" colspan=\"6\">" +
		// reportDetailStatus.getReason()
		// + "</b> </font> </td></tr>");
		// }
		//
		// }

	}

	public static void writeReport() {
		/**
		 * Write into csv file.
		 */
		// Collections.sort(reportDetailsStatus, new
		// Comparator<ReportDetailStatus>() {
		//
		// public int compare(ReportDetailStatus o1, ReportDetailStatus o2) {
		// return o1.getMethodeName().compareTo(o2.getMethodeName());
		// }
		// });
		// for (ReportDetailStatus getMethod : reportDetailsStatus) {
		// System.out.println("---------Method------" +
		// getMethod.getMethodeName());
		// String testMethod[] = getMethod.getMethodeName().split(":");
		// System.out.println("---------MethodTD------" + testMethod[2]);
		// }
		try {
			String timeStamp = DateUtil.getTimeStamp();
			timeStamp = timeStamp.replaceAll("[\\/\\-\\:]", "_");
			// String fileName = SCREEN_SHOTS_LOCATION + "Result_" + timeStamp +
			// ".html";
			String fileName = SCREEN_SHOTS_LOCATION + "Result.html";
			long millis = endDate.getTime() - startDate.getTime();
			// This will not work fro andriod as the TimeUtil in Andriod is old
			// and does not
			// comtain below methods
			/*
			 * String z = String.format( "%d hr, %d min, %d sec",
			 * TimeUnt.MILLISECONDS.toHours(millis),
			 * TimeUnit.MILLISECONDS.toMinutes(millis),
			 * TimeUnit.MILLISECONDS.toSeconds(millis) -
			 * TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
			 * .toMinutes(millis))); System.out.println(z);
			 */

			String midString = "<th width=\"15%\">Executed By :</th>"
					+ "<td align=\"center\" width=\"35%\" colspan=\"3\" id=\"testedBy\">"
					+ getCompName()
					+ "</td>"
					+ "      </tr>"
					// + " <tr>"
					// + " <th width=\"15%\">Browser:</th>"
					// + " <td align=\"center\" width=\"35%\">"
					// + PropertiesUtil.getProperty("browser")
					// + "</td>"
					// + " <th width=\"15%\">Env:</th>"
					// + " <td align=\"center\">"
					// + PropertiesUtil.getProperty("environment")
					// + "</td>"
					// + " </tr>"
					+ "      </tbody></table>"
					+ "      </center>"
					+ "</center>"
					// + "<br>"
					+ "<br>"
					+ "<center><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"95%\"><tbody>"
					+ "</tbody></table>"
					+ "<center>"
					+ "      <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"98%\">"
					+ "      <tbody>"
					+ "      <tr>"
					+ "      <th width=\"100%\" class=\"header\" colspan=\"4\">Summary Report</th>"
					+ "      </tr>"
					+ "      <tr>"
					+ "      <td width=\"25%\" class=\"alt\">Total Test Cases:</td>"
					+ "      <td width=\"25%\" class=\"alt\">Test Cases Passed:</td>"
					+ "      <td width=\"25%\" class=\"alt\">Test Cases Failed:</td>"
					+ "      <td width=\"25%\" class=\"alt\">Test Cases Skipped:</td>"
					+ "      </tr><tr><td width=\"25%\" class=\"result_text\">"
					+ (passCounter + failCounter + skipCounter)
					+ "</td>"
					+ "      <td width=\"25%\" class=\"result_pper\">"
					+ passCounter
					+ "</td>"
					+ "      <td width=\"25%\" class=\"result_fper\">"
					+ failCounter
					+ "</td>"
					+ "      <td width=\"25%\" class=\"result_skiper\">"
					+ skipCounter
					+ "</td>"
					+ "      </tr>"
					+ "      </tbody></table>"
					+ "      </center>"
					+ "</center>"
					+ "<br>"
					+ "<br>"
					+ getCustomPieChart()
					+ "<center><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"95%\"><tbody>"
					+ "</tbody></table>"
					+ "<center>"
					+ "      <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"98%\">"
					+ "      <tbody>"
					+ "      <tr>"
					+ "      <th width=\"100%\" class=\"header\" colspan=\"8\">Detailed Results Report</th>"
					+ "      </tr>"
					+ "      <tr>"
					+ "      <td width=\"35%\" class=\"alt\" colspan=\"1\">Test Name</td>"
					+ "      <td width=\"10%\" class=\"alt\" colspan=\"1\">Status</td>"
					+ "      <td width=\"35%\" class=\"alt\" colspan=\"6\">Reason</td>";

			String endstring = "<td align=\"center\" colspan=\"3\">"
					+ CommonUtils.getTimeFromMilliSeconds(endDate.getTime())
					+ "</td></tr>"
					+ "      <tr>"
					+ "      <th width=\"15%\">Duration</th>"
					+ "      <td align=\"center\" width=\"35%\" colspan=\"1\"id=\"timeDur\">"
					+ CommonUtils.getDurationBreakdown(millis) + "</td>";

			PrintWriter pw = new PrintWriter(new File(fileName));
			pw.print(resultsVar + endstring + midString + "\n");
			addResults(pw);
			pw.print("</body></html>");
			pw.close();
			/*
			 * File file = new File(fileName); boolean b = file.createNewFile();
			 * BufferedWriter out = new BufferedWriter(new
			 * FileWriter(fileName)); out.write(resultsVar + endstring +
			 * midString + "\n"); addResults(out); out.close();
			 */
			// for (ReportDetailStatus rds : reportDetailsStatus) {
			// writeResultToCSV(rds);
			// }
		} catch (IOException e) {
			log.info(" ////Exception Occured  : " + e);
		}
	}

	public static String getCompName() {
		try {
			String computername = InetAddress.getLocalHost().getHostName()
					+ "/" + System.getProperty("user.name");
			return computername;
		} catch (Exception e) {
			log.info("Exception caught =" + e.getMessage());
			return null;
		}
	}

	public static String getCustomPieChart() {
		String pieChart = "<center><section><div> <canvas id=\"canvas\" width=\"200\" height=\"200\"> 	This text is displayed if your browser does not support HTML5 Canvas. 	</canvas> 	</div> ";
		pieChart = pieChart + "	<script type=\"text/javascript\">";
		pieChart = pieChart
				+ "var data = ["
				+ passCounter
				+ ", "
				+ failCounter
				+ ","
				+ skipCounter
				+ "]; 	var colors = [\"#088A4B\",\"#9E1212\",\"#ECD078\"]; 		var labels = [\"Pass\", \"Fail\", \"Skipped\"];";
		pieChart = pieChart
				+ "function getTotal(){var myTotal = 0;for (var j = 0; j < data.length; j++) {	myTotal += (typeof data[j] == 'number') ? data[j] : 0;	}return myTotal;}";
		pieChart = pieChart
				+ "function plotData() {canvas = document.getElementById(\"canvas\");var context = canvas.getContext(\"2d\");for (var i = 0; i < data.length; i++) {  drawSegment(canvas, context, i);	}}";
		pieChart = pieChart
				+ "function getLcd(){var total = 0;for(var i=0;i<data.length;i++)total = total + data[i];return 360/total;}";
		pieChart = pieChart
				+ "function degreesToRadians(degrees) {    return (degrees * Math.PI)/180;	}";
		pieChart = pieChart
				+ "function sumTo(a, i) {  var sum = 0;    for (var j = 0; j < i; j++) {     sum += a[j]*getLcd();   }   return sum;}";
		pieChart = pieChart
				+ "function drawSegment(canvas, context, i) {   context.save();    var centerX = Math.floor(canvas.width / 2);    var centerY = Math.floor(canvas.height / 2);  radius = Math.floor(canvas.width / 2);";
		pieChart = pieChart
				+ " var startingAngle = degreesToRadians(sumTo(data, i),i);  var arcSize = degreesToRadians(data[i]*getLcd(),i);   var endingAngle = startingAngle + arcSize;  context.beginPath();";
		pieChart = pieChart
				+ " context.moveTo(centerX, centerY); context.arc(centerX, centerY, radius,startingAngle, endingAngle, false); context.closePath(); context.fillStyle = colors[i]; context.fill();  context.restore();";
		pieChart = pieChart + "drawSegmentLabel(canvas, context, i);}";
		pieChart = pieChart
				+ "function drawSegmentLabel(canvas, context, i) { if(data[i] <= 0) return; context.save();  var x = Math.floor(canvas.width / 2); var y = Math.floor(canvas.height / 2);";
		pieChart = pieChart
				+ " var angle = degreesToRadians(sumTo(data, i)); context.translate(x, y); context.rotate(angle); var dx = Math.floor(canvas.width * 0.5) -30; var dy = Math.floor(canvas.height * 0.05);";
		pieChart = pieChart
				+ "context.textAlign = \"right\"; var fontSize = Math.floor(canvas.height / 25); context.font = fontSize + \"pt Helvetica\";context.fillText(labels[i]+\" (\"+data[i]+\")\", dx, dy);context.restore();}";
		pieChart = pieChart + "		plotData();</script></section></center><br>";

		return pieChart;
	}

	private static void writeResultToCSV(ReportDetailStatus result) {
		System.out
				.println("Inside writeResultToCSV " + result.getDescription());
		try {
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(System.getProperty("user.dir")
							+ "\\test-output\\" + "Report.csv", true), "UTF-8");
			BufferedWriter fbw = new BufferedWriter(writer);
			String testMethod[] = result.getMethodeName().split(":");
			if (testMethod.length > 1) {
				if (result.getReason().contains("Actaul")) {
					fbw.write(testMethod[0]
							+ ","
							+ testMethod[2]
							+ ","
							+ result.getStatus()
							+ ","
							+ result.getReason().split("Actual :")[1]
									.split("<")[0]
							+ ","
							+ result.getReason().split("Expected :")[1]
									.split("<")[0]);
				} else {
					fbw.write(testMethod[0] + "," + "" + ","
							+ result.getStatus() + "," + result.getReason());
				}
			} else {
				System.out.println("Inside Else..");
				fbw.write(testMethod[0] + "," + "" + "," + result.getStatus()
						+ "," + result.getReason());
			}
			fbw.newLine();
			fbw.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

	}

	public static void setReportDetailStatus(
			ReportDetailStatus lreportDetailStatus) {
		try {
			DriverFactory.getDriver();

		} catch (Exception e) {
			reportDetailsStatusForUnKnownBrowser.add(lreportDetailStatus);
			return;
		}

		switch (DriverFactory.getBrowser()) {
		case firefox:
		case firefox_parallel:
			reportDetailsStatusForFireFox.add(lreportDetailStatus);
			break;
		case iexplore:
		case iexplore_parallel:
		case iexplore_remote:
			reportDetailsStatusForIE.add(lreportDetailStatus);
			break;
		case chrome:
			reportDetailsStatusForChrome.add(lreportDetailStatus);
			break;
		}

		reportDetailsStatus.add(lreportDetailStatus);
	}

	private static String uniqueFileNameFrom(String fileName) {
		fileName = fileName.replaceAll("[^a-zA-Z0-9]+", "_"); // fileName.replaceAll("[\\^\\\\.\\-:;#_]",
		// );
		if (fileName.length() > 256) {
			return fileName.substring(0, 50);
		} else {
			return fileName;
		}

	}
}

// Sorts by Test Case Description.
class sortTestCase implements Comparator<ReportDetailStatus> {
	public int compare(ReportDetailStatus o1, ReportDetailStatus o2) {
		String testcase1 = ((ReportDetailStatus) o1).getDescription();
		String testcase2 = ((ReportDetailStatus) o2).getDescription();
		return testcase1.compareTo(testcase2);
	}
}
