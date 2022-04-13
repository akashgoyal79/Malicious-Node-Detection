package com.networkBuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.NodeProperty.Node;

public class UpperLevel {
	List<Integer>route;//destination->source
	List<Double> routeEnergyWaste=new ArrayList();//destination->source
	List<Integer> l;
	int attackMethod;
	int attackRound;
	double[] psend;
	double [] psend_initial;
	double ETX;
	double Efs;
	double Eo;
	double Energy_initial[];
	double Energy_remain_theoratical[];
	double Energy_remain_real[];
	boolean attack;//to decide to attack or not
	Map<Integer,Node>nodeProperty;
	public UpperLevel(List<Integer>route, double[] psend, double[] psend_initial, double ETX, double Efs, Map<Integer,
					  Node>nodeProperty, double Energy_initial[], double Energy_remain_theoratical[], double[] Energy_remain_real, double Eo, boolean attack, int attackMethod, int attackRound){
		this.route=route;
		this.psend=psend;
		this.psend_initial=psend_initial;
		this.ETX=ETX;
		this.Efs=Efs;
		this.nodeProperty=nodeProperty;
		this.Energy_initial=Energy_initial;
		this.Energy_remain_theoratical=Energy_remain_theoratical;
		this.Energy_remain_real=Energy_remain_real;
		InitialEnergy_remain(this.Energy_remain_theoratical, this.Energy_remain_real, this.Energy_initial);
		this.Eo=Eo;
		this.attack=attack;
		this.attackRound=attackRound;
		this.attackMethod=attackMethod;
	}
	public int Monitoring(int source, int destination, Random r2, boolean hasAttacked, List<Integer> l) throws Exception{
		this.l=l;
		boolean dead=false;
		Random r3=new Random(3447l);
		int countLoop=0;
		int compromisedNode=(attack&&hasAttacked)?r2.nextInt(route.size()-2)+1:0;
		if(attack&&hasAttacked&&attackMethod==3){
			l.clear();
			for(int i=0;i<route.size();i++){
				l.add((i<=compromisedNode)?70:100);
			}
		}
		while(!dead){
			countLoop++;
			System.out.print("\r\t"+"Round = "+countLoop);
			double EnergyWaste_Destination=l.get(0)*psend[route.get(0)]*ETX;
			routeEnergyWaste.add(EnergyWaste_Destination);
			for(int i=1;i<route.size()-1;i++){
				double EnergyWaste=l.get(i)*psend[route.get(i)]*(2*ETX+Efs*nodeProperty.get(route.get(i)).distance[route.get(i-1)]);
				routeEnergyWaste.add(EnergyWaste);
			}
			double EnergyWaste_Source=l.get(route.size()-1)*psend[route.get(route.size()-1)]*(ETX+Efs*nodeProperty.get(route.get(route.size()-1)).distance[route.get(route.size()-2)]);
			routeEnergyWaste.add(EnergyWaste_Source);//should clear routeEnergyWaste in the end of monitoring
			for(int i=0;i<route.size();i++){
				Energy_remain_theoratical[route.get(i)]-=routeEnergyWaste.get(i);
				Energy_remain_real[route.get(i)]-=routeEnergyWaste.get(i);
			}
			List<Integer> psend_theoratical_status=AdjustPsend.adjustPsend(psend, psend_initial, Energy_remain_theoratical, Energy_initial, route);
			if(attack&&hasAttacked&&countLoop>=attackRound){//and trigger condition, attack aim to/represent by psend
				Attack ack=new Attack();
				ack.attack(psend, psend_initial,route, attackMethod, compromisedNode, ETX, Efs, nodeProperty, Energy_remain_theoratical, Energy_remain_real, Energy_initial, r3);
			}
			List<Integer> psend_actual_status=AdjustPsend.seeStatus(psend, psend_initial, route);//now psend can be only set to several certain values
			routeEnergyWaste.clear();
			for(int i=0;i<route.size();i++){
				if(psend_actual_status.get(i)==7){
					System.out.println("\n\tNode "+route.get(i)+" compromised, Trying to find another route");
					for(int aaa=route.size()-1;aaa>=0;aaa--){
						System.out.print(psend_actual_status.get(aaa)+" ");
					}
					if(route.get(i)==source||route.get(i)==destination){
						System.err.println("\tSource/destination compromised, program terminated");
						System.exit(0);
					}
					return route.get(i);//when compromised, return node_num and choose route again
				}
				else if(psend_actual_status.get(i)==0&&psend_theoratical_status.get(i)==0){
					//System.out.println(Energy_remain_theoratical[route.get(i)]+"    "+Energy_initial[route.get(i)]);
					System.out.println("\n\tNode "+route.get(i)+" dead naturally, Looptime = "+countLoop);
					dead=true;
					break;
				}
				//check whether psend status and theoretical energy status can fit, if can->continue, if not-> compromised and return node_num
				else if(psend_theoratical_status.get(i)!=psend_actual_status.get(i)||(psend_actual_status.get(i)==0&&psend_theoratical_status.get(i)!=0)){
					System.out.println("\n\tNode "+route.get(i)+" compromised, Trying to find another route");
					for(int aaa=route.size()-1;aaa>=0;aaa--){
						System.out.print(psend_actual_status.get(aaa)+" ");
					}
					if(route.get(i)==source||route.get(i)==destination){
						System.err.println("\tSource/destination compromised, program terminated");
						System.exit(0);
					}
					return route.get(i);
				}
			}
			if(countLoop==5){
				for(int aaa=route.size()-1;aaa>=0;aaa--){
					System.out.print(psend_actual_status.get(aaa)+" ");
				}
			}
			Thread.sleep(100);
		}
			return -1;//route dead
	}
	public void InitialEnergy_remain(double[] Energy_remain_theoretical, double[] Energy_remain_real, double[] Energy_initial){
		for(int i=0;i<Energy_initial.length;i++){
			Energy_remain_theoretical[i]=Energy_initial[i];
			Energy_remain_real[i]=Energy_initial[i];
		}
	}
}
