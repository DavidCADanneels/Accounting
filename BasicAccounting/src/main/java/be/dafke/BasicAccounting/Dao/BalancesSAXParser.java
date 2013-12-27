package be.dafke.BasicAccounting.Dao;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Balance;

import java.io.File;
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
    public static void writeBalance(Balance balance, File xmlFolder, String header){
        System.out.println("Balances.TOXML(" + balance.toString() + ")");
        try {
            File xmlFile = new File(xmlFolder, balance.getName()+".xml");
            Writer writer = new FileWriter(xmlFile);

            writer.write(header);

            writer.write("<Balance>\r\n");
            writer.write("  <name>" + balance.getName() + "</name>\r\n");
            writer.write("  <LeftName>" + balance.getLeftName() + "</LeftName>\r\n");
            writer.write("  <RightName>" + balance.getRightName() + "</RightName>\r\n");
            writer.write("  <LeftTotalName>" + balance.getLeftTotalName() + "</LeftTotalName>\r\n");
            writer.write("  <RightTotalName>" + balance.getRightTotalName() + "</RightTotalName>\r\n");
            writer.write("  <LeftResultName>" + balance.getLeftResultName() + "</LeftResultName>\r\n");
            writer.write("  <RightResultName>" + balance.getRightResultName() + "</RightResultName>\r\n");
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
                writer.write("  <line>\r\n");
                if (i < nrLeft) {
                    writer.write("    <name1>" + leftAccounts.get(i).toString() + "</name1>\r\n");
                    writer.write("    <amount1>" + leftAccounts.get(i).getSaldo() + "</amount1>\r\n");
//                } else {
//                    writer.write("    <name1></name1>\r\n");
//                    writer.write("    <amount1></amount1>\r\n");
                }
                if (i < nrRight) {
                    writer.write("    <amount2>" + BigDecimal.ZERO.subtract(rightAccounts.get(i).getSaldo())
                            + "</amount2>\r\n");
                    writer.write("    <name2>" + rightAccounts.get(i).toString() + "</name2>\r\n");
//                } else {
//                    writer.write("    <amount2></amount2>\r\n");
//                    writer.write("    <name2></name2>\r\n");
                }
                writer.write("  </line>\r\n");
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