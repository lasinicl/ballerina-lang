type Person record {|
   int id;
   string fname;
   string lname;
|};

function testSimpleQueryExpr() returns Person[]{
    Person p1 = {id: 1, fname: "Alex", lname: "George"};
    Person p2 = {id: 6, fname: "Ranjan", lname: "Fonseka"};
    Person p3 = {id: 4, fname: "Nina", lname: "Frost"};
    Person p4 = {id: 2, fname: "Sanjiva", lname: "Weeravarana"};

    Person[] personList = [p1, p2];

    Person[] op =
       from var person in personList
       //order by person.id ascending
       //let int stid = 1
       where person.id == 1
       select {
           id: person.id,
           fname : person.fname,
           lname : person.lname
       };

    return op;
}
