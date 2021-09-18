package com.tutorials.database;

import com.tutorials.model.Airline;
import com.tutorials.model.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvoiceDatabase implements InvoiceDAO {


    public static final String NEW_INVOICE = "INSERT INTO invoices " +
            "(airline_name, date, flight_number, reg_number, uplift_liters, uplift_kg, price, total_price )" +
            " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String QUERY_INVOICES = "SELECT * FROM invoices";
    public static final String QUERY_INVOICES_BY_AIRLINE = "SELECT * FROM invoices WHERE airline_name = ?";
    public static final String QUERY_INVOICES_BY_INVOICE_NUMBER = "SELECT * FROM invoices WHERE invoice_number = ?";


    private PreparedStatement newInvoice;
    private PreparedStatement queryInvoicesByAirline;
    private PreparedStatement queryInvoicesByInvNumber;
    private PreparedStatement queryInvoicesByDatePeriod;            // needs to be implemented
    private PreparedStatement queryInvoicesByDatePeriodAndAirline;  // needs to be implemented


    @Override
    public List<Invoice> listAllInvoices() {

        List<Invoice> invoices = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(QUERY_INVOICES)) {

            while (results.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceNumber(results.getInt(1));
                invoice.setAirline(results.getString(2));
                invoice.setDate(results.getString(3));
                invoice.setFlightNumber(results.getString(4));
                invoice.setRegistration(results.getString(5));
                invoice.setUpliftLiters(results.getInt(6));
                invoice.setUpliftInKg(results.getDouble(7));
                invoice.setPrice(results.getDouble(8));
                invoice.setTotalPrice(results.getDouble(9));

                invoices.add(invoice);
            }

        } catch (SQLException e) {
            System.out.println("Cant execute query " + e.getMessage());
            return Collections.emptyList();
        }
        return invoices;
    }

    @Override
    public List<Invoice> listInvoicesByAirline(String airlineName) {

        List<Invoice> invoices = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement queryInvoicesByAirline = conn.prepareStatement(QUERY_INVOICES_BY_AIRLINE) ) {

            queryInvoicesByAirline.setString(1, airlineName );
            ResultSet results = queryInvoicesByAirline.executeQuery();

            while (results.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceNumber(results.getInt(1));
                invoice.setAirline(results.getString(2));
                invoice.setDate(results.getString(3));
                invoice.setFlightNumber(results.getString(4));
                invoice.setRegistration(results.getString(5));
                invoice.setUpliftLiters(results.getInt(6));
                invoice.setUpliftInKg(results.getDouble(7));
                invoice.setPrice(results.getDouble(8));
                invoice.setTotalPrice(results.getDouble(9));

                invoices.add(invoice);
            }
            results.close();

        } catch (SQLException e) {
            System.out.println("Cant execute query - List invoices by airline " + e.getMessage());
            return Collections.emptyList();
        }
        return invoices;
    }

    @Override
    public Invoice findInvoiceByNum(int invoiceNumber) {
        return null;
    }

    @Override
    public int newInvoice(Invoice invoice) throws SQLException {

        if (!isInDatabase(invoice.getInvoiceNumber())){
            try {
                newInvoice.setString(1, invoice.getAirline());
                newInvoice.setString(2, invoice.getDate());
                newInvoice.setString(3, invoice.getFlightNumber());
                newInvoice.setString(4, invoice.getRegistration());
                newInvoice.setInt(5, invoice.getUpliftLiters());
                newInvoice.setDouble(6, invoice.getUpliftInKg());
                newInvoice.setDouble(7, 340);
                newInvoice.setDouble(8, 500);

                int update = newInvoice.executeUpdate();
                return update;

            } catch (SQLException e) {
                System.out.println("Cant execute query " + e.getMessage());
            }
        }
        return 0;
    }

    @Override
    public boolean updateInvoice(Invoice invoice) {
        return false;
    }

    @Override
    public boolean deleteInvoice(Invoice invoice) {
        return false;
    }

    @Override
    public boolean isInDatabase(Invoice invoice) {
        try {
            queryInvoicesByInvNumber.setInt(1, invoice.getInvoiceNumber());
            ResultSet results = queryInvoicesByInvNumber.executeQuery();

            if (results.next()){
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Cant execute query" + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isInDatabase(int invNumber) {
        try {
            queryInvoicesByInvNumber.setInt(1, invNumber);
            ResultSet results = queryInvoicesByInvNumber.executeQuery();

            if (results.next()){
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Cant execute query" + e.getMessage());
        }
        return false;
    }
}
