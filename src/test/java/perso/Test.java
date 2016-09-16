package perso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.InputSource;

import com.tibco.exchange.tibreview.Main;
import com.tibco.exchange.tibreview.common.Util;
import com.tibco.exchange.tibreview.model.rules.Tibrules;
import com.tibco.exchange.tibreview.parser.RulesParser;
import com.tibco.exchange.tibreview.processor.processrule.XPathProcessor;

public class Test {

//	private static String[] main = { "-r", "src/test/resources/testrules.xml", "-c",
//			"src/test/resources/config.properties", "-i", "project", "-s",
//			"src/test/resources", "-o", "pmd", "-t", "C:/tmp/output" };
	
	private static String[] main = { "-r", "src/test/resources/testrules.xml", "-c",
			"src/test/resources/config.properties", "-i", "project", "-s",
			"C:/pro/workspace/tibco_workspace2/Test", "-o", "pmd", "-t", "C:/tmp/output" };

	private static final String PROCESSES_PACKAGE = "Processes";
	private static final String PACKAGE_DELIMITER = ".";
	private static final String PROCESS_EXTENSION = ".bwp";
	
	public static void main(String[] args) throws Exception {
		// Test.parsing();
		// Trash.list();
		// Trash.xpath();
		//Trash.regexp();
//		String tmp = "adzaddza.dzadza.Processes.package.subpackage.Test.bwp";
//		String PROCESS_PACKAGE_DELIMITER = PACKAGE_DELIMITER + PROCESSES_PACKAGE + PACKAGE_DELIMITER;
//
//		tmp = tmp.substring(0, tmp.indexOf(PROCESS_EXTENSION));
//		int a = tmp.indexOf(PROCESS_PACKAGE_DELIMITER);
//		int b = tmp.lastIndexOf(PACKAGE_DELIMITER);
//		
//		System.out.println(tmp.substring(a + PROCESS_PACKAGE_DELIMITER.length(), b));
//		System.out.println(tmp.substring(b + 1, tmp.length()));
//		
//		
//		
		Main.main(main);
		
		//ProcessSource ps = new ProcessSource(new File("src/test/resources/Processes/Process.bwp"));
		//System.out.println(ps.getProcessModel());
	}
	
	public static void list() throws Exception {
		System.out.println(Util.listFile("C:/pro/workspace/tibco_workspace2/", ".bwp"));
	}

	public static void parsing() throws Exception {
		Tibrules rules = RulesParser.getInstance().parseFile("src/test/resources/tibrules.xml");
		System.out.println(rules.getProcess().getRule().get(0).getImpl().getXpath());
	}

	public static final String[] TIBCO_NAMESPACES = { "bpws",
			"http://docs.oasis-open.org/wsbpel/2.0/process/executable", "tibex",
			"http://www.tibco.com/bpel/2007/extensions" };

	public static void xpath() throws Exception {
		InputSource is = new InputSource("C:/tmp/emp.xml");
		XPathProcessor processor = XPathProcessor.getInstance();
		System.out.println(processor.eval(is, "count(/bpws:process/tibex:ProcessInfo) >= 1"));
	}

	public static void regexp() throws Exception {
		String input = "aaaa%AFZAFAZ%%%X";
		Pattern p = Pattern.compile("\\%(.*?)\\%");
		Matcher m = p.matcher(input);
		while (m.find()) {
			String found = m.group(1);
			System.out.println(found);
			input = input.replaceAll("\\%"+found+"\\%", found);
			System.out.println("input="+input);
		}
	}
}
