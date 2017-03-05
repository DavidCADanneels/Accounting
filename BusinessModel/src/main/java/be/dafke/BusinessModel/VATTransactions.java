package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.Utils.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ddanneels on 25/12/2016.
 */
public class VATTransactions extends BusinessCollection<VATTransaction> {
    private final VATFields vatFields;
    private Account creditAccount, debitAccount, creditCNAccount, debitCNAccount;
    private Integer[] vatPercentages = new Integer[]{0, 6, 12, 21};
    private final MultiValueMap<Calendar,VATTransaction> vatTransactions = new MultiValueMap<>();
    private Accounting accounting;

    public Integer[] getVatPercentages() {
        return vatPercentages;
    }

    public void setCreditAccount(Account creditAccount) {
        this.creditAccount = creditAccount;
    }

    public void setDebitAccount(Account debitAccount) {
        this.debitAccount = debitAccount;
    }

    public Account getCreditAccount() {
        return creditAccount;
    }

    public Account getDebitAccount() {
        return debitAccount;
    }

    public Account getCreditCNAccount() {
        return creditCNAccount;
    }

    public void setCreditCNAccount(Account creditCNAccount) {
        this.creditCNAccount = creditCNAccount;
    }

    public Account getDebitCNAccount() {
        return debitCNAccount;
    }

    public void setDebitCNAccount(Account debitCNAccount) {
        this.debitCNAccount = debitCNAccount;
    }

    public VATTransactions(Accounting accounting) {
        this.accounting = accounting;
        this.vatFields = accounting.getVatFields();
    }

    @Override
    public VATTransaction addBusinessObject(VATTransaction vatTransaction){
        if(vatTransaction!=null) {
            Calendar date = vatTransaction.getDate();
            vatTransactions.addValue(date, vatTransaction);
            for (VATBooking vatBooking : vatTransaction.getBusinessObjects()) {
                VATField vatField = vatBooking.getVatField();
                if (vatField != null) {
                    vatField.addBusinessObject(vatBooking.getVatMovement());
                }
            }
//            Contact contact = vatTransaction.getContact();
//            BigDecimal turnOverAmount = vatTransaction.getTurnOverAmount();
//            BigDecimal vatAmount = vatTransaction.getVATAmount();
//            if (contact != null && turnOverAmount != null && vatAmount != null) {
//                contact.increaseTurnOver(turnOverAmount);
//                contact.increaseVATTotal(vatAmount);
//            }

        }
        return vatTransaction;
    }

    public Accounting getAccounting() {
        return accounting;
    }


    @Override
    public ArrayList<VATTransaction> getBusinessObjects(){
        return new ArrayList<>(vatTransactions.values());
    }

