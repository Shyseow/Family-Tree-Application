package FamilyTreeApp;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;

/**
 *File Name: GUI.java
 *Purpose: GUI component of the application
 * 
 *Assumption: Has Person and Tree classes, only support English language
 *@author Seow Hui Yin
 *@version 1.0
 *@since Submission due date: 22/11/2018 
 */

public class GUI {
       
    private JFrame mainFrame;
    private JPanel editPanel;
    private JPanel detailPanel;
    private final JLabel footer = new JLabel("Program loaded");
    private File currentFile;
    private JTree tree;
    private Tree FamilyTree;


    /**
     * Default constructor and initialize the variables
     */
    public GUI() {

        FamilyTree = new Tree();
        currentFile = null;
        tree = new JTree();
        newGUI();
    }
    
        /**
     * Show error dialog with error message from an exception 
     * @param e Exception to get the message 
     */
    private void ErrorDialog(Exception e) {
        JOptionPane.showMessageDialog(mainFrame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    

    /**
     * Set up different panels 
     */
    private void newGUI() {

        mainFrame = new JFrame("Family Tree by Seow Hui Yin_33255186");
        mainFrame.setSize(600, 800);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(Color.WHITE);
        //the program will determine what to do when the user attempts to close the window
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        //set up the menu bar
        MenuBar();

        //set up the header section
        NorthPanel();

        //set up the control section- where data is displayed
        EditPanel();

        //set up the footer bar
        FooterBar();

        //displays the empty tree
        displayTree(FamilyTree);

        //check if user wants to continue using unsavedContinue function
      mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (unsavedContinue()) {
                    System.exit(0);
                }
            }
        });

