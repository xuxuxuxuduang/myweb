package com.paopao.web.pipeline.baidu;

import com.paopao.web.util.DateUtil;
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
 * 自定义pipline类(对比昨天)
 */
public class BaiduComparePipeline implements Pipeline {

    private String path = "D:\\aaa\\66";

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
            writeToExcel(list, entry.getKey());
        }
    }

    /**
     * 将数据写到excel表格里, 并与昨日做对比
     * @param list
     * @param key
     */
    public void writeToExcel(List<Map<String, Object>> list, String key){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = df.format(DateUtil.getBeforeDays(1));
        List<String> yesterdayList = readData(path + File.separator + yesterday + key + ".xls");
        if (list != null) {
            //创建一个新的excel文档对象
            HSSFWorkbook hw = new HSSFWorkbook();
            //获得第一个sheet
            HSSFSheet hs = hw.createSheet();
            hs.setColumnWidth(0, 3 * 256);
            hs.setColumnWidth(1, 75 * 256);
            hs.setColumnWidth(2, 20 * 256);
            hs.setColumnWidth(3, 80 * 256);
            HSSFRow firstRow = hs.createRow(0);
            HSSFCell secondCell = firstRow.createCell(1);
            String time = df.format(new Date());
            secondCell.setCellValue(time);
            for (int i = 0; i < list.size(); i++) {
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
                //如果是新增的  高亮展示
                if(!yesterdayList.contains(list.get(i).get("title").toString())){
                    HSSFCellStyle style = hw.createCellStyle();
                    style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
                    style.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    hc.setCellStyle(style);
                    hc1.setCellStyle(style);
                    hc2.setCellStyle(style);
                    hc3.setCellStyle(style);
                }
            }
            //写到文件中
            try {
                hw.write(new File(path + File.separator + df.format(new Date()) +  key + ".xls"));
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

    /**
     * 获得某个文件的数据
     * @param fileName
     * @return
     */
    public List<String> readData(String fileName){
        List<String> data = new ArrayList<>();
        File file = new File(fileName);
        if(file.exists()){
            try{
                HSSFWorkbook hw = new HSSFWorkbook(new FileInputStream(file));
                HSSFSheet sheetAt = hw.getSheetAt(0);
                for (int i = 0; i < sheetAt.getLastRowNum(); i++) {
                    //获得第i行
                    HSSFRow hr = sheetAt.getRow(i + 1);
                    //获得第二个格子
                    HSSFCell hc = hr.getCell(1);
                    data.add( hc.getStringCellValue());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return data;
    }
}
