package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils
import org.w3c.dom.Element

import java.util.logging.Level
import java.util.logging.Logger

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*
import static be.dafke.Accounting.BusinessModelDao.XMLReader.*
import static be.dafke.Accounting.BusinessModelDao.XMLWriter.getXmlHeader 

class MealOrderIO {
    static void readMealOrders(Accounting accounting){
        Accounts accounts = accounting.accounts
        Journals journals = accounting.journals

        MealOrders mealOrders = accounting.mealOrders
        Meals meals = accounting.meals

        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$MEAL_ORDERS$XML_EXTENSION")
        if(xmlFile.exists()) {
            Element rootElement = getRootElement(xmlFile, MEAL_ORDERS)
            String mealOrderServiceJournalString = getValue(rootElement, MEAL_ORDER_SERVICE_JOURNAL)
            String mealOrderSalesJournalString = getValue(rootElement, MEAL_ORDER_SALES_JOURNAL)
            String mealOrderBalanceAccountString = getValue(rootElement, MEAL_ORDER_BALANCE_ACCOUNT)
            String mealOrderServiceAccountString = getValue(rootElement, MEAL_ORDER_SERVICE_ACCOUNT)
            String mealOrderRevenueAccountString = getValue(rootElement, MEAL_ORDER_REVENUE_ACCOUNT)

            if (mealOrderServiceJournalString != null) {
                mealOrders.setMealOrderServiceJournal(journals.getBusinessObject(mealOrderServiceJournalString))
            }
            if (mealOrderSalesJournalString != null) {
                mealOrders.setMealOrderSalesJournal(journals.getBusinessObject(mealOrderSalesJournalString))
            }
            if (mealOrderBalanceAccountString != null) {
                mealOrders.setMealOrderBalanceAccount(accounts.getBusinessObject(mealOrderBalanceAccountString))
            }
            if (mealOrderServiceAccountString != null) {
                mealOrders.setMealOrderServiceAccount(accounts.getBusinessObject(mealOrderServiceAccountString))
            }
            if (mealOrderRevenueAccountString != null) {
                mealOrders.setMealOrderRevenueAccount(accounts.getBusinessObject(mealOrderRevenueAccountString))
            }

            for (Element mealOrderElement : getChildren(rootElement, MEAL_ORDER)) {
//                String name = getValue(mealOrderElement, NAME)
//                MealOrder mealOrder = new MealOrder(name)
                MealOrder mealOrder = new MealOrder()

                String idString = getValue(mealOrderElement, ID)
                Integer orderId = Utils.parseInt(idString)
                mealOrder.setId(orderId)

                mealOrder.setDescription(getValue(mealOrderElement, DESCRIPTION))
                mealOrder.setDate(Utils.toCalendar(getValue(mealOrderElement, DATE)))

                for (Element element : getChildren(mealOrderElement, MEAL)) {
                    String mealNr = getValue(element, MEAL_NR)
                    Meal meal = meals.getBusinessObject(mealNr)

                    String nrSting = getValue(element, NR_OF_ITEMS)
                    Integer nr = Utils.parseInt(nrSting)

                    MealOrderItem mealOrderItem = new MealOrderItem(nr, meal)

                    mealOrder.addBusinessObject(mealOrderItem)
                }
                try {
                    mealOrders.addBusinessObject(mealOrder)
                } catch (EmptyNameException | DuplicateNameException e) {
                    e.printStackTrace()
                }
            }
        }
    }

    static void writeMealOrders(Accounting accounting) {
        MealOrders mealOrders = accounting.mealOrders
        File file = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$MEAL_ORDERS$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(MEAL_ORDERS, 2)
            Journal mealOrderSalesJournal = mealOrders.getMealOrderSalesJournal()
            Journal mealOrderServiceJournal = mealOrders.getMealOrderServiceJournal()
            writer.write """\
  <$MEAL_ORDER_SALES_JOURNAL>${(mealOrderSalesJournal==null?"null":mealOrderSalesJournal.name)}</$MEAL_ORDER_SALES_JOURNAL>
  <$MEAL_ORDER_SERVICE_JOURNAL>${(mealOrderServiceJournal==null?"null":mealOrderServiceJournal.name)}</$MEAL_ORDER_SERVICE_JOURNAL>
  <$MEAL_ORDER_SERVICE_ACCOUNT>$mealOrders.mealOrderServiceAccount</$MEAL_ORDER_SERVICE_ACCOUNT>
  <$MEAL_ORDER_REVENUE_ACCOUNT>$mealOrders.mealOrderRevenueAccount</$MEAL_ORDER_REVENUE_ACCOUNT>
  <$MEAL_ORDER_BALANCE_ACCOUNT>$mealOrders.mealOrderBalanceAccount</$MEAL_ORDER_BALANCE_ACCOUNT>"""
            for (MealOrder order : mealOrders.businessObjects) {
                writer.write """
  <$MEAL_ORDER>
    <$DATE>${Utils.toString(order.date)}</$DATE>
    <$DESCRIPTION>$order.description</$DESCRIPTION>
    <$NAME>$order.name</$NAME>
    <$ID>$order.id</$ID>
    <$TOTAL_PRICE>$order.totalPrice</$TOTAL_PRICE>"""
                for (MealOrderItem orderItem : order.businessObjects) {
                    Meal meal = orderItem.getMeal()

                    writer.write """
    <$MEAL>
      <$NR_OF_ITEMS>$orderItem.numberOfItems</$NR_OF_ITEMS>
      <$MEAL_NR>$meal.name</$MEAL_NR>
      <$NAME>$meal.mealName</$NAME>
    </$MEAL>"""
                }
                writer.write"""
  </$MEAL_ORDER>"""
            }
            writer.write """
</$MEAL_ORDERS>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(MealOrder.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
