package org.apache.maven.shared.scriptinterpreter;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests the Groovy interpreter facade.
 * 
 * @author Benjamin Bentmann
 * @version $Id: GroovyScriptInterpreterTest.java 1194925 2011-10-29 16:34:29Z olamy $
 */
public class GroovyScriptInterpreterTest
    extends TestCase
{

    public void testEvaluateScript()
        throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ScriptInterpreter interpreter = new GroovyScriptInterpreter();
        assertEquals( Boolean.TRUE, interpreter.evaluateScript( "print \"Test\"\nreturn true", null, null,
                                                                new PrintStream( out ) ) );
        assertEquals( "Test", out.toString() );
    }

    public void testEvaluateScriptVars()
        throws Exception
    {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put( "testVar", "data" );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ScriptInterpreter interpreter = new GroovyScriptInterpreter();
        assertEquals( Boolean.TRUE, interpreter.evaluateScript( "print testVar\nreturn true", null, vars,
                                                                new PrintStream( out ) ) );
        assertEquals( "data", out.toString() );
    }

}
