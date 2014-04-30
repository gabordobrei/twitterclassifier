package hu.bme.tmit.twitterclassifier.classalg;

import hu.bme.tmit.twitterclassifier.TweetClass;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class DummyTweetClassifyingAlgorithm implements TweetClassifyingAlgorithm {

	@Override
	public Collection<TweetClass> classify(List<List<String>> tokenizedTrainTweets,
			List<List<String>> tokenizedTestTweets) {
		
		return Collections2.transform(tokenizedTrainTweets, new Function<List<String>, TweetClass>() {

			@Override
			public TweetClass apply(List<String> strList) {
				return strList.size() > 3 ? new TweetClass("Broncos", "mert hosszú") : new TweetClass("Seahowks",
						"mert rövid");
			}
		});
	}

}
