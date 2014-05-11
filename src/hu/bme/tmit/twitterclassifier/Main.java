package hu.bme.tmit.twitterclassifier;

import hu.bme.tmit.twitterclassifier.classifying.SimpleTweetClassifying;
import hu.bme.tmit.twitterclassifier.classifying.TweetClassifying;
import hu.bme.tmit.twitterclassifier.logger.Logger;

import java.io.File;
import java.text.MessageFormat;
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

		TweetClassifying classifying = new SimpleTweetClassifying(trainTweetSource, testTweetSource);
		log.i("Classifying...");
		classifying.setDebug(debug);

		Stopwatch stopwatch = Stopwatch.createStarted();

		double[] clazz = classifying.classify();
		log.i("Classified");
		log.i("------------------------------------");

		try {
			log.i(MessageFormat.format("(egyeb: {0},\tBroncos: {1},\tSeahawks: {2},\tNFL: {3})", clazz[0], clazz[1],
					clazz[2], clazz[3]));
		} catch (NullPointerException e) {
			log.e(e.getMessage());
		}

		log.i("Finished!");

		stopwatch.elapsed(TimeUnit.MICROSECONDS);
		log.i(stopwatch.toString());

	}

	private static void exit() {
		log.e("Exiting...");
		System.exit(-1);
	}

	private static void printHelp() {
		System.out
				.println("Usage:\tTwitterClassifier -test <test_file_name> -train <train_file_name> -debug [true|false]");
	}

}
