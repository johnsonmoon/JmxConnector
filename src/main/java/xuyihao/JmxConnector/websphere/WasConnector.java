package xuyihao.JmxConnector.websphere;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import com.ibm.websphere.management.*;
import com.ibm.websphere.management.exception.ConnectorException;

import xuyihao.JmxConnector.jmx.entity.JmxMBean;
import xuyihao.JmxConnector.jmx.entity.JmxMBeanAttribute;
import xuyihao.JmxConnector.jmx.entity.JmxMBeanOperation;
import xuyihao.JmxConnector.util.CommonUtils;

/**
 * 
 * @Author Xuyh created at 2016年10月24日 下午3:11:12
 */
public class WasConnector {
	private AdminClient adminClient = null;
	private String ip = "";
	private String port = "";
	private String userName = "";
	private String userPassword = "";
	private String trustStore = "";
	private String keyStore = "";
	private String trustStorePassword = "";
	private String keyStorePassword = "";
	private List<ObjectInstance> objectInstanceList = new ArrayList<>();

	/**
	 * ip和port不能为空
	 * 
	 * @param ip
	 * @param port
	 * @param userName
	 * @param userPassword
	 * @param trustStore
	 * @param keyStore
	 * @param trustStorePassword
	 * @param keyStorePassword
	 * @param objectInstanceList
	 */
	public WasConnector(String ip, String port, String userName, String userPassword, String trustStore, String keyStore,
			String trustStorePassword, String keyStorePassword) {
		super();
		this.ip = ip;
		this.port = port;
		this.userName = userName;
		this.userPassword = userPassword;
		this.trustStore = trustStore;
		this.keyStore = keyStore;
		this.trustStorePassword = trustStorePassword;
		this.keyStorePassword = keyStorePassword;
	}

