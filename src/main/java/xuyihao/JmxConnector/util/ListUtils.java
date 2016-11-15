package xuyihao.JmxConnector.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表操作工具类
 * 
 * @Author Xuyh created at 2016年10月25日 下午3:53:01
 */
public class ListUtils {
	public static List<String> removeDuplicate(List<String> oldList) {
		List<String> newList = new ArrayList<String>();
		for (String value : oldList) {
			boolean has = false;
			for (String check : newList) {
				if (check.equals(value)) {
					has = true;
				}
			}
			if (!has) {
				newList.add(value);
			}
		}
		return newList;
	}
}
