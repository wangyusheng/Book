package book.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 双重验证机制
 * 
 * @author Administrator
 * 
 */
public class DefaultServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1442578004156312197L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//访问来源
		if (request.getHeader("user-agent") != null
				&& (request.getHeader("user-agent").toLowerCase().indexOf(
						"mozilla") != -1)) {
			System.out.println("访问端-PC");
		} else {
			System.out.println("访问端-手机");
		}
		//解析命令
		String action = request.getParameter("action");
		System.out.println(action);
		if (action == null || action.equals("")) {
			PrintWriter out = response.getWriter();
			out.println("参数无效，无法启动");
			out.flush();
			out.close();
			return;
		}
		//注册页面
		if (action.equals("register")) {
			/**
			 * 为一个<servlet-name>）
			 */
			RequestDispatcher rd = getServletContext().getNamedDispatcher(
					"register");
			rd.forward(request, response);
			return;
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
