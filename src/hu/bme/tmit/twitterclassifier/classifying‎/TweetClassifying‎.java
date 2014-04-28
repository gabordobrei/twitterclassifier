package hu.bme.tmit.twitterclassifier.classifying‎;

import hu.bme.tmit.twitterclassifier.TweetClass;

import java.io.File;
import java.util.Collection;

public interface TweetClassifying‎ {

	public Collection<TweetClass> classify(File file);

	public void setDebug(boolean debug);

}
