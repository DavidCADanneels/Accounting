package be.dafke.Accounting.Objects.Coda.Parsing;

import java.io.Serializable;

public class Information implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String sequenceNumber, detailNumber, bankReference, transactionCode, communication;
	private final boolean structured, part2;

	private String communication2, communication3;
	private boolean part3, info;

	public static Information parse(String line) {
		String nr1 = line.substring(2, 6);
		String nr2 = line.substring(6, 10);
		String bankRef = line.substring(10, 31);
		String transCode = line.substring(31, 39);
		String struc = line.substring(39, 40);
		boolean structured = "1".equals(struc);
		String comm = line.substring(40, 113);
		String part2 = line.substring(125, 126);
		boolean part2Coming = "1".equals(part2);
		String info = line.substring(127, 128);
		boolean infoComing = "1".equals(info);
		return new Information(nr1, nr2, bankRef, transCode, structured, comm, part2Coming, infoComing);
	}

	public Information(String sequenceNumber, String detailNumber, String bankReference, String transactionCode,
			boolean structured, String communication, boolean part2, boolean info) {
		this.sequenceNumber = sequenceNumber;
		this.detailNumber = detailNumber;
		this.bankReference = bankReference;
		this.transactionCode = transactionCode;
		this.structured = structured;
		this.communication = communication;
		this.part2 = part2;
		this.info = info;
	}

	public void addPart2(String line) {
		String nr1 = line.substring(2, 6);
		String nr2 = line.substring(6, 10);
		if (!nr1.equals(sequenceNumber)) System.err.println("SequenceNumber not equal [" + nr1 + "!=" + sequenceNumber
				+ "]");
		if (!nr2.equals(detailNumber)) System.err.println("DetailNumber not equal [" + nr2 + "!=" + detailNumber + "]");
		communication2 = line.substring(10, 115);
		String part3String = line.substring(125, 126);
		part3 = "1".equals(part3String);
		String infoString = line.substring(127, 128);
		info = "1".equals(infoString);
	}

	public void addPart3(String line) {
		String nr1 = line.substring(2, 6);
		String nr2 = line.substring(6, 10);
		if (!nr1.equals(sequenceNumber)) System.err.println("SequenceNumber not equal [" + nr1 + "!=" + sequenceNumber
				+ "]");
		if (!nr2.equals(detailNumber)) System.err.println("DetailNumber not equal [" + nr2 + "!=" + detailNumber + "]");
		communication3 = line.substring(10, 100);
		String infoString = line.substring(127, 128);
		info = "1".equals(infoString);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("INFORMATION\r\n");
		builder.append("sequenceNumber=" + sequenceNumber + "\r\n");
		builder.append("detailNumber=" + detailNumber + "\r\n");
		builder.append("bankReference=" + bankReference + "\r\n");
		builder.append("transactionCode=" + transactionCode + "\r\n");
		builder.append("structured=" + structured + "\r\n");
		builder.append("communication=" + communication + "\r\n");
		if (part2) {
			builder.append("PART2\r\n");
			builder.append("communication2=" + communication2 + "\r\n");
		}
		if (part3) {
			builder.append("PART3\r\n");
			builder.append("communication3=" + communication3 + "\r\n");
		}
		return builder.toString();
	}

}