	/**
	 * 
	 * @param ip
	 * @param port
	 */
	public WasConnector(String ip, String port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	/**
	 * 获取所有MBean的个数
	 * 
	 * @return
	 */
	public int getMBeanCount() {
		if (adminClient == null) {
			connect();
		}
		try {
			// XXX 将无序的Set集合添加到有序的List集合中，并为下一次查找提供材料
			if (objectInstanceList.isEmpty() || objectInstanceList.size() == 0) {
				@SuppressWarnings("unchecked")
				Set<ObjectInstance> objectInstanceSet = adminClient.queryMBeans(null, null);
				for (ObjectInstance instance : objectInstanceSet) {
					objectInstanceList.add(instance);
				}
			}
			return objectInstanceList.size();
		} catch (ConnectorException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 获取className对应的所有MBean实例的objectName
	 * 
	 * @param className
	 * @return
	 */
	public List<String> getObjectNamesByClassName(String className) {
		List<String> objectNameList = new ArrayList<>();
		if (adminClient == null) {
			connect();
		}
		try {
			@SuppressWarnings("unchecked")
			Set<ObjectInstance> objectInstanceSet = adminClient.queryMBeans(null, null);
			for (ObjectInstance instance : objectInstanceSet) {
				if (instance.getClassName().equals(className)) {
					objectNameList.add(instance.getObjectName().toString());
				}
			}
		} catch (ConnectorException e) {
			e.printStackTrace();
		}
		return objectNameList;
	}

	/**
	 * 获取监控系统的所有MBean的objectName
	 * 
	 * @return
	 */
	public List<String> getObjectNames() {
		List<String> objectNameList = new ArrayList<>();
		if (adminClient == null) {
			connect();
		}
		try {
			@SuppressWarnings("unchecked")
			Set<ObjectInstance> objectInstanceSet = adminClient.queryMBeans(null, null);
			for (ObjectInstance instance : objectInstanceSet) {
				objectNameList.add(instance.getObjectName().toString());
			}
		} catch (ConnectorException e) {
			e.printStackTrace();
		}
		return objectNameList;
	}

	/**
	 * 获取监控系统的所有MBean的className
	 * 
	 * @return
	 */
	public List<String> getClassNames() {
		List<String> classNameList = new ArrayList<>();
		if (adminClient == null) {
			connect();
		}
		try {
			@SuppressWarnings("unchecked")
			Set<ObjectInstance> objectInstanceSet = adminClient.queryMBeans(null, null);
			for (ObjectInstance instance : objectInstanceSet) {
				classNameList.add(instance.getClassName());
			}
		} catch (ConnectorException e) {
			e.printStackTrace();
		}
		return classNameList;
	}

	/**
	 * 获取domain（命名空间）
	 * 
	 * @return
	 */
	public String getDomainName() {
		String temp = "";
		if (adminClient == null) {
			connect();
		}
		try {
			temp = adminClient.getDomainName();
		} catch (ConnectorException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * 获取指定MBean的指定属性值
	 * 
	 * @param objectName
	 * @param attrName
	 * @return
	 */
	public Object getMBeanAttributeValue(String objectName, String attrName) {
		Object attrValue = null;
		if (adminClient == null) {
			connect();
		}
		try {
			ObjectName obName = new ObjectName(objectName);
			attrValue = adminClient.getAttribute(obName, attrName);
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (ConnectorException e2) {
			e2.printStackTrace();
		} catch (ReflectionException e3) {
			e3.printStackTrace();
		} catch (InstanceNotFoundException e5) {
			e5.printStackTrace();
		} catch (AttributeNotFoundException e6) {
			e6.printStackTrace();
		} catch (MBeanException e7) {
			e7.printStackTrace();
		}
		return attrValue;
	}

	/**
	 * 由objectName获取MBean的详细信息
	 * 
	 * @param objectName
	 * @return
	 */
	public JmxMBean getMBeanInfo(String objectName) {
		JmxMBean bean = null;
		if (adminClient == null) {
			connect();
		}
		try {
			ObjectName obName = new ObjectName(objectName);
			List<JmxMBeanAttribute> jmxMBeanAttributeList = new ArrayList<>();
			List<JmxMBeanOperation> jmxMBeanOperationList = new ArrayList<>();
			MBeanInfo mBeanInfo = adminClient.getMBeanInfo(obName);
			String className = mBeanInfo.getClassName();
			for (MBeanAttributeInfo attributeInfo : mBeanInfo.getAttributes()) {
				String attrName = attributeInfo.getName();
				Object attrValue = "";
				try {
					attrValue = adminClient.getAttribute(obName, attrName);
				} catch (Exception e) {
					CommonUtils.outputLine(e.getMessage());
					attrValue = "";
				}
				String attrType = attributeInfo.getType();
				String attrDescription = attributeInfo.getDescription();
				boolean isReadable = attributeInfo.isReadable();
				boolean isWritable = attributeInfo.isWritable();
				JmxMBeanAttribute attribute = new JmxMBeanAttribute(attrName, attrValue, attrType, attrDescription, isReadable,
						isWritable);
				jmxMBeanAttributeList.add(attribute);
			}
			for (MBeanOperationInfo operationInfo : mBeanInfo.getOperations()) {
				String operName = operationInfo.getName();
				String operDescription = operationInfo.getDescription();
				String operReturnType = operationInfo.getReturnType();
				JmxMBeanOperation operation = new JmxMBeanOperation(operName, operDescription, operReturnType);
				jmxMBeanOperationList.add(operation);
			}
			bean = new JmxMBean(objectName.toString(), className, jmxMBeanAttributeList, jmxMBeanOperationList);
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (ConnectorException e2) {
			e2.printStackTrace();
		} catch (ReflectionException e3) {
			e3.printStackTrace();
		} catch (IntrospectionException e4) {
			e4.printStackTrace();
		} catch (InstanceNotFoundException e5) {
			e5.printStackTrace();
		}
		return bean;
	}

	/**
	 * 进行soap连接
	 * 
	 * @return
	 */
	public boolean connect() {
		boolean flag = true;
		if (ip.equals("") || port.equals("")) {
			return false;
		}
		try {
			Properties connectionProps = new Properties();
			connectionProps.setProperty(AdminClient.CONNECTOR_TYPE, AdminClient.CONNECTOR_TYPE_SOAP);
			connectionProps.setProperty(AdminClient.CONNECTOR_HOST, ip);
			connectionProps.setProperty(AdminClient.CONNECTOR_PORT, port);
			if (trustStore != null && !trustStore.trim().equals("") && keyStore != null && !keyStore.trim().equals("")) {
				connectionProps.setProperty(AdminClient.CONNECTOR_SECURITY_ENABLED, "true");
			} else {
				connectionProps.setProperty(AdminClient.CONNECTOR_SECURITY_ENABLED, "false");
			}
			if (userName != null && !userName.trim().equals("")) {
				connectionProps.setProperty(AdminClient.USERNAME, userName);
			}
			if (userPassword != null && !userPassword.trim().equals("")) {
				connectionProps.setProperty(AdminClient.PASSWORD, userPassword);
			}
			if (trustStore != null && !trustStore.trim().equals("")) {
				connectionProps.setProperty("javax.net.ssl.trustStore", trustStore);
			}
			if (keyStore != null && !keyStore.trim().equals("")) {
				connectionProps.setProperty("javax.net.ssl.keyStore", keyStore);
			}
			if (trustStorePassword != null && !trustStorePassword.trim().equals("")) {
				connectionProps.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
			}
			if (keyStorePassword != null && !keyStorePassword.trim().equals("")) {
				connectionProps.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
			}
			adminClient = AdminClientFactory.createAdminClient(connectionProps);
			CommonUtils.outputLine("websphere 连接成功!");
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
}
