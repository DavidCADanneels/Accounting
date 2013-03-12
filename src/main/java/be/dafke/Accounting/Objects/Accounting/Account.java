package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.BusinessTypeCollection;
import be.dafke.Accounting.Objects.BusinessTyped;
import be.dafke.Accounting.Objects.WriteableBusinessObject;
import be.dafke.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeMap;

/**
  * Boekhoudkundige rekening
  * @author David Danneels
  * @since 01/10/2010
 */
public class Account extends WriteableBusinessObject implements BusinessTyped<AccountType> {
    private static final String TYPE = "type";
    private AccountType type;
    private BigDecimal debitTotal, creditTotal;
    private final MultiValueMap<Calendar,Movement> movements;
    private BusinessTypeCollection businessTypeCollection;

    public Account() {
        movements = new MultiValueMap<Calendar,Movement>();
        debitTotal = BigDecimal.ZERO;
        debitTotal = debitTotal.setScale(2);
        creditTotal = BigDecimal.ZERO;
        creditTotal = creditTotal.setScale(2);
    }

    @Override
    public void setBusinessTypeCollection(BusinessTypeCollection businessTypeCollection) {
        this.businessTypeCollection = businessTypeCollection;
    }

    @Override
    public BusinessTypeCollection getBusinessTypeCollection() {
        return businessTypeCollection;
    }

    @Override
    public void setType(AccountType type) {
        this.type = type;
    }

    @Override
    public AccountType getType() {
        return type;
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

    public ArrayList<Movement> getMovements() {
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
    public Set<String> getInitKeySet(){
        Set<String> keySet = super.getInitKeySet();
        keySet.add(TYPE);
        return keySet;
    }

    @Override
    public TreeMap<String,String> getUniqueProperties() {
        TreeMap<String,String> outputMap = super.getUniqueProperties();
        return outputMap;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String,String> outputMap = super.getInitProperties();
        outputMap.put(TYPE, getType().getName());
        return outputMap;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
        String typeName = properties.get(TYPE);
        if(typeName!=null){
            type = (AccountType) businessTypeCollection.getBusinessObject(typeName);
        }
    }
    @Override
    public TreeMap<String,String> getProperties() {
        TreeMap<String,String> outputMap = super.getProperties();
        return outputMap;
    }

    @Override
    public void setProperties(TreeMap<String, String> properties) {
        super.setProperties(properties);
    }
}