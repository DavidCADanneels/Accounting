package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.MustBeRead;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static be.dafke.BusinessModel.VATField.AMOUNT;
import static be.dafke.BusinessModel.VATTransactions.VAT_FIELDS;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATFields extends BusinessCollection<VATField> implements MustBeRead {

    public static final String VATFIELD = "VATField";

    public VATFields() {
        setName(VAT_FIELDS);
    }

    public void addDefaultFields(){
        try {
            addBusinessObject(new VATField("1"));
            addBusinessObject(new VATField("2"));
            addBusinessObject(new VATField("3"));
            addBusinessObject(new VATField("81"));
            addBusinessObject(new VATField("82"));
            addBusinessObject(new VATField("83"));
            addBusinessObject(new VATField("54"));
            addBusinessObject(new VATField("59"));
            addBusinessObject(new VATField("49"));
            addBusinessObject(new VATField("64"));
            addBusinessObject(new VATField("85"));
            addBusinessObject(new VATField("63"));
        } catch (EmptyNameException e) {
            e.printStackTrace();
        } catch (DuplicateNameException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getChildType() {
        return VATFIELD;
    }

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<>();
        keySet.add(NAME);
        keySet.add(AMOUNT);
        return keySet;
    }

    @Override
    public VATField createNewChild(TreeMap<String, String> properties) {
        String name = properties.get(NAME);
        String amountString = properties.get(AMOUNT);
        VATField vatField = new VATField(name);
        BigDecimal amount = amountString==null?BigDecimal.ZERO:Utils.parseBigDecimal(amountString);
        // TODO: remove this setAmount when we can read old transactions from XML
        vatField.setAmount(amount);
        return vatField;
    }

    public BigDecimal getField(String nr){
        if("XX".equals(nr)){
            return getXX();
        } else if("YY".equals(nr)){
            return getYY();
        } else if("71".equals(nr)){
            BigDecimal XX = getXX();
            BigDecimal YY = getYY();
            if(XX.compareTo(YY)>0){
                return XX.subtract(YY);
            } else return BigDecimal.ZERO;
        } else if("72".equals(nr)){
            BigDecimal YY = getYY();
            BigDecimal XX = getXX();
            if(YY.compareTo(XX)>0){
                return YY.subtract(XX);
            } else return BigDecimal.ZERO;
        } else{
            VATField vatField = getBusinessObject(nr);
            BigDecimal bigDecimal = vatField.getAmount();
            return bigDecimal==null?BigDecimal.ZERO:bigDecimal;
        }
    }

    public BigDecimal getXX() {
        return getField("54").add(getField("63"));
    }

    public BigDecimal getYY() {
        return getField("59").add(getField("64"));
    }

}