    public VATTransaction getBusinessObject(Integer id){
        return vatTransactions.values().stream().filter(vatTransaction -> vatTransaction.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void removeBusinessObject(VATTransaction vatTransaction){
        Calendar date = vatTransaction.getTransaction().getDate();
        vatTransactions.removeValue(date,vatTransaction);
        for(VATBooking vatBooking:vatTransaction.getBusinessObjects()){
            VATField vatField = vatBooking.getVatField();
            if(vatField!=null) {
                vatField.removeBusinessObject(vatBooking.getVatMovement());
            }
        }
    }

    private VATField getCostField(VATTransaction.PurchaseType purchaseType){
        if(purchaseType== VATTransaction.PurchaseType.GOODS){
            return vatFields.getBusinessObject("81");
        } else if(purchaseType==VATTransaction.PurchaseType.SERVICES){
            return vatFields.getBusinessObject("82");
        } else if(purchaseType==VATTransaction.PurchaseType.INVESTMENTS){
            return vatFields.getBusinessObject("83");
        }
        return null;
    }

    public VATTransaction purchase(Booking booking, Booking bookingVat, VATTransaction.PurchaseType purchaseType) {
        BigDecimal vatAmount = bookingVat.getAmount();

        BigDecimal costAmount = booking.getAmount();
        Transaction transaction = booking.getTransaction();
        Calendar date = transaction.getDate();
        // TODO: check if date is changed if date is changed afterwards + remove date parameter from constructor)
        VATTransaction vatTransaction = new VATTransaction(date);

        VATField vatField = getCostField(purchaseType);
        VATMovement vatMovement = new VATMovement(costAmount);
        VATBooking costBooking = new VATBooking(vatField, vatMovement);
        booking.addVatBooking(costBooking);

        VATBooking vatBooking = new VATBooking(vatFields.getBusinessObject("59"), new VATMovement(vatAmount));
        bookingVat.addVatBooking(vatBooking);

        vatTransaction.addBusinessObject(costBooking);
        vatTransaction.addBusinessObject(vatBooking);
        return vatTransaction;
    }

    public VATTransaction purchaseCN(Booking booking, Booking bookingVat, VATTransaction.PurchaseType purchaseType) {
        BigDecimal vatAmount = bookingVat.getAmount();

        BigDecimal costAmount = booking.getAmount();
        Transaction transaction = booking.getTransaction();
        Calendar date = transaction.getDate();
        VATTransaction vatTransaction = new VATTransaction(date);

        VATBooking CNCostBooking = new VATBooking(vatFields.getBusinessObject("85"), new VATMovement(costAmount));
        VATBooking CNVATBooking = new VATBooking(vatFields.getBusinessObject("63"), new VATMovement(vatAmount));

        VATField vatField = getCostField(purchaseType);
        VATMovement vatMovement = new VATMovement(costAmount.negate());
        VATBooking costBooking = new VATBooking(vatField, vatMovement);
        booking.addVatBooking(costBooking);
        booking.addVatBooking(CNCostBooking);
        bookingVat.addVatBooking(CNVATBooking);

        vatTransaction.addBusinessObject(CNCostBooking);
        vatTransaction.addBusinessObject(CNVATBooking);
        vatTransaction.addBusinessObject(costBooking);
        return vatTransaction;
    }

    public VATTransaction sale(Booking booking, Booking bookingVat, Integer pct) {
        BigDecimal vatAmount = bookingVat.getAmount();
        VATBooking revenueBooking = null;
        BigDecimal revenueAmount = booking.getAmount();
        Transaction transaction = booking.getTransaction();
        Calendar date = transaction.getDate();
        VATTransaction vatTransaction = new VATTransaction(date);

        if(pct==0){
            revenueBooking = new VATBooking(vatFields.getBusinessObject("0"), new VATMovement(revenueAmount));
        } else if(pct==6){
            revenueBooking = new VATBooking(vatFields.getBusinessObject("1"), new VATMovement(revenueAmount));
        } else if(pct==12){
            revenueBooking = new VATBooking(vatFields.getBusinessObject("2"), new VATMovement(revenueAmount));
        } else if(pct==21){
            revenueBooking = new VATBooking(vatFields.getBusinessObject("3"), new VATMovement(revenueAmount));
        }
        booking.addVatBooking(revenueBooking);

        VATBooking vatBooking = new VATBooking(vatFields.getBusinessObject("54"), new VATMovement(vatAmount));
        bookingVat.addVatBooking(vatBooking);

        vatTransaction.addBusinessObject(revenueBooking);
        vatTransaction.addBusinessObject(vatBooking);
        return vatTransaction;
    }

    public VATTransaction saleCN(Booking booking, Booking bookingVat, int pct) {
        BigDecimal btwAmount = bookingVat.getAmount();

        BigDecimal revenueAmount = booking.getAmount();
        Transaction transaction = booking.getTransaction();
        Calendar date = transaction.getDate();
        VATTransaction vatTransaction = new VATTransaction(date);
        VATBooking revenueBooking = new VATBooking(vatFields.getBusinessObject("49"), new VATMovement(revenueAmount));
        VATBooking vatBooking = new VATBooking(vatFields.getBusinessObject("64"), new VATMovement(btwAmount));
        vatTransaction.addBusinessObject(revenueBooking);
        vatTransaction.addBusinessObject(vatBooking);

        booking.addVatBooking(revenueBooking);
        bookingVat.addVatBooking(vatBooking);

        return vatTransaction;
    }

}
