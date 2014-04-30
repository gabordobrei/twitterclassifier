package hu.bme.tmit.twitterclassifier.classifying;

import hu.bme.tmit.twitterclassifier.TweetClass;

import java.util.Collection;

public interface TweetClassifying {

	public Collection<TweetClass> classify();

	public void setDebug(boolean debug);

}
