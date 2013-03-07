package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

public class Accountings {
	private final HashMap<String, Accounting> accountings = new HashMap<String, Accounting>();
	private Accounting currentAccounting = null;
    private final File xmlFile;
    private final File htmlFile;
    private final File xsl2XmlFile;
    private final File xsl2HtmlFile;
    private final File dtdFile;

    public Accountings(){
        File xmlFolder = new File(System.getProperty("Accountings_xml"));
        File xslFolder = new File(System.getProperty("Accountings_xsl"));
        File dtdFolder = new File(System.getProperty("Accountings_dtd"));

        xmlFile = new File(xmlFolder, "Accountings.xml");
        htmlFile = new File(xmlFolder, "Accountings.html");

        xsl2XmlFile = new File(xslFolder, "Accountings2Xml.xsl");
        xsl2HtmlFile = new File(xslFolder, "Accountings2Html.xsl");

        dtdFile = new File(dtdFolder, "Accountings.dtd");
    }

    public void createDefaultValuesIfNull(){
        for(Accounting accounting:getAccountings()){
            accounting.createXmlFolders();
            accounting.createHtmlFolders();
        }
    }

    public File getXmlFile() {
        return xmlFile;
    }

    public File getHtmlFile() {
        return htmlFile;
    }

    public File getXsl2XmlFile() {
        return xsl2XmlFile;
    }

    public File getXsl2HtmlFile() {
        return xsl2HtmlFile;
    }

    public File getDtdFile() {
        return dtdFile;
    }

    public Accounting getCurrentAccounting() {
		return currentAccounting;
	}

	public void addAccounting(Accounting accounting) {
		accountings.put(accounting.toString(), accounting);
	}

	public Collection<Accounting> getAccountings() {
		return accountings.values();
	}

	public void setCurrentAccounting(String name) {
		currentAccounting = accountings.get(name);
	}

	public Accounting addAccounting(String name) throws EmptyNameException, DuplicateNameException {
        if(name==null || "".equals(name.trim())){
            throw new EmptyNameException();
        }
        if(accountings.containsKey(name.trim())){
            throw new DuplicateNameException();
        }
		Accounting accounting = new Accounting();
        accounting.setName(name);
		addAccounting(accounting);
        return accounting;
	}
}
