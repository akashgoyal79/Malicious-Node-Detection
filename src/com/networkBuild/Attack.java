package com.networkBuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import com.NodeProperty.Node;

public class Attack {
	public void attack(double[] psend, double[] psend_initial, List<Integer>route, int attackMethod, int compromisedNode, double ETX, double Efs, Map<Integer,Node> nodeProperty, double[] Energy_remain_theoratical, double[] Energy_remain_real, double[] Energy_initial, Random r3){
		//how to attack the network, still need to be implemented
		switch(attackMethod){
			case 1://physical damage
				psend[route.get(compromisedNode)]=0;
				break;
			case 2://jamming
				int l_malicious=100;
				double EnergyWaste_malicious=l_malicious*psend[route.get(compromisedNode)]*(ETX+Efs*nodeProperty.get(route.get(compromisedNode)).distance[route.get(compromisedNode-1)]);
				Energy_remain_theoratical[route.get(compromisedNode)]-=EnergyWaste_malicious;
				AdjustPsend.AttackMethod_2(route, compromisedNode, Energy_remain_theoratical, Energy_initial, psend, psend_initial);
				break;
			case 3://man in the middle
				Energy_remain_real[route.get(compromisedNode)]-=(100-70)*psend[route.get(compromisedNode)]*ETX;
				AdjustPsend.AttackMethod_2(route, compromisedNode, Energy_remain_real, Energy_initial, psend, psend_initial);
				break;
			case 4:
				boolean semi=(r3.nextDouble()<=0.3)?true:false;//30% chance send with random(compromised) speed
				if(semi) psend[route.get(compromisedNode)]*=r3.nextDouble();
				break;
		}
	}
}
	