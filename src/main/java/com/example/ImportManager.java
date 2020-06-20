package com.example;

import com.example.entity.Block;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImportManager {

    public static void main(String[] args) throws IOException, SQLException, ExecutionException, InterruptedException {

        var isFreshSetup = false;
        var start = 0;
        var incrementBy = 10;

        var importHelper = new ImportHelper();

        if (isFreshSetup) {
            importHelper.setupFreshDb();
            start = -9;
        } else {
            start = importHelper.getLastScannedBlockNumber();
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
