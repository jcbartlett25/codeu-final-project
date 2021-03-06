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

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.io.*;

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
import com.codeu.wikisearch.service.WikiFetcher;

@Controller
public class SearchController {

    private final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final SearchService searchService;
    private final HashSet<String> stopWords = new HashSet<String>();
    private final HashMap<String, ArrayList<String>> wordVec = new HashMap<String, ArrayList<String>>();
    @Autowired
    public SearchController(SearchService searchService) throws Exception {
        this.searchService = searchService;

        String line = null;
        try {
            // Process stopwords
            FileReader fr = new FileReader("stopwords.txt");
            BufferedReader reader = new BufferedReader(fr);
            while ((line = reader.readLine()) != null) {
                stopWords.add(line);
            }

            
            // Process Word2Vec
            fr = new FileReader("WordVectors2.txt");
            reader = new BufferedReader(fr);
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\\s+");
                String keyWord = split[0];
                ArrayList<String> relatedWords = new ArrayList<String>();
                for (int i = 1; i < split.length; i++) {
                    relatedWords.add(split[i]);
                }
                wordVec.put(keyWord, relatedWords);
            }
            

            reader.close();

        } catch(FileNotFoundException e) {
            logger.debug("Unable to open file");
            
        } catch(IOException e) {
            logger.debug("Error reading/writing file");
        }
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
            urls = searchService.search(term, wordVec, stopWords);
        }
        catch(Exception e) { //IOException
            e.printStackTrace();
        }

        // fetcher used to get pages from Wikipedia
        WikiFetcher wf = new WikiFetcher();

        /*
        for (int i = 0; i < 5; i++) {

            Elements paragraphs = wf.fetchWikipedia(urls.get(i));
            for(Node node : paragraphs) {
                logger.debug(node.toString());
            }
        }
        */
        return urls;
    }


}
