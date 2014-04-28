package hu.bme.tmit.twitterclassifier.classifying‎;

import hu.bme.tmit.twitterclassifier.classalg.TweetClassifyingAlgorithm;
import hu.bme.tmit.twitterclassifier.lemmatizer.Lemmatizer;
import hu.bme.tmit.twitterclassifier.logger.Logger;
import hu.bme.tmit.twitterclassifier.sanitizer.Sanitezer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.InvalidFormatException;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

public abstract class BaseTweetClassifying implements TweetClassifying‎ {
	public static final Logger log = new Logger(BaseTweetClassifying.class);
	protected boolean debug;
	private File tweetsFile;
	
	public BaseTweetClassifying(File file) {
		this.tweetsFile = file;
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
	
}
