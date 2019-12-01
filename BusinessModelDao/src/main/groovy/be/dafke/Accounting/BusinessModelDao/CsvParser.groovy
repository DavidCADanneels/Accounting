package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.BankAccount
import be.dafke.Accounting.BusinessModel.CounterParties
import be.dafke.Accounting.BusinessModel.CounterParty
import be.dafke.Accounting.BusinessModel.Statement
import be.dafke.Accounting.BusinessModel.Statements
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils

class CsvParser {
    private HashMap<String,String> transactionCodes

    CsvParser(){
        transactionCodes = new HashMap<String, String>()
        transactionCodes.put("Europese overschrijving","101")
        transactionCodes.put("Storting vanwege","150")
        transactionCodes.put("Aankoop met AXA bankkaart","402")
        transactionCodes.put("Aankp buitenl.mt AXA bnkkrt","402")
        transactionCodes.put("Geldopname met AXA bankkaart","404")
        transactionCodes.put("Kapitalisatie","501")
        transactionCodes.put("Domiciliëring","501")
        transactionCodes.put("Geldafh. via SelfService", "404")
        transactionCodes.put("Gldopn.buitenl.met bankkaart","404")
    }

    void parseFile(File[] files, CounterParties counterParties, Statements statements) {
        int counter = 0
        for(File file : files) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file))
                String line = reader.readLine()
                System.out.println(line)
                line = reader.readLine()
                while (line != null) {
                    counter++
                    String sequenceNumber = Integer.toString(counter)
                    if(counter<10){
                        sequenceNumber = "000"+sequenceNumber
                    } else if(counter<100){
                        sequenceNumber = "00"+sequenceNumber
                    } else if(counter<1000){
                        sequenceNumber = "0"+sequenceNumber
                    }
                    String[] parts = line.split("")
                    Statement statement = new Statement()
                    statement.setDate(Utils.toCalendar(parts[0]))
                    statement.setName("CSV-"+sequenceNumber)
                    BigDecimal amount = Utils.parseBigDecimal(parts[4].replace(".","").replace(',','.'))
                    boolean debit = false
                    if(amount.compareTo(BigDecimal.ZERO)<0){
                        amount = amount.negate()
                        debit = true
                    }
                    statement.setDebit(debit)
                    statement.setAmount(amount)
                    String codeWoord = parts[6]
                    System.out.println(codeWoord)
                    String shortWoord = codeWoord.replaceAll("\"", "")
                    String code = transactionCodes.get(shortWoord)
                    System.out.println(shortWoord)
                    statement.setTransactionCode(code)
                    System.out.println(code)
                    statement.setTransactionCode(transactionCodes.get(parts[6].replaceAll("\"", "")))
                    statement.setCommunication(parts[14].replaceAll("\"","").trim()+parts[15].replaceAll("\"","").trim())
                    try {
                        statements.addBusinessObject(statement)
                    } catch (EmptyNameException e) {
                        e.printStackTrace()  //To change body of catch statement use File | Settings | File Templates.
                    } catch (DuplicateNameException e) {
                        e.printStackTrace()  //To change body of catch statement use File | Settings | File Templates.
                    }

                    String accountNumber = parts[7].replaceAll("\"","").trim()
                    if(!"".equals(accountNumber)){
                        BankAccount bankAccount = new BankAccount(accountNumber)
                        bankAccount.setBic(parts[8].replaceAll("\"", "").trim())
                        CounterParty counterParty = new CounterParty()
                        counterParty.addAccount(bankAccount)
                        counterParty.setName(parts[9].replaceAll("\"","").trim())
                        String line1 = parts[10].replaceAll("\"","").trim()+" "+parts[11].replaceAll("\"","").trim()
                        String line2 = parts[12].replaceAll("\"","").trim()+" "+parts[13].replaceAll("\"","").trim()
                        ArrayList<String> addressLines = new ArrayList<String>()
                        addressLines.add(line1.trim())
                        addressLines.add(line2.trim())
                        counterParty.setAddressLines(addressLines)
                        statement.setCounterParty(counterParty)
                        try {
                            counterParties.addBusinessObject(counterParty)
                        } catch (EmptyNameException e) {
                            e.printStackTrace()  //To change body of catch statement use File | Settings | File Templates.
                        } catch (DuplicateNameException e) {
                            e.printStackTrace()  //To change body of catch statement use File | Settings | File Templates.
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
}
