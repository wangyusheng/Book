package book.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BaseDao;
import dao.BookDao;

import bean.AddressDealBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import tools.CodeTools;
import tools.Tools;

public class BaseServlet extends HttpServlet {

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
		if ("add_address".equals(action)) {
			// 添加地址
			addAddress(requestJson, response);
		} else if ("retrievalAddress-user".equals(action)) {
			System.out.println("retrievalAddress-user");
			// 检索信息
			retrievalAddress(requestJson, response);
		} else if ("retrieval_ImAccount".equals(action)) {
			retrieval_ImAccount(requestJson, response);
		}

	}

	private void retrieval_ImAccount(JSONObject requestJson,
			HttpServletResponse response) throws IOException {
		System.out.println("retrieval_ImAccount:" + requestJson);
		int userId = -1;
		try {
			userId = requestJson.getInt("userId");

		} catch (Exception e) {
			e.printStackTrace();
			CodeTools.getInstance().onError(4, response);
			return;
		}
		if (userId <= 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		String imAccount = new BaseDao().retrieval_ImAccount(userId);
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("imAccount", imAccount);
		out.println(jsonObject.toString());
		out.flush();
		out.close();

	}

	/**
	 * 检索信息
	 * 
	 * @param requestJson
	 * @param response
	 * @throws IOException
	 */
	private void retrievalAddress(JSONObject requestJson,
			HttpServletResponse response) throws IOException {
		String str_UserId = requestJson.getString("userId");
		int userId = -1;
		try {
			userId = Integer.parseInt(str_UserId);
		} catch (Exception e) {
			e.printStackTrace();
			CodeTools.getInstance().onError(3, response);
			return;
		}
		if (userId < 1) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		JSONArray addressArray = new BaseDao().retrievalAddress(userId);
		// 返回
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("addressArray", addressArray);
		out.println(jsonObject.toString());
		out.flush();
		out.close();

	}

	private void addAddress(JSONObject requestJson, HttpServletResponse response)
			throws IOException {
		int userId = -1;
		try {
			userId = requestJson.getInt("userId");
		} catch (Exception e) {
			CodeTools.getInstance().onError(2, response);
			return;
		}
		if (userId < 1) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		String address1 = requestJson.getString("address1");
		String address2 = requestJson.getString("address2");
		String contact = requestJson.getString("contact");
		String phone = requestJson.getString("phone");
		if (Tools.strIsOK(address1) && Tools.strIsOK(address2)
				&& Tools.strIsOK(contact) && Tools.strIsOK(phone)) {
			// 添加
			AddressDealBean address = new AddressDealBean();
			address.setUserId(userId);
			address.setAddress1(address1);
			address.setAddress2(address2);
			address.setContact(contact);
			address.setPhone(phone);

			int result = new BaseDao().addAddress(address);
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

		} else {
			CodeTools.getInstance().onError(3, response);
			return;
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
