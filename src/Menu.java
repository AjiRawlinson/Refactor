import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Menu extends JFrame{

    private ArrayList<Customer> customerList = new ArrayList<Customer>();
    private int position = 0;
    private String password;
    private Customer customer = null;
    private CustomerAccount acc = new CustomerAccount();
    JFrame f, f1;
    JLabel firstNameLabel, surnameLabel, pPPSLabel, dOBLabel;
    JTextField firstNameTextField, surnameTextField, pPSTextField, dOBTextField;
    JLabel customerIDLabel, passwordLabel;
    JTextField customerIDTextField, passwordTextField;
    Container content;
    Customer e;
    Administrator administrator;


    JPanel panel2;
    JButton add;

    public static void main(String[] args)
    {
        Menu driver = new Menu();
        driver.menuStart();
    }

    //********************************************************************************************************************
    //                                           MENU FUNCTION
    //********************************************************************************************************************
    public void menuStart()
    {
		   /*The menuStart method asks the user if they are a new customer, an existing customer or an admin. It will then start the create customer process
		  if they are a new customer, or will ask them to log in if they are an existing customer or admin.*/

        f = new JFrame("User Type");
        f.setSize(400, 300);
        f.setLocation(200, 200);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) { System.exit(0); }
        });

        JPanel userTypePanel = new JPanel();
        final ButtonGroup userType = new ButtonGroup();
        JRadioButton radioButton;
        userTypePanel.add(radioButton = new JRadioButton("Existing Customer"));
        radioButton.setActionCommand("Customer");
        userType.add(radioButton);

        userTypePanel.add(radioButton = new JRadioButton("Administrator"));
        radioButton.setActionCommand("Administrator");
        userType.add(radioButton);

        userTypePanel.add(radioButton = new JRadioButton("New Customer"));
        radioButton.setActionCommand("New Customer");
        userType.add(radioButton);

        JPanel continuePanel = new JPanel();
        JButton continueButton = new JButton("Continue");
        continuePanel.add(continueButton);

        Container content = f.getContentPane();
        content.setLayout(new GridLayout(2, 1));
        content.add(userTypePanel);
        content.add(continuePanel);


        continueButton.addActionListener(new ActionListener(  ) {
            public void actionPerformed(ActionEvent ae) {
                String user = userType.getSelection().getActionCommand();

                //Aji Change: changed if statements to switch statement for menu choices.
                // Separated each menu option into separate methods.
                switch (user) {
                    case "New Customer":
                        newCustomer();
                        break;
                    case "Administrator":
                        adminSelect();
                        break;
                    case "Customer":
                        customerSelect();
                        break;
                }
            }
        });f.setVisible(true);
    }

    //********************************************************************************************************************
    //                                           SELECTING NEW CUSTOMER
    //********************************************************************************************************************
    public void newCustomer() {
        f.dispose();
        f1 = new JFrame("Create New Customer");
        f1.setSize(400, 300);
        f1.setLocation(200, 200);
        f1.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) { System.exit(0); }
        });
        Container content = f1.getContentPane();
        content.setLayout(new BorderLayout());

        firstNameLabel = new JLabel("First Name:", SwingConstants.RIGHT);
        surnameLabel = new JLabel("Surname:", SwingConstants.RIGHT);
        pPPSLabel = new JLabel("PPS Number:", SwingConstants.RIGHT);
        dOBLabel = new JLabel("Date of birth", SwingConstants.RIGHT);
        firstNameTextField = new JTextField(20);
        surnameTextField = new JTextField(20);
        pPSTextField = new JTextField(20);
        dOBTextField = new JTextField(20);
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(firstNameLabel);
        panel.add(firstNameTextField);
        panel.add(surnameLabel);
        panel.add(surnameTextField);
        panel.add(pPPSLabel);
        panel.add(pPSTextField);
        panel.add(dOBLabel);
        panel.add(dOBTextField);

        panel2 = new JPanel();
        add = new JButton("Add");
        JButton cancel = new JButton("Cancel");

        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String PPS = pPSTextField.getText();
                String firstName = firstNameTextField.getText();
                String surname = surnameTextField.getText();
                String DOB = dOBTextField.getText();
                password = "";
                String CustomerID = "ID"+PPS ;

                f1.dispose();

                boolean validPassword = false; //changed boolean name and value to false, easier to read.
                do{ //while the password is invalid
                    password = JOptionPane.showInputDialog(f, "Enter 7 character Password;"); //why exactly 7 characters?

                    if(password.length() == 7) {//Making sure password is 7 characters
                        validPassword = true;
                    }
                    else {
                        JOptionPane.showMessageDialog(null, null, "Password must be 7 charatcers long", JOptionPane.OK_OPTION);
                    }
                }while(!validPassword);

                ArrayList<CustomerAccount> accounts = new ArrayList<CustomerAccount> ();
                Customer newCust = new Customer(PPS, surname, firstName, DOB, CustomerID, password, accounts);
                customerList.add(newCust);
                JOptionPane.showMessageDialog(f, "CustomerID = " + CustomerID +"\n Password = " + password  ,"Customer created.",  JOptionPane.INFORMATION_MESSAGE);
                menuStart();
                f1.dispose();
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f1.dispose();
                menuStart();
            }
        });

        panel2.add(add);
        panel2.add(cancel);

        content.add(panel, BorderLayout.CENTER);
        content.add(panel2, BorderLayout.SOUTH);

        f1.setVisible(true);
    }

    //********************************************************************************************************************
    //                                           SELECTING ADMIN
    //********************************************************************************************************************
    public void adminSelect() { //why not use one dialog box for password and username??
        administrator = new Administrator();
        boolean validUsername = false, validPassword = false;

        while(!validUsername) {//Aji Change: made log on simpler, no need to ask user to if they want to retry, just add warning message.
            Object adminUsername = JOptionPane.showInputDialog(f, "Enter Administrator Username:");
            if(!adminUsername.equals(administrator.getUsername()))//search admin list for admin with matching admin username
            {
                JOptionPane.showMessageDialog(null, "Username Incorrect", "Error", JOptionPane.WARNING_MESSAGE);
            } else { validUsername = true; }
        }

        while(!validPassword) {//Aji Change: Same as username input
            Object adminPassword = JOptionPane.showInputDialog(f, "Enter Administrator Password;");
            if(!adminPassword.equals(administrator.getPassword())) {//search admin list for admin with matching admin password
                JOptionPane.showMessageDialog(null, "Password Incorrect", "Error", JOptionPane.WARNING_MESSAGE);
            } else { validPassword = true; }
        }

        if(validPassword && validUsername) {
            f.dispose();
            admin();
        }
    }

    //********************************************************************************************************************
    //                                           SELECTING EXISTING CUSTOMER
    //********************************************************************************************************************
    public void customerSelect() {
        boolean customerIDFound = true, validPassword = true;
//        Customer customer = null;

        do{// while checking for customer id
            Object customerID = JOptionPane.showInputDialog(f, "Enter Customer ID:");
            for (Customer aCustomer: customerList) {
                if(aCustomer.getCustomerID().equals(customerID)) {//search customer list for matching customer ID
                    customerIDFound = true;
                    customer = aCustomer;
                }
            }

            if(!customerIDFound){//could not find customer ID
                JOptionPane.showMessageDialog(null, "Customer ID Incorrect", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } while(!customerIDFound);

        do{//check for password
            Object customerPassword = JOptionPane.showInputDialog(f, "Enter Customer Password;");
            if(!customer.getPassword().equals(customerPassword)) {//check if custoemr password is correct
                JOptionPane.showMessageDialog(null, "Customer ID Incorrect", "Error", JOptionPane.WARNING_MESSAGE);
            } else { validPassword = true; }

        } while(!validPassword);

        if(customerIDFound && validPassword) {f.dispose();
            customer(customer);
        }
    }

    //********************************************************************************************************************
    //                                           ADMIN MENU
    //********************************************************************************************************************
    public void admin() {
        setupJFrame("Administrator Menu", 400, 400);

        JPanel deleteCustomerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteCustomer = new JButton("Delete Customer");
        deleteCustomer.setPreferredSize(new Dimension(250, 20));
        deleteCustomerPanel.add(deleteCustomer);

        JPanel deleteAccountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteAccount = new JButton("Delete Account");
        deleteAccount.setPreferredSize(new Dimension(250, 20));
        deleteAccountPanel.add(deleteAccount);

        JPanel bankChargesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton bankChargesButton = new JButton("Apply Bank Charges");
        bankChargesButton.setPreferredSize(new Dimension(250, 20));
        bankChargesPanel.add(bankChargesButton);

        JPanel interestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton interestButton = new JButton("Apply Interest");
        interestPanel.add(interestButton);
        interestButton.setPreferredSize(new Dimension(250, 20));

        JPanel editCustomerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton editCustomerButton = new JButton("Edit existing Customer");
        editCustomerPanel.add(editCustomerButton);
        editCustomerButton.setPreferredSize(new Dimension(250, 20));

        JPanel navigatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton navigateButton = new JButton("Navigate Customer Collection");
        navigatePanel.add(navigateButton);
        navigateButton.setPreferredSize(new Dimension(250, 20));

        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton summaryButton = new JButton("Display Summary Of All Accounts");
        summaryPanel.add(summaryButton);
        summaryButton.setPreferredSize(new Dimension(250, 20));

        JPanel accountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton accountButton = new JButton("Add an Account to a Customer");
        accountPanel.add(accountButton);
        accountButton.setPreferredSize(new Dimension(250, 20));

        JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton returnButton = new JButton("Exit Admin Menu");
        returnPanel.add(returnButton);

        JLabel label1 = new JLabel("Please select an option");

        content = f.getContentPane();
        content.setLayout(new GridLayout(9, 1));
        content.add(label1);
        content.add(accountPanel);
        content.add(bankChargesPanel);
        content.add(interestPanel);
        content.add(editCustomerPanel);
        content.add(navigatePanel);
        content.add(summaryPanel);
        content.add(deleteCustomerPanel);
        content.add(returnPanel);

        //-----------------------------------------------------------------------------------------------------------------
        //                                           Bank Charge
        //-----------------------------------------------------------------------------------------------------------------

        bankChargesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!custListEmpty()) {
                    Object customerID = JOptionPane.showInputDialog(f, "Customer ID of Customer You Wish to Apply Charges to:");
                    customer = getCustomerByID(customerID.toString());
                    setupJFrame("Bank Chargers", 400, 400);

                    JComboBox<String> box = new JComboBox<String>();
                    for (int i = 0; i < customer.getAccounts().size(); i++) {
                        box.addItem(customer.getAccounts().get(i).getNumber());
                    }
                    box.getSelectedItem();

                    JPanel boxPanel = new JPanel();
                    boxPanel.add(box);
                    JPanel buttonPanel = new JPanel();
                    JButton continueButton = new JButton("Apply Charge");
                    JButton returnButton = new JButton("Return");
                    buttonPanel.add(continueButton);
                    buttonPanel.add(returnButton);
                    Container content = f.getContentPane();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(boxPanel);
                    content.add(buttonPanel);

                    if (customer.getAccounts().isEmpty()) {
                        JOptionPane.showMessageDialog(f, "This customer has no accounts! \n The admin must add acounts to this customer.", "Oops!", JOptionPane.INFORMATION_MESSAGE);
                        f.dispose();
                        admin();
                    } else {
                        acc = getCustomerAccountByAccID(box.getSelectedItem().toString());

                        continueButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent ae) {
                                administrator.bankCharge(acc);
                            }
                        });

                        returnButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent ae) {
                                f.dispose();
                                admin();
                            }
                        });
                    }
                }
            }
        });

        //-----------------------------------------------------------------------------------------------------------------
        //                                           Interest Charge
        //-----------------------------------------------------------------------------------------------------------------
        interestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!custListEmpty()) {
                    Object customerID = JOptionPane.showInputDialog(f, "Customer ID of Customer You Wish to Apply Interest to:");
                    customer = getCustomerByID(customerID.toString());
                    setupJFrame("Interest", 400, 400);

                    JComboBox<String> box = new JComboBox<String>();
                    for (int i = 0; i < customer.getAccounts().size(); i++) {
                        box.addItem(customer.getAccounts().get(i).getNumber());
                    }

                    box.getSelectedItem();

                    JPanel boxPanel = new JPanel();

                    JLabel label = new JLabel("Select an account to apply interest to:");
                    boxPanel.add(label);
                    boxPanel.add(box);
                    JPanel buttonPanel = new JPanel();
                    JButton continueButton = new JButton("Apply Interest");
                    JButton returnButton = new JButton("Return");
                    buttonPanel.add(continueButton);
                    buttonPanel.add(returnButton);
                    Container content = f.getContentPane();
                    content.setLayout(new GridLayout(2, 1));

                    content.add(boxPanel);
                    content.add(buttonPanel);

                    continueButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            administrator.bankInterest(getCustomerAccountByAccID(box.getSelectedItem().toString()));
                            f.dispose();
                            admin();
                        }
                    });

                    returnButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            f.dispose();
                            menuStart();
                        }
                    });
                }
            }
        });

        //-----------------------------------------------------------------------------------------------------------------
        //                                           Edit Customer Details
        //-----------------------------------------------------------------------------------------------------------------
        editCustomerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!custListEmpty()) {
                    Object customerID = JOptionPane.showInputDialog(f, "Customer ID of Customer You Want to Search:");
                    customer = getCustomerByID(customerID.toString());
                    if(customer == null) {
                        f.dispose();
                        admin();
                    }

                    setupJFrame("Edit Customer", 400, 400);

                    firstNameLabel = new JLabel("First Name:", SwingConstants.LEFT);
                    surnameLabel = new JLabel("Surname:", SwingConstants.LEFT);
                    pPPSLabel = new JLabel("PPS Number:", SwingConstants.LEFT);
                    dOBLabel = new JLabel("Date of birth", SwingConstants.LEFT);
                    customerIDLabel = new JLabel("CustomerID:", SwingConstants.LEFT);
                    passwordLabel = new JLabel("Password:", SwingConstants.LEFT);
                    firstNameTextField = new JTextField(20);
                    surnameTextField = new JTextField(20);
                    pPSTextField = new JTextField(20);
                    dOBTextField = new JTextField(20);
                    customerIDTextField = new JTextField(20);
                    passwordTextField = new JTextField(20);

                    JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JPanel cancelPanel = new JPanel();

                    textPanel.add(firstNameLabel);
                    textPanel.add(firstNameTextField);
                    textPanel.add(surnameLabel);
                    textPanel.add(surnameTextField);
                    textPanel.add(pPPSLabel);
                    textPanel.add(pPSTextField);
                    textPanel.add(dOBLabel);
                    textPanel.add(dOBTextField);
                    textPanel.add(customerIDLabel);
                    textPanel.add(customerIDTextField);
                    textPanel.add(passwordLabel);
                    textPanel.add(passwordTextField);

                    firstNameTextField.setText(customer.getFirstName());
                    surnameTextField.setText(customer.getSurname());
                    pPSTextField.setText(customer.getPPS());
                    dOBTextField.setText(customer.getDOB());
                    customerIDTextField.setText(customer.getCustomerID());
                    passwordTextField.setText(customer.getPassword());

                    JButton saveButton = new JButton("Save");
                    JButton cancelButton = new JButton("Exit");

                    cancelPanel.add(cancelButton, BorderLayout.SOUTH);
                    cancelPanel.add(saveButton, BorderLayout.SOUTH);
                    Container content = f.getContentPane();
                    content.setLayout(new GridLayout(2, 1));
                    content.add(textPanel, BorderLayout.NORTH);
                    content.add(cancelPanel, BorderLayout.SOUTH);

                    f.setContentPane(content);
                    f.setSize(340, 350);
                    f.setLocation(200, 200);
                    f.setVisible(true);
                    f.setResizable(false);

                    saveButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            customer.setFirstName(firstNameTextField.getText());
                            customer.setSurname(surnameTextField.getText());
                            customer.setPPS(pPSTextField.getText());
                            customer.setDOB(dOBTextField.getText());
                            customer.setCustomerID(customerIDTextField.getText());
                            customer.setPassword(passwordTextField.getText());

                            JOptionPane.showMessageDialog(null, "Changes Saved.");
                        }
                    });

                    cancelButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            f.dispose();
                            admin();
                        }
                    });
                }
            }
        });

        //-----------------------------------------------------------------------------------------------------------------
        //                                           Bank Summary
        //-----------------------------------------------------------------------------------------------------------------
        summaryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                setupJFrame("Summory of Transations", 550, 400);

                JLabel label1 = new JLabel("Summary of all transactions: ");

                JPanel returnPanel = new JPanel();
                JButton returnButton = new JButton("Return");
                returnPanel.add(returnButton);

                JPanel textPanel = new JPanel();

                textPanel.setLayout(new BorderLayout());
                JTextArea textArea = new JTextArea(40, 20);
                textArea.setEditable(false);
                textPanel.add(label1, BorderLayout.NORTH);
                textPanel.add(textArea, BorderLayout.CENTER);
                textPanel.add(returnButton, BorderLayout.SOUTH);

                JScrollPane scrollPane = new JScrollPane(textArea);
                textPanel.add(scrollPane);

                for (int custIndex = 0; custIndex < customerList.size(); custIndex++)//For each customer, for each account, it displays each transaction.
                {
                    for (int accIndex = 0; accIndex < customerList.get(custIndex).getAccounts().size(); accIndex++) {
                        acc = customerList.get(custIndex).getAccounts().get(accIndex);
                        for (int transationIndex = 0; transationIndex < customerList.get(custIndex).getAccounts().get(accIndex).getTransactionList().size(); transationIndex++) {
                            textArea.append(acc.getTransactionList().get(transationIndex).toString());
                        }
                    }
                }
                textPanel.add(textArea);
                content.removeAll();

                Container content = f.getContentPane();
                content.setLayout(new GridLayout(1, 1));
                content.add(textPanel);
                ;

                returnButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        f.dispose();
                        admin();
                    }
                });
            }
        });

        //-----------------------------------------------------------------------------------------------------------------
        //                                           Customer Navigation
        //-----------------------------------------------------------------------------------------------------------------
        navigateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                f.dispose();

                if (!custListEmpty()) {

                    JButton first, previous, next, last, cancel;
                    JPanel gridPanel, buttonPanel, cancelPanel;

                    Container content = getContentPane();
                    content.setLayout(new BorderLayout());

                    buttonPanel = new JPanel();
                    gridPanel = new JPanel(new GridLayout(8, 2));
                    cancelPanel = new JPanel();

                    firstNameLabel = new JLabel("First Name:", SwingConstants.LEFT);
                    surnameLabel = new JLabel("Surname:", SwingConstants.LEFT);
                    pPPSLabel = new JLabel("PPS Number:", SwingConstants.LEFT);
                    dOBLabel = new JLabel("Date of birth", SwingConstants.LEFT);
                    customerIDLabel = new JLabel("CustomerID:", SwingConstants.LEFT);
                    passwordLabel = new JLabel("Password:", SwingConstants.LEFT);
                    firstNameTextField = new JTextField(20);
                    surnameTextField = new JTextField(20);
                    pPSTextField = new JTextField(20);
                    dOBTextField = new JTextField(20);
                    customerIDTextField = new JTextField(20);
                    passwordTextField = new JTextField(20);

                    first = new JButton("First");
                    previous = new JButton("Previous");
                    next = new JButton("Next");
                    last = new JButton("Last");
                    cancel = new JButton("Cancel");

                    firstNameTextField.setText(customerList.get(0).getFirstName());
                    surnameTextField.setText(customerList.get(0).getSurname());
                    pPSTextField.setText(customerList.get(0).getPPS());
                    dOBTextField.setText(customerList.get(0).getDOB());
                    customerIDTextField.setText(customerList.get(0).getCustomerID());
                    passwordTextField.setText(customerList.get(0).getPassword());

                    firstNameTextField.setEditable(false);
                    surnameTextField.setEditable(false);
                    pPSTextField.setEditable(false);
                    dOBTextField.setEditable(false);
                    customerIDTextField.setEditable(false);
                    passwordTextField.setEditable(false);

                    gridPanel.add(firstNameLabel);
                    gridPanel.add(firstNameTextField);
                    gridPanel.add(surnameLabel);
                    gridPanel.add(surnameTextField);
                    gridPanel.add(pPPSLabel);
                    gridPanel.add(pPSTextField);
                    gridPanel.add(dOBLabel);
                    gridPanel.add(dOBTextField);
                    gridPanel.add(customerIDLabel);
                    gridPanel.add(customerIDTextField);
                    gridPanel.add(passwordLabel);
                    gridPanel.add(passwordTextField);

                    buttonPanel.add(first);
                    buttonPanel.add(previous);
                    buttonPanel.add(next);
                    buttonPanel.add(last);

                    cancelPanel.add(cancel);

                    content.add(gridPanel, BorderLayout.NORTH);
                    content.add(buttonPanel, BorderLayout.CENTER);
                    content.add(cancelPanel, BorderLayout.AFTER_LAST_LINE);

                    first.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            position = 0;
                            setTextFieldsByCustListIndex(position);
                        }
                    });

                    previous.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            if (position < 1) {
                            }//don't do anything
                            else {
                                position = position - 1;
                                setTextFieldsByCustListIndex(position);
                            }
                        }
                    });

                    next.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            if (position == customerList.size() - 1) {
                            }//don't do anything
                            else {
                                position = position + 1;
                                setTextFieldsByCustListIndex(position);
                            }
                        }
                    });

                    last.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            position = customerList.size() - 1;
                            setTextFieldsByCustListIndex(position);
                        }
                    });

                    cancel.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            dispose();
                            admin();
                        }
                    });
                    setContentPane(content);
                    setSize(400, 300);
                    setVisible(true);
                }
            }
        });

        //-----------------------------------------------------------------------------------------------------------------
        //                                           Account Navigation
        //-----------------------------------------------------------------------------------------------------------------
        accountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                f.dispose();

                if (!custListEmpty()) {
                    Object customerID = JOptionPane.showInputDialog(f, "Customer ID of Customer You Want to add account to:");
                    customer = getCustomerByID(customerID.toString());
                    String accountNumber = "" + (customerList.indexOf(customer) + 1) * 10 + (customer.getAccounts().size() + 1);
                    administrator.addAccount(customer, accountNumber);

                    f.dispose();
                    admin();
                }
            }
        });

        //-----------------------------------------------------------------------------------------------------------------
        //                                           Delete Customer
        //-----------------------------------------------------------------------------------------------------------------
        deleteCustomer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                if (!custListEmpty()) {
                    Object customerID = JOptionPane.showInputDialog(f, "Customer ID of Customer You Wish to Delete:");
                    customer = getCustomerByID(customerID.toString());

                    if (customer.getAccounts().size() > 0) {
                        JOptionPane.showMessageDialog(f, "This customer has accounts. \n You must delete a customer's accounts before deleting a customer ", "Oops!", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        customerList.remove(customer);
                        JOptionPane.showMessageDialog(f, "Customer Deleted ", "Success.", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        //-----------------------------------------------------------------------------------------------------------------
        //                                           Delete Account *Unfinished*
        //-----------------------------------------------------------------------------------------------------------------
        deleteAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!custListEmpty()) {
                    Object customerID = JOptionPane.showInputDialog(f, "Customer ID of Customer Delete the Account of:");
                    customer = getCustomerByID(customerID.toString());

                    //Here I would make the user select a an account to delete from a combo box. If the account had a balance of 0 then it would be deleted. (I do not have time to do this)
                }
            }

        });

        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                f.dispose();
                menuStart();
            }
        });
    }

    //********************************************************************************************************************
    //                                           CUSTOMER MENU
    //********************************************************************************************************************
    public void customer(Customer cust) {
        e = cust;
        setupJFrame("Customer Menu", 400, 400);

        if(e.getAccounts().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "This customer does not have any accounts yet. \n An admin must create an account for this customer \n for them to be able to use customer functionality. " ,"Oops!",  JOptionPane.INFORMATION_MESSAGE);
            f.dispose();
            menuStart();
        }
        else
        {
            JPanel buttonPanel = new JPanel();
            JPanel boxPanel = new JPanel();
            JPanel labelPanel = new JPanel();

            JLabel label = new JLabel("Select Account:");
            labelPanel.add(label);

            JButton returnButton = new JButton("Return");
            buttonPanel.add(returnButton);
            JButton continueButton = new JButton("Continue");
            buttonPanel.add(continueButton);

            JComboBox<String> box = new JComboBox<String>();
            for (int i =0; i < e.getAccounts().size(); i++)
            {
                box.addItem(e.getAccounts().get(i).getNumber());
            }

            for(int i = 0; i<e.getAccounts().size(); i++)
            {
                if(e.getAccounts().get(i).getNumber() == box.getSelectedItem() )
                {
                    acc = e.getAccounts().get(i);
                }
            }

            boxPanel.add(box);
            content = f.getContentPane();
            content.setLayout(new GridLayout(3, 1));
            content.add(labelPanel);
            content.add(boxPanel);
            content.add(buttonPanel);

            returnButton.addActionListener(new ActionListener(  ) {
                public void actionPerformed(ActionEvent ae) {
                    f.dispose();
                    menuStart();
                }
            });

            continueButton.addActionListener(new ActionListener(  ) {//new method for all of this?
                public void actionPerformed(ActionEvent ae) {
                    setupJFrame("Custumer Menu", 400, 400);

                    JPanel statementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JButton statementButton = new JButton("Display Bank Statement");
                    statementButton.setPreferredSize(new Dimension(250, 20));

                    statementPanel.add(statementButton);

                    JPanel lodgementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JButton lodgementButton = new JButton("Lodge money into account");
                    lodgementPanel.add(lodgementButton);
                    lodgementButton.setPreferredSize(new Dimension(250, 20));

                    JPanel withdrawalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JButton withdrawButton = new JButton("Withdraw money from account");
                    withdrawalPanel.add(withdrawButton);
                    withdrawButton.setPreferredSize(new Dimension(250, 20));

                    JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    JButton returnButton = new JButton("Exit Customer Menu");
                    returnPanel.add(returnButton);

                    JLabel label1 = new JLabel("Please select an option");

                    content = f.getContentPane();
                    content.setLayout(new GridLayout(5, 1));
                    content.add(label1);
                    content.add(statementPanel);
                    content.add(lodgementPanel);
                    content.add(withdrawalPanel);
                    content.add(returnPanel);

                    //-----------------------------------------------------------------------------------------------------------------
                    //                                           Bank Statement
                    //-----------------------------------------------------------------------------------------------------------------
                    statementButton.addActionListener(new ActionListener(  ) {
                        public void actionPerformed(ActionEvent ae) {

                            setupJFrame("Statement", 400, 400);

                            JLabel label1 = new JLabel("Summary of account transactions: ");

                            JPanel returnPanel = new JPanel();
                            JButton returnButton = new JButton("Return");
                            returnPanel.add(returnButton);

                            JPanel textPanel = new JPanel();

                            textPanel.setLayout( new BorderLayout() );
                            JTextArea textArea = new JTextArea(40, 20);
                            textArea.setEditable(false);
                            textPanel.add(label1, BorderLayout.NORTH);
                            textPanel.add(textArea, BorderLayout.CENTER);
                            textPanel.add(returnButton, BorderLayout.SOUTH);

                            JScrollPane scrollPane = new JScrollPane(textArea);
                            textPanel.add(scrollPane);

                            for (int i = 0; i < acc.getTransactionList().size(); i ++)//put in customer class?
                            {
                                textArea.append(acc.getTransactionList().get(i).toString());

                            }

                            textPanel.add(textArea);
                            content.removeAll();

                            Container content = f.getContentPane();
                            content.setLayout(new GridLayout(1, 1));
                            content.add(textPanel);

                            returnButton.addActionListener(new ActionListener(  ) {
                                public void actionPerformed(ActionEvent ae) {
                                    f.dispose();
                                    customer(e);
                                }
                            });
                        }
                    });

                    //-----------------------------------------------------------------------------------------------------------------
                    //                                           Bank Lodgement
                    //-----------------------------------------------------------------------------------------------------------------
                    lodgementButton.addActionListener(new ActionListener(  ) {
                        public void actionPerformed(ActionEvent ae) {
                            e.makeLodgement(acc);
                            f.dispose();
                            customer(e);
                        }
                    });

                    //-----------------------------------------------------------------------------------------------------------------
                    //                                           Withdraw Funds
                    //-----------------------------------------------------------------------------------------------------------------
                    withdrawButton.addActionListener(new ActionListener(  ) {
                        public void actionPerformed(ActionEvent ae) {
                            e.withdrawFunds(acc);
                            f.dispose();
                            customer(e);
                        }
                    });

                    returnButton.addActionListener(new ActionListener(  ) {
                        public void actionPerformed(ActionEvent ae) {
                            f.dispose();
                            menuStart();
                        }
                    });		}
            });
        }
    }

    //********************************************************************************************************************
    //                                          USEFUL METHODS
    //********************************************************************************************************************

    public boolean custListEmpty() {
        if (customerList.isEmpty()) {
            JOptionPane.showMessageDialog(f, "There are no customers yet!", "Error", JOptionPane.INFORMATION_MESSAGE);
            f.dispose();
            admin();
            return true;
        }
        return false;
    }

    public Customer getCustomerByID(String customerID) {
        for (Customer aCustomer : customerList) {
            if (aCustomer.getCustomerID().equals(customerID)) {
                return aCustomer;
            }
        }
        JOptionPane.showMessageDialog(f, "Error", "User not found.", JOptionPane.WARNING_MESSAGE);
        f.dispose();
        admin();
        return null;
    }

    public CustomerAccount getCustomerAccountByAccID(String AccountID) {
        if (customer.getAccounts().isEmpty()) {
            JOptionPane.showMessageDialog(f, "This customer has no accounts! \n The admin must add acounts to this customer.", "Oops!", JOptionPane.INFORMATION_MESSAGE);
            f.dispose();
            admin();
        } else {
            for (int i = 0; i < customer.getAccounts().size(); i++) {
                if (customer.getAccounts().get(i).getNumber().equalsIgnoreCase(AccountID)) {
                    return customer.getAccounts().get(i);
                }
            }
        }
        return null;
    }

    public void setTextFieldsByCustListIndex(int index) {
        firstNameTextField.setText(customerList.get(index).getFirstName());
        surnameTextField.setText(customerList.get(index).getSurname());
        pPSTextField.setText(customerList.get(index).getPPS());
        dOBTextField.setText(customerList.get(index).getDOB());
        customerIDTextField.setText(customerList.get(index).getCustomerID());
        passwordTextField.setText(customerList.get(index).getPassword());
    }

    public void setupJFrame(String title, int sizeL, int sizeW) {
        f = new JFrame(title);
        f.setSize(sizeW, sizeL);
        f.setLocation(200, 200);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        f.setVisible(true);
    }
}