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

package org.skife.jdbi.v2.spring;

import org.skife.jdbi.v2.IDBI;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

/**
 *
 */
public class DummyService implements Service
{
    private final IDBI dbi;

    public DummyService(IDBI dbi)
    {
        this.dbi = dbi;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void inPropagationRequired(Callback c)
    {
        c.call(dbi);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void inRequiresNew(Callback c)
    {
        c.call(dbi);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void inNested(Callback c)
    {
        c.call(dbi);
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW, isolation = Isolation.READ_UNCOMMITTED)
    public void inRequiresNewReadUncommitted(Callback c)
    {
        c.call(dbi);
    }
}
