package com.codeu.wikisearch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

@Service
public class HelloWorldService {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldService.class);

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

        WikiSearch wikisearch = WikiSearch.search(term, index); 

        List<Entry<String, Integer>> results = wikisearch.getResults();
        wikisearch.print();

        ArrayList<String> urls = new ArrayList<String>();

        for (Entry<String, Integer> result : results) {
            urls.add(result.getKey());
        }

        //model.addObject("link1", "twitter.com");
        //model.addObject("link2", "facebook.com");
        
        return urls;
    }

}