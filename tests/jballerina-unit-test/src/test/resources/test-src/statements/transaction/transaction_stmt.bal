import ballerina/io;
import ballerina/lang.'transaction as transactions;

function testRollback() {
    string|error x =  trap actualCode(0, false);
    if(x is string) {
        assertEquality("start fc-0 inTrx Commit endTrx end", x);
    }
}

function testCommit() {
    string|error x =  trap actualCode(0, false);
    if(x is string) {
        assertEquality("start fc-0 inTrx Commit endTrx end", x);
    }
}

function testPanic() {
    string|error x =  trap actualCode(1, false);
    if(x is string) {
        assertEquality("start fc-1 inTrx blowUp", x);
    }
}

function actualCode(int failureCutOff, boolean requestRollback) returns (string) {
    string a = "";
    a = a + "start";
    a = a + " fc-" + failureCutOff.toString();
    int count = 0;

    transaction {
        a = a + " inTrx";
        count = count + 1;
        if transactional {
            io:println("Transactional mode");
        }
        if (count <= failureCutOff) {
            a = a + " blowUp"; // transaction block panic scenario, Set failure cutoff to 0, for not blowing up.
            int bV = blowUp();
        }
        if (requestRollback) { // Set requestRollback to true if you want to try rollback scenario, otherwise commit
            a = a + " Rollback";
            rollback;
        } else {
            a = a + " Commit";
            var i = commit;
        }
        a = a + " endTrx";
        a = (a + " end");
    }

    io:println("## Transaction execution completed ##");
    return a;
}

function blowUp()  returns int {
    if (5 == 5) {
        error err = error("TransactionError");
        panic err;
    }
    return 5;
}

function testLocalTransaction1(int i) returns int|error {
    int x = i;

    transaction {
        x += 1;
        check commit;
    }

    transaction {
        x += 1;
        check commit;
    }
    return x;
}

function testLocalTransaction2(int i) returns int|error {
    int x = i;

    transaction {
        x += 1;
        check commit;
    }

    return x;
}

function testMultipleTrxBlocks() returns error? {
    int i = check testLocalTransaction1(1);
    int j = check testLocalTransaction2(i);

    assertEquality(4, j);
}

type AssertionError error;

const ASSERTION_ERROR_REASON = "AssertionError";

function assertEquality(any|error expected, any|error actual) {
    if expected is anydata && actual is anydata && expected == actual {
        return;
    }

    if expected === actual {
        return;
    }

    panic AssertionError(ASSERTION_ERROR_REASON,
            message = "expected '" + expected.toString() + "', found '" + actual.toString () + "'");
}

string ss = "";
function testTrxHandlers() returns string {
    ss = ss + "started";
    transactions:Info transInfo;
    var onRollbackFunc = function(transactions:Info? info, error? cause, boolean willTry) {
        ss = ss + " trxAborted";
    };

    var onCommitFunc = function(transactions:Info? info) {
        ss = ss + " trxCommited";
    };

    transaction {
        transInfo = transactions:info();
        transactions:onRollback(onRollbackFunc);
        transactions:onCommit(onCommitFunc);
        trxfunction();
        var commitRes = commit;
    }
    ss += " endTrx";
    return ss;
}

transactional function trxfunction() {
    ss = ss + " within transactional func";
}

public function testTransactionInsideIfStmt() returns int {
    int a = 10;
    if (a == 10) {
        int c = 8;
        transaction {
            int b = a + c;
            a = b;
            var commitRes = commit;
        }
    }
    return a;
}

public function testArrowFunctionInsideTransaction() returns int {
    int a = 10;
    int b = 11;
    transaction {
        int c = a + b;
        function (int, int) returns int arrow = (x, y) => x + y + a + b + c;
        a = arrow(1, 1);
        var commitRes = commit;
    }
    return a;
}

public function testAssignmentToUninitializedVariableOfOuterScopeFromTrxBlock() returns int|string {
    int|string s;
    transaction {
        s = "init-in-transaction-block";
        var commitRes = commit;
    }
    return s;
}

function testTrxReturnVal() returns string {
    string str = "start";
    transaction {
        str = str + " within transaction";
        var commitRes = commit;
        str = str + " end.";
        return str;
    }
}

function testInvokingTrxFunc() returns string {
    string str = "start";
    string res = funcWithTrx(str);
    return res + " end.";

}

function funcWithTrx(string str) returns string {
    transaction {
        string res = str + " within transaction";
        var commitRes = commit;
        return res;
    }
}
