package perso;

import com.tibco.exchange.tibreview.Main;

public class Test {
	private static String[] main = { "-r", "src/test/resources/tibrules.xml", "-c",
			"src/test/resources/config.properties", "-i", "project", "-s", "C:/pro/workspace/tibco_workspace2/Test",
			"-o", "pmd", "-t", "C:/tmp/output" };

	public static void main(String[] args) throws Exception {
		Main.main(main);
	}
}
