package edu.cityu.textrank

import groovy.time.TimeCategory

import java.text.SimpleDateFormat

class Weibo {
	String id;
	Date createDate;
	String content;
	//String fullContent;
	@Override
	public String toString(){
		"ID: ${id}\nDate: ${createDate}\nContent: ${content}";
	}
	@Override
	public int hashCode(){
		return id.hashCode()+createDate.hashCode()+content.hashCode();
	}
	public static main(String[] args){
		String fileName="20150101";
		Date fileDate=new SimpleDateFormat("yyyyMMdd").parse(fileName);
		Date startDate=new SimpleDateFormat("yyyyMMddhh").parse('2015010100');
		Date endDate=new SimpleDateFormat("yyyyMMddhh").parse('2015010100');
		println fileDate.before(endDate);
		println fileDate.after(startDate);
		if(fileDate.before(endDate)&&fileDate.after(startDate)){
			
		}else{
			String diff=TimeCategory.minus(startDate, fileDate).toString();
			if(diff.contains("day")){}
			else
				println diff;
		}
	}
	
}
