package be.dafke.Accounting.Objects;

import be.dafke.Utils;

import java.io.File;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * User: Dafke
 * Date: 4/03/13
 * Time: 13:19
 */
public class WriteableBusinessObject extends BusinessObject implements Writeable {
    private static final String XML = "xml";
    private static final String HTML = "html";
    protected Set<String> keySet;

    protected WriteableBusinessObject(){
        keySet = new TreeSet<String>();
        keySet.add(NAME);
        keySet.add(XML);
        keySet.add(HTML);

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
    public Set<String> getKeySet() {
        return keySet;
    }

    @Override
    public String getXmlHeader() {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
                "<?xml-stylesheet type=\"text/xsl\" href=\"" + xsl2XmlFile + "\"?>\r\n" +
                "<!DOCTYPE " + businessObjectType + " SYSTEM \"" + dtdFile + "\">\r\n";

    }

    @Override
    public TreeMap<String,String> getOutputMap() {
        TreeMap<String,String> outputMap = new TreeMap<String, String>();
        outputMap.put(NAME, getName());
        outputMap.put(XML, getXmlFile().getPath());
        if(getHtmlFile()!=null){
            outputMap.put(HTML, getHtmlFile().getPath());
        }
        return outputMap;
    }

    @Override
    public void setProperties(TreeMap<String, String> inputMap) {
        setName(inputMap.get(NAME));
        String xmlPath = inputMap.get(XML);
        String htmlPath = inputMap.get(HTML);
        if(xmlPath!=null){
            xmlFile = new File(xmlPath);
        }
        if(htmlPath!=null){
            htmlFile = new File(htmlPath);
        }
    }


    @Override
    public File getDtdFile() {
        return dtdFile;
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
