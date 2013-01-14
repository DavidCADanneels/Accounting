package be.dafke.Accounting.Objects.Accounting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class JournalTypes extends HashMap<String, JournalType> implements Serializable {
//	private static JournalTypes types = null;
//
//	public static JournalTypes getInstance() {
//		if (types == null) {
//			types = new JournalTypes();
//		}
//		return types;
//	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JournalTypes() {
		put("<default>", new JournalType());
	}

	public ArrayList<JournalType> getAllTypes() {
		return new ArrayList<JournalType>(values());
	}
}
