package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.Utils.MultiValueMap;

import java.math.BigDecimal;
import java.util.*;

import static be.dafke.BusinessModel.Movement.CREDIT;
import static be.dafke.BusinessModel.Movement.DEBIT;
import static be.dafke.BusinessModel.Movement.ID;

/**
  * Boekhoudkundige rekening
  * @author David Danneels
  * @since 01/10/2010
 */
public class Account extends BusinessCollection<Movement> {
    public static final String TYPE = "type";
    public static final String DEFAULTAMOUNT = "defaultAmount";
    public static final String MOVEMENT = "Movement";
    private AccountType type;
    private BigDecimal debitTotal, creditTotal;
    private final MultiValueMap<Calendar,Movement> movements;
    private BigDecimal defaultAmount = null;

    public Account(String name) {
        setName(name);
        movements = new MultiValueMap<>();
        debitTotal = BigDecimal.ZERO;
        debitTotal = debitTotal.setScale(2);
        creditTotal = BigDecimal.ZERO;
        creditTotal = creditTotal.setScale(2);
    }

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<>();
        keySet.add(NAME);
        keySet.add(ID);
        keySet.add(DEBIT);
        keySet.add(CREDIT);
        return keySet;
    }

    @Override
    public Movement createNewChild(TreeMap<String, String> properties){
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
    public Properties getOutputProperties() {
        Properties outputMap = new Properties();
        outputMap.put(NAME,getName());
        // FIXME NullPointerException if type==null / Type must be defined
        outputMap.put(TYPE, getType().getName());
        if(defaultAmount!=null){
            outputMap.put(DEFAULTAMOUNT, defaultAmount.toString());
        }
        return outputMap;
    }
}