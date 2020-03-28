package java200327_Swing_JDBC_ShoppingBag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SDAO {

	private static SDAO single;
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;

	private SDAO() {

	}

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("클래스 로드 실패 : " + e.getMessage());
		}
	}

	public static SDAO getInstance() {
		if (single == null) {
			single = new SDAO();
		}
		return single;
	}

	private boolean connect() {
		boolean cFlag = false;
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "11111111");
			cFlag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cFlag;
	}

	public ArrayList<String[]> getList() {
		ArrayList<String[]> objList = new ArrayList<>();
		SDTO searchDTO = null;

		if (connect()) {
			try {
				String sql = "select * from shoppingBag";
				stmt = conn.createStatement();
				if (stmt != null) {
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						searchDTO = new SDTO();
						searchDTO.setProduct(rs.getString("product"));
						searchDTO.setAmount(rs.getString("amount"));
						searchDTO.setUnitPrice(rs.getString("unitPrice"));
						searchDTO.setTotalPrice(rs.getString("totalPrice"));

						objList.add(searchDTO.getArray());
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("DB 접속 오류..!");
			System.exit(0);
		}
		return objList;
	}

	public boolean insertOne(SDTO dto) {
		boolean result = false;

		if (this.connect()) {
			try {
				String sql = "insert into shoppingBag values (?,?,?,?)";
				PreparedStatement psmt = conn.prepareStatement(sql);

				psmt.setString(1, dto.getProduct());
				psmt.setString(2, dto.getAmount());
				psmt.setString(3, dto.getUnitPrice());
				psmt.setString(4, dto.getTotalPrice());

				int r = psmt.executeUpdate();

				if (r > 0) {
					result = true;
				}
				psmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("DB 접속 오류..!");
			System.exit(0);
		}
		return result;
	}

	public boolean updateOne(SDTO dto) {
		boolean result = false;

		if (this.connect()) {
			try {
				String sql = "update shoppingBag set product = ?, amount = ? , unitPrice = ?, totalPrice = ? where product = ?";
				PreparedStatement psmt = conn.prepareStatement(sql);
				psmt.setString(1, dto.getProduct());
				psmt.setString(2, dto.getAmount());
				psmt.setString(3, dto.getUnitPrice());
				psmt.setString(4, dto.getTotalPrice());
				psmt.setString(5, dto.getProduct());

				int r = psmt.executeUpdate();

				if (r > 0) {
					result = true;
				}
				psmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("DB 접속 오류..!");
			System.exit(0);
		}
		return result;
	}

	public boolean deleteOne(SDTO dto) {
		boolean result = false;

		if (this.connect()) {
			try {
				String sql = "delete from shoppingbag where product = ?";
				PreparedStatement psmt = conn.prepareStatement(sql);
				psmt.setString(1, dto.getProduct());

				int r = psmt.executeUpdate();

				if (r > 0) {
					result = true;
				}
				psmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("DB 접속 오류..!");
			System.exit(0);
		}
		return result;
	}
	
	/////
	public void saveDB(ArrayList<SDTO> saveAll) {
		int a = saveAll.size();
		System.out.println(a);
		if (connect()) {
			try {
				String sql = "delete from shoppingbag";
				PreparedStatement psmt = conn.prepareStatement(sql);
				psmt.executeUpdate();
				
				sql = "insert into shoppingBag values (?,?,?,?)";
				psmt = conn.prepareStatement(sql);

				for(SDTO s : saveAll) {
					
				psmt.setString(1, s.getProduct());
				psmt.setString(2, s.getAmount());
				psmt.setString(3, s.getUnitPrice());
				psmt.setString(4, s.getTotalPrice());
				
				psmt.executeUpdate();

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
