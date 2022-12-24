package com.tzachi.workers;

import com.tzachi.RuleEvaluatorException;
import com.tzachi.common.Constants.Evaluator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class WriterResultWorker extends Thread {

    private final BlockingQueue<String> matchFactsQueue;
    @Getter
    File file;

    public WriterResultWorker(BlockingQueue<String> matchFactsQueue) {
        this.matchFactsQueue = matchFactsQueue;
        file = new File(System.currentTimeMillis() + ".txt");
    }

    public void run() {
        log.info("WriterResultWorker started to write file: " + file);
        long startEvaluateTime = System.currentTimeMillis();

        String line;
        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            while (!(line = matchFactsQueue.take()).equals(Evaluator.EOF_MARKER)) {
                log.trace("write fact: " + line);
                writer.write(line);
                writer.newLine();
            }
            //Quick and dirty - wait for the other worker to complete processing
            Thread.sleep(1000);
        } catch (IOException | InterruptedException e) {
            throw new RuleEvaluatorException(
                "Failed to write to file: '" + file.getName() + "'. reason: " + e.getMessage(), e);
        }
        log.info("WriterResultWorker completed to write file in: " + (System.currentTimeMillis() - startEvaluateTime) +
            " miles");
    }
}


