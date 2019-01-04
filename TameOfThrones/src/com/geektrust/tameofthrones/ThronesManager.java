package com.geektrust.tameofthrones;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import com.geektrust.tameofthrones.model.Kingdom;
import com.geektrust.tameofthrones.model.Message;

public class ThronesManager {
	Set<Kingdom> kingdoms = new HashSet<>();
	Map<String , Kingdom> kingdomMap = new HashMap<>();
	Ruler southeros;
	final int MIN = 3;
	Map<String, String> messages = new HashMap<>();
	Set<String> allegianceGiven = new HashSet<>();
	Set<String> competingKingdoms = new HashSet<>();
	Map<String , Ruler> competingRulers = new HashMap<>();
	List<Message> ballot = new ArrayList<>();
	
	
	public List<String> generate(Ruler ruler) {
		Random r = new Random();
		List<Kingdom> kingdomList = new ArrayList<>(kingdoms);
		int messageCount = r.nextInt(kingdoms.size()- MIN) + MIN;
		System.out.println(messageCount);
		for(int i = 0 ; i < messageCount ; i++) {
			int randomIndex = r.nextInt(kingdomList.size() -1);
			Kingdom k = kingdomList.get(randomIndex);
			System.out.println("Kingdom picked  : " + k.toString());
			StringBuilder str = new StringBuilder();
			str.append(shuffle(k.getEmblem()));
			System.out.println("Message generated " + str.toString());
			messages.put(k.getName(), str.toString());
			kingdomList.remove(randomIndex);
		}
		
		return null;
	}
	public void setup() {
		Kingdom k1 = new Kingdom("Land" ,"Panda");
		Kingdom k2 = new Kingdom("Water", "Octopus");
		Kingdom k3 = new Kingdom("ice", "mammoth");
		Kingdom k4 = new Kingdom("air", "owl");
		Kingdom k5 = new Kingdom("fire", "dragon");
		Kingdom k6 = new Kingdom("space", "gorilla");
		kingdoms.addAll(Arrays.asList(k1,k2,k3,k4,k5));
		kingdomMap.put("land", k1);
		kingdomMap.put("water", k2);
		kingdomMap.put("ice", k3);
		kingdomMap.put("air", k4);
		kingdomMap.put("fire", k5);
		kingdomMap.put("space", k6);
		
	}
	
	static String shuffle(String string){

	    List<Character> list = string.chars().mapToObj(c -> new Character((char) c))
	                                         .collect(Collectors.toList());
	    Collections.shuffle(list);
	    StringBuilder sb = new StringBuilder();
	    list.forEach(c -> sb.append(c));
	    int length = sb.length();
	    StringBuilder sb2 = new StringBuilder();
	    for(int i =0 ; i <length ;i++) {
	    	sb2.append("z");
	    	sb2.append(sb.charAt(i));
	    }
	    return sb2.toString();
	}
	public Ruler getRuler() {
		return this.southeros;
	}
	
	public void updateRuler(Ruler ruler) {
		messages.forEach((k,v) -> {
			Kingdom kingdom = kingdomMap.get(k);
			String emblem = kingdom.getEmblem().toLowerCase();
			if(checkMessage(emblem, v)) {
				ruler.addAllies(kingdom);
			}
		});
		if(ruler.getAllies().size() >=1) {
			southeros = ruler;
		}
	}
	public void addMessage(String kingdom, String message) {
		if(!messages.containsKey(kingdom)){
			messages.put(kingdom, message);
		}
		else {
			System.out.println("Message already sent to kingdom! "+ kingdom);
		}
	}
	public void clearMessage() {
		messages.clear();
	}
	
	public boolean checkMessage(String emblem , String message) {
		Map<Character , Integer> chMap = new HashMap<>();
		for(int i = 0 ; i <message.length();i++) {
			Character ch = message.charAt(i);
			if(chMap.containsKey(ch)) {
				chMap.merge(ch, 1, Integer::sum);
			}else {
				chMap.put(ch, 1);
			}
		}
//		System.out.println(chMap.toString());
		for(int i = 0 ; i < emblem.length();i++) {
			Character ch = emblem.charAt(i);
			if(chMap.containsKey(ch)) {
				if(chMap.get(ch)== 0)
					return false;
				else
					chMap.merge(ch, -1, Integer::sum);
			}else {
				return false;
			}
//			System.out.println(chMap.toString());
		}
		return true;
	}
	public void runElections(List<String> competingKingdoms) {
		ballotProcess(competingKingdoms);
		boolean tie = false;
		System.out.println("Results after round one ballot count");
		if(tie) {
			//reduce competingKingdoms
			ballotProcess(competingKingdoms);
		}
	}

