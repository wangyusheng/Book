package book.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bean.UserBean;
import tools.CodeTools;
import tools.IMTools;
import tools.Tools;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import dao.BaseDao;
import dao.UserDao;

public class RegisterServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7819649467694483657L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("RegisterServlet");

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
		if (action == null || !"register".equals(action)) {
			// 返回错误的
			CodeTools.getInstance().onError(1, response);
			return;
		}

		try {
			String username = requestJson.getString("username");
			String password = requestJson.getString("password");
			String interests = requestJson.getString("interest");
			String age = requestJson.getString("age");
			String vocation = requestJson.getString("vocation");
			String email = requestJson.getString("email");
			String sex = requestJson.getString("sex");

			register(username, password, interests, age, vocation, email, sex,
					response);

		} catch (JSONException e1) {
			// Json 数据格式不合法
			CodeTools.getInstance().onError(4, response);
			return;
		}

		return;
	}

	/**
	 * 注册
	 * 
	 * @param username
	 * @param password
	 * @param interest
	 * @param age
	 * @param vocation
	 * @param sex
	 * @param email
	 * @throws IOException
	 */
	private void register(String username, String password, String interests,
			String age, String vocation, String email, String sex,
			HttpServletResponse response) throws IOException {
		// 第一步 查询是否存在相同的用户名
		boolean sign = new UserDao().findUserByUserName(username);
		if (sign) {
			// 存在
			CodeTools.getInstance().onError(6, response);
			return;
		}
		// 插入数据
		UserBean userBean = new UserBean();
		userBean.setUserName(username);
		userBean.setPassword(password);
		userBean.setPotraitpath("");
		userBean.setNickname("");
		userBean.setInterest(interests);
		userBean.setVocation(vocation);
		userBean.setAge(age);
		userBean.setSex(sex);
		userBean.setEmail(email);
		userBean.setGeneration_time(System.currentTimeMillis() + "");

		// 添加IM账号
		String im_Account = IMTools.createSubAccount(username);
		if (Tools.strIsOK(im_Account)) {
			userBean.setIm_account(im_Account);
		} else {
			userBean.setIm_account("");
		}
		int result = new UserDao().addUser(userBean);
		if (result == 1) {
			// 添加成功
			PrintWriter out = response.getWriter();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorcode", 0);
			out.println(jsonObject.toString());
			out.flush();
			out.close();
			return;
		}
		CodeTools.getInstance().onError(8, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
