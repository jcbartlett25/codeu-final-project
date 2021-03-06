package com.codeu.wikisearch.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;


/**
 * Encapsulates a map from search term to frequency (count).
 * 
 * @author downey
 *
 */
public class TermCounter {
	
	private Map<String, Double> map;
	private String label;
	
	public TermCounter(String label) {
		this.label = label;
		this.map = new HashMap<String, Double>();
	}
	
	public String getLabel() {
		return label;
	}
	
	/**
	 * Returns the total of all counts.
	 * 
	 * @return
	 */
	public Double size() {
		Double total = 0.0;
		for (Double value: map.values()) {
			total += value;
		}
		return total;
	}

	/**
	 * Takes a collection of Elements and counts their words.
	 * 
	 * @param paragraphs
	 */
	public void processElements(Elements paragraphs) {
		for (Node node: paragraphs) {
			processTree(node);
		}
		calculateRelativeTermFrequency();
	}
	
	/**
	 * Finds TextNodes in a DOM tree and counts their words.
	 * 
	 * @param root
	 */
	public void processTree(Node root) {
		// NOTE: we could use select to find the TextNodes, but since
		// we already have a tree iterator, let's use it.
		for (Node node: new WikiNodeIterable(root)) {
			if (node instanceof TextNode) {
				processText(((TextNode) node).text());
			}
		}
	}

	/**
	 * Splits `text` into words and counts them.
	 * 
	 * @param text  The text to process.
	 */
	public void processText(String text) {
		// replace punctuation with spaces, convert to lower case, and split on whitespace
		String[] array = text.replaceAll("\\pP", " ").toLowerCase().split("\\s+");
		
		for (int i=0; i<array.length; i++) {
			String term = array[i];
			incrementTermCount(term);
		}
	}

	/**
	 * Increments the counter associated with `term`.
	 * 
	 * @param term
	 */
	public void incrementTermCount(String term) {
		// System.out.println(term);
		put(term, get(term) + 1.0);
	}

	private void calculateRelativeTermFrequency() {
		Double size = size();
		for (String key : keySet()) {
			map.put(key, get(key) / size);
			//System.out.println(get(key));
		}
	}

	/*
	private void calculateTfIdf() {

		Double totalSize = index.urlSetKeys().size();
		List<String> terms = index.termSet();
		List<String> urls = new ArrayList<String>();
		for (String term : terms) {

			Double termSize = index.getURLs.size();
			Double idf = Math.log(totalSize / termSize);
			put(term, get(term) * idf);
		}
	}
	*/

	/**
	 * Adds a term to the map with a given count.
	 * 
	 * @param term
	 * @param count
	 */
	public void put(String term, Double count) {
		map.put(term, count);
	}

	/**
	 * Returns the count associated with this term, or 0 if it is unseen.
	 * 
	 * @param term
	 * @return
	 */
	public Double get(String term) {
		Double count = map.get(term);
		return count == null ? 0.0 : count;
	}

	/**
	 * Returns the set of terms that have been counted.
	 * 
	 * @return
	 */
	public Set<String> keySet() {
		return map.keySet();
	}
	
	/**
	 * Print the terms and their counts in arbitrary order.
	 */
	public void printCounts() {
		for (String key: keySet()) {
			Double count = get(key);
			System.out.println(key + ", " + count);
		}
		System.out.println("Total of all counts = " + size());
	}

}
