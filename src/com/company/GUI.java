package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUI extends javax.swing.JFrame{

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

    JToolBar toolBar;

    private List<String[]> ResultSetToArray(ResultSet result) {
        List<String[]> table = new ArrayList<>();
        try {
            int nCol = result.getMetaData().getColumnCount();

            while (result.next()) {
                String[] row = new String[nCol];
                for (int iCol = 1; iCol <= nCol; iCol++) {
                    Object obj = result.getObject(iCol);
                    row[iCol - 1] = (obj == null) ? null : obj.toString();
                }
                table.add(row);
            }

        } catch(Exception e) {

        }

        return table;
    }

    private void cardsSearch() {
        try {
            Connect conn = new Connect();
            conn.connect();
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
                        loadListings(conn, data.get(finalI).cardID);
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

    private void loadListings(Connect conn, String cardID) {
        List<Listing> data = conn.GetListings(cardID);
        System.out.println(data.size());
        listingBox.removeAll();
        for(int i = 0; i < data.size(); i++) {
            JPanel tempPanel = new JPanel();
            Button temp = new Button("Add to cart");
            JLabel tempLabelPrice = new JLabel(data.get(i).price);
            //tempLabelPrice.setOpaque(true);
            tempPanel.add(temp);
            tempPanel.add(tempLabelPrice);
            tempPanel.setPreferredSize(new Dimension(100, 150));
            listingBox.add(tempPanel);
            listingPanel.revalidate();
        }
    }


    public GUI() {

        //Initialize Frame
        setName("Card Database");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));

        getContentPane().setLayout(new BorderLayout(5, 5));


        //Initialize Panels
        splitPane = new JSplitPane();
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        topCenterPanel = new JPanel();
        topLeftPanel = new JPanel();
        topRightPanel = new JPanel();

        //Setup Panels
        topLeftPanel.setPreferredSize(new Dimension(200, 410));
        topRightPanel.setPreferredSize(new Dimension(200, 410));
        bottomPanel.setBackground(Color.GRAY);
        bottomPanel.setPreferredSize(new Dimension(200, 100));

        // Setup BorderLayout
        topPanel.setLayout(new BorderLayout(5, 5));
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topCenterPanel, BorderLayout.CENTER);
        topPanel.add(topRightPanel, BorderLayout.EAST);
        getContentPane().add(topPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        //Setup Bottom Layout
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
        toolBar.add(new JButton("Toolbar 1"));
        toolBar.add(new JButton("Toolbar 2"));
        toolBar.setOrientation(JToolBar.HORIZONTAL);
        getContentPane().add(toolBar, BorderLayout.NORTH);

        //Setup Listing Panel
        listingBox = new Box(BoxLayout.Y_AXIS);
        listingPanel = new JScrollPane(listingBox);
        topRightPanel.add(listingPanel);
        listingPanel.setPreferredSize(new Dimension(topRightPanel.getPreferredSize().width, topRightPanel.getPreferredSize().height));

        // Pack GUI and center
        pack();
        setLocationRelativeTo(null);
    }
}
