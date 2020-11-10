/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.ballerinalang.observe.nativeimpl;

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.observability.ObserveUtils;
import io.ballerina.runtime.observability.ObserverContext;
import io.ballerina.runtime.observability.TracingUtils;
import io.ballerina.runtime.observability.tracer.TracersStore;
import io.opentracing.Tracer;
import org.ballerinalang.config.ConfigRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static io.ballerina.runtime.observability.ObservabilityConstants.CONFIG_TRACING_ENABLED;
import static io.ballerina.runtime.observability.ObservabilityConstants.UNKNOWN_RESOURCE;
import static io.ballerina.runtime.observability.ObservabilityConstants.UNKNOWN_SERVICE;

/**
 * This class wraps opentracing apis and exposes extern functions to use within ballerina.
 */
public class OpenTracerBallerinaWrapper {

    private static final OpenTracerBallerinaWrapper instance = new OpenTracerBallerinaWrapper();
    private final TracersStore tracerStore;
    private final boolean enabled;
    private final Map<Long, ObserverContext> observerContextMap = new HashMap<>();
    private final AtomicLong spanIdCounter = new AtomicLong();

    private static final int SYSTEM_TRACE_INDICATOR = -1;
    static final int ROOT_SPAN_INDICATOR = -2;

    private OpenTracerBallerinaWrapper() {
        enabled = ConfigRegistry.getInstance().getAsBoolean(CONFIG_TRACING_ENABLED);
        tracerStore = TracersStore.getInstance();
    }

    public static OpenTracerBallerinaWrapper getInstance() {
        return instance;
    }

    private long startSpan(ObserverContext observerContext, boolean isClient, String spanName) {
        observerContext.setFunctionName(spanName);
        TracingUtils.startObservation(observerContext, isClient);
        long spanId = this.spanIdCounter.getAndIncrement();
        observerContextMap.put(spanId, observerContext);
        return spanId;
    }

    /**
     * Method to start a span using parent span context.
     *
     * @param env          native context
     * @param serviceName  name of the service to which the span is attached to
     * @param spanName     name of the span
     * @param tags         key value paired tags to attach to the span
     * @param parentSpanId id of parent span
     * @return unique id of the created span
     */
    public long startSpan(Environment env, String serviceName, String spanName, Map<String, String> tags,
                          long parentSpanId) {
        if (!enabled) {
            return -1;
        }

        ObserverContext currentObserverContext = ObserveUtils.getObserverContextOfCurrentFrame(env);
        if (serviceName == null && currentObserverContext != null) {
            serviceName = currentObserverContext.getServiceName();
        }
        if (serviceName == null) {
            serviceName = UNKNOWN_SERVICE;
        }

        Tracer tracer = tracerStore.getTracer(serviceName);
        if (tracer == null) {
            return -1;
        }

        ObserverContext observerContext = new ObserverContext();
        observerContext.setServiceName(serviceName);
        observerContext.setResourceName(currentObserverContext != null ? currentObserverContext.getResourceName()
                                                                       : UNKNOWN_RESOURCE);
        tags.forEach((observerContext::addTag));

        if (parentSpanId == SYSTEM_TRACE_INDICATOR) {
            observerContext.setSystemSpan(true);
            if (currentObserverContext != null) {
                observerContext.setParent(currentObserverContext);
            }
            ObserveUtils.setObserverContextToCurrentFrame(env, observerContext);
            return startSpan(observerContext, true, spanName);
        } else if (parentSpanId != ROOT_SPAN_INDICATOR) {
            ObserverContext parentOContext = observerContextMap.get(parentSpanId);
            if (parentOContext == null) {
                return -1;
            }
            observerContext.setParent(parentOContext);
            return startSpan(observerContext, true, spanName);
        }

        return startSpan(observerContext, true, spanName);
    }

    /**
     * Method to mark a span as finished.
     *
     *
     * @param env current environment
     * @param spanId id of the Span
     * @return boolean to indicate if span was finished
     */
    public boolean finishSpan(Environment env, long spanId) {
        if (!enabled) {
            return false;
        }
        ObserverContext observerContext = observerContextMap.get(spanId);
        if (observerContext != null) {
            if (observerContext.isSystemSpan()) {
                ObserveUtils.setObserverContextToCurrentFrame(env, observerContext.getParent());
            }
            TracingUtils.stopObservation(observerContext);
            observerContext.setFinished();
            observerContextMap.remove(spanId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to add tags to an existing span.
     *
     * @param env current environment
     * @param tagKey the key of the tag
     * @param tagValue the value of the tag
     * @param spanId id of the Span
     * @return boolean to indicate if tag was added to the span
     */
    public boolean addTag(Environment env, String tagKey, String tagValue, long spanId) {
        if (!enabled) {
            return false;
        }
        if (spanId == -1) {
            ObserverContext observer = ObserveUtils.getObserverContextOfCurrentFrame(env);
            if (observer != null) {
                observer.addTag(tagKey, tagValue);
                return true;
            }
        }
        ObserverContext observerContext = observerContextMap.get(spanId);
        if (observerContext != null) {
            observerContext.addTag(tagKey, tagValue);
            return true;
        } else {
            return false;
        }
    }
}
