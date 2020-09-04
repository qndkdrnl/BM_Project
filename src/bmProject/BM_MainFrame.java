package bmProject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
public class BM_MainFrame extends BM_InfoValue{
	private JFrame jfMain;
	private JTabbedPane jtpMainTab;
	private final String titleString[],tabNameString[],menuString[],messageString[],errorString[],noString,bookInfoString[],userInfoString[],buttonNameString[];
	private JTextField jtfInfo[][],jtfTmp[];
	private JLabel jlthumbnail[];
	private DefaultTableModel dtmModel[];
	private JPanel tabjPanel[];
	private Font ngP,ngB;
	private Toolkit tk;
	private Dimension screensize;
	private int identify=5,isLogin=0,selectedRow=0,isLate=0;
	private final int colNum[][]={{7,0,8,12,14},{0,1,2,5,4,7},{7,0,8,12,14},{0,1,2,5,4,3},{7,0,8,9,10}};
	private double multplyCoef;
	private boolean checkByUser;
	private StringBuffer neededDate,tempST;
	private BM_DAO dao;
	BM_MainFrame(){
		/*
		 * USER_CLASS=1	: 일반
		 * USER_CLASS=1	: 직원
		 * USER_STATUS=1	: 활동
		 * USER_STATUS=2	: 정지
		 * USER_STATUS=3	: 탈퇴
		 * BOOK_STATUS=1	: 대여가능
		 * BOOK_STATUS=2	: 대여중
		 * BOOK_STATUS=3	: 폐기
		 * */
		dao=new BM_DAO();
		tk= Toolkit.getDefaultToolkit();
		screensize=tk.getScreenSize();
		if(1000<=screensize.getWidth()) {
			multplyCoef=1*screensize.getWidth()/1000;
		}
		else {
			multplyCoef=1;
		}
		setUIFont(new FontUIResource("NanumGothic", Font.PLAIN, 13));
		ngP=new Font("NanumGothic",Font.PLAIN,15);
		ngB=new Font("NanumGothic",Font.BOLD,15);
		checkByUser=false;
		tempST=new StringBuffer();
		neededDate=new StringBuffer();
		jfMain=new JFrame();
		titleString=new String[10];
		tabNameString=new String[5];
		menuString=new String[6];
		messageString=new String[8];
		errorString=new String[10];
		bookInfoString=new String[18];
		userInfoString=new String[19];
		buttonNameString=new String[14];
		jtfInfo=new JTextField[5][11];
		jtfTmp=new JTextField[2];
		jtfTmp[0]=new JTextField();
		jtfTmp[1]=new JTextField();
		dtmModel=new DefaultTableModel[6];
		dtmModel[0]=new DefaultTableModel(0,6);
		dtmModel[1]=new DefaultTableModel(0,7);
		dtmModel[2]=new DefaultTableModel(0,6);
		dtmModel[3]=new DefaultTableModel(0,7);
		dtmModel[4]=new DefaultTableModel(0,6);
		dtmModel[5]=new DefaultTableModel(0,7);
		tabjPanel=new JPanel[6];
		jlthumbnail=new JLabel[6];
		for(int i=0;i<jtfInfo.length;i++) {
			tabjPanel[i]=new JPanel();
			tabjPanel[i].setBackground(Color.LIGHT_GRAY);
			jlthumbnail[i]=new JLabel();
			jlthumbnail[i].setHorizontalAlignment(JLabel.CENTER);
		}
		noString="NO.";
		bookInfoString[5]="ISBN";
		bookInfoString[6]="ISBN13";
		userInfoString[0]="ID";
		if(System.getProperty("user.language").toString().equals("ko")) {
			titleString[0]="도서 관리 시스템";
			titleString[1]="로그인";
			titleString[2]="책 정보 확인";
			titleString[3]="가입자 정보 확인";
			titleString[4]="에러";
			titleString[5]="검색 결과";
			titleString[6]="입력 결과";
			titleString[7]="연체료 납부";
			errorString[0]="아이디/비밀번호가 틀렸습니다";
			errorString[1]="검색 조건은 한글자 이상 입력해주세요";
			errorString[2]="정보를 다시 확인해서 입력해주세요";
			errorString[3]="더 이상 대출할 수 없습니다, 최대 대츨 권수 : ";
			errorString[4]="이미 반납한 책입니다";
			errorString[5]="KAKAO API 에러입니다 : ";
			errorString[6]="DB 오류입니다 : ";
			errorString[7]="도서 정보 입력에 실패했습니다 정보를 다시 확인해주세요";
			errorString[8]="100원 이상만 납부 가능합니다";
			errorString[9]="연체료 이하만 납부 가능합니다";
			messageString[0]="종 료";
			messageString[1]="종료하시겠습니까?";
			messageString[2]="검색이 끝났습니다";
			messageString[3]="검색 조건에 해당하는 결과가 없습니다";
			messageString[4]="DB에 정보 입력이 성공했습니다";
			messageString[5]="연체료를 납부하시겠습니까? ";
			messageString[6]="원 입니다";
			messageString[7]="연체료가 납부되었습니다";
			tabNameString[0]="도서 검색";
			tabNameString[1]="회원 검색";
			tabNameString[2]="대출/반납";
			tabNameString[3]="회원 가입/수정";
			tabNameString[4]="도서 반입/수정";
			menuString[0]="파 일";
			menuString[1]="로그인";
			menuString[2]="로그아웃";
			menuString[3]="종 료";
			menuString[4]="아이디";
			menuString[5]="비밀번호";
			bookInfoString[0]="제 목";
			bookInfoString[1]="저 자";
			bookInfoString[2]="역 자";
			bookInfoString[3]="출판사";
			bookInfoString[4]="출판일";
			bookInfoString[7]="관리번호";
			bookInfoString[8]="소장처";
			bookInfoString[9]="반입일";
			bookInfoString[10]="반출일";
			bookInfoString[11]="이미지";
			bookInfoString[12]="도서상태";
			bookInfoString[13]="대출일";
			bookInfoString[14]="반납예정일";
			bookInfoString[15]="대출가능";
			bookInfoString[16]="대출 중";
			bookInfoString[17]="폐 기";
			userInfoString[1]="이 름";
			userInfoString[2]="전화번호";
			userInfoString[3]="가입일";
			userInfoString[4]="주 소";
			userInfoString[5]="분 류";
			userInfoString[6]="상 태";
			userInfoString[7]="대여 가능";
			userInfoString[8]="대여 현황";
			userInfoString[9]="연체료";
			userInfoString[10]="입 력";
			userInfoString[11]="반납일";
			userInfoString[12]="일 반";
			userInfoString[13]="직 원";
			userInfoString[14]="활 동";
			userInfoString[15]="정 지";
			userInfoString[16]="탈 퇴";
			userInfoString[17]="회원정보 수정";
			userInfoString[18]="신규 회원가입";
			buttonNameString[0]="확 인";
			buttonNameString[1]="취 소";
			buttonNameString[2]="종 료";
			buttonNameString[3]="검 색";
			buttonNameString[4]="초기화";
			buttonNameString[5]="반 입";
			buttonNameString[6]="반 출";
			buttonNameString[7]="대 출";
			buttonNameString[8]="반 납";
			buttonNameString[9]="연체료 반납";
			buttonNameString[10]="가 입";
			buttonNameString[11]="수 정";
			buttonNameString[12]="도서관 내 검색";
			buttonNameString[13]="인터넷 검색";
		}
		else {
			titleString[0]="Book Mangement System";
			titleString[1]="Login";
			titleString[2]="Checking Book Info";
			titleString[3]="Checking User Info";
			titleString[4]="Error";
			titleString[5]="Searchinig Result";
			titleString[6]="Inserting Result";
			titleString[7]="Paying Late Fee";
			errorString[0]="Wrong ID/PW";
			errorString[1]="More then one work for searching";
			errorString[2]="Check again Info";
			errorString[3]="Can't check out anymore. Maximum checking out number : ";
			errorString[4]="이미 반납한 책입니다";
			errorString[5]="KAKAO API Error : ";
			errorString[6]="DB Error : ";
			errorString[7]="Fail to input on DB. Check again Info";
			errorString[8]="You must pay more then 100 won";
			errorString[9]="You can't pay more then late fee";
			messageString[0]="Quit";
			messageString[1]="Do you want to quit?";
			messageString[2]="Searching Complete";
			messageString[3]="No result";
			messageString[4]="Success to input on DB";
			messageString[5]="Do you want to pay late fee? ";
			messageString[6]="won";
			messageString[7]="Paying late fee is done";
			tabNameString[0]="Search Book";
			tabNameString[1]="Search User";
			tabNameString[2]="Checking out/Return";
			tabNameString[3]="Addmission/Modify User";
			tabNameString[4]="Import/Modify Book";
			menuString[0]="File";
			menuString[1]="Login";
			menuString[2]="Logout";
			menuString[3]="Quit";
			menuString[4]="I D";
			menuString[5]="Password";
			bookInfoString[0]="Titel";
			bookInfoString[1]="Author";
			bookInfoString[2]="Translators";
			bookInfoString[3]="Publisher";
			bookInfoString[4]="Pub Date";
			bookInfoString[7]="Book ID";
			bookInfoString[8]="Book Loc";
			bookInfoString[9]="Import Date";
			bookInfoString[10]="Export Date";
			bookInfoString[11]="Image";
			bookInfoString[12]="Book Status";
			bookInfoString[13]="Checked Out Date";
			bookInfoString[14]="Return Date";
			bookInfoString[15]="On Library";
			bookInfoString[16]="Checked Out";
			bookInfoString[17]="Export";
			userInfoString[1]="Name";
			userInfoString[2]="PhoneNumber";
			userInfoString[3]="Add Date";
			userInfoString[4]="Address";
			userInfoString[5]="Class";
			userInfoString[6]="Status";
			userInfoString[7]="Possible Checking out";
			userInfoString[8]="Checking out";
			userInfoString[9]="Late Fee";
			userInfoString[10]="Insert";
			userInfoString[11]="Returned Date";
			userInfoString[12]="User";
			userInfoString[13]="Employee";
			userInfoString[14]="Activate";
			userInfoString[15]="Deactivate";
			userInfoString[16]="Dismissed";
			userInfoString[17]="Modify User Info";
			userInfoString[18]="Add User";
			buttonNameString[0]="O K";
			buttonNameString[1]="Cancle";
			buttonNameString[2]="Quit";
			buttonNameString[3]="Search";
			buttonNameString[4]="Init";
			buttonNameString[5]="Import";
			buttonNameString[6]="Export";
			buttonNameString[7]="Checking Out";
			buttonNameString[8]="Return";
			buttonNameString[9]="Paying Late Fee";
			buttonNameString[10]="Addmission";
			buttonNameString[11]="Modify";
			buttonNameString[12]="Searching on Library";
			buttonNameString[13]="Searching on Internet";
		}
	}
	public void mainScreen() {
		jtpMainTab = new JTabbedPane();
		JMenuBar mbMain=new JMenuBar();
		JMenu mFile=new JMenu(menuString[0]);
		JMenuItem mfLogin=new JMenuItem(menuString[1]);
		//JMenuItem mfLogout=new JMenuItem(menuString[2]);
		JMenuItem mfQuit=new JMenuItem(menuString[3]);
		mfLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jfMain.add(jtpMainTab);
				loginDialog();
			}
		});
		/*mfLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Clicked "+e.getSource());
			}
		});*/
		mfQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quitDialog();
			}
		});
		mFile.add(mfLogin);
		//mFile.add(mfLogout);
		mFile.add(mfQuit);
		mbMain.add(mFile);
		jfMain.setDefaultCloseOperation(0);
		jfMain.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				quitDialog();
			}
		});
		jfMain.setTitle(titleString[0]);
		jfMain.setJMenuBar(mbMain);
		jfMain.add(jtpMainTab);
		jfMain.setSize(1280, 800);
		//jfMain.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jfMain.setResizable(false);
		jfMain.setVisible(true);
		loginDialog();
	}
	public void searchBookTab(int tabNum) {		//도서 검색 탭
		//[도서 검색] 객체 생성, 변수 선언
		String colNameString[]= {noString,bookInfoString[7],bookInfoString[0],bookInfoString[8],bookInfoString[12],bookInfoString[14]};
		dtmModel[tabNum].setColumnIdentifiers(colNameString);
		JTable jtTable = new JTable(dtmModel[tabNum]) {
			private static final long serialVersionUID = 9121749437267121978L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();		//JTabel 열 정렬
		celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
		JScrollPane jsPane=new JScrollPane(jtTable);
		JPanel sbtjPanel[]=new JPanel[3];
		JLabel sbtjLabel[]=new JLabel[10];
		JButton sbtjButton[]=new JButton[2];		
		int pWidth,pHeight,sbtjpWidth[],sbtjpHeight[];
		sbtjpWidth=new int [3];
		sbtjpHeight=new int [3];
		pWidth=tabjPanel[tabNum].getWidth();
		pHeight=tabjPanel[tabNum].getHeight();
		//[도서 검색] 내부패널 0~2 생성, 추가, 위치 설정
		tabjPanel[tabNum].setLayout(null);
		for(int i=0;i<sbtjPanel.length;i++) {
			sbtjPanel[i]=new JPanel();
			tabjPanel[tabNum].add(sbtjPanel[i]);
		}
		sbtjPanel[0].setBounds(0, 0, (pWidth*5/6)-1, (pHeight/3)-1);
		sbtjPanel[1].setBounds(pWidth*5/6, 0, (pWidth/6)-1, (pHeight/3)-1);
		sbtjPanel[2].setBounds(0, pHeight/3, pWidth-1, (pHeight*2/3)-1);
		for(int i=0;i<sbtjPanel.length;i++) {
			sbtjpWidth[i]=sbtjPanel[i].getWidth();
			sbtjpHeight[i]=sbtjPanel[i].getHeight();
		}
		//[도서 검색] 내부패널[0] 레이아웃 설정 및 내부패널[0]에 들어갈 JLabel,JTextField 생성,설정 
		sbtjPanel[0].setLayout(null);
		for(int i=0;i<sbtjLabel.length;i++) {							//JLabel 설정
			sbtjLabel[i]=new JLabel();
			sbtjLabel[i].setVerticalAlignment(JLabel.CENTER);
			sbtjLabel[i].setHorizontalAlignment(JLabel.CENTER);
			sbtjLabel[i].setFont(ngB);
		}
		sbtjLabel[0].setText(bookInfoString[0]);
		sbtjLabel[1].setText(bookInfoString[1]);
		sbtjLabel[2].setText(bookInfoString[2]);
		sbtjLabel[3].setText(bookInfoString[3]);
		sbtjLabel[4].setText(bookInfoString[4]);
		sbtjLabel[5].setText(bookInfoString[5]);
		sbtjLabel[6].setText(bookInfoString[7]);
		sbtjLabel[7].setText(bookInfoString[8]);
		sbtjLabel[8].setText(bookInfoString[12]);
		sbtjLabel[9].setText(bookInfoString[14]);
		for(int i=0;i<jtfInfo[tabNum].length;i++) {					//JTextField 설정
			jtfInfo[tabNum][i]=new JTextField();
			jtfInfo[tabNum][i].setFont(ngP);
			jtfInfo[tabNum][i].setHorizontalAlignment(JLabel.CENTER);
		}
		//[도서 검색] 내부패널[0]에 생성한 객체 추가
		for(int i=0;i<sbtjLabel.length;i++) {
			sbtjPanel[0].add(sbtjLabel[i]);
			sbtjPanel[0].add(jtfInfo[tabNum][i]);
		}
		sbtjPanel[0].add(jtfInfo[tabNum][10]);
		sbtjPanel[0].add(jlthumbnail[tabNum]);
		//[도서 검색] 내부패널[0] 내 위치 설정
		sbtjLabel[0].setBounds(sbtjpWidth[0]/6, sbtjpHeight[0]/16, sbtjpWidth[0]/12,sbtjpHeight[0]/8);
		jtfInfo[tabNum][0].setBounds(sbtjpWidth[0]/4+5, sbtjpHeight[0]/16, sbtjpWidth[0]*3/4-15, sbtjpHeight[0]/8);
		for(int i=1,j=1;i<sbtjLabel.length-2;j++) {
			sbtjLabel[i++].setBounds(sbtjpWidth[0]/6, sbtjpHeight[0]*(3*j+1)/16, sbtjpWidth[0]/12,sbtjpHeight[0]/8);
			sbtjLabel[i++].setBounds(sbtjpWidth[0]*7/12, sbtjpHeight[0]*(3*j+1)/16, sbtjpWidth[0]/12,sbtjpHeight[0]/8);
		}
		sbtjLabel[9].setBounds(sbtjpWidth[0]*19/24, sbtjpHeight[0]*13/16, sbtjpWidth[0]/12,sbtjpHeight[0]/8);
		jlthumbnail[tabNum].setBounds(0,0,sbtjpWidth[0]/6,(pHeight/3)-2);
		for(int i=1,j=1;i<5;j++) {
			jtfInfo[tabNum][i++].setBounds(sbtjpWidth[0]/4+5, sbtjpHeight[0]*(3*j+1)/16, sbtjpWidth[0]/3-15, sbtjpHeight[0]/8);
			jtfInfo[tabNum][i++].setBounds(sbtjpWidth[0]*2/3+5, sbtjpHeight[0]*(3*j+1)/16, sbtjpWidth[0]/3-15, sbtjpHeight[0]/8);
		}
		jtfInfo[tabNum][5].setBounds(sbtjpWidth[0]/4+5, sbtjpHeight[0]*5/8, sbtjpWidth[0]*7/48-15, sbtjpHeight[0]/8);
		jtfInfo[tabNum][6].setBounds(sbtjpWidth[0]*19/48+5, sbtjpHeight[0]*5/8, sbtjpWidth[0]*9/48-15, sbtjpHeight[0]/8);
		jtfInfo[tabNum][7].setBounds(sbtjpWidth[0]*2/3+5, sbtjpHeight[0]*5/8, sbtjpWidth[0]/3-15, sbtjpHeight[0]/8);
		jtfInfo[tabNum][8].setBounds(sbtjpWidth[0]/4+5, sbtjpHeight[0]*13/16, sbtjpWidth[0]/3-15, sbtjpHeight[0]/8);
		jtfInfo[tabNum][9].setBounds(sbtjpWidth[0]*2/3+5, sbtjpHeight[0]*13/16, sbtjpWidth[0]/8-15, sbtjpHeight[0]/8);
		jtfInfo[tabNum][10].setBounds(sbtjpWidth[0]*7/8+5, sbtjpHeight[0]*13/16, sbtjpWidth[0]/8-15, sbtjpHeight[0]/8);
		//[도서 검색] 내부패널[1] 레이아웃 설정 및 내부패널[1]에 들어갈 JButton 생성,설정
		sbtjPanel[1].setLayout(null);
		sbtjButton[0]=new JButton(buttonNameString[3]);
		sbtjButton[1]=new JButton(buttonNameString[4]);
		sbtjButton[0].setFont(ngB);
		sbtjButton[1].setFont(ngB);
		//[도서 검색] 내부패널[1]에 생성한 객체 추가
		sbtjPanel[1].add(sbtjButton[0]);
		sbtjPanel[1].add(sbtjButton[1]);
		//[도서 검색] 내부패널[1] 내 JButton Action 설정
		sbtjButton[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(searchAction(tabNum,false)) {
					sbtjButton[0].setEnabled(false);
				}
			}
		});
		sbtjButton[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<8;i++) {
					jtfInfo[tabNum][i].setEditable(true);
					jtfInfo[tabNum][i].setBackground(Color.WHITE);
				}
				sbtjButton[0].setEnabled(true);
				clearTextFieldAndTable(tabNum);
			}
		});
		//[도서 검색] 내부패널[1] 내 위치 설정
		sbtjButton[0].setBounds(10, 10, sbtjpWidth[1]-20, sbtjpHeight[1]*4/5-20);
		sbtjButton[1].setBounds(10, sbtjpHeight[1]*4/5, sbtjpWidth[1]-20, sbtjpHeight[1]/5-10);
		//[도서 검색] JTabel 행 클릭시 Action 설정
		ListSelectionModel rowSel = jtTable.getSelectionModel();
		rowSel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowSel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (!e.getValueIsAdjusting() && -1 < lsm.getMinSelectionIndex()) {
					selectedRow = lsm.getMinSelectionIndex();
					infoToTextField(tabNum, selectedRow);
				}
			}
		});
		//[도서 검색] 내부패널[2] 레이아웃 설정 및 JTable 설정
		sbtjPanel[2].setLayout(new FlowLayout());
		//[도서 검색] JTabel 열 크기
		jtTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);// JTable 열 크기 조절
		//jtTable.setAutoCreateRowSorter(true);//JTabel 열에 맞춰 행 정렬
		jtTable.setRowHeight(20);
		jtTable.getColumn(colNameString[0]).setPreferredWidth((int) (50 * multplyCoef));
		jtTable.getColumn(colNameString[0]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[1]).setPreferredWidth((int) (150 * multplyCoef));
		jtTable.getColumn(colNameString[1]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[2]).setPreferredWidth((int) (400 * multplyCoef));
		jtTable.getColumn(colNameString[3]).setPreferredWidth((int) (200 * multplyCoef));
		jtTable.getColumn(colNameString[3]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[4]).setPreferredWidth((int) (100 * multplyCoef));
		jtTable.getColumn(colNameString[4]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[5]).setPreferredWidth((int) (100 * multplyCoef));
		jtTable.getColumn(colNameString[5]).setCellRenderer(celAlignCenter);
		//[도서 검색] 내부패널[2]에 JScrollPane 추가, 설정
		sbtjPanel[2].add(jsPane);
		jsPane.setPreferredSize(new Dimension(pWidth, pHeight*2/3));
		//[도서 검색] JTextField, JButton 최초상태 설정 
		for(int i=8;i<jtfInfo[tabNum].length;i++) {
			jtfInfo[tabNum][i].setEditable(false);
			jtfInfo[tabNum][i].setBackground(Color.LIGHT_GRAY);
		}
		tabjPanel[tabNum].updateUI();
	}
	public void searchUserTab(int tabNum) {		//회원 검색 탭
		//[회원 검색] 객체 생성, 변수 선언
		String colNameString[]= {noString,userInfoString[0],userInfoString[1],userInfoString[2],userInfoString[5],userInfoString[4],userInfoString[7]};
		dtmModel[tabNum].setColumnIdentifiers(colNameString);
		JTable jtTable = new JTable(dtmModel[tabNum]) {
			private static final long serialVersionUID = 3119174906746953392L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();		//JTabel 열 정렬
		celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
		JScrollPane jsPane=new JScrollPane(jtTable);
		JPanel sutjPanel[]=new JPanel[3];
		JLabel sutjLabel[]=new JLabel[10];
		JButton sutjButton[]=new JButton[3];
		int pWidth,pHeight,sutjpWidth[],sutjpHeight[];
		sutjpWidth=new int [3];
		sutjpHeight=new int [3];
		pWidth=tabjPanel[tabNum].getWidth();
		pHeight=tabjPanel[tabNum].getHeight();
		//[회원 검색] 내부패널 0~2 생성, 추가, 위치 설정
		tabjPanel[tabNum].setLayout(null);		
		for(int i=0;i<sutjPanel.length;i++) {
			sutjPanel[i]=new JPanel();
			tabjPanel[tabNum].add(sutjPanel[i]);
		}
		sutjPanel[0].setBounds(0, 0, (pWidth*5/6)-1, (pHeight/3)-1);
		sutjPanel[1].setBounds(pWidth*5/6, 0, (pWidth/6)-1, (pHeight/3)-1);
		sutjPanel[2].setBounds(0, pHeight/3, pWidth-1, (pHeight*2/3)-1);
		for(int i=0;i<sutjPanel.length;i++) {
			sutjpWidth[i]=sutjPanel[i].getWidth();
			sutjpHeight[i]=sutjPanel[i].getHeight();
		}
		//[회원 검색] 내부패널[0] 레이아웃 설정 및 내부패널[0]에 들어갈 JLabel,JTextField,JComboBox 생성,설정 
		sutjPanel[0].setLayout(null);
		for(int i=0;i<sutjLabel.length;i++) {							//JLabel 설정
			sutjLabel[i]=new JLabel(userInfoString[i]);
			sutjLabel[i].setVerticalAlignment(JLabel.CENTER);
			sutjLabel[i].setHorizontalAlignment(JLabel.CENTER);
			sutjLabel[i].setFont(ngB);
		}
		for(int i=0;i<jtfInfo[tabNum].length;i++) {					//JTextField 설정
			jtfInfo[tabNum][i]=new JTextField();
			jtfInfo[tabNum][i].setFont(ngP);
			jtfInfo[tabNum][i].setHorizontalAlignment(JLabel.CENTER);
		}
		//[회원 검색] 내부패널[0]에 생성한 객체 추가
		for(int i=0;i<sutjLabel.length;i++) {
			sutjPanel[0].add(sutjLabel[i]);
			sutjPanel[0].add(jtfInfo[tabNum][i]);
		}
		//[회원 검색] 내부패널[0] 내 위치 설정
		for(int i=0,j=0;i<4;j++) {
			sutjLabel[i++].setBounds(0, sutjpHeight[0]*(3*j+1)/16, sutjpWidth[0]/12,sutjpHeight[0]/8);
			sutjLabel[i++].setBounds(sutjpWidth[0]/2, sutjpHeight[0]*(3*j+1)/16, sutjpWidth[0]/12,sutjpHeight[0]/8);
		}
		sutjLabel[4].setBounds(0, sutjpHeight[0]*7/16, sutjpWidth[0]/12,sutjpHeight[0]/8);
		sutjLabel[5].setBounds(0, sutjpHeight[0]*5/8, sutjpWidth[0]/12,sutjpHeight[0]/8);
		sutjLabel[6].setBounds(sutjpWidth[0]/4, sutjpHeight[0]*5/8, sutjpWidth[0]/12,sutjpHeight[0]/8);
		sutjLabel[7].setBounds(sutjpWidth[0]/2, sutjpHeight[0]*5/8, sutjpWidth[0]/12,sutjpHeight[0]/8);
		sutjLabel[8].setBounds(sutjpWidth[0]*3/4, sutjpHeight[0]*5/8, sutjpWidth[0]/12,sutjpHeight[0]/8);
		sutjLabel[9].setBounds(0, sutjpHeight[0]*13/16, sutjpWidth[0]/12,sutjpHeight[0]/8);
		for(int i=0,j=0;i<4;j++) {
			jtfInfo[tabNum][i++].setBounds(sutjpWidth[0]/12+5, sutjpHeight[0]*(3*j+1)/16, sutjpWidth[0]*5/12-15, sutjpHeight[0]/8);
			jtfInfo[tabNum][i++].setBounds(sutjpWidth[0]*7/12+5, sutjpHeight[0]*(3*j+1)/16, sutjpWidth[0]*5/12-15, sutjpHeight[0]/8);
		}
		jtfInfo[tabNum][4].setBounds(sutjpWidth[0]/12+5, sutjpHeight[0]*7/16, sutjpWidth[0]*11/12-15, sutjpHeight[0]/8);
		jtfInfo[tabNum][5].setBounds(sutjpWidth[0]/12+5, sutjpHeight[0]*5/8, sutjpWidth[0]/6-15, sutjpHeight[0]/8);
		jtfInfo[tabNum][6].setBounds(sutjpWidth[0]/3+5, sutjpHeight[0]*5/8, sutjpWidth[0]/6-15, sutjpHeight[0]/8);
		jtfInfo[tabNum][7].setBounds(sutjpWidth[0]*7/12+5, sutjpHeight[0]*5/8, sutjpWidth[0]/6-15, sutjpHeight[0]/8);
		jtfInfo[tabNum][8].setBounds(sutjpWidth[0]*5/6+5, sutjpHeight[0]*5/8, sutjpWidth[0]/6-15, sutjpHeight[0]/8);
		jtfInfo[tabNum][9].setBounds(sutjpWidth[0]/12+5, sutjpHeight[0]*13/16, sutjpWidth[0]/6-15, sutjpHeight[0]/8);
		//[회원 검색] 내부패널[1] 레이아웃 설정 및 내부패널[1]에 들어갈 JButton 생성,설정 
		sutjPanel[1].setLayout(null);
		sutjButton[0]=new JButton(buttonNameString[3]);
		sutjButton[1]=new JButton(buttonNameString[0]);
		sutjButton[2]=new JButton(buttonNameString[4]);
		//[회원 검색] 내부패널[1]에 생성한 객체 추가
		for(int i=0;i<sutjButton.length;i++) {
			sutjButton[i].setFont(ngB);
			sutjPanel[1].add(sutjButton[i]);
		}
		//[회원 검색] 내부패널[1] 내 JButton Action 설정
		sutjButton[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(searchAction(tabNum,false)) {
					sutjButton[0].setEnabled(false);
					sutjButton[1].setEnabled(false);
				}
			}
		});
		sutjButton[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCondition(1,1);
				if(userInfoDialog(tabNum)) {
					if(searchAction(5,false)) {
						jtfInfo[2][10].setEditable(true);
						jtfInfo[2][10].setBackground(Color.WHITE);
						jtTable.setEnabled(false);
						sutjButton[1].setEnabled(false);
						jtpMainTab.setSelectedIndex(2);
					}
				}
			}
		});
		sutjButton[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<5;i++) {
					jtfInfo[tabNum][i].setEditable(true);
					jtfInfo[tabNum][i].setBackground(Color.WHITE);
				}
				jtfInfo[2][9].setEditable(false);
				jtfInfo[2][9].setBackground(Color.LIGHT_GRAY);
				sutjButton[0].setEnabled(true);
				sutjButton[1].setEnabled(false);
				jtTable.setEnabled(true);
				clearTextFieldAndTable(tabNum);
				clearTextFieldAndTable(2);
			}
		});
		//[회원 검색] 내부패널[1] 내 위치 설정
		sutjButton[0].setBounds(10, 10, sutjpWidth[1]-20, sutjpHeight[1]*2/5-15);
		sutjButton[1].setBounds(10, sutjpHeight[1]*2/5+5, sutjpWidth[1]-20, sutjpHeight[1]*2/5-15);
		sutjButton[2].setBounds(10, sutjpHeight[1]*4/5, sutjpWidth[1]-20, sutjpHeight[1]/5-10);
		//[회원 검색] 내부패널[2] 레이아웃 설정 및 JTable 설정
		sutjPanel[2].setLayout(new FlowLayout());
		//[회원 검색] JTabel 행 클릭시 Action 설정
		ListSelectionModel rowSel = jtTable.getSelectionModel();
		rowSel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowSel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (!e.getValueIsAdjusting() && -1 < lsm.getMinSelectionIndex()) {
					selectedRow = lsm.getMinSelectionIndex();
					infoToTextField(tabNum, selectedRow);
					infoToTextField(2,selectedRow);
					sutjButton[1].setEnabled(true);
				}
			}
		});
		//[회원 검색] JTabel 열 크기
		jtTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);// JTable 열 크기 조절
		jtTable.setRowHeight(20);
		jtTable.getColumn(colNameString[0]).setPreferredWidth((int) (50 * multplyCoef));
		jtTable.getColumn(colNameString[0]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[1]).setPreferredWidth((int) (100 * multplyCoef));
		jtTable.getColumn(colNameString[1]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[2]).setPreferredWidth((int) (100 * multplyCoef));
		jtTable.getColumn(colNameString[2]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[3]).setPreferredWidth((int) (150 * multplyCoef));
		jtTable.getColumn(colNameString[3]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[4]).setPreferredWidth((int) (50 * multplyCoef));
		jtTable.getColumn(colNameString[4]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[5]).setPreferredWidth((int) (500 * multplyCoef));
		jtTable.getColumn(colNameString[5]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[6]).setPreferredWidth((int) (50 * multplyCoef));
		jtTable.getColumn(colNameString[6]).setCellRenderer(celAlignCenter);
		//[회원 검색] 내부패널[2]에 JScrollPane 추가, 설정
		sutjPanel[2].add(jsPane);
		jsPane.setPreferredSize(new Dimension(pWidth, pHeight*2/3));
		//[회원 검색] JTextField, JButton 최초상태 설정 
		for(int i=5;i<jtfInfo[tabNum].length;i++) {
			jtfInfo[tabNum][i].setEditable(false);
			jtfInfo[tabNum][i].setBackground(Color.LIGHT_GRAY);
		}
		sutjButton[1].setEnabled(false);
		tabjPanel[tabNum].updateUI();
	}
	public void checkOutReturnTab(int tabNum) {	//대출/반납 탭
		//[대출/반납] 객체 생성, 변수 선언
		String colNameString[]= {noString,bookInfoString[7],bookInfoString[0],bookInfoString[13],bookInfoString[14],userInfoString[11],userInfoString[9]};
		String colNameString2nd[]= {noString,bookInfoString[7],bookInfoString[0],bookInfoString[8],bookInfoString[12],bookInfoString[13]};
		//[대출/반납] 1번째 테이블(가입자 책 대여정보)
		dtmModel[5].setColumnIdentifiers(colNameString);
		JTable jtTable = new JTable(dtmModel[5]) {
			private static final long serialVersionUID = -4646544488916313603L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();		//JTabel 열 정렬
		celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
		JScrollPane jsPane=new JScrollPane(jtTable);
		//[대출/반납] 2번째 테이블(대여/반납할 도서 검색 정보)
		dtmModel[tabNum].setColumnIdentifiers(colNameString2nd);
		JTable jtTable2nd = new JTable(dtmModel[tabNum]) {
			private static final long serialVersionUID = -5047071400863018253L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JScrollPane jsPane2nd = new JScrollPane(jtTable2nd);
		JPanel cortjPanel[]=new JPanel[4];
		JLabel cortjLabel[]=new JLabel[11];
		JButton cortjButton[]=new JButton[5];
		JComboBox<String> jcbBookID= new JComboBox<String>();
		DefaultListCellRenderer jcbAlignCenter=new DefaultListCellRenderer();
		jcbAlignCenter.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		int pWidth,pHeight,cortjpWidth[],cortjpHeight[];
		cortjpWidth=new int [4];
		cortjpHeight=new int [4];
		pWidth=tabjPanel[tabNum].getWidth();
		pHeight=tabjPanel[tabNum].getHeight();
		//[대출/반납] 내부패널 0~3 생성, 추가, 위치 설정
		tabjPanel[tabNum].setLayout(null);		
		for(int i=0;i<cortjPanel.length;i++) {
			cortjPanel[i]=new JPanel();
			tabjPanel[tabNum].add(cortjPanel[i]);
		}
		cortjPanel[0].setBounds(0, 0, (pWidth*5/6)-1, (pHeight/3)-1);
		cortjPanel[1].setBounds(pWidth*5/6, 0, (pWidth/6)-1, (pHeight/3)-1);
		cortjPanel[2].setBounds(0, pHeight/3, pWidth-1, (pHeight*1/3)-1);
		cortjPanel[3].setBounds(0, pHeight*2/3, pWidth-1, (pHeight*1/3)-1);
		for(int i=0;i<cortjPanel.length;i++) {
			cortjpWidth[i]=cortjPanel[i].getWidth();
			cortjpHeight[i]=cortjPanel[i].getHeight();
		}
		//[대출/반납] 내부패널[0] 레이아웃 설정 및 내부패널[0]에 들어갈 JLabel,JTextField,JComboBox 생성,설정 
		cortjPanel[0].setLayout(null);
		for(int i=0;i<cortjLabel.length;i++) {							//JLabel 설정
			cortjLabel[i]=new JLabel(userInfoString[i]);
			cortjLabel[i].setVerticalAlignment(JLabel.CENTER);
			cortjLabel[i].setHorizontalAlignment(JLabel.CENTER);
			cortjLabel[i].setFont(ngB);
		}
		jcbBookID.addItem("--------");									//JComboBox 설정
		jcbBookID.addItem(bookInfoString[7]);
		jcbBookID.addItem(bookInfoString[0]);
		jcbBookID.setFont(ngB);
		jcbBookID.setSelectedIndex(0);
		jcbBookID.setRenderer(jcbAlignCenter);
		for(int i=0;i<jtfInfo[tabNum].length;i++) {					//JTextField 설정
			jtfInfo[tabNum][i]=new JTextField();
			jtfInfo[tabNum][i].setFont(ngP);
			jtfInfo[tabNum][i].setHorizontalAlignment(JLabel.CENTER);
		}
		cortjButton[4]=new JButton(buttonNameString[9]);				//JBuuton 설정
		cortjButton[4].setFont(ngB);
		//[대출/반납] 내부패널[0]에 생성한 객체 추가
		for(int i=0;i<cortjLabel.length;i++) {
			cortjPanel[0].add(cortjLabel[i]);
			cortjPanel[0].add(jtfInfo[tabNum][i]);
		}
		cortjPanel[0].add(jcbBookID);
		cortjPanel[0].add(cortjButton[4]);
		cortjPanel[0].add(jtfTmp[0]);
		//[대출/반납] 내부패널[0] 내 JButton,JComboBox Action 설정
		jcbBookID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jcbBookID.getSelectedIndex()==1) {
					jtfTmp[0].setText("BOOK_ID");
				}
				else if (jcbBookID.getSelectedIndex()==2) {
					jtfTmp[0].setText("TITLE");
				}
			}
		});
		cortjButton[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(inputAction(tabNum,0)) {
					System.out.println(selectedInfo);
					jtfInfo[tabNum][9].setText(Integer.toString(Integer.parseInt(jtfInfo[tabNum][9].getText())-Integer.parseInt(selectedInfo.get(3))));
					if(searchAction(5,false)) {
						cortjButton[4].setEnabled(false);
						messageDialog(messageString[7],titleString[7]);
					}
				}
				jtfInfo[tabNum][10].setEditable(true);
				jtfInfo[tabNum][10].setBackground(Color.WHITE);
			}
		});
		//[대출/반납] 내부패널[0] 내 위치 설정
		cortjButton[4].setBounds(cortjpWidth[0]*7/24, cortjpHeight[0]*13/16, cortjpWidth[0]/6,cortjpHeight[0]/8);
		for(int i=0,j=0;i<4;j++) {
			cortjLabel[i++].setBounds(0, cortjpHeight[0]*(3*j+1)/16, cortjpWidth[0]/12,cortjpHeight[0]/8);
			cortjLabel[i++].setBounds(cortjpWidth[0]/2, cortjpHeight[0]*(3*j+1)/16, cortjpWidth[0]/12,cortjpHeight[0]/8);
		}
		cortjLabel[4].setBounds(0, cortjpHeight[0]*7/16, cortjpWidth[0]/12,cortjpHeight[0]/8);
		cortjLabel[5].setBounds(0, cortjpHeight[0]*5/8, cortjpWidth[0]/12,cortjpHeight[0]/8);
		cortjLabel[6].setBounds(cortjpWidth[0]/4, cortjpHeight[0]*5/8, cortjpWidth[0]/12,cortjpHeight[0]/8);
		cortjLabel[7].setBounds(cortjpWidth[0]/2, cortjpHeight[0]*5/8, cortjpWidth[0]/12,cortjpHeight[0]/8);
		cortjLabel[8].setBounds(cortjpWidth[0]*3/4, cortjpHeight[0]*5/8, cortjpWidth[0]/12,cortjpHeight[0]/8);
		cortjLabel[9].setBounds(0, cortjpHeight[0]*13/16, cortjpWidth[0]/12,cortjpHeight[0]/8);
		cortjLabel[10].setBounds(cortjpWidth[0]/2, cortjpHeight[0]*13/16, cortjpWidth[0]/12,cortjpHeight[0]/8);
		jcbBookID.setBounds(cortjpWidth[0]*7/12+5, cortjpHeight[0]*13/16, cortjpWidth[0]/8,cortjpHeight[0]/8);		
		for(int i=0,j=0;i<4;j++) {
			jtfInfo[tabNum][i++].setBounds(cortjpWidth[0]/12+5, cortjpHeight[0]*(3*j+1)/16, cortjpWidth[0]*5/12-15, cortjpHeight[0]/8);
			jtfInfo[tabNum][i++].setBounds(cortjpWidth[0]*7/12+5, cortjpHeight[0]*(3*j+1)/16, cortjpWidth[0]*5/12-15, cortjpHeight[0]/8);
		}
		jtfInfo[tabNum][4].setBounds(cortjpWidth[0]/12+5, cortjpHeight[0]*7/16, cortjpWidth[0]*11/12-15, cortjpHeight[0]/8);
		jtfInfo[tabNum][5].setBounds(cortjpWidth[0]/12+5, cortjpHeight[0]*5/8, cortjpWidth[0]/6-15, cortjpHeight[0]/8);
		jtfInfo[tabNum][6].setBounds(cortjpWidth[0]/3+5, cortjpHeight[0]*5/8, cortjpWidth[0]/6-15, cortjpHeight[0]/8);
		jtfInfo[tabNum][7].setBounds(cortjpWidth[0]*7/12+5, cortjpHeight[0]*5/8, cortjpWidth[0]/6-15, cortjpHeight[0]/8);
		jtfInfo[tabNum][8].setBounds(cortjpWidth[0]*5/6+5, cortjpHeight[0]*5/8, cortjpWidth[0]/6-15, cortjpHeight[0]/8);
		jtfInfo[tabNum][9].setBounds(cortjpWidth[0]/12+5, cortjpHeight[0]*13/16, cortjpWidth[0]/6-15, cortjpHeight[0]/8);
		jtfInfo[tabNum][10].setBounds(cortjpWidth[0]*17/24+10, cortjpHeight[0]*13/16, cortjpWidth[0]*7/24-20, cortjpHeight[0]/8);
		//[대출/반납] 내부패널[1] 레이아웃 설정 및 내부패널[1]에 들어갈 JButton 생성,설정 
		cortjPanel[1].setLayout(null);
		cortjButton[0]=new JButton(buttonNameString[3]);
		cortjButton[1]=new JButton(buttonNameString[7]);
		cortjButton[2]=new JButton(buttonNameString[8]);
		cortjButton[3]=new JButton(buttonNameString[4]);
		//[대출/반납] 내부패널[1]에 생성한 객체 추가
		for(int i=0;i<cortjButton.length-1;i++) {
			cortjButton[i].setFont(ngB);
			cortjPanel[1].add(cortjButton[i]);
		}
		//[대출/반납] 내부패널[1] 내 JButton Action 설정
		cortjButton[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jtfInfo[tabNum][0].getText().length()==0||jcbBookID.getSelectedIndex()==0) {}
				else if(searchAction(tabNum,false)) {
					cortjButton[0].setEnabled(false);
					jcbBookID.setEnabled(false);
				}
			}
		});
		cortjButton[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int maxCheckout=0;
				if (jtfInfo[tabNum][7].getText().equals("0")) {
					if(jtfInfo[tabNum][5].getText().equals(userInfoString[12])) {
						maxCheckout=20;
					}
					else if(jtfInfo[tabNum][5].getText().equals(userInfoString[12])) {
						maxCheckout=50;
					}
					errorDialog(errorString[3]+Integer.toString(maxCheckout));
				}
				else {
					if(inputAction(tabNum,3)) {
						if(searchAction(5,false)) {
							cortjButton[0].setEnabled(true);
							cortjButton[1].setEnabled(false);
							jcbBookID.setEnabled(true);
							jcbBookID.setSelectedIndex(0);
							clearTextFieldAndTable(tabNum);
						}
					}
					jtfInfo[tabNum][10].setEditable(true);
					jtfInfo[tabNum][10].setBackground(Color.WHITE);
				}
			}
		});
		cortjButton[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(inputAction(tabNum,4)) {
					if(searchAction(5,false)) {
						cortjButton[0].setEnabled(true);
						cortjButton[2].setEnabled(false);
						jcbBookID.setEnabled(true);
						jcbBookID.setSelectedIndex(0);
						clearTextFieldAndTable(tabNum);
					}
				}
				jtfInfo[tabNum][10].setEditable(true);
				jtfInfo[tabNum][10].setBackground(Color.WHITE);
			}
		});
		cortjButton[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jtfInfo[tabNum][10].setEditable(true);
				jtfInfo[tabNum][10].setBackground(Color.WHITE);
				cortjButton[0].setEnabled(true);
				cortjButton[1].setEnabled(false);
				cortjButton[2].setEnabled(false);
				cortjButton[4].setEnabled(false);
				jcbBookID.setEnabled(true);
				jcbBookID.setSelectedIndex(0);
				clearTextFieldAndTable(tabNum);
				tabjPanel[tabNum].updateUI();
			}
		});
		//[대출/반납] 내부패널[1] 내 위치 설정
		cortjButton[0].setBounds(10, 10, cortjpWidth[1]-20, cortjpHeight[1]*2/5-15);
		cortjButton[1].setBounds(10, cortjpHeight[1]*2/5+5, cortjpWidth[1]/2-15, cortjpHeight[1]*2/5-15);
		cortjButton[2].setBounds(cortjpWidth[1]/2+5, cortjpHeight[1]*2/5+5, cortjpWidth[1]/2-15, cortjpHeight[1]*2/5-15);
		cortjButton[3].setBounds(10, cortjpHeight[1]*4/5, cortjpWidth[1]-20, cortjpHeight[1]/5-10);
		//[대출/반납] 내부패널[2] 레이아웃 설정 및 JTable(가입자 책 대여정보) 설정
		cortjPanel[2].setLayout(new FlowLayout());
		//[대출/반납] 1번째 JTabel 행 클릭시 Action 설정
		ListSelectionModel rowSel=jtTable.getSelectionModel();//JTabel 행 클릭시 액션
		rowSel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowSel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm=(ListSelectionModel)e.getSource();
				if(!e.getValueIsAdjusting()&&-1<lsm.getMinSelectionIndex()) {
					selectedRow=lsm.getMinSelectionIndex();
					if (!searchUserBookList[5].get(selectedRow).equals("0")) {
						cortjButton[4].setEnabled(true);
					}
					else {
						cortjButton[4].setEnabled(false);
					}
				}
			}
		});
		//[대출/반납] 1번째 JTabel 열 크기
		jtTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);//JTable 열 크기 조절
		jtTable.setRowHeight(20);
		jtTable.getColumn(colNameString[0]).setPreferredWidth((int) (50*multplyCoef));
		jtTable.getColumn(colNameString[0]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[1]).setPreferredWidth((int) (150*multplyCoef));
		jtTable.getColumn(colNameString[1]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[2]).setPreferredWidth((int) (500*multplyCoef));
		jtTable.getColumn(colNameString[3]).setPreferredWidth((int) (75*multplyCoef));
		jtTable.getColumn(colNameString[3]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[4]).setPreferredWidth((int) (100*multplyCoef));
		jtTable.getColumn(colNameString[4]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[5]).setPreferredWidth((int) (100*multplyCoef));
		jtTable.getColumn(colNameString[5]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[6]).setPreferredWidth((int) (75*multplyCoef));
		jtTable.getColumn(colNameString[6]).setCellRenderer(celAlignCenter);
		//[대출/반납] 내부패널[2]에 1번째 JScrollPane 추가, 설정
		cortjPanel[2].add(jsPane);
		jsPane.setPreferredSize(new Dimension(pWidth, pHeight*1/3));
		//[대출/반납] 내부패널[3] 레이아웃 설정 및 JTable(대여/반납할 도서 검색 정보) 설정
		cortjPanel[3].setLayout(new FlowLayout());
		//[대출/반납] 2번째 JTabel 행 클릭시 Action 설정
		ListSelectionModel rowSel2nd = jtTable2nd.getSelectionModel();// JTabel 행 클릭시 액션
		rowSel2nd.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowSel2nd.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm=(ListSelectionModel)e.getSource();
				if(!e.getValueIsAdjusting()&&-1<lsm.getMinSelectionIndex()) {
					selectedRow=lsm.getMinSelectionIndex();
					if (searchBookInfo[12].get(selectedRow).toString().equals("1")) {
						cortjButton[1].setEnabled(true);
						cortjButton[2].setEnabled(false);
					}
					else if (searchBookInfo[12].get(selectedRow).toString().equals("2")) {
						cortjButton[1].setEnabled(false);
						cortjButton[2].setEnabled(true);
					}
					else {
						cortjButton[1].setEnabled(false);
						cortjButton[2].setEnabled(false);
						
					}
					cortjButton[4].setEnabled(false);
				}
			}
		});
		//[대출/반납] 2번째 JTabel 열 크기
		jtTable2nd.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);// JTable 열 크기 조절
		jtTable2nd.setAutoCreateRowSorter(true);// JTabel 정렬
		jtTable2nd.setRowHeight(20);
		jtTable2nd.getColumn(colNameString2nd[0]).setPreferredWidth((int) (50*multplyCoef));
		jtTable2nd.getColumn(colNameString2nd[0]).setCellRenderer(celAlignCenter);
		jtTable2nd.getColumn(colNameString2nd[1]).setPreferredWidth((int) (150*multplyCoef));
		jtTable2nd.getColumn(colNameString2nd[1]).setCellRenderer(celAlignCenter);
		jtTable2nd.getColumn(colNameString2nd[2]).setPreferredWidth((int) (400*multplyCoef));
		jtTable2nd.getColumn(colNameString2nd[3]).setPreferredWidth((int) (200*multplyCoef));
		jtTable2nd.getColumn(colNameString2nd[3]).setCellRenderer(celAlignCenter);
		jtTable2nd.getColumn(colNameString2nd[4]).setPreferredWidth((int) (100*multplyCoef));
		jtTable2nd.getColumn(colNameString2nd[4]).setCellRenderer(celAlignCenter);
		jtTable2nd.getColumn(colNameString2nd[5]).setPreferredWidth((int) (100*multplyCoef));
		jtTable2nd.getColumn(colNameString2nd[5]).setCellRenderer(celAlignCenter);
		//[대출/반납] 내부패널[3]에 2번째 JScrollPane 추가, 설정
		cortjPanel[3].add(jsPane2nd);
		jsPane2nd.setPreferredSize(new Dimension(pWidth, pHeight*1/3));
		//[대출/반납] JTextField, JButton 최초상태 설정 
		for(int i=0;i<jtfInfo[tabNum].length-1;i++) {
			jtfInfo[tabNum][i].setEditable(false);
			jtfInfo[tabNum][i].setBackground(Color.LIGHT_GRAY);
		}
		cortjButton[1].setEnabled(false);
		cortjButton[2].setEnabled(false);
		cortjButton[4].setEnabled(false);
		tabjPanel[tabNum].updateUI();
	}
	public void addmissionUserTab(int tabNum) {	//회원 가입 탭
		//[회원 가입] 객체 생성, 변수 선언
		String colNameString[]= {noString,userInfoString[0],userInfoString[1],userInfoString[2],userInfoString[5],userInfoString[4],userInfoString[3]};
		dtmModel[tabNum].setColumnIdentifiers(colNameString);
		JTable jtTable = new JTable(dtmModel[tabNum]) {
			private static final long serialVersionUID = 3256480772096899405L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();		//JTabel 열 정렬
		celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
		JScrollPane jsPane=new JScrollPane(jtTable);
		JPanel autjPanel[]=new JPanel[3];
		JLabel autjLabel[]=new JLabel[9];
		JButton autjButton[]=new JButton[5];
		JComboBox<String> jcbUserClass= new JComboBox<String>();
		JComboBox<String> jcbUserStatus= new JComboBox<String>();
		JComboBox<String> jcbIsAddmission= new JComboBox<String>();
		DefaultListCellRenderer jcbAlignCenter=new DefaultListCellRenderer();
		jcbAlignCenter.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		int pWidth,pHeight,autjpWidth[],autjpHeight[];
		autjpWidth=new int [3];
		autjpHeight=new int [3];
		pWidth=tabjPanel[tabNum].getWidth();
		pHeight=tabjPanel[tabNum].getHeight();
		//[회원 가입] 내부패널 0~2 생성, 추가, 위치 설정
		tabjPanel[tabNum].setLayout(null);		
		for(int i=0;i<autjPanel.length;i++) {
			autjPanel[i]=new JPanel();
			tabjPanel[tabNum].add(autjPanel[i]);
		}
		autjPanel[0].setBounds(0, 0, (pWidth*5/6)-1, (pHeight/3)-1);
		autjPanel[1].setBounds(pWidth*5/6, 0, (pWidth/6)-1, (pHeight/3)-1);
		autjPanel[2].setBounds(0, pHeight/3, pWidth-1, (pHeight*2/3)-1);
		for(int i=0;i<autjPanel.length;i++) {
			autjpWidth[i]=autjPanel[i].getWidth();
			autjpHeight[i]=autjPanel[i].getHeight();
		}
		//[회원 가입] 내부패널[0] 레이아웃 설정 및 내부패널[0]에 들어갈 JLabel,JTextField,JComboBox 생성,설정 
		autjPanel[0].setLayout(null);
		for(int i=0;i<autjLabel.length;i++) {							//JLabel 설정
			autjLabel[i]=new JLabel(userInfoString[i]);
			autjLabel[i].setVerticalAlignment(JLabel.CENTER);
			autjLabel[i].setHorizontalAlignment(JLabel.CENTER);
			autjLabel[i].setFont(ngB);
		}
		jcbUserClass.addItem("--------");								//JComboBox 설정
		jcbUserClass.addItem(userInfoString[12]);
		jcbUserClass.addItem(userInfoString[13]);
		jcbUserClass.setSelectedIndex(0);
		jcbUserClass.setRenderer(jcbAlignCenter);
		jcbUserClass.setFont(ngB);
		jcbUserStatus.addItem("--------");
		jcbUserStatus.addItem(userInfoString[14]);
		jcbUserStatus.addItem(userInfoString[15]);
		jcbUserStatus.addItem(userInfoString[16]);
		jcbUserStatus.setSelectedIndex(0);
		jcbUserStatus.setRenderer(jcbAlignCenter);
		jcbUserStatus.setFont(ngB);
		for(int i=0;i<jtfInfo[tabNum].length;i++) {					//JTextField 설정
			jtfInfo[tabNum][i]=new JTextField();
			jtfInfo[tabNum][i].setFont(ngP);
			jtfInfo[tabNum][i].setHorizontalAlignment(JLabel.CENTER);
		}
		//[회원 가입] 내부패널[0] 내 JComboBox Action 설정
		jcbUserClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jtfInfo[tabNum][5].setText(Integer.toString(jcbUserClass.getSelectedIndex()));
				if (jcbIsAddmission.getSelectedIndex()==2) {
					if (jcbUserClass.getSelectedIndex()==1) {
						jtfInfo[tabNum][7].setText("20");
					}
					else if (jcbUserClass.getSelectedIndex()==2) {
						jtfInfo[tabNum][7].setText("50");
					}
				}
				else if (jcbIsAddmission.getSelectedIndex()==1) {
					if (jcbUserClass.getSelectedIndex()==1) {
						jtfInfo[tabNum][7].setText(Integer.toString(20-Integer.parseInt(jtfInfo[tabNum][8].getText())));
					}
					else if (jcbUserClass.getSelectedIndex()==2) {
						jtfInfo[tabNum][7].setText(Integer.toString(50-Integer.parseInt(jtfInfo[tabNum][8].getText())));
					}
				}
			}
		});
		jcbUserStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jtfInfo[tabNum][6].setText(Integer.toString(jcbUserStatus.getSelectedIndex()));
				if (jcbUserStatus.getSelectedIndex()==3) {
					jtfInfo[tabNum][7].setText("0");
				}
			}
		});
		//[회원 가입] 내부패널[0]에 생성한 객체 추가
		for(int i=0;i<autjLabel.length;i++) {
			autjPanel[0].add(autjLabel[i]);
			autjPanel[0].add(jtfInfo[tabNum][i]);
		}
		autjPanel[0].add(jcbUserClass);
		autjPanel[0].add(jcbUserStatus);
		//[회원 가입] 내부패널[0] 내 위치 설정
		for(int i=0,j=0;i<4;j++) {
			autjLabel[i++].setBounds(0, autjpHeight[0]*(3*j+1)/16, autjpWidth[0]/12,autjpHeight[0]/8);
			autjLabel[i++].setBounds(autjpWidth[0]/2, autjpHeight[0]*(3*j+1)/16, autjpWidth[0]/12,autjpHeight[0]/8);
		}
		autjLabel[4].setBounds(0, autjpHeight[0]*7/16, autjpWidth[0]/12,autjpHeight[0]/8);
		autjLabel[5].setBounds(0, autjpHeight[0]*5/8, autjpWidth[0]/12,autjpHeight[0]/8);
		autjLabel[6].setBounds(autjpWidth[0]/4, autjpHeight[0]*5/8, autjpWidth[0]/12,autjpHeight[0]/8);
		autjLabel[7].setBounds(autjpWidth[0]/2, autjpHeight[0]*5/8, autjpWidth[0]/12,autjpHeight[0]/8);
		autjLabel[8].setBounds(autjpWidth[0]*3/4, autjpHeight[0]*5/8, autjpWidth[0]/12,autjpHeight[0]/8);
		for(int i=0,j=0;i<4;j++) {
			jtfInfo[tabNum][i++].setBounds(autjpWidth[0]/12+5, autjpHeight[0]*(3*j+1)/16, autjpWidth[0]*5/12-15, autjpHeight[0]/8);
			jtfInfo[tabNum][i++].setBounds(autjpWidth[0]*7/12+5, autjpHeight[0]*(3*j+1)/16, autjpWidth[0]*5/12-15, autjpHeight[0]/8);
		}
		jtfInfo[tabNum][4].setBounds(autjpWidth[0]/12+5, autjpHeight[0]*7/16, autjpWidth[0]*11/12-15, autjpHeight[0]/8);
		jtfInfo[tabNum][7].setBounds(autjpWidth[0]*7/12+5, autjpHeight[0]*5/8, autjpWidth[0]/6-15, autjpHeight[0]/8);
		jtfInfo[tabNum][8].setBounds(autjpWidth[0]*5/6+5, autjpHeight[0]*5/8, autjpWidth[0]/6-15, autjpHeight[0]/8);
		jcbUserClass.setBounds(autjpWidth[0]/12+5, autjpHeight[0]*5/8, autjpWidth[0]/6-15, autjpHeight[0]/8);
		jcbUserStatus.setBounds(autjpWidth[0]/3+5, autjpHeight[0]*5/8, autjpWidth[0]/6-15, autjpHeight[0]/8);
		//[회원 가입] 내부패널[1] 레이아웃 설정 및 내부패널[1]에 들어갈 JButton,JComboBox 생성,설정 
		autjPanel[1].setLayout(null);
		autjButton[0]=new JButton(buttonNameString[3]);
		autjButton[1]=new JButton(buttonNameString[0]);
		autjButton[2]=new JButton(buttonNameString[10]);
		autjButton[3]=new JButton(buttonNameString[11]);
		autjButton[4]=new JButton(buttonNameString[4]);
		jcbIsAddmission.addItem("--------");								//JComboBox 설정
		jcbIsAddmission.addItem(userInfoString[17]);
		jcbIsAddmission.addItem(userInfoString[18]);
		jcbIsAddmission.setSelectedIndex(0);
		jcbIsAddmission.setRenderer(jcbAlignCenter);
		jcbIsAddmission.setFont(ngB);
		//[회원 가입] 내부패널[1]에 생성한 객체 추가
		for(int i=0;i<autjButton.length;i++) {
			autjButton[i].setFont(ngB);
			autjPanel[1].add(autjButton[i]);
		}
		autjPanel[1].add(jcbIsAddmission);
		//[회원 가입] 내부패널[1] 내 JButton Action 설정
		jcbIsAddmission.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearTextFieldAndTable(tabNum);
				jcbUserClass.setSelectedIndex(0);
				jcbUserStatus.setEnabled(false);
				autjButton[1].setEnabled(false);
				autjButton[3].setEnabled(false);
				if(jcbIsAddmission.getSelectedIndex()==0) {
					for(int i=0;i<autjLabel.length;i++) {
						jtfInfo[tabNum][i].setEditable(false);
						jtfInfo[tabNum][i].setBackground(Color.LIGHT_GRAY);
					}
					jcbUserClass.setEnabled(false);
					jcbUserStatus.setSelectedIndex(0);
					autjButton[0].setEnabled(false);
					autjButton[2].setEnabled(false);
				}
				else if(jcbIsAddmission.getSelectedIndex()==1) {
					for(int i=0;i<autjLabel.length-4;i++) {
						jtfInfo[tabNum][i].setEditable(true);
						jtfInfo[tabNum][i].setBackground(Color.WHITE);
					}
					jcbUserClass.setEnabled(false);
					jcbUserStatus.setSelectedIndex(0);
					autjButton[0].setEnabled(true);
					autjButton[2].setEnabled(false);
					jtTable.setEnabled(true);
				}
				else if(jcbIsAddmission.getSelectedIndex()==2) {
					getDateFromToday(0);
					clearTextFieldAndTable(tabNum);
					for(int i=0;i<autjLabel.length-4;i++) {
						jtfInfo[tabNum][i].setEditable(true);
						jtfInfo[tabNum][i].setBackground(Color.WHITE);
					}
					autjButton[0].setEnabled(false);
					autjButton[2].setEnabled(true);
					jcbUserClass.setEnabled(true);
					jcbUserStatus.setSelectedIndex(1);
					jtfInfo[tabNum][3].setText(neededDate.toString());
					jtfInfo[tabNum][8].setText("0");
				}
			}
		});
		autjButton[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(searchAction(tabNum,false)) {
					autjButton[0].setEnabled(false);
					jcbIsAddmission.setEnabled(false);
				}
			}
		});
		autjButton[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<autjLabel.length;i++) {
					jtfInfo[tabNum][i].setEditable(true);
					jtfInfo[tabNum][i].setBackground(Color.WHITE);
				}
				jtfInfo[tabNum][0].setEditable(false);
				jtfInfo[tabNum][0].setBackground(Color.LIGHT_GRAY);
				autjButton[1].setEnabled(false);
				autjButton[3].setEnabled(true);
				jcbUserClass.setEnabled(true);
				jcbUserStatus.setEnabled(true);
				jtTable.setEnabled(false);
			}
		});
		autjButton[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(inputAction(tabNum,5)) {
					if (searchAction(tabNum, false)) {
						autjButton[2].setEnabled(false);
						jcbUserClass.setEnabled(false);
						jcbUserStatus.setEnabled(false);
						jtTable.setEnabled(true);
					}
				}
				else{
					for(int i=0;i<autjLabel.length-4;i++) {
						jtfInfo[tabNum][i].setEditable(true);
						jtfInfo[tabNum][i].setBackground(Color.WHITE);
					}
				}
			}
		});
		autjButton[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(inputAction(tabNum,6)) {
					if (searchAction(tabNum, false)) {
						autjButton[3].setEnabled(false);
						jcbUserClass.setEnabled(false);
						jcbUserStatus.setEnabled(false);
						jtTable.setEnabled(true);
					}
				}
				else{
					for(int i=1;i<autjLabel.length;i++) {
						jtfInfo[tabNum][i].setEditable(true);
						jtfInfo[tabNum][i].setBackground(Color.WHITE);
					}
				}
			}
		});
		autjButton[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				autjButton[0].setEnabled(false);
				autjButton[1].setEnabled(false);
				autjButton[2].setEnabled(false);
				autjButton[3].setEnabled(false);
				jcbUserClass.setSelectedIndex(0);
				jcbUserClass.setEnabled(false);
				jcbUserStatus.setSelectedIndex(0);
				jcbUserStatus.setEnabled(false);
				jcbIsAddmission.setSelectedIndex(0);
				jcbIsAddmission.setEnabled(true);
				jtTable.setEnabled(true);
				clearTextFieldAndTable(tabNum);
			}
		});
		//[회원 가입] 내부패널[1] 내 위치 설정
		jcbIsAddmission.setBounds(10, 10, autjpWidth[1]-20, autjpHeight[1]/5-15);
		autjButton[0].setBounds(10, autjpHeight[1]/5+5, autjpWidth[1]/2-15, autjpHeight[1]*3/10-15);
		autjButton[1].setBounds(10, autjpHeight[1]/2+5, autjpWidth[1]/2-15, autjpHeight[1]*3/10-15);
		autjButton[2].setBounds(autjpWidth[1]/2+5, autjpHeight[1]/5+5, autjpWidth[1]/2-15, autjpHeight[1]*3/10-15);
		autjButton[3].setBounds(autjpWidth[1]/2+5, autjpHeight[1]/2+5, autjpWidth[1]/2-15, autjpHeight[1]*3/10-15);
		autjButton[4].setBounds(10, autjpHeight[1]*4/5, autjpWidth[1]-20, autjpHeight[1]/5-10);
		//[회원 가입] 내부패널[2] 레이아웃 설정 및 JTable 설정
		autjPanel[2].setLayout(new FlowLayout());
		//[회원 가입] JTabel 행 클릭시 Action 설정
		ListSelectionModel rowSel=jtTable.getSelectionModel();//JTabel 행 클릭시 액션
		rowSel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowSel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm=(ListSelectionModel)e.getSource();
				if(!e.getValueIsAdjusting()&&-1<lsm.getMinSelectionIndex()) {
					selectedRow=lsm.getMinSelectionIndex();
					infoToTextField(tabNum,selectedRow);
					jcbUserClass.setSelectedIndex(Integer.parseInt(searchUserInfo[5].get(selectedRow)));
					jcbUserStatus.setSelectedIndex(Integer.parseInt(searchUserInfo[6].get(selectedRow)));
					if (jcbIsAddmission.getSelectedIndex()==1) {
						autjButton[1].setEnabled(true);
					}
				}
			}
		});
		//[회원 가입] JTabel 열 크기
		jtTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);//JTable 열 크기 조절
		jtTable.setRowHeight(20);
		jtTable.getColumn(colNameString[0]).setPreferredWidth((int) (50*multplyCoef));
		jtTable.getColumn(colNameString[0]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[1]).setPreferredWidth((int) (100*multplyCoef));
		jtTable.getColumn(colNameString[1]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[2]).setPreferredWidth((int) (100*multplyCoef));
		jtTable.getColumn(colNameString[2]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[3]).setPreferredWidth((int) (150*multplyCoef));
		jtTable.getColumn(colNameString[3]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[4]).setPreferredWidth((int) (50*multplyCoef));
		jtTable.getColumn(colNameString[4]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[5]).setPreferredWidth((int) (450*multplyCoef));
		jtTable.getColumn(colNameString[5]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[6]).setPreferredWidth((int) (100*multplyCoef));
		jtTable.getColumn(colNameString[6]).setCellRenderer(celAlignCenter);
		//[회원 가입] 내부패널[2]에 JScrollPane 추가, 설정
		autjPanel[2].add(jsPane);
		jsPane.setPreferredSize(new Dimension(pWidth, pHeight*2/3));
		//[회원 가입] JTextField, JButton 최초상태 설정 
		autjButton[0].setEnabled(false);
		autjButton[1].setEnabled(false);
		autjButton[2].setEnabled(false);
		autjButton[3].setEnabled(false);
		jcbUserClass.setEnabled(false);
		jcbUserStatus.setEnabled(false);
		for(int i=0;i<autjLabel.length;i++) {
			jtfInfo[tabNum][i].setEditable(false);
			jtfInfo[tabNum][i].setBackground(Color.LIGHT_GRAY);
		}
		tabjPanel[tabNum].updateUI();
	}
	public void importBookTab(int tabNum) {		//도서 반입 탭
		//[도서 반입] 객체 생성, 변수 선언
		String colNameString[]= {noString,bookInfoString[7],bookInfoString[0],bookInfoString[8],bookInfoString[9],bookInfoString[10]};
		dtmModel[tabNum].setColumnIdentifiers(colNameString);
		JTable jtTable = new JTable(dtmModel[tabNum]) {
			private static final long serialVersionUID = -120410813886196534L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();		//JTabel 열 정렬
		celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
		JScrollPane jsPane=new JScrollPane(jtTable);
		JPanel ibtjPanel[]=new JPanel[3];
		JLabel ibtjLabel[]=new JLabel[11];
		JButton ibtjButton[]=new JButton[5];
		JComboBox<String> jcbBookStatus= new JComboBox<String>();
		JComboBox<String> jcbSearchingClass= new JComboBox<String>();
		JComboBox<String> jcbAPIClass= new JComboBox<String>();
		DefaultListCellRenderer jcbAlignCenter=new DefaultListCellRenderer();
		jcbAlignCenter.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		int pWidth,pHeight,ibtjpWidth[],ibtjpHeight[];
		ibtjpWidth=new int [3];
		ibtjpHeight=new int [3];
		pWidth=tabjPanel[tabNum].getWidth();
		pHeight=tabjPanel[tabNum].getHeight();
		//[도서 반입] 내부패널 0~2 생성, 추가, 위치 설정
		tabjPanel[tabNum].setLayout(null);
		for(int i=0;i<ibtjPanel.length;i++) {
			ibtjPanel[i]=new JPanel();
			tabjPanel[tabNum].add(ibtjPanel[i]);
		}
		ibtjPanel[0].setBounds(0, 0, (pWidth*5/6)-1, (pHeight/3)-1);
		ibtjPanel[1].setBounds(pWidth*5/6, 0, (pWidth/6)-1, (pHeight/3)-1);
		ibtjPanel[2].setBounds(0, pHeight/3, pWidth-1, (pHeight*2/3)-1);
		for(int i=0;i<ibtjPanel.length;i++) {
			ibtjpWidth[i]=ibtjPanel[i].getWidth();
			ibtjpHeight[i]=ibtjPanel[i].getHeight();
		}
		//[도서 반입] 내부패널[0] 레이아웃 설정 및 내부패널[0]에 들어갈 JLabel,JTextField,JComboBox 생성,설정 
		ibtjPanel[0].setLayout(null);
		for(int i=0;i<ibtjLabel.length;i++) {							//JLabel 설정
			ibtjLabel[i]=new JLabel();
			ibtjLabel[i].setVerticalAlignment(JLabel.CENTER);
			ibtjLabel[i].setHorizontalAlignment(JLabel.CENTER);
			ibtjLabel[i].setFont(ngB);
		}
		ibtjLabel[0].setText(bookInfoString[0]);
		ibtjLabel[1].setText(bookInfoString[1]);
		ibtjLabel[2].setText(bookInfoString[2]);
		ibtjLabel[3].setText(bookInfoString[3]);
		ibtjLabel[4].setText(bookInfoString[4]);
		ibtjLabel[5].setText(bookInfoString[5]);
		ibtjLabel[6].setText(bookInfoString[7]);
		ibtjLabel[7].setText(bookInfoString[8]);
		ibtjLabel[8].setText(bookInfoString[9]);
		ibtjLabel[9].setText(bookInfoString[10]);
		ibtjLabel[10].setText(bookInfoString[12]);
		jcbBookStatus.addItem("--------");
		jcbBookStatus.addItem(bookInfoString[15]);					//JComboBox 설정
		jcbBookStatus.addItem(bookInfoString[16]);
		jcbBookStatus.addItem(bookInfoString[17]);
		jcbBookStatus.setSelectedIndex(0);
		jcbBookStatus.setRenderer(jcbAlignCenter);
		jcbBookStatus.setFont(ngB);
		for(int i=0;i<jtfInfo[tabNum].length;i++) {					//JTextField 설정
			jtfInfo[tabNum][i]=new JTextField();
			jtfInfo[tabNum][i].setFont(ngP);
			jtfInfo[tabNum][i].setHorizontalAlignment(JLabel.CENTER);
		}
		//[도서 반입] 내부패널[0]에 생성한 객체 추가
		for(int i=0;i<ibtjLabel.length;i++) {
			ibtjPanel[0].add(ibtjLabel[i]);
			ibtjPanel[0].add(jtfInfo[tabNum][i]);
		}
		ibtjPanel[0].add(jlthumbnail[tabNum]);
		ibtjPanel[0].add(jcbBookStatus);
		ibtjPanel[0].add(jtfTmp[1]);
		//[도서 반입] 내부패널[0] 내 JComboBox Action 설정
		jcbBookStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jtfTmp[1].setText(Integer.toString(jcbBookStatus.getSelectedIndex()));
				if (jcbBookStatus.getSelectedIndex()==3) {
					getDateFromToday(0);
					jtfInfo[tabNum][10].setText(neededDate.toString());
				}
			}
		});
		//[도서 반입] 내부패널[0] 내 위치 설정
		ibtjLabel[0].setBounds(ibtjpWidth[0]/6, ibtjpHeight[0]/16, ibtjpWidth[0]/12,ibtjpHeight[0]/8);
		jtfInfo[tabNum][0].setBounds(ibtjpWidth[0]/4+5, ibtjpHeight[0]/16, ibtjpWidth[0]*3/4-15, ibtjpHeight[0]/8);
		for(int i=1,j=1;i<ibtjLabel.length-2;j++) {
			ibtjLabel[i++].setBounds(ibtjpWidth[0]/6, ibtjpHeight[0]*(3*j+1)/16, ibtjpWidth[0]/12,ibtjpHeight[0]/8);
			ibtjLabel[i++].setBounds(ibtjpWidth[0]*7/12, ibtjpHeight[0]*(3*j+1)/16, ibtjpWidth[0]/12,ibtjpHeight[0]/8);
		}
		ibtjLabel[9].setBounds(ibtjpWidth[0]*19/24, ibtjpHeight[0]*13/16, ibtjpWidth[0]/12,ibtjpHeight[0]/8);
		ibtjLabel[10].setBounds(ibtjpWidth[0]*19/24, ibtjpHeight[0]*7/16, ibtjpWidth[0]/12,ibtjpHeight[0]/8);
		jlthumbnail[tabNum].setBounds(0,0,ibtjpWidth[0]/6,(pHeight/3)-2);
		for(int i=1,j=1;i<5;j++) {
			jtfInfo[tabNum][i++].setBounds(ibtjpWidth[0]/4+5, ibtjpHeight[0]*(3*j+1)/16, ibtjpWidth[0]/3-15, ibtjpHeight[0]/8);
			if (i==4) { break; }
			jtfInfo[tabNum][i++].setBounds(ibtjpWidth[0]*2/3+5, ibtjpHeight[0]*(3*j+1)/16, ibtjpWidth[0]/3-15, ibtjpHeight[0]/8);
		}
		jtfInfo[tabNum][4].setBounds(ibtjpWidth[0]*2/3+5, ibtjpHeight[0]*7/16, ibtjpWidth[0]/8-15, ibtjpHeight[0]/8);
		jtfInfo[tabNum][5].setBounds(ibtjpWidth[0]/4+5, ibtjpHeight[0]*5/8, ibtjpWidth[0]*7/48-15, ibtjpHeight[0]/8);
		jtfInfo[tabNum][6].setBounds(ibtjpWidth[0]*19/48+5, ibtjpHeight[0]*5/8, ibtjpWidth[0]*9/48-15, ibtjpHeight[0]/8);
		jtfInfo[tabNum][7].setBounds(ibtjpWidth[0]*2/3+5, ibtjpHeight[0]*5/8, ibtjpWidth[0]/3-15, ibtjpHeight[0]/8);
		jtfInfo[tabNum][8].setBounds(ibtjpWidth[0]/4+5, ibtjpHeight[0]*13/16, ibtjpWidth[0]/3-15, ibtjpHeight[0]/8);
		jtfInfo[tabNum][9].setBounds(ibtjpWidth[0]*2/3+5, ibtjpHeight[0]*13/16, ibtjpWidth[0]/8-15, ibtjpHeight[0]/8);
		jtfInfo[tabNum][10].setBounds(ibtjpWidth[0]*7/8+5, ibtjpHeight[0]*13/16, ibtjpWidth[0]/8-15, ibtjpHeight[0]/8);
		jcbBookStatus.setBounds(ibtjpWidth[0]*7/8+5, ibtjpHeight[0]*7/16, ibtjpWidth[0]/8-15, ibtjpHeight[0]/8);
		//[도서 반입] 내부패널[1] 레이아웃 설정 및 내부패널[1]에 들어갈 JButton,JComboBox 생성,설정
		ibtjPanel[1].setLayout(null);
		ibtjButton[0]=new JButton(buttonNameString[3]);
		ibtjButton[1]=new JButton(buttonNameString[0]);
		ibtjButton[2]=new JButton(buttonNameString[5]);
		ibtjButton[3]=new JButton(buttonNameString[11]);
		ibtjButton[4]=new JButton(buttonNameString[4]);
		jcbSearchingClass.addItem("--------");
		jcbSearchingClass.addItem(buttonNameString[12]);
		jcbSearchingClass.addItem(buttonNameString[13]);
		jcbSearchingClass.setSelectedIndex(0);
		jcbSearchingClass.setRenderer(jcbAlignCenter);
		jcbSearchingClass.setFont(ngB);
		jcbAPIClass.addItem("--------");
		jcbAPIClass.addItem(bookInfoString[0]);
		jcbAPIClass.addItem(bookInfoString[1]);
		jcbAPIClass.addItem(bookInfoString[3]);
		jcbAPIClass.addItem(bookInfoString[5]+"10");
		jcbAPIClass.addItem(bookInfoString[5]+"13");
		jcbAPIClass.setSelectedIndex(0);
		jcbAPIClass.setRenderer(jcbAlignCenter);
		jcbAPIClass.setFont(ngB);
		//[도서 반입] 내부패널[1]에 생성한 객체 추가
		for(int i=0;i<ibtjButton.length;i++) {
			ibtjButton[i].setFont(ngB);
			ibtjPanel[1].add(ibtjButton[i]);
		}
		ibtjPanel[1].add(jcbSearchingClass);
		ibtjPanel[1].add(jcbAPIClass);
		//[도서 반입] 내부패널[1] 내 JButton,JComboBox Action 설정
		jcbSearchingClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearTextFieldAndTable(tabNum);
				jtTable.setEnabled(true);
				jcbAPIClass.setSelectedIndex(0);
				if(jcbSearchingClass.getSelectedIndex()==0) {
					ibtjButton[0].setEnabled(false);
					jcbBookStatus.setSelectedIndex(0);
					jcbBookStatus.setEnabled(false);
					jcbAPIClass.setEnabled(false);
					for (int i = 0; i <jtfInfo[tabNum].length; i++) {
						jtfInfo[tabNum][i].setEditable(false);
						jtfInfo[tabNum][i].setBackground(Color.LIGHT_GRAY);
					}
				}
				else if(jcbSearchingClass.getSelectedIndex()==1) {
					ibtjButton[0].setEnabled(true);
					ibtjButton[2].setEnabled(false);
					ibtjButton[3].setEnabled(false);
					jcbAPIClass.setEnabled(false);
					for (int i = 0; i <jtfInfo[tabNum].length; i++) {
						jtfInfo[tabNum][i].setEditable(true);
						jtfInfo[tabNum][i].setBackground(Color.WHITE);
					}
				}else if(jcbSearchingClass.getSelectedIndex()==2) {
					ibtjButton[0].setEnabled(false);
					ibtjButton[2].setEnabled(false);
					ibtjButton[3].setEnabled(false);
					jcbBookStatus.setSelectedIndex(0);
					jcbBookStatus.setEnabled(false);
					jcbAPIClass.setSelectedIndex(0);
					jcbAPIClass.setEnabled(true);
				}
			}
		});
		jcbAPIClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearTextFieldAndTable(tabNum);
				for(int i=0;i<jtfInfo[tabNum].length;i++) {
					jtfInfo[tabNum][i].setEditable(false);
					jtfInfo[tabNum][i].setBackground(Color.LIGHT_GRAY);
				}
				if (jcbAPIClass.getSelectedIndex()==5) {
					jtfInfo[tabNum][6].setEditable(true);
					jtfInfo[tabNum][6].setBackground(Color.WHITE);
					ibtjButton[0].setEnabled(true);
				}
				else if (jcbAPIClass.getSelectedIndex()==4) {
					jtfInfo[tabNum][5].setEditable(true);
					jtfInfo[tabNum][5].setBackground(Color.WHITE);
					ibtjButton[0].setEnabled(true);
				}
				else if (jcbAPIClass.getSelectedIndex()==3) {
					jtfInfo[tabNum][3].setEditable(true);
					jtfInfo[tabNum][3].setBackground(Color.WHITE);
					ibtjButton[0].setEnabled(true);
				}
				else if (jcbAPIClass.getSelectedIndex()==2) {
					jtfInfo[tabNum][1].setEditable(true);
					jtfInfo[tabNum][1].setBackground(Color.WHITE);
					ibtjButton[0].setEnabled(true);
				}
				else if (jcbAPIClass.getSelectedIndex()==1) {
					jtfInfo[tabNum][0].setEditable(true);
					jtfInfo[tabNum][0].setBackground(Color.WHITE);
					ibtjButton[0].setEnabled(true);
				}
			}
		});
		ibtjButton[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jcbSearchingClass.getSelectedIndex() == 1 && searchAction(tabNum,false)) {
					ibtjButton[0].setEnabled(false);
					ibtjButton[1].setEnabled(false);
					ibtjButton[2].setEnabled(false);
					ibtjButton[3].setEnabled(false);
					jcbBookStatus.setEnabled(false);
					jcbSearchingClass.setEnabled(false);
					jcbAPIClass.setEnabled(false);
					jtTable.setEnabled(true);
				} else if (jcbSearchingClass.getSelectedIndex() == 2 && searchAction(tabNum,true)) {
					ibtjButton[0].setEnabled(false);
					ibtjButton[1].setEnabled(false);
					ibtjButton[2].setEnabled(false);
					ibtjButton[3].setEnabled(false);
					jcbBookStatus.setEnabled(false);
					jcbSearchingClass.setEnabled(false);
					jcbAPIClass.setEnabled(false);
					jtTable.setEnabled(true);
				}
			}
		});
		ibtjButton[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<jtfInfo[tabNum].length;i++) {
					jtfInfo[tabNum][i].setEditable(true);
					jtfInfo[tabNum][i].setBackground(Color.WHITE);
				}
				if (jcbSearchingClass.getSelectedIndex() == 1) {
					ibtjButton[3].setEnabled(true);
					jcbBookStatus.setEnabled(true);
					jtfInfo[tabNum][7].setEditable(false);
					jtfInfo[tabNum][7].setBackground(Color.LIGHT_GRAY);
				} else if (jcbSearchingClass.getSelectedIndex() == 2) {
					jcbBookStatus.setSelectedIndex(1);
					getDateFromToday(0);
					jtfInfo[tabNum][9].setText(neededDate.toString());
					ibtjButton[2].setEnabled(true);
					jcbBookStatus.setEnabled(false);
				}
				ibtjButton[1].setEnabled(false);
				jcbSearchingClass.setEnabled(false);
				jcbAPIClass.setEnabled(false);
				jtTable.setEnabled(false);
			}
		});
		ibtjButton[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(inputAction(tabNum,2)) {
					ibtjButton[2].setEnabled(false);
					jcbBookStatus.setEnabled(false);
					jtTable.setEnabled(true);
				}
				else{
					for(int i=0;i<jtfInfo[tabNum].length;i++) {
						jtfInfo[tabNum][i].setEditable(true);
						jtfInfo[tabNum][i].setBackground(Color.WHITE);
					}
				}
			}
		});
		ibtjButton[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(inputAction(tabNum,1)) {
					if (searchAction(tabNum,false)) {
						ibtjButton[3].setEnabled(false);
						jcbBookStatus.setEnabled(false);
						jtTable.setEnabled(true);
					}
				}
				else{
					for(int i=0;i<jtfInfo[tabNum].length;i++) {
						jtfInfo[tabNum][i].setEditable(true);
						jtfInfo[tabNum][i].setBackground(Color.WHITE);
					}
					jtfInfo[tabNum][7].setEditable(false);
					jtfInfo[tabNum][7].setBackground(Color.LIGHT_GRAY);
				}
			}
		});
		ibtjButton[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ibtjButton[0].setEnabled(true);
				ibtjButton[1].setEnabled(false);
				ibtjButton[2].setEnabled(false);
				ibtjButton[3].setEnabled(false);
				jcbBookStatus.setSelectedIndex(0);
				jcbBookStatus.setEnabled(false);
				jcbSearchingClass.setSelectedIndex(0);
				jcbSearchingClass.setEnabled(true);
				jcbAPIClass.setSelectedIndex(0);
				jcbAPIClass.setEnabled(false);
				jtTable.setEnabled(true);
				clearTextFieldAndTable(tabNum);
			}
		});
		//[도서 반입] 내부패널[1] 내 위치 설정
		jcbSearchingClass.setBounds(10, 10, ibtjpWidth[1]-20, ibtjpHeight[1]/5-15);
		jcbAPIClass.setBounds(10, ibtjpHeight[1]/5+5, ibtjpWidth[1]-20, ibtjpHeight[1]/5-15);
		ibtjButton[0].setBounds(10, ibtjpHeight[1]*2/5, ibtjpWidth[1]/2-15, ibtjpHeight[1]/5-10);
		ibtjButton[1].setBounds(10, ibtjpHeight[1]*3/5, ibtjpWidth[1]/2-15, ibtjpHeight[1]/5-10);
		ibtjButton[2].setBounds(ibtjpWidth[1]/2+5, ibtjpHeight[1]*2/5, ibtjpWidth[1]/2-15, ibtjpHeight[1]/5-10);
		ibtjButton[3].setBounds(ibtjpWidth[1]/2+5, ibtjpHeight[1]*3/5, ibtjpWidth[1]/2-15, ibtjpHeight[1]/5-10);
		ibtjButton[4].setBounds(10, ibtjpHeight[1]*4/5, ibtjpWidth[1]-20, ibtjpHeight[1]/5-10);
		//[도서 반입] 내부패널[2] 레이아웃 설정 및 JTable 설정
		ibtjPanel[2].setLayout(new FlowLayout());
		//[도서 반입] JTabel 행 클릭시 Action 설정
		ListSelectionModel rowSel=jtTable.getSelectionModel();
		rowSel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowSel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm=(ListSelectionModel)e.getSource();
				if(!e.getValueIsAdjusting()&&-1<lsm.getMinSelectionIndex()) {
					selectedRow=lsm.getMinSelectionIndex();
					infoToTextField(tabNum,selectedRow);
					ibtjButton[1].setEnabled(true);
					if (searchBookInfo[12].get(selectedRow).equals("1")) {
						jcbBookStatus.setSelectedIndex(1);
					}
					else if (searchBookInfo[12].get(selectedRow).equals("2")) {
						jcbBookStatus.setSelectedIndex(2);
					}
					else if (searchBookInfo[12].get(selectedRow).equals("3")) {
						jcbBookStatus.setSelectedIndex(3);
					}
					else {
						jcbBookStatus.setSelectedIndex(0);
					}
				}
			}
		});
		//[도서 반입] JTabel 열 크기
		jtTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);//JTable 열 크기 조절
		jtTable.setRowHeight(20);
		jtTable.getColumn(colNameString[0]).setPreferredWidth((int) (50*multplyCoef));
		jtTable.getColumn(colNameString[0]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[1]).setPreferredWidth((int) (150*multplyCoef));
		jtTable.getColumn(colNameString[1]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[2]).setPreferredWidth((int) (400*multplyCoef));
		jtTable.getColumn(colNameString[3]).setPreferredWidth((int) (200*multplyCoef));
		jtTable.getColumn(colNameString[3]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[4]).setPreferredWidth((int) (100*multplyCoef));
		jtTable.getColumn(colNameString[4]).setCellRenderer(celAlignCenter);
		jtTable.getColumn(colNameString[5]).setPreferredWidth((int) (100*multplyCoef));
		jtTable.getColumn(colNameString[5]).setCellRenderer(celAlignCenter);
		//[도서 반입] 내부패널[2]에 JScrollPane 설정
		ibtjPanel[2].add(jsPane);
		jsPane.setPreferredSize(new Dimension(pWidth, pHeight*2/3));
		//[도서 반입] JButton, JTextField 최초상태 설정
		for (int i = 0; i < jtfInfo[tabNum].length; i++) {
			jtfInfo[tabNum][i].setEditable(false);
			jtfInfo[tabNum][i].setBackground(Color.LIGHT_GRAY);
		}
		ibtjButton[0].setEnabled(false);
		ibtjButton[1].setEnabled(false);
		ibtjButton[2].setEnabled(false);
		ibtjButton[3].setEnabled(false);
		jcbBookStatus.setEnabled(false);
		jcbAPIClass.setEnabled(false);
		tabjPanel[tabNum].updateUI();
	}
	public boolean searchAction(int tabNum,boolean API) {	//입력된 정보로 DB,API에서 검색
		boolean result=false;
		tempST.setLength(0);
		if (API) {
			BM_UsingAPI api=new BM_UsingAPI();
			getCondition(tabNum,1);
			if(checkConditionEmpty(selectedInfo)) {
				tempST.append(api.searchBookOnAPI(selectedInfo));
				if(tempST.toString().equals("true")){
					copyResult(api.searchBookInfo,tabNum);
					api.initValues("book");
					infoToTable(tabNum);
					for(int i=0;i<jtfInfo[tabNum].length;i++) {
						jtfInfo[tabNum][i].setEditable(false);
						jtfInfo[tabNum][i].setBackground(Color.LIGHT_GRAY);
					}
					tabjPanel[tabNum].updateUI();
					messageDialog(messageString[2],titleString[5]);
					result=true;
				}
				else if(tempST.toString().equals("empty")){
					messageDialog(messageString[3],titleString[5]);
					result=false;
				}
				else {
					errorDialog(errorString[5]+tempST.toString());
					result=false;
				}
			}
			else {
				errorDialog(errorString[1]);
				result=false;
			}
		}
		else if (!API) {
			getCondition(tabNum,0);
			if(checkConditionEmpty(selectedInfo)) {
				if(tabNum==1||tabNum==3) {
					if(tabNum==1) {
						clearTextFieldAndTable(3);
					}
					else if(tabNum==3) {
						clearTextFieldAndTable(1);
					}
					initValues("user");
					tempST.append(dao.searchUser(selectedInfo));
				}
				else if(tabNum==5) {
					getCondition(tabNum, 0);
					initValues("userbook");
					tempST.append(dao.searchUserBookList(selectedInfo.get(0).toString().trim()));
				}
				else if(tabNum==4||tabNum==0||tabNum==2){
					if(tabNum==0) {
						clearTextFieldAndTable(4);
					}
					else if(tabNum==4) {
						clearTextFieldAndTable(0);
					}
					initValues("book");
					tempST.append(dao.searchBookOnDB(selectedInfo));
				}
				if(tempST.toString().equals("true")){
					if(tabNum==1||tabNum==3) {
						copyResult(dao.searchUserInfo,tabNum);
						dao.initValues("user");
					}
					else if (tabNum==5) {
						copyResult(dao.searchUserBookList,tabNum);
						dao.initValues("userbook");
					}
					else if(tabNum==4||tabNum==0||tabNum==2){
						copyResult(dao.searchBookInfo,tabNum);
						dao.initValues("book");
					}
					infoToTable(tabNum);
					if (tabNum < 5) {
						for (int i = 0; i < jtfInfo[tabNum].length; i++) {
							jtfInfo[tabNum][i].setEditable(false);
							jtfInfo[tabNum][i].setBackground(Color.LIGHT_GRAY);
						}
						tabjPanel[tabNum].updateUI();
					}
					else if(tabNum==5) {
						tabjPanel[2].updateUI();
					}
					messageDialog(messageString[2],titleString[5]);
					result=true;
				}
				else if(tempST.toString().equals("empty")){
					messageDialog(messageString[3],titleString[5]);
					result=false;
				}
				else {
					errorDialog(errorString[6]+tempST.toString());
					result=false;
				}
			}
			else {
				errorDialog(errorString[1]);
				result=false;
			}
		}
		tempST.setLength(0);
		return result;
	}
	public boolean inputAction(int tabNum,int editNum) {	//입력된 정보로 DB에 입력
		//editNum 0:연체비,1:폐기,수정,2:반입,3:대출,4:반납,5:회원가입, 6:회원수정
		boolean result=false,finalCheck=false;
		tempST.setLength(0);
		for(int i=0;i<jtfInfo[tabNum].length;i++) {
			jtfInfo[tabNum][i].setEditable(false);
			jtfInfo[tabNum][i].setBackground(Color.LIGHT_GRAY);
		}
		if (editNum!=0) {
			getCondition(tabNum,2);
		}
		else if(editNum==0&&tabNum==2){
			getCondition(tabNum,1);
		}
		if(checkInputInfoEmpty(selectedInfo)) {
			if (tabNum!=3&&editNum!=0) {
				finalCheck=bookInfoDialog(editNum,tabNum);
			}
			else if (tabNum==3) {
				finalCheck=userInfoDialog(tabNum);
			}
			else if (tabNum==2&&editNum==0) {
				finalCheck=lateFeeDialog(searchUserBookList[5].get(selectedRow));
			}
			if(finalCheck) {
				if(editNum==3) {
					tempST.append(dao.checkingoutBook(selectedInfo));
				}
				else if(editNum==4) {
					getIsLate(selectedInfo.get(14));
					tempST.append(dao.returnBook(selectedInfo,isLate));
				}
				else if(editNum==5) {
					tempST.append(dao.addmissionUser(selectedInfo));
				}
				else if(editNum==2) {
					tempST.append(dao.importBook(selectedInfo));
				}
				else if(editNum==1) {
					tempST.append(dao.modifiyBook(selectedInfo));
				}
				else if(editNum==6) {
					tempST.append(dao.modifyUser(selectedInfo));
				}
				else if (editNum==0) {
					tempST.append(dao.payLateFee(selectedInfo));
				}
				if(tempST.toString().equals("true")){
					messageDialog(messageString[4],titleString[6]);
					result=true;
				}
				else if(tempST.toString().equals("false")){
					errorDialog(errorString[7]);
					result=false;
				}
				else {
					errorDialog(errorString[6]+tempST.toString());
					result=false;
				}
			}
			else if(finalCheck!=true) {
				result=false;
			}
		}
		else {
			errorDialog(errorString[2]);
			result=false;
		}
		tempST.setLength(0);
		return result;
	}
	public void getDateFromToday(int plus) {
		neededDate.setLength(0);
		neededDate.append(dao.getDate(plus).toString().trim());
		if (neededDate.length()!=10) {
			errorDialog(errorString[6]+neededDate.toString());
			neededDate.setLength(0);
		}
	}
	public void getIsLate(String returnDate) {
		StringBuffer tempST = new StringBuffer();
		isLate=0;
		tempST.append(dao.isLate(returnDate));
		if (4<tempST.length()) {
			errorDialog(errorString[6]+neededDate.toString());
			tempST.setLength(0);
		}
		else {
			isLate=Integer.parseInt(tempST.toString());
		}
		tempST.setLength(0);
	}
	public void getCondition(int tabNum,int input) {		//상단에 입력한 정보를 selectedInfo에 저장
		//select 0:searchDB, 1:searchAPI, 2:inputDB or checkingout/return book
		selectedInfo.clear();
		if(tabNum==5) {
			selectedInfo.add(jtfInfo[1][0].getText().trim());
		}
		else if(tabNum==4&&input==0) {
			for(int i=0;i<jtfInfo[tabNum].length;i++) {
				selectedInfo.add(jtfInfo[tabNum][i].getText().trim());
			}
		}
		else if(tabNum==4&&input==1) {
			selectedInfo.add(jtfInfo[tabNum][0].getText().trim());
			selectedInfo.add(jtfInfo[tabNum][1].getText().trim());
			selectedInfo.add(jtfInfo[tabNum][3].getText().trim());
			selectedInfo.add(jtfInfo[tabNum][5].getText().trim());
			selectedInfo.add(jtfInfo[tabNum][6].getText().trim());
		}
		else if(tabNum==4&&input==2) {
			for(int i=0;i<jtfInfo[tabNum].length;i++) {
				selectedInfo.add(jtfInfo[tabNum][i].getText().trim());
			}
			selectedInfo.add(searchBookInfo[11].get(selectedRow));	//thumbnail
			selectedInfo.add(jtfTmp[1].getText().trim());				//bookstatus
			selectedInfo.add("");										//checkoutdate
			selectedInfo.add("");										//returndate
		}
		else if(tabNum==2&&input==0) {
			selectedInfo.add(jtfTmp[0].getText().trim());
			selectedInfo.add(jtfInfo[tabNum][10].getText().trim());
		}
		else if(tabNum==2&&input==1) {
			selectedInfo.add(jtfInfo[tabNum][0].getText().trim());
			selectedInfo.add(searchUserBookList[0].get(selectedRow).trim());
			selectedInfo.add(searchUserBookList[6].get(selectedRow).trim());
		}
		else if(tabNum==2&&input==2) {
			for (int i=0;i<searchBookInfo.length-2;i++) {
				selectedInfo.add(searchBookInfo[i].get(selectedRow).toString().trim());
			}
			getDateFromToday(0);
			if (searchBookInfo[13].get(selectedRow).toString().trim().equals("")) {
				selectedInfo.add(neededDate.toString());
				getDateFromToday(30);
				selectedInfo.add(neededDate.toString());
			}
			else {
				selectedInfo.add(searchBookInfo[13].get(selectedRow).toString().trim());
				selectedInfo.add(searchBookInfo[14].get(selectedRow).toString().trim());
			}
			getDateFromToday(0);
			selectedInfo.add(jtfInfo[tabNum][0].getText().trim());
			selectedInfo.add(neededDate.toString());
		}
		else if((tabNum==1||tabNum==3)&&input==0) {
			for(int i=0;i<jtfInfo[tabNum].length-6;i++) {
				selectedInfo.add(jtfInfo[tabNum][i].getText().trim());
			}
		}
		else if(tabNum==1&&input!=0) {
			for(int i=0;i<jtfInfo[tabNum].length-2;i++) {
				selectedInfo.add(jtfInfo[tabNum][i].getText().trim());
			}
		}
		else if(tabNum==3&&input==2) {
			for(int i=0;i<9;i++) {
				selectedInfo.add(jtfInfo[tabNum][i].getText().trim());
			}
		}
		else if(tabNum==0) {
			for(int i=0;i<jtfInfo[tabNum].length-3;i++) {
				selectedInfo.add(jtfInfo[tabNum][i].getText().trim());
			}
		}
	}
	public void copyResult(ArrayList source[],int tabNum) {	//다른 객체(DB,API)의 ArrayList를 복제
		if(tabNum==1||tabNum==3) {
			initValues("user");
			for(int i=0;i<source.length;i++) {
				searchUserInfo[i].addAll(source[i]);
			}
		}
		else if(tabNum==5) {
			initValues("userbook");
			for(int i=0;i<source.length;i++) {
				searchUserBookList[i].addAll(source[i]);
			}
		}
		else if (tabNum==4||tabNum==0||tabNum==2) {
			initValues("book");
			for(int i=0;i<source.length;i++) {
				searchBookInfo[i].addAll(source[i]);
			}
		}
	}
	public void infoToTable(int tabNum) {			//입력된 정보를 해당 탭의 테이블에 입력
		dtmModel[tabNum].setRowCount(0);
		if(tabNum==1||tabNum==3) {
			for (int i = 0; i < searchUserInfo[0].size(); i++) {
				Object rowData[] = new Object[7];
				rowData[0] = Integer.toString(i + 1);
				for (int j=1;j<rowData.length;j++) {
					if (colNum[tabNum][j-1]==5&&tabNum!=5) {
						if (searchUserInfo[colNum[tabNum][j-1]].get(i).trim().equals("1")) {
							rowData[j] = userInfoString[12];
						}
						else if (searchUserInfo[colNum[tabNum][j-1]].get(i).trim().equals("2")) {
							rowData[j] = userInfoString[13];
						}
					}
					else {
						rowData[j] = searchUserInfo[colNum[tabNum][j-1]].get(i).trim();
					}
				}
				dtmModel[tabNum].insertRow(i, rowData);
			}
		}
		else if (tabNum==5) {
			for (int i = 0; i < searchUserBookList[0].size(); i++) {
				Object rowData[] = new Object[7];
				rowData[0] = Integer.toString(i + 1);
				for (int j=1;j<rowData.length;j++) {
					rowData[j] = searchUserBookList[j-1].get(i).trim();
				}
				dtmModel[tabNum].insertRow(i, rowData);
			}
		}
		else if(tabNum==4||tabNum==0||tabNum==2){
			for (int i = 0; i < searchBookInfo[0].size(); i++) {
				Object rowData[] = new Object[6];
				rowData[0] = Integer.toString(i + 1);
				for (int j=1;j<rowData.length;j++) {
					if (colNum[tabNum][j-1]==12) {
						if (searchBookInfo[colNum[tabNum][j-1]].get(i).trim().equals("1")) {
							rowData[j] = bookInfoString[15];
						}
						else if (searchBookInfo[colNum[tabNum][j-1]].get(i).trim().equals("2")) {
							rowData[j] = bookInfoString[16];
						}
						else if (searchBookInfo[colNum[tabNum][j-1]].get(i).trim().equals("3")) {
							rowData[j] = bookInfoString[17];
						}
					}
					else {
						rowData[j] = searchBookInfo[colNum[tabNum][j-1]].get(i).trim();
					}
				}
				dtmModel[tabNum].insertRow(i, rowData);
			}
		}
	}
	public void infoToTextField(int tabNum,int rowNum) {	//테이블에 출력된 정보를 상단에 출력
		for(int i=0;i<jtfInfo[tabNum].length;i++) {
			jtfInfo[tabNum][i].setText("");
		}
		if (tabNum==2||tabNum==1||tabNum==3) {
			for(int i=0;i<jtfInfo[tabNum].length-1;i++) {
				if (i==5) {
					if (searchUserInfo[i].get(rowNum).equals("1")) {
						jtfInfo[tabNum][i].setText(userInfoString[12]);
					}
					else if (searchUserInfo[i].get(rowNum).equals("2")) {
						jtfInfo[tabNum][i].setText(userInfoString[13]);
					}
				}
				else if (i==6) {
					if (searchUserInfo[i].get(rowNum).equals("1")) {
						jtfInfo[tabNum][i].setText(userInfoString[14]);
					}
					else if (searchUserInfo[3].get(rowNum).equals("2")) {
						jtfInfo[tabNum][i].setText(userInfoString[15]);
					}
					else if (searchUserInfo[3].get(rowNum).equals("3")) {
						jtfInfo[tabNum][i].setText(userInfoString[16]);
					}
				}
				else {
					jtfInfo[tabNum][i].setText(searchUserInfo[i].get(rowNum));
				}
			}
		}
		else if (tabNum==0) {
			jlthumbnail[tabNum].setIcon(null);
			for(int i=0;i<jtfInfo[tabNum].length-2;i++) {
				jtfInfo[tabNum][i].setText(searchBookInfo[i].get(rowNum));
			}
			if (searchBookInfo[12].get(rowNum).equals("1")) {
				jtfInfo[tabNum][9].setText(bookInfoString[15]);
			}
			else if (searchBookInfo[12].get(rowNum).equals("2")) {
				jtfInfo[tabNum][9].setText(bookInfoString[16]);
			}
			else if (searchBookInfo[12].get(rowNum).equals("3")) {
				jtfInfo[tabNum][9].setText(bookInfoString[17]);
			}
			jtfInfo[tabNum][10].setText(searchBookInfo[14].get(rowNum));
			try {
				jlthumbnail[tabNum].setIcon(new ImageIcon(new URL(searchBookInfo[11].get(rowNum))));
			} catch (MalformedURLException e) {
				errorDialog(e.getMessage());
			}
		}
		else if (tabNum==4) {
			jlthumbnail[tabNum].setIcon(null);
			for(int i=0;i<jtfInfo[tabNum].length;i++) {
				jtfInfo[tabNum][i].setText(searchBookInfo[i].get(rowNum));
			}
			jtfTmp[1].setText(searchBookInfo[12].get(rowNum));
			try {
				jlthumbnail[tabNum].setIcon(new ImageIcon(new URL(searchBookInfo[11].get(rowNum))));
			} catch (MalformedURLException e) {
				errorDialog(e.getMessage());
			}			
		}
		tabjPanel[tabNum].updateUI();
	}
	public void clearTextFieldAndTable(int tabNum){							//해당 탭의 출력물을 모두 지움
		if(tabNum!=2) {
			for(int i=0;i<jtfInfo[tabNum].length;i++) {
				jtfInfo[tabNum][i].setText("");
			}
		}
		else if (tabNum==2) {
			jtfInfo[tabNum][10].setText("");
		}
		if(tabNum==0||tabNum==4) {
			jlthumbnail[tabNum].setIcon(null);
		}
		else if(tabNum==1) {
			dtmModel[5].setRowCount(0);
			for(int i=0;i<jtfInfo[2].length;i++) {
				jtfInfo[2][i].setText("");
			}
		}
		dtmModel[tabNum].setRowCount(0);
		tabjPanel[tabNum].updateUI();
	}
	public boolean checkConditionEmpty(ArrayList<String> infoForQuery) {
		StringBuffer nullCheck=new StringBuffer();
		boolean lengthIsOne=false;
		for(int i=0;i<infoForQuery.size();i++) {
			if(infoForQuery.get(i).trim().length()==1&&!(i==8&&infoForQuery.size()==9)) {
				lengthIsOne=true;
				break;
			}
			nullCheck.append(infoForQuery.get(i).trim());
		}
		if(nullCheck.toString().equals("")||lengthIsOne) {
			return false;
		}
		else {
			return true;
		}
	}
	public boolean checkInputInfoEmpty(ArrayList<String> infoForQuery) {
		boolean lengthIsOneOrZero=false;
		if(infoForQuery.size()<10) {
			for(int i=0;i<infoForQuery.size();i++) {
				if(((infoForQuery.get(i).trim().length()<2&&i!=8&&i!=5&&i!=6)||(i==5&&infoForQuery.get(i).equals("0"))||(i==6&&infoForQuery.get(i).equals("0")))&&!(i==2&&infoForQuery.size()==3)) {
					lengthIsOneOrZero=true;
					break;
				}
			}
		}
		else {
			for(int i=0;i<infoForQuery.size();i++) {
				if((infoForQuery.get(i).trim().length()<2&&(i!=2&&i<10))||(i==12&&infoForQuery.get(i).equals("0"))) {
					lengthIsOneOrZero=true;
					break;
				}
			}
		}
		if(lengthIsOneOrZero) {
			return false;
		}
		else {
			return true;
		}
	}
	public void loginDialog() {									//로그인 다이얼로그
		JDialog dLogin=new JDialog(jfMain,true);
		JLabel ldjLabel[]=new JLabel[2];
		JTextField ldjtfID=new JTextField("",10);
		JPasswordField ldjtfPW=new JPasswordField("",10);
		JButton ldjButton[]=new JButton[2];
		dLogin.setTitle(titleString[1]);
		for(int i=0;i<ldjLabel.length;i++) {
			ldjLabel[i]=new JLabel();			
			ldjButton[i]=new JButton(buttonNameString[i]);
			ldjLabel[i].setVerticalAlignment(JLabel.CENTER);
			ldjLabel[i].setHorizontalAlignment(JLabel.CENTER);
			ldjLabel[i].setFont(ngB);
		}
		ldjtfID.setFont(ngP);
		ldjtfPW.setFont(ngP);
		ldjLabel[0].setText(menuString[4]);
		ldjLabel[1].setText(menuString[5]);
		ldjButton[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tempST.setLength(0);
				tempST.append(dao.connDB(ldjtfID.getText(),new String(ldjtfPW.getPassword())));
				if(tempST.toString().equals("true")) {
					if (ldjtfID.getText().equals("project")) {
						identify=5;
					}
					tempST.setLength(0);
					tempST.append(dao.modifyLateFee().equals("true"));
					if (tempST.toString().equals("true")) {}
					else {
						errorDialog(tempST.toString());
					}
					for(int i=0;i<identify;i++) {
						jtpMainTab.addTab(tabNameString[i], tabjPanel[i]);
					}
					SwingUtilities.updateComponentTreeUI(jfMain);
					switch(identify) {
					case 5:
						addmissionUserTab(3);
						importBookTab(4);
					case 3:
						searchUserTab(1);
						checkOutReturnTab(2);
					case 0:
						searchBookTab(0);
						break;
					}
					isLogin=1;
					dLogin.dispose();
				}
				else if(tempST.toString().equals("ORA-01017: invalid username/password; logon denied\n")){
					errorDialog(errorString[0]);
				}
				else {
					errorDialog(tempST.toString());
				}
			}
		});
		ldjButton[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quitDialog();
			}
		});
		dLogin.setLayout(null);
		ldjLabel[0].setBounds(10, 10, 60, 30);
		ldjLabel[1].setBounds(10, 50, 60, 30);
		ldjtfID.setBounds(80, 10, 160, 30);
		ldjtfPW.setBounds(80, 50, 160, 30);
		ldjButton[0].setBounds(35, 90, 80, 30);
		ldjButton[1].setBounds(135, 90, 80, 30);
		dLogin.add(ldjLabel[0]);
		dLogin.add(ldjLabel[1]);
		dLogin.add(ldjtfID);
		dLogin.add(ldjtfPW);
		dLogin.add(ldjButton[0]);
		dLogin.add(ldjButton[1]);
		dLogin.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				quitDialog();
			}
		});
		dLogin.setSize(250, 170);
		dLogin.setLocationRelativeTo(jfMain);
		dLogin.setResizable(false);
		dLogin.setVisible(true);
	}
	public boolean bookInfoDialog(int editNum, int tabNum) {	//책 대출/반납&반입,반출 시 DB입력 전 정보 확인용 다이얼로그 
		checkByUser=false;
		JDialog dBookInfo=new JDialog(jfMain,true);
		JLabel bidjLabel[]=new JLabel[12];
		JTextField bidjtField[]=new JTextField[12];
		JButton bidjButton[]=new JButton[2];
		dBookInfo.setTitle(titleString[2]);
		for(int i=0;i<bidjLabel.length;i++) {
			bidjLabel[i]=new JLabel();
			bidjLabel[i].setVerticalAlignment(JLabel.CENTER);
			bidjLabel[i].setHorizontalAlignment(JLabel.CENTER);
			bidjLabel[i].setFont(ngB);
			bidjtField[i]=new JTextField("",10);
			bidjtField[i].setHorizontalAlignment(JLabel.CENTER);
			bidjtField[i].setFont(ngB);
			bidjtField[i].setEditable(false);
			bidjtField[i].setBackground(Color.LIGHT_GRAY);
		}
		for(int i=0;i<bidjLabel.length-6;i++) {
			bidjLabel[i].setText(bookInfoString[i]);
			bidjtField[i].setText(selectedInfo.get(i));;
		}
		bidjLabel[6].setText(bookInfoString[7]);
		bidjtField[6].setText(selectedInfo.get(6));;
		bidjLabel[7].setText(bookInfoString[8]);
		bidjtField[7].setText(selectedInfo.get(7));;
		bidjLabel[8].setText(bookInfoString[12]);
		bidjtField[8].setText(selectedInfo.get(8));;
		if (tabNum==2&&editNum!=4) {
			bidjLabel[9].setText(bookInfoString[13]);
			bidjLabel[10].setText(bookInfoString[14]);
			bidjtField[10].setText(selectedInfo.get(13));
			bidjtField[11].setText(selectedInfo.get(14));
		}
		else if (tabNum==2&&editNum==4) {
			bidjLabel[9].setText(userInfoString[11]);
			bidjLabel[10].setText(bookInfoString[14]);
			bidjtField[10].setText(neededDate.toString());
			bidjtField[11].setText(selectedInfo.get(14));
		}
		else {
			bidjLabel[9].setText(bookInfoString[9]);
			bidjLabel[10].setText(bookInfoString[10]);
			bidjtField[10].setText(selectedInfo.get(9));
			bidjtField[11].setText(selectedInfo.get(10));
		}
		bidjLabel[11].setText("");
		if(selectedInfo.get(12).trim().equals("1")) {
			bidjtField[9].setText(bookInfoString[15]);
		}else if(selectedInfo.get(12).trim().equals("2")) {
			bidjtField[9].setText(bookInfoString[16]);
		}else if(selectedInfo.get(12).trim().equals("3")){
			bidjtField[9].setText(bookInfoString[17]);
		}
		try {
			bidjLabel[11].setIcon(new ImageIcon(new URL(selectedInfo.get(11))));
		} catch (MalformedURLException e1) {
			errorDialog(e1.getMessage());
		}
		bidjButton[0]=new JButton(buttonNameString[0]);
		bidjButton[1]=new JButton(buttonNameString[1]);
		bidjButton[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkByUser=true;
				dBookInfo.dispose();
			}
		});
		bidjButton[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dBookInfo.dispose();
			}
		});
		dBookInfo.setLayout(null);
		dBookInfo.setSize(700, 510);
		for(int i=0;i<bidjLabel.length-3;i++) {
			bidjLabel[i].setBounds(160, 10+i*40, 70, 30);
		}
		for(int i=0,j=0;i<bidjtField.length-2;i++) {
			if(i==5) {
				i+=2;
				j++;
			}
			bidjtField[i].setBounds(240, 10+j*40, 450, 30);
			j++;
		}
		bidjtField[5].setBounds(240, 210, 190, 30);
		bidjtField[6].setBounds(450, 210, 240, 30);
		bidjLabel[9].setBounds(160, 370, 70, 30);
		bidjLabel[10].setBounds(430, 370, 70, 30);			//160(시작)+  530(전체   -160(70+10)*2 370
		bidjtField[10].setBounds(240, 370, 180, 30);
		bidjtField[11].setBounds(510, 370, 180, 30);
		bidjLabel[11].setBounds(10,55,140,230);//thumnail
		bidjButton[0].setBounds(250,420,80,30);
		bidjButton[1].setBounds(370,420,80,30);
		dBookInfo.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dBookInfo.dispose();
			}
		});
		for(int i=0;i<bidjLabel.length;i++) {
			dBookInfo.add(bidjLabel[i]);
			dBookInfo.add(bidjtField[i]);
		}
		dBookInfo.add(bidjButton[0]);
		dBookInfo.add(bidjButton[1]);
		dBookInfo.setLocationRelativeTo(jfMain);;
		dBookInfo.setResizable(false);
		dBookInfo.setVisible(true);
		return checkByUser;
	}
	public boolean userInfoDialog(int tabNum) {					//회원확인 및 가입 시 DB입력 전 정보 확인용 다이얼로그
		checkByUser=false;
		JDialog dUserInfo=new JDialog(jfMain,true);
		JLabel uidjLabel[]=new JLabel[9];
		JTextField uidjtField[]=new JTextField[9];
		JButton uidjButton[]=new JButton[2];
		dUserInfo.setTitle(titleString[3]);
		for(int i=0;i<uidjLabel.length;i++) {
			uidjLabel[i]=new JLabel(userInfoString[i]);
			uidjLabel[i].setVerticalAlignment(JLabel.CENTER);
			uidjLabel[i].setHorizontalAlignment(JLabel.CENTER);
			uidjLabel[i].setFont(ngB);
			uidjtField[i]=new JTextField("",10);
			uidjtField[i].setHorizontalAlignment(JLabel.CENTER);
			uidjtField[i].setFont(ngB);
			uidjtField[i].setEditable(false);
			uidjtField[i].setBackground(Color.LIGHT_GRAY);
		}
		for (int i=0;i<uidjtField.length;i++) {
			if (i==5) {
				if (selectedInfo.get(i).equals("1")) {
					uidjtField[i].setText(userInfoString[12]);
				}
				else if (selectedInfo.get(i).equals("2")) {
					uidjtField[i].setText(userInfoString[13]);
				}
				else{
					uidjtField[i].setText(selectedInfo.get(i));
				}
			}
			else if (i==6) {
				if (selectedInfo.get(i).equals("1")) {
					uidjtField[i].setText(userInfoString[14]);
				}
				else if (selectedInfo.get(i).equals("2")) {
					uidjtField[i].setText(userInfoString[15]);
				}
				else if (selectedInfo.get(i).equals("3")) {
					uidjtField[i].setText(userInfoString[16]);
				}
				else{
					uidjtField[i].setText(selectedInfo.get(i));
				}
			}
			else {
				uidjtField[i].setText(selectedInfo.get(i));
			}
		}
		uidjButton[0]=new JButton(buttonNameString[0]);
		uidjButton[1]=new JButton(buttonNameString[1]);
		uidjButton[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkByUser=true;
				dUserInfo.dispose();
			}
		});
		uidjButton[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dUserInfo.dispose();
			}
		});
		dUserInfo.setLayout(null);
		dUserInfo.setSize(550, 290);
		uidjLabel[0].setBounds(10, 10, 70, 30);
		uidjtField[0].setBounds(90, 10, 170, 30);
		uidjLabel[1].setBounds(280, 10, 70, 30);
		uidjtField[1].setBounds(370, 10, 170, 30);
		uidjLabel[2].setBounds(10, 50, 70, 30);
		uidjtField[2].setBounds(90, 50,170, 30);
		uidjLabel[3].setBounds(280, 50, 70, 30);
		uidjtField[3].setBounds(370, 50, 170, 30);
		uidjLabel[4].setBounds(10, 90, 70, 30);
		uidjtField[4].setBounds(90, 90, 450, 30);
		uidjLabel[5].setBounds(10, 130, 70, 30);
		uidjtField[5].setBounds(90, 130, 170, 30);
		uidjLabel[6].setBounds(280, 130, 70, 30);
		uidjtField[6].setBounds(370, 130, 170, 30);
		uidjLabel[7].setBounds(10, 170, 70, 30);
		uidjtField[7].setBounds(90, 170, 170, 30);
		uidjLabel[8].setBounds(280, 170, 70, 30);
		uidjtField[8].setBounds(370, 170, 170, 30);
		uidjButton[0].setBounds(175,220,80,30);
		uidjButton[1].setBounds(295,220,80,30);
		dUserInfo.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dUserInfo.dispose();
			}
		});
		for(int i=0;i<uidjLabel.length;i++) {
			dUserInfo.add(uidjLabel[i]);
			dUserInfo.add(uidjtField[i]);
		}
		dUserInfo.add(uidjButton[0]);
		dUserInfo.add(uidjButton[1]);
		dUserInfo.setLocationRelativeTo(jfMain);
		dUserInfo.setResizable(false);
		dUserInfo.setVisible(true);
		return checkByUser;
	}
	public boolean lateFeeDialog(String lateFee) {	//해당 회원의 연체료 반납 다이얼로그
		StringBuffer paidFee =new StringBuffer();
		paidFee.append(JOptionPane.showInputDialog(jfMain, messageString[5]+lateFee+messageString[6], titleString[7], JOptionPane.PLAIN_MESSAGE, null, null, null));
		if (paidFee.toString()==null) {
			paidFee.setLength(0);
		}
		if (paidFee.toString().replaceAll("[^0-9]", "").length()<3) {
			paidFee.setLength(0);
			errorDialog(errorString[8]);
			return false;
		}
		else if (Integer.parseInt(searchUserBookList[5].get(selectedRow))<Integer.parseInt(paidFee.toString().replaceAll("[^0-9]", ""))) {
			paidFee.setLength(0);
			errorDialog(errorString[9]);
			return false;
		}
		else if(2<paidFee.toString().replaceAll("[^0-9]", "").length()) {
			selectedInfo.add(paidFee.toString().replaceAll("[^0-9]", ""));
			paidFee.setLength(0);
			return true;
		}
		else {
			return false;
		}
	}
	public void messageDialog(String message, String title) {	//각종 기능 수행 시 뜨는 메시지 다이얼로그
		JOptionPane.showMessageDialog(jfMain, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	public void errorDialog(String message) {						//각종 에러시 뜨는 에러 다이얼로그
		JOptionPane.showMessageDialog(jfMain, message, titleString[4], JOptionPane.ERROR_MESSAGE);
	}
	public void quitDialog() {										//프로그램 종료 시 뜨는 종료 다이얼로그 
		int result=JOptionPane.showConfirmDialog(jfMain, messageString[1], messageString[0], JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result==0&&isLogin==1) {
			dao.quitDAO();
			System.exit(0);
		}
		else if (result==0&&isLogin==0) {
			System.exit(0);
		}
	}
	public void setUIFont(FontUIResource f) {						//프로그램 전체 UI에 일괄 폰트 적용
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, f);
			}
		}
	}
}
