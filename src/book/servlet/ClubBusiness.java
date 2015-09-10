package book.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BookDao;
import dao.InfobuyDao;

import bean.InfobuyBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import tools.CodeTools;
import tools.Tools;

public class ClubBusiness extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6034799607136771454L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String jSONString = Tools.readJSONString(request);
		if (jSONString == null || "".equals(jSONString)) {
			CodeTools.getInstance().onError(1, response);
			return;
		}
		// 提取信息
		JSONObject requestJson = null;
		try {
			requestJson = JSONObject.fromObject(jSONString);
		} catch (JSONException e1) {
			// Json 数据格式不合法
			CodeTools.getInstance().onError(1, response);
			return;
		}
		String action = requestJson.getString("action");
		if (action == null) {
			CodeTools.getInstance().onError(1, response);
			return;
		}
		if ("release-Infobuy".equals(action)) {
			doReleaseInfobuy(requestJson, response);

		} else if ("retrievalInfobuy".equals(action)) {
			dealretrievalInfobuy(requestJson, response);
		} else {
			CodeTools.getInstance().onError(1, response);
			return;
		}
		System.out.println(requestJson);
	}

	/**
	 * 检索俱乐部
	 * 
	 * @param requestJson
	 * @param response
	 * @throws IOException
	 */
	private void dealretrievalInfobuy(JSONObject requestJson,
			HttpServletResponse response) throws IOException {
		int InfobuyId = -1, type = -1;
		try {
			InfobuyId = requestJson.getInt("InfobuyId");
			type = requestJson.getInt("type");
		} catch (Exception e) {
			CodeTools.getInstance().onError(2, response);
			return;
		}
		if (InfobuyId <0 || type < 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		JSONArray InfobuyArray = new InfobuyDao().retrievalInfobuy(InfobuyId, type);
		// 返回
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("InfobuyArray", InfobuyArray);
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

	/**
	 * 俱乐部发布
	 * 
	 * @param requestJson
	 * @param response
	 * @throws IOException
	 */
	private void doReleaseInfobuy(JSONObject requestJson,
			HttpServletResponse response) throws IOException {

		int user_id = -1;
		String bookname = null;
		String price = null;
		String more_Info = null;
		try {
			user_id = requestJson.getInt("user_id");
			bookname = requestJson.getString("bookname");
			price = requestJson.getString("price");
			more_Info = requestJson.getString("more_Info");
		} catch (Exception e) {
			CodeTools.getInstance().onError(2, response);
			return;
		}
		if (user_id <= 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}

		if (!Tools.strIsOK(bookname) || !Tools.strIsOK(price)
				|| !Tools.strIsOK(more_Info) ) {
			CodeTools.getInstance().onError(3, response);
			return;
		}

		InfobuyBean InfobuyBean = new InfobuyBean();
		InfobuyBean.setUser_id(user_id);
		InfobuyBean.setbookname(bookname);
		InfobuyBean.setprice(price);
		InfobuyBean.setAddress(more_Info);
		InfobuyBean.setGenerate_time(System.currentTimeMillis() + "");
		int result = new InfobuyDao().insert(InfobuyBean);
		if (result == 1) {
			// 返回
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
