/*
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.commons.scxml.invoke;

import java.net.URL;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.env.SimpleDispatcher;
import org.apache.commons.scxml.env.SimpleErrorHandler;
import org.apache.commons.scxml.env.SimpleErrorReporter;
import org.apache.commons.scxml.env.jexl.JexlContext;
import org.apache.commons.scxml.env.jexl.JexlEvaluator;
import org.apache.commons.scxml.io.SCXMLDigester;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.State;

/**
 * Unit tests {@link org.apache.commons.scxml.SCXMLExecutor}.
 * Testing <invoke>
 */
public class InvokeTest extends TestCase {
    /**
     * Construct a new instance of SCXMLExecutorTest with
     * the specified name
     */
    public InvokeTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(InvokeTest.class);
        suite.setName("SCXML Executor Tests, wildcard event match");
        return suite;
    }

    // Test data
    private URL invoke01;
    private SCXMLExecutor exec;

    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {
        invoke01 = this.getClass().getClassLoader().
            getResource("org/apache/commons/scxml/invoke/invoker-01.xml");
    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        invoke01 = null;
    }

    /**
     * Test the SCXML documents, usage of &lt;invoke&gt;
     */
    public void testInvoke01Sample() {
        try {
            SCXML scxml = SCXMLDigester.digest(invoke01,
                new SimpleErrorHandler());
            exec = new SCXMLExecutor(new JexlEvaluator(), new SimpleDispatcher(),
                new SimpleErrorReporter());
            assertNotNull(exec);
            exec.setRootContext(new JexlContext());
            exec.setStateMachine(scxml);
            exec.registerInvokerClass("scxml", SimpleSCXMLInvoker.class);
            exec.go();
            Set currentStates = exec.getCurrentStatus().getStates();
            assertEquals(1, currentStates.size());
            assertEquals("invoker", ((State)currentStates.iterator().
                next()).getId());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public static void main(String args[]) {
        TestRunner.run(suite());
    }
}

