package book.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import dao.BookDao;
import dao.UserDao;

import bean.BookBean;

import sun.misc.BASE64Decoder;
import tools.CodeTools;
import tools.Tools;
import util.FileUtil;

/**
 * ����ͼ��
 * 
 * @author Administrator
 * 
 */
public class BookBusiness extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -534838199240540602L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("�����������");

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String jSONString = Tools.readJSONString(request);
		if (jSONString == null || "".equals(jSONString)) {
			CodeTools.getInstance().onError(1, response);
			return;
		}
		// ��ȡ��Ϣ
		JSONObject requestJson = null;
		try {
			requestJson = JSONObject.fromObject(jSONString);
		} catch (JSONException e1) {
			// Json ���ݸ�ʽ���Ϸ�
			CodeTools.getInstance().onError(1, response);
			return;
		}
		String action = requestJson.getString("action");
		if (action == null) {
			CodeTools.getInstance().onError(1, response);
			return;
		}
		if ("releaseBook".equals(action)) {
			System.out.println("releaseBook");
			dealReleaseBook(requestJson, response);
		} else if ("retrievalBook".equals(action)) {
			int bookId = -1, type = -1;
			try {
				bookId = requestJson.getInt("bookId");
				type = requestJson.getInt("type");
			} catch (Exception e) {
				CodeTools.getInstance().onError(2, response);
				return;
			}
			dealretrievalBook(bookId, type, response);
		} else if ("retrievalBook-user".equals(action)) {
			dealretrievalBook(requestJson.getString("userId"),
					requestJson.getString("bookId"),
					requestJson.getString("type"), response);
		} else if ("retrievalBookSingle".equals(action)) {
			dealretrievalBook(requestJson, response);
		} else {
			CodeTools.getInstance().onError(1, response);
			return;
		}

	}

	private void dealretrievalBook(JSONObject requestJson,
			HttpServletResponse response) throws IOException {

		int book_id = -1;
		try {
			book_id =requestJson.getInt("bookId");
		} catch (Exception e) {
			// ���������ݲ��Ϸ�
			CodeTools.getInstance().onError(4, response);
			return;
		}
		if (book_id <= 0) {
			// ���������ݲ��Ϸ�
			CodeTools.getInstance().onError(3, response);
			return;
		}
		// ��ѯ
		JSONObject jsonResult = new BookDao().retrievalBook(book_id);
		// ����
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("jsonObject", jsonResult);
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

	private void dealretrievalBook(String str_userId, String str_bookId,
			String str_type, HttpServletResponse response) throws IOException {
		System.out.println("str_bookId=" + str_bookId);
		System.out.println("str_type=" + str_type);
		if (str_bookId == null || str_type == null || str_userId == null) {
			CodeTools.getInstance().onError(2, response);
			return;
		}
		int bookId = -1, type = -1, userId = -1;
		try {
			bookId = Integer.parseInt(str_bookId);
			type = Integer.parseInt(str_type);
			userId = Integer.parseInt(str_userId);
		} catch (Exception e) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		if (bookId < 0 || type < 0 || userId < 1) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		JSONArray bookArray = new BookDao().retrievalBook(bookId, type, userId);
		// ����
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("bookArray", bookArray);
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

	private void dealretrievalBook(int bookId, int type,
			HttpServletResponse response) throws IOException {
		
		if (bookId < 0 || type < 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		JSONArray bookArray = new BookDao().retrievalBook(bookId, type);
		// ����
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		jsonObject.put("bookArray", bookArray);
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

	private void dealReleaseBook(JSONObject requestJson,
			HttpServletResponse response) throws IOException {

		String str_userId = requestJson.getString("userId");
		String str_rating = requestJson.getString("rating");
		int userId = -1, rating = -1;
		try {

			userId = Integer.parseInt(str_userId);
			rating = Integer.parseInt(str_rating);
		} catch (Exception e) {
			CodeTools.getInstance().onError(4, response);
			return;
		}
		if (userId <= 0 || rating < 0) {
			CodeTools.getInstance().onError(4, response);
			return;
		}

		String authorName = requestJson.getString("authorName");
		String bookName = requestJson.getString("bookName");
		String content = requestJson.getString("content");
		String newOld = requestJson.getString("newOld");
		String interest = requestJson.getString("interest");
		String transaction = requestJson.getString("transaction");
		String times = requestJson.getString("times");

		String generation_Time = Tools.getTime();

		// �������ݿ� ��ñ��
		BookBean bookBean = new BookBean();
		bookBean.setUser_Id(userId);
		bookBean.setBook_Name(bookName);
		bookBean.setAuthor_name(authorName);
		bookBean.setAbstract_content(content);
		bookBean.setRating(rating);
		bookBean.setNew_Old(newOld);
		bookBean.setInterest(interest);
		bookBean.setTranscation(transaction);
		bookBean.setTime_Release(times);
		bookBean.setGeneration_time(generation_Time);

		int result = new BookDao().addBook(bookBean);
		if (result == 0) {
			CodeTools.getInstance().onError(8, response);
			return;
		}
		// ���book_Id
		int book_id = new BookDao().retrievalBookId(userId, generation_Time);
		System.out.println("book_id=" + book_id);
		if (book_id == -1) {
			CodeTools.getInstance().onError(8, response);
			return;
		}
		// ����book_id��Ū����
		String fileName = requestJson.getString("fileName");
		String fileContent = requestJson.getString("fileContent");
		if (Tools.strIsOK(fileName) && Tools.strIsOK(fileContent)) {
			// �洢�ļ�

			BASE64Decoder decoder = new BASE64Decoder();
			byte[] imgBytes;

			try {
				imgBytes = decoder.decodeBuffer(fileContent);
				String prefix = Tools.getFilePrefix(fileName);
				fileName = book_id + prefix;
				String getFileSavePath = getFileSavePath();
				File tempFile = new File(getFileSavePath);
				if (!tempFile.exists()) {
					tempFile.mkdirs();
				}

				String fileSavePath = getFileSavePath + File.separator
						+ fileName;

				System.out.println("fileSavePath=" + fileSavePath);
				FileUtil.getFileFromBytes(imgBytes, fileSavePath);
				// �޸����ݿ�
				// // �����û���
				result = new BookDao().updatePicturePath(book_id, fileName);
				if (result == 1) {
					PrintWriter out = response.getWriter();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("errorcode", 0);
					out.println(jsonObject.toString());
					out.flush();
					out.close();
					System.out.println("����bookͼƬ��ַ�ɹ���");
				} else {
					CodeTools.getInstance().onError(8, response);
				}

			} catch (IOException e) {
				e.printStackTrace();
				CodeTools.getInstance().onError(9, response);
				return;

			}
		}

		// ����
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorcode", 0);
		out.println(jsonObject.toString());
		out.flush();
		out.close();

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	private String getFileSavePath() {
		return getServletContext().getRealPath("") + File.separator
				+ "upload_book";
	}
}
