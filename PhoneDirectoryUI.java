import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class PhoneDirectoryUI extends JFrame {

    private Map<String, java.util.List<Contact>> contacts = new TreeMap<>(String.CASE_INSENSITIVE_ORDER); // TreeMap with case-insensitive comparator

    private JTextField nameField, phoneNumberField, addressField, emailField, dobField, searchField;
    private JTextArea outputArea;

    public PhoneDirectoryUI() {
        setTitle("Telephone Directory System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridy++;
        inputPanel.add(new JLabel("Phone Number (10 digits):"), gbc);
        gbc.gridy++;
        inputPanel.add(new JLabel("Address:"), gbc);
        gbc.gridy++;
        inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        inputPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(nameField, gbc);
        gbc.gridy++;
        phoneNumberField = new JTextField();
        phoneNumberField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(phoneNumberField, gbc);
        gbc.gridy++;
        addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(addressField, gbc);
        gbc.gridy++;
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(emailField, gbc);
        gbc.gridy++;
        dobField = new JTextField();
        dobField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(dobField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LINE_START;
        JButton insertButton = new JButton("Insert Contact");
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertContact();
            }
        });
        inputPanel.add(insertButton, gbc);

        gbc.gridy++;
        JButton editButton = new JButton("Edit Contact");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editContact();
            }
        });
        inputPanel.add(editButton, gbc);

        gbc.gridy++;
        JButton showAllButton = new JButton("Show All Contacts");
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllContacts();
            }
        });
        inputPanel.add(showAllButton, gbc);

        add(inputPanel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search/Delete"));
        GridBagConstraints gbcSearch = new GridBagConstraints();
        gbcSearch.gridx = 0;
        gbcSearch.gridy = 0;
        gbcSearch.insets = new Insets(5, 5, 5, 5);
        searchPanel.add(new JLabel("Search:"), gbcSearch);
        gbcSearch.gridx = 1;
        gbcSearch.weightx = 1;
        gbcSearch.fill = GridBagConstraints.HORIZONTAL;
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(150, 25));
        searchPanel.add(searchField, gbcSearch);
        gbcSearch.gridx = 2;
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchContact();
            }
        });
        searchPanel.add(searchButton, gbcSearch);

        gbcSearch.gridy++;
        gbcSearch.gridx = 0;
        gbcSearch.gridwidth = 3;
        gbcSearch.fill = GridBagConstraints.NONE;
        JButton deleteButton = new JButton("Delete Contact");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContact();
            }
        });
        searchPanel.add(deleteButton, gbcSearch);

        gbcSearch.gridy++;
        JButton viewDuplicatesButton = new JButton("View Duplicate Contacts");
        viewDuplicatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewDuplicateContacts();
            }
        });
        searchPanel.add(viewDuplicatesButton, gbcSearch);

        add(searchPanel, BorderLayout.EAST);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void insertContact() {
        String name = nameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String dob = dobField.getText();

        if (phoneNumber.length() != 10) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits.", "Invalid Phone Number", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Date of Birth must be in the format YYYY-MM-DD.", "Invalid Date of Birth", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!contacts.containsKey(name)) {
            contacts.put(name, new ArrayList<>());
        }

        Contact contact = new Contact(name, phoneNumber, address, email, dob);
        contacts.get(name).add(contact); // Store contact using name as key
        outputArea.append("Contact inserted successfully with name: " + name + "\n");
        clearFields();
    }

    private void editContact() {
        String nameToEdit = JOptionPane.showInputDialog("Enter Contact Name to edit:");
        if (nameToEdit != null && contacts.containsKey(nameToEdit)) {
            Contact contact = contacts.get(nameToEdit).get(0); // Get the first occurrence of contact with the given name
            String newName = JOptionPane.showInputDialog("Enter new name (leave empty to keep current):", contact.getName());
            if (!newName.isEmpty()) {
                contacts.remove(nameToEdit);
                contacts.put(newName, new ArrayList<>(Collections.singletonList(contact)));
            }
            String newPhoneNumber = JOptionPane.showInputDialog("Enter new phone number (leave empty to keep current):", contact.getPhoneNumber());
            if (!newPhoneNumber.isEmpty()) {
                contact.setPhoneNumber(newPhoneNumber);
            }
            String newAddress = JOptionPane.showInputDialog("Enter new address (leave empty to keep current):", contact.getAddress());
            if (!newAddress.isEmpty()) {
                contact.setAddress(newAddress);
            }
            String newEmail = JOptionPane.showInputDialog("Enter new email (leave empty to keep current):", contact.getEmail());
            if (!newEmail.isEmpty()) {
                contact.setEmail(newEmail);
            }
            String newDob = JOptionPane.showInputDialog("Enter new date of birth (YYYY-MM-DD) (leave empty to keep current):", contact.getDob());
            if (!newDob.isEmpty()) {
                if (!newDob.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Date of Birth must be in the format YYYY-MM-DD.", "Invalid Date of Birth", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                contact.setDob(newDob);
            }
            outputArea.append("Contact with name " + nameToEdit + " edited successfully.\n");
        } else {
            outputArea.append("Contact with name " + nameToEdit + " not found.\n");
        }
    }

    private void deleteContact() {
        String nameToDelete = JOptionPane.showInputDialog("Enter Contact Name to delete:");
        if (nameToDelete != null && contacts.containsKey(nameToDelete)) {
            contacts.remove(nameToDelete);
            outputArea.append("Contact with name " + nameToDelete + " deleted successfully.\n");
        } else {
            outputArea.append("Contact with name " + nameToDelete + " not found.\n");
        }
    }

    private void searchContact() {
        String searchTerm = searchField.getText().toLowerCase();
        boolean found = false;

        for (Map.Entry<String, java.util.List<Contact>> entry : contacts.entrySet()) {
            String name = entry.getKey();
            java.util.List<Contact> contactList = entry.getValue();
            for (Contact contact : contactList) {
                
                if (contact.getName().toLowerCase().contains(searchTerm) ||
                        contact.getPhoneNumber().toLowerCase().contains(searchTerm) ||
                        contact.getEmail().toLowerCase().contains(searchTerm) ||
                        contact.getDob().toLowerCase().contains(searchTerm)) {
                    outputArea.append("Contact Found:\n");
                    outputArea.append("Name: " + contact.getName() + "\n");
                    outputArea.append("Phone Number: " + contact.getPhoneNumber() + "\n");
                    outputArea.append("Address: " + contact.getAddress() + "\n");
                    outputArea.append("Email: " + contact.getEmail() + "\n");
                    outputArea.append("Date of Birth: " + contact.getDob() + "\n");
                    found = true;
                }
            }
        }

        if (!found) {
            outputArea.append("Contact with search term " + searchTerm + " not found.\n");
        }

        searchField.setText("");
    }

    private void viewDuplicateContacts() {
        boolean foundDuplicates = false;

        for (Map.Entry<String, java.util.List<Contact>> entry : contacts.entrySet()) {
            String name = entry.getKey();
            java.util.List<Contact> contactList = entry.getValue();
            if (contactList.size() > 1) {
                outputArea.append("Duplicate Contacts for " + name + ":\n");
                for (Contact contact : contactList) {
                    outputArea.append("Name: " + contact.getName() + "\n");
                    outputArea.append("Phone Number: " + contact.getPhoneNumber() + "\n");
                    outputArea.append("Address: " + contact.getAddress() + "\n");
                    outputArea.append("Email: " + contact.getEmail() + "\n");
                    outputArea.append("Date of Birth: " + contact.getDob() + "\n");
                    outputArea.append("----------------------------------------\n");
                }
                foundDuplicates = true;
            }
        }

        if (!foundDuplicates) {
            outputArea.append("No duplicate contacts found.\n");
        }
    }

    private void showAllContacts() {
        if (contacts.isEmpty()) {
            outputArea.append("No contacts available.\n");
            return;
        }

        outputArea.append("All Contacts:\n");
        for (Map.Entry<String, java.util.List<Contact>> entry : contacts.entrySet()) {
            java.util.List<Contact> contactList = entry.getValue();
            for (Contact contact : contactList) {
                outputArea.append("Name: " + contact.getName() + "\n");
                outputArea.append("Phone Number: " + contact.getPhoneNumber() + "\n");
                outputArea.append("Address: " + contact.getAddress() + "\n");
                outputArea.append("Email: " + contact.getEmail() + "\n");
                outputArea.append("Date of Birth: " + contact.getDob() + "\n\n");
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneNumberField.setText("");
        addressField.setText("");
        emailField.setText("");
        dobField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PhoneDirectoryUI().setVisible(true);
            }
        });
    }
}

class Contact {
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    private String dob;

    public Contact(String name, String phoneNumber, String address, String email, String dob) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}














