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
    private File locationHtml;
    private File locationXml;
//    private File xmlFile, htmlFile;

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
        File xmlFile = FileSystemView.getFileSystemView().getChild(locationXml, journal.getName() + ".xml");
        File xslFile = FileSystemView.getFileSystemView().getChild(accounting.getLocationXSL(), "Journal.xsl");
        File htmlFile = FileSystemView.getFileSystemView().getChild(locationHtml, journal.getName() + ".html");
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
            abbreviations.remove(journal.getAbbreviation());
        } else {
            throw new NotEmptyException();
        }
    }

    public Journal modifyJournalName(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        if(newName==null || "".equals(newName.trim())){
            throw new EmptyNameException();
        }
        Journal journal = get(oldName);
        remove(oldName);
        if(containsKey(newName.trim())){
            put(oldName, journal);
            throw new DuplicateNameException();
        }
        put(newName, journal);
        journal.setName(newName.trim());
        return journal;
    }

    public Journal modifyJournalAbbreviation(String oldAbbreviation, String newAbbreviation) throws EmptyNameException, DuplicateNameException {
        if(newAbbreviation==null || "".equals(newAbbreviation.trim())){
            throw new EmptyNameException();
        }
        Journal journal = abbreviations.get(oldAbbreviation);
        abbreviations.remove(oldAbbreviation);
        if(abbreviations.containsKey(newAbbreviation.trim())){
            abbreviations.put(oldAbbreviation, journal);
            throw new DuplicateNameException();
        }
        abbreviations.put(newAbbreviation,journal);
        journal.setAbbreviation(newAbbreviation.trim());
        return journal;
    }

    public void setLocationHtml(File locationHtml) {
        this.locationHtml = locationHtml;
        if(!this.locationHtml.exists()){
            this.locationHtml.mkdir();
        }
//        htmlFile = FileSystemView.getFileSystemView().getChild(this.locationHtml, "Journals.html");
    }

    public File getLocationHtml(){
        return locationHtml;
    }

    public void setLocationXml(File locationXml) {
        this.locationXml = locationXml;
        if(!this.locationXml.exists()){
            this.locationXml.mkdir();
        }
//        xmlFile = FileSystemView.getFileSystemView().getChild(this.locationXml, "Journals.xml");
    }

    public File getLocationXml(){
        return locationXml;
    }
}