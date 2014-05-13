package hu.bme.tmit.twitterclassifier.classalg;

import hu.bme.tmit.twitterclassifier.logger.Logger;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class WekaWrapperTweetClassifiyingAlgorithm implements TweetClassifyingAlgorithm {

	public static final Logger log = new Logger(WekaWrapperTweetClassifiyingAlgorithm.class);
	private static final Map<String, Integer> seahawks = new HashMap<String, Integer>();
	private static final Map<String, Integer> nfl = new HashMap<String, Integer>();
	private static final Map<String, Integer> reklam = new HashMap<String, Integer>();
	private static final Map<String, Integer> broncos = new HashMap<String, Integer>();

	final int seahawksCount = 60;
	final int nflCount = 229;
	final int reklamCount = 261;
	final int broncosCount = 45;

	private final int[] classProbabilities = { seahawksCount, nflCount, reklamCount, broncosCount };

	@Override
	public double[] run(List<List<String>> tokenizedTrainTweets, List<List<String>> tokenizedTestTweets) {
		final Stopwatch stopwatch = Stopwatch.createStarted();
		train(tokenizedTrainTweets);

		stopwatch.elapsed(TimeUnit.MICROSECONDS);
		log.i("Training in " + stopwatch.toString());

		stopwatch.stop();
		stopwatch.start();
		double[] d = test(tokenizedTestTweets);

		stopwatch.elapsed(TimeUnit.MICROSECONDS);
		log.i("Testing in " + stopwatch.toString());
		return d;
	}

	private double[] test(List<List<String>> tokenizedTestTweets) {

		int[] classCounts = { 0, 0, 0, 0 };
		double fullCount = 0.0;
		double[] decision = new double[4];
		int idx = 0;
		for (List<String> tweet : tokenizedTestTweets) {
			/** tweetenként nézzük **/

			for (int i = 0; i < 4; i++) {
				decision[i] = Math.log((double) classProbabilities[i] / 595.0);
			}

			for (String word : tweet) {
				for (int i = 0; i < 4; i++) {
					decision[i] += Math.log(getMagic(word, i));

				}

			}

			double m = Math.max(Math.max(decision[0], decision[1]), Math.max(decision[2], decision[3]));

			for (int i = 0; i < decision.length; i++) {
				if (Double.compare(decision[i], m) == 0) {

					// System.err.println(MessageFormat.format("{0}: {1}->{2} ({3})",
					// ++idx, idx / 25, i, tweet.toString()));
					System.err.println(MessageFormat.format("{0}", i));
					classCounts[i]++;
				}
			}

			fullCount += 1.0;

		}

		double[] toReturn = new double[4];

		try {
			for (int i = 0; i < 4; i++) {
				toReturn[i] = classCounts[i] / fullCount;
			}
		} catch (Exception e) {
			return null;
		}

		return toReturn;
	}

	private double getMagic(String word, int idx) {

		double c;

		switch (idx) {
		case 0:

			c = seahawks.containsKey(word) ? (double) seahawks.get(word) : 0.0;
			return (c + 1.0) / (60.0 + seahawks.size());

		case 1:
			c = nfl.containsKey(word) ? (double) nfl.get(word) : 0.0;
			return (c + 1.0) / (60.0 + nfl.size());

		case 2:
			c = reklam.containsKey(word) ? (double) reklam.get(word) : 0.0;
			return (c + 1.0) / (60.0 + reklam.size());
		case 3:
			c = broncos.containsKey(word) ? (double) broncos.get(word) : 0.0;
			return (c + 1.0) / (60.0 + broncos.size());

		}

		return 0;
	}

	private void train(List<List<String>> tokenizedTrainTweets) {
		for (int i = 0; i < seahawksCount; i++) {
			for (String s : tokenizedTrainTweets.get(i)) {
				if (seahawks.containsKey(s)) {
					seahawks.put(s, seahawks.get(s) + 1);
				} else {
					seahawks.put(s, 1);
				}
			}
		}

		for (int i = seahawksCount; i < seahawksCount + nflCount; i++) {
			for (String s : tokenizedTrainTweets.get(i)) {
				if (nfl.containsKey(s)) {
					nfl.put(s, nfl.get(s) + 1);
				} else {
					nfl.put(s, 1);
				}
			}
		}

		for (int i = seahawksCount + nflCount; i < seahawksCount + nflCount + reklamCount; i++) {
			for (String s : tokenizedTrainTweets.get(i)) {
				if (reklam.containsKey(s)) {
					reklam.put(s, reklam.get(s) + 1);
				} else {
					reklam.put(s, 1);
				}
			}
		}

		for (int i = seahawksCount + nflCount + reklamCount; i < seahawksCount + nflCount + reklamCount + broncosCount; i++) {
			for (String s : tokenizedTrainTweets.get(i)) {
				if (broncos.containsKey(s)) {
					broncos.put(s, broncos.get(s) + 1);
				} else {
					broncos.put(s, 1);
				}
			}
		}

	}
}