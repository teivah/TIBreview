
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

import com.tibco.exchange.tibreview.Main;
import com.tibco.exchange.tibreview.common.Util;
import com.tibco.exchange.tibreview.model.Tibrules;
import com.tibco.exchange.tibreview.model.parser.RulesParser;
import com.tibco.exchange.tibreview.processor.XPathProcessor;

@SuppressWarnings("unused")
public class Trash {
	public static void main(String[] args) throws Exception {
		// Test.parsing();
		// Trash.list();
		// Trash.xpath();
		
		String[] main = { "-r", "C:/pro/workspace/java_workspace/tibreview/src/test/resources/tibrules.xml", "-c",
				"C:/pro/workspace/java_workspace/tibreview/src/test/resources/config.properties", "-i", "project", "-s",
				"C:/pro/workspace/tibco_workspace2/Test", "-o", "csv", "-t", "C:/tmp/output" };
		Main.main(main);
	}

	public static void list() throws Exception {
		System.out.println(Util.listFile("C:/pro/workspace/tibco_workspace2/", ".bwp"));
	}

	public static void parsing() throws Exception {
		Tibrules rules = RulesParser.parseFile("src/test/resources/tibrules.xml");
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
}
