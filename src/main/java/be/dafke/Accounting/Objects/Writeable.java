package be.dafke.Accounting.Objects;

import java.io.File;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 1:48
 */
public interface Writeable {

    public String getXmlHeader();

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
