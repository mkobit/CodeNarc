/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package org.codenarc.tool

import groovy.text.SimpleTemplateEngine
import org.codenarc.ruleset.RuleSets
import org.codenarc.ruleset.XmlFileRuleSet
import org.apache.log4j.Logger

/**
 * Java application (main() method) that generates the "codenarc-rule-index.apt.template" file.
 * which is the APT file for the Rule Index page of the project web site.
 *
 * @author Chris Mair
 * @version $Revision: $ - $Date:  $
 */
class GenerateRuleIndexPage {

    protected static final RULE_INDEX_FILE = 'src/site/apt/codenarc-rule-index.apt'
    private static final TEMPLATE_FILE = 'src/main/resources/templates/codenarc-rule-index.apt.template'
    private static final LOG = Logger.getLogger(GenerateRuleIndexPage)

    protected static ruleIndexFile = RULE_INDEX_FILE

    /**
     * Write out all current rule index to the 'codenarc-rule-index.apt' APT file
     * @param args - command-line args (not used)
     */
    static void main(String[] args) {

        def rulesByRuleSet = [:]
        def numberOfRules = 0
        RuleSets.ALL_RULESET_FILES.each { ruleSetPath ->
            def ruleSet = new XmlFileRuleSet(ruleSetPath)
            def ruleSetName = ruleSetPath - 'rulesets/' - '.xml'
            rulesByRuleSet[ruleSetName] = GenerateUtil.sortRules(ruleSet.rules)
            numberOfRules += rulesByRuleSet[ruleSetName].size()
        }

        def binding = [ruleSets:rulesByRuleSet, numberOfRules:numberOfRules]
        def ruleSetTemplateFile = new File(TEMPLATE_FILE)

        def engine = new SimpleTemplateEngine()
        def ruleSetText = engine.createTemplate(ruleSetTemplateFile).make(binding)

        def outputFile = new File(ruleIndexFile)
        outputFile.text = ruleSetText

        LOG.info("Finished writing $ruleIndexFile")
    }


}