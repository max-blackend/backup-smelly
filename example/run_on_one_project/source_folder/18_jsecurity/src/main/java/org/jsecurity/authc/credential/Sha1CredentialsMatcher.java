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
package org.jsecurity.authc.credential;

import org.jsecurity.crypto.hash.AbstractHash;
import org.jsecurity.crypto.hash.Hash;
import org.jsecurity.crypto.hash.Sha1Hash;

/**
 * <tt>HashedCredentialsMatcher</tt> implementation that expects the stored <tt>AuthenticationInfo</tt> credentials to be
 * SHA hashed.
 *
 * <p><b>Note:</b> <a href="http://en.wikipedia.org/wiki/MD5">MD5</a> and
 * <a href="http://en.wikipedia.org/wiki/SHA_hash_functions">SHA-1</a> algorithms are now known to be vulnerable to
 * compromise and/or collisions (read the linked pages for more).  While most applications are ok with either of these
 * two, if your application mandates high security, use the SHA-256 (or higher) hashing algorithms and their
 * supporting <code>CredentialsMatcher</code> implementations.</p>
 *
 * @author Les Hazlewood
 * @since 0.9
 */
public class Sha1CredentialsMatcher extends HashedCredentialsMatcher {

    /**
     * Creates a new <em>uninitialized</em> {@link Sha1Hash Sha1Hash} instance, without it's byte array set.
     *
     * @return a new <em>uninitialized</em> {@link Sha1Hash Sha1Hash} instance, without it's byte array set.
     */
    protected AbstractHash newHashInstance() {
        return new Sha1Hash();
    }

    /**
     * This implementation merely returns
     * <code>new {@link Sha1Hash#Sha1Hash(Object, Object, int) Sha1Hash(credentials,salt,hashIterations)}</code>.
     */
    protected Hash hashProvidedCredentials(Object credentials, Object salt, int hashIterations) {
        return new Sha1Hash(credentials, salt, hashIterations);
    }
}
