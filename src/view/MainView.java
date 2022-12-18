package view;

import controller.Controller;
import model.InvoiceHeader;
import model.InvoiceLine;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class MainView extends JFrame {

    //Menu Bar Components
    private JMenuBar mainMenuBar;
    private JMenu fileMenu;
    private JMenuItem loadFileMenuItem;
    private JMenuItem saveFileMenuItem;
    private JPanel leftPanel;
    private JLabel invoicesTableLabel;

    //Left Panel Components (Invoice Header Components)
    private JPanel invoiceTableFramePanel;
    private DefaultTableModel invoiceTableModel;
    private JTable invoicesTable;
    private String[] invoiceTableFields ={"No.","Date","Customer","Total"};
    private Vector<String> invoiceItemTableFields;
    //Just Dummy Data
    private String [][] invoiceTableData={{"1","12-1-2020","Ahmed Moaz","30"},{"4","1-6-2021","Ali Moaz","50"},{"7","9-7-2021","Layla Ezz","606"}};

    private JPanel invoicesFunctionsPanel;
    private JButton createNewInvoiceButton;
    private JButton deleteInvoiceButton;


    //Right Panel Components (Invoice Lines Components)
    private int rightPannelStatus=0;
    //0 means no row is selected and no new invoice is being created the whole panel is disabled
    //1 means empty panel to create new Invoice with new unique Invoice Number
    //2 a row is selected and you can update in it
    private JPanel rightPanel;
    private JLabel illustrationLabel;
    private JPanel invoiceNumberPanel;
    private JLabel invoiceNumberLabel;
    private JLabel invoiceNumberValue;

    private JPanel invoiceDatePanel;
    private JLabel invoiceDateLabel;
    private JTextField invoiceDateTextField;

    private JPanel invoiceCustomerPanel;
    private JLabel invoiceCustomerNameLabel;
    private JTextField invoiceCustomerNameTextField;

    private JPanel invoiceTotalPanel;
    private JLabel invoiceTotalLabel;
    private JLabel invoiceTotalValue;

    private DefaultTableModel invoiceItemsTableModel;
    private JTable invoiceItemsTable;
    private String [] invoiceItemsTableFields={"No.","Item Name","Item Price","Count","Item Total"};
    private ArrayList<ArrayList<String>> invoiceItemsTableData;
    //Dummy Data
    //private String[][]={""}

    private JLabel addDeleteIllustrationLabel;
    private JPanel invoiceItemsAddDeleteFunctionsPanel;
    private JButton addInvoiceItemButton;
    private JButton deleteInvoiceItemButton;
    private JPanel invoiceItemsSaveCancelFunctionsPanel;
    private JButton saveInvoiceChangesButton;
    private JButton cancelInvoiceChangesButton;



    public MainView(String title){
        //Main Frame initial parameters
        super(title);
        this.setSize(700,600);
        this.setLocation(100,100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(1,2,10,10));
        //Constructing left and right panels
        this.leftPanel =new JPanel();
        this.rightPanel =new JPanel();
        this.add(leftPanel);
        this.add(rightPanel);
        //Adding Menu Bar
        this.addMenuBar();
        //Adding Left and Right panels components
        this.addInvoicesTable();
        this.addRightPanel();
    }

    /**
     * Adds the Menu bar with File Menu
     * Containing both Load File and Save File options
     */
    private void addMenuBar(){
        this.mainMenuBar= new JMenuBar();
        this.fileMenu= new JMenu("File");

        this.loadFileMenuItem= new JMenuItem("Load File", 'l');
        this.loadFileMenuItem.addActionListener(new loadFileActionListener());
        this.loadFileMenuItem.setAccelerator(KeyStroke.getKeyStroke('L', KeyEvent.CTRL_DOWN_MASK));

        this.saveFileMenuItem= new JMenuItem("Save File",'s');
        this.saveFileMenuItem.addActionListener(new saveFileActionListener());
        this.saveFileMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));

        this.fileMenu.add(this.loadFileMenuItem);
        this.fileMenu.add(this.saveFileMenuItem);
        this.mainMenuBar.add(this.fileMenu);
        this.setJMenuBar(this.mainMenuBar);


    }

    /**
     * load File Action Listener
     * It shows File Loading Tip then call a function
     * to do the actual files loading
     */
    class loadFileActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JDialog d = new JDialog(MainView.this,"File Loading Tip",true);
            JLabel l = new JLabel("Please Select 2 Files: \n an invoice header file and invoice line file");
            d.add(l);
            JPanel actionsPanel= new JPanel();
            actionsPanel.setLayout(new BoxLayout(actionsPanel,BoxLayout.X_AXIS));
            JButton ok= new JButton("continue");
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    d.dispose();
                    MainView.this.loadFile();
                }
            });
            JButton cancel= new JButton("cancel");
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    d.dispose();
                }
            });
            actionsPanel.add(ok);
            actionsPanel.add(cancel);
            d.add(actionsPanel);
            d.setSize(450,100);
            d.setLayout(new FlowLayout());
            d.setLocation(200,200);
            d.setVisible(true);
        }
    }

    /**
     * This function performs some checks on the file format and then loads the files
     * Checks for
     * 1.file extension
     * 2.Number of files selected (only 2 files should be used)
     * 3.Format of each field in each file
     */
    private void loadFile(){
        JFileChooser invoiceFileChooser = new JFileChooser();
        invoiceFileChooser.setMultiSelectionEnabled(true);
        File[] files = invoiceFileChooser.getSelectedFiles();
        int dialogReturnValue = invoiceFileChooser.showOpenDialog(loadFileMenuItem);
        String invoiceHeaderFilename="";
        String invoiceLinesFilename="";
        String invoiceHeaderFileDirectory="";
        if (dialogReturnValue == JFileChooser.APPROVE_OPTION) {
            if(invoiceFileChooser.getSelectedFiles().length!=2){
                JDialog d = new JDialog(MainView.this,"File Loading Error",true);
                JLabel l = new JLabel("Please Select Exactly 2 Files: \n an invoice header file and invoice line file");
                d.add(l);
                JButton ok= new JButton("Ok");
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        d.dispose();
                    }
                });
                d.add(ok);
                d.setSize(450,100);
                d.setLayout(new FlowLayout());
                d.setLocation(200,200);
                d.setVisible(true);
            }else {

                invoiceHeaderFilename = invoiceFileChooser.getSelectedFiles()[0].getName();
                invoiceLinesFilename = invoiceFileChooser.getSelectedFiles()[1].getName();
                boolean fileExtensionCheck = (invoiceHeaderFilename.endsWith(".csv")) && (invoiceLinesFilename.endsWith(".csv"));
                String check="";
                if(!fileExtensionCheck){
                    check+="Invalid File extensions";
                }
                else {
                    invoiceHeaderFileDirectory = invoiceFileChooser.getCurrentDirectory().toString();
                    invoiceHeaderFilename = invoiceHeaderFileDirectory + "/" + invoiceHeaderFilename;
                    invoiceLinesFilename = invoiceHeaderFileDirectory + "/" + invoiceLinesFilename;
                    Controller controller = Controller.getInstance();
                    check += controller.loadInvoiceData(invoiceHeaderFilename, invoiceLinesFilename);
                }
                if(check.equals("")) {
                    MainView.this.refreshInvoiceTable();
                }
                else {
                    JDialog d = new JDialog(MainView.this,"File Loading Error",true);
                    JLabel l = new JLabel(check);
                    d.setLayout(new FlowLayout());
                    d.add(l);
                    JButton ok= new JButton("Ok");
                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            d.dispose();
                        }
                    });
                    d.add(ok);
                    d.setSize(450,100);
                    d.setLocation(200,200);
                    d.setVisible(true);
                }
            }
        }
        if (dialogReturnValue == JFileChooser.CANCEL_OPTION) {
            invoiceHeaderFilename="You pressed cancel";
            invoiceHeaderFileDirectory="";
        }
    }

    /**
     * Refreshes the Invoice Table and makes sure that its values are updated
     * This function is called when a new file is loaded or when an invoice is deleted, created or updated
     */
    private void refreshInvoiceTable(){
        Controller controller = Controller.getInstance();
        ArrayList<InvoiceHeader> invoiceHeaders=controller.getInvoiceHeaders();
        Vector<String> invoiceTableFields=new Vector<String>();
        invoiceTableFields.add("No.");
        invoiceTableFields.add("Date");
        invoiceTableFields.add("Customer");
        invoiceTableFields.add("Total");
        Vector<Vector<String>> invoiceData= new Vector<Vector<String>>();
        this.invoiceTableModel= new DefaultTableModel(invoiceTableFields,0);
        for(InvoiceHeader ih : invoiceHeaders){
            Vector<String> invoice= new Vector<String>();
            invoice.add(""+ih.getInvoiceNum());
            invoice.add(""+ih.getInvoiceDate());
            invoice.add(""+ih.getCustomerName());
            invoice.add(""+ih.getTotalInvoicePrice());
            invoiceData.add(invoice);
            MainView.this.invoiceTableModel.addRow(invoice);
        }
        this.invoicesTable.setModel(MainView.this.invoiceTableModel);
    }

    /**
     * Save file action Listener
     * Create 2 files with random names starting with "invoiceheader" and "invoiceline"
     * It allows the user to choose the destination
     */
    class saveFileActionListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser saveInvoiceFileChooser = new JFileChooser();
                saveInvoiceFileChooser.isDirectorySelectionEnabled();
                // Demonstrate "Open" dialog:
                String invoiceHeaderFilename="";
                String invoiceLinesFilename="";
                String invoiceHeaderFileDirectory="";
                int returnValue = saveInvoiceFileChooser.showOpenDialog(saveFileMenuItem);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    Random rand = new Random();
                    int fileNamePart= rand.nextInt();
                    invoiceHeaderFilename="InvoiceHeader"+fileNamePart+".csv";
                    invoiceLinesFilename="InvoiceLine"+fileNamePart+".csv";
                    invoiceHeaderFileDirectory=saveInvoiceFileChooser.getCurrentDirectory().toString();
                    invoiceHeaderFilename= invoiceHeaderFileDirectory+"/"+invoiceHeaderFilename;
                    invoiceLinesFilename= invoiceHeaderFileDirectory+"/"+invoiceLinesFilename;
                    String headerData="";
                    TableModel invoicesTableModels= (TableModel) invoicesTable.getModel();
                    for (int count = 0; count < invoicesTableModels.getRowCount(); count++){
                        headerData+=invoicesTableModels.getValueAt(count, 0).toString();
                        headerData+=",";
                        headerData+=invoicesTableModels.getValueAt(count, 1).toString();
                        headerData+=",";
                        headerData+=invoicesTableModels.getValueAt(count, 2).toString();
                        headerData+="\n";
                    }
                    Controller controller= Controller.getInstance();

                    controller.saveInvoiceHeaders(headerData,invoiceHeaderFilename,invoiceLinesFilename);
                }
                if (returnValue == JFileChooser.CANCEL_OPTION) {
                }
            }
        }


    /**
     * This adds the components in the left panel and arranges them
     */

    private void addInvoicesTable(){

        //Constructing and setting the alignment the left panel
        this.leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));
        this.leftPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        //Adds a decorative label at the top of the left panel marking the Invoices Table
        this.invoicesTableLabel=new JLabel("Invoices Table");
        this.invoicesTableLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Construct, updates and arranges the invoice table
        this.invoicesTable= new JTable(invoiceTableData,invoiceTableFields);
        this.invoicesTable.setDefaultEditor(Object.class,null);
        this.refreshInvoiceTable();
        this.invoicesTable.getSelectionModel().addListSelectionListener(new InvoiceTableRowSelectionListener());
        //this.invoiceTableFramePanel= new JPanel();
        //this.invoiceTableFramePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        //this.invoiceTableFramePanel.add(this.invoicesTable);

        //Constructing a panel to arrange Create and Delete buttons
        this.invoicesFunctionsPanel= new JPanel();
        this.invoicesFunctionsPanel.setLayout(new BoxLayout(this.invoicesFunctionsPanel,BoxLayout.X_AXIS));
        this.createNewInvoiceButton= new JButton("Create New Invoice");
        this.createNewInvoiceButton.addActionListener(new CreateNewInvoiceActionListener());
        this.deleteInvoiceButton= new JButton("Delete Invoice");
        this.deleteInvoiceButton.addActionListener(new DeleteInvoiceActionListener());
        this.invoicesFunctionsPanel.add(this.createNewInvoiceButton);
        this.invoicesFunctionsPanel.add(this.deleteInvoiceButton);

        //Adding to the left panel the components (label , table, and the function buttons (Create and Delete))
        this.leftPanel.add(this.invoicesTableLabel);
        this.leftPanel.add(new JScrollPane(this.invoicesTable));
        this.leftPanel.add(this.invoicesFunctionsPanel);

    }

    /**
     * Delete Invoice Action Listener
     * Deletes the selected Invoice
     * Notifies the user if no invoice is selected
     * Verifies that the user wants to delete the selected invoice
     * then either cancel deleting or deletes the invoice
     */
    class DeleteInvoiceActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int column = 0;
            int row = invoicesTable.getSelectedRow();
            if(row==-1){
                JDialog d = new JDialog(MainView.this,"No Invoice Selected",true);
                JLabel l = new JLabel("Please Select an Invoice");
                d.add(l);
                JButton b= new JButton("Ok");
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        d.dispose();
                    }
                });
                d.add(b);
                d.setSize(200,100);
                d.setLayout(new FlowLayout());
                d.setLocation(200,200);
                d.setVisible(true);
            }else {
                TableModel invoicesTableModels= (TableModel) invoicesTable.getModel();
                String headerData=" Invoice Number: ";
                headerData+=invoicesTableModels.getValueAt(row, 0).toString();
                headerData+=",\n Date:";
                headerData+=invoicesTableModels.getValueAt(row, 1).toString();
                headerData+=",\n Customer Name: ";
                headerData+=invoicesTableModels.getValueAt(row, 2).toString();

                JDialog d = new JDialog(MainView.this,"Delete Invoice",true);
                JTextArea l = new JTextArea("Are you sure you want to delete invoice: \n"+headerData);
                d.add(l);
                JButton yesDeleteInvoice= new JButton("Yes");
                yesDeleteInvoice.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        d.dispose();
                        int invoiceNumber = Integer.parseInt(invoicesTable.getModel().getValueAt(row, 0).toString());
                        Controller controller= Controller.getInstance();
                        controller.deleteInvoiceHeader(invoiceNumber);
                        ((DefaultTableModel)invoicesTable.getModel()).removeRow(row);
                        MainView.this.clearRightPanel();
                    }
                });
                d.add(yesDeleteInvoice);
                JButton cancelDeleteInvoice= new JButton("Cancel");
                cancelDeleteInvoice.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        d.dispose();
                    }
                });
                d.add(cancelDeleteInvoice);
                d.pack();
                d.setSize(300,200);
                d.setLayout(new FlowLayout());
                d.setLocation(200,200);
                d.setVisible(true);

            }

        }
    }

    /**
     * Invoice Table Row Selection Listener
     * It updates the right panel with the detailed information of the selected invoice
     */
    class InvoiceTableRowSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
                int row = MainView.this.invoicesTable.getSelectedRow();
                if(row!=-1) {
                    TableModel invoicesTableModels = (TableModel) MainView.this.invoicesTable.getModel();
                    String invoiceNumberString = invoicesTableModels.getValueAt(row, 0).toString();
                    MainView.this.invoiceNumberValue.setText(invoiceNumberString);
                    String invoiceDateString = invoicesTableModels.getValueAt(row, 1).toString();
                    MainView.this.invoiceDateTextField.setText(invoiceDateString);
                    String invoiceCustomerName = invoicesTableModels.getValueAt(row, 2).toString();
                    MainView.this.invoiceCustomerNameTextField.setText(invoiceCustomerName);
                    double totalPrice = MainView.this.refreshInvoiceItemsTable(Integer.parseInt(invoiceNumberString));
                    MainView.this.invoiceTotalValue.setText("" + totalPrice);
                    MainView.this.setPanelEnabled(rightPanel, true);
                    MainView.this.illustrationLabel.setText("");
                }

        }
    }

    /**
     * Create a new Invoice Action Listener (Button)
     */
    class CreateNewInvoiceActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Controller controller= Controller.getInstance();
            MainView.this.rightPannelStatus = 1;
            MainView.this.setPanelEnabled(rightPanel,true);
            MainView.this.clearRightPanel();
            MainView.this.illustrationLabel.setText("");
            MainView.this.invoiceNumberValue.setText(""+controller.getNextInvoiceNum());
        }
    }

    /**
     * Updates the invoiceLines Table and returns the total price value
     * to be updated in the interface immediately
     * @param invoiceNum
     * @return the total invoice price to be written in the totalInvoiceValue label
     */

    private double refreshInvoiceItemsTable(int invoiceNum){
        Controller controller = Controller.getInstance();
        InvoiceHeader ih= controller.getInvoice(invoiceNum);
        this.invoiceItemTableFields=new Vector<String>();
        this.invoiceItemTableFields.add("No.");
        this.invoiceItemTableFields.add("Item Name");
        this.invoiceItemTableFields.add("Item Price");
        this.invoiceItemTableFields.add("Count");
        this.invoiceItemTableFields.add("Item Total");
        //Disable Editing the first and last invoice line table columns
        // ( count and total item price are not inputted by the user)
        MainView.this.invoiceItemsTableModel= new DefaultTableModel(invoiceItemTableFields,0){
            @Override
            public boolean isCellEditable(int row, int column){
                if ((column==0)||(column==4))
                    return false;
                return true;
            }
        };
        int i=1;
        //double total=0;
        for(InvoiceLine il : ih.getInvoiceLines()){
            Vector<String> invoiceItems= new Vector<String>();
            invoiceItems.add(""+i);
            invoiceItems.add(""+il.getItemName());
            double price=il.getItemPrice();
            invoiceItems.add(""+il.getItemPrice());
            int count=il.getCount();
            invoiceItems.add(""+count);
            double itemTotalPrice= count*price;
            invoiceItems.add(""+itemTotalPrice);
            MainView.this.invoiceItemsTableModel.addRow(invoiceItems);
            i+=1;
        }
        MainView.this.invoiceItemsTable.setModel(MainView.this.invoiceItemsTableModel);
        return ih.getTotalInvoicePrice();
    }

    /**
     * This function adds and arranges the right panel components
     */
    private void addRightPanel(){

        //this.rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
        this.rightPanel.setLayout(new GridLayout(9,1));

        //Illustrative label
        this.illustrationLabel= new JLabel("Select an invoice or Create a new Invoice");
        this.illustrationLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        //Arranging Invoice number label and value
        this.invoiceNumberPanel= new JPanel();
        this.invoiceNumberPanel.setLayout(new BoxLayout(this.invoiceNumberPanel,BoxLayout.X_AXIS));
        this.invoiceNumberPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.invoiceNumberLabel=new JLabel("Invoice Number:");
        this.invoiceNumberValue= new JLabel("");
        this.invoiceNumberPanel.add(this.invoiceNumberLabel);
        this.invoiceNumberPanel.add(this.invoiceNumberValue);

        //Arranging Invoice Date label and value
        this.invoiceDatePanel= new JPanel();
        this.invoiceDatePanel.setLayout(new BoxLayout(this.invoiceDatePanel,BoxLayout.X_AXIS));
        this.invoiceDatePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.invoiceDateLabel= new JLabel("Date:");
        this.invoiceDateTextField = new JTextField();
        //this.invoiceDateTextField.setSize(10,10);
        this.invoiceDatePanel.add(this.invoiceDateLabel);
        this.invoiceDatePanel.add(this.invoiceDateTextField);

        //Arranging Customer name label and value
        this.invoiceCustomerPanel= new JPanel();
        this.invoiceCustomerPanel.setLayout(new BoxLayout(this.invoiceCustomerPanel,BoxLayout.X_AXIS));
        this.invoiceCustomerPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.invoiceCustomerNameLabel= new JLabel("Customer Name:");
        this.invoiceCustomerNameTextField= new JTextField("");
        //this.invoiceCustomerNameTextField.setSize(10,10);
        this.invoiceCustomerPanel.add(this.invoiceCustomerNameLabel);
        this.invoiceCustomerPanel.add(this.invoiceCustomerNameTextField);

        //Arranging total Invoice price label and value
        this.invoiceTotalPanel= new JPanel();
        this.invoiceTotalPanel.setLayout(new BoxLayout(this.invoiceTotalPanel,BoxLayout.X_AXIS));
        this.invoiceTotalPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.invoiceTotalLabel= new JLabel("Total:");
        this.invoiceTotalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.invoiceTotalValue= new JLabel("0.0");
        this.invoiceTotalPanel.add(this.invoiceTotalLabel);
        this.invoiceTotalPanel.add(this.invoiceTotalValue);

        //Arranging the invoice Items table
        this.invoiceItemTableFields=new Vector<String>();
        this.invoiceItemTableFields.add("No.");
        this.invoiceItemTableFields.add("Item Name");
        this.invoiceItemTableFields.add("Item Price");
        this.invoiceItemTableFields.add("Count");
        this.invoiceItemTableFields.add("Item Total");
        Vector<Vector<String>> invoiceItemTableData= new Vector<Vector<String>>();
        this.invoiceItemsTable= new JTable(invoiceItemTableData, invoiceItemTableFields);

        //Decorative Label
        this.addDeleteIllustrationLabel= new JLabel("Press save to save Adding or deleting invoice Data");
        this.addDeleteIllustrationLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        //Constructing and arranging Add or Delete invoice Items (Buttons)
        this.invoiceItemsAddDeleteFunctionsPanel = new JPanel();
        this.invoiceItemsAddDeleteFunctionsPanel.setLayout(new BoxLayout(this.invoiceItemsAddDeleteFunctionsPanel,BoxLayout.X_AXIS));
        this.invoiceItemsAddDeleteFunctionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.addInvoiceItemButton= new JButton("Add Item");
        this.addInvoiceItemButton.addActionListener(new AddInvoiceItemButtonActionListener());
        this.deleteInvoiceItemButton= new JButton("Delete Selected Item");
        this.deleteInvoiceItemButton.addActionListener(new DeleteInvoiceItemButtonActionListener());
        this.invoiceItemsAddDeleteFunctionsPanel.add(addInvoiceItemButton);
        this.invoiceItemsAddDeleteFunctionsPanel.add(deleteInvoiceItemButton);

        //Constructing and arranging Save (updated or New Invoice) and Cancel Invoice (Buttons)
        this.invoiceItemsSaveCancelFunctionsPanel = new JPanel();
        this.invoiceItemsSaveCancelFunctionsPanel.setLayout(new BoxLayout(this.invoiceItemsSaveCancelFunctionsPanel,BoxLayout.X_AXIS));
        this.invoiceItemsSaveCancelFunctionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.saveInvoiceChangesButton= new JButton("Save");
        this.saveInvoiceChangesButton.addActionListener(new SaveInvoiceChangesActionListener());
        this.cancelInvoiceChangesButton= new JButton("Cancel");
        this.cancelInvoiceChangesButton.addActionListener(new CancelInvoiceChangesActionList());
        this.invoiceItemsSaveCancelFunctionsPanel.add(saveInvoiceChangesButton);
        this.invoiceItemsSaveCancelFunctionsPanel.add(cancelInvoiceChangesButton);

        //Adding the components to the right panel
        this.rightPanel.add(this.illustrationLabel);
        this.rightPanel.add(this.invoiceNumberPanel);
        this.rightPanel.add(this.invoiceDatePanel);
        this.rightPanel.add(this.invoiceCustomerPanel);
        this.rightPanel.add(this.invoiceTotalPanel);
        this.rightPanel.add(new JScrollPane(this.invoiceItemsTable));
        this.rightPanel.add(this.addDeleteIllustrationLabel);
        this.rightPanel.add(this.invoiceItemsAddDeleteFunctionsPanel);
        this.rightPanel.add(this.invoiceItemsSaveCancelFunctionsPanel);

        if(rightPannelStatus==0)
            setPanelEnabled(rightPanel,false);
        else
            setPanelEnabled(rightPanel,true);


    }

    /**
     * Add Invoice Item Action Listener (Button)
     * Adds a new row to enter a new invoice item
     */
    class AddInvoiceItemButtonActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultTableModel invoiceItemsTableModels= (DefaultTableModel) MainView.this.invoiceItemsTable.getModel();
            int rowCount= invoiceItemsTableModels.getRowCount();
            Vector<String> rowData= new Vector<>();
            rowData.add(Integer.toString(rowCount+1));
            rowData.add("");
            rowData.add("");
            rowData.add("");
            rowData.add("");
            invoiceItemsTableModels.addRow(rowData);
        }
    }


    /**
     * Deletes Invoice Item Action Listener (Button)
     * Notifies the user if no invoice item is selected
     * Deletes the selected Invoice Line
     * Does not save the deletion in the actual file until Save Button is pressed
     */
    class DeleteInvoiceItemButtonActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultTableModel invoiceItemsTableModels= (DefaultTableModel) MainView.this.invoiceItemsTable.getModel();
            int row= MainView.this.invoiceItemsTable.getSelectedRow();
            if(row==-1){
                JDialog d = new JDialog(MainView.this,"No Item Selected",true);
                JLabel l = new JLabel("Please Select an Item to delete");
                d.add(l);
                JButton b= new JButton("Ok");
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    d.dispose();
                }
                 });
                d.add(b);
                d.setSize(200,100);
                d.setLayout(new FlowLayout());
                d.setLocation(200,200);
                d.setVisible(true);
            }else {
                DefaultTableModel invoicesTableModels= (DefaultTableModel) MainView.this.invoiceItemsTable.getModel();
                invoicesTableModels.removeRow(row);

                for(int i=0;i<invoiceItemsTableModels.getRowCount();i++){
                    invoiceItemsTableModels.setValueAt(""+(i+1),i,0);
                }
            }
        }
    }

    /**
     * Save Invoice Action Listener
     * save invoice changes (New Invoice or Update invoice)
     * Fields Validation happens in this step ...
     * A feedBack is given to the user with the invalid fields
     */
    class SaveInvoiceChangesActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Controller controller = Controller.getInstance();
            String validation=controller.validateDateFormat(MainView.this.invoiceDateTextField.getText())+"\n";
            validation+=controller.validateNameFormat(MainView.this.invoiceCustomerNameTextField.getText())+"\n";
            TableModel invoiceItemsTableModels= (TableModel) invoiceItemsTable.getModel();
            for (int count = 0; count < invoiceItemsTableModels.getRowCount(); count++) {
                validation += (controller.validateNameFormat(invoiceItemsTableModels.getValueAt(count, 1).toString())+"\n");
                validation += (controller.validateDoubleFormat(invoiceItemsTableModels.getValueAt(count, 2).toString())+"\n");
                validation += (controller.validateIntegerFormat(invoiceItemsTableModels.getValueAt(count, 3).toString())+"\n");
            }
            if(validation.trim().equals("")) {

                int invNumber =Integer.parseInt(MainView.this.invoiceNumberValue.getText());
                String invDate= MainView.this.invoiceDateTextField.getText();
                String invCustomerName=MainView.this.invoiceCustomerNameTextField.getText();
                ArrayList<InvoiceLine> ill = new ArrayList<>();
                InvoiceHeader ih= new InvoiceHeader(invNumber, invDate,invCustomerName,ill);

                for (int count = 0; count < invoiceItemsTableModels.getRowCount(); count++) {
                    InvoiceLine il = new InvoiceLine();
                    il.setItemName(invoiceItemsTableModels.getValueAt(count, 1).toString());
                    il.setItemPrice(Double.parseDouble(invoiceItemsTableModels.getValueAt(count, 2).toString()));
                    il.setCount(Integer.parseInt(invoiceItemsTableModels.getValueAt(count, 3).toString()));
                    ill.add(il);
                }
                ih.setInvoiceLines(ill);
                controller.updateInvoiceHeader(ih);
                MainView.this.refreshInvoiceTable();
            }
            else{
                JDialog d = new JDialog(MainView.this,"Incorrect Input Format",true);
                JLabel l = new JLabel(validation);
                d.add(l);
                JButton b= new JButton("Ok");
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        d.dispose();
                    }
                });
                d.add(b);
                d.pack();
                d.setSize(400,200);
                d.setLayout(new FlowLayout());
                d.setLocation(200,200);
                d.setVisible(true);

            }
        }
    }

    /**
     * Cancel Invoice Action Listener (Button)
     * Cancels saving any changes to an existing invoice or a new invoice
     */
    class CancelInvoiceChangesActionList implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = MainView.this.invoicesTable.getSelectedRow();
            if(row==-1){
                MainView.this.rightPannelStatus=0;
                MainView.this.illustrationLabel.setText("Select an invoice or Create a new Invoice");
                MainView.this.setPanelEnabled(MainView.this.rightPanel,false);
            }
            else{
                MainView.this.rightPannelStatus=2;
                MainView.this.illustrationLabel.setText("");
                MainView.this.setPanelEnabled(MainView.this.rightPanel,true);
                TableModel invoicesTableModels= (TableModel) invoicesTable.getModel();
                String invoiceNumberString=invoicesTableModels.getValueAt(row, 0).toString();
                MainView.this.invoiceNumberValue.setText(invoiceNumberString);
                String invoiceDateString=invoicesTableModels.getValueAt(row, 1).toString();
                MainView.this.invoiceDateTextField.setText(invoiceDateString);
                String invoiceCustomerName=invoicesTableModels.getValueAt(row, 2).toString();
                MainView.this.invoiceCustomerNameTextField.setText(invoiceCustomerName);
                double totalPrice=MainView.this.refreshInvoiceItemsTable(Integer.parseInt(invoiceNumberString));
                MainView.this.invoiceTotalValue.setText(""+totalPrice);
            }
        }
    }

    /**
     * Enabling or Disabling Right Panel Components
     * The panel is disabled when neither an invoice is selected from the left panel
     *                              or no new invoice is being created
     * @param panel
     * @param isEnabled true or false
     */
    private void setPanelEnabled(JPanel panel, Boolean isEnabled) {
        panel.setEnabled(isEnabled);

        Component[] components = panel.getComponents();

        for (Component component : components) {
            if (component instanceof JPanel) {
                setPanelEnabled((JPanel) component, isEnabled);
            }
            component.setEnabled(isEnabled);
        }
    }

    /**
     * Clearing the values of the Right panel when no being used
     */
    private void clearRightPanel(){
        this.invoiceNumberValue.setText("");
        this.invoiceDateTextField.setText("");
        this.invoiceCustomerNameTextField.setText("");
        this.invoiceTotalValue.setText("0.0");

        this.invoiceItemTableFields=new Vector<String>();
        this.invoiceItemTableFields.add("No.");
        this.invoiceItemTableFields.add("Item Name");
        this.invoiceItemTableFields.add("Item Price");
        this.invoiceItemTableFields.add("Count");
        this.invoiceItemTableFields.add("Item Total");
        this.invoiceItemsTableModel=new DefaultTableModel(invoiceItemTableFields,0){
            @Override
            public boolean isCellEditable(int row, int column){
                if ((column==0)||(column==4))
                    return false;
                return true;
            }
        };
        Vector<String> invoiceItems= new Vector<String>();
        invoiceItems.add("1");
        invoiceItems.add("");
        invoiceItems.add("");
        invoiceItems.add("");
        invoiceItems.add("");
        this.invoiceItemsTableModel.addRow(invoiceItems);
        this.invoiceItemsTable.setModel(MainView.this.invoiceItemsTableModel);
    }


}
