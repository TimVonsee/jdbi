/* Copyright 2004-2006 Brian McCallister
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
package org.skife.jdbi.v2;

import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ReWrittenStatement;
import org.skife.jdbi.v2.tweak.StatementRewriter;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Statement rewriter which replaces named parameter tokens of the form :tokenName 
 */
public class NamedParameterStatementRewriter implements StatementRewriter
{
    public ReWrittenStatement rewrite(String raw, Parameters params)
    {
        ParsedStatement ps = new ParsedStatement(raw);
        return new MyReWrittenStatement(ps);
    }

    private static class MyReWrittenStatement implements ReWrittenStatement
    {
        private final ParsedStatement parsed;

        MyReWrittenStatement(ParsedStatement parsed)
        {
            this.parsed = parsed;
        }

        public void bind(Parameters params, PreparedStatement statement) throws SQLException
        {
            if (parsed.isPositionalOnly())
            {
                // no named params, is easy
                boolean finished = false;
                for (int i = 0; !finished; ++i)
                {
                    Argument a = params.forPosition(i);
                    if (a != null)
                    {
                        a.apply(i + 1, statement);
                    }
                    else
                    {
                        finished = true;
                    }
                }
            }
            else
            {
                String[] named_params = parsed.getNamedParams();
                for (int i = 0; i < named_params.length; i++)
                {
                    String named_param = named_params[i];

                    Argument a = params.forName(named_param);
                    if (a == null)
                    {
                        a = params.forPosition(i);
                    }

                    if (a == null) {
                        String msg = String.format("Unable to execute, no named parameter matches " +
                                                   "\"%s\" and no positional param for place %d (which is %d in " +
                                                   "the JDBC 'start at 1' scheme) has been set.",
                                                   named_param, i, i+1);
                        throw new UnableToExecuteStatementException(msg);
                    }

                    a.apply(i + 1, statement);
                }
            }
        }

        public String getSql()
        {
            return parsed.getSubstitutedSql();
        }
    }


}
