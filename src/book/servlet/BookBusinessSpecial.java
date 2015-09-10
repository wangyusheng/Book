package book.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
import tools.CodeTools;
import tools.Tools;
import util.FileUtil;
import bean.BookBean;
import dao.BookDao;

public class BookBusinessSpecial extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1707528421116238917L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("有请求进来了");

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
		System.out.println("requestJson=" + requestJson);
		String action = requestJson.getString("action");
		if (action == null) {
			CodeTools.getInstance().onError(1, response);
			return;
		}

		if ("retrievalAllMyBook".equals(action)) {
			int userId = -1, bookId = -1, type = -1;
			try {
				userId = requestJson.getInt("userId");
				bookId = requestJson.getInt("bookId");
				type = requestJson.getInt("type");
			} catch (Exception e) {
				CodeTools.getInstance().onError(2, response);
				return;
			}
			if (userId <= 0 || bookId < 0 || type < 0) {
				CodeTools.getInstance().onError(3, response);
				return;
			}
			dealretrievalAllMyBook(userId, bookId, type, response);
		} else if ("retrievalMyBeforeTrade".equals(action)) {
			int userId = -1, bookId = -1, type = -1;
			try {
				userId = requestJson.getInt("userId");
				bookId = requestJson.getInt("bookId");
				type = requestJson.getInt("type");
			} catch (Exception e) {
				CodeTools.getInstance().onError(2, response);
				return;
			}
			if (userId <= 0 || bookId <0 || type < 0) {
				CodeTools.getInstance().onError(3, response);
				return;
			}

			dealretrievalBeforeTrade(userId, bookId, type, response);

		} else if ("retrievalMyTrading".equals(action)) {
			int userId = -1, bookId = -1, type = -1;
			try {
				userId = requestJson.getInt("userId");
				bookId = requestJson.getInt("bookId");
				type = requestJson.getInt("type");
			} catch (Exception e) {
				CodeTools.getInstance().onError(2, response);
				return;
			}
			if (userId <= 0 || bookId < 0 || type < 0) {
				CodeTools.getInstance().onError(3, response);
				return;
			}

			dealretrievalTradeBook(userId, bookId, type, 1, response);

		} else if ("retrievalMyTraded".equals(action)) {
			int userId = -1, bookId = -1, type = -1;
			try {
				userId = requestJson.getInt("userId");
				bookId = requestJson.getInt("bookId");
				type = requestJson.getInt("type");
			} catch (Exception e) {
				CodeTools.getInstance().onError(2, response);
				return;
			}
			if (userId <= 0 || bookId <0 || type < 0) {
				CodeTools.getInstance().onError(3, response);
				return;
			}

			dealretrievalTradeBook(userId, bookId, type, 2, response);

		} else {
			CodeTools.getInstance().onError(1, response);
			return;
		}

	}

	private void dealretrievalTradeBook(int userId, int bookId, int type,
			int state, HttpServletResponse response) throws IOException {

		JSONArray bookArray = new BookDao().retrievalBook(bookId, type, userId,
				state);
		// 返回
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("bookArray", bookArray);
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

	private void dealretrievalBeforeTrade(int userId, int bookId, int type,
			HttpServletResponse response) throws IOException {
		JSONArray bookArray = new BookDao().retrievalBeforeTrade(bookId, type,
				userId);
		// 返回
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("bookArray", bookArray);
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

	private void dealretrievalAllMyBook(int userId, int bookId, int type,
			HttpServletResponse response) throws IOException {
		JSONArray bookArray = new BookDao().retrievalAllMyBook(bookId, type,
				userId);
		// 返回
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("bookArray", bookArray);
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
