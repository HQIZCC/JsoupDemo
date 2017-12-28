package hqizcc.jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetProvince {
	
	
	
	public static void main(String[] args) throws IOException {
		
		Properties proper = new Properties();
		
		String path = GetProvince.class.getClassLoader().getResource("province.properties").getPath();
		
		File file = new File(path);
		
		FileInputStream fis = new FileInputStream(file);
		
		proper.load(fis);
		
		String provinceUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2013/";
		
		Connection conn = Jsoup.connect(provinceUrl);
		
		Document document = conn.get();
		
		Elements select = document.select(".provincetr").select("td");
		
		for (Element element : select) {
			
			String provinceNum = element.select("a").attr("href");
			
			provinceNum = provinceNum.substring(0, provinceNum.indexOf("."));
			
			String provinceName = element.select("a").text();
			
			OutputStream os = new FileOutputStream(path);
			
			proper.setProperty(provinceNum, provinceName);
			
			proper.store(os, provinceName);
			
			System.out.println(provinceNum + " " + provinceName);
			
		}
//		int count = 0;
//		for (Element element : select) {
//			
//			count ++ ;
//			System.out.println(element);
//		}
//		
//		System.out.println(count);
//		System.out.println(body);
		
		
	}
}
