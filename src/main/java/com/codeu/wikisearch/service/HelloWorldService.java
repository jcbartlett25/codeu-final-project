package com.codeu.wikisearch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;

import java.io.IOException;

import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

@Service
public class HelloWorldService {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldService.class);

    public ModelAndView search(String term) throws IOException {

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

        wikisearch.print();

        ModelAndView model = new ModelAndView();
        model.setViewName("random");

        //model.addObject("link1", "twitter.com");
        //model.addObject("link2", "facebook.com");
        
        return model;
    }

}