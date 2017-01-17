package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Mortgage;
import be.dafke.BusinessModel.MortgageTransaction;
import be.dafke.BusinessModel.Mortgages;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.NAME;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;
import static be.dafke.Utils.Utils.parseBigDecimal;
import static be.dafke.Utils.Utils.parseInt;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class MortgageIO {
    public static final String MORTGAGES = "Mortgages";
    public static final String MORTGAGE = "Mortgage";
    public final static String TOTAL = "total";
    public final static String NRPAYED = "nrPayed";
    public final static String CAPITAL_ACCOUNT = "CapitalAccount";
    public final static String INTREST_ACCOUNT = "IntrestAccount";
    public static final String NR = "nr";
    public static final String MENSUALITY = "mensuality";
    public static final String INTREST = "intrest";
    public static final String CAPITAL = "capital";
    public static final String RESTCAPITAL = "restCapital";
    public static final String MORTGAGE_TRANSACTION = "MortgageTransaction";

    public static void readMortgages(Mortgages mortgages, Accounts accounts, File accountingFolder) {
        File xmlFile = new File(accountingFolder, MORTGAGES+".xml");
        File mortgagesFolder = new File(accountingFolder, MORTGAGES);
        Element rootElement = getRootElement(xmlFile, MORTGAGES);
        for (Element element : getChildren(rootElement, MORTGAGE)) {

            Mortgage mortgage = new Mortgage();
            mortgage.setName(getValue(element, NAME));
            String startCapitalString = getValue(element, TOTAL);
            String nrPayedString = getValue(element, NRPAYED);
            if(startCapitalString!=null){
                mortgage.setStartCapital(parseBigDecimal(startCapitalString));
            }
            if(nrPayedString!=null){
                mortgage.setAlreadyPayed(parseInt(nrPayedString));
            }
            String capitalAccount = getValue(element, CAPITAL_ACCOUNT);
            if(capitalAccount!=null){
                mortgage.setCapitalAccount(accounts.getBusinessObject(capitalAccount));
            }
            String intrestAccount = getValue(element, INTREST_ACCOUNT);
            if(intrestAccount!=null){
                mortgage.setIntrestAccount(accounts.getBusinessObject(intrestAccount));
            }

            try {
                mortgages.addBusinessObject(mortgage);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }

        for(Mortgage mortgage:mortgages.getBusinessObjects()){
            readMortgage(mortgage, mortgagesFolder);
        }
    }

    public static void readMortgage(Mortgage mortgage, File mortgagesFolder) {
        String name = mortgage.getName();
        File xmlFile = new File(mortgagesFolder, name+".xml");
        Element rootElement = getRootElement(xmlFile, MORTGAGE);
        for (Element element : getChildren(rootElement, MORTGAGE_TRANSACTION)) {
            MortgageTransaction mortgageTransaction = new MortgageTransaction();
            mortgageTransaction.setMortgage(mortgage);
            mortgageTransaction.setNr(parseInt(getValue(element, NR)));
            mortgageTransaction.setMensuality(parseBigDecimal(getValue(element, MENSUALITY)));
            mortgageTransaction.setCapital(parseBigDecimal(getValue(element, CAPITAL)));
            mortgageTransaction.setIntrest(parseBigDecimal(getValue(element, INTREST)));
            mortgageTransaction.setRestCapital(parseBigDecimal(getValue(element, RESTCAPITAL)));
        }
    }


    public static void writeMortgages(Mortgages mortgages, File accountingFolder){
        File mortgagesFile = new File(accountingFolder, MORTGAGES+".xml");
        File mortgagesFolder = new File(accountingFolder, MORTGAGES);
        try{
            Writer writer = new FileWriter(mortgagesFile);
            writer.write(getXmlHeader(MORTGAGES, 2));
            for(Mortgage mortgage: mortgages.getBusinessObjects()) {
                writer.write(
                        "<"+MORTGAGE+">\n" +
                    // TODO: decide whether to store it here or in Mortgage.xml
                        "  <"+NAME+">" + mortgage.getName() + "</"+NAME+">\n" +
                        "  <"+CAPITAL_ACCOUNT+">" + mortgage.getCapitalAccount() + "</"+CAPITAL_ACCOUNT+">\n" +
                        "  <"+INTREST_ACCOUNT+">" + mortgage.getIntrestAccount() + "</"+INTREST_ACCOUNT+">\n" +
                        "  <"+NRPAYED+">" + mortgage.getNrPayed() + "</"+NRPAYED+">\n" +
                        "  <"+TOTAL+">" + mortgage.getStartCapital() + "</"+TOTAL+">\n" +
                    // TODO: until here
                        "</"+MORTGAGE+">\n"
                );
            }
            writer.write("</Mortgages>");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Mortgages.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Mortgage mortgage:mortgages.getBusinessObjects()){
            writeMortgage(mortgage, mortgagesFolder);
        }
    }

    public static void writeMortgage(Mortgage mortgage, File mortgagesFolder){
        File mortgagesFile = new File(mortgagesFolder, mortgage.getName()+".xml");
        try{
            Writer writer = new FileWriter(mortgagesFile);
            writer.write(getXmlHeader(MORTGAGE, 3));
            // TODO: decide whether to store it here or in Mortgages.xml
            writer.write(
                    "  <"+NAME+">" + mortgage.getName() + "</"+NAME+">\n" +
                    "  <"+CAPITAL_ACCOUNT+">" + mortgage.getCapitalAccount() + "</"+CAPITAL_ACCOUNT+">\n" +
                    "  <"+INTREST_ACCOUNT+">" + mortgage.getIntrestAccount() + "</"+INTREST_ACCOUNT+">\n" +
                    "  <"+NRPAYED+">" + mortgage.getNrPayed() + "</"+NRPAYED+">\n" +
                    "  <"+TOTAL+">" + mortgage.getStartCapital() + "</"+TOTAL+">\n"
            );
            // TODO: until here
            for(MortgageTransaction mortgageTransaction:mortgage.getBusinessObjects()){
                writer.write(
                    "  <"+MORTGAGE_TRANSACTION+">\n" +
                    "    <"+RESTCAPITAL+">"+mortgageTransaction.getRestCapital()+"</"+RESTCAPITAL+">\n" +
                    "    <"+MENSUALITY+">"+mortgageTransaction.getMensuality()+"</"+MENSUALITY+">\n" +
                    "    <"+NR+">"+mortgageTransaction.getNr()+"</"+NR+">\n" +
                    "    <"+INTREST+">"+mortgageTransaction.getIntrest()+"</"+INTREST+">\n" +
                    "    <"+CAPITAL+">"+mortgageTransaction.getCapital()+"</"+CAPITAL+">\n" +
                    "  </"+MORTGAGE_TRANSACTION+">\n"
                );
            }
            writer.write("</"+MORTGAGE+">");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Mortgage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
