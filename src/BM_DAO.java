package bmProject;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
public class BM_DAO extends BM_InfoValue{
	private Connection conn;
	private Statement stmt; 
	private ResultSet rs;
	private ArrayList<String> listCondition;
	private StringBuffer testEmpty,query;
	private int resultRowChange=0,checkingoutRow=1;
	public BM_DAO(){
		query=new StringBuffer();
		testEmpty= new StringBuffer();
		listCondition = new ArrayList<String>();
		/*
		 * USER_CLASS=1	: 일반
		 * USER_CLASS=1	: 직원
		 * USER_STATUS=1	: 활동
		 * USER_STATUS=2	: 정지
		 * USER_STATUS=3	: 탈퇴
		 * BOOK_STATUS=1	: 대여 가능
		 * BOOK_STATUS=2	: 대여 중
		 * BOOK_STATUS=3	: 폐기
		 * LATE_FOR_RETURN=0	: 연체 안됨
		 * LATE_FOR_RETURN=1	: 연체
		 * */
	}
	public String connDB(String id, String pw) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", id, pw);
				return "true";
			} catch (ClassNotFoundException e) {
				return e.getMessage();
			} catch (SQLException e) {
				return e.getMessage();
			}
	}
	public String searchBookOnDB(ArrayList<String> infoForQuery) {
		query.setLength(0);
		testEmpty.setLength(0);
		listCondition.clear();
		listCondition.add("LOWER(TITLE) LIKE ");
		listCondition.add("LOWER(AUTHOR) LIKE ");
		listCondition.add("LOWER(TRANSLATORS) LIKE ");
		listCondition.add("LOWER(PUBLISHER) LIKE ");
		listCondition.add("PUBDATE LIKE ");
		listCondition.add("ISBN10 LIKE ");
		listCondition.add("ISBN13 LIKE ");
		listCondition.add("LOWER(BOOK_ID) LIKE ");
		listCondition.add("LOWER(BOOK_LOC) LIKE ");
		listCondition.add("LOWER(IMPORT_DATE) LIKE ");
		listCondition.add("LOWER(EXPORT_DATE) LIKE ");
		query.append("SELECT TITLE,AUTHOR,NVL(TRANSLATORS,' ') AS TRANSLATORS,PUBLISHER,"); 
		query.append("PUBDATE,ISBN10,ISBN13,BOOK_ID,BOOK_LOC,");
		query.append("IMPORT_DATE,NVL(EXPORT_DATE,' ') AS EXPORT_DATE,");//09,10
		query.append("NVL(THUMBNAIL_URL,' ') AS THUMBNAIL_URL,BOOK_STATUS,");//11,12
		query.append("CASE WHEN BOOK_STATUS<>2 THEN ' ' ELSE CHECKED_OUT_DATE END AS CHECKED_OUT_DATE,"); //13
		query.append("CASE WHEN BOOK_STATUS<>2 THEN ' ' ELSE RETURN_DATE END AS RETURN_DATE ");//14
		query.append("FROM (SELECT * FROM BOOKINFO JOIN BOOKSTATUS USING (BOOK_ID)) LEFT OUTER JOIN("); 
		query.append("SELECT BOOK_ID, MAX(CHECKED_OUT_DATE) CHECKED_OUT_DATE,MAX(RETURN_DATE) RETURN_DATE "); 
		query.append("FROM CHECKINGOUT GROUP BY BOOK_ID) USING (BOOK_ID) WHERE ");
		if(infoForQuery.get(0).equals("BOOK_ID")) {
			query.append(listCondition.get(7) + "LOWER('%" + infoForQuery.get(1) + "%')");
		}
		else if(infoForQuery.get(0).equals("TITLE")) {
			query.append(listCondition.get(0) + "LOWER('%" + infoForQuery.get(1) + "%')");
		}
		else {
			for (int i = 0, cnt = 0; i < infoForQuery.size(); i++) {
				if (infoForQuery.get(i).isEmpty()) {
				} else if (infoForQuery.get(i).isEmpty() != true && cnt == 0) {
					query.append(listCondition.get(i) + "LOWER('%" + infoForQuery.get(i) + "%')");
					cnt = 1;
				} else {
					query.append(" AND " + listCondition.get(i) + "LOWER('%" + infoForQuery.get(i) + "%')");
					cnt = 1;
				}
			}
		}
		query.append(" ORDER BY BOOK_ID DESC");
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery(query.toString());
			while(rs.next()) {
				for(int i=0;i<searchBookInfo.length;i++) {
					searchBookInfo[i].add(rs.getString(i+1));
					testEmpty.append(rs.getString(i+1));
				}
			}
			if(testEmpty.toString().isEmpty()) {
				testEmpty.setLength(0);
				rs.close();
				stmt.close();
				return "empty";
			}
			else {
				testEmpty.setLength(0);
				rs.close();
				stmt.close();
				return "true";
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public String searchUser(ArrayList<String> infoForQuery) {
		query.setLength(0);
		testEmpty.setLength(0);
		listCondition.clear();
		listCondition.add("LOWER(USER_ID) LIKE ");
		listCondition.add("LOWER(USER_NAME) LIKE ");
		listCondition.add("USER_PHONENUMBER LIKE ");
		listCondition.add("LOWER(DATE_OF_ADMISSION) LIKE ");
		listCondition.add("LOWER(USER_ADDRESS) LIKE ");
		query.setLength(0);
		query.append("SELECT USER_ID,USER_NAME,USER_PHONENUMBER,DATE_OF_ADMISSION,USER_ADDRESS,USER_CLASS,"); 
		query.append("USER_STATUS,NUM_OF_POSSIBLE_CHECKED_OUT,NUM_OF_CHECKED_OUT_BOOK,TOTAL_LATE_FEE ");
		query.append("FROM USERINFO JOIN USERSTATUS USING (USER_ID) WHERE ");
		for(int i=0,cnt=0;i<listCondition.size();i++) {
			if(infoForQuery.get(i).isEmpty()) {}
			else if(infoForQuery.get(i).isEmpty()!=true && cnt==0) {
				query.append(listCondition.get(i)+"LOWER('%"+infoForQuery.get(i)+"%')");
				cnt=1;
			}
			else {
				query.append(" AND "+listCondition.get(i)+"LOWER('%"+infoForQuery.get(i)+"%')");
				cnt=1;
			}
		}
		query.append(" ORDER BY DATE_OF_ADMISSION DESC,USER_STATUS DESC");
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery(query.toString());
			while(rs.next()) {
				for(int i=0;i<searchUserInfo.length;i++) {
					searchUserInfo[i].add(rs.getString(i+1));
					testEmpty.append(rs.getString(i+1));
				}
			}
			if(testEmpty.toString().isEmpty()) {
				testEmpty.setLength(0);
				rs.close();
				stmt.close();
				return "empty";
			}
			else {
				testEmpty.setLength(0);
				rs.close();
				stmt.close();
				return "true";
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public String searchUserBookList(String userID) {
		testEmpty.setLength(0);
		query.setLength(0);
		query.append("SELECT BOOK_ID,TITLE,CHECKED_OUT_DATE,RETURN_DATE,NVL(RETURNED_DATE,' ') AS RETURNED_DATE,");
		query.append("LATE_FEE-PAID_FEE AS FEE,NO FROM BOOKINFO JOIN CHECKINGOUT "); 
		query.append("USING (BOOK_ID) WHERE USER_ID='"+userID+"' ");		
		query.append("ORDER BY CHECKINGOUT.MODIFIED_DATE DESC");
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery(query.toString());
			while(rs.next()) {
				for(int i=0;i<searchUserBookList.length;i++) {
					searchUserBookList[i].add(rs.getString(i+1));
					testEmpty.append(rs.getString(i+1));
				}
			}
			testEmpty.setLength(0);
			rs.close();
			stmt.close();
			return "true";
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public String importBook(ArrayList<String> infoForQuery) {
		resultRowChange=0;
		query.setLength(0);
		query.append("INSERT ALL INTO BOOKINFO VALUES (");
		for(int i=0;i<11;i++) {
			query.append("'"+infoForQuery.get(i).toString().trim()+"',");
		}
		query.append("'"+infoForQuery.get(11).toString().trim()+"',SYSDATE,'') ");
		query.append("INTO BOOKSTATUS VALUES (");
		query.append("'"+infoForQuery.get(7).toString().trim()+"',");
		query.append(infoForQuery.get(12).toString().trim()+",SYSDATE,'') ");
		query.append("SELECT * FROM DUAL");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "true";
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public String modifiyBook(ArrayList<String> infoForQuery) {
		query.setLength(0);
		resultRowChange=0;
		query.append("UPDATE BOOKSTATUS SET BOOK_STATUS='"+infoForQuery.get(12)+"',MODIFIED_DATE=SYSDATE WHERE BOOK_ID='"+infoForQuery.get(7)+"'");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
		query.append("UPDATE BOOKINFO SET TITLE='"+infoForQuery.get(0)+"',AUTHOR='"+infoForQuery.get(1)+"',");
		query.append("TRANSLATORS='"+infoForQuery.get(2)+"',PUBLISHER='"+infoForQuery.get(3)+"',PUBDATE='"+infoForQuery.get(4)+"',");
		query.append("ISBN10='"+infoForQuery.get(5)+"',ISBN13='"+infoForQuery.get(6)+"',BOOK_LOC='"+infoForQuery.get(8)+"',");
		query.append("IMPORT_DATE='"+infoForQuery.get(9)+"',EXPORT_DATE='"+infoForQuery.get(10)+"',MODIFIED_DATE=SYSDATE WHERE BOOK_ID='"+infoForQuery.get(7)+"'");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "true";
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public String checkingoutBook(ArrayList<String> infoForQuery) {
		checkingoutRow=1;
		resultRowChange=0;
		query.setLength(0);
		query.append("SELECT NVL(MAX(NO),0) FROM CHECKINGOUT");
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery(query.toString());
			rs.next();
			checkingoutRow+=Integer.parseInt(rs.getString(1));
			testEmpty.setLength(0);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			return e.getMessage();
		}
		query.setLength(0);
		query.append("UPDATE BOOKSTATUS SET BOOK_STATUS=2,MODIFIED_DATE=SYSDATE WHERE BOOK_ID='"+infoForQuery.get(7)+"'");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
		query.append("UPDATE USERSTATUS SET NUM_OF_POSSIBLE_CHECKED_OUT=NUM_OF_POSSIBLE_CHECKED_OUT-1, ");
		query.append("NUM_OF_CHECKED_OUT_BOOK=NUM_OF_CHECKED_OUT_BOOK+1,MODIFIED_DATE=SYSDATE WHERE USER_ID='"+infoForQuery.get(15)+"'");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
		query.append("INSERT INTO CHECKINGOUT VALUES("+checkingoutRow+",'"+infoForQuery.get(15)+"','"+infoForQuery.get(7));
		query.append("','"+infoForQuery.get(13)+"','"+infoForQuery.get(14)+"','',0,0,0,SYSDATE,'')");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				testEmpty.setLength(0);
				stmt.close();
				return "true";
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public String returnBook(ArrayList<String> infoForQuery,int lateDay) {
		resultRowChange=0;
		query.setLength(0);
		if(lateDay<1) {
			query.append("UPDATE CHECKINGOUT SET RETURNED_DATE='"+infoForQuery.get(16)+"',MODIFIED_DATE=SYSDATE ");
			query.append("WHERE USER_ID='"+infoForQuery.get(15)+"' AND BOOK_ID='"+infoForQuery.get(7)+"'");
			query.append("AND RETURNED_DATE IS NULL");
		}
		else if(0<lateDay) {
			query.append("UPDATE CHECKINGOUT SET RETURNED_DATE='"+infoForQuery.get(16)+"',");
			query.append("LATE_FOR_RETURN=1,LATE_FEE=100*"+lateDay+",MODIFIED_DATE=SYSDATE ");
			query.append("WHERE USER_ID='"+infoForQuery.get(15)+"' AND BOOK_ID='"+infoForQuery.get(7)+"'");
			query.append("AND RETURNED_DATE IS NULL");
		}
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
		query.append("UPDATE BOOKSTATUS SET BOOK_STATUS=1,MODIFIED_DATE=SYSDATE WHERE BOOK_ID='"+infoForQuery.get(7)+"'");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
		query.append("UPDATE USERSTATUS SET NUM_OF_POSSIBLE_CHECKED_OUT=NUM_OF_POSSIBLE_CHECKED_OUT+1, ");
		query.append("NUM_OF_CHECKED_OUT_BOOK=NUM_OF_CHECKED_OUT_BOOK-1,MODIFIED_DATE=SYSDATE WHERE USER_ID='"+infoForQuery.get(15)+"'");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "true";
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public String addmissionUser(ArrayList<String> infoForQuery) {
		query.setLength(0);
		resultRowChange=0;
		query.append("INSERT ALL INTO USERINFO VALUES (");
		for(int i=0;i<5;i++) {
			query.append("'"+infoForQuery.get(i).toString().trim()+"',");
		}
		query.append("'"+infoForQuery.get(5).toString().trim()+"',SYSDATE,'') ");
		query.append("INTO USERSTATUS VALUES (");
		query.append("'"+infoForQuery.get(0).toString().trim()+"',");
		query.append(infoForQuery.get(6).toString().trim()+",");
		query.append(infoForQuery.get(7).toString().trim()+",0,0,SYSDATE,'') ");
		query.append("SELECT * FROM DUAL");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "true";
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public String modifyUser(ArrayList<String> infoForQuery) {
		query.setLength(0);
		resultRowChange=0;
		query.append("UPDATE USERSTATUS SET USER_STATUS="+infoForQuery.get(6)+",");
		query.append("NUM_OF_POSSIBLE_CHECKED_OUT="+infoForQuery.get(7)+",");
		query.append("NUM_OF_CHECKED_OUT_BOOK="+infoForQuery.get(8)+",");
		query.append("MODIFIED_DATE=SYSDATE WHERE USER_ID='"+infoForQuery.get(0)+"'");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
		query.append("UPDATE USERINFO SET USER_NAME='"+infoForQuery.get(1)+"',USER_PHONENUMBER='"+infoForQuery.get(2)+"',");
		query.append("DATE_OF_ADMISSION='"+infoForQuery.get(3)+"',USER_ADDRESS='"+infoForQuery.get(4)+"',");
		query.append("USER_CLASS="+infoForQuery.get(5)+",MODIFIED_DATE=SYSDATE WHERE USER_ID='"+infoForQuery.get(0)+"'");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "true";
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public String payLateFee(ArrayList<String> infoForQuery) {
		query.setLength(0);
		resultRowChange=0;
		query.append("UPDATE CHECKINGOUT SET PAID_FEE=PAID_FEE+'"+infoForQuery.get(3)+"',MODIFIED_DATE=SYSDATE ");
		query.append("WHERE USER_ID='"+infoForQuery.get(0)+"' AND BOOK_ID='"+infoForQuery.get(1)+"' ");
		query.append("AND NO='"+infoForQuery.get(2)+"'");
		System.out.println(query);
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
		query.append("UPDATE USERSTATUS A SET TOTAL_LATE_FEE=(SELECT FEE FROM (SELECT SUM(LATE_FEE-PAID_FEE) AS FEE,");
		query.append("USER_ID FROM CHECKINGOUT JOIN USERSTATUS USING (USER_ID) WHERE USER_ID ='"+infoForQuery.get(0)+"' ");
		query.append("GROUP BY USER_ID) B WHERE A.USER_ID=B.USER_ID),");
		query.append("MODIFIED_DATE=SYSDATE WHERE USER_ID ='"+infoForQuery.get(0)+"'");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "true";
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public String modifyLateFee() {
		query.setLength(0);
		resultRowChange=0;
		query.append("UPDATE CHECKINGOUT B SET B.LATE_FOR_RETURN=(SELECT A.LATE_FOR_RETURN FROM (");
		query.append("SELECT NO,CASE WHEN TO_DATE(SYSDATE)-TO_DATE(RETURN_DATE)<0 THEN '0' ELSE '1' END AS LATE_FOR_RETURN ");
		query.append("FROM CHECKINGOUT) A WHERE B.NO=A.NO),B.LATE_FEE=(SELECT C.LATE_FEE FROM (");
		query.append("SELECT NO,CASE WHEN TO_DATE(SYSDATE)-TO_DATE(RETURN_DATE)<0 THEN 0 ELSE TO_NUMBER(TO_DATE(SYSDATE)-TO_DATE(RETURN_DATE))*100 END AS LATE_FEE ");
		query.append("FROM CHECKINGOUT) C WHERE B.NO=C.NO),B.MODIFIED_DATE=SYSDATE WHERE RETURNED_DATE IS NULL");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
		query.append("MERGE INTO USERSTATUS A USING (SELECT SUM(LATE_FEE-PAID_FEE) AS FEE, USER_ID, USER_STATUS ");
		query.append("FROM CHECKINGOUT JOIN USERSTATUS USING (USER_ID) GROUP BY USER_ID,USER_STATUS ");
		query.append("HAVING 0<SUM(LATE_FEE-PAID_FEE)) B ON (A.USER_ID=B.USER_ID AND B.USER_STATUS<>3) WHEN MATCHED THEN ");
		query.append("UPDATE SET TOTAL_LATE_FEE=(SELECT FEE FROM (SELECT SUM(LATE_FEE-PAID_FEE) AS FEE, USER_ID ");
		query.append("FROM CHECKINGOUT JOIN USERSTATUS USING (USER_ID) GROUP BY USER_ID HAVING 0<SUM(LATE_FEE-PAID_FEE)) C ");
		query.append("WHERE A.USER_ID=C.USER_ID), MODIFIED_DATE=SYSDATE");
		try {
			stmt=conn.createStatement();
			resultRowChange=stmt.executeUpdate(query.toString());
			if(resultRowChange==0) {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "false";
			}
			else {
				resultRowChange=0;
				query.setLength(0);
				stmt.close();
				return "true";
			}
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public String getDate(int plus) {
		testEmpty.setLength(0);
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery("SELECT TO_CHAR(SYSDATE+"+plus+",'YYYY-MM-DD') FROM DUAL");
			rs.next();
			testEmpty.append(rs.getString(1));
			rs.close();
			stmt.close();
			return testEmpty.toString();
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public String isLate(String returnDate) {
		testEmpty.setLength(0);
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery("SELECT TO_DATE(SYSDATE)-TO_DATE('"+returnDate+"') FROM DUAL");
			rs.next();
			testEmpty.append(rs.getString(1));
			rs.close();
			stmt.close();
			return testEmpty.toString();
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	public void quitDAO() {
		try {
			conn.close();
		} catch (SQLException e) {
		}
	}
}