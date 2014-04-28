package hu.bme.tmit.twitterclassifier.model;

import java.io.PrintStream;
import twitter4j.Status;
import twitter4j.TweetEntity;

public class Collector {

	// private static final SimpleDateFormat formatter = new
	// SimpleDateFormat("HH:mm:ss:SSS");

	private PrintStream printStream = System.out;

	public Collector(PrintStream ps) {
		this.printStream = ps;
	}

	public void print(Status tweet, boolean clearing) {

		String printableTweet;
		if (clearing) {
			printableTweet = tweet.getText().replaceAll("\n", " ").replaceAll("  ", " ");
		} else {
			printableTweet = tweet.getText();
		}
		
		TweetEntity[] hashTags = tweet.getHashtagEntities();

		for (TweetEntity tweetEntity : hashTags) {
			printableTweet += ", #" + tweetEntity.getText();
		}

		printStream.println(printableTweet);
	}
}
