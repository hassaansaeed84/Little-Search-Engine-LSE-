package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		int freq=1;
		HashMap<String,Occurrence> mp=new HashMap<String, Occurrence>();		
		Scanner sc= new Scanner(new File(docFile));
		while(sc.hasNext()) {
			String a =sc.next();
			if(getKeyword(a)!=null)
			{
				if(mp.containsKey(getKeyword(a)))
				{
					mp.get(getKeyword(a)).frequency++;
					continue;
				}
				else 
				{
					Occurrence o=new Occurrence(docFile, freq);
					mp.put(getKeyword(a), o);
				}
			}
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return mp;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws)
	{
		/** COMPLETE THIS METHOD **/
		for (String keyword : kws.keySet())
		{
			ArrayList<Occurrence> AO = new ArrayList<Occurrence>();

			if (keywordsIndex.containsKey(keyword)==true)
			{
				AO= keywordsIndex.get(keyword);
				Occurrence a=kws.get(keyword);
				AO.add(a);
				insertLastOccurrence(AO);
				keywordsIndex.put(keyword, AO);
			}
			else 
			{
				Occurrence a=kws.get(keyword);
				AO.add(a);
				insertLastOccurrence(AO);
				keywordsIndex.put(keyword, AO);
			}
		}
//		ArrayList<Occurrence> O=new ArrayList<Occurrence>();
//		int i=0;
//		for(String kw : kws.keySet()) {
//			O.add(kws.get(i));
//			insertLastOccurrence(O);
//			keywordsIndex.put(kw, O);
//			if(i+1<=kws.size()-1) 
//			{
//				i++;
//			}
//		}
	}
	
	/**
	 * Given a word, returns it askeyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word)
	{
		/** COMPLETE THIS METHOD **/
		String Word=word.toLowerCase();
		if(noiseWords.contains(Word))
		{
			return null;
		}
		if(Word.length()>1) {
			while(Word.charAt(Word.length()-1)=='.' || Word.charAt(Word.length()-1)==',' 
					|| Word.charAt(Word.length()-1)=='?' || Word.charAt(Word.length()-1)==':' 
					|| Word.charAt(Word.length()-1)==';'|| Word.charAt(Word.length()-1)=='!')
				{
					Word=Word.substring(0, Word.length()-1);
				}
		}
		
		for(int i=0; i<Word.length();i++)
		{
			if(Character.isLetter(Word.charAt(i))==false)
			{
			return null;
			}
		}
		return Word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		if(occs.size()==1) {
			return null;
		}
		ArrayList<Integer> o= new ArrayList<Integer>();
		int f=occs.get(occs.size()-1).frequency;
		int l=0;
		int r =occs.size()-2;
		int m=0;
		while(l<=r) {
			m=l+r/2;
			o.add(m);
			if(f==occs.get(m).frequency) {
				break;
			}
			else if(f<occs.get(m).frequency){
				l=m+1;
				
			}
			else {
				r=m-1;
			}
		}
		if(f>occs.get(m).frequency) {
			occs.add(m, occs.get(occs.size()-1));
			occs.remove(occs.size()-1);
		}
		else {
			occs.add(m+1, occs.get(occs.size()-1));
			occs.remove(occs.size()-1);
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return o;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		kw1=kw1.toLowerCase();
		kw2=kw2.toLowerCase();
		boolean contain=false;
		boolean contain2=false;
		ArrayList<String> s= new ArrayList<String>();
		ArrayList<Occurrence> First=new ArrayList<Occurrence>();
		ArrayList<Occurrence> Second=new ArrayList<Occurrence>();
		ArrayList<Occurrence> Final= new ArrayList<Occurrence>();
		if(keywordsIndex.containsKey(kw1)==true && noiseWords.contains(kw1)==false) {
			contain=true;
			First=keywordsIndex.get(kw1);
		}
		if(keywordsIndex.containsKey(kw2)==true && noiseWords.contains(kw2)==false) {
			contain2=true;
			Second=keywordsIndex.get(kw2);
		}
		if(contain==true ||contain2==true) 
		{
			if(contain==true && contain2!=true)
			{
				for(int i=0; i<First.size();i++)
				{
					if(s.size()<5) {
						s.add(First.get(i).document);
					}
				}
				return s;
			}
			else if(contain2==true && contain==false) 
			{
				for(int i=0; i<Second.size();i++)
				{
					if(s.size()<5) 
					{
						s.add(Second.get(i).document);
					}
				}
				return s;
			}
			else 
			{
				for(int i=0; i<First.size();i++)
				{
						Final.add(First.get(i));
				}
				for(int i=0; i<Second.size();i++)
				{

						Final.add(Second.get(i));
				}
			}
		}
			for(int x=0; x<Final.size();x++) 
			{
				for(int y=x+1;y<Final.size();y++) 
				{
					if(Final.get(x).document==Final.get(y).document)
					{
						if(Final.get(x).frequency<Final.get(y).frequency) 
						{
							Final.remove(x);
						}
						else 
						{
							Final.remove(y);
						}
					}
			}
			}
			for(int x=0; x<Final.size();x++) 
			{
				for(int y=x+1;y<Final.size();y++) 
				{
					if(Final.get(x).frequency<Final.get(y).frequency)
					{
						Occurrence temp=Final.get(x);
						Final.set(x, Final.get(y));
						Final.set(y, temp);
					}
				}
			}
			for(int i=0; i<Final.size() ;i++) {
				s.add(Final.get(i).document);
			}
			while(s.size()>=6) {
				s.remove(s.size()-1);
			}
			return s;
}
}