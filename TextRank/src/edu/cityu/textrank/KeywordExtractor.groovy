package edu.cityu.textrank
import groovy.time.TimeCategory

class KeywordExtractor{
	public static Set<String> extractKeywords(String content,int windowSize,int number){
		def timeStart = new Date();
		ArrayList<String> segments=FnlpManager.justSegment(content);
		Map<String,Double> results=getTopTextRank(segments,windowSize,number);
		def timeEnd=new Date();
		println "Keyword extraction completed in "+TimeCategory.minus(timeEnd, timeStart);
		
		return results.keySet();
		
	}
	public static Set<String> extractKeywords(String content){
		int l=content.length();
		int expected=Math.round(Math.sqrt(l));
		//use window size 5
		return extractKeywords(content,5,expected);
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
			if(c>=keywordNum)
				break;
		}
		//merge keywords if possible
		
		return keywords;
	}
	public static void main(String[] args){
		String content="�ж���˹������Ա��9�����罻��վ���ر�ʾ�����������ǰ��Ա˹ŵ�ǣ��Ѿ�����ί�������ıӻ������������ڷ��������Ӻ��漴ɾ��������˹���־ܾ��������ۣ���һֱЭ��˹ŵ�ǵ�ά�����ܷ�������Ͷ��ί����������������˹�����������ίԱ����ϯ��ʲ�Ʒ��ڸ�������������¶˹ŵ���ѽ���ί�������ıӻ����飬�������Ϊ˹ŵ�ǵĶ����������½�չ���������������ڼ�������������ɾ������ʲ�Ʒ�������ǿ�������˹��Ӫ����̨�����Ų�����˵��������̨�Ѿ��������ϣ�����ʲ�Ʒ���������������ݡ�����ί������פĪ˹�ƴ�ʹ�ݡ�����˹��ͳ�������ˡ��Լ��⽻�����ܾ��������ۡ���ά�����ܾͷ���˹ŵ������ʽ����ί�������ıӻ���˵�����ʵ�ʱ�乫���йؾ���������˹ŵ������Ŀǰ����Ī˹��л��÷���ֻ��������������������ڡ�����ǰ��Լ20�������ύ�ӻ����룬ί��������������ϺͲ���ά�ǣ��Ⱥ��ʾ��Ӧ������˹ŵ�ǻ�û������������������һ���⽻�粨������ά����ͳĪ����˹��ר�������ڱ�ŷ�޶���Ի���˹ŵ���ڻ���Ϊ�ɾܾ������¼������¹���֮һ��������ͻȻת�ڷ磬�ⳤ�����]�ű�ʾԸ����κ������Ǹ����ǿ����ʱ����û�йر���ջ���ר�����䡣";
		Set<String> keywords2 = extractKeywords(content,2,10);
		println keywords2;
		Set<String> keywords3 = extractKeywords(content,3,10);
		println keywords3;
		Set<String> keywords5 = extractKeywords(content,5,10);
		println keywords5;
		Set<String> keywords10 = extractKeywords(content,10,10);
		println keywords10;
		
	}
}
