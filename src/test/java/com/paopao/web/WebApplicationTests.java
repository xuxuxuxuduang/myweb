package com.paopao.web;

import com.paopao.web.biz.BaiduBiz;
import com.paopao.web.util.PhantomTools;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class WebApplicationTests {

    @Autowired
    PhantomTools tools;
    @Autowired
    BaiduBiz baiduBiz;

   /* @Test
    void contextLoads() throws  Exception{
        baiduBiz.doSearch("因赛集团", 3, false);
        *//*System.out.println(tools.getAjaxCotnent("http://www.xicidaili.com/"));*//*
       *//* System.setProperty("phantomjs.binary.path", "D:\\aaa\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.phantomjs();
        //此处可以设置一些desiredCapabilities的属性（浏览器的头信息）
        PhantomJSDriver driver = new PhantomJSDriver(desiredCapabilities);
        //打开sosobtc.com页面
        driver.get("https://www.baidu.com/");
        String title = driver.getTitle();
        System.out.println(title);*//*
    }

    @Test
    public void test() throws Exception{
        //设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", true);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        //js支持
        dcaps.setJavascriptEnabled(true);
        //驱动支持（第二参数表明的是你的phantomjs引擎所在的路径）
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "D:\\aaa\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        //创建无界面浏览器对象
        PhantomJSDriver driver = new PhantomJSDriver(dcaps);

        //设置隐性等待（作用于全局）
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        long start = System.currentTimeMillis();
        //打开页面
        driver.get("http://saatchi.com/en-us/news/guillermo-vega-joins-saatchi-saatchi-london-as-chief-creative-officer/");
        Thread.sleep(30 * 1000);
        JavascriptExecutor js = driver;
        for (int i = 0; i < 33; i++) {
            System.out.println(i);
            js.executeScript("window.scrollBy(0,1000)");
            //睡眠10s等js加载完成
            Thread.sleep(5 * 100);
        }
        //指定了OutputType.FILE做为参数传递给getScreenshotAs()方法，其含义是将截取的屏幕以文件形式返回。
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        Thread.sleep(3000);
        //利用FileUtils工具类的copyFile()方法保存getScreenshotAs()返回的文件对象
        FileUtils.copyFile(srcFile, new File("D:\\aaa\\juejin-01.png"));
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + " 毫秒");
        driver.close();
    }*/

}
