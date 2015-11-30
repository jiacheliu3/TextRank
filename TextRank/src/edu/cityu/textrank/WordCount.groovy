package edu.cityu.textrank

class WordCount {
	Date hour;
	public Map<String,Integer> hotwords;
	public WordCount(Date date){
		hour=date;
		hotwords=new LinkedHashMap<>();
		
	}
	public Set<String> getKeywordSet(){
		return hotwords.keySet();
	}
	public ArrayList<String> getTopKeywords(int number){
		LinkedHashMap<String,Integer> results=hotwords.sort{ a, b -> b.value <=> a.value };
		int count=0;
		def r=[];
		for(def e:results){
			r.add(e.key);
			count++;
			if(count>=number){break;}
		}
		
		return r;
	}
	public void addKeywords(Collection<String> c){
		c.each{
			if(hotwords.containsKey(it)){
				hotwords[it]+=1;
			}
			else{
				hotwords.put(it, 1);
			}
		}
	}
	public void addKeywords(Map<String,Integer> map){
		map.each{key,value->
			if(hotwords.containsKey(key)){
				int i=value;
				hotwords[key]+=i;
			}
			else{
				hotwords.put(key, value);
			}
		}
	}
	//obsolete
	public static Date convertToHour(Date date){
		Date trun=date;
		//round the datetime to hours, 2:01~2:59 belong to 2:00
		trun.set(minute:0,second:0,hourOfDay:0);
		
		return trun;
	}
//	public static void main(String[] args){
//		Date date=new Date();
//		Date hourly=convertToHour(date);
//		println hourly;
//	}
}
