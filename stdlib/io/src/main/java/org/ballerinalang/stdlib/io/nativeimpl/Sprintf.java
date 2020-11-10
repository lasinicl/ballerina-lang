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
package org.ballerinalang.stdlib.io.nativeimpl;

import io.ballerina.runtime.api.TypeTags;
import io.ballerina.runtime.api.creators.ErrorCreator;
import io.ballerina.runtime.api.types.ArrayType;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.utils.TypeUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BString;

import java.util.IllegalFormatConversionException;

/**
 * Extern function ballerina/io#sprintf.
 *
 * @since 0.964.0
 */
public class Sprintf {

    private static final BString NOT_ENOUGH_FORMAT_ARGUMENTS = StringUtils.fromString("not enough format arguments");

    private Sprintf() {
    }

    /**
     * sprintf accept a format specifier and a list of arguments in an array and returns a formatted
     * string. Examples:
     * sprintf("%s is awesome!", ["Ballerina"]) = "Ballerina is awesome!"
     * sprintf("%10.2f", [12.5678]) = "     12.57"
     *
     * @param format A format string
     * @param args   Arguments referenced by the format specifiers in the format string
     * @return a formatted string that specified as in format
     */
    public static BString sprintf(BString format, Object... args) {
        StringBuilder result = new StringBuilder();

        /* Special chars in case additional formatting is required later
         *
         * Primitive built-in types (Same as Java String.format())
         * b            boolean
         * B            boolean (ALL_CAPS)
         * d            int
         * f            float
         * s            string
         * x            hex
         * X            HEX (ALL_CAPS)
         *
         * s            is applicable for any of the supported types in Ballerina. These values will be converted to
         * their string representation and displayed.
         */

        // i keeps index for checking %
        // j reads format specifier to apply
        // k records number of format specifiers seen so far, used to read respective array element
        for (int i = 0, j, k = 0; i < format.length(); i++) {
            if (format.getValue().charAt(i) == '%' && ((i + 1) < format.length())) {

                // skip % character
                j = i + 1;

                if (k >= args.length) {
                    // there's not enough arguments
                    throw ErrorCreator.createError(NOT_ENOUGH_FORMAT_ARGUMENTS);
                }
                StringBuilder padding = new StringBuilder();
                while (Character.isDigit(format.getValue().charAt(j)) || format.getValue().charAt(j) == '.') {
                    padding.append(format.getValue().charAt(j));
                    j += 1;
                }
                try {
                    char formatSpecifier = format.getValue().charAt(j);
                    Object ref = args[k];
                    switch (formatSpecifier) {
                        case 'b':
                        case 'B':
                        case 'd':
                        case 'f':
                            if (ref == null) {
                                throw ErrorCreator
                                        .createError(StringUtils.fromString("illegal format conversion '" +
                                                                                    format.getValue().charAt(j) +
                                                                                    " != ()'"));
                            }
                            result.append(String.format("%" + padding + formatSpecifier, ref));
                            break;
                        case 'x':
                        case 'X':
                            if (ref == null) {
                                throw ErrorCreator
                                        .createError(StringUtils.fromString("illegal format conversion '" +
                                                                                    format.getValue().charAt(j) +
                                                                                    " != ()'"));
                            }
                            formatHexString(result, k, padding, formatSpecifier, args);
                            break;
                        case 's':
                            if (ref != null) {
                                result.append(String.format("%" + padding + "s",
                                                            StringUtils.getStringValue(ref, null)));
                            }
                            break;
                        case '%':
                            result.append("%");
                            break;
                        default:
                            // format string not supported
                            throw ErrorCreator
                                    .createError(StringUtils.fromString("unknown format conversion '" +
                                                                                format.getValue().charAt(j) + "'"));
                    }
                } catch (IllegalFormatConversionException e) {
                    throw ErrorCreator.createError(StringUtils.fromString("illegal format conversion '" +
                                                                                  format.getValue().charAt(j) + " != " +
                                                                                  TypeUtils.getType(args[k]) + "'"));
                }
                if (format.getValue().charAt(j) == '%') {
                    // special case %%, don't count as a format specifier
                    i++;
                } else {
                    k++;
                    i = j;
                }
                continue;
            }
            // no match, copy and continue
            result.append(format.getValue().charAt(i));
        }
        return StringUtils.fromString(result.toString());
    }

    private static void formatHexString(StringBuilder result, int k, StringBuilder padding, char x, Object... args) {
        final Object argsValues = args[k];
        final Type type = TypeUtils.getType(argsValues);
        if (TypeTags.ARRAY_TAG == type.getTag() && TypeTags.BYTE_TAG == ((ArrayType) type).getElementType().getTag()) {
            BArray byteArray = ((BArray) argsValues);
            for (int i = 0; i < byteArray.size(); i++) {
                result.append(String.format("%" + padding + x, byteArray.getByte(i)));
            }
        } else {
            result.append(String.format("%" + padding + x, argsValues));
        }
    }
}
