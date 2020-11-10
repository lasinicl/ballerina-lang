/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.stdlib.runtime.nativeimpl;

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;

import java.util.UUID;

/**
 * Extern function to get invocation context record.
 *
 * @since 0.970.0
 */
public class GetInvocationContext {

    public static BMap<BString, Object> getInvocationContext(Environment env) {
        return getInvocationContextRecord(env);
    }

    private static final String RUNTIME_INVOCATION_CONTEXT_PROPERTY = "RuntimeInvocationContext";
    private static final String STRUCT_TYPE_INVOCATION_CONTEXT = "InvocationContext";
    private static final String INVOCATION_ID_KEY = "id";
    private static final String INVOCATION_ATTRIBUTES = "attributes";

    private static BMap<BString, Object> getInvocationContextRecord(Environment env) {
        BMap<BString, Object> invocationContext =
                (BMap<BString, Object>) env.getStrandLocal(RUNTIME_INVOCATION_CONTEXT_PROPERTY);
        if (invocationContext == null) {
            invocationContext = initInvocationContext();
            env.setStrandLocal(RUNTIME_INVOCATION_CONTEXT_PROPERTY, invocationContext);
        }
        return invocationContext;
    }

    private static BMap<BString, Object> initInvocationContext() {
        BMap<BString, Object> invocationContextInfo =
                ValueCreator.createRecordValue(Constants.BALLERINA_RUNTIME_PKG_ID, STRUCT_TYPE_INVOCATION_CONTEXT);
        UUID invocationId = UUID.randomUUID();
        invocationContextInfo.put(StringUtils.fromString(INVOCATION_ID_KEY),
                                  StringUtils.fromString(invocationId.toString()));
        invocationContextInfo.put(StringUtils.fromString(INVOCATION_ATTRIBUTES), ValueCreator.createMapValue());
        return invocationContextInfo;
    }
}
