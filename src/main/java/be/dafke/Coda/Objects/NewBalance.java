package be.dafke.Coda.Objects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import be.dafke.Coda.CodaParser;

public class NewBalance implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String nr, account, currency;
	private final BigDecimal amount;
	private final Calendar date;
	private final boolean debit;

	public static NewBalance parse(String line) {
		String nr = line.substring(1, 4);
		String account = line.substring(4, 38).trim();
		String currency = line.substring(38, 41);
		String sign = line.substring(41, 42);
		boolean debit = "1".equals(sign);
		String amountString = line.substring(42, 57);
		BigDecimal amount = CodaParser.convertBigDecimal(amountString);
		String date = line.substring(57, 63);
		Calendar cal = CodaParser.convertDate(date);
		return new NewBalance(nr, account, currency, debit, amount, cal);
	}

	public NewBalance(String nr, String account, String currency, boolean debit, BigDecimal amount, Calendar date) {
		this.nr = nr;
		this.account = account;
		this.currency = currency;
		this.debit = debit;
		this.amount = amount;
		this.date = date;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("NEW BALANCE\r\n");
		builder.append("nr=" + nr + "\r\n");
		builder.append("account=" + account + "\r\n");
		builder.append("currency=" + currency + "\r\n");
		if (debit) builder.append("D/C = D" + "\r\n");
		else builder.append("D/C = C" + "\r\n");
		builder.append("amount=" + amount + "\r\n");
		builder.append("date=" + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1) + "/"
				+ date.get(Calendar.YEAR) + "\r\n");
		return builder.toString();
	}
}
