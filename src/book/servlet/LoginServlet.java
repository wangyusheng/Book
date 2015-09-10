package book.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.UserBean;

import tools.CodeTools;
import tools.Tools;

import dao.UserDao;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class LoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		// 访问来源
		if (request.getHeader("user-agent") != null
				&& (request.getHeader("user-agent").toLowerCase().indexOf(
						"mozilla") != -1)) {
			System.out.println("访问端-PC");
		} else {
			System.out.println("访问端-手机");
		}

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
		//System.out.println("action:" + action);

		if (action == null || !"login".equals(action)) {
			// 返回错误的
			CodeTools.getInstance().onError(1, response);
			return;
		}

		String username = requestJson.getString("username");
		String password = requestJson.getString("password");
		if (username == null || password == null) {
			CodeTools.getInstance().onError(4, response);
			return;
		}
		//System.out.println("username:" + username);
		//System.out.println("password:" + password);

		// 查询数据库 看是否存在该用户
		UserBean userBean = new UserDao().validateUser(username, password);
		if (userBean == null) {
			CodeTools.getInstance().onError(5, response);
		} else {

			PrintWriter out = response.getWriter();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorcode", 0);
			jsonObject.put("userid", userBean.getUser_id());
			jsonObject.put("sex", userBean.getSex());
			out.println(jsonObject.toString());
			out.flush();
			out.close();
		}

		return;

	}

	// private void onSucces(int userid, HttpServletResponse response)
	// throws IOException {
	//		
	//
	// }

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
