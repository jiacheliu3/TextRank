package edu.cityu.textrank

class WordCountRepo {
	public static HashSet<WordCount> wordCounts=new HashSet<>();
	public static void add(WordCount w){
		wordCounts.add(w);
	}
	public static Set<String> report(){
		//use a final WordCount object to generate the final integrated word count
		WordCount wc=new WordCount(new Date());
		wordCounts.each{
			wc.addKeywords(it.hotwords);
		}
		return wc.getKeywordSet();
	}
	public static ArrayList<String> getTopKeywords(int number){
		//use a final WordCount object to generate the final integrated word count
		WordCount wc=new WordCount(new Date());
		wordCounts.each{
			wc.addKeywords(it.hotwords);
		}
		return wc.getTopKeywords(number);
	}

}
