package hu.bme.tmit.twitterclassifier.sanitizer;

public class DefaultSanitezer implements Sanitezer {

	@Override
	public String sanitize(String word) {
		
		word = word.replaceAll("\"", "");
		if (word.startsWith("-")) {
			word = word.substring(1, word.length());
		}
		return word;
	}

}
