package be.dafke.Accounting.Objects;

import java.io.File;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 3:58
 */
public interface WriteableCollection extends Writeable{

    public void setHtmlFolder(File parentFolder);

    public void setXmlFolder(File parentFolder);

    //    public File getXmlFolder();

    public File getHtmlFolder();

    public void createXmlFolder();

    public void createHtmlFolder();

}
