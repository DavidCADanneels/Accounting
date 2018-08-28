package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;
import static be.dafke.Utils.Utils.parseStringList;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class BalancesIO {



    public static void readBalances(Accounting accounting){
        Balances balances = accounting.getBalances();
        Accounts accounts = accounting.getAccounts();
        AccountTypes accountTypes = accounting.getAccountTypes();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/" +BALANCES + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, BALANCES);
        for (Element element: getChildren(rootElement, BALANCE)){

            String name = getValue(element, NAME);
            Balance balance = new Balance(name, accounts);

            balance.setLeftName(getValue(element, LEFTNAME));
            balance.setRightName(getValue(element, RIGHTNAME));
            balance.setLeftTotalName(getValue(element, LEFTTOTALNAME));
            balance.setRightTotalName(getValue(element, RIGHTTOTALNAME));
            balance.setLeftResultName(getValue(element, LEFTRESULTNAME));
            balance.setRightResultName(getValue(element, RIGHTRESULTNAME));

            String leftTypesString = getValue(element, LEFTTYPES);
            ArrayList<String> leftTypesList = parseStringList(leftTypesString);
            ArrayList<AccountType> leftTypes = new ArrayList<>();
            for(String s: leftTypesList){
                leftTypes.add(accountTypes.getBusinessObject(s));
            }
            balance.setLeftTypes(leftTypes);

            String rightTypesString = getValue(element, RIGHTTYPES);
            ArrayList<String> rightTypesList = parseStringList(rightTypesString);
            ArrayList<AccountType> rightTypes = new ArrayList<>();
            for(String s: rightTypesList){
                rightTypes.add(accountTypes.getBusinessObject(s));
            }
            balance.setRightTypes(rightTypes);

            try {
                balances.addBusinessObject(balance);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeBalancePdfFiles(Accounting accounting){
        BalancesIO.writeIndividualBalances(accounting);

        String accountingName = accounting.getName();
    File subFolder = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + PDF + "/" + BALANCES);
        subFolder.mkdirs();

        String resultXmlPath = ACCOUNTINGS_XML_FOLDER +accountingName + "/" + BALANCES + "/" + "ResultBalance" + XML_EXTENSION;
        String yearXmlPath = ACCOUNTINGS_XML_FOLDER +accountingName+ "/" + BALANCES + "/" + "YearBalance" + XML_EXTENSION;
        String relationsXmlPath = ACCOUNTINGS_XML_FOLDER +accountingName+ "/" + BALANCES + "/" + "RelationsBalance" + XML_EXTENSION;
        String xslPath = XSLFOLDER + "BalancePdf.xsl";
        String resultPdfPath = ACCOUNTINGS_XML_FOLDER +accountingName+"/PDF/Balances/ResultBalance.pdf";
        String yearPdfPath = ACCOUNTINGS_XML_FOLDER +accountingName+"/PDF/Balances/YearBalance.pdf";
        String relationsPdfPath = ACCOUNTINGS_XML_FOLDER +accountingName+"/PDF/Balances/RelationsBalance.pdf";
        try {
            PDFCreator.convertToPDF(resultXmlPath, xslPath, resultPdfPath);
            PDFCreator.convertToPDF(yearXmlPath, xslPath, yearPdfPath);
            PDFCreator.convertToPDF(relationsXmlPath, xslPath, relationsPdfPath);
        } catch (IOException | FOPException | TransformerException ex) {
            ex.printStackTrace();
        }
    }

    public static void writeBalances(Accounting accounting){
        Balances balances = accounting.getBalances();
        File balancesFile = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + BALANCES+ XML_EXTENSION);
        try{
            Writer writer = new FileWriter(balancesFile);
            writer.write(getXmlHeader(BALANCES, 2));
            for(Balance balance: balances.getBusinessObjects()) {
                ArrayList<String> leftTypesString = new ArrayList<>();
                for(AccountType type:balance.getLeftTypes()){
                    leftTypesString.add(type.getName());
                }
                ArrayList<String> righttTypesString = new ArrayList<>();
                for(AccountType type:balance.getRightTypes()){
                    righttTypesString.add(type.getName());
                }
                writer.write(
                        "  <"+BALANCE+">\n" +
                        "    <"+NAME+">" + balance.getName() + "</"+NAME+">\n" +
                        "    <"+LEFTNAME+">" + balance.getLeftName() + "</"+LEFTNAME+">\n" +
                        "    <"+RIGHTNAME+">" + balance.getRightName() + "</"+RIGHTNAME+">\n" +
                        "    <"+LEFTTOTALNAME+">" + balance.getLeftTotalName() + "</"+LEFTTOTALNAME+">\n" +
                        "    <"+RIGHTTOTALNAME+">" + balance.getRightTotalName() + "</"+RIGHTTOTALNAME+">\n" +
                        "    <"+LEFTRESULTNAME+">" + balance.getLeftResultName() + "</"+LEFTRESULTNAME+">\n" +
                        "    <"+RIGHTRESULTNAME+">" + balance.getRightResultName() + "</"+RIGHTRESULTNAME+">\n" +
                        "    <"+LEFTTYPES+">" + Utils.toString(leftTypesString) + "</"+LEFTTYPES+">\n" +
                        "    <"+RIGHTTYPES+">" + Utils.toString(righttTypesString) + "</"+RIGHTTYPES+">\n" +
                        "  </"+BALANCE+">\n"
                );
            }
            writer.write("</"+BALANCES+">");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Balances.class.getName()).log(Level.SEVERE, null, ex);
        }
//        writeIndividualBalances(accounting);
    }

    public static void writeIndividualBalances(Accounting accounting){
        Balances balances = accounting.getBalances();
        File balancesFolder = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + BALANCES);
        balancesFolder.mkdirs();
        for(Balance balance:balances.getBusinessObjects()){
            writeBalance(balance, balancesFolder);
        }
    }

    public static void writeBalance(Balance balance, File balancesFolder) {
        File file = new File(balancesFolder, balance.getName()+ XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(BALANCE, 3));
            writer.write(
                    "    <"+NAME+">" + balance.getName() + "</"+NAME+">\n" +
                        "    <"+LEFTNAME+">" + balance.getLeftName() + "</"+LEFTNAME+">\n" +
                        "    <"+RIGHTNAME+">" + balance.getRightName() + "</"+RIGHTNAME+">\n" +
                        "    <"+LEFTTOTALNAME+">" + balance.getLeftTotalName() + "</"+LEFTTOTALNAME+">\n" +
                        "    <"+RIGHTTOTALNAME+">" + balance.getRightTotalName() + "</"+RIGHTTOTALNAME+">\n" +
                        "    <"+LEFTRESULTNAME+">" + balance.getLeftResultName() + "</"+LEFTRESULTNAME+">\n" +
                        "    <"+RIGHTRESULTNAME+">" + balance.getRightResultName() + "</"+RIGHTRESULTNAME+">\n"
            );
            for (BalanceLine balanceLine : balance.getBusinessObjects()) {
                Account leftAccount = balanceLine.getLeftAccount();
                Account rightAccount = balanceLine.getRightAccount();
                writer.write("  <"+BALANCE_LINE+">\n");
                if(leftAccount!=null){
                    writer.write("    <"+NAME1+">"+leftAccount.getName()+"</"+NAME1+">\n");
                    writer.write("    <"+AMOUNT1+">"+leftAccount.getSaldo()+"</"+AMOUNT1+">\n");
                }
                if(rightAccount!=null){
                    writer.write("    <"+NAME2+">"+rightAccount.getName()+"</"+NAME2+">\n");
                    writer.write("    <"+AMOUNT2+">"+rightAccount.getSaldo().negate()+"</"+AMOUNT2+">\n");
                }
                writer.write("  </"+BALANCE_LINE+">\n");
            }
            writer.write("    <"+TOTALLEFT+">"+balance.getTotalLeft()+"</"+TOTALLEFT+">\n");
            writer.write("    <"+TOTALRIGHT+">"+balance.getTotalRight()+"</"+TOTALRIGHT+">\n");
            writer.write("  </"+BALANCE+">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Balance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
