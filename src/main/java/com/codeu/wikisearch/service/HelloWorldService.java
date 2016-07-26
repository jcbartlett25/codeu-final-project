package com.codeu.wikisearch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;

import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

@Service
public class HelloWorldService {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldService.class);

    public ArrayList<String> search(String term) throws IOException {

        // fetcher used to get pages from Wikipedia
        final WikiFetcher wf = new WikiFetcher();
        Jedis jedis;
        JedisIndex index = null;
        try {
            // make a JedisIndex
            jedis = JedisMaker.make();
            index = new JedisIndex(jedis);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        /*     
        String source = "https://en.wikipedia.org/wiki/Joel_Stratton";
        WikiCrawler wc = new WikiCrawler(source, index);
        
        // for testing purposes, load up the queue
        Elements paragraphs = wf.fetchWikipedia(source);
        wc.queueInternalLinks(paragraphs);

        ArrayList<String> urls = new ArrayList<String>();

        // loop until we index a new page
        String res;
        int i = 1;
        do {
            res = wc.crawl(false);
            i++;
        } while (!wc.isQueueEmpty() && i<20);
        */
        
        
        WikiSearch wikisearch = WikiSearch.search(term, index); 

        List<Entry<String, Integer>> results = wikisearch.getResults();
        wikisearch.print();
        

        ArrayList<String> urls = new ArrayList<String>();

        for (Entry<String, Integer> result : results) {
            urls.add(result.getKey());
        }

        return urls;
    }

}