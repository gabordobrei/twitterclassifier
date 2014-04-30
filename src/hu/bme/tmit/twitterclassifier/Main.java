package hu.bme.tmit.twitterclassifier;

import hu.bme.tmit.twitterclassifier.classifying.SimpleTweetClassifying;
import hu.bme.tmit.twitterclassifier.classifying.TweetClassifying;
import hu.bme.tmit.twitterclassifier.logger.Logger;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Main {

	final static Logger log = new Logger(Main.class);

	public static void main(String args[]) throws Exception {

		if (args.length < 3) {
			printHelp();
			exit();
		}
		String testPath = null, trainPath = null;
		boolean debug = false;
		for (int i = 0; i < args.length; i++) {
			if ("-test".matches(args[i])) {
				testPath = args[i + 1];
			}
			if ("-train".matches(args[i])) {
				trainPath = args[i + 1];
			}
			if ("-debug".matches(args[i])) {
				debug = Boolean.parseBoolean(args[i + 1]);
			}
		}

		Logger.setLevel(Logger.DEBUG);
		if (!debug) {
			Logger.setLevel(Logger.INFO);
			// log.setPrintStream(new PrintStream(new
			// FileOutputStream("error.log", true)));
		}

		File testTweetSource = new File(testPath);
		File trainTweetSource = new File(trainPath);
		
		if (!testTweetSource.exists() || !trainTweetSource.exists()) {
			log.e("asdf");
			printHelp();
			exit();

		}
		if (!testTweetSource.canRead() || !trainTweetSource.canRead()) {
			log.e("Cannot read file!");
			exit();
		}

		// log.setPrintStream(new PrintStream(new FileOutputStream("log.txt",
		// false)));

		TweetClassifying classifying = new SimpleTweetClassifying(testTweetSource, trainTweetSource);
		log.i("Classifying...");
		classifying.setDebug(debug);

		Stopwatch stopwatch = Stopwatch.createStarted();

		Collection<TweetClass> clazz = classifying.classify();
		log.i("Classified");
		log.i("------------------------------------");
		/*
		 * for (TweetClass tweetClass : clazz) { log.i(tweetClass.toString()); }
		 */
		log.i("Finished!");

		stopwatch.elapsed(TimeUnit.MICROSECONDS);
		log.i(stopwatch.toString());

		/*
		 * PrintStream tweetsFile = new PrintStream(new FileOutputStream(
		 * "tweets.log", false)); // The factory instance is re-useable and
		 * thread safe. Twitter twitter = TwitterFactory.getSingleton();
		 * 
		 * Query query = new Query("#superbowl"); query.setCount(10);
		 * 
		 * QueryResult qr = twitter.search(query); List<Status> qrTweets =
		 * qr.getTweets();
		 * 
		 * if (qrTweets.size() != 0) { Collector c = new Collector(tweetsFile);
		 * for (Status tweet : qrTweets) { if (tweet.getText().length() > 5)
		 * c.print(tweet, true); // log.d(tweet.getText()); } }
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

	}

	private static void exit() {
		log.e("Exiting...");
		System.exit(-1);
	}

	private static void printHelp() {
		System.out.println("Usage:\tTwitterClassifier -test <test_file_name> -train <train_file_name> -debug [true|false]");
	}

}
