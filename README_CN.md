Nagrand
==========

Nagrand 是一个轻量的groovy orm框架。
基于Groovy的动态特性，Nagrand可以支持动态类型的查询，无需配置即可支持多种条件查询

-------

## 使用方式
Maven依赖
```xml
<dependency>
    <groupId>io.github.noahshen</groupId>
    <artifactId>nagrand</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

注册实体类

```groovy
Nagrand nagrand = ...
nagrand.register(Person)
```

## 基本示例

### 定义Entity
```groovy
@Entity
class Person {
    def name
    int age
}
```

### 创建
```groovy
def person = new Person(name: 'Spiderman', age: 30)
person.save()
```
### 更新
```groovy
def person = new Person(name: 'Spiderman', age: 30)
person.save()

person.name = 'Batman'
person.save()
```

### 删除
```groovy
def person = new Person(name: 'Spiderman', age: 30)

//...

person.delete()
```

### 查询

### 基于ID进行查询
```groovy
Integer id = 1
Person p = Person.get(id)
if (!p) {
   //... not found
}
```

### 动态查询

动态查询可以让一个类基于它的各种属性进行查询，就像是类自带的方法一样：

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

多个属性查询

```groovy
List<Person> persons = Person.findByNameAndAge('Superman', 32)
assert persons.size() == 1
assert persons*.name == ["Superman"]
```

跟多其他查询条件

```groovy
List<Person> persons = Person.findByAge(32) {
    order("name", "desc")
}
assert persons.size() == 2
assert persons[0].name == "Superman"
```

### Where查询

Where查询比起动态Find查询更灵活
可以使用如下方法使用where查询:

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

其他查询条件:

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

### Groovy-style 查询

Nagrand还支持grooovy-style风格的查询，支持编译时校验的DSL风格的查询方式

```groovy
def person = Person.where {
   firstName == "Ironman"
}
```

需要注意的是 `firstName == "Ironman"` 条件本身不会执行，Nagrand是在groovy **编译代码**时，拦截编译过程，将 `firstName == "Ironman"` 这段代码**替换**为 `eq( "firstName", "Ironman")`

所以这种方式完全等价于：
```groovy
def person = Person.where {
   eq( "firstName", "Ironman")
}
```
但是groovy-style的查询方式更直观，而且在编译时就可以校验你的查询条件是否正确

比如，如果使用一个**不存在**的属性作为查询条件，那么在**编译时**就会报错，比如使用nickname作为查询条件：
```
[ERROR] Failed to execute goal org.codehaus.gmavenplus:gmavenplus-plugin:1.2:compile (default) on project gstormtest: Error occurred while calling a method on a Groovy class from classpath. InvocationTargetException: startup failed:
[ERROR] /Users/noahshen/workspace/nagrandtest/src/main/groovy/nagrandtest/services/PersonService.groovy: 12: unknown groovy-style whereable
[ERROR] @ line 12, column 13.
[ERROR] nickname = "Noah"
[ERROR] ^
[ERROR] 
[ERROR] 1 error
[ERROR] -> [Help 1]
[ERROR] 
```

更复杂的查询条件：

```groovy
def person = Person.where {
    (lastName != "Shen" && firstName != "Noah") || (firstName == "Sara" && age > 20)
}
```

Groovy 操作符对应的查询条件及含义:

| 操作符   | where方法 | 描述  |
| --------   | -----   | ----  |
| ==         | eq      | 等于   |
| !=         | nq      | 不等于   |
| >          | gt      | 大于  |
| <          | lt      | 小于 |
| \>=        | ge      | 大于等于 |
| <=         | le      | 小于等于 |
| in         | inList  | 集合中是否包含 |


### 事件

`.beforeInsert` 方法会在entity第一次保存的时候会被调用

```groovy
class Item {
  void beforeInsert() {
    ...
  }
}
```

`.beforeUpdate` 方法会在entity每次被更新的时候被调用

```groovy
class Item {
  void beforeUpdate() {
    ...
  }
}
```

### 乐观锁及版本
Nagrand支持基于version属性的乐观锁，该version属性的值会保存在数据库表中version列

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


### 自动更新时间

如果定义了dateCreated属性，那么当第一次保存该对象的时候，dateCreated会被设置为当前时间，同样的，lastUpdated会在每次被更新的时候，会更新为当前时间

```groovy
class ClassAutoTimestamp {
    Integer id
    String name
    Date dateCreated
    Date lastUpdated
}
```
## 开源许可
Nagrand是基于Apache License 2开源协议