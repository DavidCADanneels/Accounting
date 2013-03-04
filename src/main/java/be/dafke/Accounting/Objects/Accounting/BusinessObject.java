package be.dafke.Accounting.Objects.Accounting;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * User: Dafke
 * Date: 4/03/13
 * Time: 13:19
 */
public class BusinessObject {
    private String name;
    private File dtdFile;
    private File xmlFile;
    private File htmlFile;
    private File xsl2XmlFile, xsl2HtmlFile;
    private final String type;
//    private boolean isSaved;

    protected BusinessObject(String name, String type){
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
//		setSaved(false);
    }

    public String getName() {
        return name;
    }

    public File getDtdFile() {
        return dtdFile;
    }

    public File getXmlFile(){
        return xmlFile;
    }

    public File getXsl2XmlFile(){
        return xsl2XmlFile;
    }

    public File getXsl2HtmlFile(){
        return xsl2HtmlFile;
    }

    public File getHtmlFile(){
        return htmlFile;
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public void setXsl2XmlFile(File xslFile) {
        this.xsl2XmlFile = xslFile;
    }

    public void setXsl2HtmlFile(File xslFile) {
        this.xsl2HtmlFile = xslFile;
    }

    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }

    protected void setDefaultFiles(File subFolder, File xslFolder, File dtdFolder) {
        xmlFile = FileSystemView.getFileSystemView().getChild(subFolder, name + ".xml");
        dtdFile = FileSystemView.getFileSystemView().getChild(dtdFolder, type + ".dtd");
        xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, type + "2xml.xsl");
        xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, type + "2html.xsl");
    }

//	/**
//	 * Deelt mee of de laatste wijzigingen aan de rekening reeds werden uitgeschreven naar een XML bestand
//	 * @return of de laatste wijzigingen aan de rekening reeds werden uitgeschreven naar een XML bestand
//	 */
//	public boolean isSaved() {
//		return save;
//	}
//
//	/**
//	 * Stelt in of de laatste wijzigingen aan de rekening reeds werden uitgeschreven naar een XML bestand
//	 * @param save of de laatste wijzigingen aan de rekening reeds werden uitgeschreven naar een XML bestand
//	 */
//	protected void setSaved(boolean save) {
//		accounting.setSaved(save);
//		this.save = save;
//	}
}
