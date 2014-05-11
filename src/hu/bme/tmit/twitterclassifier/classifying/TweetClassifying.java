package hu.bme.tmit.twitterclassifier.classifying;


public interface TweetClassifying {

	public double[] classify();

	public void setDebug(boolean debug);

}
