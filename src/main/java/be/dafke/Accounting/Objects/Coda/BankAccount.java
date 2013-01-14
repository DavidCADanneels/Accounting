package be.dafke.Accounting.Objects.Coda;

import java.io.Serializable;

public class BankAccount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*	public enum AccountType{
			Belgian, Foreign, Belgian_IBAN, Foreign_IBAN;
		}
	*/private final String accountNumber;
	// private AccountType accountType;
	private String bic;
	private String currency;

	public BankAccount(String accountNumber) {// , AccountType accountType){
		this.accountNumber = accountNumber;
		// this.accountType = accountType;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(accountNumber + "\r\n");
		builder.append(bic + "\r\n");
		builder.append(currency + "\r\n");
		return builder.toString();
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getBic() {
		return bic;
	}

	public String getCurrency() {
		return currency;
	}

}
