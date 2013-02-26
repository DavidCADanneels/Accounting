package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Exceptions.NotEmptyException;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
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

    private Accounting accounting;

	public Journals(Accounting accounting) {
		super();
        this.accounting = accounting;
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

//	public void setSaved(boolean save) {
//		accounting.setSavedXML(save);
//		accounting.setSavedHTML(save);
//		this.save = save;
//	}

    public Journal addJournal(String name, String abbreviation, JournalType type) throws EmptyNameException, DuplicateNameException {
        if(name==null || "".equals(name.trim())){
            throw new EmptyNameException();
        }
        if(abbreviation==null || "".equals(abbreviation.trim())){
            throw new EmptyNameException();
        }
        if(containsKey(name.trim()) || abbreviations.containsKey(abbreviation.trim())){
            throw new DuplicateNameException();
        }
        Journal journal = new Journal(name.trim(), abbreviation.trim(), type);
        File xmlFile = FileSystemView.getFileSystemView().getChild(accounting.getJournalLocationXml(), name + ".xml");
        File xslFile = FileSystemView.getFileSystemView().getChild(accounting.getLocationXSL(), "Journal.xsl");
        File htmlFile = FileSystemView.getFileSystemView().getChild(accounting.getJournalLocationHtml(), name + ".html");
        journal.setXmlFile(xmlFile);
        journal.setXslFile(xslFile);
        journal.setHtmlFile(htmlFile);
        put(journal.getName(), journal);
        abbreviations.put(journal.getAbbreviation(), journal);
        return journal;
    }

    public void removeJournal(Journal journal) throws NotEmptyException {
        if(journal.getTransactions().isEmpty()){
            remove(journal.getName());
        } else {
            throw new NotEmptyException();
        }
    }

    public Journal renameJournal(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        Journal journal = get(oldName);
        abbreviations.remove(journal.getAbbreviation());
        journal = addJournal(newName, journal.getAbbreviation(),journal.getType());
        remove(oldName);
        return journal;
    }

    public Journal reAbbreviateJournal(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        Journal journal = abbreviations.get(oldName);
        remove(oldName);
        journal = addJournal(newName, journal.getAbbreviation(),journal.getType());
        abbreviations.remove(journal.getAbbreviation());
        return journal;
    }
}