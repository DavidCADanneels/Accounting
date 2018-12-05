package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ddanneels on 25/12/2016.
 */
public class VATTransactions extends BusinessCollection<VATTransaction> {
    private final VATFields vatFields;
    private Account creditAccount, debitAccount, creditCNAccount, debitCNAccount;
    private Account deliverooBalanceAccount, deliverooServiceAccount, deliverooRevenueAccount;
    private Journal deliverooSalesJournal, deliverooServiceJournal;
    private Integer[] vatPercentages = new Integer[]{0, 6, 12, 21};
    private final HashMap<Integer,VATTransaction> vatTransactionsPerId = new HashMap<>();
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

    public Account getDeliverooBalanceAccount() {
        return deliverooBalanceAccount;
    }

    public void setDeliverooBalanceAccount(Account deliverooBalanceAccount) {
        this.deliverooBalanceAccount = deliverooBalanceAccount;
    }

    public Account getDeliverooServiceAccount() {
        return deliverooServiceAccount;
    }

    public void setDeliverooServiceAccount(Account deliverooServiceAccount) {
        this.deliverooServiceAccount = deliverooServiceAccount;
    }

    public Account getDeliverooRevenueAccount() {
        return deliverooRevenueAccount;
    }

    public void setDeliverooRevenueAccount(Account deliverooRevenueAccount) {
        this.deliverooRevenueAccount = deliverooRevenueAccount;
    }

    public Journal getDeliverooSalesJournal() {
        return deliverooSalesJournal;
    }

    public void setDeliverooSalesJournal(Journal deliverooSalesJournal) {
        this.deliverooSalesJournal = deliverooSalesJournal;
    }

    public Journal getDeliverooServiceJournal() {
        return deliverooServiceJournal;
    }

    public void setDeliverooServiceJournal(Journal deliverooServiceJournal) {
        this.deliverooServiceJournal = deliverooServiceJournal;
    }

    public VATTransactions(Accounting accounting) {
        this.accounting = accounting;
        this.vatFields = accounting.getVatFields();
    }

    public VATTransactions(VATFields vatFields) {
        this.accounting = null;
        this.vatFields = vatFields;
    }

    @Override
    public VATTransaction addBusinessObject(VATTransaction vatTransaction){
        if(vatTransaction!=null) {
            vatTransactionsPerId.put(vatTransaction.getId(), vatTransaction);
            if(!vatTransaction.isRegistered()) {
                for (VATBooking vatBooking : vatTransaction.getBusinessObjects()) {
                    VATField vatField = vatBooking.getVatField();
                    if (vatField != null) {
                        vatField.addBusinessObject(vatBooking.getVatMovement());
                    }
                }
            }
        }
        return vatTransaction;
    }

    public void registerVATTransactions(List<VATTransaction> vatTransactions){
        if(vatTransactions!=null) {
            vatTransactions.forEach(vatTransaction -> {
                vatTransaction.setRegistered();
                ArrayList<VATBooking> businessObjects = vatTransaction.getBusinessObjects();
                for (VATBooking vatBooking : businessObjects){
                    VATField vatField = vatBooking.getVatField();
                    if (vatField != null) {
                        vatField.setRegistered(vatBooking.getVatMovement());
                    }
                }
            });
        }
    }

    public Accounting getAccounting() {
        return accounting;
    }


    @Override
    public ArrayList<VATTransaction> getBusinessObjects(){
        return new ArrayList<>(vatTransactionsPerId.values());
    }

    public VATTransaction getBusinessObject(Integer id){
        return vatTransactionsPerId.get(id);
    }

    @Override
    public void removeBusinessObject(VATTransaction vatTransaction){
        vatTransactionsPerId.remove(vatTransaction.getId());
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
        VATTransaction vatTransaction = new VATTransaction();

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
        VATTransaction vatTransaction = new VATTransaction();

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

    public VATBooking getVatSalesBooking(Booking bookingVat){
        BigDecimal vatAmount = bookingVat.getAmount();
        return new VATBooking(vatFields.getBusinessObject("54"), new VATMovement(vatAmount));
    }
}
