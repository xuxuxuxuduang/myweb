package com.paopao.web.biz.baseBiz;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import us.codecraft.webmagic.processor.PageProcessor;

public abstract class AbsBaseBiz implements IbaseBiz, PageProcessor {

    protected String baseUrl;

    protected int page;

    protected int pageSize;

    protected int count;

    protected String searchKey;

    public AbsBaseBiz(){}

    public AbsBaseBiz(String baseUrl, int page, int pageSize, int count, String searchKey){
        this.baseUrl = baseUrl;
        this.page = page;
        this.pageSize = pageSize;
        this.count = count;
        this.searchKey = searchKey;
    }

    public void initPhantomDriver(){
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
    }
}
