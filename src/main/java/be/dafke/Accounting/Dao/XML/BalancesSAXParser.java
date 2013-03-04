package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Balance;
import be.dafke.Accounting.Objects.Accounting.Balances;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 5:05
 */
public class BalancesSAXParser {

    // WRITE
    public static void writeBalances(Balances balances) {
        try {
            Writer writer = new FileWriter(balances.getXmlFile());

            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n");
            writer.write("<!DOCTYPE Balances SYSTEM \"" + balances.getDtdFile().getCanonicalPath() + "\">\r\n");
            writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + balances.getXsl2XmlFile().getCanonicalPath() + "\"?>\r\n");

            writer.write("<Balances>\r\n");
            for(Balance balance:balances.getBalances()){
                writer.write("    <Balance>\r\n");
                writer.write("      <name>" + balance.getName() + "</name>\r\n");
                writer.write("      <xml>" + balance.getXmlFile() + "</xml>\r\n");
                writer.write("      <html>" + balance.getHtmlFile() + "</html>\r\n");
                writer.write("    </Balance>\r\n");
            }
            writer.write("</Balances>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Balance balance : balances.getBalances()){
            writeBalance(balance);
        }
    }
    //
    private static void writeBalance(Balance balance){
        System.out.println("Balances.TOXML(" + balance.toString() + ")");
        try {
            Writer writer = new FileWriter(balance.getXmlFile());

            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n");
            writer.write("<!DOCTYPE Balance SYSTEM \"" + balance.getDtdFile().getCanonicalPath() + "\">\r\n");
            writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + balance.getXsl2XmlFile().getCanonicalPath() + "\"?>\r\n");

            writer.write("<Balance>\r\n  <name>" + balance.getName() //$NON-NLS-1$ //$NON-NLS-2$
                    + "</name>\r\n  <left>" + balance.getLeftName() + "</left>\r\n  <right>" + balance.getRightName() + "</right>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            ArrayList<Account> leftAccounts = balance.getLeftAccounts();
            ArrayList<Account> rightAccounts = balance.getRightAccounts();

            int nrLeft = leftAccounts.size();
            int nrRight = rightAccounts.size();
            int max;
            if (nrLeft > nrRight) {
                max = nrLeft;
            } else {
                max = nrRight;
            }
            for(int i = 0; i < max; i++) {
                writer.write("  <line>\r\n"); //$NON-NLS-1$
                if (i < nrLeft) {
                    writer.write("    <name1>" + leftAccounts.get(i).toString() + "</name1>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
                    writer.write("    <amount1>" + leftAccounts.get(i).saldo() + "</amount1>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    writer.write("    <name1></name1>\r\n"); //$NON-NLS-1$
                    writer.write("    <amount1></amount1>\r\n"); //$NON-NLS-1$
                }
                if (i < nrRight) {
                    writer.write("    <amount2>" + BigDecimal.ZERO.subtract(rightAccounts.get(i).saldo()) //$NON-NLS-1$
                            + "</amount2>\r\n"); //$NON-NLS-1$
                    writer.write("    <name2>" + rightAccounts.get(i).toString() + "</name2>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    writer.write("    <amount2></amount2>\r\n"); //$NON-NLS-1$
                    writer.write("    <name2></name2>\r\n"); //$NON-NLS-1$
                }
                writer.write("  </line>\r\n"); //$NON-NLS-1$
            }
            writer.write("</Balance>"); //$NON-NLS-1$
            writer.flush();
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
