package perso;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.tibco.exchange.tibreview.model.sax.PartnerLinkModel;
import com.tibco.exchange.tibreview.parser.ProcessHandler;

public class SaxTest {
	public static void main(String[] args) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			InputStream xmlInput = new FileInputStream("src/testrules/Test.bwp");

			SAXParser saxParser = factory.newSAXParser();
			ProcessHandler handler = new ProcessHandler();
			saxParser.parse(xmlInput, handler);

			for (PartnerLinkModel link : handler.getListPartnerLink()) {
				System.out.println(link);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
