class KeywordExtractor{
	public static Set<String> extractKeywords(String content,int windowSize,int number){
		ArrayList<String> segments=FnlpManager.justSegment(content);
		Map<String,Double> results=getTopTextRank(segments,windowSize,number);
		return results.keySet();
		
	}
	public static Map<String,Double> getTopTextRank(ArrayList<String> words,int windowSize,int keywordNum){
		//construct node set
		HashSet<String> nodes=new HashSet<>();
		nodes.addAll(words);
		//construct edge set
		HashSet<Map> edges=new HashSet<>();
		int iter=words.size()-windowSize+1;
		for(int i=0;i<iter;i++){
			String source=words[i];
			//for every slide of the window, establish links among every two nodes
			for(int j=0;j<windowSize;j++){
				String target=words[i+j];
				Map<String,String> edge=new HashMap<>();
				edge.put('source', words);
				edge.put('target', target);
				edge.put('weight',1.0);
				
			}
		}
		
		//calculate page rank
		Map<String,Double> pageRank=PageRankCalc.calculatePageRank(nodes, edges, false);
		//return the top keywords
		int c=0;
		Map<String,Double> keywords=new HashMap<>();
		for(def e : pageRank){
			keywords.put(e.key, e.value);
			c++;
			if(c>=keywordNum)
				break;
		}
		return keywords;
	}
	public static void main(String[] args){
		String content="有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
		Set<String> keywords2 = extractKeywords(content,2,10);
		println keywords2;
		Set<String> keywords3 = extractKeywords(content,3,10);
		println keywords3;
		Set<String> keywords5 = extractKeywords(content,5,10);
		println keywords5;
		Set<String> keywords10 = extractKeywords(content,2,10);
		println keywords10;
		
	}
}
