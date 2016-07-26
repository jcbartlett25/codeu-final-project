package com.codeu.wikisearch.web;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codeu.wikisearch.service.HelloWorldService;

@Controller
public class WelcomeController {

    private final Logger logger = LoggerFactory.getLogger(WelcomeController.class);
    private final HelloWorldService helloWorldService;

    @Autowired
    public WelcomeController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    // Index page routing
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Map<String, Object> model) {

        logger.debug("Homepage requested!");
        
        return "index";
    }

    // Random page routing
    @RequestMapping(value = "/random", method = RequestMethod.GET)
    public String random(Map<String, Object> model) {

        logger.debug("Randompage requested!");
        
        return "random";
    }
  
    @RequestMapping(value="/api/search", method = RequestMethod.GET)
    @ResponseBody()
    public ArrayList<String> search(@RequestParam("term") String term) throws IOException {

        logger.debug("path hit!");
        logger.debug("search term: " + term);
        
        ArrayList<String> urls = null;

        try {
            urls = helloWorldService.search(term);
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return urls;
    }


}