package tools;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class CodeTools {

	public static CodeTools instance = new CodeTools();

	private CodeTools() {

	}

	public static CodeTools getInstance() {
		return instance;
	}

	public void onError(int error_code, HttpServletResponse response)
			throws IOException {
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", error_code);
		out.println(jsonObject.toString());
		out.flush();
		out.close();

	}
	

}
