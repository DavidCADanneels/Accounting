package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Utils.MultiValueMap

import java.util.function.Predicate

class Account extends BusinessCollection<Movement> implements Comparable<Account>{
    AccountType type
    BigDecimal debitTotal, creditTotal
    final MultiValueMap<Calendar,Movement> movements
    BigDecimal defaultAmount = null
    BigInteger number = null
    Contact contact = null

    Account(String name) {
        setName(name)
        movements = new MultiValueMap()
        debitTotal = BigDecimal.ZERO
        debitTotal = debitTotal.setScale(2)
        creditTotal = BigDecimal.ZERO
        creditTotal = creditTotal.setScale(2)
    }

    Account(Account account){
        this(account.name)
        number = account.number
        defaultAmount = account.defaultAmount
        type= account.type
        contact = account.contact
    }

    static Predicate<Account> name(String name) {
        { account -> account.name != null && account.name.toLowerCase().equals(name.toLowerCase()) }
    }

    static Predicate<Account> namePrefix(String prefix) {
        { Account account -> prefix == null || (account.name != null && account.name.toLowerCase().startsWith(prefix.toLowerCase())) }
    }

    static Predicate<Account> saldoNotZero() {
        { account -> account.saldo.compareTo(BigDecimal.ZERO) != 0 }
    }

    static Predicate<Account> namePrefixCaseSensitive(String prefix) {
        { account -> prefix == null || (account.name != null && account.name.startsWith(prefix)) }
    }

    static Predicate<Account> numberPrefix(String prefix){
        { account -> prefix == null || (account.number == null || account.number.toString().startsWith(prefix)) }
    }

    static Predicate<Account> ofType(AccountType accountType){
        { account -> account.type == accountType }
    }

    @Override
    String toString(){
        getName()
    }

    Account getSubAccount(Predicate<Movement> predicate) {
        List<Movement> movements1 = getBusinessObjects(predicate)
        Account subAccount = new Account(this)
        for(Movement movement : movements1){
            subAccount.book(movement.date, movement, true)
        }
        subAccount
    }

    BigDecimal getSaldoOfYear(int year) {
        getSubAccount(Movement.ofYear(year)).saldo
    }

    BigDecimal getSaldo() {
        BigDecimal result = debitTotal.subtract creditTotal
        result = result.setScale(2)
        result
    }

    @Override
    ArrayList<Movement> getBusinessObjects() {
        movements.values()
    }

    @Override
    boolean isDeletable(){
        businessObjects.empty
    }

//    protected
    void book(Calendar date, Movement movement, boolean book) {
        movements.addValue(date, movement)
        if(book) {
            if (movement.debit) {
                debitTotal = debitTotal.add(movement.amount)
                debitTotal = debitTotal.setScale(2)
            } else {
                creditTotal = creditTotal.add(movement.amount)
                creditTotal = creditTotal.setScale(2)
            }
        }
    }

//    protected
    void unbook(Calendar date, Movement movement, boolean book) {
        if(book) {
            if (movement.debit) {
                debitTotal = debitTotal.subtract(movement.amount)
                debitTotal = debitTotal.setScale(2)
            } else {
                creditTotal = creditTotal.subtract(movement.amount)
                creditTotal = creditTotal.setScale(2)
            }
        }
        movements.removeValue(date, movement)
    }

    @Override
    Movement addBusinessObject(Movement movement) {
        addBusinessObject(movement, true)
    }

    Movement addBusinessObject(Movement movement, boolean toBook) {
        Calendar date = movement.date
        book(date,movement, toBook)
        movement
    }

    @Override
    void removeBusinessObject(Movement movement){
        removeBusinessObject(movement, true)
    }

    void removeBusinessObject(Movement movement, boolean book){
        Calendar date = movement.date
        unbook(date,movement, book)
    }

    @Override
    int compareTo(Account o) {
        getName().compareTo(o.name)
    }
}