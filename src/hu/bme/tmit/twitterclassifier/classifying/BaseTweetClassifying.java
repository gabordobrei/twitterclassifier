package hu.bme.tmit.twitterclassifier.classifying;

import hu.bme.tmit.twitterclassifier.TweetClass;
import hu.bme.tmit.twitterclassifier.classalg.TweetClassifyingAlgorithm;
import hu.bme.tmit.twitterclassifier.lemmatizer.Lemmatizer;
import hu.bme.tmit.twitterclassifier.logger.Logger;
import hu.bme.tmit.twitterclassifier.sanitizer.Sanitezer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.InvalidFormatException;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public abstract class BaseTweetClassifying implements TweetClassifying {
	public static final Logger log = new Logger(BaseTweetClassifying.class);
	protected boolean debug;
	private File trainFile, testFile;

	public BaseTweetClassifying(File trainFile, File testFile) {
		this.trainFile = trainFile;
		this.testFile = testFile;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	protected abstract Lemmatizer getLemmatizer();

	protected abstract Tokenizer getTokenizer() throws InvalidFormatException, IOException;

	protected abstract Sanitezer getSanitezer();

	protected abstract Predicate<String> getPredicate();

	protected abstract TweetClassifyingAlgorithm getTweetClassifyingAlgorithm();

	protected abstract Supplier<List<String>> getStoplistSupplier();

	protected abstract POSTagger getPosTagger() throws FileNotFoundException, IOException;

	@Override
	public Collection<TweetClass> classify() {

		/* Configuration */
		try {
			List<List<String>> tokenizedTrainTweets = new ArrayList<List<String>>();
			List<List<String>> tokenizedTestTweets = new ArrayList<List<String>>();

			final Tokenizer tokenizer = getTokenizer();
			TweetClassifyingAlgorithm classifyingAlgorithm = getTweetClassifyingAlgorithm();
			Sanitezer sanitezer = getSanitezer();
			Supplier<List<String>> stoplistSupplier = getStoplistSupplier();
			POSTagger posTagger = getPosTagger();
			Lemmatizer lemmatizer = getLemmatizer();
			Predicate<String> predicate = getPredicate();
			Set<String> stopList = new HashSet<>(stoplistSupplier.get());

			final Stopwatch stopwatch = Stopwatch.createStarted();

			/********************************************************/
			/* Tokenize */
			List<String[]> lines = Files.readLines(trainFile, Charsets.UTF_8, new LineProcessor<List<String[]>>() {
				List<String[]> result = new ArrayList<String[]>();

				public boolean processLine(String line) {
					log.d("train: - " + stopwatch.toString());

					result.add(tokenizer.tokenize(line));
					return true;
				}

				public List<String[]> getResult() {
					return result;
				}

			});
			
			stopwatch.elapsed(TimeUnit.MICROSECONDS);
			log.d("train: - " + stopwatch.toString());


			for (String[] toks : lines) {
				// String[] toks;
				// toks = tokenizer.tokenize(CharStreams.toString(new
				// FileReader(tweetsFile)));
				List<String> toksTemp = new ArrayList<>();

				/* Stoplist's Predicate */
				for (String token : toks) {
					if (!stopList.contains(token) && predicate.apply(token)) {
						toksTemp.add(sanitezer.sanitize(token));
					}
				}
				toks = toksTemp.toArray(new String[toksTemp.size()]);

				/* POS Tagging */
				String[] tags = posTagger.tag(toks);

				/* Collecting NOUNS (and stemming..) */
				List<String> nounsInLine = new ArrayList<>();
				for (int i = 0; i < tags.length; i++) {

					if (tags[i].startsWith("NN")) {
						String word = toks[i];

						/* Plural, stemming */
						if (tags[i].endsWith("S")) {
							lemmatizer.setCurrent(toks[i]);
							if (lemmatizer.lemmatize()) {
								word = lemmatizer.getCurrent();
								if (debug) {
									//log.d(MessageFormat.format("Stemming: {0} -> {1}", toks[i], word));
								}
							}
						}
						nounsInLine.add(word);
					}
				}
				tokenizedTrainTweets.add(nounsInLine);
			}

			/********************************************************/
			/* Tokenize */
			lines = Files.readLines(testFile, Charsets.UTF_8, new LineProcessor<List<String[]>>() {
				List<String[]> result = new ArrayList<String[]>();

				public boolean processLine(String line) {
					result.add(tokenizer.tokenize(line));
					return true;
				}

				public List<String[]> getResult() {
					return result;
				}

			});
			stopwatch.elapsed(TimeUnit.MICROSECONDS);
			log.i("test: - " + stopwatch.toString());

			for (String[] toks : lines) {
				// String[] toks;
				// toks = tokenizer.tokenize(CharStreams.toString(new
				// FileReader(tweetsFile)));
				List<String> toksTemp = new ArrayList<>();

				/* Stoplist's Predicate */
				for (String token : toks) {
					if (!stopList.contains(token) && predicate.apply(token)) {
						toksTemp.add(sanitezer.sanitize(token));
					}
				}
				toks = toksTemp.toArray(new String[toksTemp.size()]);

				/* POS Tagging */
				String[] tags = posTagger.tag(toks);

				/* Collecting NOUNS (and stemming..) */
				List<String> nounsInLine = new ArrayList<>();
				for (int i = 0; i < tags.length; i++) {

					if (tags[i].startsWith("NN")) {
						String word = toks[i];

						/* Plural, stemming */
						if (tags[i].endsWith("S")) {
							lemmatizer.setCurrent(toks[i]);
							if (lemmatizer.lemmatize()) {
								word = lemmatizer.getCurrent();
								if (debug) {
									log.d(MessageFormat.format("Stemming: {0} -> {1}", toks[i], word));
								}
							}
						}
						nounsInLine.add(word);
					}
				}
				tokenizedTestTweets.add(nounsInLine);
			}
			/********************************************************/

			/* Classifying */
			for (List<String> nounsInLine : tokenizedTrainTweets) {
				log.d("---");
				for (String n : nounsInLine) {
					log.d(n);
				}
			}
			for (List<String> nounsInLine : tokenizedTestTweets) {
				log.d("---");
				for (String n : nounsInLine) {
					log.d(n);
				}
			}

			Collection<TweetClass> clazz = new ArrayList<>();
			// clazz = classifyingAlgorithm.run(tokenizedTrainTweets,
			// tokenizedTestTweets);

			/* Wohohoho! We're done! */
			return clazz;
		} catch (IOException e) {
			log.e(e.getMessage());
			return null;
		}
	}

}
