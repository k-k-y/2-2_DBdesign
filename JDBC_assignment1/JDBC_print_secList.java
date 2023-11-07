package assignment1;

import java.sql.*;
import java.util.Scanner;

public class JDBC_print_secList 
{
	static final String DB_URL ="jdbc:mysql://localhost/assignment1_schema?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	static final String USER = "root"; // user name
	static final String PASS = "k6385112"; // user password
	
	static final String QUERY1 = "SELECT * FROM student WHERE ID = ?";
	static final String QUERY2 = "SELECT T.course_id, C.title, T.sec_id, T.semester, T.year, T.grade " +
							 "FROM course as C, takes as T WHERE ID = ? and T.course_id = C.course_id"; 
	
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
	
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			pstmt1 = conn.prepareStatement(QUERY1);
			pstmt2 = conn.prepareStatement(QUERY2);
			
			while (true)
			{
				System.out.print("학번(ID) >> ");
				String input = sc.nextLine(); 
				
				if (input == "") // exit condition
				{
					System.out.println("프로그램 종료!!");
					break;
				}
				
				pstmt1.setString(1,  input); // 쿼리문의 ? 자리에 입력 넣기
				pstmt2.setString(1,  input);
				
				try (ResultSet rs = pstmt1.executeQuery())
				{
					if (!rs.next())
						System.out.println(input + "학번을 가진 학생은 없습니다.");
					else
					{
						System.out.println("<학생정보>");
						System.out.print("학번 : " + rs.getString("ID") + ", ");
						System.out.print("이름 : " + rs.getString("name") + ", ");
						System.out.print("학과: " + rs.getString("dept_name") + ", ");
						System.out.println("총 수강학점 : " + rs.getString("tot_cred"));
						System.out.println("<수강과목 정보>");
					}
				}
				
				try (ResultSet rs = pstmt2.executeQuery())
				{
					while (rs.next())
					{
						System.out.print("과목번호 : " + rs.getString("T.course_id") + ", ");
						System.out.print("과목명 : " + rs.getString("C.title") + ", ");
						System.out.print("분반번호 : " + rs.getString("T.sec_id") + ", ");
						System.out.print("년도 : " + rs.getString("T.year") + ", ");
						System.out.print("학기 : " + rs.getString("T.semester") + ", ");
						System.out.println("성적 : " + rs.getString("T.grade"));
					}
					System.out.println("");
					
				}
			}
			
			
		} catch (SQLException e) {
			System.out.println("SQLException : " + e);
			} finally {
					try {
						pstmt1.close();
						pstmt2.close();
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
	}

}
