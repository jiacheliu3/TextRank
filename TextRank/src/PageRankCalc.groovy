import Jama.Matrix


class PageRankCalc {
	public static double damping=0.85;
	public static double threshold=0.00001;
	public static setDamping(double d){
		damping=d;
	}
	public static setThreshold(double d){
		threshold=d;
	}
	public static Map<String,Integer> constructIndexMap(Collection<String> nodes){
		Map<String,Integer> indexMap=new HashMap<>();
		int count=0;
		for(String node:nodes){
			indexMap.put(node, count);
			count++;
			
		}
		return indexMap;
	}
	public static Matrix constructTransitionMatrix(Collection<String> nodes,Set<Map> edges,Map<String,Integer> indexMap,boolean isDirected){
		int n=nodes.size();
		
		//construct adjacency matrix
		Map<String,Integer> sizeMap=new HashMap<>();
		Matrix A=new Matrix(n,n);
		for(Map<String,String> m:edges){
			String source=m["source"];
			String target=m["target"];
			double weight=m["weight"];
			int row=indexMap[source];
			int col=indexMap[target];
			
			A.set(row, col, weight);
			
			if(sizeMap.containsKey(source)){
				sizeMap[source]+=1;
			}else{
				sizeMap.put(source,1);
			}
			//if the graph is not directed, the target also gives out the link
			if(!isDirected){
				A.set(col, row, weight);
				
				if(sizeMap.containsKey(target)){
					sizeMap[target]+=1;
				}else{
					sizeMap.put(target,1);
				}
			}
		}	
		//construct transition matrix
		Matrix M=new Matrix(n,n);
		for(def e:indexMap){
			String nodeName=e.key;
			int row=indexMap[nodeName];
			int size;
			if(sizeMap.containsKey(nodeName)){
				size=sizeMap[nodeName];
			}
			else{
				size=0;
			}
			if(size==0){
				double s=1.0/(double)n;
				for(int j=0;j<n;j++){
					M.set(row, j,s);
				}
			}
			else{
				for(int i=0;i<n;i++){
					double d=A.get(row, i);
					M.set(row,i,d/size);
				}
			}
			
		
			
			
		}
		
		println("Transition matrix complete.");
		return M;
		
	}
	public static double calculateError(Matrix A, Matrix B){
		Matrix diff=A.minus(B);
		return diff.normF();//sqrt of squares of all elements
	}
	public static Map<String,Double> calculatePageRank(Collection<String> nodes,Set<Map> edges,boolean isDirected){
		
		int n=nodes.size();
		//construct index map storing all nodes and index
		Map<String,Integer> indexMap=constructIndexMap(nodes);
		//construct transition matrix
		Matrix M=constructTransitionMatrix(nodes,edges,indexMap,isDirected);
		//initialize the matrix
		Matrix N=new Matrix(1,n,1.0/n);
		//iteratively calculate
		Matrix I=new Matrix(1,n,(1.0-damping)/n);
//		//approximation of times of iterations
//		int iter=(int)(Math.log(n)/Math.log(10))+1;
//		for(int i=0;i<iter;i++){
//			N=N.times(M.times(damping)).plus(I);
//		}
		boolean converged=false;
		while(!converged){
			Matrix nextN=N.times(M.times(damping)).plus(I);
			//nextN.print(0, n-1);
			double error=calculateError(N,nextN);
			N=nextN;
			if(error<=threshold){
				converged=true;
			}
		}
		//assign ranks to nodes
		Map<String,Double> pageRank=new HashMap<>();
		def findName={number->
			for(def e:indexMap){
				if(e.value==number)
					return e.key;
			}
		}
		double[][] ranks=N.getArray();
		for(int i=0;i<n;i++){
			pageRank.put(findName(i), ranks[0][i])
		}
		return pageRank;
		
	}
	public static Map<String,Double> normalize(Map<String,Double> pagerank){
		double sum=0.0;
		int count=pagerank.size();
		Map<String,Double> normalizedRank=new HashMap<>();
		pagerank.each{
			sum+=it.value;
			
		}
		pagerank.each{
			normalizedRank.put(it.key, it.value/sum);
		}
		return normalizedRank;
	}
	public static void main(String[] args){
		def nodes=['a','b','c','d','e','f','g','h','i','j','k'];
		def set=[['source':'b','target':'c','weight':1],
			['source':'b','target':'c','weight':1],
			['source':'c','target':'b','weight':1],
			['source':'d','target':'a','weight':1],
			['source':'d','target':'b','weight':1],
			['source':'e','target':'b','weight':1],
			['source':'e','target':'d','weight':1],
			['source':'e','target':'f','weight':1],
			['source':'f','target':'b','weight':1],
			['source':'f','target':'e','weight':1],
			['source':'g','target':'b','weight':1],
			['source':'g','target':'e','weight':1],
			['source':'h','target':'b','weight':1],
			['source':'h','target':'e','weight':1],
			['source':'i','target':'b','weight':1],
			['source':'i','target':'e','weight':1],
			['source':'j','target':'e','weight':1],
			['source':'k','target':'e','weight':1]];
		Set<Map<String>> edges=new HashSet<>();
		edges.addAll(set);

		def yarank=calculatePageRank(nodes,edges,false);
		println yarank;
		yarank=normalize(yarank);
		println yarank;
	}
}
