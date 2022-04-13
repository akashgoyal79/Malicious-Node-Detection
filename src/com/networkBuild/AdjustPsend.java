package com.networkBuild;

import java.util.ArrayList;
import java.util.List;

public class AdjustPsend {
	static public List<Integer> adjustPsend(double psend[], double psend_initial[], double Energy_remain_theoratical[], double Energy_initial[], List<Integer> route){
		List<Integer> psend_theoratical_status=new ArrayList<>();
		for(int i=0;i<route.size();i++){//non-exclude destination
			double Node_energy_remain=Energy_remain_theoratical[route.get(i)];
			double Node_energy_initial=Energy_initial[route.get(i)];
			int Node_num=route.get(i);
			if(Node_energy_remain>=0.84*Node_energy_initial){
				psend[Node_num]=psend_initial[Node_num];
				psend_theoratical_status.add(6);
			}
			else if(Node_energy_remain>=0.68*Node_energy_initial&&Node_energy_remain<0.84*Node_energy_initial){
				psend[Node_num]=psend_initial[Node_num]*0.335;
				psend_theoratical_status.add(5);
			}
			else if(Node_energy_remain>=0.52*Node_energy_initial&&Node_energy_remain<0.68*Node_energy_initial){
				psend[Node_num]=psend_initial[Node_num]*0.115;
				psend_theoratical_status.add(4);
			}
			else if(Node_energy_remain>=0.36*Node_energy_initial&&Node_energy_remain<0.52*Node_energy_initial){
				psend[Node_num]=psend_initial[Node_num]*0.0753;
				psend_theoratical_status.add(3);
			}
			else if(Node_energy_remain>=0.2*Node_energy_initial&&Node_energy_remain<0.36*Node_energy_initial){
				psend[Node_num]=psend_initial[Node_num]*0.0561;
				psend_theoratical_status.add(2);
			}
			else if(Node_energy_remain>=0.04*Node_energy_initial&&Node_energy_remain<0.2*Node_energy_initial){
				psend[Node_num]=psend_initial[Node_num]*0.0222;
				psend_theoratical_status.add(1);
			}
			else{
				psend[Node_num]=0;//node_dead
				psend_theoratical_status.add(0);
			}
		}
		return psend_theoratical_status;
	}
	static public List<Integer> seeStatus(double psend[], double psend_initial[], List<Integer> route){
		List<Integer> psend_actual_status=new ArrayList();
		for(int i=0;i<route.size();i++){//non-exclude destination
			int Node_num=route.get(i);
			if(psend[Node_num]==psend_initial[Node_num]){
				psend_actual_status.add(6);
			}
			else if(psend[Node_num]==psend_initial[Node_num]*0.335){
				psend_actual_status.add(5);
			}
			else if(psend[Node_num]==psend_initial[Node_num]*0.115){
				psend_actual_status.add(4);
			}
			else if(psend[Node_num]==psend_initial[Node_num]*0.0753){
				psend_actual_status.add(3);
			}
			else if(psend[Node_num]==psend_initial[Node_num]*0.0561){
				psend_actual_status.add(2);
			}
			else if(psend[Node_num]==psend_initial[Node_num]*0.0222){
				psend_actual_status.add(1);
			}
			else if(psend[Node_num]==0){
				psend_actual_status.add(0);
			}
			else{
				psend_actual_status.add(7);//compromised
			}
		}
		return psend_actual_status;
	}
	static public void AttackMethod_2(List<Integer> route, int compromisedNode, double[] Energy_remain_theoratical, double[] Energy_initial, double[] psend, double[] psend_initial){
			double Node_energy_remain=Energy_remain_theoratical[route.get(compromisedNode)];
			double Node_energy_initial=Energy_initial[route.get(compromisedNode)];
			int Node_num=route.get(compromisedNode);
			if(Node_energy_remain>=0.84*Node_energy_initial){
				psend[Node_num]=psend_initial[Node_num];
			}
			else if(Node_energy_remain>=0.68*Node_energy_initial&&Node_energy_remain<0.84*Node_energy_initial){
				psend[Node_num]=psend_initial[Node_num]*0.335;
			}
			else if(Node_energy_remain>=0.52*Node_energy_initial&&Node_energy_remain<0.68*Node_energy_initial){
				psend[Node_num]=psend_initial[Node_num]*0.115;
			}
			else if(Node_energy_remain>=0.36*Node_energy_initial&&Node_energy_remain<0.52*Node_energy_initial){
				psend[Node_num]=psend_initial[Node_num]*0.0753;
			}
			else if(Node_energy_remain>=0.2*Node_energy_initial&&Node_energy_remain<0.36*Node_energy_initial){
				psend[Node_num]=psend_initial[Node_num]*0.0561;
			}
			else if(Node_energy_remain>=0.04*Node_energy_initial&&Node_energy_remain<0.2*Node_energy_initial){
				psend[Node_num]=psend_initial[Node_num]*0.0222;
			}
			else{
				psend[Node_num]=0;//node_dead
			}
	}
}
