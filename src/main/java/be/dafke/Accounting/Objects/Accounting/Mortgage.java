package be.dafke.Accounting.Objects.Accounting;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

public class Mortgage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Vector<BigDecimal>> table;
	private int alreadyPayed = 0;
    private final String name;
	private Account capital, intrest;
	private final BigDecimal startCapital;
    private File xmlFile;
    private File xsl2XmlFile, xsl2HtmlFile;
    private File htmlFile;
    private File dtdFile;

    public Mortgage(String name, BigDecimal startCapital) {
		this.name = name;
		this.startCapital = startCapital;
	}

	@Override
	public String toString() {
		return name;
	}

	public BigDecimal getStartCapital() {
		return startCapital;
	}

	public void setCapitalAccount(Account capital) {
		this.capital = capital;
	}

	public Account getCapitalAccount() {
		return capital;
	}

	public void setIntrestAccount(Account intrest) {
		this.intrest = intrest;
	}

	public Account getIntrestAccount() {
		return intrest;
	}

	public ArrayList<Vector<BigDecimal>> getTable() {
		return table;
	}

	public void setTable(ArrayList<Vector<BigDecimal>> table) {
		this.table = table;
	}

	public void setPayed(int nr) {
		alreadyPayed = nr;
	}

	public int getNrPayed() {
		return alreadyPayed;
	}

	public void increasePayed() {
		alreadyPayed++;
	}

    public String getName() {
        return name;
    }

	public void pay(Transaction transaction) {
        // TODO: check if not everything is payed yet get(alreadyPayed) --> ArrayOutOfBoundsException
		BigDecimal intrestAmount = table.get(alreadyPayed).get(1);
		BigDecimal kapitalAmount = table.get(alreadyPayed).get(2);
        transaction.addBooking(intrest,intrestAmount,true,false);
        transaction.addBooking(capital, kapitalAmount,true,false);
		transaction.addMortgage(this);
		System.out.println("Restkapitaal: " + table.get(alreadyPayed).get(3));
	}

	public boolean isPayedOff() {
		return alreadyPayed == table.size();
	}

    public File getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
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

    public void setXsl2HtmlFile(File xsl2HtmlFile) {
        this.xsl2HtmlFile = xsl2HtmlFile;
    }

    public void setXsl2XmlFile(File xsl2XmlFile) {
        this.xsl2XmlFile = xsl2XmlFile;
    }

    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }

    public File getHtmlFile() {
        return htmlFile;
    }
    protected void setDefaultFiles(File subFolder, File xslFolder, File dtdFolder) {
        xmlFile = FileSystemView.getFileSystemView().getChild(subFolder, name + ".xml");
        dtdFile = FileSystemView.getFileSystemView().getChild(dtdFolder, "Mortgage.dtd");
        xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgage2xml.xsl");
        xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgage2html.xsl");
    }
}
