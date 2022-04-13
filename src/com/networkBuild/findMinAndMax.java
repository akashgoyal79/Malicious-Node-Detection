package com.networkBuild;

public class findMinAndMax {
	public static double[] Min(double dis2source[]){
		double min_num=dis2source[0];
		double index=0;
		for(int i=0;i<dis2source.length;i++){
			if(dis2source[i]<min_num){
				min_num=dis2source[i];
				index=i;
			}
		}
		double result[]={index,min_num};
		return result;
	}
	public static double Max(double dis2source[]){
		double max_num=dis2source[0];
		double index=0;
		for(int i=0;i<dis2source.length;i++){
			if(dis2source[i]>max_num){
				max_num=dis2source[i];
				index=i;
			}
		}
		double result=max_num;
		return result;
	}
}
