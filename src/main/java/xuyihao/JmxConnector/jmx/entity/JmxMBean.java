package xuyihao.JmxConnector.jmx.entity;

import java.util.List;

/**
 * 
 * @Author Xuyh created at 2016年10月17日 上午10:10:40
 */
public class JmxMBean {
	private String objectName = "";
	private String className = "";
	private List<JmxMBeanAttribute> attributeList = null;
	private List<JmxMBeanOperation> operationList = null;

	public JmxMBean() {
		super();
	}

	public JmxMBean(String objectName, String className, List<JmxMBeanAttribute> attributeList,
			List<JmxMBeanOperation> operationList) {
		super();
		this.objectName = objectName;
		this.className = className;
		this.attributeList = attributeList;
		this.operationList = operationList;
	}

	@Override
	public String toString() {
		String info = "objectName : " + objectName + "\r\nclassName : " + className;
		info += "\r\nAttributes-------------------------------";
		for (JmxMBeanAttribute attribute : attributeList) {
			info += ("\r\n" + attribute.toString());
		}
		info += "\r\nOperations-------------------------------";
		for (JmxMBeanOperation operation : operationList) {
			info += ("\r\n" + operation.toString());
		}
		return info;
	}

	/**
	 * 通过operName获取MBean操作
	 * 
	 * @param operName
	 * @return
	 */
	public JmxMBeanOperation getOperation(String operName) {
		if (operationList != null && !operationList.isEmpty() && operationList.size() > 0) {
			for (JmxMBeanOperation operation : operationList) {
				if (operation.getOperName().equals(operName)) {
					return operation;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	/**
	 * 通过attrName获取MBean属性
	 * 
	 * @param attrName
	 * @return
	 */
	public JmxMBeanAttribute getAttribute(String attrName) {
		if (attributeList != null && !attributeList.isEmpty() && attributeList.size() > 0) {
			for (JmxMBeanAttribute attribute : attributeList) {
				if (attribute.getAttrName().equals(attrName)) {
					return attribute;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<JmxMBeanAttribute> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<JmxMBeanAttribute> attributeList) {
		this.attributeList = attributeList;
	}

	public List<JmxMBeanOperation> getOperationList() {
		return operationList;
	}

	public void setOperationList(List<JmxMBeanOperation> operationList) {
		this.operationList = operationList;
	}
}
