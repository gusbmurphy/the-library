package fun.gusmurphy.library.springboothex;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomArchHelpers {

    public static final ArchCondition<JavaClass> notDependOnAdaptersOutsideItsOwnPackage =
            new ArchCondition<>("depend on adapters outside its own package") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    var ourSubPackageName = Arrays.stream(item.getPackageName().split("\\.")).toList().getLast();
                    var dependencies = item.getDirectDependenciesFromSelf();
                    var adapterDependencies = dependencies.stream()
                            .filter(d -> d.getTargetClass().getName().contains("adapter"))
                            .collect(Collectors.toSet());

                    for (var dependency : adapterDependencies) {
                        if (!dependency.getTargetClass().getPackageName().contains(ourSubPackageName)) {
                            String message = String.format(
                                    "Class %s depends on other adapter class %s not within its own subpackage", item.getName(), dependency.getTargetClass().getName());
                            events.add(SimpleConditionEvent.violated(dependency, message));
                        }
                    }
                }
            };
}
