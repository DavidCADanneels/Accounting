package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils
import org.apache.fop.apps.FOPException
import org.w3c.dom.Element

import javax.xml.transform.TransformerException
import java.util.logging.Level
import java.util.logging.Logger

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*
import static be.dafke.Accounting.BusinessModelDao.XMLReader.*
import static be.dafke.Accounting.BusinessModelDao.XMLWriter.getXmlHeader
import static be.dafke.Utils.Utils.parseStringList 

class BalancesIO {

    static void readBalances(Accounting accounting){
        Balances balances = accounting.balances
        Accounts accounts = accounting.accounts
        AccountTypes accountTypes = accounting.accountTypes
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.name+"/" +BALANCES + XML_EXTENSION)
        Element rootElement = getRootElement(xmlFile, BALANCES)
        for (Element element: getChildren(rootElement, BALANCE)){

            String name = getValue(element, NAME)
            Balance balance = new Balance(name, accounts)

            balance.setLeftName(getValue(element, LEFTNAME))
            balance.setRightName(getValue(element, RIGHTNAME))
            balance.setLeftTotalName(getValue(element, LEFTTOTALNAME))
            balance.setRightTotalName(getValue(element, RIGHTTOTALNAME))
            balance.setLeftResultName(getValue(element, LEFTRESULTNAME))
            balance.setRightResultName(getValue(element, RIGHTRESULTNAME))

            String leftTypesString = getValue(element, LEFTTYPES)
            ArrayList<String> leftTypesList = parseStringList(leftTypesString)
            ArrayList<AccountType> leftTypes = new ArrayList<>()
            for(String s: leftTypesList){
                leftTypes.add(accountTypes.getBusinessObject(s))
            }
            balance.setLeftTypes(leftTypes)

            String rightTypesString = getValue(element, RIGHTTYPES)
            ArrayList<String> rightTypesList = parseStringList(rightTypesString)
            ArrayList<AccountType> rightTypes = new ArrayList<>()
            for(String s: rightTypesList){
                rightTypes.add(accountTypes.getBusinessObject(s))
            }
            balance.setRightTypes(rightTypes)

            try {
                balances.addBusinessObject(balance)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static String createPdfFolder(Accounting accounting) {
        String accountingName = accounting.name
        String resultPdfPolderPath = ACCOUNTINGS_PDF_FOLDER + accountingName + "/" + BALANCES + "/"
        File targetFolder = new File(resultPdfPolderPath)
        targetFolder.mkdirs()
        resultPdfPolderPath
    }

    static void writeBalancePdfFiles(Accounting accounting){
        String inputXmlPath = writeIndividualBalances(accounting)
        String xslPath = XSLFOLDER + "BalancePdf.xsl"

        String resultPdfPolderPath = createPdfFolder(accounting)

        String resultXmlPath = inputXmlPath + "ResultBalance" + XML_EXTENSION
        String yearXmlPath = inputXmlPath + "YearBalance" + XML_EXTENSION
        String relationsXmlPath = inputXmlPath + "RelationsBalance" + XML_EXTENSION

        String resultPdfPath = resultPdfPolderPath + "ResultBalance.pdf"
        String yearPdfPath = resultPdfPolderPath + "YearBalance.pdf"
        String relationsPdfPath = resultPdfPolderPath + "RelationsBalance.pdf"
        try {
            PDFCreator.convertToPDF(resultXmlPath, xslPath, resultPdfPath)
            PDFCreator.convertToPDF(yearXmlPath, xslPath, yearPdfPath)
            PDFCreator.convertToPDF(relationsXmlPath, xslPath, relationsPdfPath)
        } catch (IOException | FOPException | TransformerException ex) {
            ex.printStackTrace()
        }
    }

    static void writeBalances(Accounting accounting){
        Balances balances = accounting.balances
        File balancesFile = new File(ACCOUNTINGS_XML_FOLDER + accounting.name + "/" + BALANCES+ XML_EXTENSION)
        try{
            Writer writer = new FileWriter(balancesFile)
            writer.write(getXmlHeader(BALANCES, 2))
            for(Balance balance: balances.businessObjects) {
                ArrayList<String> leftTypesString = new ArrayList<>()
                for(AccountType type:balance.getLeftTypes()){
                    leftTypesString.add(type.name)
                }
                ArrayList<String> righttTypesString = new ArrayList<>()
                for(AccountType type:balance.getRightTypes()){
                    righttTypesString.add(type.name)
                }
                writer.write(
                        "  <"+BALANCE+">\n" +
                                "    <"+NAME+">" + balance.name + "</"+NAME+">\n" +
                                "    <"+LEFTNAME+">" + balance.getLeftName() + "</"+LEFTNAME+">\n" +
                                "    <"+RIGHTNAME+">" + balance.getRightName() + "</"+RIGHTNAME+">\n" +
                                "    <"+LEFTTOTALNAME+">" + balance.getLeftTotalName() + "</"+LEFTTOTALNAME+">\n" +
                                "    <"+RIGHTTOTALNAME+">" + balance.getRightTotalName() + "</"+RIGHTTOTALNAME+">\n" +
                                "    <"+LEFTRESULTNAME+">" + balance.getLeftResultName() + "</"+LEFTRESULTNAME+">\n" +
                                "    <"+RIGHTRESULTNAME+">" + balance.getRightResultName() + "</"+RIGHTRESULTNAME+">\n" +
                                "    <"+LEFTTYPES+">" + Utils.toString(leftTypesString) + "</"+LEFTTYPES+">\n" +
                                "    <"+RIGHTTYPES+">" + Utils.toString(righttTypesString) + "</"+RIGHTTYPES+">\n" +
                                "  </"+BALANCE+">\n"
                )
            }
            writer.write("</"+BALANCES+">")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Balances.class.name).log(Level.SEVERE, null, ex)
        }
        writeIndividualBalances(accounting)
    }

    static String writeIndividualBalances(Accounting accounting){
        Balances balances = accounting.balances
        String path = ACCOUNTINGS_XML_FOLDER + accounting.name + "/" + BALANCES + "/"
        File balancesFolder = new File(path)
        balancesFolder.mkdirs()
        for(Balance balance:balances.businessObjects){
            writeBalance(balance, balancesFolder)
        }
        path
    }

    static void writeBalance(Balance balance, File balancesFolder) {
        File file = new File(balancesFolder, balance.name+ XML_EXTENSION)
        try {
            Writer writer = new FileWriter(file)
            writer.write(getXmlHeader(BALANCE, 3))
            writer.write(
                    "    <"+NAME+">" + balance.name + "</"+NAME+">\n" +
                            "    <"+LEFTNAME+">" + balance.getLeftName() + "</"+LEFTNAME+">\n" +
                            "    <"+RIGHTNAME+">" + balance.getRightName() + "</"+RIGHTNAME+">\n" +
                            "    <"+LEFTTOTALNAME+">" + balance.getLeftTotalName() + "</"+LEFTTOTALNAME+">\n" +
                            "    <"+RIGHTTOTALNAME+">" + balance.getRightTotalName() + "</"+RIGHTTOTALNAME+">\n" +
                            "    <"+LEFTRESULTNAME+">" + balance.getLeftResultName() + "</"+LEFTRESULTNAME+">\n" +
                            "    <"+RIGHTRESULTNAME+">" + balance.getRightResultName() + "</"+RIGHTRESULTNAME+">\n"
            )
            for (BalanceLine balanceLine : balance.businessObjects) {
                Account leftAccount = balanceLine.getLeftAccount()
                Account rightAccount = balanceLine.getRightAccount()
                writer.write("  <"+BALANCE_LINE+">\n")
                if(leftAccount!=null){
                    writer.write("    <"+NAME1+">"+leftAccount.name+"</"+NAME1+">\n")
                    writer.write("    <"+AMOUNT1+">"+leftAccount.saldo+"</"+AMOUNT1+">\n")
                }
                if(rightAccount!=null){
                    writer.write("    <"+NAME2+">"+rightAccount.name+"</"+NAME2+">\n")
                    writer.write("    <"+AMOUNT2+">"+rightAccount.saldo.negate()+"</"+AMOUNT2+">\n")
                }
                writer.write("  </"+BALANCE_LINE+">\n")
            }
            writer.write("    <"+TOTALLEFT+">"+balance.getTotalLeft()+"</"+TOTALLEFT+">\n")
            writer.write("    <"+TOTALRIGHT+">"+balance.getTotalRight()+"</"+TOTALRIGHT+">\n")
            writer.write("  </"+BALANCE+">\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Balance.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
