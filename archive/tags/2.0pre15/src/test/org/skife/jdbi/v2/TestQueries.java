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

import org.skife.jdbi.derby.Tools;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TestQueries extends DBITestCase
{
    private BasicHandle h;

    public void setUp() throws Exception
    {
        super.setUp();
        h = openHandle();
    }

    public void tearDown() throws Exception
    {
        if (h != null) h.close();
        Tools.stop();
    }

    public void testCreateQueryObject() throws Exception
    {
        h.createStatement("insert into something (id, name) values (1, 'eric')").execute();
        h.createStatement("insert into something (id, name) values (2, 'brian')").execute();

        List<Map<String, Object>> results = h.createQuery("select * from something order by id").list();
        assertEquals(2, results.size());
        Map<String, Object> first_row = results.get(0);
        assertEquals("eric", first_row.get("name"));
    }

    public void testMappedQueryObject() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.insert("insert into something (id, name) values (2, 'brian')");

        Query<Something> query = h.createQuery("select * from something order by id").map(Something.class);

        List<Something> r = query.list();
        Something eric = r.get(0);
        assertEquals("eric", eric.getName());
        assertEquals(1, eric.getId());
    }

	public void testMappedQueryObjectWithNulls() throws Exception
	{
		h.insert("insert into something (id, name, integerValue) values (1, 'eric', null)");

		Query<Something> query = h.createQuery("select * from something order by id").map(Something.class);

		List<Something> r = query.list();
		Something eric = r.get(0);
		assertEquals("eric", eric.getName());
		assertEquals(1, eric.getId());
		assertNull(eric.getIntegerValue());
	}

	public void testMappedQueryObjectWithNullForPrimitiveIntField() throws Exception
	{
		h.insert("insert into something (id, name, intValue) values (1, 'eric', null)");

		Query<Something> query = h.createQuery("select * from something order by id").map(Something.class);

		List<Something> r = query.list();
		Something eric = r.get(0);
		assertEquals("eric", eric.getName());
		assertEquals(1, eric.getId());
		assertEquals(0, eric.getIntValue());
	}

	public void testMapper() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.insert("insert into something (id, name) values (2, 'brian')");

        Query<String> query = h.createQuery("select name from something order by id").map(new ResultSetMapper<String>()
        {
            public String map(int index, ResultSet r, StatementContext ctx) throws SQLException
            {
                return r.getString(1);
            }
        });

        String name = query.list().get(0);
        assertEquals("eric", name);
    }

    public void testConvenienceMethod() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.insert("insert into something (id, name) values (2, 'brian')");

        List<Map<String, Object>> r = h.select("select * from something order by id");
        assertEquals(2, r.size());
        assertEquals("eric", r.get(0).get("name"));
    }

    public void testConvenienceMethodWithParam() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.insert("insert into something (id, name) values (2, 'brian')");

        List<Map<String, Object>> r = h.select("select * from something where id = ?", 1);
        assertEquals(1, r.size());
        assertEquals("eric", r.get(0).get("name"));
    }

    public void testPositionalArgWithNamedParam() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.insert("insert into something (id, name) values (2, 'brian')");

        List<Something> r = h.createQuery("select * from something where name = :name")
                .bind(0, "eric")
                .map(Something.class)
                .list();

        assertEquals(1, r.size());
        assertEquals("eric", r.get(0).getName());
    }

    public void testMixedSetting() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.insert("insert into something (id, name) values (2, 'brian')");

        List<Something> r = h.createQuery("select * from something where name = :name and id = :id")
                .bind(0, "eric")
                .bind("id", 1)
                .map(Something.class)
                .list();

        assertEquals(1, r.size());
        assertEquals("eric", r.get(0).getName());
    }

    public void testHelpfulErrorOnNothingSet() throws Exception
    {
        try
        {
            h.createQuery("select * from something where name = :name").list();
            fail("should have raised exception");
        }
        catch (UnableToExecuteStatementException e)
        {
            assertTrue("execution goes through here", true);
        }
        catch (Exception e)
        {
            fail("Raised incorrect exception");
        }
    }

    public void testFirstResult() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.insert("insert into something (id, name) values (2, 'brian')");

        Something r = h.createQuery("select * from something order by id")
                .map(Something.class)
                .first();

        assertNotNull(r);
        assertEquals("eric", r.getName());
    }

    public void testIteratedResult() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.insert("insert into something (id, name) values (2, 'brian')");

        ResultIterator<Something> i = h.createQuery("select * from something order by id")
                .map(Something.class)
                .iterator();

        assertTrue(i.hasNext());
        Something first = i.next();
        assertEquals("eric", first.getName());
        assertTrue(i.hasNext());
        Something second = i.next();
        assertEquals(2, second.getId());
        assertFalse(i.hasNext());

        i.close();
    }

    public void testIteratorBehavior() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.insert("insert into something (id, name) values (2, 'brian')");

        ResultIterator<Something> i = h.createQuery("select * from something order by id")
                .map(Something.class)
                .iterator();

        assertTrue(i.hasNext());
        assertTrue(i.hasNext());
        Something first = i.next();
        assertEquals("eric", first.getName());
        assertTrue(i.hasNext());
        Something second = i.next();
        assertEquals(2, second.getId());
        assertFalse(i.hasNext());

        i.close();
    }

    public void testIteratorBehavior2() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.insert("insert into something (id, name) values (2, 'brian')");

        ResultIterator<Something> i = h.createQuery("select * from something order by id")
                .map(Something.class)
                .iterator();

        Something first = i.next();
        assertEquals("eric", first.getName());
        Something second = i.next();
        assertEquals(2, second.getId());
        assertFalse(i.hasNext());

        i.close();
    }

    public void testIteratorBehavior3() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.insert("insert into something (id, name) values (2, 'eric')");

        int count = 0 ;
        for (Something s : h.createQuery("select * from something order by id").map(Something.class))
        {
            count++;
            assertEquals("eric", s.getName());
        }

        assertEquals(2, count);

    }

    public void testFetchSize() throws Exception
    {
        h.createScript("default-data").execute();

        Query<Something> q = h.createQuery("select id, name from something order by id").map(Something.class);

        q.setFetchSize(1);

        ResultIterator<Something> r = q.iterator();

        assertTrue(r.hasNext());
        r.next();
        assertTrue(r.hasNext());
        r.next();
        assertFalse(r.hasNext());
    }

    public void testFirstWithNoResult() throws Exception
    {
        Something s = h.createQuery("select id, name from something").map(Something.class).first();
        assertNull(s);
    }
}