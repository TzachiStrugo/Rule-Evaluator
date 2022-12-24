package com.tzachi;

import com.tzachi.expression.MvelRuleFactory;
import com.tzachi.reader.JsonRuleDefinitionReader;
import com.tzachi.rule.Rule;
import com.tzachi.workers.EvaluatorWorker;
import com.tzachi.workers.ReaderWorker;
import com.tzachi.workers.WriterResultWorker;
import io.vavr.control.Try;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RuleEvaluatorManager {

    private static final String RULES_DEFAULT_PATH_FILE = "src/main/resources/rules.json";

    private static final int NUMBER_OF_EVALUATOR_WORKERS = 2;

    private static final int JOB_TOTAL_LINES = 1000;

    List<Rule<String>> rules;

    private final MvelRuleFactory mvelRuleFactory;


    public RuleEvaluatorManager() {
        this(RULES_DEFAULT_PATH_FILE);
    }

    public RuleEvaluatorManager(String rulesDefinitionURI) {
        mvelRuleFactory = new MvelRuleFactory(new JsonRuleDefinitionReader());
       rules = buildRules(rulesDefinitionURI);
    }

    public void evaluate(URL factsURL) throws InterruptedException {
        internalEvaluate(factsURL);

    }

    /**
     * Created for test purpose
     * @param factsURL A URL for fact
     */
    File internalEvaluate(URL factsURL) throws InterruptedException {

        long startEvaluateTime = System.currentTimeMillis();

        //1. Create two queues:
        // A. for all lines reading as stream by the reader worker
        // B. for all lines that ruled found match.
        BlockingQueue<String> factsQueue = new ArrayBlockingQueue<>(JOB_TOTAL_LINES);
        BlockingQueue<String> matchFactsQueue = new ArrayBlockingQueue<>(JOB_TOTAL_LINES);

        //2. Create reader that read file as stream and insert line to queue job
        ReaderWorker readerWorker = new ReaderWorker(factsURL, factsQueue);
        readerWorker.start();
        //3. Create writer that write facts that match to all rule conditions
        WriterResultWorker writerWorker = new WriterResultWorker(matchFactsQueue);
        writerWorker.start();

        //4. Init evaluator worker and start processing jobs
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        List<EvaluatorWorker> workers = new ArrayList<>(NUMBER_OF_EVALUATOR_WORKERS);
        for (int i = 0; i < NUMBER_OF_EVALUATOR_WORKERS; i++) {
            EvaluatorWorker evaluatorWorker = EvaluatorWorker.builder()
                .workerId(i)
                .rules(rules)
                .factsQueue(factsQueue)
                .matchFactsQueue(matchFactsQueue)
                .build();
            workers.add(evaluatorWorker);
            evaluatorWorker.start();
        }

        //5. Wait for the Reader worker and the Writer worker to complete
        readerWorker.join();
        writerWorker.join();

        log.info("Completed to evaluates rules in: " + (System.currentTimeMillis() - startEvaluateTime) + " miles");
        return writerWorker.getFile();
    }


    private List<Rule<String>> buildRules(String pathnameRules) {
        FileReader fileReader = Try.of(() -> new FileReader(pathnameRules))
            .getOrElseThrow(t -> new RuleEvaluatorException("Failed to load rules configuration from path: " +
                "'" + pathnameRules + "'. please check path and try again", t));

        return Try.of(() -> mvelRuleFactory.createRules(fileReader))
            .getOrElseThrow(t -> new RuleEvaluatorException("Failed to parse json rules file: '" + pathnameRules +
                "'. reason: " + t.getMessage()));
    }
}
