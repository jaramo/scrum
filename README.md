# scrum

## Run Application
```
./gradlew bootRun
```
This command starts the application in the port `8080`.

This configuration can be changed in the [application configuration file](src/main/resources/application.yaml).

## Tests
```
./gradlew cleanTest test
```

## Considerations

### Technical
I've chosen to follow the functional programming approach, meaning that the data is immutable
    and copies of modified elements should be returned.

For that purpose, I've used a third party library, [vavr](https://www.vavr.io/), that implements Java collections in a
    functional way. It also enables you to do pattern matching and other functional programming operations like `flatMap`, `reduce` or `fold`.

Given the verbose nature of Java, functional implementation of [vavr](https://www.vavr.io/), can be a bit difficult to read.
    Nevertheless, it allows a more concise implementation, thanks to the use of lambdas and tuples instead of java collectors,
    once you get used to it.

For example, In the class [SprintService](src/main/java/com/pinguin/scrum/issue/service/SprintService.java), 
    pattern matching is used over a list of unassigned stories to do the story assignment for a given `week-developer`.
    
Where, in a functional programming language, you can find something like:
```scala
unassignedStories match {
  case head::tail if (head.estimation < weeklyPlaning.remainingCapacity) =>  ...
  case head::tail  => ...
  case nil => ...
```

the java implementation is like this:
```java
Match(unassignedStories).of(
    Case($Cons($(story -> story.getEstimation() <= planing.capacity), $()), (head, tail) -> ...),
    Case($Cons($(), $()), (head, tail) -> ...),
    Case($Nil(), () -> ...)
)
```

Even with the overhead of the java verbosity, the use **pattern matching** with **guards**, makes the implementation much more
    strait forward and easy to follow than the typical `if (cond) { ... } else { ... }`.

### Business Logic
The story assignation algorithm assumes that no story could be estimated to be greater than the total story points 
    that a developer can complete in a week, meaning that a story can not be planned for longer than a week.

I've chosen not to implement the UPDATE operation for the entities since it is not relevant for the purpose of the test.

I've also decided to have separate tables for the entities `Story` and `Bug` even if both are subclasses of `Issue` because
    I don't like SQL table polymorphism.
    
In my opinion, having mixed data in the same table doesn't bring any noticeable benefit,
    in fact, it makes queries more complex and, it also adds polymorphism problems like casting to the proper subclass when fetching the data.

Instead, I prefer to abstract common operations in code, like in `IssueService` class. Having injected both repositories make queries easier
    and, it is always possible to abstract operations on the parent class with proper `Typing` and  use of `lambdas`.