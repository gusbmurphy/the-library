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
                    var ourSubPackageName =
                            Arrays.stream(item.getPackageName().split("\\.")).toList().getLast();
                    var dependencies = item.getDirectDependenciesFromSelf();
                    var adapterDependencies =
                            dependencies.stream()
                                    .filter(d -> d.getTargetClass().getName().contains("adapter"))
                                    .collect(Collectors.toSet());

                    for (var dependency : adapterDependencies) {
                        if (!dependency
                                .getTargetClass()
                                .getPackageName()
                                .contains(ourSubPackageName)) {
                            String message =
                                    String.format(
                                            "Class %s depends on other adapter class %s not within its own subpackage",
                                            item.getName(), dependency.getTargetClass().getName());
                            events.add(SimpleConditionEvent.violated(dependency, message));
                        }
                    }
                }
            };

    public static final ArchCondition<JavaClass> notDependOnSecondaryPortsUnlessImplementingThem =
            conditionThatClassDoesNotDependOnPackageUnlessImplementing(
                    "secondary",
                    "not depend on secondary ports unless implementing them",
                    "Class %s depends on secondary port %s that it is not implementing");

    public static final ArchCondition<JavaClass> notDependOnPrimaryPortsUnlessImplementingThem =
            conditionThatClassDoesNotDependOnPackageUnlessImplementing(
                    "primary",
                    "not depend on primary ports unless implementing them",
                    "Class %s depends on primary port %s that it is not implementing");

    public static final ArchCondition<JavaClass> notDependOnImplementationsOfPrimaryPorts =
            new ArchCondition<>("not depend on implementations of primary ports") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    for (var dependency : item.getDirectDependenciesFromSelf()) {
                        if (implementsPrimaryPort(dependency)) {
                            String message =
                                    String.format(
                                            "Class %s depends on implementation of primary port %s",
                                            item.getName(), dependency.getTargetClass().getName());
                            events.add(SimpleConditionEvent.violated(dependency, message));
                        }
                    }
                }
            };

    public static final ArchCondition<JavaClass> notImplementSecondaryPorts =
            new ArchCondition<>("not implement secondary ports") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    var secondaryPortDependencies = getDependenciesFromPackage(item, "secondary");

                    for (var dependency : secondaryPortDependencies) {
                        var isAnImplementedClass =
                                classImplementsInterface(item, dependency.getTargetClass());

                        if (isAnImplementedClass) {
                            String message =
                                    String.format(
                                            "Class %s implements secondary port %s",
                                            item.getName(), dependency.getTargetClass().getName());
                            events.add(SimpleConditionEvent.violated(dependency, message));
                        }
                    }
                }
            };

    private static ArchCondition<JavaClass>
            conditionThatClassDoesNotDependOnPackageUnlessImplementing(
                    String targetPackageName, String description, String violationMessageFormat) {
        return new ArchCondition<>(description) {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                var targetPackageDependencies = getDependenciesFromPackage(item, targetPackageName);

                for (var dependency : targetPackageDependencies) {
                    var isAnImplementedClass =
                            classImplementsInterface(item, dependency.getTargetClass());

                    if (!isAnImplementedClass) {
                        String message =
                                String.format(
                                        violationMessageFormat,
                                        item.getName(),
                                        dependency.getTargetClass().getName());
                        events.add(SimpleConditionEvent.violated(dependency, message));
                    }
                }
            }
        };
    }

    private static List<Dependency> getDependenciesFromPackage(
            JavaClass javaClass, String packageName) {
        return javaClass.getDirectDependenciesFromSelf().stream()
                .filter(d -> d.getTargetClass().getPackage().getName().contains(packageName))
                .toList();
    }

    private static boolean classImplementsInterface(JavaClass javaClass, JavaClass javaInterface) {
        return javaClass.getInterfaces().stream().anyMatch(c -> c.equals(javaInterface));
    }

    private static boolean implementsPrimaryPort(Dependency dependency) {
        return dependency.getTargetClass().getInterfaces().stream()
                .anyMatch(c -> c.toErasure().getPackageName().contains("primary"));
    }
}
