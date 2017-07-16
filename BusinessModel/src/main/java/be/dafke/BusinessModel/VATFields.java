package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.math.BigDecimal;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATFields extends BusinessCollection<VATField> {
    private Accounting accounting;

    public VATFields() {
        super();
    }
    public VATFields(Accounting accounting) {
        super();
        this.accounting = accounting;
    }
    public VATFields(VATFields vatFields) {
        this(vatFields.accounting);
        try {
            for (VATField vatField : vatFields.getBusinessObjects()) {
                addBusinessObject(vatField);
            }
        } catch (EmptyNameException e) {
            e.printStackTrace();
        } catch (DuplicateNameException e) {
            e.printStackTrace();
        }
    }

    public Accounting getAccounting() {
        return accounting;
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
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
//            BigDecimal bigDecimal = vatField.getSaldo();
//            return bigDecimal==null?BigDecimal.ZERO:bigDecimal;
        }
    }

    public VATField get71() {
        VATField vatField = new VATField("71");
        BigDecimal XX = getXX().getSaldo();
        BigDecimal YY = getYY().getSaldo();
        if(XX.compareTo(YY)>0){
            vatField.setAmount(XX.subtract(YY));
        } else{
            vatField.setAmount(BigDecimal.ZERO);
        }
        return vatField;
    }
    public VATField get72() {
        VATField vatField = new VATField("72");
        BigDecimal YY = getYY().getSaldo();
        BigDecimal XX = getXX().getSaldo();
        if(YY.compareTo(XX)>0){
            vatField.setAmount(YY.subtract(XX));
        } else {
            vatField.setAmount(BigDecimal.ZERO);
        }
        return vatField;
    }
    public VATField getXX() {
        VATField vatField = new VATField("XX");
        vatField.setAmount(getBusinessObject("54").getSaldo().add(getBusinessObject("63").getSaldo()));
        return vatField;
    }

    public VATField getYY() {
        VATField vatField = new VATField("YY");
        vatField.setAmount(getBusinessObject("59").getSaldo().add(getBusinessObject("64").getSaldo()));
        return vatField;
    }

}
