package com.paopao.web.processor;

import com.paopao.web.config.HttpClientDownloader;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 自动爬取百度
 */
public class BaiduProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);


    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
         /* httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy("127.0.0.1",8888)));*/
        Spider.create(new BaiduProcessor())
                .setDownloader(httpClientDownloader)
                .addUrl("")
                .addPipeline(new ConsolePipeline()).thread(1).run();
    }
}
