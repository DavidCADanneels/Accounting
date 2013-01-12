package be.dafke.Coda.Objects;

import java.io.Serializable;
import java.util.Calendar;

import be.dafke.Coda.CodaParser;

public class Header implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Calendar date;
	private final boolean duplicate;
	private final String bank, BIC, fileReference, addressee, transactionReference, relatedReference, companyNumber;

	public static Header parse(String line) {
		String date = line.substring(5, 11);
		Calendar cal = CodaParser.convertDate(date);
		int bank = Integer.parseInt(line.substring(11, 14));
		boolean duplicate = "D".equals(line.substring(16, 17));
		String fileReference = line.substring(24, 34);
		String addressee = line.substring(34, 60).trim();
		String BIC = line.substring(60, 71).trim();
		String companyNumber = line.substring(71, 82).trim();
		String transactionReference = line.substring(88, 104).trim();
		String relatedReference = line.substring(104, 120).trim();
		return new Header(cal, bank, duplicate, fileReference, addressee, BIC, companyNumber, transactionReference,
				relatedReference);
	}

	public Header(Calendar date, int bankNumber, boolean duplicate, String fileReference, String addressee, String BIC,
			String companyNumber, String transactionReference, String relatedReference) {
		this.date = date;
		/*		if(CodaParser.banks.containsKey(bankNumber)){
					bank = CodaParser.banks.get(bankNumber)+ " ("+bankNumber+")";
				}else{
		*/bank = "Unknown Bank (" + bankNumber + ")";
		// }
		this.duplicate = duplicate;
		this.fileReference = fileReference;
		this.addressee = addressee;
		this.BIC = BIC;
		this.transactionReference = transactionReference;
		this.relatedReference = relatedReference;
		this.companyNumber = companyNumber;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("HEADER\r\n");
		builder.append("date=" + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1) + "/"
				+ date.get(Calendar.YEAR) + "\r\n");
		builder.append("bank=" + bank + "\r\n");
		builder.append("duplicate=" + duplicate + "\r\n");
		builder.append("fileReference=" + fileReference + "\r\n");
		builder.append("addressee=" + addressee + "\r\n");
		builder.append("BIC=" + BIC + "\r\n");
		builder.append("transactionReference=" + transactionReference + "\r\n");
		builder.append("relatedReference=" + relatedReference + "\r\n");
		builder.append("companyNumber=" + companyNumber + "\r\n");
		return builder.toString();
	}
}
