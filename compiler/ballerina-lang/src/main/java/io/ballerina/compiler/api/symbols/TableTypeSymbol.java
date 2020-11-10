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
package io.ballerina.compiler.api.symbols;

import java.util.List;
import java.util.Optional;

/**
 * Represents the table type descriptor.
 *
 * @since 2.0.0
 */
public interface TableTypeSymbol extends TypeSymbol {

    /**
     * Get the row type parameter.
     *
     * @return {@link TypeSymbol}
     */
    TypeSymbol rowTypeParameter();

    /**
     * Get the key type constraint.
     *
     * @return {@link Optional} type descriptor
     */
    Optional<TypeSymbol> keyConstraintTypeParameter();

    /**
     * Get the list of key specifiers.
     *
     * @return {@link List} of key names
     */
    List<String> keySpecifiers();
}
