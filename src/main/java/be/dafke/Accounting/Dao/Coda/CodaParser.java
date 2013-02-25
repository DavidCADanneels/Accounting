/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.dafke.Accounting.Dao.Coda;

import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Coda.CounterParties;
import be.dafke.Accounting.Objects.Coda.Movement;
import be.dafke.Accounting.Objects.Coda.Movements;

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
	public void parseFile(File[] files, Accounting accounting) {
		CounterParties counterParties = accounting.getCounterParties();
		Movements movements = accounting.getMovements();
		Movement movement = null;
		for(File file : files) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				while (line != null) {
					if (line.startsWith("21")) {
						movement = Movement.parse(line, counterParties);
						movements.add(movement);
					} else if (line.startsWith("22")) {
						movement.addPart2(line);
					} else if (line.startsWith("23")) {
						movement.addPart3(line);
					}
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException io) {

			}
		}
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