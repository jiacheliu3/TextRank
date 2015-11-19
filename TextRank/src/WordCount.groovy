
class WordCount {
	Date hour;
	Map<String,Integer> hotwords;
	public WordCount(Date date){
		hour=convertToHour(date);
		hotwords=new LinkedHashMap<>();
		
	}
	public static Date convertToHour(Date date){
		Date trun=date;
		//round the datetime to hours, 2:01~2:59 belong to 2:00
		trun.set(minute:0,second:0,hourOfDay:0);
		
		return trun;
	}
	public static void main(String[] args){
		Date date=new Date();
		Date hourly=convertToHour(date);
		println hourly;
	}
}
