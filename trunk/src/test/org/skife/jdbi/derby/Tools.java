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
package org.skife.jdbi.derby;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.skife.jdbi.HandyMapThing;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;

public class Tools
{
    public static final String CONN_STRING = "jdbc:derby:testing";
    public static Driver driver;
    public static boolean running = false;
    public static EmbeddedDataSource dataSource;

    public static void start() throws SQLException, IOException
    {
        if (!running)
        {
            running = true;
            System.setProperty("derby.system.home", "build/db");
            File db = new File("build/db");
            db.mkdirs();

            dataSource = new EmbeddedDataSource();
            dataSource.setCreateDatabase("create");
            dataSource.setDatabaseName("testing");

            final Connection conn = dataSource.getConnection();
            conn.close();
        }
    }

    public static void stop() throws SQLException
    {
        final Connection conn = getConnection();
        final Statement delete = conn.createStatement();
        try
        {
            delete.execute("delete from something");
        }
        catch (SQLException e)
        {
            // may not exist
        }
        delete.close();
        final String[] drops = {"drop table something",
                                "drop function do_it",
                                "drop procedure INSERTSOMETHING"};
        for (String drop : drops)
        {
            final Statement stmt = conn.createStatement();
            try
            {
                stmt.execute(drop);
            }
            catch (Exception e)
            {
                // may not exist
            }

        }
    }

    public static Connection getConnection() throws SQLException
    {
        return dataSource.getConnection();
    }

    public static void dropAndCreateSomething() throws SQLException
    {
        final Connection conn = getConnection();

        final Statement create = conn.createStatement();
        try
        {
            create.execute("create table something ( id integer, name varchar(50), integerValue integer, intValue integer )");
        }
        catch (Exception e)
        {
            // probably still exists because of previous failed test, just delete then
            create.execute("delete from something");
        }
        create.close();
        conn.close();
    }

    public static String doIt()
    {
        return "it";
    }

    public static DataSource getDataSource()
    {
        return dataSource;
    }

    public static <K> HandyMapThing<K> map(K k, Object v)
    {
        HandyMapThing<K>s =  new HandyMapThing<K>();
        return s.add(k, v);
    }
}
