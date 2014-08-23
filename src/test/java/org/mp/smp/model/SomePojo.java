package org.mp.smp.model;

public class SomePojo {

	private String name;
	private Integer age;

	public SomePojo() {
		this("SomePojo");
	}

	public SomePojo(final String name) {
		super();
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(final Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
