package com.geektrust.tameofthrones;

import java.util.List;
import java.util.Scanner;

import com.geektrust.tameofthrones.model.Kingdom;
import com.geektrust.tameofthrones.model.Ruler;

public class GoldenCrownApp {
	static ThronesManager throneManager = new ThronesManager();
	
	public static void main(String[] args) {
		throneManager.setup();
		System.out.println("1. Who is the ruler of Southeros\n2. Allies of Ruler\n3. Send Message\n4. exit");
		Scanner sc = new Scanner(System.in);
		int command = sc.nextInt();
		while(command != 4) {
			switch(command) {
			case 1:
				if(throneManager.getRuler() ==  null)
					System.out.println("None");
				else 
					System.out.println(throneManager.getRuler().getName());
				break;
			case 2:
				if(throneManager.getRuler() ==  null)
					System.out.println("None");
				else {
					List<Kingdom> allies= throneManager.getRuler().getAllies();
					allies.forEach(allie -> System.out.print(allie.getName()+ " ,"));

				}
				break;
			case 3:
				Kingdom k6 = new Kingdom("space", "gorilla");
				Ruler ruler = new Ruler("King Shan", k6);
//				throneManager.generate(ruler);
				
				//manually enter
				int input = sc.nextInt();
				sc.nextLine();
				for(int i = 0 ; i < input; i++) {
					String str=sc.nextLine();
					String[] tokens = messageParser(str);
					if(tokens.length == 2) {
						String kingdom = tokens[0].toLowerCase();
						String message = tokens[1].toLowerCase();
						throneManager.addMessage(kingdom, message);
					}
				}
				throneManager.updateRuler(ruler);
				break;
			case 4:
				break;
			}
			if(command != 4)
				command = sc.nextInt();
		}
	}

	private static String[] messageParser(String nextLine) {
		String[] tokens = nextLine.split(",");
		return tokens;
	}
}
