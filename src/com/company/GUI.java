package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUI extends javax.swing.JFrame {



    //Basic Database Info
    private String currentUser = "username0";
    private String currentCart = "cart0";
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

    private void cardsSearch() {
        try {
            List<Card> data = conn.GetCards(searchBar.getText());
            // Loop through all rows
            box.removeAll();
            for (int i = 0; i < data.size(); i++) {
                //searchPanel.removeAll();
                System.out.println(data.get(i).cardName);
                Button temp = new Button(data.get(i).cardName);
                int finalI = i;
                temp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        loadCard(data.get(finalI));
                        loadListings(data.get(finalI).cardID);
                    }
                });
                temp.setPreferredSize(new Dimension(100, 150));
                box.add(temp);
                searchPanel.revalidate();
                //pack();
            }

        } catch (Exception exc) {System.out.println("You suck");}
    }

    private void loadCard(Card card) {
        centerPanel.removeAll();
        System.out.println(card.cardName);
        JLabel label = new JLabel(card.cardName);
        JTextArea desc = new JTextArea(card.text);
        desc.setPreferredSize(new Dimension(300, 90));
        desc.setLineWrap(true);
        label.setSize(200, 40);
        label.setOpaque(true);
        centerPanel.add(label);
        centerPanel.add(desc);
        centerPanel.revalidate();
    }

    private void setCart() {
        List<CartItem> data = conn.GetCart(currentUser);
        System.out.println("Items in cart: " + data.size());
        cartBox.removeAll();
        for(int i = 0; i < data.size(); i++) {
            JButton bt = new JButton(data.get(i).price);
            cartBox.add(bt);
        }
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
        System.out.println(data.size());
        listingBox.removeAll();
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
            tempPanel.setPreferredSize(new Dimension(100, 150));
            listingBox.add(tempPanel);

        }
        repaint();
    }






    public GUI() {
        conn = new Connect();

        conn.connect();



        //Initialize Main Frames
        menuBar = new JMenuBar();
        menu = new JMenu("User");
        menu.setMnemonic(KeyEvent.VK_A);

        menu.getAccessibleContext().setAccessibleDescription("Menu");
        menuBar.add(menu);

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
        cartScrollPane.setPreferredSize(new Dimension(c.getPreferredSize().width, c.getPreferredSize().height));
        cartScrollPane.setBackground(Color.GRAY);

        cartFrame.add(cartScrollPane);

        //Setup Menu
        //a group of JMenuItems
        menuItem = new JMenuItem("Change user");
        //menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        //menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menu.add(menuItem);

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

        // Pack GUI and center
        pack();
        setLocationRelativeTo(null);
    }
}
