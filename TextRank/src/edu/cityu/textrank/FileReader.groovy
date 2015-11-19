package edu.cityu.textrank
import java.text.SimpleDateFormat


class FileReader {
	public static void readFile(File file){
		String name=file.getName();
		println name;
		Date d = new SimpleDateFormat("yyyyMMdd").parse(name);
		//println d;
		ArrayList<String> items=file.readLines();
		WordCount wc=new WordCount(d);
		items.each{
			if(it=="" || it==null){
				//do nothing
			}
			else{
			Set<String> keywords=KeywordExtractor.extractKeywords(it);
			wc.addKeywords(keywords);
			WordCountRepo.add(wc);
			}
		}
		
	}
	public static void main(String[] args){
		readFile(new File("C:\\Users\\user\\sorted\\20120108"));
	}
}
