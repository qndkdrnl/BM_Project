package bmProject;
import java.util.ArrayList;
public class BM_InfoValue {
	ArrayList<String> searchUserInfo[], searchBookInfo[],searchUserBookList[],selectedInfo;
	BM_InfoValue(){
		searchUserInfo=new ArrayList[10];
		searchBookInfo=new ArrayList[15];
		searchUserBookList=new ArrayList[7];
		selectedInfo=new ArrayList<String>();
		for(int i=0;i<searchBookInfo.length;i++) {
			searchBookInfo[i]=new ArrayList<String>();
		}
		for(int i=0;i<searchUserInfo.length;i++) {
			searchUserInfo[i]=new ArrayList<String>();
		}
		for (int i=0;i<searchUserBookList.length;i++) {
			searchUserBookList[i]=new ArrayList<String>();
		}
	}
	public void initValues(String cleanOne) {
		if (cleanOne.equals("book")) {
			for(int i=0;i<searchBookInfo.length;i++) {
				searchBookInfo[i].clear();
			}
		}
		else if(cleanOne.equals("user")) {
			for(int i=0;i<searchUserInfo.length;i++) {
				searchUserInfo[i].clear();
			}
		}
		else if(cleanOne.equals("userbook")) {
			for(int i=0;i<searchUserBookList.length;i++) {
				searchUserBookList[i].clear();
			}
		}
	}
}
