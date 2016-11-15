package xuyihao.JmxConnector.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 
 * @Author Xuyh created at 2016年10月25日 上午10:29:16
 */
public class CommonUtils {
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * 控制台输入一行
	 * 
	 * @return
	 */
	public static String inputLine() {
		String message = "";
		try {
			message = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 控制台输出一行
	 * 
	 * @param message
	 */
	public static void outputLine(String message) {
		System.out.println(message);
	}

	/**
	 * 获取文件输入流
	 * 
	 * @param filePathName
	 * @return
	 */
	public static BufferedWriter getFileOutputWriter(String filePathName) {
		File file = new File(filePathName);
		BufferedWriter writer = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer;
	}

	/**
	 * 利用BufferedWriter输出一行（控制台、文件）
	 * 
	 * @param writer
	 * @param message
	 * @return
	 */
	public static boolean writeLine(BufferedWriter writer, String message) {
		try {
			writer.write(message + "\r\n");
			return true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/**
	 * 写入文件并关闭输出流
	 * 
	 * @param writer
	 * @return
	 */
	public static boolean writeFile(BufferedWriter writer) {
		try {
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}
