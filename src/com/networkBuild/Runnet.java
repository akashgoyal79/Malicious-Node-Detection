package com.networkBuild;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.NodeProperty.Node;

public class Runnet {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.print("\n********************PARAMETER SETTING********************\n");
		System.out.print("Please input the number of nodes : ");
		BufferedReader br0 = new BufferedReader(new InputStreamReader(System.in));
		String arg0=br0.readLine();
		int nodeNum=Integer.valueOf(arg0);		
		System.out.print("Please input the source node (1-"+nodeNum+") : ");
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		String arg1=br1.readLine();
		int source=Integer.valueOf(arg1);
		System.out.print("\nPlease input the destination node (1-"+nodeNum+") : ");
		BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
		String arg2=br2.readLine();
		int destination=Integer.valueOf(arg2);
		System.out.print("\nPlease input whether to attack (true/false) : ");
		BufferedReader br3 = new BufferedReader(new InputStreamReader(System.in));
		String arg3=br3.readLine();
		boolean attack=Boolean.valueOf(arg3);
		int attackMethod=0, attackRound=0;
		if(attack){
			System.out.print("\nIf attack, please input attack method(1-4)");
			System.out.print("\n1: Physical Damage");
			System.out.print("\n2: Jamming");
			System.out.print("\n3: Man-in-the-middle");
			System.out.print("\n4: Semi-damaged");
			System.out.print("\nPlease select the attack method : ");
			BufferedReader br4 = new BufferedReader(new InputStreamReader(System.in));
			String arg4=br4.readLine();
			attackMethod=Integer.valueOf(arg4);
			System.out.print("DEPLOY : AttackMethod "+attackMethod+"  => ");
			switch(attackMethod){
			case 1: System.out.println("Physical Damage"); break;
			case 2: System.out.println("Jamming"); break;
			case 3: System.out.println("Man In The Middle"); break;
			case 4: System.out.println("Semi-damaged"); break;
			}
			System.out.print("\nPlease input the round to attack (start from 1): ");
			BufferedReader br5 = new BufferedReader(new InputStreamReader(System.in));
			String arg5=br5.readLine();
			attackRound=Integer.valueOf(arg5);
		}
		else{
			System.out.print("NO ATTACK DEPLOYED");
		}
		System.out.print("\n********************TRANSMISSION START********************\n");
		NetFormation nf=new NetFormation(source,destination, attackMethod, attackRound, nodeNum);
		nf.startMonitoring(attack);
	}

}
class NetFormation{
	Random r=new Random(3447l);
	boolean initial=true;
	int source;
	int destination;
	int nodeNum;
	int zoom=5;
	int rateTimes=1000;
	double distanceThreshold=25*zoom;//**threshold of distance, more than this will lose connection
	int coordinateTimes=100*zoom;
	double energyThreshold=150;
	List<Integer> l=new ArrayList();
	double ETX=5e-8;
	double Efs=1e-11;
	double Eo;
	int attackMethod;
	int attackRound;
	Random r2=new Random(123l);
	Map<Integer, Node> nodeProperty=new HashMap<Integer, Node>();
	List<Integer> route=new ArrayList();//destination->source
	final double psend_initial[];
	double psend[];
	final double Energy_initial[];
	double Energy_remain_theoratical[];
	double Energy_remain_real[];
	public NetFormation(int source, int destination, int attackMethod, int attackRound, int nodeNum) throws Exception{//build initial net and get initial route,lower level
		this.source=source;
		this.destination=destination;
		this.attackMethod=attackMethod;
		this.attackRound=attackRound;
		this.nodeNum=nodeNum;
		this.psend_initial=new double[nodeNum];
		this.psend=new double[nodeNum];
		this.Energy_initial=new double[nodeNum];
		this.Energy_remain_theoratical=new double[nodeNum];
		this.Energy_remain_real=new double[nodeNum];
		Initial(psend,r,rateTimes);
		for(int i=0;i<nodeNum;i++)
			psend_initial[i]=psend[i];
		Initial_Energy(Energy_initial, psend);
		Eo=findMinAndMax.Max(Energy_initial);
		for(int i=0;i<nodeNum;i++){
			nodeProperty.put(i, new Node(nodeNum,r,coordinateTimes));
		}
		Set<Integer> nodeKey=nodeProperty.keySet();
		while(route.size()<2){
			if(route.size()!=0){
				distanceThreshold+=zoom;
				route.clear();
			}
			//FileWriter fw=new FileWriter(new File("/Users/SamZhang/Documents/Eclipse_Neon/WSN/src/data/distance.txt"));
			for(int i:nodeKey){
				nodeProperty.get(i).calculateDistance(i,nodeProperty,distanceThreshold);//initialize all nodes' coordinates and distance
				/*for(int le=0;le<nodeProperty.get(i).distance.length;le++)
					fw.write(String.valueOf(nodeProperty.get(i).distance[le])+" ");
				fw.write("\n");*/
			}
			//fw.close();
			List<Integer> Nothing=new ArrayList();
			LowerLevel ll=new LowerLevel();
			route=ll.formRoute(nodeProperty, source, destination, nodeNum, psend, energyThreshold,initial, Nothing);//can get the route from source to destination
		}
		this.initial=false;
		System.out.println("Hop times(Number of routers) : "+(route.size()-2));
		System.out.print("Source to Destination : "+"  ");
		for(int i=route.size()-1;i>=0;i--)
			System.out.print(route.get(i)+" ");
		System.out.print('\n');
		LinesComponent.perform(new ArrayList(nodeProperty.values()));
		
	}
	public void startMonitoring(boolean attack) throws Exception{//upper level
		boolean hasAttacked=true;
		if(attack&&hasAttacked&&route.size()==2){
			System.out.println("CANNOT ATTACK SOURCE/DESTINATION, QUIT");
			System.exit(0);
		}
		UpperLevel ul=new UpperLevel(route, psend, psend_initial,ETX, Efs, nodeProperty,Energy_initial, Energy_remain_theoratical, Energy_remain_real,Eo,attack, attackMethod, attackRound);
		while(true){
			LinesComponent.comp.drawPath((ArrayList)route);
			Thread.sleep(3000);
			l.clear();
			for(int i=0;i<route.size();i++) l.add(100);
			int netStatus=ul.Monitoring(source,destination, r2, hasAttacked, l);
			hasAttacked=false;
			if(netStatus!=-1){
				List<Integer> route_new=new ArrayList();
				int destination_new_index=-1;
				while(route_new.size()<2){
					Set<Integer> nodeKey=nodeProperty.keySet();
					if(!route_new.isEmpty()){
						distanceThreshold+=zoom;
						route_new.clear();
						for(int i:nodeKey){
							nodeProperty.get(i).calculateDistance(i,nodeProperty,distanceThreshold);//initialize all nodes' coordinates and distance
						}
					}
					int compromisedNode=netStatus;
					for(int i:nodeKey){
						if(i!=compromisedNode)
							nodeProperty.get(i).distance[compromisedNode]=Node.INF;//all nodes cannot reach compromised node
						else{
							int loop=(nodeProperty.get(i).distance).length;
							for(int lo=0;lo<loop;lo++){
								nodeProperty.get(i).distance[lo]=Node.INF;// compromised node can not reach any other node
							}
						}
					}
					int compromised_Routeindex=-1;
					for(int i=0;i<route.size();i++){
						if(route.get(i)==compromisedNode){
							compromised_Routeindex=i;
							break;
						}
					}
					int source_new=route.get(compromised_Routeindex+1);
					int destination_new=route.get(compromised_Routeindex-1);
					int source_new_index=-1;
					destination_new_index=-1;
					for(int i=0;i<route.size();i++){
						if(route.get(i)==source_new){
							source_new_index=i;// the location of source_new in route
						}
						if(route.get(i)==destination_new){
							destination_new_index=i;// the location of destination_new in route
						}
					}
					List<Integer> avoid_nodes=new ArrayList();//should avoid nodes that are already in the route to be in the new route
					int count=0;
					for(int i=0;i<route.size();i++){
						if(i<destination_new_index||i>source_new_index){
							avoid_nodes.add(route.get(i));
							count++;
						}
					}
					LowerLevel ll=new LowerLevel();
					route_new=ll.formRoute(nodeProperty, source_new, destination_new, nodeNum, psend, energyThreshold,initial,avoid_nodes);
					if(route_new.size()==2) System.out.println(" ");
					//System.err.println(route_new.size());
				}
				List<Integer> temp=new ArrayList();//temp store the value of route
				for(int i=0;i<route.size();i++){
					temp.add(route.get(i));
				}
				int totalLength=route.size()+route_new.size()-3;//-2 already there -1 compromised node
				int route_new_indexCount=1;
				int route_indexCount=0;
				route.clear();
				for(int i=0;i<totalLength;i++){
					if(i<=destination_new_index){
						route.add(temp.get(route_indexCount));
						route_indexCount++;
						if(i==destination_new_index)
							route_indexCount++;
					}
					else if(i>destination_new_index&&i<=(destination_new_index+route_new.size()-2)){
						route.add(route_new.get(route_new_indexCount));
						route_new_indexCount++;
					}
					else{
						route.add(temp.get(route_indexCount));
						route_indexCount++;
					}
				}
				System.out.println("Hop times : "+(route.size()-2));
				System.out.print("Source to Destination : "+" ");
				for(int i=route.size()-1;i>=0;i--)
					System.out.print(route.get(i)+" ");
				System.out.print('\n');
			}
			else{
				break;
			}
		}
	}
	
	public void Initial(double array[], Random r, double rateTimes){
		for(int i=0;i<array.length;i++){
			array[i]=r.nextDouble()*rateTimes;
		}
	}
	public void Initial_Energy(double[] array,double psend[]){
		for(int i=0;i<array.length;i++){
			array[i]=psend[i]/10000;
		}
	}
	

}
