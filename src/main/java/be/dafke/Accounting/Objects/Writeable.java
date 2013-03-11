package be.dafke.Accounting.Objects;

import java.io.File;
import java.util.Set;
import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 1:48
 */
public interface Writeable {

    public Set<String> getKeySet();

    public String getXmlHeader();

    public TreeMap<String, String> getOutputMap();

    public void setProperties(TreeMap<String,String> inputMap);

    public File getDtdFile();

    public void xmlToHtml();

    public File getXmlFile();

    public File getXsl2XmlFile();

    public File getXsl2HtmlFile();

    public File getHtmlFile();

    public void setXmlFile(File xmlFile);

    public void setXsl2XmlFile(File xslFile);

    public void setXsl2HtmlFile(File xslFile);

    public void setHtmlFile(File htmlFile);

    public void setSaved(boolean saved);

    public boolean isSaved();

}
