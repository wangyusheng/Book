package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.MySqlConnectPool;
import bean.ClubBean;

public class ClubDao {

	private static int SIZE = 10;

	/**
	 * 添加俱乐部信息
	 * 
	 * @param clubBean
	 * @return
	 */
	public int insert(ClubBean clubBean) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// 添加用户
			pstmt = conn
					.prepareStatement("insert into club_info(user_id,topic,recommend_book,time,"
							+ "address,generate_time) values(?,?,?,?,?,?)");

			pstmt.setInt(1, clubBean.getUser_id());
			pstmt.setString(2, clubBean.getTopic());
			pstmt.setString(3, clubBean.getRecommend_book());
			pstmt.setString(4, clubBean.getTime());
			pstmt.setString(5, clubBean.getAddress());
			pstmt.setString(6, clubBean.getGenerate_time());
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

	public JSONArray retrievalClub(int clubId, int type) {
		System.out.println("book_id=" + clubId + "," + "type=" + type);
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
						.prepareStatement("select * from club_info where state=0 order by club_id desc limit ?");
				pstmt.setInt(1, SIZE);

			} else if (type == 1) {
				pstmt = conn
						.prepareStatement("select * from club_info where club_id <? and state=0 order by club_id desc limit ?");
				pstmt.setInt(1, clubId);
				pstmt.setInt(2, SIZE);
			} else if (type == 2) {
				pstmt = conn
						.prepareStatement("select * from club_info where club_id >? and state=0 order by club_id desc limit ?");
				pstmt.setInt(1, clubId);
				pstmt.setInt(2, SIZE);
			}
			rs = pstmt.executeQuery();
			// 判断结果
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("club_id", rs.getInt(1));
				jsonObject.put("user_id", rs.getInt(2));
				jsonObject.put("topic", rs.getString(3));
				jsonObject.put("recommend_book", rs.getString(4));
				jsonObject.put("time", rs.getString(5));
				jsonObject.put("address", rs.getString(6));
				jsonObject.put("enroll_num", rs.getInt(7));
				jsonObject.put("concern_num", rs.getInt(8));
				jsonObject.put("accusation_num", rs.getInt(9));
				jsonObject.put("generate_time", rs.getString(10));
				jsonObject.put("state", rs.getInt(11));
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
