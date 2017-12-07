package hqizcc;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.*;
import java.nio.BufferOverflowException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JddtDemo1 {

        // 省份对应的map
        private Map<String, String> provinceMap = new HashMap<String, String>();

        // 市级对应的map
        private Map<String, String> cityMap = new HashMap<String, String>();

        // 县级对应的map
        private Map<String, String> countyMap = new HashMap<String, String>();

        // 城镇对应的map
        private Map<String, String> villageMap = new HashMap<String, String>();

        // 村级对应的map
        private Map<String, String> cunMap = new HashMap<String, String>();

    public void getHtml() throws IOException {

        String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2013/";
        Connection conn = Jsoup.connect(url);

        Document document= conn.get();

        Element body = document.body();

        System.out.println(body);

       /* BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));

        BufferedWriter bfw = null;

        while(bfr.readLine() != null){
            String str = bfr.readLine();

            bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Administrator\\Desktop")));

            bfw.write(str);
            System.out.println("写出完毕");
        }

        bfw.close();

        bfr.close();*/

    }

    // 获取省份
    public void getProvice() throws IOException {

        // 目标位置
        String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2013/";

        // 建立连接
        Connection connect = Jsoup.connect(url).proxy("114.55.55.98", 80);

        // 获取document对象
        Document document = connect.timeout(500000000).get();

        // 根据对应的class属性获取省份信息
        Elements provinces = document.select("tr").select(".provincetr");

        // 通过map将对应的信息存储到map中
        for (Element e : provinces){
            Elements tds = e.select("td");

            for(Element td : tds){

                String proUrl = td.select("a").attr("href").toString();

                String proNum = proUrl.split("\\.")[0];

                String proName = td.select("a").text();

                System.out.println(proNum + " " + proName);

                provinceMap.put(proNum, proName);
            }
        }
    }

    // 获取城市
    public void getCity(Map<String, String> provinceMap) throws IOException {

        Set<String> keys = provinceMap.keySet();

        String cityUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2013/";

        for (String key : keys) {

            String cityUrl1 = cityUrl + key + ".html";

            Connection conn = Jsoup.connect(cityUrl1).proxy("223.223.187.195", 80);

            Document document = conn.timeout(50000000).get();

//            Element body = document.body();

            Elements cityTables = document.select("table").select(".citytable");

            for (Element cityTable : cityTables){

                Elements cityTrs = cityTable.select("tr").select(".citytr");

                // 输出对应的省份
//            System.out.println(provinceMap.get(key));
                for (Element cityTr : cityTrs){

//                String countUrl = cityTr.select("td").select("a").attr("href").toString();
                    String cityName = cityTr.select("a").text();

                    String cityNum = cityName.substring(0,6);

                    cityName = cityName.substring(12);

                    System.out.println(cityNum + " " + cityName);

                    cityMap.put(cityNum, cityName);

                }
            }
        }

    }

    // 获取对应的县或者区
    public void getCounty(Map<String, String> cityMap) throws IOException {

        String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2013/";

        Set<String> keys = cityMap.keySet();

        for (String key : keys){

//            System.out.println(key);

            String url1 = key.substring(0,2);

            String url2 = key.substring(0,4);

            String countyUrl = url + url1 + "/" + url2 + ".html";

            System.out.println(countyUrl);

            Connection connect = Jsoup.connect(countyUrl).proxy("119.130.115.226", 808);

            Document document = connect.timeout(500000000).get();

            /*Element body = document.bod

            System.out.println(body);

            break;*/



            Elements countyTables = document.select("table").select(".countytable");

            for (Element countyTable : countyTables){

                Elements countytrs = countyTable.select("tr").select(".countytr");

                for (Element countytr : countytrs){
//                countyUrl = countytr.select("td").select("a").attr("href");

                    if (countytr.select("a").text() != null){
                        String countyName = countytr.select("a").text();

//                        System.out.println(countyName);

                        String countyNum = countyName.substring(0,6);
//
//                        System.out.println(countyNum);

                  countyName = countyName.substring(12);

                  countyMap.put(countyNum, countyName);

//                System.out.println(countyNum + countyName);
                    } else {
                        String countyNum = countytr.select("td").text();
                        String countyName = countytr.select("td").text();

                        System.out.println(countyNum + " " + countyName);
                    }


                }
            }


        }

    }

    public static void main(String[] args) {

        JddtDemo1 jd = new JddtDemo1();

        // 获取省份
        try {
            jd.getProvice();
        } catch (IOException e) {
            System.out.println("获取省份报错了");
            e.printStackTrace();
        }

        // 获取城市
        try {
            jd.getCity(jd.provinceMap);
        } catch (IOException e) {
            System.out.println("获取城市报错了");
            e.printStackTrace();
        }

        // 获取县区
        try {
            jd.getCounty(jd.cityMap);
        } catch (IOException e) {
            System.out.println("获取区县报错了");
            e.printStackTrace();
        }

//        System.out.println(jd.countyMap);
    }
}
