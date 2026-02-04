package fun.gusmurphy.library.springboothex;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static fun.gusmurphy.library.springboothex.CustomArchHelpers.*;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    private static final String BASE_PACKAGE_NAME = "fun.gusmurphy.library.springboothex.";
    private static final String APP_PACKAGE = BASE_PACKAGE_NAME + "application..";
    private static final String PORT_PACKAGE = BASE_PACKAGE_NAME + "port..";
    private static final String ADAPTER_PACKAGE = BASE_PACKAGE_NAME + "adapter..";
    private static final String JAVA_PACKAGES = "..java..";

    @ArchTest
    public static final ArchRule applicationClassesCantDependOnBasicallyAnything =
            noClasses()
                    .that()
                    .resideInAPackage(APP_PACKAGE)
                    .should()
                    .dependOnClassesThat()
                    .resideOutsideOfPackages(APP_PACKAGE, JAVA_PACKAGES, PORT_PACKAGE);

    @ArchTest
    public static final ArchRule applicationClassesCantDependOnPrimaryPortsUnlessImplementingThem =
            classes()
                    .that()
                    .resideInAPackage(APP_PACKAGE)
                    .should(notDependOnPrimaryPortsUnlessImplementingThem);

    @ArchTest
    public static final ArchRule applicationClassesCantImplementSecondaryPorts =
            classes().that().resideInAPackage(APP_PACKAGE).should(notImplementSecondaryPorts);

    @ArchTest
    public static final ArchRule adaptersCannotDependOnOtherAdapters =
            classes()
                    .that()
                    .resideInAPackage(ADAPTER_PACKAGE)
                    .should(notDependOnAdaptersOutsideItsOwnPackage);

    @ArchTest
    public static final ArchRule adaptersCannotDependOnPrimaryPortImplementations =
            classes()
                    .that()
                    .resideInAPackage(ADAPTER_PACKAGE)
                    .should(notDependOnImplementationsOfPrimaryPorts);

    @ArchTest
    public static final ArchRule adaptersCannotDependOnSecondaryPortsUnlessImplementingThem =
            classes()
                    .that()
                    .resideInAPackage(ADAPTER_PACKAGE)
                    .should(notDependOnSecondaryPortsUnlessImplementingThem);
}
