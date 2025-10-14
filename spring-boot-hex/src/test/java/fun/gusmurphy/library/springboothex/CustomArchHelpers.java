package fun.gusmurphy.library.springboothex;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomArchHelpers {

    public static final ArchCondition<JavaClass> notDependOnAdaptersOutsideItsOwnPackage =
            new ArchCondition<>("not depend on adapters outside its own package") {
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

    public static final ArchCondition<JavaClass> notDependOnDrivenPortsUnlessImplementingThem = conditionThatClassDoesNotDependOnPackageUnlessImplementing(
            "driven",
            "not depend on driven ports unless implementing them",
            "Class %s depends on driven port %s that it is not implementing"
    );

    public static final ArchCondition<JavaClass> notDependOnDrivingPortsUnlessImplementingThem = conditionThatClassDoesNotDependOnPackageUnlessImplementing(
            "driving",
            "not depend on driving ports unless implementing them",
            "Class %s depends on driving port %s that it is not implementing"
    );

    public static final ArchCondition<JavaClass> notImplementDrivenPorts =
            new ArchCondition<>("not implement driven ports") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    var drivenPortDependencies = getDependenciesFromPackage(item, "driven");

                    for (var dependency : drivenPortDependencies) {
                        var isAnImplementedClass = classImplementsInterface(item, dependency.getTargetClass());

                        if (isAnImplementedClass) {
                            String message = String.format(
                                    "Class %s implements driven port %s", item.getName(), dependency.getTargetClass().getName());
                            events.add(SimpleConditionEvent.violated(dependency, message));
                        }
                    }
                }
            };

    private static ArchCondition<JavaClass> conditionThatClassDoesNotDependOnPackageUnlessImplementing(
            String targetPackageName,
            String description,
            String violationMessageFormat
    ) {
        return new ArchCondition<>(description) {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                var targetPackageDependencies = getDependenciesFromPackage(item, targetPackageName);

                for (var dependency : targetPackageDependencies) {
                    var isAnImplementedClass = classImplementsInterface(item, dependency.getTargetClass());

                    if (!isAnImplementedClass) {
                        String message = String.format(
                                violationMessageFormat, item.getName(), dependency.getTargetClass().getName());
                        events.add(SimpleConditionEvent.violated(dependency, message));
                    }
                }
            }
        };
    }

    private static List<Dependency> getDependenciesFromPackage(JavaClass javaClass, String packageName) {
        return javaClass.getDirectDependenciesFromSelf().stream()
                .filter(d -> d.getTargetClass().getPackage().getName().contains(packageName))
                .toList();
    }

    private static boolean classImplementsInterface(JavaClass javaClass, JavaClass javaInterface) {
        return javaClass.getInterfaces().stream()
                .anyMatch(c -> c.equals(javaInterface));
    }
}
