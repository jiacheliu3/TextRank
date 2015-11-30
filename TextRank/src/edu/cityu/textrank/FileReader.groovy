package edu.cityu.textrank
import java.text.SimpleDateFormat


class FileReader {
	public static void readFile(File file){
		String name=file.getName();
		println "Processing file: "+name;
		Date d = new SimpleDateFormat("yyyyMMdd").parse(name);
		//println d;
		ArrayList<String> items=file.readLines();
		WordCount wc=new WordCount(d);
		items.each{
			if(it=="" || it==null){
				//do nothing
			}
			else{
				if(it.length()<=10){}
				else{
					String time=it.substring(0,10).trim();
					String line=it.substring(10);
					Set<String> keywords=KeywordExtractor.extractKeywords(line);
					wc.addKeywords(keywords);
					
				}
			}
		}
		WordCountRepo.add(wc);
		
	}
	public static void readFile(File file,Date startDate,Date endDate){
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
				if(it.length()<=10){}
				else{
					String time=it.substring(0,10).trim();
					String line=it.substring(10);
					
					Date lineDate=new SimpleDateFormat("yyyyMMddhh").parse(time);
					
					if(lineDate.before(endDate)&&lineDate.after(startDate)){
						Set<String> keywords=KeywordExtractor.extractKeywords(line);
						wc.addKeywords(keywords);
						
					}else{
						//println "Not in the desired time range. Dispose this line."
					}
				}
			}
		}
		WordCountRepo.add(wc);
		
	}
//	public static void main(String[] args){
//		readFile(new File("C:\\Users\\user\\sorted\\20120108"));
//	}
}
