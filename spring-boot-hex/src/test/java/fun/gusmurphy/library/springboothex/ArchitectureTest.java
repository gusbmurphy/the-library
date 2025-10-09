package fun.gusmurphy.library.springboothex;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    private static final String BASE_PACKAGE_NAME = "fun.gusmurphy.library.springboothex.";
    private static final String DOMAIN_PACKAGE = BASE_PACKAGE_NAME + "domain..";
    private static final String APPLICATION_PACKAGE = BASE_PACKAGE_NAME + "application..";
    private static final String ADAPTER_PACKAGE = BASE_PACKAGE_NAME + "adapter..";
    private static final String JAVA_PACKAGE_MATCHER = "..java..";

    @ArchTest
    public static final ArchRule domainClassesCantDependOnBasicallyAnything = noClasses()
            .that().resideInAPackage(DOMAIN_PACKAGE)
            .should().dependOnClassesThat().resideOutsideOfPackages(DOMAIN_PACKAGE, JAVA_PACKAGE_MATCHER);

    @ArchTest
    public static final ArchRule applicationClassesCannotDependOnAdapters = noClasses()
            .that().resideInAPackage(APPLICATION_PACKAGE)
            .should().dependOnClassesThat().resideInAPackage(ADAPTER_PACKAGE);

    private static final ArchCondition<JavaClass> notDependOnAdaptersOutsideItsOwnPackage =
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

    @ArchTest
    public static final ArchRule adaptersCannotDependOnOtherAdapters = classes()
            .that().resideInAPackage(ADAPTER_PACKAGE)
            .should(notDependOnAdaptersOutsideItsOwnPackage);
}
