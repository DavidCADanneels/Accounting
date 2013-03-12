package be.dafke.Accounting.Objects;

import be.dafke.Utils;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 4/03/13
 * Time: 16:23
 */
public class WriteableBusinessCollection<V extends WriteableBusinessObject> extends BusinessCollection<V> implements WriteableCollection{
    private static final String XML = "xml";
    private static final String HTML = "html";
    private static final String XMLFOLDER = "xmlFolder";
    private static final String HTMLFOLDER = "htmlFolder";
    protected File htmlFolder;
    protected File xmlFolder;

    @Override
    public void setHtmlFolder(File parentFolder){
        setHtmlFile(new File(parentFolder, getBusinessObjectType() + ".html"));
        htmlFolder = new File(parentFolder, getBusinessObjectType());
        for(V writeableBusinessObject : getBusinessObjects()){
            writeableBusinessObject.setHtmlFile(new File(htmlFolder, writeableBusinessObject.getName() + ".html"));
        }
    }

    @Override
    public void setXmlFolder(File parentFolder) {
        setXmlFile(new File(parentFolder, getBusinessObjectType() + ".xml"));
        xmlFolder = new File(parentFolder, getBusinessObjectType());
        for(WriteableBusinessObject writeableBusinessObject : getBusinessObjects()){
            writeableBusinessObject.setXmlFile(new File(xmlFolder, writeableBusinessObject.getName() + ".xml"));
        }
    }

//    @Override
//    protected File getXmlFolder(){
//        return xmlFolder;
//    }
//
    @Override
    public File getHtmlFolder() {
        return htmlFolder;
    }

    @Override
    public void createXmlFolder(){
        if(xmlFolder.mkdirs()){
            System.out.println(xmlFolder + " has been created");
        }
    }

    @Override
    public void createHtmlFolder(){
        if(htmlFolder.mkdirs()){
            System.out.println(htmlFolder + " has been created");
        }
    }

    // -------------------------------------------------------------------------------------

    /**For internal use:
     * modify and merge
     *
     */
    @Override
    protected V addBusinessObject(V value, Map.Entry<String,String> mapEntry){
        super.addBusinessObject(value, mapEntry);
        value.setXmlFile(new File(xmlFolder, value.getName() + ".xml"));
        if(htmlFolder!=null){
            value.setHtmlFile(new File(htmlFolder, value.getName() + ".html"));
        }
        return value;
    }

    // -------------------------------------------------------------------------------------

    // Duplicates from WriteableBusinessObject

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

    public WriteableBusinessCollection(){
        File xslFolder = new File(System.getProperty("Accountings_xsl"));
        xsl2XmlFile = new File(xslFolder, businessObjectType + "2xml.xsl");
        xsl2HtmlFile = new File(xslFolder, businessObjectType + "2html.xsl");

        File dtdFolder = new File(System.getProperty("Accountings_dtd"));
        dtdFile = new File(dtdFolder, businessObjectType + ".dtd");
    }

    public String getXmlHeader() {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
                "<?xml-stylesheet type=\"text/xsl\" href=\"" + xsl2XmlFile + "\"?>\r\n" +
                "<!DOCTYPE " + businessObjectType + " SYSTEM \"" + dtdFile + "\">\r\n";

    }

    @Override
    public File getDtdFile() {
        return dtdFile;
    }

    public void xmlToHtml() {
        Utils.xmlToHtml(xmlFile, xsl2HtmlFile, htmlFile, null);
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

    // KeySets and Properties ==============================================

    @Override
    public Set<String> getInitKeySet() {
        Set<String> keySet = super.getInitKeySet();
        keySet.add(XML);
        keySet.add(HTML);
        keySet.add(XMLFOLDER);
        keySet.add(HTMLFOLDER);
        return keySet;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String, String> properties = super.getUniqueProperties();
        if(xmlFile!=null){
            properties.put(XML, xmlFile.getPath());
        }
        if(htmlFile!=null){
            properties.put(HTML, htmlFile.getPath());
        }
        if(xmlFolder!=null){
            properties.put(XMLFOLDER, xmlFolder.getPath());
        }
        if(htmlFolder!=null){
            properties.put(HTMLFOLDER, htmlFolder.getPath());
        }
        return properties;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
        String xmlPath = properties.get(XML);
        String htmlPath = properties.get(HTML);
        String xmlFolderPath = properties.get(XMLFOLDER);
        String htmlFolderPath = properties.get(HTMLFOLDER);
        if(xmlPath!=null){
            xmlFile = new File(xmlPath);
        }
        if(htmlPath!=null){
            htmlFile = new File(htmlPath);
        }
        if(xmlFolderPath!=null){
            xmlFolder = new File(xmlFolderPath);
        }
        if(htmlFolderPath!=null){
            htmlFolder = new File(htmlFolderPath);
        }
    }

    @Override
    public TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> properties = super.getUniqueProperties();
//        if(xmlFile!=null){
//            properties.put(XML, xmlFile.getPath());
//        }
//        if(htmlFile!=null){
//            properties.put(HTML, htmlFile.getPath());
//        }
//        if(xmlFolder!=null){
//            properties.put(XMLFOLDER, xmlFolder.getPath());
//        }
//        if(htmlFolder!=null){
//            properties.put(HTMLFOLDER, htmlFolder.getPath());
//        }
        return properties;
    }

    @Override
    public Set<String> getCollectionKeySet(){
        Set<String> collectionKeySet = super.getCollectionKeySet();
        return collectionKeySet;
    }

    @Override
    public TreeMap<String,String> getProperties() {
        TreeMap<String, String> outputMap = super.getProperties();
        return outputMap;
    }

    @Override
    public void setProperties(TreeMap<String, String> properties){
        super.setProperties(properties);
    }
}
