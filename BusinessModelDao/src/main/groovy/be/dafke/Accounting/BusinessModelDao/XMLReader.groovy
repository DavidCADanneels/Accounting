package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import static AccountsIO.readAccounts
import static AllergenesIO.readAllergenes
import static BalancesIO.readBalances
import static ContactsIO.readContacts
import static IngredientOrdersIO.readIngredientOrders
import static IngredientsIO.readIngredients
import static MealOrderIO.readMealOrders
import static MealsIO.readMeals
import static MortgageIO.readMortgages
import static ProjectsIO.readProjects
import static PromoOrderIO.readPromoOrders
import static PurchaseOrderIO.readPurchaseOrders
import static SalesOrderIO.readSalesOrders
import static StockIO.readStockSettings
import static StockIO.readStockTransactions
import static StockOrderIO.readStockOrders
import static VATIO.readVATTransactions
import static be.dafke.Accounting.BusinessModelDao.GoodsIO.readGoods
import static be.dafke.Accounting.BusinessModelDao.JournalsIO.*
import static be.dafke.Accounting.BusinessModelDao.ServicesIO.readServices
import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*

class XMLReader {

    static final String VAT_ACCOUNTING = "VatAccounting"
    static final String CONTACTS_ACCOUNTING = "ContactsAccounting"
    static final String MEAL_ORDER_ACCOUNTING = "MealOrderAccounting"
    static final String PROJECTS_ACCOUNTING = "ProjectsAccounting"
    static final String TRADE_ACCOUNTING = "TradeAccounting"
    static final String MORTGAGES_ACCOUNTING = "MortgagesAccounting"

