package be.dafke.Coda.Objects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import be.dafke.Coda.CodaParser;

public class OldBalance implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String type, nr, account, currency, accountHolder, accountDescription;
	private final BigDecimal amount;
	private final Calendar date;
	private final boolean debit;

	public static OldBalance parse(String line) {
		String type = line.substring(1, 2);
		if ("0".equals(type)) {
			type = "Belgian";
		} else if ("1".equals(type)) {
			type = "Foreign";
		} else if ("2".equals(type)) {
			type = "Belgian IBAN";
		} else if ("3".equals(type)) {
			type = "Foreign IBAN";
		}
		String nr = line.substring(2, 5);
		String account = line.substring(5, 39).trim();
		String currency = line.substring(39, 42);
		String sign = line.substring(42, 43);
		boolean debit = "1".equals(sign);
		String amountString = line.substring(43, 58);
		BigDecimal amount = CodaParser.convertBigDecimal(amountString);
		String date = line.substring(58, 64);
		Calendar cal = CodaParser.convertDate(date);
		String accountHolder = line.substring(64, 90).trim();
		String accountDescription = line.substring(90, 125).trim();
		// String nr2= line.substring(125, 128); // = nr
		return new OldBalance(type, nr, account, currency, debit, amount, cal, accountHolder, accountDescription);
	}

	public OldBalance(String type, String nr, String account, String currency, boolean debit, BigDecimal amount,
			Calendar date, String accountHolder, String accountDescription) {
		this.type = type;
		this.nr = nr;
		this.account = account;
		this.currency = currency;
		this.debit = debit;
		this.amount = amount;
		this.date = date;
		this.accountHolder = accountHolder;
		this.accountDescription = accountDescription;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("OLD BALANCE\r\n");
		builder.append("type=" + type + "\r\n");
		builder.append("nr=" + nr + "\r\n");
		builder.append("account=" + account + "\r\n");
		builder.append("currency=" + currency + "\r\n");
		if (debit) builder.append("D/C = D" + "\r\n");
		else builder.append("D/C = C" + "\r\n");
		builder.append("amount=" + amount + "\r\n");
		builder.append("date=" + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1) + "/"
				+ date.get(Calendar.YEAR) + "\r\n");
		builder.append("accountHolder=" + accountHolder + "\r\n");
		builder.append("accountDescription=" + accountDescription + "\r\n");
		return builder.toString();
	}
}
