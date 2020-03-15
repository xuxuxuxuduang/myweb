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
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.*;

import static com.paopao.web.util.ReptileUtil.replaceSymbol;

public class BaiduBiz extends AbsBaseBiz {

    private static String baseUrl = "https://www.baidu.com/s?tn=news&rtt=myRtt&bsst=1&cl=2&wd=searchKey&medium=0&pn=myPn";

    private String myRtt;

    private List<Map<String, String>> result;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100)
            .setDomain(".baidu.com")
            .setUserAgent(userAgent)
            .addHeader("Accept", "*/*")
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
            .addHeader("Referer", "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E5%9B%A0%E8%B5%9B%E9%9B%86%E5%9B%A2&oq=%25E5%259B%25A0%25E8%25B5%259B%25E9%259B%2586%25E5%259B%25A2&rsv_pq=89d6df00000ede92&rsv_t=df2aBFAdKYuHrHjKNuOoQoXylAGlbiZWfHpVkXGzE7KZtw1SdV9PzTmaNpg&rqlang=cn&rsv_enter=0&rsv_dl=tb")
            .addHeader("is_referer", "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E5%9B%A0%E8%B5%9B%E9%9B%86%E5%9B%A2&rsv_pq=a0ffe1190000aca6&rsv_t=ea57G2yAdcAtfdM9g1wE0Z1klvowhbYXxcdr3tOxzUxGBtPO7PMn0agj1eg&rqlang=cn&rsv_dl=tb&rsv_enter=1&rsv_sug3=16&rsv_sug1=10&rsv_sug7=100&rsv_sug2=0&inputT=26488&rsv_sug4=26488")
            .addHeader("is_xhr", "1")
            .addHeader("is_pbs", "%E5%9B%A0%E8%B5%9B%E9%9B%86%E5%9B%A2")
            .addHeader("X-Requested-With", "XMLHttpRequest")
            .addHeader("Connection", "keep-alive")
            .addHeader("Cookie", "BIDUPSID=3D9E299453D2F71504AD7ED688C96EFC; PSTM=1556449131; BD_UPN=12314753; ispeed_lsm=2; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BDUSS=kdSUFBFdHdPdTN3NXZSSFJYcEdzVWdYfjB6VW9GNFRCUTZXV0I4SjA0TTNoeVZlRVFBQUFBJCQAAAAAAAAAAAEAAACTGQeFbXlsaWZlNTQ5AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADf6~V03-v1dW; H_WISE_SIDS=139417_136722_100805_138441_113879_132549_139148_120197_138471_138233_138878_137978_132551_118879_118877_118848_118820_118803_138165_107319_138882_138844_139451_139284_139296_136862_138148_139174_136196_137105_139274_139400_139692_139520_133847_137734_137468_138565_131423_139397_139246_136537_110085_127969_139136_138614_139792_139160_139827_139882_139978_139513_128196_138312_138426_139732_139557_139677_139927_139221_139866; plus_cv=1::m:49a3f4a6; BAIDUID=CFB387FB2DB6A1589C4F89A74DA0E832:FG=1; yjs_js_security_passport=e3153aa97427f69e231b3de9714fd2ec96d40a4b_1577324813_js; delPer=0; BD_CK_SAM=1; PSINO=6; BD_HOME=1; BDRCVFR[feWj1Vr5u3D]=I67x6TjHwwYf0; BDSFRCVID=nUksJeCCxG3eg7cu33CAyQgqPqfpzuq1UpSA3J; H_BDCLCKID_SF=JJkO_D_atKvjDbTnMITHh-F-5fIX5-RLfKoPB-OF5lOTJh0Rbttb-nQyDPbChloM-GRD0tJLb4DaStJbLjbke4tX-NFeqTF8tU5; COOKIE_SESSION=31096_0_7_3_5_5_0_0_6_3_2_0_0_0_0_0_1577327891_0_1577358983%7C9%23178788_104_1577104476%7C9; H_PS_PSSID=1434_21114_30210_30284_22159; H_PS_645EC=430fbixboHp5pddhy%2BytOWEplQq0XZlkJAfj4IiwDV7gqr6rCTyQtO8V%2FhgIlsIUlMu%2F; WWW_ST=1577359097657");

    @Autowired
    private PhantomTools phantomTools;

    public BaiduBiz(int page, int pageSize, int count, String searchKey, String myRtt){
        super(baseUrl, page, pageSize, count, searchKey);
        this.myRtt = myRtt;
        this.result = new ArrayList<>();
    }

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        List<Selectable> all = html.css(".result").nodes();
        if(!CollectionUtils.isEmpty(all)){
            for (int i = 0 ; i < all.size(); i++) {
                Map<String, String> map = new HashMap<>();
                List<Selectable> titles = all.get(i).css("h3:has(em)").nodes();
                List<Selectable> times = all.get(i).css(".c-author").nodes();
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
        if(count > 0){
            String url = new String(baseUrl)
                    .replace("searchKey", searchKey).replace("myPn", String.valueOf((super.page - 1) * pageSize)).replace("myRtt", myRtt);;
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
         /* httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy("127.0.0.1",8888)));*/
        String url = new String(baseUrl)
                .replace("searchKey", searchKey).replace("myPn", String.valueOf((page - 1) * pageSize)).replace("myRtt", myRtt);
        page ++;
        count --;
        Spider.create(this)
                .setDownloader(httpClientDownloader)
                .addUrl(url)
                .addPipeline(new BaiduPipeline()).thread(1).run();
    }

    public static void main(String[] args) {
        BaiduBiz baiduBiz = new BaiduBiz(1, 10, 44, "科大讯飞", "4");
        baiduBiz.doSearch();
    }
}
