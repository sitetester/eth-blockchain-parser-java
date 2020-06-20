package com.example;

import com.example.entity.Block;
import com.example.entity.Transaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ImportHelper {

    public final String DB_NAME = "blockchain.db";
    public boolean isFreshSetup = true;


    public void setupFreshDb() throws IOException, SQLException {

        Files.deleteIfExists(Paths.get(DB_NAME));

        var conn = getConnection();
        File file = new File(
                Objects.requireNonNull(ImportHelper.class.getClassLoader().getResource("db/schema.sql")).getFile()
        );

        var sql = Files.readString(Paths.get(file.getPath()));
        var stmt = conn.createStatement();
        stmt.executeUpdate(sql);
    }

    public Connection getConnection() {
        Connection conn = null;

        try {
            String url = "jdbc:sqlite:" + DB_NAME;
            conn = DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public int getLastScannedBlockNumber() throws SQLException {

        var stmt = getConnection().createStatement();

        var sql = "SELECT max(number) FROM `blocks` LIMIT 1";
        var rs = stmt.executeQuery(sql);

        return rs.getInt(1);
    }

    void addBlock(Block block) throws SQLException {

        var stmt = getConnection().createStatement();

        var sql = "\nINSERT INTO blocks (`number`, `hash`, `difficulty`, `transactionsCount`) VALUES \n";
        sql += blockToRow(block);
        stmt.executeUpdate(sql);

        if (block.getTransactions().length > 0) {
            addBlockTransactions(block.getTransactions());
        }
    }

    private void addBlockTransactions(Transaction[] transactions) throws SQLException {

        var sql = "\nINSERT INTO transactions (`hash`, `blockNumber`, `from`, `to`) VALUES \n";
        sql += transactionsToRows(transactions, transactions.length);
        System.out.println(sql);

        var stmt = getConnection().createStatement();
        stmt.executeUpdate(sql);
    }

    private String blockToRow(Block block) {

        var row = "(";
        row += hexToInt(block.getNumber()) + ", ";
        row += "\"" + block.getHash() + "\", ";
        row += "\"" + block.getDifficulty() + "\", ";
        row += block.getTransactions().length;
        row += ")";

        return row;
    }

    private String transactionsToRows(Transaction[] transactions, int maxSize) {

        AtomicInteger counter = new AtomicInteger();
        List<String> rows = List.of(transactions).stream()
                .map((transaction) -> {
                    counter.addAndGet(1);

                    var row = "(";
                    row += "\"" + transaction.getHash() + "\", ";
                    row += "\"" + hexToInt(transaction.getBlockNumber()) + "\", ";
                    row += "\"" + transaction.getFrom() + "\", ";
                    row += "\"" + transaction.getTo() + "\"";
                    row += ")";

                    if (counter.get() < maxSize) {
                        row += ",";
                    }

                    return row;
                })
                .collect(Collectors.toList());


        return String.join("\n", rows);
    }


    private int hexToInt(String hex) {
        return Integer.parseInt(hex.substring(2), 16);
    }

}
