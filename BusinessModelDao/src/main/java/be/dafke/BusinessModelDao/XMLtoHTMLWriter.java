package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.Accounting;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import org.apache.fop.cli.InputHandler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Vector;

public class XMLtoHTMLWriter {

    public static void xmlToHtml(File xmlFile, File xslFile, File htmlFile, Vector params) {
        InputHandler inputHandler = new InputHandler(xmlFile, xslFile, params);
        try {
            if (!htmlFile.exists()) {
//                htmlFile.getParentFile().mkdirs();
                if(htmlFile.createNewFile()){
                    System.out.println(htmlFile + " has been created");
                }
            }
            OutputStream out = new BufferedOutputStream(new FileOutputStream(htmlFile));
            inputHandler.transformTo(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
