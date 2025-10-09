package fun.gusmurphy.library.springboothex;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    private static final String DOMAIN_PACKAGE = "fun.gusmurphy.library.springboothex.domain";
    private static final String JAVA_PACKAGE_MATCHER = "..java..";

    @ArchTest
    public static final ArchRule domainClassesCantDependOnBasicallyAnything = noClasses()
            .that().resideInAPackage(DOMAIN_PACKAGE)
            .should().dependOnClassesThat().resideOutsideOfPackages(DOMAIN_PACKAGE, JAVA_PACKAGE_MATCHER);

}
