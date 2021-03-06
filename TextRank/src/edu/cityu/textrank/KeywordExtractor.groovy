package edu.cityu.textrank
import groovy.time.TimeCategory

class KeywordExtractor{
	public static Set<String> extractKeywords(String content,int windowSize,int number){
		//def timeStart = new Date();
		ArrayList<String> segments=FnlpManager.justSegment(content);
		Map<String,Double> results=getTopTextRank(segments,windowSize,number);
		//def timeEnd=new Date();
		//println "Keyword extraction completed in "+TimeCategory.minus(timeEnd, timeStart);
		
		return results.keySet();
		
	}
	public static Set<String> extractKeywords(String content){
		int l=content.length();
		int expected=Math.round(Math.sqrt(l));
		//use window size 5
		return extractKeywords(content,5,expected);
	}
	public static Set<String> extractKeywords(String content,String modelBase){
		FnlpManager.setBase(modelBase);
		int l=content.length();
		int expected=Math.round(Math.sqrt(l));
		//use window size 5
		return extractKeywords(content,5,expected);
	}
	public static Map<String,Double> getTopTextRank(ArrayList<String> words,int windowSize,int keywordNum){
		//construct node set
		HashSet<String> nodes=new HashSet<>();
		nodes.addAll(words);
		//construct index map
		LinkedHashMap<Integer,String> indexMap=new LinkedHashMap<>();
		int index=0;
		nodes.each{
			indexMap.put(index,it);
			index++;
		}
		//construct edge set
		HashSet<Map> edges=new HashSet<>();
		int iter=words.size()-windowSize+1;
		for(int i=0;i<iter;i++){
			String source=words[i];
			if(source.matches(Patterns.NONTEXT.value())){
				continue;
			}
			//for every slide of the window, establish links among every two nodes
			for(int j=1;j<windowSize;j++){
				String target=words[i+j];
				if(target.matches(Patterns.NONTEXT.value())){
					continue;
				}
				Map<String,String> edge=new HashMap<>();
				edge.put('source', source);
				edge.put('target', target);
				edge.put('weight',1.0);
				edges.add(edge);
			}
		}
		
		//calculate page rank
		Map<String,Double> pageRank=PageRankCalc.calculatePageRank(nodes, edges, false);
		//sort by pagerank
		pageRank=pageRank.sort { a, b -> b.value <=> a.value }
		//return the top keywords
		int c=0;
		Map<String,Double> keywords=new HashMap<>();
		for(def e : pageRank){
			keywords.put(e.key, e.value);
			c++;
			if(c>=keywordNum*1.5)
				break;
		}
		//merge keywords if possible
		for(def e:indexMap){
			String word=e.value;
			Integer i=e.key;
			if(indexMap.containsKey(i+1)){
				String nextWord=indexMap[new Integer(i+1)];
				if(keywords.containsKey(word)&&keywords.containsKey(nextWord)){
					keywords.put(word+nextWord,Math.max(keywords[word],keywords[nextWord]));
					keywords.remove(word);
					keywords.remove(nextWord);
					
				}
					
			}
		}
		//output final keywords
		int w=0;
		keywords=keywords.sort { a, b -> b.value <=> a.value }
		Map<String,Double> finalKeywords=new LinkedHashMap<>();
		for(def e : keywords){
			finalKeywords.put(e.key, e.value);
			w++;
			if(w>=keywordNum)
				break;
		}
		
		return finalKeywords;
	}
	public static void main(String[] args){
		String content="有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
		Set<String> keywords2 = extractKeywords(content,"C:\\Users\\user\\git\\TextRank\\TextRank\\models\\");
		println keywords2;
	}
}
