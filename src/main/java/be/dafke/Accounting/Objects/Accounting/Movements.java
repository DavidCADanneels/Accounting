package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.GUI.CodaManagement.SearchOptions;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;

public class Movements {
	/**
	 * 
	 */
	private ArrayList<Movement> movements = new ArrayList<Movement>();
    private File xmlFile;
    private File xsl2XmlFile;
    private File xsl2HtmlFile;
    private File dtdFile;
    private File htmlFile;

    public void add(Movement value) {
		movements.add(value);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("CounterParties:");
		for (Movement movement : movements){
			builder.append("\r\n").append(movement.toString());
		}
		return builder.toString();
	}

	public Movement getMovement(int nr) {
		return movements.get(nr);
	}

	public ArrayList<Movement> getAllMovements() {
		return movements;
	}

	public ArrayList<Movement> getMovements(SearchOptions searchOptions) {
		ArrayList<Movement> result = new ArrayList<Movement>();
        CounterParty counterParty = searchOptions.getCounterParty();
        String transactionCode = searchOptions.getTransactionCode();
        String communication = searchOptions.getCommunication();
        boolean searchOnCounterParty = searchOptions.isSearchOnCounterParty();
        boolean searchOnTransactionCode = searchOptions.isSearchOnTransactionCode();
        boolean searchOnCommunication = searchOptions.isSearchOnCommunication();
		for(Movement movement : movements) {
			if ((!searchOnTransactionCode || transactionCode.equals(movement.getTransactionCode()))  &&
                    (!searchOnCommunication || communication.equals(movement.getCommunication())) &&
                    (!searchOnCounterParty || counterParty == movement.getCounterParty())) {
				result.add(movement);
			}
		}
		return result;
	}

	public int getSize() {
		return movements.size();
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
            htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, name + ".html");
        }
    }

    public void setDefaultXmlFolderAndFiles(Accounting accounting, String name, boolean overwrite) {
        File xmlFolder = accounting.getXmlFolder();
        File xslFolder = accounting.getXslFolder();
        File dtdFolder = accounting.getDtdFolder();
        if(overwrite || xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, name + ".xml");
        }
        if(overwrite || xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Movements2xml.xsl");
        }
        if(overwrite || xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Movements2html.xsl");
        }
        if(overwrite || dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(dtdFolder, "Movements.dtd");
        }
    }
}