    static Document getDocument(File xmlFile){
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance()
            //            documentBuilderFactory.setValidating(true)
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder()
            Document doc = dBuilder.parse(xmlFile.getAbsolutePath())
            doc.getDocumentElement().normalize()

            doc
        } catch (Exception e) {
            e.printStackTrace()
            null
        }
    }

    static Element getRootElement(File xmlFile, String collectionName){
        Document doc = getDocument(xmlFile)
        (Element) doc.getElementsByTagName(collectionName).item(0)
    }

    static ArrayList<Element> getChildren(Element rootElement, String elementName){
        ArrayList<Element> children = new ArrayList<>()
        NodeList childrenNodeList = rootElement.getElementsByTagName(elementName)
        for (int i = 0; i < childrenNodeList.getLength(); i++) {
            children.add((Element) childrenNodeList.item(i))
        }
        children
    }

    static void readAccountings(Accountings accountings){
        File xmlFile = new File(ACCOUNTINGS_XML_FILE)
        if(!xmlFile.exists()){
            return
        }

        Element rootElement = getRootElement(xmlFile, ACCOUNTINGS)
        for (Element element : getChildren(rootElement, ACCOUNTING)) {

            String name = getValue(element, NAME)
            Accounting accounting = new Accounting(name)

            accounting.accountTypes.addDefaultTypes()
//            accounting.journalTypes.addDefaultType(accounting.accountTypes)

            String vatAccountingString = getValue(element, VAT_ACCOUNTING)
            boolean vatAccounting = Boolean.valueOf(vatAccountingString)
            accounting.vatAccounting = vatAccounting

            String projectsAccountingString = getValue(element, PROJECTS_ACCOUNTING)
            boolean projectsAccounting = Boolean.valueOf(projectsAccountingString)
            accounting.projectsAccounting = projectsAccounting

            String contactsAccountingString = getValue(element, CONTACTS_ACCOUNTING)
            boolean contactsAccounting = Boolean.valueOf(contactsAccountingString)
            accounting.contactsAccounting = contactsAccounting

            String tradeAccountingString = getValue(element, TRADE_ACCOUNTING)
            boolean tradeAccounting = Boolean.valueOf(tradeAccountingString)
            accounting.tradeAccounting = tradeAccounting

            String mealsAccountingString = getValue(element, MEAL_ORDER_ACCOUNTING)
            boolean mealsAccounting = Boolean.valueOf(mealsAccountingString)
            accounting.mealsAccounting = mealsAccounting

            String mortgagesAccountingString = getValue(element, MORTGAGES_ACCOUNTING)
            boolean mortgagesAccounting = Boolean.valueOf(mortgagesAccountingString)
            accounting.mortgagesAccounting = mortgagesAccounting

            try {
                accountings.addBusinessObject(accounting)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static void readSession() {
        Accountings accountings = Session.accountings
        String homeDir = System.getProperty("user.home")
        File homeFolder = new File(homeDir)
        File subFolder = new File(homeFolder, ".Accounting")
        File xmlFile = new File(subFolder, "Session.xml")
        if (!xmlFile.exists()) {
            return
        }
        Element rootElement = getRootElement(xmlFile, SESSION)
        String activeAccounting = getValue(rootElement, ACTIVE_ACCOUNTING)
        if (activeAccounting) Session.activeAccounting = accountings.getBusinessObject(activeAccounting)

        for (Element accountingElement : getChildren(rootElement, ACCOUNTING)) {
            String accountingName = getValue(accountingElement, NAME)
            String activeJournalName = getValue(accountingElement, ACTIVE_JOURNAL)
            Accounting accounting = accountings.getBusinessObject(accountingName)
            Journals journals = accounting.journals
            Journal activeJournal = activeJournalName==null?null:journals?.getBusinessObject(activeJournalName)
//            accounting.setActiveJournal(activeJournal)
            boolean showNumbers = getBooleanValue(accountingElement, SHOW_NUMBERS)

            AccountingSession accountingSession = new AccountingSession()
            accountingSession.activeJournal = activeJournal
            accountingSession.showNumbers = showNumbers
            Session.addAccountingSession(accounting,accountingSession)

            for (Element journalElement : getChildren(accountingElement, JOURNAL)) {


                String journalName = getValue(journalElement, NAME)
                Journal journal = journals.getBusinessObject(journalName)


                ArrayList<String> checkedTypesLeft = new ArrayList<>()
                ArrayList<String> checkedTypesRight = new ArrayList<>()
                String checkedLeftString = getValue(journalElement, CHECKED_LEFT)
                String checkedRightString = getValue(journalElement, CHECKED_RIGHT)
                if (checkedLeftString != null) {
                    String[] checkedList = checkedLeftString.split(",")
                    checkedTypesLeft.addAll(Arrays.asList(checkedList))
                }
                if (checkedRightString != null) {
                    String[] checkedList = checkedRightString.split(",")
                    checkedTypesRight.addAll(Arrays.asList(checkedList))
                }

                AccountTypes accountTypes = accounting.accountTypes

                JournalSession journalSession = new JournalSession()
                boolean showNumbersLeftJournal = getBooleanValue(journalElement, SHOW_NUMBERS_LEFT)
                journalSession.showNumbersLeft = showNumbersLeftJournal
                boolean showNumbersRightJournal = getBooleanValue(journalElement, SHOW_NUMBERS_RIGHT)
                journalSession.showNumbersRight = showNumbersRightJournal

                checkedTypesLeft.forEach({ typeName ->
                    AccountType type = accountTypes.getBusinessObject(typeName)
                    journalSession.setTypeCheckedLeft(type, true)
                })
                checkedTypesRight.forEach({ typeName ->
                    AccountType type = accountTypes.getBusinessObject(typeName)
                    journalSession.setTypeCheckedRight(type, true)
                })

                accountingSession.addJournalSession(journal,journalSession)

            }

        }
    }

    static void readAccountingSkeleton(Accounting accounting) {
        readAccounts(accounting)
        readJournalTypes(accounting)
        if (accounting.vatAccounting) {
            accounting.vatFields.addDefaultFields()
            //            readVATFields(accounting)
            readVATTransactions(accounting)
        }
        readJournals(accounting)
    }

    static void readAccountingDetails(Accounting accounting) {
        // FIXME: ID must be updated to max of new accounting: no static int any longer !
        if(accounting && !accounting.read) {
            readBalances(accounting)
            readTransactions(accounting)
            readJournalsContent(accounting.journals, accounting)
            if (accounting.mortgagesAccounting) {
                readMortgages accounting
            }
            if (accounting.projectsAccounting) {
                readProjects accounting
            }
            if (accounting.contactsAccounting) {
                readContacts accounting
            }
            if (accounting.tradeAccounting) {
                readStockSettings accounting
                readAllergenes accounting
                readIngredients accounting
                readIngredientOrders accounting
//                readArticles accounting
                readServices accounting
                readGoods accounting
                readPurchaseOrders accounting
                readSalesOrders accounting
                readStockOrders accounting
                readPromoOrders accounting
                readStockTransactions accounting
            }
            if (accounting.mealsAccounting){
//                readIngredients(accounting)
                readMeals(accounting)
                readMealOrders(accounting)
            }
        }
        if(accounting){
            accounting.read = true
        }
    }

    static String getValue(Element element, String tagName){
        NodeList nodeList = element.getElementsByTagName(tagName)
        if(nodeList.getLength()==0){
//            System.err.println("The tag " + tagName + " is not present.")
            return null
            // the tag is not present
        } else {
            nodeList = nodeList.item(0).getChildNodes()
            if(nodeList.getLength()==0){
//                System.err.println("The tag " + tagName + " is empty.")
                return null
                // the tag is empty
            } else {
                if(nodeList.item(0).getNodeValue().equals("null")){
//                    System.err.println("The tag " + tagName + " equals \"null\"")
                    return null
                }
                nodeList.item(0).getNodeValue()
            }
        }
    }

    static boolean getBooleanValue(Element element, String tagName){
        NodeList nodeList = element.getElementsByTagName(tagName)
        if(nodeList.getLength()==0){
//            System.err.println("The tag " + tagName + " is not present.")
            false
            // the tag is not present
        } else {
            nodeList = nodeList.item(0).getChildNodes()
            if(nodeList.getLength()==0){
//                System.err.println("The tag " + tagName + " is empty.")
                false
                // the tag is empty
            } else {
                if(nodeList.item(0).getNodeValue().equals("false")){
//                    System.err.println("The tag " + tagName + " equals \"null\"")
                    false
                }
                String nodeValue = nodeList.item(0).getNodeValue()
                nodeValue && nodeValue.equals(("true"))
            }
        }
    }

}