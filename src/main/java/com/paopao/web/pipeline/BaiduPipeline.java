package com.paopao.web.pipeline;

import com.paopao.web.biz.BaiduBiz;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 自定义pipline类
 */
public class BaiduPipeline implements Pipeline {

    private String path = "D:\\aaa\\66";

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
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
                    hw.write(new File(path + File.separator + df.format(new Date()) +  entry.getKey() + ".xls"));
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
}
