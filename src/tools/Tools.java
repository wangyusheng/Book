package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class Tools {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss-ms");

	public static String getTime() {
		Date nowTime = new Date();
		return simpleDateFormat.format(nowTime);
	}

	public static String getFilePrefix(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	public static String readJSONString(HttpServletRequest request)
			throws IOException {
		StringBuffer json = new StringBuffer();
		String line = null;
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			json.append(line);
		}

		return json.toString();
	}

	public static boolean strIsOK(String str) {
		if (str == null) {
			return false;
		}
		if ("".equals(str)) {
			return false;
		}
		return true;
	}
}
