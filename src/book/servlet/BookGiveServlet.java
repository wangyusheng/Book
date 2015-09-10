package book.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import tools.CodeTools;
import tools.Tools;
import bean.GiveBookBean;
import bean.GiveBookOrderBefore;
import dao.BaseDao;
import dao.BookDao;
import dao.GiveBookDao;

public class BookGiveServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3565055262614295612L;
	//private static final String INSERT = "book_give_insert";

	//private static final String RETRIEVAL = "book_give_retrieval";

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
		
		if ("book_give_insert".equals(action)) {
			insert(requestJson, response);//完成
		} else if ("book_give_retrieval".equals(action)) {
			dealretrievalGiveBookInfo(requestJson, response);//完成
		} else if ("book_give_insertOrder".equals(action)) {
			// 添加订单
			dealInsertGiveBookOrder(requestJson, response);
		} else if ("book_give_retrievalOrder".equals(action)) {
			dealRetrivealGiveBookOrder(requestJson, response);

		} else if ("book_give_confirmOrder".equals(action)) {
			// 确认订单
			doConfirmgiveBookOrder(requestJson, response);

		} else if ("book_give_retrievalOrderFinish".equals(action)) {
			// 检索已完成的订单
			//
			
			System.out.println("book_give_retrievalOrderFinish"+requestJson.toString());
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
		JSONObject resultObject = new GiveBookDao()
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
	 *            jsonObject.put("action", "book_give_confirmOrder");
	 *            jsonObject.put("book_id", book_id); jsonObject.put("type",
	 *            type);
	 * @throws IOException
	 */
	private void doConfirmgiveBookOrder(JSONObject requestJson,
			HttpServletResponse response) throws IOException {
		System.out.println("doConfirmgiveBookOrder:"
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
		boolean sign = new GiveBookDao().confirmOrder(book_id, type);
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

	private void dealRetrivealGiveBookOrder(JSONObject requestJson,
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

		JSONObject resultObject = new GiveBookDao()
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
	private void dealInsertGiveBookOrder(JSONObject requestJson,
			HttpServletResponse response) throws IOException {
		System.out.println("dealInsertGiveBookOrder:" + requestJson);
		int releaseUser = 0, obtainUser = 0, releaseBook = 0;
		String str_releaseMsg = null, str_obtainMsg = null;
		try {
			releaseUser = Integer.parseInt(requestJson
					.getString("release_user"));
			obtainUser = Integer.parseInt(requestJson.getString("obtain_user"));

			releaseBook = Integer.parseInt(requestJson
					.getString("release_book"));
			//obtainBook = Integer.parseInt(requestJson.getString("obtain_book"));

			str_releaseMsg = requestJson.getString("release_msg");
			str_obtainMsg = requestJson.getString("obtain_msg");
		} catch (Exception e) {
			CodeTools.getInstance().onError(4, response);
			return;
		}
		if (releaseUser <= 0 || obtainUser <= 0 || releaseBook <= 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		if (!Tools.strIsOK(str_releaseMsg) || !Tools.strIsOK(str_obtainMsg)) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		// 插入 数据
		GiveBookBean giveBookBean = new GiveBookBean();
		giveBookBean.setRelease_user(releaseUser);
		giveBookBean.setObtain_user(obtainUser);
		giveBookBean.setRelease_book(releaseBook);
		
		giveBookBean.setRelease_msg(str_releaseMsg);
		giveBookBean.setObtain_msg(str_obtainMsg);

		String generation_Time = Tools.getTime();
		giveBookBean.setGenerate_time(generation_Time);

	//	ArrayList<Integer> allObtainBookId = new GiveBookDao()
	//			.getAllObtainBookId(giveBookBean.getRelease_book());

	//	for (int temp : allObtainBookId) {
	//		System.out.println(temp);
	//	}
		// 移除这一个
	//	allObtainBookId.remove(new Integer(giveBookBean.getObtain_book()));

		int[] result = new GiveBookDao().insertgiveBookOrder(
				giveBookBean);

		int len = result.length;
		int count = 0;
		for (int i = 0; i < len; i++) {
			if (result[i] == 1) {
				count++;
			}
		}
		if (count >= 3) {

			JSONObject bookObject = new BookDao()
					.retrievalBook(giveBookBean.getRelease_book());
			String bookName = "";
			try {
				bookName = bookObject.getString("book_name");
			} catch (Exception e) {
				System.out.println("有bookId 但是没有查询到记录");
				return;
			}

			JSONArray jsonArray = new JSONArray();
			
			ArrayList<Integer> allObtainUser = new GiveBookDao()
						.getAllObtainUser(releaseBook,releaseUser);
			for (int temp_obtainUser : allObtainUser) {
					jsonArray.add(new BaseDao().retrieval_ImAccount(temp_obtainUser));

			}	

			// 最后一个是获得者
			jsonArray.add(new BaseDao().retrieval_ImAccount(giveBookBean
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

	private void dealretrievalGiveBookInfo(JSONObject requestJson,
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

		JSONArray giveBookInfoArray = new GiveBookDao().retrieval(
				bookId, infoId, type);
		// 返回
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("GiveBookInfoArray", giveBookInfoArray);
		out.println(jsonObject.toString());
		out.flush();
		out.close();

	}

	private void insert(JSONObject requestJson, HttpServletResponse response)
			throws IOException {
		String str_obtainUser = requestJson.getString("own_userId");
		String obtainAddress = requestJson.getString("own_address");

		String str_releaseUser = requestJson.getString("release_userId");
		String str_releaseBook = requestJson.getString("release_bookId");
	
		String release_bookname = requestJson.getString("release_bookname");
	
		int obtainUser = -1, releaseUser = -1, releaseBook = -1;
		try {
			obtainUser = Integer.parseInt(str_obtainUser);
			releaseUser = Integer.parseInt(str_releaseUser);
			releaseBook = Integer.parseInt(str_releaseBook);

		} catch (Exception e) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		if (obtainUser <= 0 || releaseUser <= 0
				|| releaseBook <= 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		// 插入数据
		GiveBookOrderBefore giveBookInfo = new GiveBookOrderBefore();
		giveBookInfo.setObtain_user(obtainUser);		
		giveBookInfo.setObtain_msg(obtainAddress);
		giveBookInfo.setRelease_user(releaseUser);
		giveBookInfo.setRelease_book(releaseBook);
		giveBookInfo.setGenerate_time(Tools.getTime());
		giveBookInfo.setRelease_bookname(release_bookname);
		int[] result = new GiveBookDao()
				.insertBookgiveInfo(giveBookInfo);
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
					.retrieval_ImAccount(giveBookInfo.getRelease_user());
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
