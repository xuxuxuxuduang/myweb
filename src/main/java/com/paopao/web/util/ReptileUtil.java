package com.paopao.web.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 自定义爬虫工具类
 */
public class ReptileUtil {
    //除去<>中的值
    public static String replaceSymbol(String context) {
        int head = context.indexOf('<'); // 标记第一个使用左括号的位置
        if (head == -1) {
            return context; // 如果context中不存在括号，什么也不做，直接跑到函数底端返回初值str
        } else {
            int next = head + 1; // 从head+1起检查每个字符
            int count = 1; // 记录括号情况
            do {
                if (context.charAt(next) == '<') {
                    count++;
                } else if (context.charAt(next) == '>') {
                    count--;
                }
                next++; // 更新即将读取的下一个字符的位置
                if (count == 0) { // 已经找到匹配的括号
                    String temp = context.substring(head, next);
                    context = context.replace(temp, ""); // 用空内容替换，复制给context
                    head = context.indexOf('<'); // 找寻下一个左括号
                    next = head + 1; // 标记下一个左括号后的字符位置
                    count = 1; // count的值还原成1
                }
            } while (head != -1); // 如果在该段落中找不到左括号了，就终止循环
        }
        return context; // 返回更新后的context
    }

    /**
     * 读本地html
     * @param filePath
     * @return
     */
    public static Html readTxtFileBodyDoc(String filePath) {
        StringBuilder sb = new StringBuilder();
        Html html = null;
        try {
            String encoding = "UTF-8";
            //读取本地的html文件方法
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                // 进行body元素提取
                Document document = document = Jsoup.parse(file, encoding);
                html = new Html(document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }

    public static void main(String[] args) {
        Html html = readTxtFileBodyDoc("D:\\java爬虫\\webmagicDemo\\src\\main\\resources\\templates\\test3.html");
        List<Selectable> nodes = html.css(".weui_media_box").nodes();
        List<Map<String, String>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(nodes)){
            nodes.forEach(node -> {
                List<Selectable> titles = node.css(".weui_media_title").nodes();
                List<Selectable> times = node.css(".weui_media_extra_info").nodes();
                if(!CollectionUtils.isEmpty(titles) && !CollectionUtils.isEmpty(times)){
                    Selectable title = titles.get(0);
                    Selectable time = times.get(0);
                    Map<String, String> map = new HashMap<>();
                    map.put("title", replaceSymbol(title.toString() != null ? title.toString() : "暂无").trim());
                    String str = title.toString().replace("<h4", "")
                            .replace("</h4>", "").replace("hrefs=\"", "<");
                    int start = str.indexOf("<");
                    int end = str.indexOf(">");
                    map.put("url", str.substring(start + 1, end - 1));
                    String timeStr = replaceSymbol(time.toString() != null ? time.toString() : "暂无").trim();
                    map.put("time", timeStr);
                    list.add(map);
                }
            });
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (list != null) {
            //创建一个新的excel文档对象
            HSSFWorkbook hw = new HSSFWorkbook();
            //获得第一个sheet
            HSSFSheet hs = hw.createSheet();
            hs.setColumnWidth(0, 2 * 256);
            hs.setColumnWidth(1, 75 * 256);
            hs.setColumnWidth(2, 10 * 256);
            hs.setColumnWidth(3, 20 * 256);
            HSSFRow firstRow = hs.createRow(0);
            HSSFCell secondCell = firstRow.createCell(1);
            String time = df.format(new Date());
            secondCell.setCellValue(time);
            for (int i = 0; i < list.size(); i++) {
                //hs.setColumnWidth(i + 1, 100 * 256);
                //获得第一行
                HSSFRow hr = hs.createRow(i + 1);
                //获得第一个格子
                HSSFCell hc = hr.createCell(0);
                //获得第二个格子
                HSSFCell hc1 = hr.createCell(1);
                //获得第三个格子
                HSSFCell hc2 = hr.createCell(2);
                //获得第四个格子
                HSSFCell hc3 = hr.createCell(3);
                //设置值
                hc.setCellValue(i + 1);
                hc1.setCellValue(list.get(i).get("title").toString());
                hc2.setCellValue(list.get(i).get("time").toString());
                hc3.setCellValue(list.get(i).get("url").toString());
            }
            //写到文件中
            try {
                hw.write(new File("D:\\aaa\\66" + File.separator + df.format(new Date()) + "科大讯飞智慧wx.xls"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    hw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
