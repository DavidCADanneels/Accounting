package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Mortgage;
import be.dafke.BusinessModel.MortgageTransaction;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
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
    public static void readCollection(Mortgage mortgage, File xmlFile){
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
    public static void writeMortgage(Mortgage mortgage, File mortgagesFolder, String header) {
        System.out.println("Mortgages.TOXML(" + mortgage.getName() + ")");
        try {
            File xmlFile = new File(mortgagesFolder, mortgage.getName()+".xml");
            Writer writer = new FileWriter(xmlFile);

            writer.write(header);

            writer.write("  <name>" + mortgage.getName() + "</name>\r\n");
            int teller = 1;
            ArrayList<MortgageTransaction> mortgageTransactions = mortgage.getBusinessObjects();
            if(mortgageTransactions!=null){
                for(MortgageTransaction vector : mortgageTransactions) {
                    writer.write("  <MortgageTransaction>\r\n"
                            + "    <nr>" + teller + "</nr>\r\n"
                            + "    <mensuality>" + vector.getMensuality() + "</mensuality>\r\n"
                            + "    <intrest>" + vector.getIntrest() + "</intrest>\r\n"
                            + "    <capital>" + vector.getCapital() + "</capital>\r\n"
                            + "    <restCapital>" + vector.getRestCapital() + "</restCapital>\r\n"
                            + "  </MortgageTransaction>\r\n");
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
