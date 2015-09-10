package book.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.InfobuyDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import tools.CodeTools;
import tools.Tools;
import bean.InfobuyBean;


public class InfobuyBusiness extends HttpServlet{

	private static final long serialVersionUID = 6200519132733164514L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
		String action = requestJson.getString("action");
		if (action == null) {
			CodeTools.getInstance().onError(1, response);
			return;
		}
		if ("release-infobuy".equals(action)) {
			doReleaseinfobuy(requestJson, response);

		} else if ("retrievalinfobuy".equals(action)) {
			dealretrievalinfobuy(requestJson, response);
		} else {
			CodeTools.getInstance().onError(1, response);
			return;
		}
		System.out.println(requestJson);
	}

	/**
	 * ��������Ϣ
	 * 
	 * @param requestJson
	 * @param response
	 * @throws IOException
	 */
	private void dealretrievalinfobuy(JSONObject requestJson,
			HttpServletResponse response) throws IOException {
		int infobuyId = -1, type = -1;
		try {
			infobuyId = requestJson.getInt("info_buy_Id");
			type = requestJson.getInt("type");
		} catch (Exception e) {
			CodeTools.getInstance().onError(2, response);
			return;
		}
		if (infobuyId <0 || type < 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		JSONArray infobuyArray = new InfobuyDao().retrievalInfobuy(infobuyId, type);
		// ����
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("infobuyArray", infobuyArray);
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

	/**
	 * ���ֲ�����
	 * 
	 * @param requestJson
	 * @param response
	 * @throws IOException
	 */
	private void doReleaseinfobuy(JSONObject requestJson,
			HttpServletResponse response) throws IOException {

		int user_id = -1;
		String bookname = null;
		String price = null;
		String more_Info = null;
		String recommend_book = null;
		try {
			user_id = requestJson.getInt("user_id");
			bookname = requestJson.getString("bookname");
			price = requestJson.getString("price");
			more_Info = requestJson.getString("more_Info");
			recommend_book = requestJson.getString("recommend_book");
		} catch (Exception e) {
			CodeTools.getInstance().onError(2, response);
			return;
		}
		if (user_id <= 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}

		if (!Tools.strIsOK(bookname) || !Tools.strIsOK(price)
				|| !Tools.strIsOK(more_Info) || !Tools.strIsOK(recommend_book)) {
			CodeTools.getInstance().onError(3, response);
			return;
		}

		InfobuyBean infobuyBean = new InfobuyBean();
		infobuyBean.setUser_id(user_id);
		infobuyBean.setbookname(bookname);
		infobuyBean.setprice(price);
		infobuyBean.setAddress(more_Info);
		infobuyBean.setGenerate_time(System.currentTimeMillis() + "");
		int result = new InfobuyDao().insert(infobuyBean);
		if (result == 1) {
			// ����
			PrintWriter out = response.getWriter();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorcode", 0);
			out.println(jsonObject.toString());
			out.flush();
			out.close();
		} else {
			CodeTools.getInstance().onError(8, response);
			return;
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}
