package be.dafke.FOP;

import org.apache.fop.cli.InputHandler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Vector;

/**
 * User: david
 * Date: 26-12-13
 * Time: 20:54
 */
public class Utils {
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
