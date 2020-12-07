package com.company;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUI extends javax.swing.JFrame {

    //Basic Database Info
    private String currentUser = "username0";
    private String currentCart = "cart0";
    private String currentCard = "card0";
    private Connect conn;

    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem;

    //Main Frames
    Container c;
    Container content;
    private CardLayout cl;
    private JPanel mainFrame;
    private JPanel cartFrame;


    // Create Splits
    private JSplitPane splitPane;

    // Create Panels
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel topCenterPanel;
    private JPanel topLeftPanel;
    private JPanel topRightPanel;

    // Create Bottom Components
    private JTextField searchBar;
    private JButton searchButton;
    private JLabel searchLabel;

    // Top Left Components
    private JScrollPane searchPanel;
    private Box box;

    // Top Right Components
    private JScrollPane listingPanel;
    private Box listingBox;

    // Top Center Components
    private Box centerPanel;

    //Toolbar
    private JToolBar toolBar;
    private JButton tbMainButton;
    private JButton tbCartButton;

    // Create Cart Components
    private JScrollPane cartScrollPane;
    private Box cartBox;

    private JPanel cardSearchEntry(Card info) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(0, 25));
        //card.setMaximumSize(new Dimension(150, 150));
        card.setSize(new Dimension(150, 150));
        //card.setPreferredSize(new Dimension(100 ,150));
        JLabel label = new JLabel(info.cardName);
        label.setHorizontalAlignment(JLabel.CENTER);
        JButton button = new JButton("View");
        button.addActionListener(e -> {
            loadCard(info);
            loadListings(info.cardID);
        });
        button.setPreferredSize(new Dimension(50, 20));
        card.add(label, BorderLayout.NORTH);
        card.add(button, BorderLayout.CENTER);
        return card;
    }

    private void cardsSearch() {
        try {
            List<Card> data = conn.GetCards(searchBar.getText());
            // Loop through all rows
            box.removeAll();
            if (data.size() != 0) {
                for (int i = 0; i < data.size(); i++) {
                    System.out.println("Card Found: " + data.get(i).cardName);
                    box.add(cardSearchEntry(data.get(i)));
                    JPanel spacer = new JPanel();
                    spacer.setSize(150, 20);
                    spacer.setBackground(Color.GRAY);
                    box.add(spacer);
                    searchPanel.revalidate();
                }
            } else {
                JTextArea label = new JTextArea("\n Search returned no results.");
                box.add(label);
                searchPanel.revalidate();
            }

        } catch (Exception exc) {System.out.println("You suck");}
    }


    private void switchMainPanel(int activePanel) {
        if (activePanel == 1) {
            cl.show(content, "Main");
        } else if (activePanel == 2) {
            cl.show(content, "Cart");
            setCart();
        }
        repaint();
    }

    private void loadListings(String cardID) {
        List<Listing> data = conn.GetListings(cardID);
        listingBox.removeAll();
        JLabel label = new JLabel("Listings:");
        listingBox.add(label);
        for(int i = 0; i < data.size(); i++) {
            JPanel tempPanel = new JPanel();
            Button temp = new Button("Add to cart");
            int finalI = i;
            temp.addActionListener(e -> {
                conn.Add2Cart(data.get(finalI).id, currentCart, data.get(finalI).cardID, 1);
                loadListings(data.get(finalI).cardID);
                switchMainPanel(2);
            });
            JLabel tempLabelPrice = new JLabel(data.get(i).price);
            //tempLabelPrice.setOpaque(true);
            tempPanel.add(temp);
            tempPanel.add(tempLabelPrice);
            tempPanel.setPreferredSize(new Dimension(100,0));
            listingBox.add(tempPanel);

        }
        repaint();
        pack();
    }

    private JPanel loadCartItem(CartItem item) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(0, 25));
        card.setSize(new Dimension(150, 150));
        JLabel label = new JLabel(item.listingID);
        label.setHorizontalAlignment(JLabel.CENTER);
        JButton button = new JButton("Remove");
        button.addActionListener(e -> {
            conn.removeCart(item.listingID);
            loadListings(item.cartID);
            setCart();
        });
        button.setPreferredSize(new Dimension(50, 20));
        card.add(label, BorderLayout.NORTH);
        card.add(button, BorderLayout.CENTER);
        return card;
    }



    private void loadCard(Card card) {
        currentCard = card.cardID;
        centerPanel.removeAll();
        System.out.println(card.cardName);
        TitledBorder outline = new TitledBorder(card.cardName + " {" + card.cmc + "}");
        JTextArea oracleText;
        JTextArea typeLine;
        JTextArea pt = new JTextArea("");
        oracleText = new JTextArea(card.text);
        oracleText.setPreferredSize(new Dimension(300, 90));
        oracleText.setLineWrap(true);
        if (card.type != "None") {
            typeLine = new JTextArea(card.type.replaceAll("(.)([A-Z])", "$1 $2") + " — " + card.subtype.replaceAll("(.)([A-Z])", "$1 $2") + " {" + card.set.toUpperCase() + "|" + card.number + "}");
        } else {
            typeLine = new JTextArea(card.type.replaceAll("(.)([A-Z])", "$1 $2") + " {" + card.set + "|" + card.number + "}");
        }

        typeLine.setPreferredSize(new Dimension(300,30));

        centerPanel.add(typeLine);
        centerPanel.add(oracleText);
        if (card.type.contains("Creature")) {
            pt = new JTextArea("Power/Toughness: [" + card.power + "/" + card.toughness + "]");
            pt.setPreferredSize(new Dimension(300,40));
        }
        centerPanel.add(pt);
        centerPanel.setBorder(outline);
        centerPanel.revalidate();
        pack();
    }

    private void setCart() {
        List<CartItem> data = conn.GetCart(currentUser);
        System.out.println("Items in cart: " + data.size());
        cartBox.removeAll();
        for(int i = 0; i < data.size(); i++) {
            cartBox.add(loadCartItem(data.get(i)));
            JPanel spacer = new JPanel();
            spacer.setSize(150, 20);
            spacer.setBackground(Color.GRAY);
            cartBox.add(spacer);
        }
    }


    public GUI() {
        conn = new Connect();
        conn.connect();



        //Initialize Main Frames
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        JMenu cardMenu = new JMenu("Card");
        menu.setMnemonic(KeyEvent.VK_A);

        menu.getAccessibleContext().setAccessibleDescription("Menu");
        menuBar.add(menu);
        menuBar.add(cardMenu);

        setJMenuBar(menuBar);

        content = new Container();
        c = getContentPane();
        cl = new CardLayout();

        mainFrame = new JPanel();
        cartFrame = new JPanel();

        content.setLayout(cl);

        c.setLayout(new BorderLayout());
        c.add(content, BorderLayout.CENTER);

        content.add("Main", mainFrame);
        content.add("Cart", cartFrame);

        // Initialize Frame
        setTitle("Card Marketplace");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));

        mainFrame.setLayout(new BorderLayout(5, 5));


        // Initialize Panels
        splitPane = new JSplitPane();
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        topCenterPanel = new JPanel();
        topLeftPanel = new JPanel();
        topRightPanel = new JPanel();

        // Setup Panels
        topLeftPanel.setPreferredSize(new Dimension(200, 410));
        topRightPanel.setPreferredSize(new Dimension(200, 410));
        bottomPanel.setBackground(Color.GRAY);
        bottomPanel.setPreferredSize(new Dimension(200, 100));

        // Setup BorderLayout
        topPanel.setLayout(new BorderLayout(5, 5));
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topCenterPanel, BorderLayout.CENTER);
        topPanel.add(topRightPanel, BorderLayout.EAST);
        mainFrame.add(topPanel, BorderLayout.CENTER);
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Setup Bottom Layout
        searchLabel = new JLabel("Search:");
        searchBar = new JTextField();
        searchBar.setPreferredSize(new Dimension(200, 25));
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardsSearch();
            }
        });

        searchBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardsSearch();
            }
        });

        bottomPanel.add(searchLabel);
        bottomPanel.add(searchBar);
        bottomPanel.add(searchButton);

        // Setup Search Panel
        box = new Box(BoxLayout.Y_AXIS);
        searchPanel = new JScrollPane(box);
        topLeftPanel.add(searchPanel);
        searchPanel.setPreferredSize(new Dimension(topLeftPanel.getPreferredSize().width, topLeftPanel.getPreferredSize().height));

        centerPanel = new Box(BoxLayout.Y_AXIS);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topCenterPanel.add(centerPanel);

        // Setup Toolbar
        toolBar = new JToolBar("TaskBar", JToolBar.HORIZONTAL);
        toolBar.setLayout(new GridLayout(1, 12));
        toolBar.setSize(getWidth(), 30);
        toolBar.setFloatable(false);
        JButton tbMainButton = new JButton("Search Listings");


        tbMainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchMainPanel(1);
            }
        });
        JButton tbCartButton = new JButton("View Cart");
        tbCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchMainPanel(2);
            }
        });
        toolBar.add(tbMainButton);
        toolBar.add(tbCartButton);
        toolBar.setOrientation(JToolBar.HORIZONTAL);
        c.add(toolBar, BorderLayout.NORTH);

        //Setup Listing Panel
        listingBox = new Box(BoxLayout.Y_AXIS);
        listingPanel = new JScrollPane(listingBox);
        topRightPanel.add(listingPanel);
        listingPanel.setPreferredSize(new Dimension(topRightPanel.getPreferredSize().width, topRightPanel.getPreferredSize().height));


        // Setup Cart Panel
        cartBox = new Box(BoxLayout.Y_AXIS);
        cartScrollPane = new JScrollPane(cartBox);
        cartScrollPane.setPreferredSize(new Dimension(c.getPreferredSize().width, 500));
        cartScrollPane.setBackground(Color.GRAY);
        cartFrame.add(cartScrollPane);
        JButton buyButton = new JButton("Buy All");
        cartFrame.add(buyButton);

        buyButton.addActionListener(e -> {
            conn.BuyItems(currentUser);
            setCart();
        });

        //Setup Menu
        //a group of JMenuItems
        menuItem = new JMenuItem("Change user");
        JMenuItem menuItem2 = new JMenuItem("Select db");
        menu.add(menuItem);
        menu.add(menuItem2);
        menuItem.addActionListener(e -> {
            String response = JOptionPane.showInputDialog(c, "What user would you like to manage?", null);
            System.out.println("Response: " + response);
            if (response == null) {
                System.out.println("No username entered");
                return;
            };
            User user = conn.GetUser(response);
            if (user.username == null) {
                JOptionPane.showInternalMessageDialog(c, "Username not found");
                System.out.println("Username not found");
                return;
            }
            System.out.println("User changed to: " + user.username);
            currentUser = response;
            currentCart = user.cartID;
            setCart();
            cartBox.revalidate();
        });
        menuItem2.addActionListener(e -> {
            File workingDirectory = new File(System.getProperty("user.dir"));
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(workingDirectory);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "SQLite Database", "db");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(c);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: " +
                        chooser.getSelectedFile().getName());
                System.out.println(chooser.getSelectedFile().getAbsolutePath());
            }
            conn = new Connect(chooser.getSelectedFile().getAbsolutePath());
        });

        // Setup Card Menu
        JMenuItem cardMenuItem = new JMenuItem("Add Listing");
        cardMenu.add(cardMenuItem);

        cardMenuItem.addActionListener(e -> {
            String response = JOptionPane.showInputDialog(c, "What is the price of the new listing?", null);
            System.out.println(response);
            if (response != null && response.matches("^\\$?(?:(?:\\d+(?:,\\d+)?(?:\\.\\d+)?)|(?:\\.\\d+))$")) {
                conn.CreateListing(currentUser, currentCard, response, "Near Mint");
                loadListings(currentCard);
            }
        });

        // Pack GUI and center
        pack();
        setLocationRelativeTo(null);
    }
}
