package fun.gusmurphy.library.springboothex;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static fun.gusmurphy.library.springboothex.CustomArchHelpers.notDependOnAdaptersOutsideItsOwnPackage;

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

    @ArchTest
    public static final ArchRule adaptersCannotDependOnOtherAdapters = classes()
            .that().resideInAPackage(ADAPTER_PACKAGE)
            .should(notDependOnAdaptersOutsideItsOwnPackage);

    @ArchTest
    public static final ArchRule adaptersCannotDependOnApplicationImplementations = noClasses()
            .that().resideInAPackage(ADAPTER_PACKAGE)
            .should().dependOnClassesThat().resideInAPackage(APPLICATION_PACKAGE);
}
