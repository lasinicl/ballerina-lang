type DeptPerson record {|
   string fname;
   string lname;
   string? dept;
|};

type Department record {|
   int id;
   string name;
|};

type Person record {|
   int id;
   string fname;
   string lname;
|};

type DeptPersonValue record {|
    DeptPerson value;
|};

function getDeptPersonValue((record {| DeptPerson value; |}|error?)|(record {| DeptPerson value; |}?) returnedVal)
returns DeptPersonValue? {
    var result = returnedVal;
    if (result is DeptPersonValue) {
        return result;
    } else {
        return ();
    }
}

function condition(string name) returns boolean{
    return name == "Alex";
}

function testSimpleJoinClauseWithRecordVariable() returns DeptPerson[]{
    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 2, fname: "Ranjan", lname: "Fonseka"};

    Department d1 = {id: 1, name:"HR"};
    Department d2 = {id: 2, name:"Operations"};

    Person[] personList = [p1, p2];
    Department[] deptList = [d1, d2];

    DeptPerson[] deptPersonList =
       from var person in personList
       join Department dept in deptList
       on person.id equals dept.id
       select {
           fname : person.fname,
           lname : person.lname,
           dept : dept.name
       };

    return deptPersonList;
}

function testSimpleJoinClauseWithRecordVariable2() returns DeptPerson[]{
    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 2, fname: "Ranjan", lname: "Fonseka"};

    Department d1 = {id: 1, name:"HR"};
    Department d2 = {id: 2, name:"Operations"};

    Person[] personList = [p1, p2];
    Department[] deptList = [d1, d2];

    DeptPerson[] deptPersonList =
       from var person in personList
       join var {id: deptId, name: deptName} in deptList
       on person.id equals deptId
       select {
           fname : person.fname,
           lname : person.lname,
           dept : deptName
       };

    return deptPersonList;
}

function testSimpleJoinClauseWithRecordVariable3() returns DeptPerson[]{
    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 2, fname: "Ranjan", lname: "Fonseka"};

    Department d1 = {id: 1, name:"HR"};
    Department d2 = {id: 2, name:"Operations"};

    Person[] personList = [p1, p2];
    Department[] deptList = [d1, d2];

    DeptPerson[] deptPersonList =
       from var person in personList
       join var {id,name} in deptList
       on person.id equals id
       select {
           fname : person.fname,
           lname : person.lname,
           dept : name
       };

    return deptPersonList;
}

function testJoinClauseWithStream() returns boolean {
    boolean testPassed = true;

    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 2, fname: "Ranjan", lname: "Fonseka"};

    Department d1 = {id: 1, name:"HR"};
    Department d2 = {id: 2, name:"Operations"};

    Person[] personList = [p1, p2];
    Department[] deptList = [d1, d2];

    stream<Person> personStream = personList.toStream();
    stream<Department> deptStream = deptList.toStream();

    stream<DeptPerson> deptPersonStream =
       stream from var person in personStream
       join var dept in deptStream
       on person.id equals dept.id
       select {
           fname : person.fname,
           lname : person.lname,
           dept : dept.name
       };

    record {| DeptPerson value; |}? deptPerson = getDeptPersonValue(deptPersonStream.next());
    testPassed = testPassed && deptPerson?.value?.fname == "Alex" && deptPerson?.value?.lname == "George"
                             && deptPerson?.value?.dept == "HR";

    return testPassed;
}

function testJoinClauseWithLimit() returns DeptPerson[]{
    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 2, fname: "Ranjan", lname: "Fonseka"};

    Department d1 = {id: 1, name:"HR"};
    Department d2 = {id: 2, name:"Operations"};

    Person[] personList = [p1, p2];
    Department[] deptList = [d1, d2];

    DeptPerson[] deptPersonList =
       from var person in personList
       join Department dept in deptList
       on person.id equals dept.id
       select {
           fname : person.fname,
           lname : person.lname,
           dept : dept.name
       }
       limit 1;

    return deptPersonList;
}

