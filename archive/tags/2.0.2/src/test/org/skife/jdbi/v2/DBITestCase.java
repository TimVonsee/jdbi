/*
 * Copyright 2004-2006 Brian McCallister
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
package org.skife.jdbi.v2;

import junit.framework.TestCase;
import org.skife.jdbi.derby.Tools;
import org.skife.jdbi.v2.tweak.TransactionHandler;
import org.skife.jdbi.v2.tweak.StatementLocator;
import org.skife.jdbi.v2.tweak.transactions.LocalTransactionHandler;
import org.skife.jdbi.v2.logging.NoOpLog;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.sql.SQLException;
import java.sql.Connection;

/**
 *
 */
public abstract class DBITestCase extends TestCase
{
    private final List<BasicHandle> handles = new ArrayList<BasicHandle>();
    private ExecutorService executor;

    public void setUp() throws Exception
    {
        Tools.start();
        Tools.dropAndCreateSomething();
    }

    public void tearDown() throws Exception
    {
        for (BasicHandle handle : handles)
        {
            handle.close();
        }
        Tools.stop();
    }

    protected StatementLocator getStatementLocator() {
        return new ClasspathStatementLocator();
    }

    protected BasicHandle openHandle() throws SQLException
    {
        Connection conn = Tools.getConnection();
        BasicHandle h = new BasicHandle(getTransactionHandler(),
                                        getStatementLocator(),
                                        new CachingStatementBuilder(new DefaultStatementBuilder()),
                                        new ColonPrefixNamedParamStatementRewriter(),
                                        conn,
                                        new HashMap<String, Object>(),
                                        new NoOpLog());
        handles.add(h);
        return h;
    }

    protected TransactionHandler getTransactionHandler()
    {
        return new LocalTransactionHandler();
    }

    protected <T> Future<T> run(Callable<T> it)
    {
        if (this.executor == null) {
            this.executor = Executors.newCachedThreadPool();
        }
        return executor.submit(it);
    }

}
