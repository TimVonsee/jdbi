/* Copyright 2004-2005 Brian McCallister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.skife.jdbi;

import org.skife.jdbi.tweak.ClasspathStatementLocator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class Script
{
    private final Handle handle;
    private final String name;

    Script(final Handle handle, final String name)
    {
        this.handle = handle;
        this.name = name;
    }

    public void run() throws DBIException, IOException
    {
        final ClassLoader loader = ClasspathStatementLocator.selectClassLoader();
        BufferedReader reader = null;
        try
        {
            final InputStream in = loader.getResourceAsStream(name + ".sql");
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            final StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null)
            {
                if (ClasspathStatementLocator.isComment(line)) continue;
                buffer.append(line).append(" ");
            }
            final String[] statements = buffer.toString().replaceAll("\n", " ").split(";");

            for (int i = 0; i != statements.length; i++)
            {
                if (!statements[i].trim().equals("")) handle.execute(statements[i]);
            }
        }
        finally
        {
            if (reader != null) reader.close();
        }
    }
}
