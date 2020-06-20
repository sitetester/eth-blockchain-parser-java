package com.example;

import com.example.entity.Block;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImportManager {

    public static void main(String[] args) throws SQLException, ExecutionException, InterruptedException {

        var incrementBy = 100;

        var importHelper = new ImportHelper();
        var start = importHelper.getLastScannedBlockNumber();
        if (start == 0) {
            start = -99;
        }

        while (true) {
            start += incrementBy;

            List<Integer> blockNumbers = IntStream.range(start, start + incrementBy).boxed().collect(Collectors.toList());
            List<Block> blocks = new EthBlocksParser().startParsing(blockNumbers);

            blocks.parallelStream().forEach(block -> {
                try {
                    importHelper.addBlock(block);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });

            System.out.print(" Done\n");

            // this should avoid `java.lang.InternalError: java.io.IOException: Too many open files` error
            Thread.sleep(5000);
        }

    }
}
