package book.servlet;

/**
 * 覆盖式写入
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.BookDao;
import dao.UserDao;

import tools.CodeTools;
import tools.Tools;

public class FileUploadServlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40;
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		String action = request.getHeader("action");
		System.out.println("有请求过来了");
		System.out.println("action=" + action);
		String upload_DIRECTORY = null;
		if (action == null) {
			// 为NULL
			CodeTools.getInstance().onError(1, response);
			return;
		}
		// 表单是否符合文件上传规则
		// 判断客户端请求是否为POST，并且enctype属性是否是“multipart/form-data"
		if (!ServletFileUpload.isMultipartContent(request)) {
			PrintWriter writer = response.getWriter();
			writer.println("Error: Form must has enctype=multipart/form-data.");
			writer.flush();
			return;
		}

		/** 上传头像 **/
		if (action.equals("upload_portrait")) {
			// 用户头像
			upload_DIRECTORY = "upload_portrait";
			dealUpLoad_Portrait(request, response, upload_DIRECTORY);

		} else if (action.equals("upload_poster")) {
			// 海报
			upload_DIRECTORY = "upload_poster";
		} else if (action.equals("upload_book")) {
			// 书籍
			upload_DIRECTORY = "upload_book";
			dealUpLoad_Book(request, response, upload_DIRECTORY);
		}
		if (upload_DIRECTORY == null) {
			// 不合法的的值
			CodeTools.getInstance().onError(1, response);
			return;
		}

		// Enumeration en = request.getParameterNames();
		// while (en.hasMoreElements()) {
		// String paramName = (String) en.nextElement();
		// String paramValue = request.getParameter(paramName);
		// System.out.println(paramName + "=" + paramValue);
		// }

		// 文件工厂类 判定初始文件内存以及文件超过内存时临时存放的位置
		// DiskFileItemFactory factory = new DiskFileItemFactory();
		// // 以byte为单位设定文件使用多少内存量后，将文件存入临时存储
		// factory.setSizeThreshold(MEMORY_THRESHOLD);
		// // 设定临时文件的存储路径
		// factory.setRepository(new
		// File(System.getProperty("java.io.tmpdir")));
		//
		// // ServletFileUpload 处理同意HTML文件中多文件上传的类，继承自FileUpload
		// ServletFileUpload upload = new ServletFileUpload(factory);
		// // 设置允许上传文件的最大大小
		// upload.setSizeMax(MAX_REQUEST_SIZE);
		//
		// System.out.println("test=" + getServletContext().getRealPath(""));
		//
		// String uploadPath = getServletContext().getRealPath("")
		// + File.separator + upload_DIRECTORY;
		//
		// // 根据存储路径生成文件夹
		// File uploadDir = new File(uploadPath);
		// if (!uploadDir.exists()) {
		// uploadDir.mkdir();
		// }
		// try {
		// List<FileItem> formItems = upload.parseRequest(request);
		// System.out.println(formItems.size());
		// if (formItems != null && formItems.size() > 0) {
		// for (FileItem item : formItems) {
		// if (!item.isFormField()) {
		// String fileName = new File(item.getName()).getName();
		// String filePath = uploadPath + File.separator
		// + fileName;
		// System.out.println("filePath" + filePath);
		// File storeFile = new File(filePath);
		// // 写入文件
		// item.write(storeFile);
		// request.setAttribute("message",
		// "Upload has been done successfully!");
		//
		// }
		// }
		//
		// }
		// } catch (Exception ex) {
		// request.setAttribute("message", "There was an error: "
		// + ex.getMessage());
		// }

	}

	@SuppressWarnings("unchecked")
	private void dealUpLoad_Book(HttpServletRequest request,
			HttpServletResponse response, String uploadDIRECTORY)
			throws IOException {
		System.out.println("dealUpLoad_Book");
		String str_bookId = request.getHeader("book_id");
		if (str_bookId == null) {
			CodeTools.getInstance().onError(2, response);

			return;
		}
		int bookId = -1;
		try {
			bookId = Integer.parseInt(str_bookId);
		} catch (Exception e) {
			CodeTools.getInstance().onError(3, response);
			return;
		}
		if (bookId <= 0) {
			CodeTools.getInstance().onError(3, response);
			return;
		}

		// 文件工厂类 判定初始文件内存以及文件超过内存时临时存放的位置
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 以byte为单位设定文件使用多少内存量后，将文件存入临时存储
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		// 设定临时文件的存储路径
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		// ServletFileUpload 处理同意HTML文件中多文件上传的类，继承自FileUpload
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置允许上传文件的最大大小
		upload.setSizeMax(MAX_REQUEST_SIZE);

		System.out.println("test=" + getServletContext().getRealPath(""));

		String uploadPath = getServletContext().getRealPath("")
				+ File.separator + uploadDIRECTORY;

		// 根据存储路径生成文件夹
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		try {
			// 处理一个文件
			List<FileItem> formItems = upload.parseRequest(request);
			System.out.println("size=" + formItems.size());
			if (formItems != null && formItems.size() > 0) {
				FileItem item = formItems.get(0);
				String fileName = new File(item.getName()).getName();
				String prefix = Tools.getFilePrefix(fileName);
				fileName = str_bookId + prefix;
				String filePath = uploadPath + File.separator + fileName;

				File storeFile = new File(filePath);
				// // 写入文件
				item.write(storeFile);

				// // 更新用户表
				int result = new BookDao().updatePicturePath(bookId, fileName);
				if (result == 1) {
					PrintWriter out = response.getWriter();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("errorcode", 0);
					out.println(jsonObject.toString());
					out.flush();
					out.close();
					System.out.println("更新book图片地址成功！");
				} else {
					CodeTools.getInstance().onError(8, response);
				}

			} else {
				CodeTools.getInstance().onError(2, response);
			}
		} catch (Exception ex) {
			// request.setAttribute("message", "There was an error: "
			// + ex.getMessage());
			System.out.println(ex.toString());
			CodeTools.getInstance().onError(2, response);
		}
	}

	/**
	 * 处理上传头像业务
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void dealUpLoad_Portrait(HttpServletRequest request,
			HttpServletResponse response, String upload_DIRECTORY)
			throws IOException {
		System.out.println("dealUpLoad_Portrait");
		String str_userId = request.getHeader("userId");
		System.out.println("str_userId:" + str_userId);
		if (str_userId == null) {
			CodeTools.getInstance().onError(2, response);

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
		//

		// 文件工厂类 判定初始文件内存以及文件超过内存时临时存放的位置
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 以byte为单位设定文件使用多少内存量后，将文件存入临时存储
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		// 设定临时文件的存储路径
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		// ServletFileUpload 处理同意HTML文件中多文件上传的类，继承自FileUpload
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置允许上传文件的最大大小
		upload.setSizeMax(MAX_REQUEST_SIZE);

		System.out.println("test=" + getServletContext().getRealPath(""));

		String uploadPath = getServletContext().getRealPath("")
				+ File.separator + upload_DIRECTORY;

		// 根据存储路径生成文件夹
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		try {
			// 处理一个文件
			List<FileItem> formItems = upload.parseRequest(request);
			System.out.println("size=" + formItems.size());
			if (formItems != null && formItems.size() > 0) {
				FileItem item = formItems.get(0);
				String fileName = new File(item.getName()).getName();
				String filePath = uploadPath + File.separator + fileName;
				System.out.println("filePath" + filePath);

				File storeFile = new File(filePath);
				// // 写入文件
				item.write(storeFile);

				// 更新用户表
				int result = new UserDao().updatePortraitPath(userId, fileName);
				if (result == 1) {
					PrintWriter out = response.getWriter();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("errorcode", 0);
					out.println(jsonObject.toString());
					out.flush();
					out.close();
					System.out.println("更新用户头像目录成功！");
				} else {
					CodeTools.getInstance().onError(8, response);
				}

			} else {
				CodeTools.getInstance().onError(2, response);
			}
		} catch (Exception ex) {
			// request.setAttribute("message", "There was an error: "
			// + ex.getMessage());
			System.out.println(ex.toString());
			CodeTools.getInstance().onError(2, response);
		}
	}
}