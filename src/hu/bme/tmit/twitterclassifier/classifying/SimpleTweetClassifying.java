package hu.bme.tmit.twitterclassifier.classifying;

import hu.bme.tmit.twitterclassifier.classalg.TweetClassifyingAlgorithm;
import hu.bme.tmit.twitterclassifier.classalg.WekaWrapperTweetClassifiyingAlgorithm;
import hu.bme.tmit.twitterclassifier.lemmatizer.Lemmatizer;
import hu.bme.tmit.twitterclassifier.lemmatizer.SnowballStemmerWrapper;
import hu.bme.tmit.twitterclassifier.sanitizer.DefaultSanitezer;
import hu.bme.tmit.twitterclassifier.sanitizer.Sanitezer;
import hu.bme.tmit.twitterclassifier.stoplist.StoplistFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

import org.tartarus.snowball.ext.englishStemmer;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.primitives.Ints;

public class SimpleTweetClassifying extends BaseTweetClassifying {

	public SimpleTweetClassifying(File trainFile, File testFile) {
		super(trainFile, testFile);
	}

	@Override
	protected Lemmatizer getLemmatizer() {
		return new SnowballStemmerWrapper(new englishStemmer());
	}

	@Override
	protected Tokenizer getTokenizer() throws InvalidFormatException, IOException {
		TokenizerModel tokenModel = null;
		try (FileInputStream modelStream = new FileInputStream("resources/en-token.bin")) {
			tokenModel = new TokenizerModel(modelStream);
		}
		Tokenizer tokenizer = new TokenizerME(tokenModel);
		return tokenizer;

	}

	@Override
	protected Sanitezer getSanitezer() {
		return new DefaultSanitezer();
	}

	@Override
	protected Predicate<String> getPredicate() {
		return new Predicate<String>() {

			@Override
			public boolean apply(String word) {
				return !(word.length() < 3 || Ints.tryParse(word) != null);
			}
		};
	}

	@Override
	protected TweetClassifyingAlgorithm getTweetClassifyingAlgorithm() {
		return new WekaWrapperTweetClassifiyingAlgorithm();
	}

	@Override
	protected Supplier<List<String>> getStoplistSupplier() {
		return new StoplistFactory();
	}

	@Override
	protected POSTagger getPosTagger() throws FileNotFoundException, IOException {
		POSModel posmodel = null;
		try (FileInputStream modelStream = new FileInputStream("resources/en-pos-maxent.bin")) {
			posmodel = new POSModel(modelStream);
		}
		POSTaggerME tagger = new POSTaggerME(posmodel);
		return tagger;
	}

}
