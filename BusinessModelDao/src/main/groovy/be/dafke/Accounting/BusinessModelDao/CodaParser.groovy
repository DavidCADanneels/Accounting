package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.BankAccount
import be.dafke.Accounting.BusinessModel.CounterParties
import be.dafke.Accounting.BusinessModel.CounterParty
import be.dafke.Accounting.BusinessModel.Statement
import be.dafke.Accounting.BusinessModel.Statements
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

import java.text.ParseException
import java.text.SimpleDateFormat

class CodaParser {

    CounterParties counterParties
    Statements statements
    String counterPartyBic

    void parseFile(File[] files, CounterParties counterParties, Statements statements) {
        this.counterParties = counterParties
        this.statements = statements
        Statement statement = null
        for(File file : files) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file))
                String line = reader.readLine()
                while (line != null) {
                    if (line.startsWith("21")) {
                        statement = parse(line)
                        try {
                            statements.addBusinessObject(statement)
                        } catch (EmptyNameException e) {
                            System.err.println("Statement name is empty.")
                        } catch (DuplicateNameException e) {
                            System.err.println("Statement name already exist.")
                        }
                    } else if (line.startsWith("22")) {
                        if(statement){
                            addPart2(statement, line)
                        } else {
                            System.err.println("Corrupt CODA file: " + file.name)
                        }
                    } else if (line.startsWith("23")) {
                        if(statement){
                            addPart3(statement, line)
                        } else {
                            System.err.println("Corrupt CODA file: " + file.name)
                        }
                    }
                    line = reader.readLine()
                }
                reader.close()
            } catch (IOException io) {
                io.printStackTrace()
            }
        }
    }

    void addPart2(Statement statement, String line) {
        String communication2 = line.substring(10, 63).trim()
        counterPartyBic = line.substring(98, 109).trim()

        // Reset communicatie
        String communication = statement.getCommunication()
        String transactionCode = statement.getTransactionCode()
        if (!statement.isStructured() && !transactionCode.equals("402") && !transactionCode.equals("404") && communication2 != null && !communication2.trim().empty) {
            // momenteel enkel indien transactionCode = 150 (CM)
            statement.setCommunication(communication.trim() + communication2.trim())
        }
    }

    void addPart3(Statement statement, String line) {
        String counterPartyAccount = line.substring(10, 44).trim()
        CounterParty counterParty = new CounterParty()
        if (!counterPartyAccount.trim().equals("")) {
            BankAccount bankAccount = new BankAccount(counterPartyAccount)
            bankAccount.setBic(counterPartyBic)
            String counterPartyCurrency = line.substring(44, 47).trim()
            bankAccount.setCurrency(counterPartyCurrency)
            counterParty.addAccount(bankAccount)
        }
        String counterPartyName = line.substring(47, 82).trim()
        counterParty.setName(counterPartyName.toUpperCase().trim())
        statement.setCounterParty(counterParty)
        try {
            counterParties.addBusinessObject(counterParty)
        } catch (EmptyNameException e) {
            System.err.println("The Name of the CounterParty cannot be empty")
        } catch (DuplicateNameException e) {
            System.err.println("The Name of the CounterParty already exists")
        }
    }


    static Calendar convertDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy")
        Calendar cal = Calendar.getInstance()
        try {
            Date datum = sdf.parse(date)
            cal.setTime(datum)
        } catch (ParseException p) {
            p.printStackTrace()
        }
        cal
    }

    static BigDecimal convertBigDecimal(String amountString) {
        BigInteger amountNoDec = new BigInteger(amountString)
        BigDecimal amount = new BigDecimal(amountNoDec, 3)
        amount = amount.setScale(2)
        amount
    }

    Statement parse(String line) {
        String sequenceNumber = line.substring(2, 6).trim()
        String sign = line.substring(31, 32).trim()
        boolean debit = "1".equals(sign)
        String amountString = line.substring(32, 47).trim()
        BigDecimal amount = CodaParser.convertBigDecimal(amountString)
        String dateString = line.substring(47, 53).trim()
        Calendar date = CodaParser.convertDate(dateString)
//		String transactionCode = line.substring(53, 61).trim
        String transactionCode = line.substring(55, 58).trim()
        String struc = line.substring(61, 62).trim()
        boolean structured = "1".equals(struc)
        String communication = line.substring(62, 115).trim()
        String statementNumber = line.substring(121, 124).trim()
        // trim numbers
//		while (statementNumber.startsWith("0")) {
//			statementNumber = statementNumber.substring(1)
//		}
//		while (sequenceNumber.startsWith("0")) {
//			sequenceNumber = sequenceNumber.substring(1)
//		}
        Statement statement = new Statement()
        statement.setName(statementNumber+"-"+sequenceNumber)
        statement.setDate(date)
        statement.setDebit(debit)
        statement.setAmount(amount)
        statement.setTransactionCode(transactionCode)
        statement.setCommunication(communication)
        statement.setStructured(structured)
        resetCommunication(statement, structured)
        statement
    }

    void resetCommunication(Statement statement, boolean structured){
        String transactionCode = statement.getTransactionCode()
        String communication = statement.getCommunication()
        if (transactionCode.equals("402") || transactionCode.equals("404")) {
            communication = communication.replaceAll("[0-9]", "")
            communication = communication.trim()
        }else if (structured) {
            String start = communication.substring(0, 3)
            communication = communication.substring(3)
            if (start.equals("101") && communication.length() >= 12) {
                // +++123/1234/12345+++
                String part1 = communication.substring(0, 3)
                String part2 = communication.substring(3, 7)
                String part3 = communication.substring(7, 12)
                communication = "+++" + part1 + "/" + part2 + "/" + part3 + "+++"
            } else if (start.equals("107")) {
                communication = communication.substring(18)
                while (communication.startsWith("0")) {
                    communication = communication.substring(1)
                }
                communication = communication.trim()
            }
            if(transactionCode.equals("501")){
                if(communication.length()>14){
                    communication = communication.substring(0,14)
                }
                communication = communication.trim()
            }
        }
        statement.setCommunication(communication)
    }
}