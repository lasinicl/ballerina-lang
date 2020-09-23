package io.ballerina.transaction.internal;

import org.ballerinalang.jvm.scheduling.Scheduler;
import org.ballerinalang.jvm.scheduling.Strand;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.ReturnType;

import static org.ballerinalang.util.BLangCompilerConstants.TRANSACTION_INTERNAL_VERSION;

/**
 * Extern function transaction:isTransactional.
 *
 * @since Swan Lake
 */
@BallerinaFunction(
        orgName = "ballerina", packageName = "transaction-internal", version = TRANSACTION_INTERNAL_VERSION,
        functionName = "isTransactional",
        args = {},
        returnType = {@ReturnType(type = TypeKind.BOOLEAN)},
        isPublic = true
)
public class IsTransactional {

    public static boolean isTransactional() {
        Strand strand = Scheduler.getStrand();
        return strand.isInTransaction();
    }
}
