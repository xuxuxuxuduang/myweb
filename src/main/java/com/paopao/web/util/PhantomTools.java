package com.paopao.web.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * @Description:根据网页地址转换成图片
 * @Author: admin
 * @CreateDate: 2018年6月22日
 */
@Component
@ConfigurationProperties(prefix="phantom")
@Data
public class PhantomTools {
    private String tempPath;// 图片保存目录
    private String BLANK;
    // 下面内容可以在配置文件中配置
    private String binPath;// 插件引入地址
    private String imageJsPath;// 截图js引入地址
    private String codeJsPath;//获取页面内容js地址

    // 执行cmd命令
    public String cmd(String imgagePath, String url, String jsPath) {
        return binPath + BLANK + jsPath + BLANK + url + BLANK + imgagePath;
    }
    //关闭命令
    public void close(Process process, BufferedReader bufferedReader) throws IOException {
        if (bufferedReader != null) {
            bufferedReader.close();
        }
        if (process != null) {
            process.destroy();
            process = null;
        }
    }
    /**
     * @param url
     * @throws IOException
     */
    public void printUrlScreen2jpg(String url) throws IOException{
        System.out.println(System.currentTimeMillis());
        String imgagePath = tempPath+"/"+System.currentTimeMillis()+".png";//图片路径
        //Java中使用Runtime和Process类运行外部程序
        Process process = Runtime.getRuntime().exec(cmd(imgagePath,url,imageJsPath));
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while ((reader.readLine()) != null) {
            close(process,reader);
        }
        System.out.println("success");
    }

    /**
     * 获得页面内容字符串
     * @param url
     * @return
     * @throws IOException
     */
    public String getAjaxCotnent(String url) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(cmd("",url,codeJsPath));//这里我的codes.js是保存在c盘下面的phantomjs目录
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sbf = new StringBuffer();
        String tmp = "";
        while((tmp = br.readLine())!=null){
            sbf.append(tmp);
        }
        return sbf.toString();
    }



}
