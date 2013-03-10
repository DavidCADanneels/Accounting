package be.dafke.Accounting.Objects.Accounting;

import be.dafke.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

/**
  * Boekhoudkundige rekening
  * @author David Danneels
  * @since 01/10/2010
 */
public class Account extends WriteableBusinessObject {
	private AccountType type;
    private BigDecimal debitTotal, creditTotal;
    private final MultiValueMap<Calendar,Movement> movements;

    public Account() {
        movements = new MultiValueMap<Calendar,Movement>();
        debitTotal = BigDecimal.ZERO;
        debitTotal = debitTotal.setScale(2);
        creditTotal = BigDecimal.ZERO;
        creditTotal = creditTotal.setScale(2);
    }

    // Setters
    public void setAccountType(AccountType type) {
        this.type = type;
    }

    // Getters
    public AccountType getAccountType() {
        return type;
    }

    public BigDecimal getSaldo() {
        BigDecimal result = debitTotal.subtract(creditTotal);
        result = result.setScale(2);
        return result;
    }

    public BigDecimal getDebetTotal() {
        return debitTotal;
    }

    public BigDecimal getCreditTotal() {
        return creditTotal;
    }

    public ArrayList<Movement> getMovements() {
        return movements.values();
    }

    @Override
    public boolean isDeletable(){
        return movements.isEmpty();
    }

	protected void book(Calendar date, Movement movement) {
        movements.addValue(date, movement);
		if (movement.isDebit()) {
            debitTotal = debitTotal.add(movement.getAmount());
            debitTotal = debitTotal.setScale(2);
		} else {
            creditTotal = creditTotal.add(movement.getAmount());
            creditTotal = creditTotal.setScale(2);
		}
	}

	protected Movement unbook(Calendar date, Movement movement) {
		if (movement.isDebit()) {
			debitTotal = debitTotal.subtract(movement.getAmount());
			debitTotal = debitTotal.setScale(2);
		} else {
			creditTotal = creditTotal.subtract(movement.getAmount());
			creditTotal = creditTotal.setScale(2);
		}
        movements.removeValue(date, movement);
        return movement;
    }
}