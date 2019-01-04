package com.geektrust.tameofthrones;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.geektrust.tameofthrones.model.Universe;

public class BreakerOfChainsApp {
//	static ThronesManager2 throneManager = new ThronesManager2();
	
	public static void main(String[] args) {
		Universe universe = new Universe();
		universe.setName("Southeros");
		universe.setup();
		System.out.println("1. Who is the ruler of Southeros\n2. Enter the kingdoms competing to be the ruler:\n3. exit");
		Scanner sc = new Scanner(System.in);
		int command = sc.nextInt();
		while(command != 3) {
			switch(command) {
			case 1:
				if(universe.getRuler() ==  null)
					System.out.println("None");
				else 
					System.out.println(universe.getRuler());
				break;
			case 2:
				sc.nextLine();
				String input = sc.nextLine();
				List<String> competingKingdoms = kingdomParser(input);
				universe.runElections(competingKingdoms);
				break;
			case 3:
				break;
			}
			if(command != 4)
				command = sc.nextInt();
		}
	}

	private static List<String> kingdomParser(String nextLine) {
		String[] tokens = nextLine.split(" ");
		return Arrays.asList(tokens) ;
	}
}
