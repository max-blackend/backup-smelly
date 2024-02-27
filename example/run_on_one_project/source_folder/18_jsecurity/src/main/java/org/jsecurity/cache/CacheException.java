/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jsecurity.cache;

import org.jsecurity.JSecurityException;

/**
 * Root class of all JSecurity exceptions related to caching operations.
 *
 * @author Jeremy Haile
 * @author Les Hazlewood
 * @since 0.2
 */
public class CacheException extends JSecurityException {

    /**
     * Creates a new <code>CacheException</code>.
     */
    public CacheException() {
        super();
    }

    /**
     * Creates a new <code>CacheException</code>.
     *
     * @param message the reason for the exception.
     */
    public CacheException(String message) {
        super(message);
    }

    /**
     * Creates a new <code>CacheException</code>.
     *
     * @param cause the underlying cause of the exception.
     */
    public CacheException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new <code>CacheException</code>.
     *
     * @param message the reason for the exception.
     * @param cause   the underlying cause of the exception.
     */
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}