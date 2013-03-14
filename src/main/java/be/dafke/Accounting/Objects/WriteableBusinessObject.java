package be.dafke.Accounting.Objects;

import be.dafke.Utils;

import java.io.File;
import java.util.Set;
import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 4/03/13
 * Time: 13:19
 */
public class WriteableBusinessObject extends BusinessObject implements Writeable {
    protected static final String XML = "xml";
    protected static final String HTML = "html";

    protected WriteableBusinessObject(){
        File xslFolder = new File(System.getProperty("Accountings_xsl"));
        xsl2XmlFile = new File(xslFolder, businessObjectType + "2xml.xsl");
        xsl2HtmlFile = new File(xslFolder, businessObjectType + "2html.xsl");

        File dtdFolder = new File(System.getProperty("Accountings_dtd"));
        dtdFile = new File(dtdFolder, businessObjectType + ".dtd");
    }

    // implements Writeable

    protected File xmlFile, htmlFile;
    protected File xsl2XmlFile, xsl2HtmlFile;
    protected File dtdFile;
    protected boolean saved;

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
    public void setXsl2HtmlFile(File xslFile) {
        this.xsl2HtmlFile = xslFile;
    }

    @Override
    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }

    @Override
    public Set<String> getInitKeySet() {
        Set<String> keySet = super.getInitKeySet();
        keySet.add(XML);
        keySet.add(HTML);
        return keySet;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String,String> properties = super.getInitProperties();
        if(xmlFile!=null){
            properties.put(XML, xmlFile.getPath());
        }
        if(htmlFile!=null){
            properties.put(HTML, htmlFile.getPath());
        }
        return properties;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
        String xmlPath = properties.get(XML);
        String htmlPath = properties.get(HTML);
        if(xmlPath!=null){
            xmlFile = new File(xmlPath);
        }
        if(htmlPath!=null){
            htmlFile = new File(htmlPath);
        }
    }
}
