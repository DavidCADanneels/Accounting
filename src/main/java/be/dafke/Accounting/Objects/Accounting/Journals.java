package be.dafke.Accounting.Objects.Accounting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Serialiseerbare map die alle dagboeken bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journals extends HashMap<String, Journal> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final HashMap<String, Journal> abbreviations;

//	private boolean save;

	/**
	 * Constructor
	 * @see HashMap<String, Journal>
	 */
	public Journals() {
		super();
//		save = false;
		abbreviations = new HashMap<String, Journal>();
	}

	/**
	 * Geeft alle dagboeken terug behalve het gegeven dagboek
	 * @param j het dagboek dat we willen uitsluiten
	 * @return alle dagboeken behalve het gegeven dagboek
	 */
	public ArrayList<Journal> getAllJournalsExcept(Journal j) {
		ArrayList<Journal> result = new ArrayList<Journal>(values());
		result.remove(j);
		return result;
	}

	public ArrayList<Journal> getAllJournals() {
		return new ArrayList<Journal>(values());
	}

	public void rename(String oldName, String newName) {
		Journal journal = get(oldName);
		journal.setName(newName);
		remove(oldName);
		put(newName, journal);
	}

//	public void setSaved(boolean save) {
//		accounting.setSavedXML(save);
//		accounting.setSavedHTML(save);
//		this.save = save;
//	}

	public void add(Journal journal) {
		super.put(journal.getName(), journal);
		abbreviations.put(journal.getAbbreviation(), journal);
	}

	public boolean containsAbbreviation(String abbr) {
		return abbreviations.containsKey(abbr);
	}

	public void reAbbrev(String oldName, String newName) {
		Journal journal = abbreviations.get(oldName);
		journal.setAbbreviation(newName);
		abbreviations.remove(oldName);
		abbreviations.put(newName, journal);
	}
}