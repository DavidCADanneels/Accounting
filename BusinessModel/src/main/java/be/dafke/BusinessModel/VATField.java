package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Created by ddanneels on 27/12/2016.
 */
public class VATField extends BusinessCollection<VATMovement> {
    public static final String AMOUNT = "amount";
    public static final String VATMOVEMENT = "VATMovement";
    private BigDecimal amount = BigDecimal.ZERO;

    public VATField(String name) {
        setName(name);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Properties getOutputProperties(){
        Properties properties = new Properties();
        properties.put(NAME,getName());
        properties.put(AMOUNT, amount);
        return properties;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public VATMovement addBusinessObject(VATMovement vatMovement){
        BigDecimal vatAmount = vatMovement.getAmount();
        if(vatAmount!=null) {
            if (vatMovement.isIncrease()) {
                amount = amount.add(vatAmount);
            } else {
                amount = amount.subtract(vatAmount);
            }
        }
        return vatMovement;
    }

    @Override
    public String getChildType() {
        return VATMOVEMENT;
    }

    @Override
    public VATMovement createNewChild(TreeMap<String, String> properties) {
        return null;
    }
}
