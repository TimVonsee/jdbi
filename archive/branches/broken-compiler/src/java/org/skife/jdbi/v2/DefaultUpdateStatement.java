package org.skife.jdbi.v2;

import org.skife.jdbi.v2.tweak.StatementRewriter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 */
final class DefaultUpdateStatement extends SQLStatement<DefaultUpdateStatement> implements UpdateStatement
{
    DefaultUpdateStatement(Connection connection, StatementRewriter statementRewriter, PreparedStatementCache cache, String sql)
    {
        super(new Parameters(), statementRewriter, connection, cache, sql);
    }

    public int execute()
    {
        return this.internalExecute(QueryPreperator.NO_OP, new QueryResultMunger<Integer>()
        {
            public Pair<Integer, ResultSet> munge(Statement results) throws SQLException
            {
                return new Pair<Integer, ResultSet>(results.getUpdateCount(), null);
            }
        }, QueryPostMungeCleanup.CLOSE_RESOURCES_QUIETLY);
    }
}
