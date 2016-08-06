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

// Word2Vec libraries
import org.canova.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;

/*
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
*/
import com.codeu.wikisearch.service.SearchService;
import com.codeu.wikisearch.service.Word2VecMaker;

@Controller
public class SearchController {

    private final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final SearchService searchService;
    //private Word2Vec vec;

    @Autowired
    public SearchController(SearchService searchService) throws Exception {
        this.searchService = searchService;
        //vec = Word2VecMaker.make("wiki_model.txt");
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

        // Get Word2Vec word vector
        /*Collection<String> wordvec = vec.wordsNearest(term, 5);
        System.out.println("printing nearest neighbors...");
        System.out.println(wordvec);*/

        try {
            /*if (!wordvec.isEmpty()) 
                urls = searchService.search(term, wordvec);
            else*/
                urls = searchService.search(term);
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return urls;
    }


}