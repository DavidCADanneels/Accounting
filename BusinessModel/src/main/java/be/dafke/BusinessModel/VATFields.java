package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.MustBeRead;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    public VATFields(VATFields vatFields) {
        setName(VATFIELD);
        try {
            for(VATField vatField : vatFields.getBusinessObjects()) {
                addBusinessObject(vatField);
            }
        } catch (EmptyNameException e) {
            e.printStackTrace();
        } catch (DuplicateNameException e) {
            e.printStackTrace();
        }
    }
    public VATFields() {
        setName(VAT_FIELDS);
    }

    public void addDefaultFields(){
        try {
            addBusinessObject(new VATField("0"));
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
            // Next 4 fields have calculated amounts
//            addBusinessObject(new VATField("XX"));
//            addBusinessObject(new VATField("YY"));
//            addBusinessObject(new VATField("71"));
//            addBusinessObject(new VATField("72"));
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

    public ArrayList<VATField> getAllFields(){
        ArrayList<VATField> vatFields = super.getBusinessObjects();
        vatFields.add(getXX());
        vatFields.add(getYY());
        VATField field71 = get71();
        VATField field72 = get72();
        if(field71.getAmount().compareTo(BigDecimal.ZERO) > 0){
            vatFields.add(field71);
        }
        // normally only one if will be used as 71 = -72
        if(field72.getAmount().compareTo(BigDecimal.ZERO) > 0){
            vatFields.add(field72);
        }
        return vatFields;
    }

    @Override
    public VATField getBusinessObject(String nr){
        if("XX".equals(nr)){
            return getXX();
        } else if("YY".equals(nr)){
            return getYY();
        } else if("71".equals(nr)){
            return get71();
        } else if("72".equals(nr)){
            return get72();
        } else{
            return super.getBusinessObject(nr);
//            if(vatField==null) return BigDecimal.ZERO;
//            BigDecimal bigDecimal = vatField.getAmount();
//            return bigDecimal==null?BigDecimal.ZERO:bigDecimal;
        }
    }

    public VATField get71() {
        VATField vatField = new VATField("71");
        BigDecimal XX = getXX().getAmount();
        BigDecimal YY = getYY().getAmount();
        if(XX.compareTo(YY)>0){
            vatField.setAmount(XX.subtract(YY));
        } else{
            vatField.setAmount(BigDecimal.ZERO);
        }
        return vatField;
    }
    public VATField get72() {
        VATField vatField = new VATField("72");
        BigDecimal YY = getYY().getAmount();
        BigDecimal XX = getXX().getAmount();
        if(YY.compareTo(XX)>0){
            vatField.setAmount(YY.subtract(XX));
        } else {
            vatField.setAmount(BigDecimal.ZERO);
        }
        return vatField;
    }
    public VATField getXX() {
        VATField vatField = new VATField("XX");
        vatField.setAmount(getBusinessObject("54").getAmount().add(getBusinessObject("63").getAmount()));
        return vatField;
    }

    public VATField getYY() {
        VATField vatField = new VATField("XX");
        vatField.setAmount(getBusinessObject("59").getAmount().add(getBusinessObject("64").getAmount()));
        return vatField;
    }

}
