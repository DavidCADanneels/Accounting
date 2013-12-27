package be.dafke.Mortgage.Dao;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.Mortgage.Objects.Mortgage;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 5:07
 */
public class MortgagesSAXParser {
    // READ
    //
    public static void readMortgage(Mortgage mortgage, File xmlFile){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(new MortgageContentHandler(mortgage));
            reader.setErrorHandler(new FoutHandler());
            reader.parse(xmlFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // WRITE
    //
    public static void writeMortgage(Mortgage mortgage, File xmlFolder, String header) {
        System.out.println("Mortgages.TOXML(" + mortgage.toString() + ")");
        try {
            File xmlFile = new File(xmlFolder, mortgage.getName()+".xml");
            Writer writer = new FileWriter(xmlFile);

            writer.write(header);

            writer.write("<Mortgage>\r\n");
            writer.write("  <name>" + mortgage.toString() + "</name>\r\n");
            int teller = 1;
            ArrayList<Vector<BigDecimal>> table = mortgage.getTable();
            if(table!=null){
                for(Vector<BigDecimal> vector : table) {
                    writer.write("  <line>\r\n" + "    <nr>" + teller + "</nr>\r\n" + "    <mensuality>"
                            + vector.get(0) + "</mensuality>\r\n" + "    <intrest>" + vector.get(1) + "</intrest>\r\n"
                            + "    <capital>" + vector.get(2) + "</capital>\r\n" + "    <restCapital>" + vector.get(3)
                            + "</restCapital>\r\n  </line>\r\n");
                    teller++;
                }
            }
            writer.write("</Mortgage>");
            writer.flush();
            writer.close();
            // setSaved(true);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
