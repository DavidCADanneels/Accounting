package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.BusinessTypeCollectionDependent;
import be.dafke.ObjectModel.BusinessTyped;
import be.dafke.Utils.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

/**
  * Boekhoudkundige rekening
  * @author David Danneels
  * @since 01/10/2010
 */
public class Account extends BusinessCollection<Movement> implements BusinessTypeCollectionDependent<AccountType>, BusinessTyped<AccountType> {
    public static final String TYPE = "type";
    public static final String DEFAULTAMOUNT = "defaultAmount";
    public static final String MOVEMENT = "Movement";
    private AccountType type;
    private BigDecimal debitTotal, creditTotal;
    private final MultiValueMap<Calendar,Movement> movements;
    private BusinessTypeCollection businessTypeCollection;
    private BigDecimal defaultAmount = null;

    public Account() {
        movements = new MultiValueMap<Calendar,Movement>();
        debitTotal = BigDecimal.ZERO;
        debitTotal = debitTotal.setScale(2);
        creditTotal = BigDecimal.ZERO;
        creditTotal = creditTotal.setScale(2);
    }

    @Override
    public Movement createNewChild(){
        return null;
    }

    @Override
    public String toString(){
        return getName();
    }

    @Override
    public String getChildType(){
        return MOVEMENT;
    }

    public void setBusinessTypeCollection(BusinessTypeCollection businessTypeCollection) {
        this.businessTypeCollection = businessTypeCollection;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public AccountType getType() {
        return type;
    }

    public void setDefaultAmount(BigDecimal defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public BigDecimal getSaldo() {
        BigDecimal result = debitTotal.subtract(creditTotal);
        result = result.setScale(2);
        return result;
    }

    public BigDecimal getDebetTotal() {
        return debitTotal;
    }

    public BigDecimal getCreditTotal() {
        return creditTotal;
    }

    @Override
    public ArrayList<Movement> getBusinessObjects() {
        return movements.values();
    }

    @Override
    public boolean isDeletable(){
        return movements.isEmpty();
    }

    protected void book(Calendar date, Movement movement) {
        movements.addValue(date, movement);
		if (movement.isDebit()) {
            debitTotal = debitTotal.add(movement.getAmount());
            debitTotal = debitTotal.setScale(2);
		} else {
            creditTotal = creditTotal.add(movement.getAmount());
            creditTotal = creditTotal.setScale(2);
		}
	}

    protected void unbook(Calendar date, Movement movement) {
		if (movement.isDebit()) {
			debitTotal = debitTotal.subtract(movement.getAmount());
			debitTotal = debitTotal.setScale(2);
		} else {
			creditTotal = creditTotal.subtract(movement.getAmount());
			creditTotal = creditTotal.setScale(2);
		}
        movements.removeValue(date, movement);
    }

    @Override
    public Movement addBusinessObject(Movement movement){
        Calendar date = movement.getDate();
        book(date,movement);
        return movement;
    }

    @Override
    public void removeBusinessObject(Movement movement){
        Calendar date = movement.getDate();
        unbook(date,movement);
    }

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = super.getInitKeySet();
        keySet.add(TYPE);
        keySet.add(DEFAULTAMOUNT);
        return keySet;
    }


    @Override
    public Properties getInitProperties() {
        Properties outputMap = super.getInitProperties();
        // FIXME NullPointerException if type==null / Type must be defined
        outputMap.put(TYPE, getType().getName());
        if(defaultAmount!=null){
            outputMap.put(DEFAULTAMOUNT, defaultAmount.toString());
        }
        return outputMap;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
        String typeName = properties.get(TYPE);
        if(typeName!=null){
            type = (AccountType) businessTypeCollection.getBusinessObject(typeName);
        }
        String defaultAmountString = properties.get(DEFAULTAMOUNT);
        if(defaultAmountString!=null){
            try{
                defaultAmount = new BigDecimal(defaultAmountString);
            } catch (NumberFormatException nfe){
                defaultAmount = null;
            }
        }
    }
}