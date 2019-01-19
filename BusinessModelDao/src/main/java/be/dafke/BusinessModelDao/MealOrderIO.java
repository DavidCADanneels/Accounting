package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;

public class MealOrderIO {
    public static void readMealOrders(Accounting accounting){
        Accounts accounts = accounting.getAccounts();
        Journals journals = accounting.getJournals();

        MealOrders mealOrders = accounting.getMealOrders();
        DeliverooMeals deliverooMeals = accounting.getDeliverooMeals();

        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+MEAL_ORDERS + XML_EXTENSION);
        if(xmlFile.exists()) {
            Element rootElement = getRootElement(xmlFile, MEAL_ORDERS);
            String deliverooServiceJournalString = getValue(rootElement, DELIVEROO_SERVICE_JOURNAL);
            String deliverooSalesJournalString = getValue(rootElement, DELIVEROO_SALES_JOURNAL);
            String deliverooBalanceAccountString = getValue(rootElement, DELIVEROO_BALANCE_ACCOUNT);
            String deliverooServiceAccountString = getValue(rootElement, DELIVEROO_SERVICE_ACCOUNT);
            String deliverooRevenueAccountString = getValue(rootElement, DELIVEROO_REVENUE_ACCOUNT);

            if (deliverooServiceJournalString != null) {
                mealOrders.setDeliverooServiceJournal(journals.getBusinessObject(deliverooServiceJournalString));
            }
            if (deliverooSalesJournalString != null) {
                mealOrders.setDeliverooSalesJournal(journals.getBusinessObject(deliverooSalesJournalString));
            }
            if (deliverooBalanceAccountString != null) {
                mealOrders.setDeliverooBalanceAccount(accounts.getBusinessObject(deliverooBalanceAccountString));
            }
            if (deliverooServiceAccountString != null) {
                mealOrders.setDeliverooServiceAccount(accounts.getBusinessObject(deliverooServiceAccountString));
            }
            if (deliverooRevenueAccountString != null) {
                mealOrders.setDeliverooRevenueAccount(accounts.getBusinessObject(deliverooRevenueAccountString));
            }

            for (Element mealOrderElement : getChildren(rootElement, MEAL_ORDER)) {
//                String name = getValue(mealOrderElement, NAME);
//                MealOrder mealOrder = new MealOrder(name);
                MealOrder mealOrder = new MealOrder();

                String idString = getValue(mealOrderElement, ID);
                Integer orderId = Utils.parseInt(idString);
                mealOrder.setId(orderId);

                mealOrder.setDescription(getValue(mealOrderElement, DESCRIPTION));
                mealOrder.setDate(Utils.toCalendar(getValue(mealOrderElement, DATE)));

                for (Element element : getChildren(mealOrderElement, MEAL)) {
                    String mealNr = getValue(element, MEAL_NR);
                    DeliverooMeal deliverooMeal = deliverooMeals.getBusinessObject(mealNr);

                    String nrSting = getValue(element, NR_OF_ITEMS);
                    Integer nr = Utils.parseInt(nrSting);

                    MealOrderItem mealOrderItem = new MealOrderItem(nr, deliverooMeal);

                    mealOrder.addBusinessObject(mealOrderItem);
                }
                try {
                    mealOrders.addBusinessObject(mealOrder);
                } catch (EmptyNameException | DuplicateNameException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeMealOrders(Accounting accounting) {
        MealOrders mealOrders = accounting.getMealOrders();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + MEAL_ORDERS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(MEAL_ORDERS, 2));
            Journal deliverooSalesJournal = mealOrders.getDeliverooSalesJournal();
            Journal deliverooServiceJournal = mealOrders.getDeliverooServiceJournal();
            writer.write(
                    "  <" + DELIVEROO_SALES_JOURNAL + ">" + (deliverooSalesJournal == null ? "null" : deliverooSalesJournal.getName()) + "</" + DELIVEROO_SALES_JOURNAL + ">\n" +
                            "  <" + DELIVEROO_SERVICE_JOURNAL + ">" + (deliverooServiceJournal == null ? "null" : deliverooServiceJournal.getName()) + "</" + DELIVEROO_SERVICE_JOURNAL + ">\n" +
                            "  <" + DELIVEROO_SERVICE_ACCOUNT + ">" + mealOrders.getDeliverooServiceAccount() + "</" + DELIVEROO_SERVICE_ACCOUNT + ">\n" +
                            "  <" + DELIVEROO_REVENUE_ACCOUNT + ">" + mealOrders.getDeliverooRevenueAccount() + "</" + DELIVEROO_REVENUE_ACCOUNT + ">\n" +
                            "  <" + DELIVEROO_BALANCE_ACCOUNT + ">" + mealOrders.getDeliverooBalanceAccount() + "</" + DELIVEROO_BALANCE_ACCOUNT + ">\n"
            );
            for (MealOrder order : mealOrders.getBusinessObjects()) {
                writer.write(
                             "  <" + MEAL_ORDER + ">\n" +
                                 "    <" + DATE + ">" + Utils.toString(order.getDate()) + "</" + DATE + ">\n" +
                                 "    <" + DESCRIPTION + ">" + order.getDescription() + "</" + DESCRIPTION + ">\n" +
                                 "    <" + NAME + ">" + order.getName() + "</" + NAME + ">\n" +
                                 "    <" + ID + ">" + order.getId() + "</" + ID + ">\n" +
                                 "    <" + TOTAL_PRICE + ">" + order.getTotalPrice() + "</" + TOTAL_PRICE + ">\n"
                );
                for (MealOrderItem orderItem : order.getBusinessObjects()) {
                    DeliverooMeal deliverooMeal = orderItem.getDeliverooMeal();

                    writer.write(
                            "    <" + MEAL + ">\n" +
                                "      <" + NR_OF_ITEMS + ">" + orderItem.getNumberOfItems() + "</" + NR_OF_ITEMS + ">\n" +
                                "      <" + MEAL_NR + ">" + deliverooMeal.getName() + "</" + MEAL_NR + ">\n" +
                                "      <" + NAME + ">" + deliverooMeal.getMealName() + "</" + NAME + ">\n" +
                                "    </" + MEAL + ">\n"
                    );
                }
                writer.write("  </" + MEAL_ORDER + ">\n");
            }
            writer.write("</" + MEAL_ORDERS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(MealOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
