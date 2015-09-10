package book.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import tools.CodeTools;
import tools.Tools;
import dao.BaseDao;

/**
 * ����͸���
 * 
 * @author Administrator
 * 
 */
public class ChannelIdServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8985297035868507015L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("ChannelIdServlet�����������");

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String jSONString = Tools.readJSONString(request);
		if (jSONString == null || "".equals(jSONString)) {
			CodeTools.getInstance().onError(1, response);
			return;
		}
		// ��ȡ��Ϣ
		JSONObject requestJson = null;
		try {
			requestJson = JSONObject.fromObject(jSONString);
		} catch (JSONException e1) {
			// Json ���ݸ�ʽ���Ϸ�
			CodeTools.getInstance().onError(1, response);
			return;
		}

		System.out.println("requestJson=" + requestJson);
		String action = requestJson.getString("action");
		if (action == null) {
			CodeTools.getInstance().onError(1, response);
			return;
		}
		if ("channelId_insertOrUpdate".equals(action)) {
			insertOrUpdate(requestJson, response);
		} else {
			CodeTools.getInstance().onError(1, response);
			return;
		}
	}

	private void insertOrUpdate(JSONObject requestJson,
			HttpServletResponse response) throws IOException {
		// ��� ����
		String str_userId = requestJson.getString("user_id");
		String channel_id = requestJson.getString("channel_id");
		int userId = -1;
		try {
			userId = Integer.parseInt(str_userId);
		} catch (Exception e) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		if (userId <= 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		// ����
		int result=new BaseDao().insertOrUpdateChannelId(userId,channel_id);
		
		if (result == 1) {
			PrintWriter out = response.getWriter();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorcode", 0);
			out.println(jsonObject.toString());
			out.flush();
			out.close();
		} else {
			CodeTools.getInstance().onError(8, response);
		}
		

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
