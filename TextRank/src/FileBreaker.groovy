import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class FileBreaker {
	static String path="C:\\sorted\\";
	
	public static Pattern id;
	public static Pattern ws;
	public static Pattern block;
	public static Pattern fp;
	public static Pattern image;
	
	static{
		init();
	}
	public static void init(){
		id = Pattern.compile("\\d+\\s*\\|\\|\\s*\\|\\|\\s*\\-?\\d*");
		ws = Pattern.compile("\\s+");
		String url=Patterns.URL.reg();
		String date=Patterns.DATE.reg();
		String time=Patterns.TIME.reg();
		String num=Patterns.NUM.reg();
		String combine=url+"\\s+"+date+"\\s+"+time+"\\s+"+num+"\\s+"+num+"\\s+"+url;
		block=Pattern.compile(combine);
		
		String forwardBlock=num+"\\s+"+num+"\\s+"+url+"\\s+"+url;
		fp=Pattern.compile(forwardBlock);
		
		image=Pattern.compile("http[s]?://.*\\.jpg");
	}
	public static Weibo processLine(String line) {
		String lineBackUp=line;
		Weibo w = new Weibo();
		Matcher m = id.matcher(line);

		if (m.find()) {
			String i = m.group();
			line = line.substring(m.end()).trim();
			// System.out.println(line);
			w.setId(i);
			System.out.println(i);
		}

		//System.out.println(line);
		Matcher ms=ws.matcher(line);
		if(!ms.find())
			System.out.println("Not found");
		int contentStart=ms.end();
		String name = line.substring(0, ms.start()).trim();
		System.out.println(name);

		Matcher bm=block.matcher(line);
		if(!bm.find()){
			System.out.println("Block not found");
			
		}
		String sub=bm.group();
		Pattern dp=Patterns.DATE.value();
		Pattern tp=Patterns.TIME.value();
		Matcher dpm=dp.matcher(sub);
		Matcher tpm=tp.matcher(sub);
		if(!dpm.find()||!tpm.find()){
			System.out.println("Date Time not found!");
		}
		else{
			System.out.println("Date:"+dpm.group()+" Time:"+tpm.group());
			try {
				Date d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dpm
						.group() + " " + tpm.group());
				w.setCreateDate(d);
			} catch (ParseException e) {
				System.out.println("Error in time parsing");
			}
		}
		int contentEnd=bm.start();
		int blockEnd=bm.end();
		String content=line.substring(contentStart,contentEnd).trim();
		System.out.println(content);
		
		//truncate line, removing studied part
		line=line.substring(blockEnd);
		
		Matcher fm=fp.matcher(line);
		if(!fm.find()){
			System.out.println("Not forwarded message!");
			
		}
		else{
			System.out.println("Forwarded message");
			int forwardStart=fm.end();
			line=line.substring(forwardStart).trim();
			Matcher fwm=ws.matcher(line);
			fwm.find();
			String ownerName=line.substring(0,fwm.start()).trim();
			System.out.println(ownerName);
			line=line.substring(fwm.end()).trim();
			
			int fcEnd;
			Matcher im=image.matcher(line);
			if(im.find()){
				fcEnd=im.start();
			}else{
				Matcher whitespace=ws.matcher(line);
				whitespace.find();
				fcEnd=whitespace.start();
				
			}
			String forwardContent=line.substring(0,fcEnd);
			System.out.println(forwardContent);
			//add to the content of weibo
			content=forwardContent+content;
			
			line=line.substring(fcEnd).trim();
			
		}
		//save current process
		w.setContent(content);
		processWeibo(w);
		
		System.out.println("Successfully created weibo "+w.toString());
		//check if another weibo is on the same line
		Matcher yabm=block.matcher(line);
		if(yabm.find()){
			System.out.println("Another weibo item on the same line!");
			processLine(line);
		}
		return w;
	}
	public static void processWeibo(Weibo w){
		Date date=w.getCreateDate();
		String dateString=processDate(date);
		File outputFile=new File(path+dateString);
		if(!outputFile.exists()){
			outputFile.createNewFile();
		}
		outputFile.withWriter('UTF-8') {
			it.writeLine w.content;
		}
		
		
	}
	public static String processDate(Date date){
		int y=date.getYear()+1900;
		String year=y+"";
		int m=date.getMonth()+1;
		String month;
		if(m<10){
			month="0"+m;
		}else{
			month=m+'';
		}
		int d=date.getDate();
		String day;
		if(d<10){
			day="0"+d;
		}else{
			day=d+'';
		}
		int h=date.getHours();
		String hour;
		if(h<10){
			hour="0"+h;
		}else{
			hour=h+'';
		}
		
		//println("Year:${y},Month:${m},Date:${d},Hour:${h}");
		return year+month+day+hour;
		
	}
	public static void processFile(File file){
		BufferedReader reader=null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF8"));
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			System.out.println("Finished processing file "+file.getPath());
		}
		String line;
		

		try {
			while ((line = reader.readLine()) != null) {
				try{
					processLine(line);
				}catch(Exception e){
					println "Something is wrong with this line, but things must go on.";
					println e.message;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void startProcess(String inputPath,String outputPath){
		File outputFile=new File(outputPath);
		if(!outputFile.isDirectory()){
			println "Error! Please put a valid directory path in outputPath!";
			return;
		}
		path=outputPath;
		
		println "Output path set to ${path}";
		File file=new File(inputPath);
		if(file.isDirectory()){
			file.eachFile {
				processFile(it);
			}
		}else{
			processFile(file);
		}
	}
	public static void main(String[] args) throws Exception {
		startProcess("C:\\data","C:\\Users\\user\\sorted\\");

	}
}
