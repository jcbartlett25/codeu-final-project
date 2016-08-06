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
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

@Service
public class SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    public ArrayList<String> search(String term) throws IOException {

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

        // crawling
        //index(term, index);
        
        ArrayList<String> urls = search(term, index);

        return urls;
    }

    /*// Called when there is word2vec associated with search term
    public ArrayList<String> search(String term, Collection<String> wordvec) throws IOException {
        Jedis jedis;
        JedisIndex index = null;

        try {
            jedis = JedisMaker.make();
            index = new JedisIndex(jedis);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        WikiSearch search1 = WikiSearch.search(term, index);
        WikiSearch union = null;
        for (String word : wordvec) {
            WikiSearch search2 = WikiSearch.search(word, index);
            union = search1.or(search2);
        }

        List<Entry<String, Double>> results = union.getResults();
        union.print();

        ArrayList<String> urls = new ArrayList<String>();
        for (Entry<String, Double> result : results) {
            urls.add(result.getKey());
        }

        return urls;
    }*/

    private ArrayList<String> search(String term, JedisIndex index) throws IOException {

        // fetcher used to get pages from Wikipedia
        final WikiFetcher wf = new WikiFetcher();

        WikiSearch wikisearch = WikiSearch.search(term, index); 

        List<Entry<String, Double>> results = wikisearch.getResults();
        wikisearch.print();
        

        ArrayList<String> urls = new ArrayList<String>();

        for (Entry<String, Double> result : results) {
            urls.add(result.getKey());
        }

        return urls;
    }

    public ArrayList<String> index(String url, JedisIndex index) throws IOException {
        
        // fetcher used to get pages from Wikipedia
        final WikiFetcher wf = new WikiFetcher();

        WikiCrawler wc = new WikiCrawler(url, index);
        
        // for testing purposes, load up the queue
        Elements paragraphs = wf.fetchWikipedia(url);
        wc.queueInternalLinks(paragraphs);

        ArrayList<String> urls = new ArrayList<String>();

        // loop until we index a new page
        String res;
        int i = 1;
        do {
            res = wc.crawl(false);
            i++;
        } while (!wc.isQueueEmpty() && i<100);

        return new ArrayList<String>();
    }

}