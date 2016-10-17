package xuyihao.JmxConnector.jmx.entity;

/**
 * 
 * @Author Xuyh created at 2016年10月17日 上午10:37:59
 */
public class JmxMBeanOperation {
	private String operName = "";
	private String operDescription = "";
	private String operReturnType = "";

	public JmxMBeanOperation() {
		super();
	}

	public JmxMBeanOperation(String operName, String operDescription, String operReturnType) {
		super();
		this.operName = operName;
		this.operDescription = operDescription;
		this.operReturnType = operReturnType;
	}

	@Override
	public String toString() {
		String json = "{\"operName\" : \"" + operName + "\", \"operDescription\" : \"" + operDescription
				+ "\", \"operReturnType\" : \"" + operReturnType + "\"}";
		return json;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getOperDescription() {
		return operDescription;
	}

	public void setOperDescription(String operDescription) {
		this.operDescription = operDescription;
	}

	public String getOperReturnType() {
		return operReturnType;
	}

	public void setOperReturnType(String operReturnType) {
		this.operReturnType = operReturnType;
	}
}
