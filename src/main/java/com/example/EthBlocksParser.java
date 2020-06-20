package com.example;

import com.example.client.InfuraEthClient;
import com.example.entity.Block;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EthBlocksParser {

    public List<Block> startParsing(List<Integer> blockNumbers) throws ExecutionException, InterruptedException {

        var str = String.format("Parsing blocks: %d - %d...", blockNumbers.get(0), blockNumbers.get(blockNumbers.size() -1));
        System.out.print(str);

        var client = new InfuraEthClient(Dotenv.load().get("INFURA_PROJECT_ID"));

        // download all the blocks asynchronously
        List<CompletableFuture<Block>> blockNumbersFutures = blockNumbers.stream()
                .map(client::getBlockByNumber)
                .collect(Collectors.toList());


        // create a combined Future using allOf()
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                blockNumbersFutures.toArray(new CompletableFuture[0])
        );

        // when all the Futures are completed, call `future.join()` to get their results and collect the results in a list -
        CompletableFuture<List<Block>> allBlockNumbersFuture = allFutures.thenApply(v -> blockNumbersFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));

        return allBlockNumbersFuture.get();

    }
}
