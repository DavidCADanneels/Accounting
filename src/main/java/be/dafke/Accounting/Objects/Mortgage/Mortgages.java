package be.dafke.Accounting.Objects.Mortgage;

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
    private final HashMap<String, Mortgage> mortgages;
    private String folder;
    private File xmlFile;
    private File xsl2XmlFile;
    private File xsl2HtmlFile;
    private File dtdFile;
    private File htmlFile;

    public Mortgages(){
        mortgages = new HashMap<String, Mortgage>();
    }

    public void addMortgageTable(String mortgageName, Mortgage table) {
        mortgages.put(mortgageName, table);
    }

    public boolean containsMortgageName(String mortgageName) {
        return mortgages.containsKey(mortgageName);
    }

    public Mortgage getMortgage(String mortgageName) {
        return mortgages.get(mortgageName);
    }

    public ArrayList<Mortgage> getMortgages() {
        return new ArrayList<Mortgage>(mortgages.values());
    }

    public void removeMortgageTable(Mortgage selectedMortgage) {
        mortgages.remove(selectedMortgage.toString());
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

    public void setDefaultHtmlFolderAndFiles(File htmlFolder, String name, boolean overwrite){
        if(overwrite || htmlFile == null || htmlFile.getPath().equals("null")){
            htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, name);
        }
        File subFolder = FileSystemView.getFileSystemView().getChild(htmlFolder, folder);
        subFolder.mkdirs();
        for(Mortgage mortgage: getMortgages()){
            mortgage.setHtmlFile(FileSystemView.getFileSystemView().getChild(subFolder, mortgage.getName() + ".html"));
        }
    }

    public void setDefaultXmlFolderAndFiles(File xmlFolder, File xslFolder, String name, boolean overwrite) {
        if(overwrite || folder == null || folder.equals("null")){
            folder = name;
        }
        if(overwrite || xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, name + ".xml");
        }
        if(overwrite || xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgages2xml.xsl");
        }
        if(overwrite || xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgages2html.xsl");
        }
        if(overwrite || dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgages.dtd");
        }
        File subFolder = FileSystemView.getFileSystemView().getChild(xmlFolder, folder);
        subFolder.mkdirs();
        for(Mortgage table:getMortgages()){
            File xmlFile = FileSystemView.getFileSystemView().getChild(subFolder, table.getName() + ".xml");
            File xslFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgage.xsl");
            table.setXmlFile(xmlFile);
            table.setXslFile(xslFile);
        }
    }
}