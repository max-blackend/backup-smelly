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
package org.jsecurity.web.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * A Servlet Filter that enables AOP-style advice for a SerlvetRequest via
 * {@link #preHandle(javax.servlet.ServletRequest, javax.servlet.ServletResponse) preHandle},
 * {@link #postHandle(javax.servlet.ServletRequest, javax.servlet.ServletResponse) postHandle},
 * and {@link #afterCompletion(javax.servlet.ServletRequest, javax.servlet.ServletResponse, Exception) afterCompletion}
 * hooks.
 *
 * @author Les Hazlewood
 * @since 0.9
 */
public abstract class AdviceFilter extends OncePerRequestFilter {

    /** The static logger available to this class only */
    private static final Log log = LogFactory.getLog(AdviceFilter.class);

    /**
     * Returns <code>true</code> if the filter chain should be allowed to continue, <code>false</code> otherwise.
     * It is called before the chain is actually consulted/executed.
     * <p/>
     * The default implementation returns <code>true</code> always and exists as a template method for subclasses.
     *
     * @param request  the incoming ServletRequest
     * @param response the outgoing ServletResponse
     * @return <code>true</code> if the filter chain should be allowed to continue, <code>false</code> otherwise.
     * @throws Exception if there is any error.
     */
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        return true;
    }

    /**
     * Allows 'post' advice logic to be called, but only if no exception occurs during filter chain execution.  That
     * is, if {@link #executeChain executeChain} throws an exception, this method will never be called.  Be aware of
     * this when implementing logic.  Most resource 'cleanup' behavior is often done in the
     * {@link #afterCompletion(javax.servlet.ServletRequest, javax.servlet.ServletResponse, Exception) afterCompletion(request,response,exception)}
     * implementation, which is guaranteed to be called for every request, even when the chain processing throws
     * an Exception.
     * <p/>
     * The default implementation does nothing (no-op) and exists as a template method for subclasses.
     *
     * @param request  the incoming ServletRequest
     * @param response the outgoing ServletResponse
     * @throws Exception if an error occurs.
     */
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
    }

    /**
     * Called in all cases in a <code>finally</code> block even if {@link #preHandle preHandle} returns
     * <code>false</code> or if an exception is thrown during filter chain processing.  Can be used for resource
     * cleanup if so desired.
     * <p/>
     * The default implementation does nothing (no-op) and exists as a template method for subclasses.
     *
     * @param request   the incoming ServletRequest
     * @param response  the outgoing ServletResponse
     * @param exception any exception thrown during {@link #preHandle preHandle}, {@link #executeChain executeChain},
     *                  or {@link #postHandle postHandle} execution, or <code>null</code> if no exception was thrown
     *                  (i.e. the chain processed successfully).
     * @throws Exception if an error occurs.
     */
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
    }

    /**
     * Actually executes the specified filter chain by calling <code>chain.doFilter(request,response);</code>.
     * <p/>
     * Can be overridden by subclasses for custom logic.
     *
     * @param request  the incoming ServletRequest
     * @param response the outgoing ServletResponse
     * @param chain    the filter chain to execute
     * @throws Exception if there is any error executing the chain.
     */
    protected void executeChain(ServletRequest request, ServletResponse response, FilterChain chain) throws Exception {
        chain.doFilter(request, response);
    }

    /**
     * Actually implements the chain execution logic, utilizing
     * {@link #preHandle(javax.servlet.ServletRequest, javax.servlet.ServletResponse) pre},
     * {@link #postHandle(javax.servlet.ServletRequest, javax.servlet.ServletResponse) post}, and
     * {@link #afterCompletion(javax.servlet.ServletRequest, javax.servlet.ServletResponse, Exception) after}
     * advice hooks.
     *
     * @param request  the incoming ServletRequest
     * @param response the outgoing ServletResponse
     * @param chain    the filter chain to execute
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException      if an IO error occurs
     */
    @SuppressWarnings({"ThrowFromFinallyBlock"})
    public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Exception exception = null;

        try {

            boolean continueChain = preHandle(request, response);
            if (log.isTraceEnabled()) {
                log.trace("Invked preHandle method.  Continuing chain?: [" + continueChain + "]");
            }

            if (continueChain) {
                executeChain(request, response, chain);
            }

            postHandle(request, response);
            if (log.isTraceEnabled()) {
                log.trace("Successfully invoked postHandle method");
            }

        } catch (Exception e) {
            exception = e;
        } finally {
            cleanup( request, response, exception );
        }
    }

    /**
     * Executes cleanup logic in the <code>finally</code> code block in the
     * {@link #doFilterInternal(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain) doFilterInternal}
     * implementation.
     * <p/>
     * This implementation specifically calls
     * {@link #afterCompletion(javax.servlet.ServletRequest, javax.servlet.ServletResponse, Exception) afterCompletion}
     * as well as handles any exceptions properly.
     *
     * @param request the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @param existing any exception that might have occurred while executing the <code>FilterChain</code> or
     * pre or post advice, or <code>null</code> if the pre/chain/post excution did not throw an <code>Exception</code>.
     * @throws ServletException if any exception other than an <code>IOException</code> is thrown.
     * @throws IOException if the pre/chain/post execution throw an <code>IOException</code>
     */
    protected void cleanup( ServletRequest request, ServletResponse response, Exception existing )
        throws ServletException, IOException {
        Exception exception = existing;
        try {
            afterCompletion(request, response, exception);
            if (log.isTraceEnabled()) {
                log.trace("Successfully invoked afterCompletion method.");
            }
        } catch (Exception e) {
            if (exception == null) {
                exception = e;
            }
        }
        if (exception != null) {
            if (exception instanceof ServletException) {
                throw (ServletException) exception;
            } else if (exception instanceof IOException) {
                throw (IOException) exception;
            } else {
                String msg = "Filter execution resulted in an unexpected Exception " +
                        "(not IOException or ServletException as the Filter api recommends).  " +
                        "Wrapping in ServletException and propagating.";
                throw new ServletException(msg, exception);
            }
        }
    }
}
