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
package org.skife.jdbi.derby;

import org.apache.derby.jdbc.EmbeddedDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Tools
{
    public static final String CONN_STRING = "jdbc:derby:testing";
    public static Driver driver;
    public static boolean running = false;

    public static final void start() throws IOException, SQLException, IOException
    {
        if (running)
        {

        }
        else
        {
            running = true;
            System.setProperty("derby.system.home", "build/db");
            File db = new File("build/db");
            db.mkdirs();
            final Properties derby = new Properties();
            derby.setProperty("derby.storage.fileSyncTransactionLog", "true");
            FileOutputStream outs = new FileOutputStream("build/db/derby.properties");
            derby.store(outs, "auto-generated properties for unit testing");
            driver = new EmbeddedDriver();
            DriverManager.registerDriver(driver);

            final Connection conn = DriverManager.getConnection("jdbc:derby:testing;create=true");
            conn.close();
        }
    }

    public final static void stop() throws SQLException
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
        for (int i = 0; i < drops.length; i++)
        {
            final String drop = drops[i];
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

    public static final Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(CONN_STRING);
    }

    public static final void dropAndCreateSomething() throws SQLException
    {
        final Connection conn = getConnection();

        final Statement create = conn.createStatement();
        try
        {
            create.execute("create table something ( id integer, name varchar(50) )");
        }
        catch (Exception e)
        {
            // probably still exists because of previous failed test, just delete then
            create.execute("delete from something");
        }
        create.close();
        conn.close();
    }

    public static void insertSomething() throws SQLException
    {
        Connection conn = DriverManager.getConnection("jdbc:default:connection");
        final PreparedStatement stmt = conn.prepareStatement("insert into something (id, name) values (?, ?)");
        stmt.setInt(1, 5);
        stmt.setString(2, "george");
        stmt.execute();
    }

    public static String doIt()
    {
        return "it";
    }
}
