package com.example;

import java.io.IOException;
import java.sql.SQLException;

public class SchemaImporter {

    public static void main(String[] args) throws IOException, SQLException {

        var importHelper = new ImportHelper();
        importHelper.setupFreshDb();

        System.out.println("Schema imported successfully!");
    }
}
