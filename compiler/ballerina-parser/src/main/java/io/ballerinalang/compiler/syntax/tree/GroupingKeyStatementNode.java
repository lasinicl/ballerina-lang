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
package io.ballerinalang.compiler.syntax.tree;

import io.ballerinalang.compiler.internal.parser.tree.STNode;

import java.util.Objects;

/**
 * This is a generated syntax tree node.
 *
 * @since 2.0.0
 */
public class GroupingKeyStatementNode extends GroupingKeyNode {

    public GroupingKeyStatementNode(STNode internalNode, int position, NonTerminalNode parent) {
        super(internalNode, position, parent);
    }

    public TypeDescriptorNode typeDescriptor() {
        return childInBucket(0);
    }

    public IdentifierToken variableName() {
        return childInBucket(1);
    }

    public Token equalToken() {
        return childInBucket(2);
    }

    public ExpressionNode expression() {
        return childInBucket(3);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <T> T apply(NodeTransformer<T> visitor) {
        return visitor.transform(this);
    }

    @Override
    protected String[] childNames() {
        return new String[]{
                "typeDescriptor",
                "variableName",
                "equalToken",
                "expression"};
    }

    public GroupingKeyStatementNode modify(
            TypeDescriptorNode typeDescriptor,
            IdentifierToken variableName,
            Token equalToken,
            ExpressionNode expression) {
        if (checkForReferenceEquality(
                typeDescriptor,
                variableName,
                equalToken,
                expression)) {
            return this;
        }

        return NodeFactory.createGroupingKeyStatementNode(
                typeDescriptor,
                variableName,
                equalToken,
                expression);
    }

    public GroupingKeyStatementNodeModifier modify() {
        return new GroupingKeyStatementNodeModifier(this);
    }

    /**
     * This is a generated tree node modifier utility.
     *
     * @since 2.0.0
     */
    public static class GroupingKeyStatementNodeModifier {
        private final GroupingKeyStatementNode oldNode;
        private TypeDescriptorNode typeDescriptor;
        private IdentifierToken variableName;
        private Token equalToken;
        private ExpressionNode expression;

        public GroupingKeyStatementNodeModifier(GroupingKeyStatementNode oldNode) {
            this.oldNode = oldNode;
            this.typeDescriptor = oldNode.typeDescriptor();
            this.variableName = oldNode.variableName();
            this.equalToken = oldNode.equalToken();
            this.expression = oldNode.expression();
        }

        public GroupingKeyStatementNodeModifier withTypeDescriptor(
                TypeDescriptorNode typeDescriptor) {
            Objects.requireNonNull(typeDescriptor, "typeDescriptor must not be null");
            this.typeDescriptor = typeDescriptor;
            return this;
        }

        public GroupingKeyStatementNodeModifier withVariableName(
                IdentifierToken variableName) {
            Objects.requireNonNull(variableName, "variableName must not be null");
            this.variableName = variableName;
            return this;
        }

        public GroupingKeyStatementNodeModifier withEqualToken(
                Token equalToken) {
            Objects.requireNonNull(equalToken, "equalToken must not be null");
            this.equalToken = equalToken;
            return this;
        }

        public GroupingKeyStatementNodeModifier withExpression(
                ExpressionNode expression) {
            Objects.requireNonNull(expression, "expression must not be null");
            this.expression = expression;
            return this;
        }

        public GroupingKeyStatementNode apply() {
            return oldNode.modify(
                    typeDescriptor,
                    variableName,
                    equalToken,
                    expression);
        }
    }
}
