package hu.bme.tmit.twitterclassifier.lemmatizer;

public interface Lemmatizer {
	
	void setCurrent(String word);

	boolean lemmatize();

	String getCurrent();

}
