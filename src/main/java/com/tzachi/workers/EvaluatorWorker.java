package com.tzachi.workers;

import com.tzachi.RuleEvaluatorException;
import com.tzachi.rule.Rule;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import static com.tzachi.common.Constants.Evaluator.EOF_MARKER;

@Builder
@Value
@Slf4j
public class EvaluatorWorker extends Thread {

    static ReentrantLock LOCK = new ReentrantLock();
    int workerId;
    List<Rule<String>> rules;

    BlockingQueue<String> factsQueue;

    BlockingQueue<String> matchFactsQueue;

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            while (true) {
                LOCK.lock();
                String fact = factsQueue.take();
                LOCK.unlock();
                if (EOF_MARKER.equals(fact)) {
                    matchFactsQueue.put(fact);
                } else {
                    applyRules(fact);
                }
            }
        } catch (InterruptedException e) {
            throw new RuleEvaluatorException("Failed to evaluate facts. reason: " + e.getMessage(), e);
        }
    }

    private void applyRules(String factLine) throws InterruptedException {

        // Run all rules
        for (Rule<String> rule : rules) {
            if(log.isTraceEnabled()) {
                log.trace("processing condition for fact: " + factLine + ", rule: " + rule.getName());
            }
            if (rule.evaluate(Collections.singletonMap("fact", factLine))) {
                rule.apply(Collections.singletonMap("fact", factLine));
                if(log.isTraceEnabled()) {
                    log.trace("apply actions for fact: " + factLine + ", rule: " +  rule.getName());
                }
                matchFactsQueue.put(factLine);
            }
        }
    }
}
