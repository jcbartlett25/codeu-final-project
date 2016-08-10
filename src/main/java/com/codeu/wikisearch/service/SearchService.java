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

import org.springframework.web.servlet.*; //ModelandView
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;

// Word2Vec libraries
import org.canova.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import com.codeu.wikisearch.service.Word2VecMaker;

@Service
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    public ArrayList<String> search(String term, 
                                    HashMap<String, ArrayList<String>> wordVec, 
                                    HashSet<String> stopWords) throws Exception {

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

        String[] separatedWords = term.split(" ");

        // crawling
        if (separatedWords[0].equals("crawl:")) {
            int n = Integer.parseInt(separatedWords[2]);
            index(separatedWords[1], n, index);
        }
        ArrayList<String> urls = search(separatedWords, wordVec, stopWords, index);

        return urls;
    }

    private ArrayList<String> search(String[] terms, 
                                     HashMap<String, ArrayList<String>> wordVec,
                                     HashSet<String> stopWords, JedisIndex index) throws IOException {

        ArrayList<String> urls = new ArrayList<String>();
        WikiSearch wikisearch = null;

        if (terms.length == 1 && !stopWords.contains(terms[0])) {
            wikisearch = processWordVec(terms[0], wordVec, stopWords, index);
        } else {

            // Find intersection of all the words in search query
            wikisearch = processWordVec(terms[0], wordVec, stopWords, index);
            for (int i = 1; i < terms.length; i++) {
                if (!stopWords.contains(terms[i])) {
                    WikiSearch search = processWordVec(terms[i], wordVec, stopWords, index);
                    wikisearch = wikisearch.and(search);
                }
            }

            // If intersection of search words is empty, find union
            if (wikisearch.getResults().size() == 0) {
                wikisearch = processWordVec(terms[0], wordVec, stopWords, index);
                for (int i = 1; i < terms.length; i++) {
                    if (!stopWords.contains(terms[i])) {
                        WikiSearch search = processWordVec(terms[i], wordVec, stopWords, index);
                        wikisearch = wikisearch.or(search);
                    }
                }
            }
        }

        // If no match is found, return empty arraylist
        if (wikisearch == null || wikisearch.getResults().size() == 0) {
            return urls;
        }
        List<Entry<String, Double>> results = wikisearch.getResults();
        //wikisearch.print();

        for (Entry<String, Double> result : results) {
            urls.add(result.getKey());
        }

        return urls;
    }

    private WikiSearch processWordVec(String term, 
                                HashMap<String, ArrayList<String>> wordVec,
                                HashSet<String> stopWords, JedisIndex index) {
        
        WikiSearch search = WikiSearch.search(term, index);

        ArrayList<String> relatedWords = wordVec.get(term);
        if (relatedWords != null) {
            WikiSearch union = null;
            for (String word : relatedWords) {
                if (!stopWords.contains(word)) {
                    WikiSearch search2 = WikiSearch.search(word, index);
                    union = search.or(search2);
                    search = union;
                }
            }
        }
        return search;
    }


    public ArrayList<String> index(String url, int n, JedisIndex index) throws IOException {
        
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
        } while (!wc.isQueueEmpty() && i < n);

        return new ArrayList<String>();
    }

}
