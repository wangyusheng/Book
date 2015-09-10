package book.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ˫����֤����
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
		//������Դ
		if (request.getHeader("user-agent") != null
				&& (request.getHeader("user-agent").toLowerCase().indexOf(
						"mozilla") != -1)) {
			System.out.println("���ʶ�-PC");
		} else {
			System.out.println("���ʶ�-�ֻ�");
		}
		//��������
		String action = request.getParameter("action");
		System.out.println(action);
		if (action == null || action.equals("")) {
			PrintWriter out = response.getWriter();
			out.println("������Ч���޷�����");
			out.flush();
			out.close();
			return;
		}
		//ע��ҳ��
		if (action.equals("register")) {
			/**
			 * Ϊһ��<servlet-name>��
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
