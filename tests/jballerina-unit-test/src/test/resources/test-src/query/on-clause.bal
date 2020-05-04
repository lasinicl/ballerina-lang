import ballerina/io;

type Person record {|
    string firstName;
    string lastName;
    int age;
    int deptId;
|};

type Department record {|
   int deptId;
   string name;
|};

function testSimpleOnClause() returns Person[] {

    Person p1 = {firstName: "Alex", lastName: "George", age: 23, deptId: 1};
    Person p2 = {firstName: "Ranjan", lastName: "Fonseka", age: 30, deptId: 2};
    Person p3 = {firstName: "John", lastName: "David", age: 33, deptId: 1};

    Department d1 = {deptId: 1, name: "HR"};
    Department d2 = {deptId: 2, name: "Finance"};

    Person[] personList = [p1, p2, p3];
    Department[] departmentList = [d1, d2];

    Person[] outputPersonList =
            from var person in personList
            from var department in departmentList
            on person.deptId == department.deptId
            select {
                   firstName: person.firstName,
                   lastName: person.lastName,
                   age: person.age,
                   deptId: person.deptId
            };

    return outputPersonList;
}

function printHello() {
    io:println("Hello");
}
