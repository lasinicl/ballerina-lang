/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.ballerina.compiler.api.impl.symbols;

import io.ballerina.compiler.api.ModuleID;
import io.ballerina.compiler.api.symbols.TypeDescKind;
import io.ballerina.compiler.api.symbols.TypeSymbol;
import io.ballerina.compiler.api.symbols.XMLElementTypeSymbol;
import org.wso2.ballerinalang.compiler.semantics.model.types.BXMLSubType;
import org.wso2.ballerinalang.compiler.util.CompilerContext;
import org.wso2.ballerinalang.compiler.util.Names;

import java.util.Optional;

/**
 * Represents the xml:Element type descriptor.
 *
 * @since 2.0.0
 */
public class BallerinaXMLElementTypeSymbol extends AbstractTypeSymbol implements XMLElementTypeSymbol {

    public BallerinaXMLElementTypeSymbol(CompilerContext context, ModuleID moduleID, BXMLSubType elementType) {
        super(context, TypeDescKind.XML, moduleID, elementType);
    }

    @Override
    public Optional<TypeSymbol> typeParameter() {
        return Optional.empty();
    }

    @Override
    public String name() {
        return Names.STRING_XML_ELEMENT;
    }

    @Override
    public Kind xmlKind() {
        return Kind.ELEMENT;
    }

    @Override
    public String signature() {
        return Names.STRING_XML_ELEMENT;
    }
}
