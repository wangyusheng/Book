package book.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import dao.UserDao;

import tools.CodeTools;

public class SettingServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		// 解析命令
		String action = request.getParameter("action");
		if (action == null) {
			// 返回错误的
			CodeTools.getInstance().onError(1, response);
			return;
		}
		if (action.equals("setting-getPortrait")) {
			// 处理
			getPortrait(request, response);
		}

	}

	/**
	 * 处理获得头像地址
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void getPortrait(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String str_userId = request.getParameter("userId");
		if (str_userId == null) {
			CodeTools.getInstance().onError(2, response);
			return;
		}
		if ("".equals(str_userId)) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
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
		// 查询
		String portraitPath = new UserDao().getPortraitpath(userId);
		if (portraitPath == null || "".equals(portraitPath)) {
			// 没有头像
			portraitPath = "";
		} else {
			portraitPath = "upload_portrait/" + portraitPath;
		}

		// 返回东西
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("portraitPath", portraitPath);
		out.println(jsonObject.toString());
		out.flush();
		out.close();

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
