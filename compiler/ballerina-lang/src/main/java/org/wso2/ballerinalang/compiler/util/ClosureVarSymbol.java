/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.wso2.ballerinalang.compiler.util;

import io.ballerina.tools.diagnostics.Location;
import org.wso2.ballerinalang.compiler.semantics.model.symbols.BSymbol;

/**
 * Class to track the closure variable symbols of lambads and arrow function expression with their positions which will
 * be used in the data flow analyzer to track uninitialized variables.
 *
 * @since 0.990.5
 */
public class ClosureVarSymbol {
    public BSymbol bSymbol;
    public Location diagnosticLocation;

    public ClosureVarSymbol(BSymbol bSymbol, Location diagnosticLocation) {
        this.bSymbol = bSymbol;
        this.diagnosticLocation = diagnosticLocation;
    }
}
