package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import bean.UserBean;
import util.MySqlConnectPool;

public class UserDao {
	/**
	 * ����ָ�����û������ֶμ�ֵ��ѯ���û��Ƿ����
	 * 
	 * @param field
	 *            Ҫ��ѯ���ֶ�
	 * @param value
	 *            ��Ӧ���ֶ�ֵ
	 * @return ���ز�ѯ���
	 */
	public boolean findUserByUserName(String value) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {

			conn = MySqlConnectPool.getConnection();
			// ���statement����
			pstmt = conn
					.prepareStatement("select * from user_info where user_name"
							+ "=?");
			pstmt.setString(1, value);
			rs = pstmt.executeQuery();
			// �жϽ��
			if (!rs.next()) {
				return false;// ���û�������
			}
			// ���û�����
			return true;

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
		return true;
	}

	/**
	 * ����û�
	 * 
	 * @param customer
	 *            Ҫ��ӵ��û�����
	 * @return ������ӽ����1�ɹ���0ʧ��
	 */
	public int addUser(UserBean user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// ����û�
			pstmt = conn
					.prepareStatement("insert into user_info(user_name,user_password,user_email,nickname,age,sex,vocation,interest,portraitpath,generation_time,im_account) values(?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getNickname());
			pstmt.setString(5, user.getAge());
			pstmt.setString(6, user.getSex());
			pstmt.setString(7, user.getVocation());
			pstmt.setString(8, user.getInterest());
			pstmt.setString(9, user.getPotraitpath());
			pstmt.setString(10, user.getGeneration_time());
			pstmt.setString(11, user.getIm_account());

			result = pstmt.executeUpdate();
			// �ύ����
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
	 * ��֤�û�
	 * 
	 * @return
	 */
	public UserBean validateUser(String userName, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// UserBean user = null;
		try {
			// ���statement����

			conn = MySqlConnectPool.getConnection();
			pstmt = conn
					.prepareStatement("select user_id,sex from user_info where user_name=? and user_password=?");
			pstmt.setString(1, userName);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			// �жϽ��
			if (rs.next()) {
				UserBean userBean = new UserBean();
				userBean.setUser_id(rs.getInt(1));
				userBean.setSex(rs.getString(2));
				return userBean;
			}
			// ���û�����
			return null;

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
		// Ĭ�Ϸ��ز�����
		return null;
	}

	/**
	 * ��֤�û�
	 * 
	 * @return
	 */
	public String getPortraitpath(int userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// UserBean user = null;
		try {
			// ���statement����

			conn = MySqlConnectPool.getConnection();
			pstmt = conn
					.prepareStatement("select portraitpath from user_info where user_id=?");
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			// �жϽ��
			if (rs.next()) {
				// ���ڽ��
				return rs.getString(1);
			}
			// ͷ�񲻴���
			return null;

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
		// Ĭ�ϲ�����
		return null;
	}

	public static void main(String args[]) {
		// UserBean user = new UserBean();
		// user.setUserName("asd8707");
		// user.setPassword("asdzxyu");
		// user.setNickName("Ц������");
		// user.setPortraitPath(null);
		// user.setGender(true);
		// int result = new UserDao().addUser(user);
		// System.out.println(result);

		System.out.println(new UserDao().findUserByUserName("asd8708"));
	}

	// TODO
	// /**
	// * �޸��û���Ϣ
	// *
	// * @param customer
	// * Ҫ�����û���Ϣ���û�����
	// * @return �����޸Ľ����1�ɹ���0ʧ��
	// */
	// public int updateUser(UserBean user) {
	// Connection conn = MySqlConnectPool.getConnection();
	// PreparedStatement pstmt = null;
	// ResultSet rs = null;
	// int result = 0;
	// try {
	// conn.setAutoCommit(false);
	// pstmt = conn
	// .prepareStatement("update user set custname=?,gender=?,address=? where email=?");
	// pstmt.setString(1, user.getCustname());
	// pstmt.setString(2, user.getGender());
	// pstmt.setString(3, user.getAddress());
	// pstmt.setString(4, user.getEmail());
	// // ִ���޸�
	// result = pstmt.executeUpdate();
	//
	// conn.commit();
	// } catch (SQLException se) {
	// try {
	// conn.rollback();
	// } catch (SQLException e) {
	// throw new RuntimeException(e);
	// }
	// throw new RuntimeException(se);
	// } finally {
	// try {
	// if (rs != null) {
	// rs.close();
	// }
	// if (pstmt != null) {
	// pstmt.close();
	// }
	// if (null != conn) {
	// conn.close();
	// }
	// } catch (SQLException e) {
	// throw new RuntimeException(e);
	// }
	// }
	// return result;
	// }

	public int updatePortraitPath(int userId, String filePath) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = MySqlConnectPool.getConnection();
			conn.setAutoCommit(false);
			// ����û�
			pstmt = conn
					.prepareStatement("update user_info set portraitpath=? where user_id=?");
			pstmt.setString(1, filePath);
			pstmt.setInt(2, userId);

			result = pstmt.executeUpdate();
			// �ύ����
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

	public void updateIMAccount(String username, String im_Account) {

	}
}
