package model;

import controller.Controller;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class FileOperations {
    public static  ArrayList<InvoiceHeader> readInvoiceHeaderFile(String invoiceHeaderFilename) {
        BufferedReader csvReader = null;
        ArrayList<InvoiceHeader> invoiceHeaders = new ArrayList<InvoiceHeader>();
        try {
            csvReader = new BufferedReader(new FileReader(invoiceHeaderFilename));
            String row="";
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                int invoiceNum= Integer.parseInt(data[0]);
                String invoiceDate= data[1];
                String customerName= data[2];
                ArrayList<InvoiceLine> dataInvoiceLines = new ArrayList<InvoiceLine>();
                InvoiceHeader readInvoiceHeader = new InvoiceHeader(invoiceNum,invoiceDate,data[2],dataInvoiceLines);
                invoiceHeaders.add(readInvoiceHeader);
            }
            csvReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return invoiceHeaders;
    }

    public static  void readInvoiceHeaderFileFromStringtoFile(String invoiceData,String invoiceHeaderFilename,String invoiceLineFilename, String currentInvoiceLineFilename) {
        BufferedReader csvReader = null;
        ArrayList<InvoiceHeader> invoiceHeaders = new ArrayList<InvoiceHeader>();
        String[] rows = invoiceData.split("\n");
        for(int i=0; i<rows.length;i++) {
            String row = rows[i];
            String[] data = row.split(",");
            int invoiceNum = Integer.parseInt(data[0]);
            String invoiceDate = data[1];
            String customerName = data[2];
            ArrayList<InvoiceLine> dataInvoiceLines = new ArrayList<InvoiceLine>();
            InvoiceHeader readInvoiceHeader = new InvoiceHeader(invoiceNum, invoiceDate, data[2], dataInvoiceLines);
            invoiceHeaders.add(readInvoiceHeader);
        }
        try {
            //File invoiceHeaderFile = new File(invoiceHeaderFilename);
            //invoiceHeaderFile.createNewFile();
            //File invoiceLineFile = new File(invoiceLineFilename);
            //invoiceLineFile.createNewFile();

            FileWriter invoiceHeaderWriter = new FileWriter(invoiceHeaderFilename);
            BufferedWriter invoiceHeaderBuffered= new BufferedWriter(invoiceHeaderWriter);
            for (InvoiceHeader ih: invoiceHeaders){
                int invoiceNum= ih.getInvoiceNum();
                invoiceHeaderBuffered.write(""+invoiceNum);
                invoiceHeaderBuffered.write(",");
                invoiceHeaderBuffered.write(""+ih.getInvoiceDate());
                invoiceHeaderBuffered.write(",");
                invoiceHeaderBuffered.write(""+ih.getCustomerName());
                invoiceHeaderBuffered.write("\n");
                invoiceHeaderBuffered.flush();
            }

            invoiceHeaderWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Files.copy(currentInvoiceLineFilename, invoiceLineFilename);
        copyFileUsingChannel(new File(currentInvoiceLineFilename), new File(invoiceLineFilename));
    }

    private static void copyFileUsingChannel(File source, File dest) {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            sourceChannel.close();
            destChannel.close();
        }catch(Exception e){

        }
    }

    public static ArrayList<InvoiceLine> readInvoiceLineFile(String invoiceLineFilename, ArrayList<InvoiceHeader> invoiceHeaders) {
        BufferedReader csvReader = null;
        ArrayList<InvoiceLine> invoiceLines= new ArrayList<InvoiceLine>();
        try {
            csvReader = new BufferedReader(new FileReader(invoiceLineFilename));
            String row="";
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                int invoiceNumber= Integer.parseInt(data[0]);
                String itemName= data[1];
                Double itemPrice = Double.parseDouble(data[2]);
                int count = Integer.parseInt(data[3]);
                InvoiceLine readInvoiceLine = new InvoiceLine(itemName,itemPrice,count);
                FileOperations.addInvoiceLinetoInvoiceHeader(invoiceNumber,readInvoiceLine, invoiceHeaders);
                invoiceLines.add(readInvoiceLine);
            }
            csvReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return invoiceLines;
    }

    private static void addInvoiceLinetoInvoiceHeader( int invoiceNumber, InvoiceLine invoiceLine, ArrayList<InvoiceHeader> invoiceHeaders){
        for(InvoiceHeader ih: invoiceHeaders){
            if( ih.getInvoiceNum()==invoiceNumber){
                ih.addInvoiceLine(invoiceLine);
                break;
            }
        }

    }

    public static void writeInvoiceDate(String invoiceHeaderFilename,String invoiceLineFilename, ArrayList<InvoiceHeader> invoiceHeaders){

        try {
            //File invoiceHeaderFile = new File(invoiceHeaderFilename);
            //invoiceHeaderFile.createNewFile();
            //File invoiceLineFile = new File(invoiceLineFilename);
            //invoiceLineFile.createNewFile();

            FileWriter invoiceHeaderWriter = new FileWriter(invoiceHeaderFilename);
            BufferedWriter invoiceHeaderBuffered= new BufferedWriter(invoiceHeaderWriter);
            FileWriter invoiceLineWriter = new FileWriter(invoiceLineFilename);
            BufferedWriter invoiceLineBuffered= new BufferedWriter(invoiceLineWriter);
            for (InvoiceHeader ih: invoiceHeaders){
                int invoiceNum= ih.getInvoiceNum();
                invoiceHeaderBuffered.write(""+invoiceNum);
                invoiceHeaderBuffered.write(",");
                invoiceHeaderBuffered.write(""+ih.getInvoiceDate());
                invoiceHeaderBuffered.write(",");
                invoiceHeaderBuffered.write(""+ih.getCustomerName());
                invoiceHeaderBuffered.write("\n");
                invoiceHeaderBuffered.flush();
                for(InvoiceLine il : ih.getInvoiceLines()){
                    invoiceLineBuffered.append(""+ih.getInvoiceNum());
                    invoiceLineBuffered.append(",");
                    invoiceLineBuffered.append(""+il.getItemName());
                    invoiceLineBuffered.append(",");
                    invoiceLineBuffered.append(""+il.getItemPrice());
                    invoiceLineBuffered.append(",");
                    invoiceLineBuffered.append(""+il.getCount());
                    invoiceLineBuffered.append("\n");
                    invoiceLineBuffered.flush();
                }
            }

            invoiceHeaderWriter.close();
            invoiceLineWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static int getNumberofFileColumns(String filename) {
        BufferedReader csvReader = null;
        int columnSize=-3;
        try {
            csvReader = new BufferedReader(new FileReader(filename));
            String row = "";
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                if (columnSize==-3){ //first row column count
                    columnSize=data.length;
                }else{
                    if(columnSize!=data.length){
                        return -2;
                    }
                }
            }

        } catch (Exception e) {
            return -4;
        }
        return columnSize;
    }

    public static String checkInvoiceHeaderFileFormat(String filename){
        Controller controller = Controller.getInstance();
        BufferedReader csvReader = null;
        String validation="";
        try {
            csvReader = new BufferedReader(new FileReader(filename));
            String row = "";
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                validation+= controller.validateIntegerFormat(data[0]);
                validation+="\n";
                validation+= controller.validateDateFormat(data[1]);
                validation+="\n";
                validation+= controller.validateNameFormat(data[2]);
                validation+="\n";
            }
            return validation.trim();
        }
        catch(Exception e){
            return "Cannot open file...";
        }
    }

    public static String checkInvoiceLineFileFormat(String filename){
        Controller controller = Controller.getInstance();
        BufferedReader csvReader = null;
        String validation="";
        try {
            csvReader = new BufferedReader(new FileReader(filename));
            String row = "";
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                validation+= controller.validateIntegerFormat(data[0]);
                validation+="\n";
                validation+= controller.validateNameFormat(data[1]);
                validation+="\n";
                validation+= controller.validateDoubleFormat(data[2]);
                validation+="\n";
                validation+= controller.validateIntegerFormat(data[3]);
                validation+="\n";
            }
            return validation.trim();
        }
        catch(Exception e){
            return "Cannot open file...";
        }
    }
}


