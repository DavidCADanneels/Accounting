package be.dafke.Accounting.Objects.Coda.Parsing;

import be.dafke.Accounting.Dao.Coda.CodaParser;

import java.io.Serializable;
import java.math.BigDecimal;

public class Trailer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int nrOfLines;
	private final BigDecimal debitTotal, creditTotal;
	private final boolean haveMoreFiles;

	public static Trailer parse(String line) {
		String nr = line.substring(16, 22);
		String debit = line.substring(22, 37);
		String credit = line.substring(37, 52);
		boolean haveMore = "1".equals(line.substring(127, 128));
		return new Trailer(Integer.parseInt(nr), CodaParser.convertBigDecimal(debit),
				CodaParser.convertBigDecimal(credit), haveMore);
	}

	public Trailer(int nrOfLines, BigDecimal debitTotal, BigDecimal creditTotal, boolean haveMoreFiles) {
		this.nrOfLines = nrOfLines;
		this.debitTotal = debitTotal;
		this.creditTotal = creditTotal;
		this.haveMoreFiles = haveMoreFiles;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("TRAILER\r\n");
		builder.append("nrOfLines=" + nrOfLines + "\r\n");
		builder.append("debitTotal=" + debitTotal + "\r\n");
		builder.append("creditTotal=" + creditTotal + "\r\n");
		builder.append("haveMoreFiles=" + haveMoreFiles + "\r\n");
		return builder.toString();
	}

}
