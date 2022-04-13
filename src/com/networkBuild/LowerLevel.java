package com.networkBuild;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.NodeProperty.Node;

public class LowerLevel {
	
	public List<Integer> formRoute(Map<Integer, Node>nodeProperty,int source, int destination, int nodeNum, 
								double psend[], double energyThreshold, boolean initial, List<Integer> avoid_nodes) throws Exception{  //to get/re-get the structure of the network
		double original_energyThreshold=energyThreshold;
		Map<Integer,Integer> nodeAndParent=new HashMap<Integer,Integer>();
		while(true){
			double dis2source[]=new double[nodeNum];
			boolean visited[]=new boolean[nodeNum];
			for(int i=0;i<nodeNum;i++){
				if(i==source)
					dis2source[source]=0;
				else{
					dis2source[i]=Node.INF;
				}
				visited[i]=false;
				if(!initial&&avoid_nodes.contains(i)){
					visited[i]=true;
				}
			}
			for(int i=0;i<nodeNum-1;i++){
				double[] temp=new double[nodeNum];
				for(int k=0;k<nodeNum;k++){
					if(!visited[k])
						temp[k]=dis2source[k];
					else{
						temp[k]=Node.INF;
					}
				}
				double[] result=findMinAndMax.Min(temp);
				int index_min=(int)result[0];
				double dis_min=result[1];
				visited[index_min]=true;
				for(int j=0;j<nodeNum;j++){
					if(dis2source[index_min]+nodeProperty.get(index_min).distance[j]<dis2source[j]&&psend[j]>energyThreshold){
						if(initial||!avoid_nodes.contains(j)){
							dis2source[j]=dis2source[index_min]+nodeProperty.get(index_min).distance[j];
							nodeAndParent.put(j, index_min);
						}
					}
				}
			}
			if(dis2source[destination]!=Node.INF){
				energyThreshold=original_energyThreshold;
				break;
			}
			else{
				energyThreshold--;
				if(energyThreshold<=0){
					//System.out.println("No connection available between route nodes "+source+" and "+destination+". Peace out");
					//System.exit(0);
					energyThreshold=original_energyThreshold;
					List<Integer> falseRoute=new ArrayList();
					falseRoute.add(-1);
					return falseRoute;
				}
				nodeAndParent.clear();
			}
		}
		/*FileWriter fw=new FileWriter("/Users/SamZhang/Documents/Eclipse_Neon/WSN/src/data/nodeAndParent.txt");
		Set<Integer> key=nodeAndParent.keySet();
		for(int i:key)
			fw.write(String.valueOf(i)+'\t'+String.valueOf(nodeAndParent.get(i))+'\n');
		fw.close();*/
		int t=destination;
		List<Integer> route=new ArrayList();//route sequence: destination->source
		route.add(destination);
		while(true){
			route.add(nodeAndParent.get(t));
			t=nodeAndParent.get(t);
			if(t==source){
				/*System.out.println("Hop times : "+(route.size()-2));
				for(int it=route.size()-1;it>=0;it--){
					System.out.print(route.get(it)+" ");
				}
				System.out.print('\n');*/
				break;
			}
		}
		return route;
	}
	
}