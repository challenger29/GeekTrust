package com.geektrust.tameofthrones.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Universe {
	private String name;
	private String ruler;
	private Map<String , Kingdom> kingdoms;
	private Map<String , List<String>> messageSelection = new HashMap<>();
	private List<Message> ballot = new ArrayList<>();

	public Universe() {
		this.kingdoms = new HashMap<>();
	}
	public Universe(String name, Map<String, Kingdom> kingdoms) {
		super();
		this.name = name;
		this.kingdoms = kingdoms;
	}
	
	public String getRuler() {
		return this.ruler;
	}
	public void setRuler(String king) {
		this.ruler = king;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Kingdom> getKingdoms() {
		return this.kingdoms;
	}
	public void setKingdoms(Map<String, Kingdom> kingdoms) {
		this.kingdoms = kingdoms;
	}
	public Map<String, List<String>> getMessageSelection() {
		return this.messageSelection;
	}
	public void setMessageSelection(Map<String, List<String>> messageSelection) {
		this.messageSelection = messageSelection;
	}
	public void setup() {
		Kingdom k1 = new Kingdom("Land" ,"Panda");
		Kingdom k2 = new Kingdom("Water", "Octopus");
		Kingdom k3 = new Kingdom("ice", "mammoth");
		Kingdom k4 = new Kingdom("air", "owl");
		Kingdom k5 = new Kingdom("fire", "dragon");
		Kingdom k6 = new Kingdom("space", "gorilla");
//		kingdoms.addAll(Arrays.asList(k1,k2,k3,k4,k5));
		kingdoms.put("land", k1);
		kingdoms.put("water", k2);
		kingdoms.put("ice", k3);
		kingdoms.put("air", k4);
		kingdoms.put("fire", k5);
		kingdoms.put("space", k6);
		setUpMessages();
		
	}
	
	public void setUpMessages() {
		List<String> airMessages = Arrays.asList("Let’s swing the sword together" , "oaaawaala");
		List <String> landMessages = Arrays.asList("a1d22n333a4444p" , "Die or play the tame of thrones");
		List<String> iceMMessages = Arrays.asList("zmzmzmzaztzozh","Ahoy! Fight for me with men and money");
		List<String> waterMessages = Arrays.asList("Summer is coming", "The quick brown fox jumps over a lazy dog multiple times.");
		List<String> fireMessages = Arrays.asList("Drag on Martin!","A DRAGON IS NOT A SLAVE.");
		List<String> spaceMessages = Arrays.asList("Walar Morghulis: All men must die.","Go ring all the bells");
		messageSelection.put("air", airMessages);
		messageSelection.put("land", landMessages);
		messageSelection.put("water", waterMessages);
		messageSelection.put("ice", iceMMessages);
		messageSelection.put("fire", fireMessages);
		messageSelection.put("space", spaceMessages);
	}

	public void sendMessage(String kingdomName) {
		for(String receiver : kingdoms.keySet()) {
			if(!receiver.equalsIgnoreCase(kingdomName)){
				Random rand = new Random();
				int totalLen = messageSelection.get(receiver).size();
				String message = messageSelection.get(receiver).get(rand.nextInt(totalLen -1));
				Message letter = new Message(kingdomName, receiver, message);
				System.out.println(letter.toString());
				ballot.add(letter);
			}
		}
		
	}
	public void runElections(List<String> competingKingdoms) {
		List<Kingdom> result = ballotProcess(competingKingdoms);
		System.out.println("Results after round one ballot count");
		
		if(result.size() != 0) {
			System.out.println("Results after second round ballot count");
			ballotProcess(competingKingdoms);
		}
		
	}
	
	public List<Kingdom> ballotProcess(List<String> ck) {
		List<Kingdom> competingKingdoms = new ArrayList<>();
		for(String kName : ck) {
			if(kingdoms.containsKey(kName)) {
				Kingdom kingdom = kingdoms.get(kName);
				competingKingdoms.add(kingdom);
				sendMessage(kName);
				
			}
		}
		System.out.println(competingKingdoms.toString());
		List<Message> randomMessages = pickRandomMessage(6);
		System.out.println(randomMessages.toString());
		List<Kingdom> result = computeAllegiance(randomMessages , competingKingdoms);
		return result;
	}

	private List<Kingdom> computeAllegiance(List<Message> randomMessages, List<Kingdom> competingKingdoms) {
		Map<String , List<String>> allies = new HashMap<>();
		Set<String> allegianceGiven = new HashSet<>();
		Set<String> competingKName = new HashSet<>();
		competingKingdoms.forEach(x -> competingKName.add(x.getName()));
		for(Message message : randomMessages) {
			String sender = message.getSender();
			String receiver = message.getReceiver();
			boolean correct = checkMessage(kingdoms.get(receiver).getEmblem(), message.getMessage());
			if(!competingKName.contains(receiver) &&  !allegianceGiven.contains(receiver) && correct) {
				if(!allies.containsKey(sender)) {
					allies.put(sender, new ArrayList<>(Arrays.asList(receiver)));
				}
				else {
					List<String> list = allies.get(sender);
					list.add(receiver);
					allies.put(sender, list);
				}
				allegianceGiven.add(receiver);
			}
		}
		int maxCount = 0;
		List<String> winnerList = new ArrayList<>();
		for(Map.Entry<String, List<String>> entry : allies.entrySet()) {
			if(entry.getValue().size() > maxCount) {
				winnerList.clear();
				winnerList.add(entry.getKey());
				maxCount = entry.getValue().size();
			}
			else if(entry.getValue().size() == maxCount) {
				winnerList.add(entry.getKey());
			}
		}
		
			
		List<Kingdom> winners = new ArrayList<>();
		
		winnerList.forEach(x ->{
			Kingdom k = kingdoms.get(x);
			List<String> alList = allies.get(x);
			alList.forEach(allie -> k.addAllies(kingdoms.get(allie)));
			winners.add(k);
			System.out.println(x + " :" + alList.size());
		});
		return winners;
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


}
