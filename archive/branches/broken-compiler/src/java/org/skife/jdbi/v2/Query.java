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
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface Query<ResultType> extends Iterable<ResultType>
{
    /**
     * Executes the select
     * <p/>
     * Will eagerly load all results
     *
     * @return
     * @throws org.skife.jdbi.v2.exceptions.UnableToCreateStatementException
     *          if there is an error creating the statement
     * @throws org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException
     *          if there is an error executing the statement
     * @throws org.skife.jdbi.v2.exceptions.ResultSetException
     *          if there is an error dealing with the result set
     */
    List<ResultType> list();

    /**
     * Executes the select.
     * <p/>
     * Specifies a maximum of one result on the JDBC statement, and map that one result
     * as the return value, or return null if there is nothing in the results
     *
     * @return first result, mapped, or null if there is no first result
     */
    ResultType first();

    <Type> Query<Type> map(Class<Type> resultType);

    <T> Query<T> map(ResultSetMapper<T> mapper);

    /**
     * Used if you need to have some exotic parameter bound.
     *
     * @param position position to bindBinaryStream this argument, starting at 0
     * @param argument exotic argument factory
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, Argument argument);

    /**
     * Used if you need to have some exotic parameter bound.
     *
     * @param name     name to bindBinaryStream this argument
     * @param argument exotic argument factory
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, Argument argument);


    public Query<ResultType> bindFromProperties(Object o);


    public Query<ResultType> bindFromMap(Map<String, ? extends Object> args);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, String value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, String value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, int value);

    /**
     * Bind an argument by name
     *
     * @param name  name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, int value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @param length   how long is the stream being bound?
     * @return the same Query instance
     */
    public Query<ResultType> bindASCIIStream(int position, InputStream value, int length);

    /**
     * Bind an argument by name
     *
     * @param name   token name to bindBinaryStream the paramater to
     * @param value  to bindBinaryStream
     * @param length bytes to read from value
     * @return the same Query instance
     */
    public Query<ResultType> bindASCIIStream(String name, InputStream value, int length);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, BigDecimal value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, BigDecimal value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bindBinaryStream(int position, InputStream value, int length);

    /**
     * Bind an argument by name
     *
     * @param name   token name to bindBinaryStream the paramater to
     * @param value  to bindBinaryStream
     * @param length bytes to read from value
     * @return the same Query instance
     */
    public Query<ResultType> bindBinaryStream(String name, InputStream value, int length);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, Blob value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, Blob value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, boolean value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, boolean value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, byte value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, byte value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, byte[] value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, byte[] value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @param length   number of characters to read
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, Reader value, int length);

    /**
     * Bind an argument by name
     *
     * @param name   token name to bindBinaryStream the paramater to
     * @param value  to bindBinaryStream
     * @param length number of characters to read
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, Reader value, int length);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, Clob value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, Clob value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, java.sql.Date value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, java.sql.Date value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, java.util.Date value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, java.util.Date value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, Double value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, Double value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, Float value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, Float value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, long value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, long value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, Object value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, Object value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, Time value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, Time value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, Timestamp value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, Timestamp value);

    /**
     * Bind an argument positionally
     *
     * @param position position to bindBinaryStream the paramater at, starting at 0
     * @param value    to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(int position, URL value);

    /**
     * Bind an argument by name
     *
     * @param name  token name to bindBinaryStream the paramater to
     * @param value to bindBinaryStream
     * @return the same Query instance
     */
    public Query<ResultType> bind(String name, URL value);

    ResultIterator<ResultType> iterator();
}