function testOnClauseWithFunction() returns DeptPerson[]{
    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 2, fname: "Ranjan", lname: "Fonseka"};

    Department d1 = {id: 1, name:"HR"};
    Department d2 = {id: 2, name:"Operations"};

    Person[] personList = [p1, p2];
    Department[] deptList = [d1, d2];

    DeptPerson[] deptPersonList =
       from var person in personList
       join Department dept in deptList
       on condition(person.fname)
       select {
           fname : person.fname,
           lname : person.lname,
           dept : dept.name
       };

    return deptPersonList;
}

function testOuterJoinClauseWithRecordVariable() returns boolean {
    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 2, fname: "Ranjan", lname: "Fonseka"};

    Department d1 = {id: 1, name:"HR"};
    Department d2 = {id: 2, name:"Operations"};

    Person[] personList = [p1, p2];
    Department[] deptList = [d1, d2];

    DeptPerson[] deptPersonList =
       from var person in personList
       outer join Department dept in deptList
       on person.id equals dept.id
       select {
           fname : person.fname,
           lname : person.lname,
           dept : dept.name
       };

    boolean testPassed = true;
    DeptPerson dp;
    any res = deptPersonList;
    testPassed = testPassed && res is DeptPerson[];
    testPassed = testPassed && res is (any|error)[];
    testPassed = testPassed && deptPersonList.length() == 4;
    dp = deptPersonList[0];
    testPassed = testPassed && dp.fname == "Alex" && dp.lname == "George" && dp.dept == "HR";
    dp = deptPersonList[1];
    testPassed = testPassed && dp.fname == "Alex" && dp.lname == "George" && dp.dept is ();
    dp = deptPersonList[2];
    testPassed = testPassed && dp.fname == "Ranjan" && dp.lname == "Fonseka" && dp.dept is ();
    dp = deptPersonList[3];
    testPassed = testPassed && dp.fname == "Ranjan" && dp.lname == "Fonseka" && dp.dept == "Operations";
    return testPassed;
}

function testOuterJoinClauseWithRecordVariable2() returns boolean {
    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 2, fname: "Ranjan", lname: "Fonseka"};

    Department d1 = {id: 1, name:"HR"};
    Department d2 = {id: 2, name:"Operations"};

    Person[] personList = [p1, p2];
    Department[] deptList = [d1, d2];

    DeptPerson[] deptPersonList =
       from var person in personList
       outer join var {id: deptId, name: deptName} in deptList
       on person.id equals deptId
       select {
           fname : person.fname,
           lname : person.lname,
           dept : deptName
       };

    boolean testPassed = true;
    DeptPerson dp;
    any res = deptPersonList;
    testPassed = testPassed && res is DeptPerson[];
    testPassed = testPassed && res is (any|error)[];
    testPassed = testPassed && deptPersonList.length() == 4;
    dp = deptPersonList[0];
    testPassed = testPassed && dp.fname == "Alex" && dp.lname == "George" && dp.dept == "HR";
    dp = deptPersonList[1];
    testPassed = testPassed && dp.fname == "Alex" && dp.lname == "George" && dp.dept is ();
    dp = deptPersonList[2];
    testPassed = testPassed && dp.fname == "Ranjan" && dp.lname == "Fonseka" && dp.dept is ();
    dp = deptPersonList[3];
    testPassed = testPassed && dp.fname == "Ranjan" && dp.lname == "Fonseka" && dp.dept == "Operations";
    return testPassed;
}

function testOuterJoinClauseWithRecordVariable3() returns boolean {
    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 2, fname: "Ranjan", lname: "Fonseka"};

    Department d1 = {id: 1, name:"HR"};
    Department d2 = {id: 2, name:"Operations"};

    Person[] personList = [p1, p2];
    Department[] deptList = [d1, d2];

    DeptPerson[] deptPersonList =
       from var person in personList
       outer join var {id,name} in deptList
       on person.id equals id
       select {
           fname : person.fname,
           lname : person.lname,
           dept : name
       };

    boolean testPassed = true;
    DeptPerson dp;
    any res = deptPersonList;
    testPassed = testPassed && res is DeptPerson[];
    testPassed = testPassed && res is (any|error)[];
    testPassed = testPassed && deptPersonList.length() == 4;
    dp = deptPersonList[0];
    testPassed = testPassed && dp.fname == "Alex" && dp.lname == "George" && dp.dept == "HR";
    dp = deptPersonList[1];
    testPassed = testPassed && dp.fname == "Alex" && dp.lname == "George" && dp.dept is ();
    dp = deptPersonList[2];
    testPassed = testPassed && dp.fname == "Ranjan" && dp.lname == "Fonseka" && dp.dept is ();
    dp = deptPersonList[3];
    testPassed = testPassed && dp.fname == "Ranjan" && dp.lname == "Fonseka" && dp.dept == "Operations";
    return testPassed;
}

