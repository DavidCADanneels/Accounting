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

    private File xmlFile;
    private File xsl2XmlFile;
    private File xsl2HtmlFile;
    private File dtdFile;
    private File htmlFile;
    private Journal currentJournal;

    public Journals() {
		super();
//		save = false;
		abbreviations = new HashMap<String, Journal>();
	}

    public Journal getCurrentJournal() {
        return currentJournal;
    }
    //
    public void setCurrentJournal(Journal journal) {
        currentJournal = journal;
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

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public File getXmlFile() {
        return xmlFile;
    }

    public void setXsl2XmlFile(File xsl2XmlFile) {
        this.xsl2XmlFile = xsl2XmlFile;
    }

    public File getXsl2XmlFile() {
        return xsl2XmlFile;
    }

    public File getXsl2HtmlFile() {
        return xsl2HtmlFile;
    }

    public void setXsl2HtmlFile(File xsl2HtmlFile) {
        this.xsl2HtmlFile = xsl2HtmlFile;
    }

    public File getDtdFile() {
        return dtdFile;
    }

    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }

    public File getHtmlFile() {
        return htmlFile;
    }

    public void setDefaultHtmlFolderAndFiles(Accounting accounting, String name, boolean overwrite){
        File htmlFolder = accounting.getHtmlFolder();
        if(overwrite || htmlFile == null || htmlFile.getPath().equals("null")){
            htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, name+".html");
        }
        File subFolder = FileSystemView.getFileSystemView().getChild(htmlFolder, name);
        subFolder.mkdirs();
        for(Journal journal: getAllJournals()){
            journal.setHtmlFile(FileSystemView.getFileSystemView().getChild(subFolder, journal.getName() + ".html"));
        }
    }

    public void setDefaultXmlFolderAndFiles(Accounting accounting, String name, boolean overwrite) {
        File xmlFolder = accounting.getXmlFolder();
        File xslFolder = accounting.getXslFolder();
        File dtdFolder = accounting.getDtdFolder();
        if(overwrite || xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Journals.xml");
        }
        if(overwrite || xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Journals2xml.xsl");
        }
        if(overwrite || xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Journals2html.xsl");
        }
        if(overwrite || dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(dtdFolder, "Journals.dtd");
        }
        File subFolder = FileSystemView.getFileSystemView().getChild(xmlFolder, name);
        subFolder.mkdirs();
        for(Journal journal: getAllJournals()){
            journal.setXmlFile(FileSystemView.getFileSystemView().getChild(subFolder, journal.getName() + ".xml"));
            journal.setDtdFile(FileSystemView.getFileSystemView().getChild(dtdFolder, "Journal.dtd"));
            journal.setXsl2XmlFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Journal2xml.xsl"));
            journal.setXsl2HtmlFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Journal2html.xsl"));
        }
    }
}