package com.tzachi;


import java.net.URL;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {


    public static void main(String[] args) {

        if(args.length == 0 ){
            log.error("Not enough arguments supplied to program. You must supply at least URL path for facts data");
            throw new RuleEvaluatorException("Not enough arguments");
        }

        if(args.length == 1) {
            try {
                URL factsData = new URL(args[0]);
                RuleEvaluatorManager evaluatorManager = new RuleEvaluatorManager();
                evaluatorManager.evaluate(factsData);
            } catch (Exception e){
                log.error("The rule evaluator failed to evaluate. reason: " + e.getMessage() );
            }
        }

        if(args.length == 2) {
            try {
                URL factsData = new URL(args[0]);
                String rulesDefinitionURI = args[1];
                RuleEvaluatorManager evaluatorManager = new RuleEvaluatorManager(rulesDefinitionURI);
                evaluatorManager.evaluate(factsData);
            } catch (Exception e){
                log.error("The rule evaluator failed to evaluate. reason: " + e.getMessage() );
            }
        }
    }
}