        mainFrame.setVisible(true);

    }

    /**
     * Header panel information
     */
    private void NorthPanel() {

        JLabel header= new JLabel("Welcome to the Family Tree Application", JLabel.LEFT); 
        header.setFont(new Font("SansSerif", Font.PLAIN, 28));
        header.setForeground(Color.black); 
                
        JButton open = new JButton("Load Tree");
        open.addActionListener(new load());

        JButton create = new JButton("Create New Tree");
        create.addActionListener(new createTree());

        JButton saveTree = new JButton("Save Tree");
        saveTree.addActionListener(new saveAction());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(0,10,10,10));
        
        //to describes how a component is placed in a GridBagLayout
        GridBagConstraints gridBag = new GridBagConstraints();
        gridBag.gridx = 0;
        gridBag.gridy = 0;
        gridBag.fill = GridBagConstraints.BOTH;
        gridBag.weightx = 1.0;
        gridBag.weighty = 1.0;
        topPanel.add(header, gridBag);
       
        //set frame layout
        //specifies how components are arranged,components are placed in a 
        //container from left to right in the order in which they’re added
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        container.setOpaque(false);
        container.add(open);
        container.add(saveTree);
        container.add(create);
 
        gridBag.gridx = 0;
        gridBag.gridy = 1;
        topPanel.add(container, gridBag);

        mainFrame.add(topPanel, BorderLayout.NORTH);
    }
    
    
    /**
     * Control panel where data are showed 
     */
    private void EditPanel() {
        editPanel = new JPanel();
        // set the transparency of a Swing lightweight component,indicating 
        //whether the component is opaque (true) or transparent (false)
        editPanel.setOpaque(false);
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        
        mainFrame.add(editPanel, BorderLayout.CENTER);
        
    }

    /**
     * Drop-down list menu bar(new,open,save,save as,exit)
     */
    private void MenuBar() {
        JMenuBar menuBar;
        menuBar = new JMenuBar();
        mainFrame.setJMenuBar(menuBar);
        JMenu Menu = new JMenu("File");

        menuBar.add(Menu);

        JMenuItem newAction = new JMenuItem("New");
        Menu.add(newAction);
        newAction.addActionListener(new createTree());
        
        JMenuItem openAction = new JMenuItem("Open");
        Menu.add(openAction);
        openAction.addActionListener(new load());
        
        Menu.addSeparator();

        JMenuItem saveAction = new JMenuItem("Save");
        Menu.add(saveAction);
        saveAction.addActionListener(new saveAction());
        
        JMenuItem saveAsAction = new JMenuItem("Save As");
        Menu.add(saveAsAction);
        saveAsAction.addActionListener(new saveAsAction());
        
        
        JMenuItem exitAction = new JMenuItem("Exit");
        Menu.addSeparator();
        Menu.add(exitAction);

        //to exit the program
        exitAction.addActionListener(new ActionListener() {
            @Override
            //Invoked when exit action occurs
            public void actionPerformed(ActionEvent e) {
                if (unsavedContinue()) {
                    System.exit(0);
                }
            }
        });       
    }

    /**
     * Display user status messages at the bottom of the screen
     */
    private void FooterBar() {
   
        JPanel footerPanel = new JPanel();
        footerPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
   
        mainFrame.add(footerPanel, BorderLayout.SOUTH);
        
        //set size to the mainframe
        footerPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 18));
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.X_AXIS));
        
        //align text to the left
        footer.setHorizontalAlignment(SwingConstants.LEFT);
        //display status message 
        footerPanel.add(footer);

    }

    /**
     * Set and edit text in footer bar
     * @param status display user status message
     */
    private void editFooter(String status) {
        footer.setText(status);
    }

    /**
     * Action class that implements ActionListner
     * To display add relative function after click button for a particular family member
     */
    private class addRelative implements ActionListener {

        private Person member;
 
        public addRelative(Person member) {
            this.member = member;
        }

        @Override
        //Invoked when add relative action occurs
        public void actionPerformed(ActionEvent e) {
            //display the add relative form for the current member
            AddRelativeInfo(member);
        }
    }

    /**
     * Action class that implements ActionListner
     * To edit info of selected family member when edit button is clicked
     */
    private class editMember implements ActionListener {

        private Person member;
        
        public editMember(Person member) {
            this.member = member;
        }

        @Override
        //Invoked when edit member action occurs
        public void actionPerformed(ActionEvent e) {
            //displays the edit member info form
            EditMember(member);
        }
    }
 
    /**
     * Action class that implements ActionListner
     * To show create tree form for a family member
     */
    private class createTree implements ActionListener {

        @Override
        //Invoked when create tree action occurs
        public void actionPerformed(ActionEvent e) {
            
            if (unsavedContinue()) {
                //check if tree is not saved and reset the main variables
                FamilyTree = new Tree();
                currentFile = null;
                //display the new (empty) tree
                displayTree(FamilyTree);
                editFooter("Blank tree created");
            }

        }
    }

    /**
     * Action class that implements ActionListner
     * displays a dialog that enables the user to easily select files or directories
     */
    private class load implements ActionListener {

        @Override
        //Invoked when load tree action occurs
        public void actionPerformed(ActionEvent e) {
            if (unsavedContinue()) {
                JFileChooser jFileChooser = new JFileChooser();
                //set file filters
                jFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Family Tree(*.ftree)", "ftree"));
                jFileChooser.setAcceptAllFileFilterUsed(true);
                
                int result = jFileChooser.showOpenDialog(mainFrame);
                //process jfilechooser result
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        //try to open the file, display the family tree
                        loadFile(jFileChooser.getSelectedFile());
                        displayTree(FamilyTree);
                        editFooter("File opened from: " + (jFileChooser.getSelectedFile().getAbsolutePath()));
                    } catch (Exception j) {
                        //error
                        ErrorDialog(j);
                        editFooter("Error: " + j.getMessage());
                    }
                }
            }

        }
    }

    /**
     * To check if the tree is loaded,and check if the user wants 
     * to continue despite the tree being loaded
     * @return true if the tree is empty or if the user wishes to continue
     */
    private boolean unsavedContinue() {
        if (FamilyTree.checkRootPerson()) {
            int dialogResult = JOptionPane.showConfirmDialog(mainFrame, "Leaving this page will discard unsaved changes. Still want to continue? ", "Warning", JOptionPane.OK_CANCEL_OPTION);
            return dialogResult == JOptionPane.OK_OPTION;
        }
        return true;
    }

    /**
     * Display the family tree
     * @param familyTree family tree of saved data
     */
    private void displayTree(Tree familyTree) {

        //create the root node
        DefaultMutableTreeNode main = new DefaultMutableTreeNode("Main");
        // examining and modifying a node's parent and children and also 
        //operations for examining the tree that the node is a part of 
        TreePath lastSelectedNode = null;

        //mutable tree node allowing objects as nodes
        DefaultMutableTreeNode top;
        
        //no data 
        if (!familyTree.checkRootPerson()) {
            top = new DefaultMutableTreeNode("No tree found.");

        } else {
            //add the root person
            top = new DefaultMutableTreeNode(familyTree.getRootPerson());
            createTree(top, familyTree.getRootPerson());
            lastSelectedNode = tree.getSelectionPath();

        }
        //Create the tree and displays a set of hierarchical data as an outline
        tree = new JTree(main);
        main.add(top);
        //JTree methods use "visible" to mean "displayed"
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setEnabled(true);
        tree.expandPath(new TreePath(main.getPath()));
        tree.getSelectionModel().addTreeSelectionListener(new selectTreeNode());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setBorder(new EmptyBorder(0, 10, 0, 10));

        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        
        //implementing a tree cell renderer that displays custom icons
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            //Sets the value of the current tree cell to value
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object nodeInfo = node.getUserObject();
                if (nodeInfo instanceof Person) {
                    setTextNonSelectionColor(Color.BLACK);
                    setBackgroundSelectionColor(Color.LIGHT_GRAY);
                    setTextSelectionColor(Color.BLACK);
                    setBorderSelectionColor(Color.WHITE);
                } else {
                    setTextNonSelectionColor(Color.GRAY);
                    setBackgroundSelectionColor(Color.WHITE);
                    setTextSelectionColor(Color.GRAY);
                    setBorderSelectionColor(Color.WHITE);
                }
                setLeafIcon(null);
                setClosedIcon(null);
                setOpenIcon(null);
                super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
                return this;
            }
        });

        //Provides a scrollable view for tree
        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setPreferredSize(new Dimension(250, 0));

        //Create detail panel 
        detailPanel = new JPanel();
        detailPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));

        JLabel promptInfo;
        JButton addNewRoot = new JButton("Add root person");
        addNewRoot.addActionListener(new addRelative(null));
        if (!familyTree.checkRootPerson()) {
            promptInfo = new JLabel("<html>Load a tree or add a new root person</html>");
            detailPanel.add(addNewRoot);
        } else {
            promptInfo = new JLabel("<html>Select a family member to view information</html>");
        }

        promptInfo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        detailPanel.add(promptInfo, BorderLayout.NORTH);
        detailPanel.setOpaque(false);

        editPanel.removeAll();

        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        editPanel.add(container);

        container.setLayout(new BorderLayout());
        container.add(treeScrollPane, BorderLayout.WEST);
        container.add(detailPanel, BorderLayout.CENTER);
        
        editPanel.add(container);
        editPanel.validate();
        editPanel.repaint();
 
        tree.setSelectionPath(lastSelectedNode);
    }

    /**
     * Cancel edit action and return to previous action
     */
    private class cancelEdit implements ActionListener {

        Person member;

        public cancelEdit(Person member) {
            this.member = member;
        }

        @Override
        //Invoked when cancel edit action occurs
        public void actionPerformed(ActionEvent e) {
            PersonInfo(member);
            editFooter("Edit action canceled");
        }
    }

    /**
     * Save existing file by prompted messages to user-overwrite saved file or
     * save as a new file if the file doesn't exist
     */
    private class saveAction implements ActionListener {

        @Override
        //Invoked when save action occurs
        public void actionPerformed(ActionEvent e) {
            try {
                if (currentFile != null) {
                    int dialogResult = JOptionPane.showConfirmDialog(mainFrame, "Want to overwrite the current tree?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        //save the file
                        saveToFile(currentFile);
                        editFooter("File saved to: " + currentFile.getPath());
                    }
                } else {
                    editFooter("File not loaded");
                    //save as instead
                    ActionListener listner = new saveAsAction();
                    listner.actionPerformed(e);

                }

            } catch (Exception j) {
                ErrorDialog(j);
                editFooter("Error: "+ j.getMessage());
            }
        }
    }

    /**
     * Save as new file or overwrite a same name file
     */
    private class saveAsAction implements ActionListener {

        @Override
        //Invoked when save as action occurs
        public void actionPerformed(ActionEvent e) {
            JFileChooser jFileChooser = new JFileChooser() {
                //check exist file name, ask to overwrite
                @Override
                public void approveSelection() {
                    File selectedFile = getSelectedFile();
                    if (selectedFile.exists() && getDialogType() == SAVE_DIALOG) {
                        int result = JOptionPane.showConfirmDialog(this, "The file already exists. Do you want to overwrite it?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
                        switch (result) {
                            case JOptionPane.YES_OPTION:
                                super.approveSelection();
                                return;
                            case JOptionPane.NO_OPTION:
                                return;
                            case JOptionPane.CLOSED_OPTION:
                                return;
                            case JOptionPane.CANCEL_OPTION:
                                cancelSelection();
                                return;
                        }
                    }
                    super.approveSelection();
                }
            };
            jFileChooser.setSelectedFile(new File("New Tree.ftree"));
            //Sets the current file filter
            jFileChooser.setFileFilter(new FileNameExtensionFilter("FamilyTree Files (*.ftree)", "ftree"));
            //show save action
            int result = jFileChooser.showSaveDialog(mainFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String filename = jFileChooser.getSelectedFile().toString();
                    if (!filename.endsWith(".ftree")) {
                        filename += ".ftree";
                    }
                    File file = new File(filename);

                    saveToFile(file);
                    displayTree(FamilyTree);
                    editFooter("File saved to: " + (file.getAbsolutePath()));
                } catch (Exception j) {
                    ErrorDialog(j);
                    editFooter("Error: "+ j.getMessage());
                }
            }
        }
    }

    /**
     * When user selects a node from tree
     */
    private class selectTreeNode implements TreeSelectionListener {
        
        /**Called whenever the value of the selection changes
         * 
         * @param event the event that characterizes the change 
         */     
        public void valueChanged(TreeSelectionEvent event) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            //no selection
            if (node == null) {
                return;
            }

            //if the selection is a familymember object
            Object nodeInfo = node.getUserObject();
            if (nodeInfo instanceof Person) {
                //display details
                PersonInfo((Person) nodeInfo);
                editFooter("Information of: " + ((Person) nodeInfo));
            }
        }
    }

    /**
     * Save the object to a file using serialization 
     * @param file file to save
     */
    private void saveToFile(File file) {
        //save to file
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {

            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            //write to file
            out.writeObject(this.FamilyTree);

            out.close();
            currentFile = file;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to save the file");
        }
    }

    /**
     * Open a file and load the data
     * @param file file to open
     */
    private void loadFile(File file) {
        // read from file
        FileInputStream fis = null;
        ObjectInputStream in = null;
        Tree ft = null;
        try {

            fis = new FileInputStream(file);
            in = new ObjectInputStream(fis);

            ft = (Tree) in.readObject();
            in.close();

            FamilyTree.setRootPerson(ft.getRootPerson());
            currentFile = file;
            tree = new JTree();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to determine file format,please select ftree file");
        }

    }

    /**
     * Display family member information 
     * @param member detail of that particular family member
     */
    private void PersonInfo(Person member) {
        tree.setEnabled(true);
        
        detailPanel.removeAll();
        detailPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        detailPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel container = new JPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        detailPanel.add(container, gbc);
        

        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);
        //Sets whether a gap between components should automatically be created.
        layout.setAutoCreateGaps(true);

        JLabel memberInfoLabel = new JLabel("Person Info: ");
        memberInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel nameLabel = new JLabel("Name");
        JLabel nameTextField = new JLabel(member.getFirstName(), 10);
        JLabel lastnameLabel = new JLabel("Surname");
        JLabel lastnameTextField = new JLabel(member.getLastName(), 10);
        JLabel maidennameLabel = new JLabel("Maiden Name");
        JLabel maidennameTextField = new JLabel();
        if (member.has(Person.Relation.MAIDENNAME)) {
            maidennameTextField.setText(member.getMaidenName());
        } else {
            maidennameTextField.setText("-");
        }

        JLabel genderLabel = new JLabel("Gender");
        JLabel genderComboBox = new JLabel(member.getGender().toString());
        
        JLabel lifeDescriptionLabel = new JLabel("Life Description");
        JTextArea lifeDescriptionTextArea = new JTextArea(5, 20);
        lifeDescriptionTextArea.setText(member.getTextDesc());
        lifeDescriptionTextArea.setWrapStyleWord(true);
        lifeDescriptionTextArea.setLineWrap(true);
        lifeDescriptionTextArea.setOpaque(false);
        lifeDescriptionTextArea.setEditable(false);
        lifeDescriptionTextArea.setFocusable(false);
        lifeDescriptionTextArea.setBackground(UIManager.getColor("Label.background"));
        lifeDescriptionTextArea.setFont(UIManager.getFont("Label.font"));
        lifeDescriptionTextArea.setBorder(UIManager.getBorder("Label.border"));
        JScrollPane lifeDescriptionScrollPane1 = new JScrollPane(lifeDescriptionTextArea);
        lifeDescriptionScrollPane1.setBorder(UIManager.getBorder("Label.border"));

        JLabel addressInfoLabel = new JLabel("Address Info: ");
        addressInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel streetNoLabel = new JLabel("Street Number:");
        JLabel streetNoTextField = new JLabel(member.getAddress().getStreetNumber(), 10);
        JLabel streetNameLabel = new JLabel("Street Name:");
        JLabel streetNameTextField = new JLabel(member.getAddress().getStreetName(), 10);
        JLabel suburbLabel = new JLabel("Suburb");
        JLabel suburbTextField = new JLabel(member.getAddress().getSuburb(), 10);
        JLabel postcodeLabel = new JLabel("Postcode");
        JLabel postcodeTextField = new JLabel(member.getAddress().getPostCode() + "", 10);

        JLabel relativeInfoLabel = new JLabel("Relative Info: ");
        relativeInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));

        JLabel fatherLabel = new JLabel("Father");
        JLabel fatherTextField = new JLabel();
        if (member.has(Person.Relation.FATHER)) {
            fatherTextField.setText(member.getFather().toString());
        } else {
            fatherTextField.setText("No record");
        }
        JLabel motherLabel = new JLabel("Mother");
        JLabel motherTextField = new JLabel();
        if (member.has(Person.Relation.MOTHER)) {
            motherTextField.setText(member.getMother().toString());
        } else {
            motherTextField.setText("No record");
        }
        JLabel spouseLabel = new JLabel("Spouse");
        JLabel spouseTextField = new JLabel();
        if (member.has(Person.Relation.SPOUSE)) {
            spouseTextField.setText(member.getSpouse().toString());
        } else {
            spouseTextField.setText("No record");
        }
        JLabel childrenLabel = new JLabel("Children");
        String children = "<html>";
        if (member.has(Person.Relation.CHILDREN)) {
            for (Person child : member.getChildren()) {
                children += child.toString() + "<br>";
            }
            children += "</html>";
        } else {
            children = "No record";
        }
        JLabel childrenTextField = new JLabel(children);

        JLabel grandChildrenLabel = new JLabel("Grand Children");
        String grandChildren = "<html>";
        if (member.has(Person.Relation.CHILDREN)) {
            for (Person child : member.getChildren()) {
                if (child.has(Person.Relation.CHILDREN)) {
                    for (Person grandChild : child.getChildren()) {
                        grandChildren += grandChild.toString() + "<br>";
                    }
                }

            }
            grandChildren += "</html>";
        } else {
            grandChildren = "No record";
        }
        JLabel grandChildrenTextField = new JLabel(grandChildren);

        //Sets the Group that positions and sizes components along the horizontal axis
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(memberInfoLabel)
                        .addComponent(nameLabel)
                        .addComponent(lastnameLabel)
                        .addComponent(maidennameLabel)
                        .addComponent(genderLabel)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(addressInfoLabel)
                        .addComponent(streetNoLabel)
                        .addComponent(streetNameLabel)
                        .addComponent(suburbLabel)
                        .addComponent(postcodeLabel)
                        .addComponent(relativeInfoLabel)
                        .addComponent(fatherLabel)
                        .addComponent(motherLabel)
                        .addComponent(spouseLabel)
                        .addComponent(childrenLabel)
                        .addComponent(grandChildrenLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameTextField)
                        .addComponent(lastnameTextField)
                        .addComponent(maidennameTextField)
                        .addComponent(lifeDescriptionScrollPane1)
                        .addComponent(genderComboBox)
                        .addComponent(addressInfoLabel)
                        .addComponent(streetNoTextField)
                        .addComponent(streetNameTextField)
                        .addComponent(suburbTextField)
                        .addComponent(postcodeTextField)
                        .addComponent(fatherTextField)
                        .addComponent(motherTextField)
                        .addComponent(spouseTextField)
                        .addComponent(childrenTextField)
                        .addComponent(grandChildrenTextField)
                )
        );

        // Sets the Group that positions and sizes components along the vertical axis
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(memberInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(nameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lastnameLabel)
                        .addComponent(lastnameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(maidennameLabel)
                        .addComponent(maidennameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(genderLabel)
                        .addComponent(genderComboBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(lifeDescriptionScrollPane1))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addressInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(streetNoLabel)
                        .addComponent(streetNoTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(streetNameLabel)
                        .addComponent(streetNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(suburbLabel)
                        .addComponent(suburbTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(postcodeLabel)
                        .addComponent(postcodeTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(relativeInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(fatherLabel)
                        .addComponent(fatherTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(motherLabel)
                        .addComponent(motherTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(spouseLabel)
                        .addComponent(spouseTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(childrenLabel)
                        .addComponent(childrenTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(grandChildrenLabel)
                        .addComponent(grandChildrenTextField))
        );

        JButton editMember = new JButton("Edit Details");
        editMember.addActionListener(new editMember(member));
        JButton addRelative = new JButton("Add Relative");
        addRelative.addActionListener(new addRelative(member));

        JPanel btncontainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btncontainer.add(editMember);
        btncontainer.add(addRelative);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        detailPanel.add(btncontainer, gbc);
        detailPanel.validate();
        detailPanel.repaint();
    }

    /**
     * Display edit member panel
     * @param member info of specified member to edit
     */
    private void EditMember(Person member) {
        tree.setEnabled(false);
        
        //reset the info panel
        detailPanel.removeAll();
        detailPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        // Create the layout
        JPanel info = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        detailPanel.add(info, gbc);
        GroupLayout layout = new GroupLayout(info);
        info.setLayout(layout);
        layout.setAutoCreateGaps(true);

        JLabel memberInfoLabel = new JLabel("Person Info: ");
        memberInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel nameLabel = new JLabel("Name");
        JTextField nameTextField = new JTextField(member.getFirstName(), 10);
        JLabel lastnameLabel = new JLabel("Surname");
        JTextField lastnameTextField = new JTextField(member.getLastName(), 10);
        JLabel maidennameLabel = new JLabel("Maiden Name");
        JTextField maidennameTextField = new JTextField(member.getMaidenName(), 10);
        if (member.getGender() != Person.Gender.FEMALE) {
            maidennameTextField.setEditable(false);
        }
        JLabel genderLabel = new JLabel("Gender");
        //Constructs a DefaultComboBoxModel object initialized with person gender
        DefaultComboBoxModel<Person.Gender> genderList = new DefaultComboBoxModel<>();
        genderList.addElement(Person.Gender.FEMALE);
        genderList.addElement(Person.Gender.MALE);
        JComboBox<Person.Gender> genderComboBox = new JComboBox<>(genderList);
        genderComboBox.setSelectedItem(member.getGender());
        genderComboBox.setEnabled(false);

        JLabel lifeDescriptionLabel = new JLabel("Life Description");
        JTextArea lifeDescriptionTextArea = new JTextArea(member.getTextDesc(), 10, 10);
        lifeDescriptionTextArea.setLineWrap(true);
        lifeDescriptionTextArea.setWrapStyleWord(true);
        JScrollPane lifeDescriptionScrollPane1 = new JScrollPane(lifeDescriptionTextArea);
        
        JLabel addressInfoLabel = new JLabel("Address Info: ");
        addressInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel streetNoLabel = new JLabel("Street Number:");
        JTextField streetNoTextField = new JTextField(member.getAddress().getStreetNumber(), 10);
        JLabel streetNameLabel = new JLabel("Street Name:");
        JTextField streetNameTextField = new JTextField(member.getAddress().getStreetName(), 10);
        JLabel suburbLabel = new JLabel("Suburb");
        JTextField suburbTextField = new JTextField(member.getAddress().getSuburb(), 10);
        JLabel postcodeLabel = new JLabel("Postcode");
        JTextField postcodeTextField = new JTextField(member.getAddress().getPostCode() + "", 10);

        //Sets the Group that positions and sizes components along the horizontal axis
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(memberInfoLabel)
                        .addComponent(nameLabel)
                        .addComponent(lastnameLabel)
                        .addComponent(maidennameLabel)
                        .addComponent(genderLabel)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(addressInfoLabel)
                        .addComponent(streetNoLabel)
                        .addComponent(streetNameLabel)
                        .addComponent(suburbLabel)
                        .addComponent(postcodeLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameTextField)
                        .addComponent(lastnameTextField)
                        .addComponent(maidennameTextField)
                        .addComponent(lifeDescriptionScrollPane1)
                        .addComponent(genderComboBox)
                        .addComponent(addressInfoLabel)
                        .addComponent(streetNoTextField)
                        .addComponent(streetNameTextField)
                        .addComponent(suburbTextField)
                        .addComponent(postcodeTextField)
                )
        );

        // Sets the Group that positions and sizes components along the vertical axis
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(memberInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(nameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lastnameLabel)
                        .addComponent(lastnameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(maidennameLabel)
                        .addComponent(maidennameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(genderLabel)
                        .addComponent(genderComboBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(lifeDescriptionScrollPane1))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addressInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(streetNoLabel)
                        .addComponent(streetNoTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(streetNameLabel)
                        .addComponent(streetNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(suburbLabel)
                        .addComponent(suburbTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(postcodeLabel)
                        .addComponent(postcodeTextField))
        );
        JButton saveMember = new JButton("Save Details");
        saveMember.addActionListener(new ActionListener() {
            @Override
            //Invoked when save member action occurs
            public void actionPerformed(ActionEvent e) {
                try {
                    member.setFirstName(nameTextField.getText().trim());
                    member.setLastName(lastnameTextField.getText().trim());
                    member.setMaidenName(maidennameTextField.getText().trim());
                    member.setTextDesc(lifeDescriptionTextArea.getText().trim());
                    member.setGender((Person.Gender) genderComboBox.getSelectedItem());
                    
                    member.getAddress().setStreetNumber(streetNoTextField.getText().trim());
                    member.getAddress().setStreetName(streetNameTextField.getText().trim());
                    member.getAddress().setSuburb(suburbTextField.getText().trim());
                    member.getAddress().setPostCode(postcodeTextField.getText().trim());
                    displayTree(FamilyTree);
                    editFooter("Family Member "+member.toString()+" added");
                } catch (Exception d) {
                    ErrorDialog(d);
                }
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new cancelEdit(member));
        
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        container.setOpaque(false);
        container.add(saveMember);
        container.add(cancel);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        detailPanel.add(container, gbc);

        detailPanel.validate();
        detailPanel.repaint();
    }

    /**
     * Display add relative panel
     * @param member info of specified member to add relative
     */
    private void AddRelativeInfo(Person member) {
        tree.setEnabled(false);
        
        detailPanel.removeAll();
        detailPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel info = new JPanel();
        JLabel memberInfoLabel = new JLabel("Add new root person", SwingConstants.LEFT);
        if (member != null) {
            memberInfoLabel.setText("Add Relative for " + member.toString());
        }

        memberInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));

        detailPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        detailPanel.add(memberInfoLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        detailPanel.add(info, gbc);
        // Create the layout
        GroupLayout layout = new GroupLayout(info);
        info.setLayout(layout);
        layout.setAutoCreateGaps(true);

        JLabel relativeTypeLabel = new JLabel("Relationship");
        DefaultComboBoxModel<Person.ImmRelative> relativeTypeList = new DefaultComboBoxModel<>();

        relativeTypeList.addElement(Person.ImmRelative.MOTHER);
        relativeTypeList.addElement(Person.ImmRelative.FATHER);
        relativeTypeList.addElement(Person.ImmRelative.SPOUSE);
        relativeTypeList.addElement(Person.ImmRelative.CHILD);
        JComboBox<Person.ImmRelative> relativeTypeComboBox = new JComboBox<>(relativeTypeList);
        
        if (member == null) {

            relativeTypeComboBox.removeAllItems();
            relativeTypeComboBox.setEnabled(false);

        }

        JLabel nameLabel = new JLabel("Name");
        JTextField nameTextField = new JTextField("Your First Name", 10);
        JLabel lastnameLabel = new JLabel("Surname");
        JTextField lastnameTextField = new JTextField("Your Surname", 10);

        JLabel maidennameLabel = new JLabel("Maiden Name");
        JTextField maidennameTextField = new JTextField(10);

        JLabel genderLabel = new JLabel("Gender");
        DefaultComboBoxModel<Person.Gender> genderList = new DefaultComboBoxModel<>();
        genderList.addElement(Person.Gender.FEMALE);
        genderList.addElement(Person.Gender.MALE);
        JComboBox<Person.Gender> genderComboBox = new JComboBox<>(genderList);

        JLabel lifeDescriptionLabel = new JLabel("Life Description");
        JTextArea lifeDescriptionTextArea = new JTextArea("Describe yourself...",10, 10);
        lifeDescriptionTextArea.setLineWrap(true);
        lifeDescriptionTextArea.setWrapStyleWord(true);
        JScrollPane lifeDescriptionScrollPane1 = new JScrollPane(lifeDescriptionTextArea);

        JLabel addressInfoLabel = new JLabel("Address Info:");
        addressInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel streetNoLabel = new JLabel("Street Number:");
        JTextField streetNoTextField = new JTextField("80", 10);
        JLabel streetNameLabel = new JLabel("Street Name:");
        JTextField streetNameTextField = new JTextField("Yishun Street", 10);
        JLabel suburbLabel = new JLabel("Suburb");
        JTextField suburbTextField = new JTextField("Singapore", 10);
        JLabel postcodeLabel = new JLabel("Postcode");
        JTextField postcodeTextField = new JTextField("810643", 10);
     
        JButton saveMember = new JButton("Add Member");
        saveMember.addActionListener(new ActionListener() {
            @Override
            //Invoked when save member action occurs
            public void actionPerformed(ActionEvent e) {

                try {
                    Address newAddress = new Address(streetNoTextField.getText(),
                            streetNameTextField.getText(),
                            suburbTextField.getText(),
                            postcodeTextField.getText());
                    Person newMember = new Person(
                            nameTextField.getText(),
                            lastnameTextField.getText(),
                            (Person.Gender) genderComboBox.getSelectedItem(),
                            newAddress,
                            lifeDescriptionTextArea.getText());
                    newMember.setMaidenName(maidennameTextField.getText());

                    if (member == null) {
                        FamilyTree.setRootPerson(newMember);
                        editFooter("Root member added");
                    } else {
                        member.addRelative((Person.ImmRelative) relativeTypeComboBox.getSelectedItem(), newMember);
                        editFooter("New member added");
                    }
                    displayTree(FamilyTree);

                } catch (Exception d) {
                    ErrorDialog(d);
                }
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new cancelEdit(member));

        relativeTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //check for a match
                switch ((Person.ImmRelative) relativeTypeComboBox.getSelectedItem()) {
                    case FATHER:
                        genderComboBox.setSelectedItem(Person.Gender.MALE);
                        maidennameTextField.setEditable(false);
                        lastnameTextField.setText(member.getLastName());
                        break;
                    case MOTHER:
                        genderComboBox.setSelectedItem(Person.Gender.FEMALE);
                        maidennameTextField.setEditable(true);
                        lastnameTextField.setText(member.getLastName());
                        break;
                    case SPOUSE:
                        lastnameTextField.setText(member.getLastName());
                        maidennameTextField.setEditable(true);
                        break;
                    case CHILD:
                        lastnameTextField.setText(member.getLastName());
                        maidennameTextField.setEditable(true);
                        break;
                }
            }
        });
        
        //Sets the Group that positions and sizes components along the horizontal axis
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(relativeTypeLabel)
                        .addComponent(nameLabel)
                        .addComponent(lastnameLabel)
                        .addComponent(maidennameLabel)
                        .addComponent(genderLabel)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(addressInfoLabel)
                        .addComponent(streetNoLabel)
                        .addComponent(streetNameLabel)
                        .addComponent(suburbLabel)
                        .addComponent(postcodeLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameTextField)
                        .addComponent(relativeTypeComboBox)
                        .addComponent(lastnameTextField)
                        .addComponent(maidennameTextField)
                        .addComponent(lifeDescriptionScrollPane1)
                        .addComponent(genderComboBox)
                        .addComponent(addressInfoLabel)
                        .addComponent(streetNoTextField)
                        .addComponent(streetNameTextField)
                        .addComponent(suburbTextField)
                        .addComponent(postcodeTextField)
                )
        );

        // Sets the Group that positions and sizes components along the vertical axis
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(relativeTypeLabel)
                        .addComponent(relativeTypeComboBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(nameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lastnameLabel)
                        .addComponent(lastnameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(maidennameLabel)
                        .addComponent(maidennameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(genderLabel)
                        .addComponent(genderComboBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(lifeDescriptionScrollPane1))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addressInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(streetNoLabel)
                        .addComponent(streetNoTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(streetNameLabel)
                        .addComponent(streetNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(suburbLabel)
                        .addComponent(suburbTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(postcodeLabel)
                        .addComponent(postcodeTextField))
        );

        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        container.setOpaque(false);
        container.add(saveMember);
        container.add(cancel);

        gbc.gridx = 0;
        gbc.gridy = 2;
        detailPanel.add(container, gbc);
        detailPanel.validate();
        detailPanel.repaint();
    }

    /**
     * To populate the jTree object for each family member of the root person using Recursive method
     * @param top Node
     * @param root Member detail
     */
    private void createTree(DefaultMutableTreeNode top, Person root) {
        DefaultMutableTreeNode parents = null;
        DefaultMutableTreeNode father = null;
        DefaultMutableTreeNode mother = null;
        DefaultMutableTreeNode spouse = null;
        DefaultMutableTreeNode children = null;
        DefaultMutableTreeNode child = null;
        DefaultMutableTreeNode spouseNode = null;

        if (root.has(Person.Relation.PARENTS) && root == FamilyTree.getRootPerson()) {
            parents = new DefaultMutableTreeNode("Parents");
            top.add(parents);

            if (root.has(Person.Relation.FATHER)) {
                father = new DefaultMutableTreeNode(root.getFather());
                parents.add(father);
            }

            if (root.has(Person.Relation.MOTHER)) {
                mother = new DefaultMutableTreeNode(root.getMother());
                parents.add(mother);
            }
        }


        if (root.has(Person.Relation.SPOUSE)) {
            spouseNode = new DefaultMutableTreeNode("Spouse");
            spouse = new DefaultMutableTreeNode(root.getSpouse());
            spouseNode.add(spouse);
            top.add(spouseNode);
        }

        if (root.has(Person.Relation.CHILDREN)) {
            children = new DefaultMutableTreeNode("Children");
            for (Person c : root.getChildren()) {
                child = new DefaultMutableTreeNode(c);
                createTree(child, c);
                children.add(child);
            }
            top.add(children);
        }

    }


}
