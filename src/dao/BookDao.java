package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tools.Tools;
import util.MySqlConnectPool;
import bean.BookBean;

public class BookDao {

	private static int SIZE = 10;

	// bookBean.setUser_Id(userId);
	// bookBean.setBook_Name(bookName);
	//
	// bookBean.setAbstract_content(content);
	// bookBean.setTime_Release(times);
	// bookBean.setNew_Old(newOld);
	// bookBean.setRating(rating);
	// bookBean.setGeneration_time(Tools.getTime());
	//
	// bookBean.setInterest(interest);
	// bookBean.setAuthor_name(authorName);
	// bookBean.setTranscation(transcation);
	//

	/**
	 * 新增书籍
	 */
	public int addBook(BookBean bookBean) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// 添加用户
			pstmt = conn
					.prepareStatement("insert into book_info(user_id,book_name,abstract,time_release,"
							+ "transaction,new_old,rating,generation_time,interest,author_name) values(?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, bookBean.getUser_Id());
			pstmt.setString(2, bookBean.getBook_Name());
			pstmt.setString(3, bookBean.getAbstract_content());
			pstmt.setString(4, bookBean.getTime_Release());
			pstmt.setString(5, bookBean.getTranscation());
			pstmt.setString(6, bookBean.getNew_Old());
			pstmt.setInt(7, bookBean.getRating());
			pstmt.setString(8, bookBean.getGeneration_time());
			pstmt.setString(9, bookBean.getInterest());
			pstmt.setString(10, bookBean.getAuthor_name());

			result = pstmt.executeUpdate();
			// 提交更改
			conn.commit();
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			throw new RuntimeException(se);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (null != conn) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	/**
	 * 根据用户ID和发行时间获得图书ID
	 * 
	 * @param userId
	 * @param generationTime
	 * @return
	 */
	public int retrievalBookId(int userId, String generationTime) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// UserBean user = null;
		try {
			// 获得statement对象

			conn = MySqlConnectPool.getConnection();
			pstmt = conn
					.prepareStatement("select book_id from book_info where user_id=? and generation_time=?");
			pstmt.setInt(1, userId);
			pstmt.setString(2, generationTime);
			rs = pstmt.executeQuery();
			// 判断结果
			if (rs.next()) {
				return rs.getInt(1);
			}
			// 该用户存在
			return -1;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 默认返回不存在
		return -1;
	}

	public int updatePicturePath(int bookId, String fileName) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// 添加用户
			pstmt = conn
					.prepareStatement("update book_info set picture=? where book_id=?");
			pstmt.setString(1, fileName);
			pstmt.setInt(2, bookId);

			result = pstmt.executeUpdate();
			// 提交更改
			conn.commit();
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			throw new RuntimeException(se);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (null != conn) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	/**
	 * type =0 第一次查询 直接查询最新的 type =1 查询小于id的 加载更多 type =2查询大于id的 刷新
	 * 
	 * @param book_id
	 * @param type
	 * @return
	 */
	public JSONArray retrievalBook(int book_id, int type) {
		System.out.println("book_id=" + book_id + "," + "type=" + type);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONArray jsonArray = new JSONArray();
		// data.getJSONArray(key)

		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象
			if (type == 0) {
				// 第一次查询

				pstmt = conn
						.prepareStatement("select * from book_info where state=0 order by book_id desc limit ?");
				pstmt.setInt(1, SIZE);

			} else if (type == 1) {
				pstmt = conn
						.prepareStatement("select * from book_info where book_id <? and state=0 order by book_id desc limit ?");
				pstmt.setInt(1, book_id);
				pstmt.setInt(2, SIZE);
			} else if (type == 2) {
				pstmt = conn
						.prepareStatement("select * from book_info where book_id >? and state=0 order by book_id desc limit ?");
				pstmt.setInt(1, book_id);
				pstmt.setInt(2, SIZE);
			}
			rs = pstmt.executeQuery();
			// 判断结果
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("book_id", rs.getInt(1));
				jsonObject.put("user_id", rs.getInt(2));
				jsonObject.put("book_name", rs.getString(3));
				jsonObject.put("abstract", rs.getString(4));
				jsonObject.put("time_release", rs.getString(5));
				jsonObject.put("transaction", rs.getString(6));
				jsonObject.put("new_old", rs.getString(7));
				jsonObject.put("picture", rs.getString(8));
				jsonObject.put("priority", rs.getInt(9));
				jsonObject.put("rating", rs.getInt(10));
				jsonObject.put("generation_time", rs.getString(11));
				jsonObject.put("author_name", rs.getString(12));
				jsonObject.put("interest", rs.getString(13));
				jsonObject.put("state",rs.getInt(14));
				jsonArray.add(jsonObject);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;

	}

	public JSONArray retrievalBook(int book_id, int type, int userId) {
		System.out.println("book_id=" + book_id + "," + "type=" + type
				+ ",userId=" + userId);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONArray jsonArray = new JSONArray();
		// data.getJSONArray(key)

		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象
			if (type == 0) {
				// 第一次查询
				pstmt = conn
						.prepareStatement("select * from book_info where user_id=? and state=0 order by book_id desc limit ? ");
				pstmt.setInt(1, userId);
				pstmt.setInt(2, SIZE);

			} else if (type == 1) {
				pstmt = conn
						.prepareStatement("select * from book_info where book_id <? and user_id=? and state=0 order by book_id desc limit ?");
				pstmt.setInt(1, book_id);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, SIZE);
			} else if (type == 2) {
				pstmt = conn
						.prepareStatement("select * from book_info where book_id >? and user_id=? and state=0 order by book_id desc limit ?");
				pstmt.setInt(1, book_id);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, SIZE);
			}
			rs = pstmt.executeQuery();
			// 判断结果
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("book_id", rs.getInt(1));
				jsonObject.put("user_id", rs.getInt(2));
				jsonObject.put("book_name", rs.getString(3));
				jsonObject.put("abstract", rs.getString(4));
				jsonObject.put("time_release", rs.getString(5));

