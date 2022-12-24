package com.tzachi.workers;

import com.tzachi.RuleEvaluatorException;
import com.tzachi.common.Constants.Evaluator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ReaderWorker extends Thread {

    private URL factURL;
    private BlockingQueue<String> queue;


    public ReaderWorker(URL factURL, BlockingQueue<String> queue) {
        this.factURL = factURL;
        this.queue = queue;
    }

    public void run() {

        log.info("ReaderWorker started to read file: " + factURL);
        long startEvaluateTime = System.currentTimeMillis();

        try (InputStream inputStream = factURL.openStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {

                log.trace("read fact: " + line);
                queue.put(line);
            }
            queue.put(Evaluator.EOF_MARKER);
        } catch (IOException | InterruptedException e) {
            throw new RuleEvaluatorException("Failed to read URL: '" + factURL + "'. reason: " + e.getMessage(), e);
        }
        log.info("ReaderWorker completed to read file in: " + (System.currentTimeMillis() - startEvaluateTime) +
            " miles");
    }
}