	private List<Message> pickRandomMessage(int count) {
		List<Message> selected = new ArrayList<>();
		Random rand = new Random();
		for(int i = 0 ; i < count ; i++ ) {
			if (ballot.size() != 0) {
				int randomIndex = rand.nextInt(ballot.size() - 1);
				Message randomElement = ballot.get(randomIndex);
				selected.add(randomElement);
				ballot.remove(randomIndex);
			}
		}
		return selected;
	}
	
	public void sendMessage(String kingdomName) {
		Random rand = new Random();
		for(Kingdom kingdom : kingdoms) {
			if(!kingdom.getName().toLowerCase().equals(kingdomName)) {
				int suggestion = rand.nextInt(1);
				String messageBody  = "";
				if(suggestion == 0) {
					messageBody = generateCorrectMessage(kingdom);
					System.out.println(messageBody);
					
				}else {
					messageBody = generateIncorrectMessage(kingdom);
					System.out.println(messageBody);
				}
				Message message = new Message(kingdomName, kingdom.getName(), messageBody);
				ballot.add(message);
			}

		}
//		kingdoms.forEach(kingdom ->{
//			if(!kingdom.getName().equals(kingdomName)) {
//				int suggestion = rand.nextInt(1);
//				String messageBody  = "";
//				if(suggestion == 0) {
//					messageBody = generateCorrectMessage(kingdom);
//					System.out.println(messageBody);
//					
//				}else {
//					messageBody = generateIncorrectMessage(kingdom);
//					System.out.println(messageBody);
//				}
//				Message message = new Message(kingdomName, kingdom.getName(), messageBody);
//				ballot.add(message);
//			}
//		});
		System.out.println(ballot.toString());
	}
	
	private String generateIncorrectMessage(Kingdom kingdom) {
		return "something";
	}
	private String generateCorrectMessage(Kingdom kingdom) {
		Scanner sc;
		String result = "default blah";
		try {
			sc = new Scanner(new File("/Users/z002r24/Documents/GeekTrust/TameOfThrones/data/messages.txt"));
		    Random rand = new Random();
		    int n = 0;
		    for(; sc.hasNext(); )
		    {
		        ++n;
		        String line = sc.nextLine();
		        result = line;
		        if(checkMessage(kingdom.getEmblem(), result))
		        {
		        	System.out.println("found "+kingdom.getEmblem()+" " + line);
		        	break;
		        }

//		        if(rand.nextInt(n) == 0) {
//		           result = line;
//		           if(checkMessage(kingdom.getEmblem(), result))
//		        	   break;
//		        }
		     }
		     sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println(result);
	     return result;      

	}
	public void ballotProcess(List<String> ck) {
		competingKingdoms.clear();
		competingRulers.clear();
		for(String kingdomName : ck) {
			competingKingdoms.add(kingdomName);
			sendMessage(kingdomName);
			Ruler ruler = new Ruler();
			ruler.setKingdom(kingdomMap.get(kingdomName));
			competingRulers.put(kingdomName, ruler);

		}
//		competingKingdoms.forEach(kingdom -> {
//			sendMessage(kingdom);
//			Ruler ruler = new Ruler();
//			ruler.setKingdom(kingdomMap.get(kingdom));
//			competingRulers.put(kingdom, ruler);
//		});
		List<Message> randomMessages = pickRandomMessage(6);
		computeAllegiance(randomMessages);
	}
	private void computeAllegiance(List<Message> randomMessages) {
		randomMessages.forEach(message -> {
			String emblem = kingdomMap.get(message.getReceiver()).getEmblem();
			boolean correct = checkMessage(emblem, message.getMessage());
			if(correct) {
				if( !competingKingdoms.contains(message.getReceiver()) ) {
					if ( !allegianceGiven.contains(message.getReceiver())){
						allegianceGiven.add(message.getReceiver());
						if(competingRulers.containsKey(message.getSender())){
							competingRulers.get(message.getSender()).addAllies(kingdomMap.get(message.getReceiver()));
						}
					}
				}
			}
		});
		
	}
}

