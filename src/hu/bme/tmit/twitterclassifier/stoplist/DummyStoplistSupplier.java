package hu.bme.tmit.twitterclassifier.stoplist;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Supplier;

/**
 * Dummy Stoplist, that provides an empty list.
 */
public class DummyStoplistSupplier implements Supplier<List<String>> {

	@Override
	public List<String> get() {
		return Collections.emptyList();
	}
}