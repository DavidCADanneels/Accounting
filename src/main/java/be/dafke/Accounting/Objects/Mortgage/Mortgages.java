package be.dafke.Accounting.Objects.Mortgage;

import be.dafke.Accounting.Objects.Accounting.Accounting;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 11:06
 */
public class Mortgages {
    private final HashMap<String, Mortgage> mortgageTables;
    private String folder;
    private File xmlFile;
    private File xsl2XmlFile;
    private File xsl2HtmlFile;
    private File dtdFile;
    private File htmlFile;
    private Accounting accounting;

    public Mortgages(Accounting accounting){
        this.accounting = accounting;
        mortgageTables = new HashMap<String, Mortgage>();
    }

    public void addMortgageTable(String mortgageName, Mortgage table) {
        mortgageTables.put(mortgageName, table);
        File xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), folder);
        File htmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getHtmlFolder(), folder);

        File xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, table.getName() + ".xml");
        File xslFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Mortgage.xsl");
        File htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, table.getName() + ".html");

        table.setXmlFile(xmlFile);
        table.setXslFile(xslFile);
        table.setHtmlFile(htmlFile);
    }

    public boolean containsMortgageName(String mortgageName) {
        return mortgageTables.containsKey(mortgageName);
    }

    public Mortgage getMortgage(String mortgageName) {
        return mortgageTables.get(mortgageName);
    }

    public ArrayList<Mortgage> getMortgages() {
        return new ArrayList<Mortgage>(mortgageTables.values());
    }

    public void removeMortgageTable(Mortgage selectedMortgage) {
        mortgageTables.remove(selectedMortgage.toString());
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
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

    public void setDtdFile(File dtdFile) {
        this.dtdFile = dtdFile;
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
}