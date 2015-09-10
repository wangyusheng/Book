package book.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.BaseDao;
import dao.BookDao;
import dao.ExchangeBookDao;
import bean.ExchangeBookBean;
import bean.ExchangeBookOrderBefore;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import tools.CodeTools;
import tools.Tools;

public class BookExchangeServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3951166958476835549L;
	private static final String INSERT = "book_exchange_insert";

	private static final String RETRIEVAL = "book_exchange_retrieval";

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
		if ("book_exchange_insert".equals(action)) {
			insert(requestJson, response);
		} else if ("book_exchange_retrieval".equals(action)) {
			dealretrievalExchangBookInfo(requestJson, response);
		} else if ("book_exchange_insertOrder".equals(action)) {
			// 添加订单
			dealInsertExchangBookOrder(requestJson, response);
		} else if ("book_exchange_retrievalOrder".equals(action)) {
			dealRetrivealExhangeBookOrder(requestJson, response);

		} else if ("book_exchange_confirmOrder".equals(action)) {
			// 确认订单
			doConfirmExchangeBookOrder(requestJson, response);

		} else if ("book_exchange_retrievalOrderFinish".equals(action)) {
			// 检索已完成的订单
			//
			
			System.out.println("book_exchange_retrievalOrderFinish"+requestJson.toString());
			dealRetrievaleOrderFinish(requestJson, response);
			
		
		} else {
			CodeTools.getInstance().onError(1, response);
			return;
		}

	}

	/**
	 * 检索已完成的订单
	 * 
	 * @param requestJson
	 * @param response
	 * @throws IOException
	 */
	private void dealRetrievaleOrderFinish(JSONObject requestJson,
			HttpServletResponse response) throws IOException {
		int book_Id = -1;
		try {
			book_Id = requestJson.getInt("book_id");
		} catch (Exception e) {
			CodeTools.getInstance().onError(2, response);
			return;
		}
		if (book_Id <= 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		JSONObject resultObject = new ExchangeBookDao()
				.retrivealBookOrderFinish(book_Id);
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("resultObject", resultObject);
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

	/**
	 * 确认订单
	 * 
	 * @param requestJson
	 * @param response
	 * 
	 * 
	 *            jsonObject.put("action", "book_exchange_confirmOrder");
	 *            jsonObject.put("book_id", book_id); jsonObject.put("type",
	 *            type);
	 * @throws IOException
	 */
	private void doConfirmExchangeBookOrder(JSONObject requestJson,
			HttpServletResponse response) throws IOException {
		System.out.println("doConfirmExchangeBookOrder:"
				+ requestJson.toString());
		int book_id = -1;
		boolean type = false;
		try {
			book_id = requestJson.getInt("book_id");
			type = requestJson.getBoolean("type");
		} catch (Exception e) {
			CodeTools.getInstance().onError(2, response);
			return;
		}
		if (book_id <= 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		boolean sign = new ExchangeBookDao().confirmOrder(book_id, type);
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		if (sign) {
			jsonObject.put("errorcode", 0);
		} else {
			jsonObject.put("errorcode", 10);
		}
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

	/**
	 * 检索订单
	 * 
	 * @param requestJson
	 * @param response
	 * @throws IOException
	 */

	private void dealRetrivealExhangeBookOrder(JSONObject requestJson,
			HttpServletResponse response) throws IOException {
		int book_Id = -1;
		try {
			book_Id = requestJson.getInt("book_id");
		} catch (Exception e) {
			CodeTools.getInstance().onError(2, response);
			return;
		}
		if (book_Id <= 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}

		JSONObject resultObject = new ExchangeBookDao()
				.retrivealBookOrder(book_Id);
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("resultObject", resultObject);
		out.println(jsonObject.toString());
		out.flush();
		out.close();

	}

	/**
	 * 添加订单
	 * 
	 * @param requestJson
	 * @param response
	 * @throws IOException
	 */
	private void dealInsertExchangBookOrder(JSONObject requestJson,
			HttpServletResponse response) throws IOException {
		System.out.println("dealInsertExchangBookOrder:" + requestJson);
		int releaseUser = 0, obtainUser = 0, releaseBook = 0, obtainBook = 0;
		String str_releaseMsg = null, str_obtainMsg = null;
		try {
			releaseUser = Integer.parseInt(requestJson
					.getString("release_user"));
			obtainUser = Integer.parseInt(requestJson.getString("obtain_user"));

			releaseBook = Integer.parseInt(requestJson
					.getString("release_book"));
			obtainBook = Integer.parseInt(requestJson.getString("obtain_book"));

			str_releaseMsg = requestJson.getString("release_msg");
			str_obtainMsg = requestJson.getString("obtain_msg");
		} catch (Exception e) {
			CodeTools.getInstance().onError(4, response);
			return;
		}
		if (releaseUser <= 0 || obtainUser <= 0 || releaseBook <= 0
				|| obtainBook <= 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		if (!Tools.strIsOK(str_releaseMsg) || !Tools.strIsOK(str_obtainMsg)) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		// 插入 数据
		ExchangeBookBean exchangeBookBean = new ExchangeBookBean();
		exchangeBookBean.setRelease_user(releaseUser);
		exchangeBookBean.setObtain_user(obtainUser);
		exchangeBookBean.setRelease_book(releaseBook);
		exchangeBookBean.setObtain_book(obtainBook);
		exchangeBookBean.setRelease_msg(str_releaseMsg);
		exchangeBookBean.setObtain_msg(str_obtainMsg);

		String generation_Time = Tools.getTime();
		exchangeBookBean.setGenerate_time(generation_Time);

		ArrayList<Integer> allObtainBookId = new ExchangeBookDao()
				.getAllObtainBookId(exchangeBookBean.getRelease_book());

		for (int temp : allObtainBookId) {
			System.out.println(temp);
		}
		// 移除这一个
		allObtainBookId.remove(new Integer(exchangeBookBean.getObtain_book()));

		int[] result = new ExchangeBookDao().insertExchangeBookOrder(
				exchangeBookBean, allObtainBookId);

		int len = result.length;
		int count = 0;
		for (int i = 0; i < len; i++) {
			if (result[i] == 1) {
				count++;
			}
		}
		if (count >= 4) {

			JSONObject bookObject = new BookDao()
					.retrievalBook(exchangeBookBean.getRelease_book());
			String bookName = "";
			try {
				bookName = bookObject.getString("book_name");
			} catch (Exception e) {
				System.out.println("有bookId 但是没有查询到记录");
				return;
			}

			JSONArray jsonArray = new JSONArray();

			if (!allObtainBookId.isEmpty()) {
				ArrayList<Integer> allObtainUser = new ExchangeBookDao()
						.getAllObtainUser(allObtainBookId);
				for (int temp_obtainUser : allObtainUser) {
					jsonArray.add(new BaseDao()
							.retrieval_ImAccount(temp_obtainUser));

				}
			}

			// 最后一个是获得者
			jsonArray.add(new BaseDao().retrieval_ImAccount(exchangeBookBean
					.getObtain_user()));

			PrintWriter out = response.getWriter();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorcode", 0);
			jsonObject.put("jsonArray", jsonArray);
			jsonObject.put("bookName", bookName);
			out.println(jsonObject.toString());
			out.flush();
			out.close();

		} else {
			CodeTools.getInstance().onError(8, response);
		}

	}

	private void dealretrievalExchangBookInfo(JSONObject requestJson,
			HttpServletResponse response) throws IOException {

		int bookId = -1, infoId = -1, type = -1;
		try {
			bookId = requestJson.getInt("book_id");
			infoId = requestJson.getInt("info_id");
			type = requestJson.getInt("type");
		} catch (Exception e) {
			CodeTools.getInstance().onError(2, response);
			return;
		}
		if (bookId <= 0 || infoId < 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}

		JSONArray exchangBookInfoArray = new ExchangeBookDao().retrieval(
				bookId, infoId, type);
		// 返回
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("exchangBookInfoArray", exchangBookInfoArray);
		out.println(jsonObject.toString());
		out.flush();
		out.close();

	}

	private void insert(JSONObject requestJson, HttpServletResponse response)
			throws IOException {
		String str_obtainUser = requestJson.getString("own_userId");
		String str_obtainBook = requestJson.getString("own_bookId");
		String obtainAddress = requestJson.getString("own_address");

		String str_releaseUser = requestJson.getString("other_userId");
		String str_releaseBook = requestJson.getString("other_bookId");
		String obtain_bookname = requestJson.getString("own_bookname");
		String obtain_picture = requestJson.getString("own_picture");
		String release_bookname = requestJson.getString("release_bookname");

		System.out.println("obtain_bookname=" + obtain_bookname);
		int obtainUser = -1, obtainBook = -1, releaseUser = -1, releaseBook = -1;
		try {
			obtainUser = Integer.parseInt(str_obtainUser);
			obtainBook = Integer.parseInt(str_obtainBook);

			releaseUser = Integer.parseInt(str_releaseUser);
			releaseBook = Integer.parseInt(str_releaseBook);

		} catch (Exception e) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		if (obtainUser <= 0 || obtainBook <= 0 || releaseUser <= 0
				|| releaseBook <= 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		// 插入数据
		ExchangeBookOrderBefore exchangeBookInfo = new ExchangeBookOrderBefore();
		exchangeBookInfo.setObtain_user(obtainUser);
		exchangeBookInfo.setObtain_book(obtainBook);
		exchangeBookInfo.setObtain_msg(obtainAddress);
		exchangeBookInfo.setRelease_user(releaseUser);
		exchangeBookInfo.setRelease_book(releaseBook);
		exchangeBookInfo.setGenerate_time(Tools.getTime());
		exchangeBookInfo.setObtain_bookname(obtain_bookname);
		exchangeBookInfo.setObtain_picture(obtain_picture);
		exchangeBookInfo.setRelease_bookname(release_bookname);
		int[] result = new ExchangeBookDao()
				.insertBookExchangeInfo(exchangeBookInfo);
		int len = result.length;
		int count = 0;
		for (int i = 0; i < len; i++) {
			if (result[i] == 1) {
				count++;
			} else {
				break;
			}
		}
		if (count == len) {
			// 返回对方的
			String imAccount = new BaseDao()
					.retrieval_ImAccount(exchangeBookInfo.getRelease_user());
			PrintWriter out = response.getWriter();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorcode", 0);
			jsonObject.put("imAccount", imAccount);
			out.println(jsonObject.toString());
			out.flush();
			out.close();

		} else {
			CodeTools.getInstance().onError(8, response);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
