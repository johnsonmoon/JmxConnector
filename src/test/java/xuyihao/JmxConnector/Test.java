package xuyihao.JmxConnector;

import java.util.List;

import xuyihao.JmxConnector.jmx.Connector;
import xuyihao.JmxConnector.util.CommonUtils;
import xuyihao.JmxConnector.websphere.WasConnector;

public class Test {

	public static void main(String... args) { 
		//test1();
		test2();
	}
	
	public static void test2(){
		//Connector connector = new Connector(ip, port, userName, userPassword);
	}

	public static void test1() {
		// WasConnector connector = new WasConnector("127.0.0.1", "8880", "wasadmin", "admin", "C:\\sslTest\\trust.jks","C:\\sslTest\\store.jks", "admin", "admin");
		WasConnector connector = new WasConnector("127.0.0.1", "8880");
		connector.connect();

		List<String> objectNameList = connector.getObjectNames();
		for (String objectName : objectNameList) {
			CommonUtils.outputLine(objectName);
		}

	}
}
