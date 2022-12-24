package com.tzachi;

/**
 *
 */
public class RuleEvaluatorException extends RuntimeException{


    /**
     * Create RuleEvaluatorException with an exception message.
     * @param msg exception message
     */
    public RuleEvaluatorException(String msg) {
        super(msg);
    }

    /**
     * Create RuleEvaluatorException with an exception message and a cause of the original exception.
     * @param msg exception message
     * @param throwable the root cause exception
     */
    public RuleEvaluatorException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
