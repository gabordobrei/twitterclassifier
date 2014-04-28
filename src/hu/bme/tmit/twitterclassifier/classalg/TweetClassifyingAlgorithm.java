package hu.bme.tmit.twitterclassifier.classalg;

import hu.bme.tmit.twitterclassifier.TweetClass;

import java.util.Collection;
import java.util.List;

public interface TweetClassifyingAlgorithm {

	public Collection<TweetClass> classify(List<String> nouns);
	
}
