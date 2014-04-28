package hu.bme.tmit.twitterclassifier;

import hu.bme.tmit.twitterclassifier.classifying‎.SimpleTweetClassifying‎;
import hu.bme.tmit.twitterclassifier.classifying‎.TweetClassifying‎;
import hu.bme.tmit.twitterclassifier.logger.Logger;
import hu.bme.tmit.twitterclassifier.model.Collector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import com.google.common.base.Stopwatch;
import com.mysql.jdbc.log.Log;

public class Main {

	final static Logger log = new Logger(Main.class);

	public static void main(String args[]) throws Exception {

		if (args.length < 2) {
			printHelp();
			exit();
		}
		String path = null;
		boolean debug = false;
		for (int i = 0; i < args.length; i++) {
			if ("-f".matches(args[i])) {
				path = args[i + 1];
			}
			if ("-d".matches(args[i])) {
				debug = Boolean.parseBoolean(args[i + 1]);
			}
		}

		Logger.setLevel(Logger.DEBUG);
		if (!debug) {
			Logger.setLevel(Logger.ERROR);
			log.setPrintStream(new PrintStream(new FileOutputStream(
					"error.log", true)));
		}

		File tweetSource = new File(path);
		if (!tweetSource.exists()) {
			printHelp();
			exit();

		}
		if (!tweetSource.canRead()) {
			log.e("Cannot read file!");
			exit();
		}

		TweetClassifying‎ classifying‎ = new SimpleTweetClassifying‎(tweetSource);
		log.i("Linking...");
		classifying‎.setDebug(debug);
		
		Collection<TweetClass> clazz = classifying‎.classify(tweetSource);
		log.i("Classified");
		log.i("------------------------------------");
		for (TweetClass tweetClass : clazz) {
			log.i(tweetClass.toString());
		}
		log.i("Finished!");
		
		
		Stopwatch stopwatch = Stopwatch.createStarted();

		PrintStream tweetsFile = new PrintStream(new FileOutputStream(
				"tweets.log", false));
/*
		// The factory instance is re-useable and thread safe.
		Twitter twitter = TwitterFactory.getSingleton();

		Query query = new Query("#superbowl");
		query.setCount(10);

		QueryResult qr = twitter.search(query);
		List<Status> qrTweets = qr.getTweets();

		if (qrTweets.size() != 0) {
			Collector c = new Collector(tweetsFile);
			for (Status tweet : qrTweets) {
				if (tweet.getText().length() > 5)
					c.print(tweet, true);
				// log.d(tweet.getText());
			}
		}
*/
		/*
		 * List<Status> Seahwaks = new ArrayList<Status>(); List<Status> Broncos
		 * = new ArrayList<Status>(); List<Status> Dunno = new
		 * ArrayList<Status>();
		 * 
		 * for (int page = 1; page <= 10; page++) { boolean odd = true;
		 * 
		 * // separate tweets into good and bad bins
		 * 
		 * for (Status t : qrTweets) { odd = !odd; TweetEntity[] hashTags =
		 * t.getHashtagEntities();
		 * 
		 * boolean br = false, se = false; for (TweetEntity tweetEntity :
		 * hashTags) { if (tweetEntity.getText().equalsIgnoreCase("broncos")) {
		 * br = true; break; } else if (tweetEntity.getText().equalsIgnoreCase(
		 * "seahwaks")) { se = true; break; } }
		 * 
		 * if (br) { if (!Broncos.contains(t)) { Broncos.add(t); } } if (se) {
		 * if (!Seahwaks.contains(t)) { Seahwaks.add(t); } } else { if
		 * (!Dunno.contains(t)) { Dunno.add(t); } }
		 * 
		 * }
		 * 
		 * }
		 * 
		 * for (int i = 0; i < Dunno.size(); i++) { System.err.println(i + ":\t"
		 * + Dunno.get(i).getId()); log.d(Dunno.get(i).getText()); } for (int i
		 * = 0; i < Seahwaks.size(); i++) { System.err.println(i + ":\t" +
		 * Seahwaks.get(i).getId()); log.d(Seahwaks.get(i).getText()); } for
		 * (int i = 0; i < Broncos.size(); i++) { System.err.println(i + ":\t" +
		 * Broncos.get(i).getId()); log.d(Broncos.get(i).getText()); }
		 * 
		 * log.d("Dunno: " + Dunno.size()); log.d("Seahwaks: " +
		 * Seahwaks.size()); log.d("Broncos: " + Broncos.size()); //
		 */

		stopwatch.elapsed(TimeUnit.MICROSECONDS);

		log.d(stopwatch.toString());

	}

	private static void exit() {
		log.e("Exiting...");
		System.exit(-1);
	}

	private static void printHelp() {
		System.out
				.println("Usage:\tTwitterClassifier -f <file_name> -d [true|false]");
	}

}
