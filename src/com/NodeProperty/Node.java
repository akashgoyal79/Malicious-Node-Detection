package com.NodeProperty;

import java.util.Map;
import java.util.Random;

public class Node {
	public static int INF=Integer.MAX_VALUE-500;//in case need to add distance
	public final double x;
	public final double y;
	public double distance[];
	public Node(int nodeNum, Random r, int coordinateTimes){
		this.x=r.nextDouble()*coordinateTimes;
		this.y=r.nextDouble()*coordinateTimes;
		this.distance=new double[nodeNum];
		initialDistance(nodeNum);
	}
	public void initialDistance(int nodeNum){
		for(int i=0;i<nodeNum;i++){
			distance[i]=INF;
		}
	}
	public void calculateDistance(int recentNodeNum,Map<Integer, Node>nodeProperty, double distanceThreshold){
		for(int i=0;i<nodeProperty.keySet().size();i++){
			if(recentNodeNum==i)
				continue;
			double dis=Math.sqrt(Math.pow((x-nodeProperty.get(i).x), 2)+Math.pow((y-nodeProperty.get(i).y), 2));
			if(dis<distanceThreshold){
				distance[i]=dis;
			}
		}
	}
}
