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

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.tools.ant.AntClassLoader;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.plexus.component.annotations.Component;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Provides a facade to evaluate Groovy scripts.
 * 
 * @author Benjamin Bentmann
 * @version $Id: GroovyScriptInterpreter.java 1361828 2012-07-15 22:30:27Z hboutemy $
 */
@Component( role = ScriptInterpreter.class, hint = "groovy" )
public class GroovyScriptInterpreter
    implements ScriptInterpreter
{

    /**
     * {@inheritDoc}
     */
    public Object evaluateScript( String script, List<String> classPath, Map<String, ? extends Object> globalVariables,
                                  PrintStream scriptOutput )
        throws ScriptEvaluationException
    {
        PrintStream origOut = System.out;
        PrintStream origErr = System.err;

        try
        {
            CompilerConfiguration config = new CompilerConfiguration( CompilerConfiguration.DEFAULT );

            if ( scriptOutput != null )
            {
                System.setErr( scriptOutput );
                System.setOut( scriptOutput );
                config.setOutput( new PrintWriter( scriptOutput ) );
            }

            ClassLoader loader = null;
            if ( classPath != null && !classPath.isEmpty() )
            {
                AntClassLoader childFirstLoader = new AntClassLoader( getClass().getClassLoader(), false );
                for ( String path : classPath )
                {
                    childFirstLoader.addPathComponent( new File( path ) );
                }
                loader = childFirstLoader;
            }

            Binding binding = new Binding( globalVariables );

            GroovyShell interpreter = new GroovyShell( loader, binding, config );

            try
            {
                return interpreter.evaluate( script );
            }
            catch ( ThreadDeath e )
            {
                throw e;
            }
            catch ( Throwable e )
            {
                throw new ScriptEvaluationException( e );
            }
        }
        finally
        {
            System.setErr( origErr );
            System.setOut( origOut );
        }
    }

}
