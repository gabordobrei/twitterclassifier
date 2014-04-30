package hu.bme.tmit.twitterclassifier;

import com.google.common.base.Objects;

public class TweetClass {
	
	private String name;
	private int size = 0;
	private String description;

	public TweetClass(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public TweetClass() {
		this(null, null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("name", name).add("size", size).toString();
	}

	
}
