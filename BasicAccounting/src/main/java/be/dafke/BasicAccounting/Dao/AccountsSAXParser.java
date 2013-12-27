package be.dafke.BasicAccounting.Dao;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Movement;
import be.dafke.Utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 4:59
 */
public class AccountsSAXParser {

    // WRITE
    //
    public static void writeAccount(Account account, File xmlFolder, String header){
        try {
            File xmlFile = new File(xmlFolder, account.getName()+".xml");
            Writer writer = new FileWriter(xmlFile);

            writer.write(header);

            writer.write("<Account>\r\n" + "  <name>" + account.getName() + "</name>\r\n");
            for(Movement movement : account.getMovements()){
                writer.write("  <action id=\""+movement.getBooking().getTransaction().getId()+"\">\r\n");
                writer.write("    <nr>" + movement.getBooking().getTransaction().getAbbreviation() + movement.getBooking().getTransaction().getId() + "</nr>\r\n");
//                writer.write("    <journal_xml>" + movement.getBooking().getTransaction().getJournal().getXmlFile() + "</journal_xml>\r\n");
//                writer.write("    <journal_html>" + movement.getBooking().getTransaction().getJournal().getHtmlFile() + "</journal_html>\r\n");
                writer.write("    <date>" + Utils.toString(movement.getBooking().getTransaction().getDate()) + "</date>\r\n");
                writer.write("    <" + (movement.isDebit() ? "debit" : "credit") + ">"
                                     + movement.getAmount().toString()
                               + "</" + (movement.isDebit() ? "debit" : "credit") + ">\r\n");
                writer.write("    <description>" + movement.getBooking().getTransaction().getDescription() + "</description>\r\n");
                writer.write("  </action>\r\n");
            }
            BigDecimal saldo = account.getSaldo();
            String resultType =(saldo.compareTo(BigDecimal.ZERO)<0)?"credit":"debit";
            writer.write("  <closed type = \"" + resultType + "\">\r\n" + "    <debitTotal>" + account.getDebetTotal() + "</debitTotal>\r\n"
                    + "    <creditTotal>" + account.getCreditTotal() + "</creditTotal>\r\n"
                    + "    <saldo>" + saldo.abs() + "</saldo>\r\n  </closed>\r\n");
            writer.write("</Account>");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}