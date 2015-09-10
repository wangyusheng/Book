package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.MySqlConnectPool;
import bean.PosterBean;


public class PosterDao {
	private static int SIZE = 10;

	/**
	 * 添加俱乐部信息
	 * 
	 * @param posterBean
	 * @return
	 */
	public int insert(PosterBean posterBean) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// 添加用户
			pstmt = conn
					.prepareStatement("insert into poster_info(user_id,topic,person,time,"
							+ "address,generate_time) values(?,?,?,?,?,?)");

			pstmt.setInt(1, posterBean.getUser_id());
			pstmt.setString(2, posterBean.getTopic());
			pstmt.setString(3, posterBean.getperson());
			pstmt.setString(4, posterBean.getTime());
			pstmt.setString(5, posterBean.getAddress());
			pstmt.setString(6, posterBean.getGenerate_time());
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

	public JSONArray retrievalposter(int posterId, int type) {
		System.out.println("poster_id=" + posterId + "," + "type=" + type);
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
						.prepareStatement("select * from poster_info where state=0 and type=0 order by poster_id desc limit ?");
				pstmt.setInt(1, SIZE);

			} else if (type == 1) {
				pstmt = conn
						.prepareStatement("select * from poster_info where poster_id <? and state=0 and and type=1order by poster_id desc limit ?");
				pstmt.setInt(1, posterId);
				pstmt.setInt(2, SIZE);
			} else if (type == 2) {
				pstmt = conn
						.prepareStatement("select * from poster_info where poster_id >? and state=0 and type=2 order by poster_id desc limit ?");
				pstmt.setInt(1, posterId);
				pstmt.setInt(2, SIZE);
			}
			rs = pstmt.executeQuery();
			// 判断结果
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("poster_id", rs.getInt(1));
				jsonObject.put("user_id", rs.getInt(2));
				jsonObject.put("topic", rs.getString(3));
				jsonObject.put("person", rs.getString(4));
				jsonObject.put("time", rs.getString(5));
				jsonObject.put("address", rs.getString(6));
				//jsonObject.put("enroll_num", rs.getInt(7));
				jsonObject.put("concern_num", rs.getInt(7));
				jsonObject.put("accusation_num", rs.getInt(8));
				jsonObject.put("generate_time", rs.getString(9));
				jsonObject.put("state", rs.getInt(10));
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
