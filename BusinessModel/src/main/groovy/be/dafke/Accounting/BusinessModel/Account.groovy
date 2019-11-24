package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Utils.MultiValueMap

import java.util.function.Predicate

class Account extends BusinessCollection<Movement> implements Comparable<Account>{
    private AccountType type
    private BigDecimal debitTotal, creditTotal
    private final MultiValueMap<Calendar,Movement> movements
    private BigDecimal defaultAmount = null
    private BigInteger number = null
    private Contact contact = null

    Account(String name) {
        setName(name)
        movements = new MultiValueMap()
        debitTotal = BigDecimal.ZERO
        debitTotal = debitTotal.setScale(2)
        creditTotal = BigDecimal.ZERO
        creditTotal = creditTotal.setScale(2)
    }

    Account(Account account){
        this(account.getName())
        setNumber(account.number)
        setDefaultAmount(account.defaultAmount)
        setType(account.type)
        setContact(account.contact)
    }

    static Predicate<Account> name(String name) {
        { account -> account.getName() != null && account.getName().toLowerCase().equals(name.toLowerCase()) }
    }

    static Predicate<Account> namePrefix(String prefix) {
        { Account account -> account.getName() != null && account.getName().toLowerCase().startsWith(prefix.toLowerCase()) }
    }

    static Predicate<Account> saldoNotZero() {
        { account -> account.getSaldo().compareTo(BigDecimal.ZERO) != 0 }
    }

    static Predicate<Account> namePrefixCaseSensitive(String prefix) {
        { account -> account.getName() != null && account.getName().startsWith(prefix) }
    }

    static Predicate<Account> numberPrefix(String prefix){
        { account -> account.getNumber() == null || account.getNumber().toString().startsWith(prefix) }
    }

    static Predicate<Account> ofType(AccountType accountType){
        { account -> account.getType() == accountType }
    }

    @Override
    String toString(){
        getName()
    }

    void setType(AccountType type) {
        this.type = type
    }

    AccountType getType() {
        type
    }

    void setDefaultAmount(BigDecimal defaultAmount) {
        this.defaultAmount = defaultAmount
    }

    BigDecimal getDefaultAmount() {
        defaultAmount
    }

    Account getSubAccount(Predicate<Movement> predicate) {
        List<Movement> movements1 = getBusinessObjects(predicate)
        Account subAccount = new Account(this)
        for(Movement movement : movements1){
            subAccount.book(movement.getDate(), movement, true)
        }
        subAccount
    }

    BigDecimal getSaldoOfYear(int year) {
        getSubAccount(Movement.ofYear(year)).getSaldo()
    }

    BigDecimal getSaldo() {
        BigDecimal result = debitTotal.subtract(creditTotal)
        result = result.setScale(2)
        result
    }

    BigInteger getNumber() {
        number
    }

    void setNumber(BigInteger number) {
        this.number = number
    }

    BigDecimal getDebetTotal() {
        debitTotal
    }

    BigDecimal getCreditTotal() {
        creditTotal
    }

    @Override
    ArrayList<Movement> getBusinessObjects() {
        movements.values()
    }

    @Override
    boolean isDeletable(){
        movements.isEmpty()
    }

//    protected
    void book(Calendar date, Movement movement, boolean book) {
        movements.addValue(date, movement)
        if(book) {
            if (movement.isDebit()) {
                debitTotal = debitTotal.add(movement.getAmount())
                debitTotal = debitTotal.setScale(2)
            } else {
                creditTotal = creditTotal.add(movement.getAmount())
                creditTotal = creditTotal.setScale(2)
            }
        }
    }

//    protected
    void unbook(Calendar date, Movement movement, boolean book) {
        if(book) {
            if (movement.isDebit()) {
                debitTotal = debitTotal.subtract(movement.getAmount())
                debitTotal = debitTotal.setScale(2)
            } else {
                creditTotal = creditTotal.subtract(movement.getAmount())
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
        Calendar date = movement.getDate()
        book(date,movement, toBook)
        movement
    }

    @Override
    void removeBusinessObject(Movement movement){
        removeBusinessObject(movement, true)
    }

    void removeBusinessObject(Movement movement, boolean book){
        Calendar date = movement.getDate()
        unbook(date,movement, book)
    }

    Contact getContact() {
        contact
    }

    void setContact(Contact contact) {
        this.contact = contact
    }

    @Override
    int compareTo(Account o) {
        getName().compareTo(o.getName())
    }
}