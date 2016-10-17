package xuyihao.JmxConnector.jmx.entity;

/**
 * 
 * @Author Xuyh created at 2016年10月17日 上午10:38:06
 */
public class JmxMBeanAttribute {
	private String attrName = "";
	private Object attrValue = "";
	private String attrType = "";
	private String attrDescription = "";
	private boolean isReadable = false;
	private boolean isWritable = false;

	public JmxMBeanAttribute() {
		super();
	}

	public JmxMBeanAttribute(String attrName, Object attrValue, String attrType, String attrDescription,
			boolean isReadable, boolean isWritable) {
		super();
		this.attrName = attrName;
		this.attrValue = attrValue;
		this.attrType = attrType;
		this.attrDescription = attrDescription;
		this.isReadable = isReadable;
		this.isWritable = isWritable;
	}

	@Override
	public String toString() {
		String json = "{\"attrName\" : \"" + attrName + "\", \"attrValue\" : \"" + attrValue + "\", \"attrType\" : \""
				+ attrType + "\", \"attrDescription\" : \"" + attrDescription + "\", \"isReadable\" : \"" + isReadable
				+ "\", \"isWritable\" : \"" + isWritable + "\"}";
		return json;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public Object getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(Object attrValue) {
		this.attrValue = attrValue;
	}

	public String getAttrType() {
		return attrType;
	}

	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}

	public String getAttrDescription() {
		return attrDescription;
	}

	public void setAttrDescription(String attrDescription) {
		this.attrDescription = attrDescription;
	}

	public boolean isReadable() {
		return isReadable;
	}

	public void setReadable(boolean isReadable) {
		this.isReadable = isReadable;
	}

	public boolean isWritable() {
		return isWritable;
	}

	public void setWritable(boolean isWritable) {
		this.isWritable = isWritable;
	}
}
