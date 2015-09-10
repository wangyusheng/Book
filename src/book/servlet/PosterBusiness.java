package book.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.PosterDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import tools.CodeTools;
import tools.Tools;
import bean.PosterBean;


public class PosterBusiness extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6740180245598059542L;
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
		if ("release-poster".equals(action)) {
			doReleaseposter(requestJson, response);

		} else if ("retrievalposter".equals(action)) {
			dealretrievalposter(requestJson, response);
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
	private void dealretrievalposter(JSONObject requestJson,
			HttpServletResponse response) throws IOException {
		int posterId = -1, type = -1;
		try {
			posterId = requestJson.getInt("posterId");
			type = requestJson.getInt("type");
		} catch (Exception e) {
			CodeTools.getInstance().onError(2, response);
			return;
		}
		if (posterId <0 || type < 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		JSONArray posterArray = new PosterDao().retrievalposter(posterId, type);
		// 返回
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("posterArray", posterArray);
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
	private void doReleaseposter(JSONObject requestJson,
			HttpServletResponse response) throws IOException {

		int user_id = -1;
		String subject = null;
		String time = null;
		String more_Info = null;
		String recommend_book = null;
		try {
			user_id = requestJson.getInt("user_id");
			subject = requestJson.getString("subject");
			time = requestJson.getString("time");
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

		if (!Tools.strIsOK(subject) || !Tools.strIsOK(time)
				|| !Tools.strIsOK(more_Info) || !Tools.strIsOK(recommend_book)) {
			CodeTools.getInstance().onError(3, response);
			return;
		}

		PosterBean posterBean = new PosterBean();
		posterBean.setUser_id(user_id);
		posterBean.setTopic(subject);
		posterBean.setTime(time);
		posterBean.setAddress(more_Info);
		posterBean.setGenerate_time(System.currentTimeMillis() + "");
		int result = new PosterDao().insert(posterBean);
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
