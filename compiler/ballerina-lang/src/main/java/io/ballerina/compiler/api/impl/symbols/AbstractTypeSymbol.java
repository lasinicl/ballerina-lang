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
import io.ballerina.compiler.api.symbols.Documentation;
import io.ballerina.compiler.api.symbols.MethodSymbol;
import io.ballerina.compiler.api.symbols.SymbolKind;
import io.ballerina.compiler.api.symbols.TypeDescKind;
import io.ballerina.compiler.api.symbols.TypeSymbol;
import io.ballerina.tools.diagnostics.Location;
import org.wso2.ballerinalang.compiler.semantics.model.types.BType;
import org.wso2.ballerinalang.compiler.util.CompilerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a Ballerina Type Descriptor.
 *
 * @since 2.0.0
 */
public abstract class AbstractTypeSymbol implements TypeSymbol {

    protected final CompilerContext context;

    private final TypeDescKind typeDescKind;
    private final ModuleID moduleID;
    private final BType bType;

    public AbstractTypeSymbol(CompilerContext context, TypeDescKind typeDescKind, ModuleID moduleID, BType bType) {
        this.context = context;
        this.typeDescKind = typeDescKind;
        this.moduleID = moduleID;
        this.bType = bType;
    }

    @Override
    public TypeDescKind typeKind() {
        return typeDescKind;
    }

    @Override
    public ModuleID moduleID() {
        return moduleID;
    }

    @Override
    public abstract String signature();

    @Override
    public String name() {
        return "";
    }

    @Override
    public SymbolKind kind() {
        return SymbolKind.TYPE;
    }

    @Override
    public Optional<Documentation> docAttachment() {
        return Optional.empty();
    }

    @Override
    public Location location() {
        return null;
    }

    @Override
    public List<MethodSymbol> builtinMethods() {
        return new ArrayList<>();
    }

    /**
     * Get the BType.
     * 
     * @return {@link BType} associated with the type desc
     */
    protected BType getBType() {
        return bType;
    }
}
