package be.dafke.Accounting.Objects.Accounting;

import java.util.ArrayList;
import java.util.HashMap;

public class JournalTypes extends HashMap<String, JournalType> {

//	private static JournalTypes types = null;
//
//	public static JournalTypes getInstance() {
//		if (types == null) {
//			types = new JournalTypes();
//		}
//		return types;
//	}

	public JournalTypes() {
		put("default", new JournalType());
	}

	public ArrayList<JournalType> getAllTypes() {
		return new ArrayList<JournalType>(values());
	}
}
