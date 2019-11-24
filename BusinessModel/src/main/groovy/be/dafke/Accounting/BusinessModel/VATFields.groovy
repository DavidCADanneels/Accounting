package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

class VATFields extends BusinessCollection<VATField> {
    private Accounting accounting

    VATFields() {
        super()
    }
    VATFields(Accounting accounting) {
        super()
        this.accounting = accounting
    }
    VATFields(VATFields vatFields) {
        this(vatFields.accounting)
        try {
            for (VATField vatField : vatFields.getBusinessObjects()) {
                addBusinessObject(vatField)
            }
        } catch (EmptyNameException e) {
            e.printStackTrace()
        } catch (DuplicateNameException e) {
            e.printStackTrace()
        }
    }

    Accounting getAccounting() {
        accounting
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
    }

    void addDefaultFields(){
        try {
            addBusinessObject(new VATField("0"))
            addBusinessObject(new VATField("1"))
            addBusinessObject(new VATField("2"))
            addBusinessObject(new VATField("3"))
            addBusinessObject(new VATField("46"))
            addBusinessObject(new VATField("49"))
            addBusinessObject(new VATField("54"))
            addBusinessObject(new VATField("55"))
            addBusinessObject(new VATField("59"))
            addBusinessObject(new VATField("63"))
            addBusinessObject(new VATField("64"))
            addBusinessObject(new VATField("81"))
            addBusinessObject(new VATField("82"))
            addBusinessObject(new VATField("83"))
            addBusinessObject(new VATField("85"))
            addBusinessObject(new VATField("86"))
            // Next 4 fields have calculated amounts
//            addBusinessObject(new VATField("XX"))
//            addBusinessObject(new VATField("YY"))
//            addBusinessObject(new VATField("71"))
//            addBusinessObject(new VATField("72"))
        } catch (EmptyNameException e) {
            e.printStackTrace()
        } catch (DuplicateNameException e) {
            e.printStackTrace()
        }
    }

    @Override
    VATField getBusinessObject(String nr){
        if("XX".equals(nr)){
            getXX()
        } else if("YY".equals(nr)){
            getYY()
        } else if("71".equals(nr)){
            get71()
        } else if("72".equals(nr)){
            get72()
        } else{
            super.getBusinessObject(nr)
//            if(vatField==null) BigDecimal.ZERO
//            BigDecimal bigDecimal = vatField.getSaldo()
//            bigDecimal==null?BigDecimal.ZERO:bigDecimal
        }
    }

    VATField get71() {
        VATField vatField = new VATField("71")
        BigDecimal XX = getXX().getSaldo()
        BigDecimal YY = getYY().getSaldo()
        if(XX.compareTo(YY)>0){
            vatField.setAmount(XX.subtract(YY))
        } else{
            vatField.setAmount(BigDecimal.ZERO)
        }
        vatField
    }
    VATField get72() {
        VATField vatField = new VATField("72")
        BigDecimal YY = getYY().getSaldo()
        BigDecimal XX = getXX().getSaldo()
        if(YY.compareTo(XX)>0){
            vatField.setAmount(YY.subtract(XX))
        } else {
            vatField.setAmount(BigDecimal.ZERO)
        }
        vatField
    }
    VATField getXX() {
        VATField vatField = new VATField("XX")
        vatField.setAmount(getBusinessObject("54").getSaldo().add(getBusinessObject("55").getSaldo()).add(getBusinessObject("63").getSaldo()))
        vatField
    }

    VATField getYY() {
        VATField vatField = new VATField("YY")
        vatField.setAmount(getBusinessObject("59").getSaldo().add(getBusinessObject("64").getSaldo()))
        vatField
    }

}
