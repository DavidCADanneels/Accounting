package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Dafke
 * Date: 4/03/13
 * Time: 13:19
 */
public class WriteableBusinessObject {
    private String name;
    private File xmlFile, htmlFile;
    private File xsl2XmlFile, xsl2HtmlFile;
    private File dtdFile;
    private String type;
//    private boolean isSaved;

    protected final static String NAME = "name";

    protected WriteableBusinessObject(){
        type = this.getClass().getSimpleName();

        File xslFolder = new File(System.getProperty("Accountings_xsl"));
        xsl2XmlFile = new File(xslFolder, type + "2xml.xsl");
        xsl2HtmlFile = new File(xslFolder, type + "2html.xsl");

        File dtdFolder = new File(System.getProperty("Accountings_dtd"));
        dtdFile = new File(dtdFolder, type + ".dtd");
    }

    public String getXmlHeader() {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
                "<?xml-stylesheet type=\"text/xsl\" href=\"" + xsl2XmlFile + "\"?>\r\n" +
                "<!DOCTYPE " + type + " SYSTEM \"" + dtdFile + "\">\r\n";

    }

    public void xmlToHtml() {
        Utils.xmlToHtml(xmlFile, xsl2HtmlFile, htmlFile, null);
    }

    public Map<String,String> getKeyMap(){
        Map<String,String> keyMap = new HashMap<String, String>();
        keyMap.put(NAME, name);
        return keyMap;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
//		setSaved(false);
    }

    public String getType() {
        return type;
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

    /**Checks if the WriteableBusinessObject is deletable:
     * @return if the WriteableBusinessObject is deletable (default: false)
     */
    public boolean isDeletable() {
        return false;
    }
    // TODO: make interfaces: Mergeable etc
    public boolean isMergeable(){
        return false;
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
