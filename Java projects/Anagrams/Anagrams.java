package edu.stevens.cs570.assignments;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Anagrams {
	/**
	 * Data Fields for Anagrams
	 */
	final Integer[] primes = {2 , 3 , 5 , 7 , 11 , 13 , 17 , 19 , 23 , 29 , 31 , 37 , 41 , 43 , 47 , 53 , 59 , 61 , 67 , 71 , 73 , 79 , 83 , 89 , 97 , 101}; 
	Map<Character,Integer> letterTable; 
	Map<Long,ArrayList<String>> anagramTable;
	
	/**
	 * Constructor
	 */
	public Anagrams(){
		this.letterTable = new HashMap<Character, Integer>();
		this.anagramTable = new HashMap<Long, ArrayList<String>>();
		builtLetterTable();	
	}
	
	/**
	 * Built the hash table letterTable
	 */
	private void builtLetterTable() {
		Character[] alphabetCharacters = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
		for (int i = 0; i < primes.length; i++) {
			letterTable.put(alphabetCharacters[i], primes[i]);
		}
	}
		
	/**
	 * Construct the hash code(key) of strings(words),this will be used as key in "anagramTabe".
	 * @param s The given word
	 * @return Hash code(key) of the word
	 */
	private long myHashCode(String s) {		
		long key = 1;
		for (int i = 0; i < s.length(); i++) {
			Character character = s.charAt(i);
			key *= letterTable.get(character);	
		}
		return key;	
	}
	
	/**
	 * Computer the key of given word,add this word to the anagramTable
	 * @param s The given word
	 */
	private void addWord(String s){
		long key = myHashCode(s);
		if (anagramTable.containsKey(key)) {
			anagramTable.get(key).add(s);	
			anagramTable.replace(key, anagramTable.get(key));//table updated
		}else {
			ArrayList<String> wordList = new ArrayList<String>();
			wordList.add(s);
			anagramTable.put(key,wordList);
		}
	}
		
	/**
	 * Open the file and build the anagramTable
	 * @param s The name of a text file
	 * @throws IOException
	 */
	public void processFile(String s) throws IOException {
		FileInputStream fstream = new FileInputStream(s);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fstream)); 
		String strLine;
		while ((strLine = bufferedReader.readLine()) != null) { 
			addWord(strLine);
		} 
		bufferedReader.close();
	}
	
	/**
	 * Get the entry with largest number of anagrams
	 * @return A list of entries that have the largest number of anagrams
	 */
	public ArrayList<Map.Entry<Long,ArrayList<String>>> getMaxEntries(){
		ArrayList<Map.Entry<Long,ArrayList<String>>> entryArrayList = new ArrayList<Map.Entry<Long,ArrayList<String>>>();//entryArrayList中装的是entry
		int max = 0;
		for (Map.Entry<Long,ArrayList<String>> entry: anagramTable.entrySet()) {
			if (entry.getValue().size() > max) {
				entryArrayList.clear();
				entryArrayList.add(entry);
				max = entry.getValue().size();
			} else {
				if (entry.getValue().size() == max) {
					entryArrayList.add(entry);
				}
			}
		}
		return entryArrayList;
	}
	
	/**
	 * test
	 * @param args
	 */
	public static void main(String[] args) {
		Anagrams a = new Anagrams ();
		final long startTime = System.nanoTime(); 
		try {
			a.processFile("words_alpha.txt"); 
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ArrayList<Map.Entry<Long,ArrayList<String>>> maxEntries = a.getMaxEntries(); 
		int length = maxEntries.get(0).getValue().size();
		final long estimatedTime = System.nanoTime() - startTime;
		final double seconds = ((double) estimatedTime/1000000000); 
		long key = maxEntries.get(0).getKey();
		System.out.println(" Elapsed Time: "+ seconds);
		System.out.println(" Key of max anagrams: " + key);
		for(int i = 0;i < maxEntries.size(); i ++){
		    System.out.println(" List of max anagrams: " + maxEntries.get(i).getValue());
		}
		System.out.println(" Length of list of max anagrams: "+ length);
	}
}
