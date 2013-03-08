/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.dafke.Accounting.Dao.Coda;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.BankAccount;
import be.dafke.Accounting.Objects.Accounting.CounterParties;
import be.dafke.Accounting.Objects.Accounting.CounterParty;
import be.dafke.Accounting.Objects.Accounting.Movement;
import be.dafke.Accounting.Objects.Accounting.Movements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author David C.A. Danneels
 */
public class CodaParser {

    private CounterParties counterParties;
    private String counterPartyBic;

	public void parseFile(File[] files, Accounting accounting) {
		counterParties = accounting.getCounterParties();
		Movements movements = accounting.getMovements();
		Movement movement = null;
		for(File file : files) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				while (line != null) {
					if (line.startsWith("21")) {
                        movement = parse(line);
                        try {
                            movements.addBusinessObject(movement);
                        } catch (EmptyNameException e) {
                            System.err.println("Movement name is empty.");
                        } catch (DuplicateNameException e) {
                            System.err.println("Movement name already exist.");
                        }
                    } else if (line.startsWith("22")) {
                        if(movement!=null){
						    addPart2(movement, line);
                        } else {
                            System.err.println("Corrupt CODA file: " + file.getName());
                        }
					} else if (line.startsWith("23")) {
                        if(movement!=null){
						    addPart3(movement, line);
                        } else {
                            System.err.println("Corrupt CODA file: " + file.getName());
                        }
					}
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException io) {
                io.printStackTrace();
			}
		}
	}

    public void addPart2(Movement movement, String line) {
        String communication2 = line.substring(10, 63).trim();
        counterPartyBic = line.substring(98, 109).trim();

        // Reset communicatie
        String communication = movement.getCommunication();
        String transactionCode = movement.getTransactionCode();
        if (!movement.isStructured() && !transactionCode.equals("402") && !transactionCode.equals("404") && communication2 != null && !communication2.trim().isEmpty()) {
            // momenteel enkel indien transactionCode = 150 (CM)
            movement.setCommunication(communication.trim() + communication2.trim());
        }
    }

    public void addPart3(Movement movement, String line) {
        String counterPartyAccount = line.substring(10, 44).trim();
        CounterParty counterParty = new CounterParty();
        if (!counterPartyAccount.trim().equals("")) {
            BankAccount bankAccount = new BankAccount(counterPartyAccount);
            bankAccount.setBic(counterPartyBic);
            String counterPartyCurrency = line.substring(44, 47).trim();
            bankAccount.setCurrency(counterPartyCurrency);
            counterParty.addAccount(bankAccount);
        }
        String counterPartyName = line.substring(47, 82).trim();
        counterParty.setName(counterPartyName.toUpperCase().trim());
        movement.setCounterParty(counterParty);
        try {
            counterParties.addBusinessObject(counterParty);
        } catch (EmptyNameException e) {
            System.err.println("The Name of the CounterParty cannot be empty");
        } catch (DuplicateNameException e) {
            System.err.println("The Name of the CounterParty already exists");
        }
    }


    public static Calendar convertDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		Calendar cal = Calendar.getInstance();
		try {
			Date datum = sdf.parse(date);
			cal.setTime(datum);
		} catch (ParseException p) {
            p.printStackTrace();
		}
		return cal;
	}

	public static BigDecimal convertBigDecimal(String amountString) {
		BigInteger amountNoDec = new BigInteger(amountString);
		BigDecimal amount = new BigDecimal(amountNoDec, 3);
		amount = amount.setScale(2);
		return amount;
	}

    public Movement parse(String line) {
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
//		while (statementNumber.startsWith("0")) {
//			statementNumber = statementNumber.substring(1);
//		}
//		while (sequenceNumber.startsWith("0")) {
//			sequenceNumber = sequenceNumber.substring(1);
//		}
        Movement movement = new Movement();
        movement.setName(statementNumber+"-"+sequenceNumber);
        movement.setStatementNr(statementNumber);
        movement.setSequenceNumber(sequenceNumber);
        movement.setDate(date);
        movement.setDebit(debit);
        movement.setAmount(amount);
        movement.setTransactionCode(transactionCode);
        movement.setCommunication(communication);
        movement.setStructured(structured);
        resetCommunication(movement, structured);
        return movement;
    }

    private void resetCommunication(Movement movement, boolean structured){
        String transactionCode = movement.getTransactionCode();
        String communication = movement.getCommunication();
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
        movement.setCommunication(communication);
    }
}