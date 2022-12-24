package com.tzachi


import spock.lang.Specification

class RuleEvaluatorManagerSpec extends Specification {

    private static final String TEST_FACT_DIRECTORY = "src/test/resources/facts/"

    def "Rule manager should verify #scenario" ()  {
        given:
        RuleEvaluatorManager ruleEvaluatorManager = new RuleEvaluatorManager("src/test/resources/rules_definition/" + rules_defenition)

        when:
        def actualFactsRulesResult = ruleEvaluatorManager.internalEvaluate(new File
                (TEST_FACT_DIRECTORY +facts_file).toURI().toURL())

        then:
        def expectedFactsRulesResult = new File(TEST_FACT_DIRECTORY + expected_result)
        List<String> actualContent = actualFactsRulesResult.readLines()
        List<String> expectedContent = expectedFactsRulesResult.readLines()

        // Sort the arrays of words
        Collections.sort(actualContent)
        Collections.sort(expectedContent)

        actualContent == expectedContent

        where:
        scenario        | rules_defenition                    | facts_file              | expected_result
        "gdsgg"         |"SSN_rule_with_two_conditions.json " | "social-security.csv"   | "expected_social-security.csv"


    }


    private boolean compareFiles (File actualFile , File expectedFile) {
        List<String> actualContent = actualFile.readLines()
        List<String> expectedContent = expectedFile.readLines()

        // Split the contents of both String variables into arrays of words
//        String[] file1Words = actualContent.split("\\s+");
//        String[] file2Words = file2Content.split("\\s+");

        // Sort the arrays of words
        Arrays.sort(actualContent)
        Arrays.sort(expectedContent)

    }
}