				jsonObject.put("transaction", rs.getString(6));
				jsonObject.put("new_old", rs.getString(7));
				jsonObject.put("picture", rs.getString(8));
				jsonObject.put("priority", rs.getInt(9));
				jsonObject.put("rating", rs.getInt(10));
				jsonObject.put("generation_time", rs.getString(11));
				jsonObject.put("author_name", rs.getString(12));
				jsonObject.put("interest", rs.getString(13));
				jsonObject.put("state",rs.getInt(14));
				jsonArray.add(jsonObject);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	public JSONArray retrievalBook(int book_id, int type, int userId,int state) {
		System.out.println("book_id=" + book_id + "," + "type=" + type
				+ ",userId=" + userId + ",state=" + state);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONArray jsonArray = new JSONArray();
		// data.getJSONArray(key)

		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象
			if (type == 0) {
				// 第一次查询

				pstmt = conn
						.prepareStatement("select * from book_info where user_id=? and state =? order by book_id desc limit ? ");
				pstmt.setInt(1, userId);
				pstmt.setInt(2, state);
				pstmt.setInt(3, SIZE);

			} else if (type == 1) {
				pstmt = conn
						.prepareStatement("select * from book_info where book_id <? and user_id=? and state =? order by book_id desc limit ?");
				pstmt.setInt(1, book_id);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, state);
				pstmt.setInt(4, SIZE);
			} else if (type == 2) {
				pstmt = conn
						.prepareStatement("select * from book_info where book_id >? and user_id=? and state =? order by book_id desc limit ?");
				pstmt.setInt(1, book_id);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, state);
				pstmt.setInt(4, SIZE);
			}
			rs = pstmt.executeQuery();
			// 判断结果
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("book_id", rs.getInt(1));
				jsonObject.put("user_id", rs.getInt(2));
				jsonObject.put("book_name", rs.getString(3));
				jsonObject.put("abstract", rs.getString(4));
				jsonObject.put("time_release", rs.getString(5));

				jsonObject.put("transaction", rs.getString(6));
				jsonObject.put("new_old", rs.getString(7));
				jsonObject.put("picture", rs.getString(8));
				jsonObject.put("priority", rs.getInt(9));
				jsonObject.put("rating", rs.getInt(10));
				jsonObject.put("generation_time", rs.getString(11));
				jsonObject.put("author_name", rs.getString(12));
				jsonObject.put("interest", rs.getString(13));
				jsonObject.put("state",rs.getInt(14));
				jsonArray.add(jsonObject);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	public JSONArray retrievalAllMyBook(int book_id, int type, int userId) {
		System.out.println("book_id=" + book_id + "," + "type=" + type
				+ ",userId=" + userId);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONArray jsonArray = new JSONArray();
		// data.getJSONArray(key)

		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象
			if (type == 0) {
				// 第一次查询

				pstmt = conn
						.prepareStatement("select * from book_info where user_id=? and type=0 order by book_id desc limit ? ");
				pstmt.setInt(1, userId);
				pstmt.setInt(2, SIZE);

			} else if (type == 1) {
				pstmt = conn
						.prepareStatement("select * from book_info where book_id <? and user_id=? and type =1 order by book_id desc limit ?");
				pstmt.setInt(1, book_id);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, SIZE);
			} else if (type == 2) {
				pstmt = conn
						.prepareStatement("select * from book_info where book_id >? and user_id=? amd type=2 order by book_id desc limit ?");
				pstmt.setInt(1, book_id);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, SIZE);
			}
			rs = pstmt.executeQuery();
			// 判断结果
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("book_id", rs.getInt(1));
				jsonObject.put("user_id", rs.getInt(2));
				jsonObject.put("book_name", rs.getString(3));
				jsonObject.put("abstract", rs.getString(4));
				jsonObject.put("time_release", rs.getString(5));
				jsonObject.put("transaction", rs.getString(6));
				jsonObject.put("new_old", rs.getString(7));
				jsonObject.put("picture", rs.getString(8));
				jsonObject.put("priority", rs.getInt(9));
				jsonObject.put("rating", rs.getInt(10));
				jsonObject.put("generation_time", rs.getString(11));
				jsonObject.put("author_name", rs.getString(12));
				jsonObject.put("interest", rs.getString(13));
				jsonObject.put("state",rs.getInt(14));
				jsonArray.add(jsonObject);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	/**
	 * 查询单个信息
	 * 
	 * @param book_id
	 * @return
	 */
	public JSONObject retrievalBook(int book_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONObject jsonObject = new JSONObject();
		// data.getJSONArray(key)

		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象

			pstmt = conn
					.prepareStatement("select * from book_info where book_id =? ");
			pstmt.setInt(1, book_id);

			rs = pstmt.executeQuery();
			// 判断结果
			if (rs.next()) {
				jsonObject.put("book_id", rs.getInt(1));
				jsonObject.put("user_id", rs.getInt(2));
				jsonObject.put("book_name", rs.getString(3));
				jsonObject.put("abstract", rs.getString(4));
				jsonObject.put("time_release", rs.getString(5));
				jsonObject.put("transaction", rs.getString(6));
				jsonObject.put("new_old", rs.getString(7));
				jsonObject.put("picture", rs.getString(8));
				jsonObject.put("priority", rs.getInt(9));
				jsonObject.put("rating", rs.getInt(10));
				jsonObject.put("generation_time", rs.getString(11));
				jsonObject.put("author_name", rs.getString(12));
				jsonObject.put("interest", rs.getString(13));
				jsonObject.put("state",rs.getInt(14));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return jsonObject;
	}

	public JSONArray retrievalBeforeTrade(int book_id, int type, int userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONArray jsonArray = new JSONArray();
		// data.getJSONArray(key)

		try {

			conn = MySqlConnectPool.getConnection();
			// 获得statement对象
			if (type == 0) {
				// 第一次查询

				pstmt = conn
						.prepareStatement("select * from book_info where user_id=? and state=0 and type=0 order by book_id desc limit ? ");
				pstmt.setInt(1, userId);
				pstmt.setInt(2, SIZE);

			} else if (type == 1) {
				pstmt = conn
						.prepareStatement("select * from book_info where book_id <? and user_id=? and state=0 and type=1 order by book_id desc limit ?");
				pstmt.setInt(1, book_id);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, SIZE);
			} else if (type == 2) {
				pstmt = conn
						.prepareStatement("select * from book_info where book_id >? and user_id=? and state=0 type=2 order by book_id desc limit ?");
				pstmt.setInt(1, book_id);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, SIZE);
			}
			rs = pstmt.executeQuery();
			// 判断结果
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("book_id", rs.getInt(1));
				jsonObject.put("user_id", rs.getInt(2));
				jsonObject.put("book_name", rs.getString(3));
				jsonObject.put("abstract", rs.getString(4));
				jsonObject.put("time_release", rs.getString(5));

				jsonObject.put("transaction", rs.getString(6));
				jsonObject.put("new_old", rs.getString(7));
				jsonObject.put("picture", rs.getString(8));
				jsonObject.put("priority", rs.getInt(9));
				jsonObject.put("rating", rs.getInt(10));
				jsonObject.put("generation_time", rs.getString(11));
				jsonObject.put("author_name", rs.getString(12));
				jsonObject.put("interest", rs.getString(13));
				jsonObject.put("state",rs.getInt(14));
				jsonArray.add(jsonObject);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}
}
