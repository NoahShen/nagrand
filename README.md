Nagrand
==========

Nagrand is a lightweight groovy orm

-------

## How To Use
Maven dependency
```xml
<dependency>
    <groupId>io.github.noahshen</groupId>
    <artifactId>nagrand</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

You need to register classes through Nagrand on app start.

```groovy
Nagrand nagrand = ...
nagrand.register(Person)
```

## Basic Samples

### Entity defination
```groovy
@Entity
class Person {
    def name
    int age
}
```

### Create
```groovy
def person = new Person(name: 'Spiderman', age: 30)
person.save()
```
### Update
```groovy
def person = new Person(name: 'Spiderman', age: 30)
person.save()

person.name = 'Batman'
person.save()
```

### Delete
```groovy
def person = new Person(name: 'Spiderman', age: 30)

//...

person.delete()
```

### Query

### Get entity by ID
```groovy
Integer id = 1
Person p = Person.get(id)
if (!p) {
   //... not found
}
```

### Dynamic Finders

Dynamic finder looks like a static method invocation.
The method is auto-generated using code synthesis at runtime, based on the properties of a entity class. 

```groovy
new Person(name: 'Spiderman', age: 30).save()
new Person(name: 'Batman', age: 31).save()
new Person(name: 'Superman', age: 32).save()
new Person(name: 'Ironman', age: 32).save()

def persons = Person.findByAge(32)
assert persons.size() == 2
assert persons*.name == ["Superman", "Ironman"]

def batman = Person.findFirstByName("Batman")
assert batman.name == "Batman"
```

Find by multi properties
```groovy
List<Person> persons = Person.findByNameAndAge('Superman', 32)
assert persons.size() == 1
assert persons*.name == ["Superman"]
```
Find by more options
```groovy
List<Person> persons = Person.findByAge(32) {
    order("name", "desc")
}
assert persons.size() == 2
assert persons[0].name == "Superman"
```

### Where Query

The where method is more flexible than dynamic finders

by using the following methods:

 - `.find {}`
 - `.findWhere {}`
 - `.where {}`
 - `.findFirst {}`
 - `.findFirstWhere {}`


```groovy
new Person(name: 'Spiderman', age: 30).save()
new Person(name: 'Batman', age: 31).save()
new Person(name: 'Superman', age: 32).save()
new Person(name: 'Ironman', age: 32).save()

List<Person> persons = Person.find {
    eq "name", "Superman"
    eq "age", 32
}
assert persons.size() == 1
assert persons*.name == ["Superman"]
```

More condition:
```groovy
def results = Account.find {
    between("balance", 500, 1000)
    eq("bankName", "boc")
    or {
        like("firstName", "Noah%")
        like("firstName", "Sara%")
    }
    maxResults(10)
    order("balance", "desc")
}
```

### Groovy-style query
Nagrand supports groovy-style query by providing an enhanced, compile-time checked query DSL for common queries

```groovy
def person = Person.where {
   firstName == "Ironman"
}
```

```groovy
def person = Person.where {
    (lastName != "Shen" && firstName != "Noah") || (firstName == "Sara" && age > 20)
}
```

Groovy operator maps onto a where method. 
The following table provides a map of Groovy operators to methods:

| Operator   | where method | description  |
| --------   | -----   | ----  |
| ==         | eq      | Equal to   |
| !=         | nq      | Not equal to   |
| >          | gt      | Greater than  |
| <          | lt      | Less than |
| \>=        | ge      | Greater than or equal to |
| <=         | le      | Less than or equal to |
| in         | inList  | Contained within the given list |


### Events

`.beforeInsert`
Called before first save
```groovy
class Item {
  void beforeInsert() {
    ...
  }
}
```

`.beforeUpdate`
Called before object update
```groovy
class Item {
  void beforeUpdate() {
    ...
  }
}
```

### Optimistic Locking and Version
Nagrand uses optimistic locking by a version property which is in turn mapped to a version column at the database.

```groovy
class ClassWithVersion {
    Integer id
    String name
    Integer version
}
```

```groovy
def entity = new ClassWithVersion(name: 'Spiderman').save()
assert entity.version == 1

entity.name = "Superman"
entity.save()
assert entity.version == 2
```

## License
Project is licensed under Apache 2 license.