function testOuterJoinClauseWithStream() returns boolean {
    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 2, fname: "Ranjan", lname: "Fonseka"};

    Department d1 = {id: 1, name:"HR"};
    Department d2 = {id: 2, name:"Operations"};

    Person[] personList = [p1, p2];
    Department[] deptList = [d1, d2];

    stream<Person> personStream = personList.toStream();
    stream<Department> deptStream = deptList.toStream();

    stream<DeptPerson> deptPersonStream =
       stream from var person in personStream
       outer join var dept in deptStream
       on person.id equals dept.id
       select {
           fname : person.fname,
           lname : person.lname,
           dept : dept.name
       };

    boolean testPassed = true;
    record {| DeptPerson value; |}? deptPerson = getDeptPersonValue(deptPersonStream.next());
    testPassed = testPassed && deptPerson?.value?.fname == "Alex" && deptPerson?.value?.lname == "George"
                             && deptPerson?.value?.dept == "HR";
    return testPassed;
}

function testOuterJoinClauseWithLimit() returns boolean {
    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 2, fname: "Ranjan", lname: "Fonseka"};

    Department d1 = {id: 1, name:"HR"};
    Department d2 = {id: 2, name:"Operations"};

    Person[] personList = [p1, p2];
    Department[] deptList = [d1, d2];

    DeptPerson[] deptPersonList =
       from var person in personList
       outer join Department dept in deptList
       on person.id equals dept.id
       select {
           fname : person.fname,
           lname : person.lname,
           dept : dept.name
       }
       limit 2;

    boolean testPassed = true;
    DeptPerson dp;
    any res = deptPersonList;
    testPassed = testPassed && res is DeptPerson[];
    testPassed = testPassed && res is (any|error)[];
    testPassed = testPassed && deptPersonList.length() == 2;
    dp = deptPersonList[0];
    testPassed = testPassed && dp.fname == "Alex" && dp.lname == "George" && dp.dept == "HR";
    dp = deptPersonList[1];
    testPassed = testPassed && dp.fname == "Alex" && dp.lname == "George" && dp.dept is ();
    return testPassed;
}

function testOuterJoinWithOnClauseWithFunction() returns boolean {
    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 2, fname: "Ranjan", lname: "Fonseka"};

    Department d1 = {id: 1, name:"HR"};
    Department d2 = {id: 2, name:"Operations"};

    Person[] personList = [p1, p2];
    Department[] deptList = [d1, d2];

    DeptPerson[] deptPersonList =
       from var person in personList
       outer join Department dept in deptList
       on condition(person.fname)
       select {
           fname : person.fname,
           lname : person.lname,
           dept : dept.name
       };

    boolean testPassed = true;
    DeptPerson dp;
    any res = deptPersonList;
    testPassed = testPassed && res is DeptPerson[];
    testPassed = testPassed && res is (any|error)[];
    testPassed = testPassed && deptPersonList.length() == 4;
    dp = deptPersonList[0];
    testPassed = testPassed && dp.fname == "Alex" && dp.lname == "George" && dp.dept == "HR";
    dp = deptPersonList[1];
    testPassed = testPassed && dp.fname == "Alex" && dp.lname == "George" && dp.dept == "Operations";
    dp = deptPersonList[2];
    testPassed = testPassed && dp.fname == "Ranjan" && dp.lname == "Fonseka" && dp.dept is ();
    dp = deptPersonList[3];
    testPassed = testPassed && dp.fname == "Ranjan" && dp.lname == "Fonseka" && dp.dept is ();
    return testPassed;
}
