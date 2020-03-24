/*
*  Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package generated.facade;
import generated.internal.STNode;

public  class ExternalFunctionBody extends NonTerminalNode{
private Node assign;
private Node annotation;
private Token externalKeyword;
private Token semicolon;

public ExternalFunctionBody(STNode node, int position, NonTerminalNode parent) {
super(node, position, parent);
}

public Node assign() {
if (assign != null) {
return assign;
}
assign = node.childInBucket(0).createFacade(getChildPosition(0), this);
return assign;
}
public Node annotation() {
if (annotation != null) {
return annotation;
}
annotation = node.childInBucket(1).createFacade(getChildPosition(1), this);
return annotation;
}
public Token externalKeyword() {
if (externalKeyword != null) {
return externalKeyword;
}
externalKeyword = createToken(2);
return externalKeyword;
}
public Token semicolon() {
if (semicolon != null) {
return semicolon;
}
semicolon = createToken(3);
return semicolon;
}

public Node childInBucket(int bucket) {
switch (bucket) {
case 0:
return assign();
case 1:
return annotation();
case 2:
return externalKeyword();
case 3:
return semicolon();
}
return null;
}
}
