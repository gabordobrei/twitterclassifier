package hu.bme.tmit.twitterclassifier.classalg;

import java.util.List;

public interface TweetClassifyingAlgorithm {

	public double[] run(List<List<String>> tokenizedTrainTweets, List<List<String>> tokenizedTestTweets);
	
}
