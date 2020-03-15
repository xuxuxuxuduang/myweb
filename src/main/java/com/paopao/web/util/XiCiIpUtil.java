package com.paopao.web.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 西刺代理
 * 一次只能取到 20页  *10 2000条数据
 * https://www.xicidaili.com/nn/1
 */
public class XiCiIpUtil {
    private static String startUrl = "https://www.xicidaili.com/nn";//   .com/nn/1....n

    public static List<Map<String, String>> doXiCiIpUtil() {
        try {
            return parseHtml(startUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析html页面
     * @param html
     */
    private static List<Map<String, String>> parseHtml(String html) throws Exception {
        //解析页面的数据
        List<Map<String, String>> list = new ArrayList<>();
        Document document = Jsoup.connect(html + "/" + 1).timeout(6000).get();
        System.out.println(document.baseUri());
        Elements items = document.select("#ip_list").get(0).select("tr");
        for (int j = 1; j < items.size(); j++) {//每页显示101条数据  从第一条开始
            Elements elements = items.get(j).select("td");
            Map<String, String> map = new HashMap<>();
            map.put("IP", elements.get(1).text());
            map.put("PORT", elements.get(2).text());
            list.add(map);
        }
        return list;
    }

}
