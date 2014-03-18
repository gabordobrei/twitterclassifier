package hu.bme.tmit.twitterclassifier;

import hu.bme.tmit.twitterclassifier.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TweetEntity;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import com.google.common.base.Stopwatch;

public class Main {

	public static void main(String args[]) throws Exception {

		Stopwatch stopwatch = Stopwatch.createStarted();
		final Logger log = new Logger(Main.class);
		Logger.setLevel(Logger.DEBUG);

		List<Status> Seahwaks = new ArrayList<Status>();
		List<Status> Broncos = new ArrayList<Status>();
		List<Status> Dunno = new ArrayList<Status>();

		// The factory instance is re-useable and thread safe.
		Twitter twitter = TwitterFactory.getSingleton();

		for (int page = 1; page <= 10; page++) {
			Query query = new Query("#superbowl");
			query.setCount(10);

			QueryResult qr = twitter.search(query);
			List<Status> qrTweets = qr.getTweets();

			// break out if there are no more tweets
			if (qrTweets.size() == 0)
				break;

			boolean odd = true;

			// separate tweets into good and bad bins
			for (Status t : qrTweets) {
				odd = !odd;
				TweetEntity[] hashTags = t.getHashtagEntities();

				boolean br = false, se = false;
				for (TweetEntity tweetEntity : hashTags) {
					if (tweetEntity.getText().equalsIgnoreCase("broncos")) {
						br = true;
						break;
					} else if (tweetEntity.getText().equalsIgnoreCase(
							"seahwaks")) {
						se = true;
						break;
					}
				}

				if (br) {
					if (!Broncos.contains(t)) {
						Broncos.add(t);
					}
				}
				if (se) {
					if (!Seahwaks.contains(t)) {
						Seahwaks.add(t);
					}
				} else {
					if (!Dunno.contains(t)) {
						Dunno.add(t);
					}
				}

			}
		}

		for (int i = 0; i < Dunno.size(); i++) {
			System.err.println(i + ":\t" + Dunno.get(i).getId());
			log.d(Dunno.get(i).getText());
		}
		for (int i = 0; i < Seahwaks.size(); i++) {
			System.err.println(i + ":\t" + Seahwaks.get(i).getId());
			log.d(Seahwaks.get(i).getText());
		}
		for (int i = 0; i < Broncos.size(); i++) {
			System.err.println(i + ":\t" + Broncos.get(i).getId());
			log.d(Broncos.get(i).getText());
		}

		log.d("Dunno: " + Dunno.size());
		log.d("Seahwaks: " + Seahwaks.size());
		log.d("Broncos: " + Broncos.size());

		stopwatch.elapsed(TimeUnit.MICROSECONDS);

		log.d(stopwatch.toString());

	}

}
