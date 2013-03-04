package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Dao.Coda.CodaParser;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

public class Movement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String sequenceNumber, transactionCode;
	private String communication;
	private final String statementNr;
	private final boolean debit;
    private boolean structured;
	private final BigDecimal amount;
	private final Calendar date;

	private String counterPartyBic;

	private CounterParty counterParty;
	private TmpCounterParty tmpCounterParty;
	private static CounterParties counterParties;
//    private String name;
//    private File xmlFile;
//    private File dtdFile;
//    private File xsl2XmlFile;
//    private File xsl2HtmlFile;

    public static Movement parse(String line, CounterParties counterParties) {
		Movement.counterParties = counterParties;
		String sequenceNumber = line.substring(2, 6).trim();
		String sign = line.substring(31, 32).trim();
		boolean debit = "1".equals(sign);
		String amountString = line.substring(32, 47).trim();
		BigDecimal amount = CodaParser.convertBigDecimal(amountString);
		String dateString = line.substring(47, 53).trim();
		Calendar date = CodaParser.convertDate(dateString);
//		String transactionCode = line.substring(53, 61).trim;
		String transactionCode = line.substring(55, 58).trim();
		String struc = line.substring(61, 62).trim();
		boolean structured = "1".equals(struc);
		String communication = line.substring(62, 115).trim();
		String statementNumber = line.substring(121, 124).trim();
		// trim numbers
		while (statementNumber.startsWith("0")) {
			statementNumber = statementNumber.substring(1);
		}
		while (sequenceNumber.startsWith("0")) {
			sequenceNumber = sequenceNumber.substring(1);
		}
		return new Movement(statementNumber, sequenceNumber, date, debit, amount, transactionCode, communication, structured);
	}

    public Movement(String statementNr, String sequenceNumber, Calendar date, boolean debit, BigDecimal amount, CounterParty counterParty, String transactionCode, String communication){
        this.statementNr = statementNr;
        this.sequenceNumber = sequenceNumber;
        this.date = date;
        this.debit = debit;
        this.amount = amount;
        this.counterParty = counterParty;
        this.transactionCode = transactionCode;
        this.communication = communication;
//        this.name = statementNr + "-" + sequenceNumber;
    }

	private Movement(String statementNr, String sequenceNumber, Calendar date, boolean debit, BigDecimal amount,
			 String transactionCode, String communication, boolean structured) {
        this.statementNr = statementNr;
        this.sequenceNumber = sequenceNumber;
        this.date = date;
        this.debit = debit;
        this.amount = amount;
        this.transactionCode = transactionCode.trim();
        this.communication = communication;
        this.structured = structured;
        resetCommunication();
    }

    private void resetCommunication(){
        if (transactionCode.equals("402") || transactionCode.equals("404")) {
            communication = communication.replaceAll("[0-9]", "");
            communication = communication.trim();
        }else if (structured) {
            String start = communication.substring(0, 3);
            communication = communication.substring(3);
            if (start.equals("101") && communication.length() >= 12) {
                // +++123/1234/12345+++
                String part1 = communication.substring(0, 3);
                String part2 = communication.substring(3, 7);
                String part3 = communication.substring(7, 12);
                communication = "+++" + part1 + "/" + part2 + "/" + part3 + "+++";
            } else if (start.equals("107")) {
                communication = communication.substring(18);
                while (communication.startsWith("0")) {
                    communication = communication.substring(1);
                }
                communication = communication.trim();
            }
            if(transactionCode.equals("501")){
                if(communication.length()>14){
                    communication = communication.substring(0,14);
                }
                communication = communication.trim();
            }
        }
    }

    private void resetCommunication(String communication2){
        if (!structured && !transactionCode.equals("402") && !transactionCode.equals("404") && communication2 != null && !communication2.trim().isEmpty()) {
            // momenteel enkel indien transactionCode = 150 (CM)
            communication = communication.trim() + communication2.trim();
        }
    }

    public void addPart2(String line) {
		String communication2 = line.substring(10, 63).trim();
		counterPartyBic = line.substring(98, 109).trim();
        resetCommunication(communication2);
    }

	public void addPart3(String line) {
		String nr1 = line.substring(2, 6).trim();
		String nr2 = line.substring(6, 10).trim();
		while (nr1.startsWith("0")) {
			nr1 = nr1.substring(1);
		}
		while (nr2.startsWith("0")) {
			nr2 = nr2.substring(1);
		}
		if (!nr1.equals(sequenceNumber)) System.err.println("SequenceNumber not equal [" + nr1 + "!=" + sequenceNumber
				+ "]");
		String counterPartyAccount = line.substring(10, 44).trim();
        String counterPartyCurrency = line.substring(44, 47).trim();
        String counterPartyName = line.substring(47, 82).trim();
        BankAccount bankAccount = null;
		if (!counterPartyAccount.trim().equals("")) {
			bankAccount = new BankAccount(counterPartyAccount);
            bankAccount.setBic(counterPartyBic);
            bankAccount.setCurrency(counterPartyCurrency);
		}
		// CounterParties counterParties = CounterParties.getInstance();
		// counterParties.remove(transactionCode);
		counterParty = counterParties.addCounterParty(counterPartyName.toUpperCase().trim(), bankAccount);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("MOVEMENT\r\n");
		builder.append("sequenceNumber=").append(sequenceNumber).append("\r\n");
		if (debit) builder.append("D/C = D\r\n");
		else builder.append("D/C = C\r\n");
		builder.append("amount=").append(amount).append("\r\n");
		builder.append("date=").append(date.get(Calendar.DAY_OF_MONTH)).append("/").append((date.get(Calendar.MONTH) + 1)).append("/").append(date.get(Calendar.YEAR)).append("\r\n");
		builder.append("transactionCode=").append(transactionCode).append("\r\n");
		builder.append("communication=").append(communication).append("\r\n");
		builder.append("statementNr=").append(statementNr).append("\r\n");
		return builder.toString();
	}

	public String getStatementNr() {
		return statementNr;
	}

	public String getSequenceNr() {
		return sequenceNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public boolean isDebit() {
		return debit;
	}

	public Calendar getDate() {
		return date;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public CounterParty getCounterParty() {
		return counterParty;
	}

	public String getCommunication() {
        return communication;
	}

	public void setCounterParty(CounterParty counterParty) {
		this.counterParty = counterParty;
	}

	public void setTmpCounterParty(TmpCounterParty counterParty) {
		this.tmpCounterParty = counterParty;
	}

	public TmpCounterParty getTmpCounterParty() {
		return tmpCounterParty;
	}

//    public String getName() {
//        return name;
//    }
//
//    public File getXmlFile() {
//        return xmlFile;
//    }
//
//    public File getDtdFile() {
//        return dtdFile;
//    }
//
//    public File getXsl2XmlFile() {
//        return xsl2XmlFile;
//    }
//
//    public File getXsl2HtmlFile() {
//        return xsl2HtmlFile;
//    }
//
//    protected void setDefaultFiles(File subFolder, File xslFolder, File dtdFolder) {
//        xmlFile = FileSystemView.getFileSystemView().getChild(subFolder, name + ".xml");
//        dtdFile = FileSystemView.getFileSystemView().getChild(dtdFolder, "Movement.dtd");
//        xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Movement2xml.xsl");
//        xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Movement2html.xsl");
//    }
}