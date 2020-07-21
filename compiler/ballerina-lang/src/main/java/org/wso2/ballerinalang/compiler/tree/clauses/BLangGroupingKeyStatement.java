package org.wso2.ballerinalang.compiler.tree.clauses;

import org.ballerinalang.model.tree.IdentifierNode;
import org.ballerinalang.model.tree.NodeKind;
import org.ballerinalang.model.tree.expressions.ExpressionNode;
import org.wso2.ballerinalang.compiler.semantics.model.types.BType;
import org.wso2.ballerinalang.compiler.tree.BLangIdentifier;
import org.wso2.ballerinalang.compiler.tree.BLangNodeVisitor;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangExpression;

/**
 * Implementation of grouping-key in "group by" clause statement.
 * grouping-key: type-descriptor variable-name = expression.
 *
 * @since Swan Lake
 */
public class BLangGroupingKeyStatement extends BLangGroupingKey {
    public BLangExpression expression;
    public IdentifierNode variableNameIdentifier;
    public BType varType; // T
    public BType resultType; // map<T>
    public BType nillableResultType; // map<T>?
    public boolean isDeclaredWithVar;

    @Override
    public NodeKind getKind() {
        return NodeKind.GROUPING_KEY_VARIABLE;
    }

    @Override
    public void accept(BLangNodeVisitor visitor) {
        visitor.visit(this);
    }

    public boolean setDeclaredWithVar() {
        return false;
    }

    public boolean isDeclaredWithVar() {
        return isDeclaredWithVar;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = (BLangExpression) expression;
    }

    @Override
    public String toString() {
        if (isDeclaredWithVar) {
            return "var " + variableNameIdentifier + " = " + expression;
        }
        return String.valueOf(type) + " " + variableNameIdentifier + " = " + expression;
    }
}
