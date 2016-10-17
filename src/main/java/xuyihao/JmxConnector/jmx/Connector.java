package xuyihao.JmxConnector.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import xuyihao.JmxConnector.jmx.entity.JmxMBean;
import xuyihao.JmxConnector.jmx.entity.JmxMBeanAttribute;
import xuyihao.JmxConnector.jmx.entity.JmxMBeanOperation;

/**
 * 
 * @Author Xuyh created at 2016年10月17日 上午9:33:25
 */
public class Connector {
	private String ip = "";
	private String port = "";
	private String userName = "";
	private String userPassword = "";
	private JMXConnector connector = null;
	private MBeanServerConnection connection = null;
	private List<ObjectInstance> objectInstanceList = new ArrayList<>();

	public Connector(String ip, String port) {
		this.ip = ip;
		this.port = port;
	}

	public Connector(String ip, String port, String userName, String userPassword) {
		this.ip = ip;
		this.port = port;
		this.userName = userName;
		this.userPassword = userPassword;
	}

	/**
	 * 获取className对应的所有MBean实例的objectName
	 * 
	 * @param className
	 * @return
	 */
	public List<String> getObjectNamesByClassName(String className) {
		List<String> objectNameList = new ArrayList<>();
		if (connection == null) {
			connect();
		}
		try {
			Set<ObjectInstance> objectInstanceSet = connection.queryMBeans(null, null);
			for (ObjectInstance instance : objectInstanceSet) {
				if (instance.getClassName().equals(className)) {
					objectNameList.add(instance.getObjectName().toString());
				}
			}
		} catch (IOException e) {
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
		if (connection == null) {
			connect();
		}
		try {
			Set<ObjectInstance> objectInstanceSet = connection.queryMBeans(null, null);
			for (ObjectInstance instance : objectInstanceSet) {
				objectNameList.add(instance.getObjectName().toString());
			}
		} catch (IOException e) {
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
		if (connection == null) {
			connect();
		}
		try {
			Set<ObjectInstance> objectInstanceSet = connection.queryMBeans(null, null);
			for (ObjectInstance instance : objectInstanceSet) {
				classNameList.add(instance.getClassName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classNameList;
	}

	/**
	 * 获取domain（命名空间）
	 * 
	 * @return
	 */
	public List<String> getDomains() {
		List<String> domains = new ArrayList<String>();
		if (connection == null) {
			connect();
		}
		try {
			String[] temp = connection.getDomains();
			for (String domain : temp) {
				domains.add(domain);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return domains;
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
		if (connection == null) {
			connect();
		}
		try {
			ObjectName obName = new ObjectName(objectName);
			attrValue = connection.getAttribute(obName, attrName);
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (IOException e2) {
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
		if (connection == null) {
			connect();
		}
		try {
			ObjectName obName = new ObjectName(objectName);
			List<JmxMBeanAttribute> jmxMBeanAttributeList = new ArrayList<>();
			List<JmxMBeanOperation> jmxMBeanOperationList = new ArrayList<>();
			MBeanInfo mBeanInfo = connection.getMBeanInfo(obName);
			String className = mBeanInfo.getClassName();
			for (MBeanAttributeInfo attributeInfo : mBeanInfo.getAttributes()) {
				String attrName = attributeInfo.getName();
				Object attrValue = connection.getAttribute(obName, attrName);
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
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (ReflectionException e3) {
			e3.printStackTrace();
		} catch (IntrospectionException e4) {
			e4.printStackTrace();
		} catch (InstanceNotFoundException e5) {
			e5.printStackTrace();
		} catch (AttributeNotFoundException e6) {
			e6.printStackTrace();
		} catch (MBeanException e7) {
			e7.printStackTrace();
		}
		return bean;
	}

	/**
	 * 获取所有MBean的个数
	 * 
	 * @return
	 */
	public int getJmxMBeanCount() {
		if (connection == null) {
			connect();
		}
		try {
			// XXX 将无序的Set集合添加到有序的List集合中，并为下一次查找提供材料
			if (objectInstanceList.isEmpty() || objectInstanceList.size() == 0) {
				Set<ObjectInstance> objectInstanceSet = connection.queryMBeans(null, null);
				for (ObjectInstance instance : objectInstanceSet) {
					objectInstanceList.add(instance);
				}
			}
			return objectInstanceList.size();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 获取连接系统MBean信息（包括属性、属性值、操作信息等）
	 * 
	 * <pre>
	 * 	由于数量可能巨大，会造成异常情况，所以设置每次查找的限制
	 * </pre>
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 */
	/*
	 * public List<JmxMBean> getJmxMBeans(int offset, int limit) { List<JmxMBean>
	 * mBeanList = new ArrayList<>(); if (connection == null) { connect(); } try {
	 * // XXX 将无序的Set集合添加到有序的List集合中，并为下一次查找提供材料 if (objectInstanceList.isEmpty()
	 * || objectInstanceList.size() == 0) { Set<ObjectInstance> objectInstanceSet
	 * = connection.queryMBeans(null, null); for (ObjectInstance instance :
	 * objectInstanceSet) { objectInstanceList.add(instance); } } for (int i =
	 * offset; i < offset + limit; i++) { ObjectInstance instanceOfList =
	 * objectInstanceList.get(i); String className =
	 * instanceOfList.getClassName(); ObjectName objectName =
	 * instanceOfList.getObjectName(); List<JmxMBeanAttribute>
	 * jmxMBeanAttributeList = new ArrayList<>(); List<JmxMBeanOperation>
	 * jmxMBeanOperationList = new ArrayList<>(); MBeanInfo mBeanInfo =
	 * connection.getMBeanInfo(objectName); for (MBeanAttributeInfo attributeInfo
	 * : mBeanInfo.getAttributes()) { String attrName = attributeInfo.getName();
	 * Object attrValue = connection.getAttribute(objectName, attrName); String
	 * attrType = attributeInfo.getType(); String attrDescription =
	 * attributeInfo.getDescription(); boolean isReadable =
	 * attributeInfo.isReadable(); boolean isWritable =
	 * attributeInfo.isWritable(); JmxMBeanAttribute attribute = new
	 * JmxMBeanAttribute(attrName, attrValue, attrType, attrDescription,
	 * isReadable, isWritable); jmxMBeanAttributeList.add(attribute); } for
	 * (MBeanOperationInfo operationInfo : mBeanInfo.getOperations()) { String
	 * operName = operationInfo.getName(); String operDescription =
	 * operationInfo.getDescription(); String operReturnType =
	 * operationInfo.getReturnType(); JmxMBeanOperation operation = new
	 * JmxMBeanOperation(operName, operDescription, operReturnType);
	 * jmxMBeanOperationList.add(operation); } JmxMBean mBean = new
	 * JmxMBean(objectName.toString(), className, jmxMBeanAttributeList,
	 * jmxMBeanOperationList); mBeanList.add(mBean); } } catch (IOException e) {
	 * e.printStackTrace(); } catch (ReflectionException e2) {
	 * e2.printStackTrace(); } catch (IntrospectionException e3) {
	 * e3.printStackTrace(); } catch (InstanceNotFoundException e4) {
	 * e4.printStackTrace(); } catch (AttributeNotFoundException e5) {
	 * e5.printStackTrace(); } catch (MBeanException e6) { e6.printStackTrace(); }
	 * return mBeanList; }
	 */

	/**
	 * 连接
	 * 
	 * @return
	 */
	public boolean connect() {
		boolean flag = true;
		String rawUrl = "service:jmx:rmi:///jndi/rmi://" + ip + ":" + port + "/jmxrmi";
		if (ip.equals("") || port.equals("")) {
			return false;
		}
		try {
			JMXServiceURL url = new JMXServiceURL(rawUrl);
			if (userName.equals("")) {
				connector = JMXConnectorFactory.connect(url, null);
			} else {
				Map<String, Object> environment = new HashMap<String, Object>();
				environment.put(JMXConnector.CREDENTIALS, new String[] { userName, userPassword });
				connector = JMXConnectorFactory.connect(url, environment);
			}
			connection = connector.getMBeanServerConnection();
			flag = true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			flag = false;
		} catch (IOException e2) {
			e2.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/**
	 * 断开jmx连接
	 * 
	 * @return
	 */
	public boolean disConnect() {
		boolean flag = true;
		try {
			connector.close();
		} catch (IOException e) {
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
