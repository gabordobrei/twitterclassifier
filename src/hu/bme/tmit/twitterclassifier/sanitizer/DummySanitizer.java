package hu.bme.tmit.twitterclassifier.sanitizer;

public class DummySanitizer implements Sanitezer {

	@Override
	public String sanitize(String word) {
		return word;
	}

}
