package com.paopao.web.biz;

import com.paopao.web.biz.baseBiz.AbsBaseBiz;
import com.paopao.web.config.HttpClientDownloader;
import com.paopao.web.pipeline.BaiduPipeline;
import com.paopao.web.util.PhantomTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.paopao.web.util.ReptileUtil.replaceSymbol;

public class GubaBiz extends AbsBaseBiz {

    private static String baseUrl = "http://guba.eastmoney.com/list,searchKey,99_myPn.html";

    private List<Map<String, String>> result;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100)
            .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
            .addHeader("Accept-Encoding", "gzip, deflate")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
            .addHeader("Connection", "keep-alive")
            .addHeader("Cookie", "qgqp_b_id=5b0575ca970e60e541abe2e36e9446c0; emshistory=%5B%22%E8%93%9D%E8%89%B2%E5%85%89%E6%A0%87%22%5D; st_si=00953004336187; st_asi=delete; st_pvi=23252300191886; st_sp=2020-03-15%2019%3A50%3A14; st_inirUrl=https%3A%2F%2Fwww.baidu.com%2Flink; st_sn=10; st_psi=202003161252265-117001301474-9423184469")
            .addHeader("Host", "guba.eastmoney.com")
            .addHeader("Referer", "http://guba.eastmoney.com/list,002230,99_1.html")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36")
            .addHeader("X-Requested-With", "XMLHttpRequest");

    @Autowired
    private PhantomTools phantomTools;

    public GubaBiz(int page, int pageSize, int count, String searchKey){
        super(baseUrl, page, pageSize, count, searchKey);
        this.result = new ArrayList<>();
    }

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        List<Selectable> all = html.css(".articleh").nodes();
        if(!CollectionUtils.isEmpty(all)){
            for (int i = 0 ; i < all.size(); i++) {
                Map<String, String> map = new HashMap<>();
                List<Selectable> titles = all.get(i).css(".l3").nodes();
                List<Selectable> times = all.get(i).css(".l5").nodes();
                if(!CollectionUtils.isEmpty(titles) && !CollectionUtils.isEmpty(times)){
                    Selectable title = titles.get(0);
                    Selectable time = times.get(0);
                    map.put("url", title.links().toString() != null ? title.links().toString() : "暂无");
                    map.put("title", replaceSymbol(title.toString() != null ? title.toString() : "暂无").trim());
                    String timeStr = replaceSymbol(time.toString() != null ? time.toString() : "暂无").trim();
                    timeStr = timeStr.substring(timeStr.lastIndexOf(";") + 1, timeStr.length() - 5);
                    map.put("time", timeStr);
                    result.add(map);
                }
            }
        }
        try{
            Thread.sleep(5000);
        }catch (Exception e) {

        }
        if(count > 0){
            String url = new String(baseUrl)
                    .replace("searchKey", searchKey).replace("myPn", String.valueOf(super.page));
            page.addTargetRequest(url);
            super.page ++;
            count --;
        }else{
            page.putField(searchKey, result);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void doSearch() {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        /*  httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy("211.147.226.4",8118)));*/
        String url = new String(baseUrl)
                .replace("searchKey", searchKey).replace("myPn", String.valueOf(page));
        page ++;
        count --;
        Spider.create(this)
                .setDownloader(httpClientDownloader)
                .addUrl(url)
                .addPipeline(new BaiduPipeline()).thread(1).run();
    }

    public static void main(String[] args) {
        GubaBiz baiduBiz = new GubaBiz(1, 10, 11, "002230");
        baiduBiz.doSearch();
    }
}
