# scrum

## Run Application
```
./gradlew bootRun
```

That starts the application in the port `8080`.

This configuration can be changed in the [application configuration file](src/main/resources/application.yaml)

## Tests
```
./gradlew cleanTest test
```

## Considerations

### Technical
I've chosen to follow the functional programming approach, meaning that the data is immutable
    and copies of modified elements should be returned.

For that purpose I've used a third party library [vavr](https://www.vavr.io/) that implements Java collections in the
    functional way, also enables you to do pattern matching and other functional programming techniques.

The problem with this library is that, given the verbose nature of Java, sometimes can be a bit difficult to read when 
    you are not used to it.

In the class [SprintService](src/main/java/com/pinguin/scrum/issue/service/SprintService.java) there is a function that 
    uses pattern matching over a list of unassigned stories to do the story assignation for a given `week-developer`.
    
The following is a simplified version much less verbose than the actual implementation:
```scala
def assignRecursive(unassignedStories: List[Story], weeklyPlaning: WeeklyPlaning): (List[Story], WeeklyPlaning) = 
    unassignedStories match {
      // If the first element of the list fits in the planning is added to it and the rest of the list is processed
      case head::tail if (head.estimation < weeklyPlaning.remainingCapacity) => 
        weeklyPlaning.add(head)  
        return assignRecursive(tail, weeklyPlaning)

      // If the first element does not fit the rest is processed and its returned with the remaining
      case head::tail  =>
        (remaining, planning) = assignRecursive(tail, weeklyPlaning)
        return (head :: remaining, planning)
                           
      // If all the stories are processed the planning is returned
      case EmptyList =>
        return (EmptyList, weeklyPlaning)
    }
```

### Bussines Logic
The story assignation algorithm assumes that no story could be estimated greater than the total story points that a developer can complete in a week,
meaning that a story can not be planned for longer than a week.

I've chosen not to implement the UPDATE operation for the entities since it is not relevant.

I've also decided to have separate tables for the entities `Story` and `Bug` even if both are subclasses of `Issue` because
I don't like SQL table polymorphism and I've opted to abstract common operations in the `IssueService`.