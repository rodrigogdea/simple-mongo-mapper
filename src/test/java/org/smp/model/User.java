package org.smp.model;

public class User {

	private boolean isAdmin;
	private String name;

	public User(String name, boolean isAdmin) {
		this.name = name;
		this.isAdmin = isAdmin;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public String getName() {
		return name;
	}

}
