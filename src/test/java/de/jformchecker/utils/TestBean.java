package de.jformchecker.utils;

import java.time.LocalDate;

public class TestBean {
	String firstname;
	String lastname;
	LocalDate birthday;
	boolean playsPiano;
	int age;

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

  @Override
  public String toString() {
    return "TestBean [firstname=" + firstname + ", lastname=" + lastname + ", birthday=" + birthday
        + ", playsPiano=" + playsPiano + "]";
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}
