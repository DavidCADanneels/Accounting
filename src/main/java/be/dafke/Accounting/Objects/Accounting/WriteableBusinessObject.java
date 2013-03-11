package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.BusinessObject;
import be.dafke.Accounting.Objects.Writeable;
import be.dafke.Utils;

import java.io.File;

/**
 * User: Dafke
 * Date: 4/03/13
 * Time: 13:19
 */
public class WriteableBusinessObject extends BusinessObject implements Writeable {
    protected WriteableBusinessObject(){
        File xslFolder = new File(System.getProperty("Accountings_xsl"));
        xsl2XmlFile = new File(xslFolder, businessObjectType + "2xml.xsl");
        xsl2HtmlFile = new File(xslFolder, businessObjectType + "2html.xsl");

        File dtdFolder = new File(System.getProperty("Accountings_dtd"));
        dtdFile = new File(dtdFolder, businessObjectType + ".dtd");
    }

    // implements Writeable

    private File xmlFile, htmlFile;
    private File xsl2XmlFile, xsl2HtmlFile;
    private File dtdFile;
    private boolean saved;

    @Override
    public boolean isSaved() {
        return saved;
    }

    @Override
    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    @Override
    public String getXmlHeader() {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
                "<?xml-stylesheet type=\"text/xsl\" href=\"" + xsl2XmlFile + "\"?>\r\n" +
                "<!DOCTYPE " + businessObjectType + " SYSTEM \"" + dtdFile + "\">\r\n";

    }

    @Override
    public void xmlToHtml() {
        Utils.xmlToHtml(xmlFile, xsl2HtmlFile, htmlFile, null);
    }

    @Override
    public File getXmlFile(){
        return xmlFile;
    }

    @Override
    public File getXsl2XmlFile(){
        return xsl2XmlFile;
    }

    @Override
    public File getXsl2HtmlFile(){
        return xsl2HtmlFile;
    }

    @Override
    public File getHtmlFile(){
        return htmlFile;
    }

    @Override
    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    @Override
    public void setXsl2XmlFile(File xslFile) {
        this.xsl2XmlFile = xslFile;
    }

    @Override
    public void setXsl2HtmlFile(File xslFile) {
        this.xsl2HtmlFile = xslFile;
    }

    @Override
    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }
}
