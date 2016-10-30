package de.jformchecker.utils;

import java.time.LocalDate;

public class TestBean {
	String firstname;
	String lastname;
	LocalDate birthday;
	boolean playsPiano;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public boolean isPlaysPiano() {
		return playsPiano;
	}

	public void setPlaysPiano(boolean playsPiano) {
		this.playsPiano = playsPiano;
	}
}
