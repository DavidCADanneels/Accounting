package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

public class Accountings {
	private final HashMap<String, Accounting> accountings = new HashMap<String, Accounting>();
	private Accounting currentAccounting = null;
    private final File xmlFolder;
    private final File htmlFolder;
    private final File xslFolder;
    private final File dtdFolder;
    private final File xmlFile;
    private final File htmlFile;
    private final File xsl2XmlFile;
    private final File xsl2HtmlFile;
    private final File dtdFile;

    public Accountings(){
        File home = new File(System.getProperty("user.home"));

        xmlFolder = new File(home, "Accounting");
        xmlFile = new File(xmlFolder, "Accountings.xml");

        htmlFolder = new File(home, "Accounting");
        htmlFile = new File(htmlFolder, "Accountings.html");

        xslFolder = new File(xmlFolder, "xsl");
        xsl2XmlFile = new File(xslFolder, "Accountings2Xml.xsl");
        xsl2HtmlFile = new File(xslFolder, "Accountings2Html.xsl");

        dtdFolder = new File(xmlFolder, "xsl");
        dtdFile = new File(dtdFolder, "Accountings.dtd");
    }

    public File getXmlFolder() {
        return xmlFolder;
    }

    public File getHtmlFolder() {
        return htmlFolder;
    }

    public File getXslFolder() {
        return xslFolder;
    }

    public File getDtdFolder() {
        return dtdFolder;
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
		Accounting accounting = new Accounting(name);
		addAccounting(accounting);
        return accounting;
	}
}
