package com.codeu.wikisearch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;

import org.springframework.web.servlet.ModelAndView;

@Service
public class HelloWorldService {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldService.class);

    public ModelAndView search(String term) {

        ModelAndView model = new ModelAndView();
        model.setViewName("random");

        model.addObject("link1", "twitter.com");
        model.addObject("link2", "facebook.com");
        
        return model;
    }

}