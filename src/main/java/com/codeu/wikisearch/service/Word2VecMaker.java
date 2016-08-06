package com.codeu.wikisearch.service;

import org.canova.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
//import org.deeplearning4j.ui.UiServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by agibsonccc on 10/9/14.
 */
public class Word2VecMaker {

    public static Word2Vec make(String filename) throws Exception {

        /*String filePath = new ClassPathResource("wikiraw.txt").getFile().getAbsolutePath();

        //log.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        SentenceIterator iter = new BasicLineIterator(filePath);
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        //log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        //log.info("Fitting Word2Vec model....");
        vec.fit();

        //log.info("Writing word vectors to text file....");
        // Write word vectors
        WordVectorSerializer.writeWordVectors(vec, "wiki_wordvec.txt");
        
        // Save model
        //log.info("Save vectors....");
        WordVectorSerializer.writeFullModel(vec, "wiki_model.txt"); */
        
        // Load model
        Word2Vec vec = WordVectorSerializer.loadFullModel(filename);
        Collection<String> lst;
        Scanner in = new Scanner(System.in);
        boolean again = true;
        while (again) {
        	boolean invalid = false;
        	do {
        		System.out.println("Enter 1 to find single word's vector.");
        		System.out.println("Enter 2 to perform a word operation.");    	
        		String option = in.next();
        		if (option.equals("1")) {
        			System.out.println("Enter a word: ");
        			String word = in.next();
        			System.out.println("Enter size of output vector: ");
        			int size = in.nextInt();
        			lst = vec.wordsNearest(word, size);
        			System.out.println(lst);
        		} else if (option.equals("2")) {
        			List<String> pos = new LinkedList<String>();
        			List<String> neg = new LinkedList<String>();
        			String word;
        			do {
        				System.out.println("Enter words to add. Enter ':q' when finished.");
        				word = in.next();
        				if (!word.equals(":q")) {
        					pos.add(word);
        				}
        			} while (!word.equals(":q"));
        			
        			do {
        				System.out.println("Enter words to subtract. Enter ':q' when finished.");
        				word = in.next();
        				if (!word.equals(":q")) {
        					neg.add(word);
        				}
        			} while (!word.equals(":q"));
        			System.out.println("Enter size of output vector: ");
        			int size = in.nextInt();
        			lst = vec.wordsNearest(pos, neg, size);
        			System.out.println(lst);
        		} else { 
        			System.out.println("Invalid input.");
        			invalid = true; 
        		}
        	} while (invalid);
        	
        	System.out.println("Would you like to search another word? (y/n)");
        	String answer = in.next();
        	if (answer.equals("n")) {
        		again  = false;
        	}
        }
        in.close();

        return vec;
    }
}
