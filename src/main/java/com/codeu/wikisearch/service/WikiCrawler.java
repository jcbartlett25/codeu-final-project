package com.codeu.wikisearch.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import redis.clients.jedis.Jedis;


public class WikiCrawler {
    // keeps track of where we started
    private final String source;
    
    // the index where the results go
    private JedisIndex index;
    
    // queue of URLs to be indexed
    private Queue<String> queue = new LinkedList<String>();
    
    // fetcher used to get pages from Wikipedia
    final static WikiFetcher wf = new WikiFetcher();

    /**
     * Constructor.
     * 
     * @param source
     * @param index
     */
    public WikiCrawler(String source, JedisIndex index) {
        this.source = source;
        this.index = index;
        queue.offer(source);
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }
    /**
     * Returns the number of URLs in the queue.
     * 
     * @return
     */
    public int queueSize() {
        return queue.size();    
    }

    /**
     * Gets a URL from the queue and indexes it.
     * @param b 
     * 
     * @return Number of pages indexed.
     * @throws IOException
     */
    public String crawl(boolean testing) throws IOException {
        if (queue.isEmpty()) {
            return null;
        }
        String url = queue.poll();
        System.out.println("Crawling " + url);

        if (testing==false && index.isIndexed(url)) {
            System.out.println("Already indexed.");
            return null;
        }
        
        Elements paragraphs;
        if (testing) {
            paragraphs = wf.readWikipedia(url);
        } else {
            paragraphs = wf.fetchWikipedia(url);
        }
        index.indexPage(url, paragraphs);
        queueInternalLinks(paragraphs);     
        return url;
    }

    /**
     * Parses paragraphs and adds internal links to the queue.
     * 
     * @param paragraphs
     */
    // NOTE: absence of access level modifier means package-level
    void queueInternalLinks(Elements paragraphs) {
        for (Element paragraph: paragraphs) {

            queueInternalLinks(paragraph);
        }
    }

    /**
     * Parses a paragraph and adds internal links to the queue.
     * 
     * @param paragraph
     */
    private void queueInternalLinks(Element paragraph) {
        Elements elts = paragraph.select("a[href]");
        for (Element elt: elts) {
            String relURL = elt.attr("href");
            
            if (relURL.startsWith("/wiki/")) {
                String absURL = "https://en.wikipedia.org" + relURL;
                //System.out.println(absURL);
                queue.offer(absURL);
            }
        }
    }
    
}