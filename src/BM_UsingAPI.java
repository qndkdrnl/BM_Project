package bmProject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;
public class BM_UsingAPI extends BM_InfoValue {
	private int responseCode=0;
	public String searchBookOnAPI(ArrayList<String> infoForQuery) {
		StringTokenizer totalResultDivide,oneResultDivide[];
		StringBuffer testEmpty=new StringBuffer();
		StringBuffer tmpInput=new StringBuffer();
		StringBuffer query= new StringBuffer();
		boolean isErr=false;
		ArrayList<String> listCondition = new ArrayList<String>();
		listCondition.add("&target=title");
		listCondition.add("&target=person");
		listCondition.add("&target=publisher");
		listCondition.add("&target=isbn");
		query.setLength(0);
		query.append("https://dapi.kakao.com/v3/search/book?query=");
		for(int i=0;i<infoForQuery.size();i++) {
			if(infoForQuery.get(i).isEmpty()) {}
			else if(2<i) {
				query.append(infoForQuery.get(i).trim()+listCondition.get(3).trim());
			}
			else if(i<3) {
				try {
					query.append(URLEncoder.encode(infoForQuery.get(i).trim(), "UTF-8")+listCondition.get(i).trim());
				} catch (UnsupportedEncodingException e) {}
			}
		}
		query.append("&size=50");
		listCondition.clear();
		testEmpty.setLength(0);
		tmpInput.setLength(0);
		try {
			HttpURLConnection conn=(HttpURLConnection)new URL(query.toString()).openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "KakaoAK b20d1e45ed8bbb7b34b76193b6de3e19");
			responseCode = conn.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				isErr=true;
			}
			String inputLine;
			ArrayList<String> list = new ArrayList<String>();
			for (;(inputLine = br.readLine()) != null;) {
				tmpInput.append(inputLine);
				testEmpty.append(inputLine.replace("{\"documents\":[],\"meta\":{\"is_end\":true,\"pageable_count\":0,\"total_count\":0}}", "").replace("{", "").replace("}", ""));
			}
			if(testEmpty.toString().isEmpty()) {
				return "empty";
			}
			else if(isErr) {
				return testEmpty.toString()+"\nCode"+Integer.toString(responseCode);
			}
			totalResultDivide= new StringTokenizer(tmpInput.toString().replace("},{", "}◣{").replace("],\"meta\":{", "◣").replace("{\"documents\":[", ""),"◣");
			int countTokens = totalResultDivide.countTokens();
			for(int i=0;i<countTokens;i++) {
				list.add(totalResultDivide.nextToken());
			}
			for(int i=0;i<list.size();i++) {
				tmpInput.setLength(0);
				tmpInput.append(list.get(i).toString().replace("{\"authors\":", "").replace(",\"contents\":", "◣").replace(",\"datetime\":", "◣")
						.replace(",\"isbn\":", "◣").replace(",\"price\":", "◣").replace(",\"publisher\":", "◣").replace(",\"sale_price\":", "◣")
						.replace(",\"thumbnail\":", "◣").replace(",\"title\":", "◣").replace(",\"translators\":", "◣").replace(",\"url\":", "◣"));
				list.remove(i);
				list.add(i, tmpInput.toString());				
			}
			oneResultDivide=new StringTokenizer[list.size()];
			for(int i=0;i<list.size()-1;i++) {	//-1:마지막은 쓰레기
				tmpInput.setLength(0);
				oneResultDivide[i]=new StringTokenizer(list.get(i),"◣");
				searchBookInfo[1].add(oneResultDivide[i].nextToken().replace("\"", "").replace("[", "").replace("]", "").trim());
				tmpInput.append(oneResultDivide[i].nextToken());
				searchBookInfo[4].add(oneResultDivide[i].nextToken().replace("\"", "").trim());	//.substring(0,10)
				searchBookInfo[7].add(oneResultDivide[i].nextToken().replace("\"", "").trim());	//"isbn10 isbn13" 7번에 임시 보관
				tmpInput.append(oneResultDivide[i].nextToken());
				searchBookInfo[3].add(oneResultDivide[i].nextToken().replace("\"", "").trim());
				tmpInput.append(oneResultDivide[i].nextToken());
				searchBookInfo[11].add(oneResultDivide[i].nextToken().replace("\"", "").trim());
				searchBookInfo[0].add(oneResultDivide[i].nextToken().replace("\"", "").trim());
				searchBookInfo[2].add(oneResultDivide[i].nextToken().replace("\"", "").replace("[", "").replace("]", "").trim());
				tmpInput.append(oneResultDivide[i].nextToken());
			}
			//PubDate에서 yyyy-mm-dd 말고 삭제
			tmpInput.setLength(0);
			for(int i=0;i<searchBookInfo[4].size();i++) {
				if(10<searchBookInfo[4].get(i).length()) {
					tmpInput.append(searchBookInfo[4].get(i).substring(0,10));
					searchBookInfo[4].remove(i);
					searchBookInfo[4].add(i, tmpInput.toString().trim());
				}
				tmpInput.setLength(0);
			}
			//isbn10 -> 5, isbn13 -> 6 이동
			for(int i=0;i<searchBookInfo[7].size();i++) {	
				if(searchBookInfo[7].get(i).length()==24) {
					oneResultDivide[i]=new StringTokenizer(searchBookInfo[7].get(i)," ");
					searchBookInfo[5].add(oneResultDivide[i].nextToken().trim());
					searchBookInfo[6].add(oneResultDivide[i].nextToken().trim());
				}
				else if(10<searchBookInfo[7].get(i).length()) {
					searchBookInfo[5].add("");
					searchBookInfo[6].add(searchBookInfo[7].get(i).trim());
				}
				else {
					searchBookInfo[5].add(searchBookInfo[7].get(i).trim());
					searchBookInfo[6].add("");
				}
			}
			searchBookInfo[7].clear();
			for(int i=0;i<searchBookInfo[0].size();i++) {
				searchBookInfo[7].add("");
				searchBookInfo[8].add("");
				searchBookInfo[9].add("");
				searchBookInfo[10].add("");
				searchBookInfo[12].add("");
				searchBookInfo[13].add("");
				searchBookInfo[14].add("");
			}
			totalResultDivide=null;
			oneResultDivide=null;
			inputLine=null;
			testEmpty.setLength(0);
			tmpInput.setLength(0);
			query.setLength(0);
			list.clear();
			br.close();
			conn.disconnect();
			return "true";
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
