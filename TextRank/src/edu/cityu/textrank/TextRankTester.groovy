package edu.cityu.textrank

class TextRankTester {
	static ArrayList<Double> recallList;
	static ArrayList<Double> precisionList;
	static ArrayList<Double> fScoreList;
	static{
		init();
	}
	public static void init(){
		recallList=new ArrayList<>();
		precisionList=new ArrayList<>();
		fScoreList=new ArrayList<>();
	}
	public static double fScore(double precision,double recall){
		double denom=precision+recall;
		if(denom==0){
			return 0;
		}else{
			return 2*(precision*recall)/denom;
		}
	}
	public static void checkAccuracy(String content,Collection<String> answer,int windowSize){
		int length=content.length();
		int siz=Math.round(Math.sqrt(length));
		Set<String> keywords=KeywordExtractor.extractKeywords(content, windowSize, siz);
		double truePos=0;
		double falPos=0;
		double trueNeg=0;
		double falNeg=0;
		
		keywords.each{it->
			answer.each{a->
				if(a==it){
					truePos++;
				}
				else if(a.contains(it)||it.contains(a)){
					//println "well this may also count";
					truePos=truePos+0.5;
				}
					
			}
		}
		println "true positive is ${truePos}";
		double recall=truePos/(double)answer.size();
		double precision=truePos/(double)keywords.size();
		double fScore=fScore(precision,recall);
		recallList.add(recall);
		precisionList.add(precision);
		fScoreList.add(fScore);
		println("Recall is ${recall}, precision is ${precision}, F-score is ${fScore}");
	}
	public static void reportAccuracy(){
		int siz=recallList.size();
		println "Now ${siz} samples";
		double recallAvg=recallList.sum()/siz;
		double precisionAvg=precisionList.sum()/siz;
		double fScoreAvg=fScoreList.sum()/siz;
		println """
			Size of test: ${siz}
			Average Recall: ${recallAvg}
			Average Precision: ${precisionAvg}
			Average F-score: ${fScoreAvg}
"""
	}
	public static void clearReport(){
		recallList.size=0;
		precisionList.size=0;
		fScoreList.size=0;
	}
	public static void readTests(String path){
		
		File folder=new File(path);
		if(!folder.isDirectory()){
			println "Please place all samples in a folder and pass it in!";
			return;
		}
		def windowSizeList=[2,3,4,5,6,10];
		windowSizeList.each{s->
			println("Now the window size is ${s}")
		
			folder.eachFile {
				println "Processing file ${it.name}";
				ArrayList<String> text=it.readLines();
				String content=text[0];
				ArrayList<String> keywords=text[1..text.size()-1];
				checkAccuracy(content,keywords,s);
			}
			reportAccuracy();
			clearReport();
		}
	}
	public static void main(String[] args){
		String path="C:\\Users\\user\\Documents\\samples";
		readTests(path);
		
	}
}
