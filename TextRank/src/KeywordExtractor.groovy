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
		String content="�ж���˹������Ա��9�����罻��վ���ر�ʾ�����������ǰ��Ա˹ŵ�ǣ��Ѿ�����ί�������ıӻ������������ڷ��������Ӻ��漴ɾ��������˹���־ܾ��������ۣ���һֱЭ��˹ŵ�ǵ�ά�����ܷ�������Ͷ��ί����������������˹�����������ίԱ����ϯ��ʲ�Ʒ��ڸ�������������¶˹ŵ���ѽ���ί�������ıӻ����飬�������Ϊ˹ŵ�ǵĶ����������½�չ���������������ڼ�������������ɾ������ʲ�Ʒ�������ǿ�������˹��Ӫ����̨�����Ų�����˵��������̨�Ѿ��������ϣ�����ʲ�Ʒ���������������ݡ�����ί������פĪ˹�ƴ�ʹ�ݡ�����˹��ͳ�������ˡ��Լ��⽻�����ܾ��������ۡ���ά�����ܾͷ���˹ŵ������ʽ����ί�������ıӻ���˵�����ʵ�ʱ�乫���йؾ���������˹ŵ������Ŀǰ����Ī˹��л��÷���ֻ��������������������ڡ�����ǰ��Լ20�������ύ�ӻ����룬ί��������������ϺͲ���ά�ǣ��Ⱥ��ʾ��Ӧ������˹ŵ�ǻ�û������������������һ���⽻�粨������ά����ͳĪ����˹��ר�������ڱ�ŷ�޶���Ի���˹ŵ���ڻ���Ϊ�ɾܾ������¼������¹���֮һ��������ͻȻת�ڷ磬�ⳤ�����]�ű�ʾԸ����κ������Ǹ����ǿ����ʱ����û�йر���ջ���ר�����䡣";
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
