package edu.cityu.textrank
import java.text.SimpleDateFormat


class TrendAnalysis {
	public static String path;
	public static void setPath(String p){
		path=p;
		println "Set path to ${path}";
	}
	//for each day count the keywords
	public static Set<String> allPeriodKeywordStudy(){
		println "Path is ${path}"
		File rootFolder=new File(path);
//		if(!rootFolder.isDirectory()){
//			println "Base path is not a folder!";
//			return;
//		}
		rootFolder.eachFile{
			FileReader.readFile(it);
		}
		Set<String> finalKeywords=WordCountRepo.report();
		return finalKeywords;
	}	
	public static Set<String> allPeriodKeywordStudy(int number){
		println "Path is ${path}"
		File rootFolder=new File(path);
//		if(!rootFolder.isDirectory()){
//			println "Base path is not a folder!";
//			return;
//		}
		rootFolder.eachFile{
			FileReader.readFile(it);
		}
		Set<String> finalKeywords=WordCountRepo.getTopKeywords(number);
		return finalKeywords;
	}
	//read user input and generate keywords
	public static Set<String> certainPeriodKeywordStudy(Date startDate,Date endDate,int number){
		File rootFolder=new File(path);
		if(!rootFolder.isDirectory()){
			println "Base path is not a folder!";
			return;
		}
		rootFolder.eachFile{
			String fileName=it.getName();
			Date fileDate=new SimpleDateFormat("yyyyMMdd").parse(fileName);
			if(fileDate.before(endDate)&&fileDate.after(startDate)){
				FileReader.readFile(it);
			}
		}
		Set<String> finalKeywords=WordCountRepo.getTopKeywords(number);
		return finalKeywords;
	}
	//return a set of keywords if successful, null if not
	public static interactiveStudy(){
		println "Please enter the date interval you want in yyyymmdd-yyyymmdd format(smaller date first):";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String inputString = reader.readLine();
		reader.close();
		String[] dates=inputString.split('-');
		try{
			//check validity of string
			String start=dates[0];
			String end=dates[1];
			if(start.matches('\\d{8}')&&end.matches('\\d{8}')){
				println "Input is valid, start processing...";
				Date s=	new SimpleDateFormat("yyyyMMdd").parse(start);
				Date e=	new SimpleDateFormat("yyyyMMdd").parse(end);
				if(e.before(s)){
					println "Invalid output! The end date shouldn't be before the start date!";
					return;
				}
				//use 20 keywords first
				Set<String> results=certainPeriodKeywordStudy(s,e,20);
				println """
Your request has been completed. The final result keywords are: 
${results}
"""
				return results;
			}else{
				println "Input is not valid! The format should be yyyymmdd-yyyymmdd"
			}
		}catch(Exception e){
			println "An error occurred when processing your request:";
			e.printStackTrace();
		}
	}
	//return a set of keywords if successful, null if not
	public static interactiveStudy(String query){
		String[] dates=query.split('-');
		try{
			//check validity of string
			String start=dates[0];
			String end=dates[1];
			if(start.matches('\\d{8}')&&end.matches('\\d{8}')){
				println "Input is valid, start processing...";
				Date s=	new SimpleDateFormat("yyyyMMdd").parse(start);
				Date e=	new SimpleDateFormat("yyyyMMdd").parse(end);
				if(e.before(s)){
					println "Invalid output! The end date shouldn't be before the start date!";
					return;
				}
				//use 20 keywords first
				Set<String> results=certainPeriodKeywordStudy(s,e,20);
				println """
Your request has been completed. The final result keywords are: 
${results}
"""
				return results;
			}else{
				println "Input is not valid! The format should be yyyymmdd-yyyymmdd"
			}
		}catch(Exception e){
			println "An error occurred when processing your request:";
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		int s=args.length;
		if(s<4){
			println("""
1st param: file path, 
2nd param: model base, 
3rd param: keyword size,
4th param: -a for all time period keyword extraction, 
			-d yyyymmdd-yyyymmdd for keyword extraction in a certain time period, the start time must be before end time""");
			return;
		}else{
			setPath(args[0]);
			String model=args[1];
			String siz=args[2];
			FnlpManager.setBase(model);
			String opt=args[3];
			def result;
			if(opt=="-a"){
				result=allPeriodKeywordStudy(Integer.parseInt(siz));
				println "Final result is \n${result}"
			}else if(opt=='-d'){
				result=interactiveStudy(args[4]);
			}
			
		}
	}
}
