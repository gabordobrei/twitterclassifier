package hu.bme.tmit.twitterclassifier.classalg;

import hu.bme.tmit.twitterclassifier.TweetClass;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class DummyTweetClassifyingAlgorithm implements
		TweetClassifyingAlgorithm {

	@Override
	public Collection<TweetClass> classify(List<String> nouns) {
		return Collections2.transform(nouns, new Function<String, TweetClass>() {

			@Override
			public TweetClass apply(String arg0) {
				return new TweetClass(arg0, arg0);
			}
		});
	}

}
