package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.Utils.MultiValueMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;

/**
  * Boekhoudkundige rekening
  * @author David Danneels
  * @since 01/10/2010
 */
public class Account extends BusinessCollection<Movement> implements Comparable<Account>{
    private AccountType type;
    private BigDecimal debitTotal, creditTotal;
    private final MultiValueMap<Calendar,Movement> movements;
    private BigDecimal defaultAmount = null;
    private BigInteger number = null;
    private Contact contact = null;

    public Account(String name) {
        setName(name);
        movements = new MultiValueMap<>();
        debitTotal = BigDecimal.ZERO;
        debitTotal = debitTotal.setScale(2);
        creditTotal = BigDecimal.ZERO;
        creditTotal = creditTotal.setScale(2);
    }

    public Account(Account account){
        this(account.getName());
        setNumber(account.number);
        setDefaultAmount(account.defaultAmount);
        setType(account.type);
        setContact(account.contact);
    }

    public static Predicate<Account> name(String name) {
        return account -> account.getName()!=null && account.getName().toLowerCase().equals(name.toLowerCase());
    }

    public static Predicate<Account> namePrefix(String prefix) {
        return account -> account.getName()!=null && account.getName().toLowerCase().startsWith(prefix.toLowerCase());
    }

    public static Predicate<Account> namePrefixCaseSensitive(String prefix) {
        return account -> account.getName()!=null && account.getName().startsWith(prefix);
    }

    public static Predicate<Account> numberPrefix(String prefix){
        return account -> account.getNumber()==null||account.getNumber().toString().startsWith(prefix);
    }

    public static Predicate<Account> ofType(AccountType accountType){
        return account -> account.getType()==accountType;
    }

    @Override
    public String toString(){
        return getName();
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

    public Account getSubAccount(Predicate<Movement> predicate) {
        List<Movement> movements1 = getBusinessObjects(predicate);
        Account subAccount = new Account(this);
        for(Movement movement : movements1){
            subAccount.book(movement.getDate(), movement, true);
        }
        return subAccount;
    }

    public BigDecimal getSaldoOfYear(int year) {
        return getSubAccount(Movement.ofYear(year)).getSaldo();
    }

    public BigDecimal getSaldo() {
        BigDecimal result = debitTotal.subtract(creditTotal);
        result = result.setScale(2);
        return result;
    }

    public BigInteger getNumber() {
        return number;
    }

    public void setNumber(BigInteger number) {
        this.number = number;
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

    protected void book(Calendar date, Movement movement, boolean book) {
        movements.addValue(date, movement);
        if(book) {
            if (movement.isDebit()) {
                debitTotal = debitTotal.add(movement.getAmount());
                debitTotal = debitTotal.setScale(2);
            } else {
                creditTotal = creditTotal.add(movement.getAmount());
                creditTotal = creditTotal.setScale(2);
            }
        }
	}

    protected void unbook(Calendar date, Movement movement, boolean book) {
        if(book) {
            if (movement.isDebit()) {
                debitTotal = debitTotal.subtract(movement.getAmount());
                debitTotal = debitTotal.setScale(2);
            } else {
                creditTotal = creditTotal.subtract(movement.getAmount());
                creditTotal = creditTotal.setScale(2);
            }
        }
        movements.removeValue(date, movement);
    }

    @Override
    public Movement addBusinessObject(Movement movement) {
        return addBusinessObject(movement, true);
    }

    public Movement addBusinessObject(Movement movement, boolean book) {
        Calendar date = movement.getDate();
        book(date,movement, book);
        return movement;
    }

    @Override
    public void removeBusinessObject(Movement movement){
        removeBusinessObject(movement, true);
    }

    public void removeBusinessObject(Movement movement, boolean book){
        Calendar date = movement.getDate();
        unbook(date,movement, book);
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public int compareTo(Account o) {
        return getName().compareTo(o.getName());
    }
}