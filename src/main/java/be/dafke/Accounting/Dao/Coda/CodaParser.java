/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.dafke.Accounting.Dao.Coda;

import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Coda.CounterParties;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.Accounting.Objects.Coda.Movement;
import be.dafke.Accounting.Objects.Coda.Movements;
import be.dafke.Accounting.Objects.Coda.Parsing.Header;
import be.dafke.Accounting.Objects.Coda.Parsing.Information;
import be.dafke.Accounting.Objects.Coda.Parsing.NewBalance;
import be.dafke.Accounting.Objects.Coda.Parsing.OldBalance;
import be.dafke.Accounting.Objects.Coda.Parsing.Statement;
import be.dafke.Accounting.Objects.Coda.Parsing.Trailer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * @author David C.A. Danneels
 */
public class CodaParser {
	private final HashMap<Integer, String> banks;

	public CodaParser() {
		banks = new HashMap<Integer, String>();
		banks.put(Integer.valueOf(750), "AXA Bank");
	}

	public void parseFile(File[] files, Accountings accountings) {
		CounterParties counterParties = accountings.getCurrentAccounting().getCounterParties();
		Movements movements = accountings.getCurrentAccounting().getMovements();
		Movement movement = null;
		Information information = null;
		for(File file : files) {
			Statement statement = new Statement();
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				while (line != null) {
					if (line.startsWith("0")) {
						Header header = Header.parse(line);
						statement.setHeader(header);
					} else if (line.startsWith("1")) {
						OldBalance oldBalance = OldBalance.parse(line);
						statement.setOldBalance(oldBalance);
					} else if (line.startsWith("21")) {
						/*
						 * if(movement!=null){ statement.addMovement(movement); Movements.add(movement); }
						 */movement = Movement.parse(line, counterParties);
						statement.addMovement(movement);
						movements.add(movement);
					} else if (line.startsWith("22")) {
						movement.addPart2(line);
					} else if (line.startsWith("23")) {
						movement.addPart3(line);
					} else if (line.startsWith("31")) {
						information = Information.parse(line);
						movement.setInformation(information);
					} else if (line.startsWith("32")) {
						information.addPart2(line);
					} else if (line.startsWith("33")) {
						information.addPart3(line);
					} else if (line.startsWith("8")) {
						NewBalance newBalance = NewBalance.parse(line);
						statement.setNewBalance(newBalance);
					} else if (line.startsWith("4")) {
						// Free communication
					} else if (line.startsWith("9")) {
						Trailer trailer = Trailer.parse(line);
						statement.setTrailer(trailer);
					}
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException io) {

			}
			// Statements.add(statement);
			System.out.println(statement);
		}
		ArrayList<CounterParty> toRemove = new ArrayList<CounterParty>();
		for(CounterParty party : counterParties.getCounterParties()) {
			if (movements.getMovements(party).isEmpty()) {
				toRemove.add(party);
			}
		}
//		for(CounterParty party : toRemove) {
//			System.err.println("remove " + party.getName());
//			counterParties.remove(party.getName());
//		}
	}

	public static Calendar convertDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		Calendar cal = Calendar.getInstance();
		try {
			Date datum = sdf.parse(date);
			cal.setTime(datum);
		} catch (ParseException p) {
		}
		return cal;
	}

	public static BigDecimal convertBigDecimal(String amountString) {
		BigInteger amountNoDec = new BigInteger(amountString);
		BigDecimal amount = new BigDecimal(amountNoDec, 3);
		amount = amount.setScale(2);
		return amount;
	}
}