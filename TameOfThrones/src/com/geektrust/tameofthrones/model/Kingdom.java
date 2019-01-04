package com.geektrust.tameofthrones.model;

import java.util.ArrayList;
import java.util.List;

public class Kingdom {
	private String name;
	private String emblem;
	private String king;
	private List<Kingdom> allies;

	
	public Kingdom(String name, String emblem) {
		super();
		this.name = name;
		this.emblem = emblem;
		this.allies = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmblem() {
		return emblem;
	}

	public void setEmblem(String emblem) {
		this.emblem = emblem;
	}
	public List<Kingdom> getAllies() {
		return allies;
	}
	public void setAllies(List<Kingdom> allies) {
		this.allies = allies;
	}

	public void addAllies(Kingdom kingdom2) {
		allies.add(kingdom2);
	}
	
	public boolean acceptAllegiance() {
		return false;
	}
	@Override
	public String toString() {
		return "Kingdom [name=" + name + ", emblem=" + emblem + "]";
	}

	
}
