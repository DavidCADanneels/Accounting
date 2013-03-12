package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.WriteableBusinessCollection;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 12:07
 */
public class Balances extends WriteableBusinessCollection<Balance> {

    public static String RESULT_BALANCE = "ResultBalance";
    public static String RELATIONS_BALANCE = "RelationsBalance";
    public static String YEAR_BALANCE = "YearBalance";

    public void addDefaultBalances(Accounting accounting){
        ArrayList<AccountType> costs = new ArrayList<AccountType>();
        ArrayList<AccountType> revenues = new ArrayList<AccountType>();
        ArrayList<AccountType> credit = new ArrayList<AccountType>();
        ArrayList<AccountType> debit = new ArrayList<AccountType>();
        ArrayList<AccountType> active = new ArrayList<AccountType>();
        ArrayList<AccountType> passive = new ArrayList<AccountType>();
//        costs.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("KOST")));
//        revenues.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("OPBRENGST")));
//        credit.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("TEGOED_VAN_KLANT")));
//        debit.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("SCHULD_AAN_LEVERANCIER")));
//        active.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("ACTIEF")));
//        active.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("TEGOED_VAN_KLANT")));
//        passive.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("PASSIEF")));
//        passive.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("SCHULD_AAN_LEVERANCIER")));

        costs.add(accounting.getAccountTypes().getBusinessObject(("Cost")));
        revenues.add(accounting.getAccountTypes().getBusinessObject(("Revenue")));
        credit.add(accounting.getAccountTypes().getBusinessObject(("Credit")));
        debit.add(accounting.getAccountTypes().getBusinessObject(("Debit")));
        active.add(accounting.getAccountTypes().getBusinessObject(("Active")));
        active.add(accounting.getAccountTypes().getBusinessObject(("Credit")));
        passive.add(accounting.getAccountTypes().getBusinessObject(("Passive")));
        passive.add(accounting.getAccountTypes().getBusinessObject(("Debit")));

        Balance resultBalance = new Balance();
        resultBalance.setName(RESULT_BALANCE);
        resultBalance.setLeftName(getBundle("Accounting").getString("KOSTEN"));
        resultBalance.setRightName(getBundle("Accounting").getString("OPBRENGSTEN"));
        resultBalance.setLeftTotalName(getBundle("Accounting").getString("TOTAAL_KOSTEN"));
        resultBalance.setRightTotalName(getBundle("Accounting").getString("TOTAAL_OPBRENGSTEN"));
        resultBalance.setLeftResultName(getBundle("Accounting").getString("VERLIES"));
        resultBalance.setRightResultName(getBundle("Accounting").getString("WINST"));
        resultBalance.setLeftTypes(costs);
        resultBalance.setRightTypes(revenues);
        resultBalance.setAccounting(accounting);

        Balance relationsBalance = new Balance();
        relationsBalance.setName(RELATIONS_BALANCE);
        relationsBalance.setLeftName(getBundle("Accounting").getString("TEGOEDEN_VAN_KLANTEN"));
        relationsBalance.setRightName(getBundle("Accounting").getString("SCHULDEN_AAN_LEVERANCIERS"));
        relationsBalance.setLeftTotalName(getBundle("Accounting").getString("TOTAAL_TEGOEDEN"));
        relationsBalance.setRightTotalName(getBundle("Accounting").getString("TOTAAL_SCHULDEN"));
        relationsBalance.setLeftResultName(getBundle("Accounting").getString("RESTEREND_TEGOED"));
        relationsBalance.setRightResultName(getBundle("Accounting").getString("RESTERENDE_SCHULD"));
        relationsBalance.setLeftTypes(credit);
        relationsBalance.setRightTypes(debit);
        relationsBalance.setAccounting(accounting);
        
        Balance yearBalance = new Balance();
        yearBalance.setName(YEAR_BALANCE);
        yearBalance.setLeftName(getBundle("Accounting").getString("ACTIVA"));
        yearBalance.setRightName(getBundle("Accounting").getString("PASSIVA"));
        yearBalance.setLeftTotalName(getBundle("Accounting").getString("TOTAAL_ACTIVA_TEGOEDEN"));
        yearBalance.setRightTotalName(getBundle("Accounting").getString("TOTAAL_PASSIVA_SCHULDEN"));
        yearBalance.setLeftResultName(getBundle("Accounting").getString("WINST"));
        yearBalance.setRightResultName(getBundle("Accounting").getString("VERLIES"));
        yearBalance.setLeftTypes(active);
        yearBalance.setRightTypes(passive);
        yearBalance.setAccounting(accounting);

        try {
            addBusinessObject(resultBalance);
            addBusinessObject(relationsBalance);
            addBusinessObject(yearBalance);
        } catch (EmptyNameException e) {
            System.err.println("The Name of a Balance can not be empty.");
        } catch (DuplicateNameException e) {
            System.err.println("The Name of a Balance already exists.");
        }
    }

    // KeySets and Properties

    @Override
    public Set<String> getInitKeySet() {
        Set<String> keySet = super.getInitKeySet();
        return keySet;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String, String> properties = super.getUniqueProperties();
        return properties;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
    }

    @Override
    public TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> properties = super.getUniqueProperties();
        return properties;
    }

    @Override
    public Set<String> getCollectionKeySet(){
        Set<String> collectionKeySet = super.getCollectionKeySet();
        return collectionKeySet;
    }

    @Override
    public TreeMap<String,String> getProperties() {
        TreeMap<String, String> outputMap = super.getProperties();
        return outputMap;
    }

    @Override
    public void setProperties(TreeMap<String, String> properties){
        super.setProperties(properties);
    }
}
