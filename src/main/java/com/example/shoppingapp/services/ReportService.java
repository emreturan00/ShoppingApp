package com.example.shoppingapp.services;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import com.example.shoppingapp.models.Report;
import com.example.shoppingapp.repository.DatabaseAdapter;

public class ReportService {
    private DatabaseAdapter databaseAdapter;

    public ReportService(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    public List<Report> viewReports() {
        // Implement the logic to fetch and return reports
        return new ArrayList<>();
    }
}
