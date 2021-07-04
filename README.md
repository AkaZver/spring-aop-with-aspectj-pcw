# Description
This project is the PoC of using Spring AOP and AspectJ PCW (post-compile weaving) at the same time

`ControllerAspect` works with `MyController` and creates Spring Proxy via CGLIB

`ServiceAspect` works with `MyService` and it modifies class-file at post-compile stage 
by adding necessary logic w/o proxies

`AnnotationAspect` exists to present how to test code with annotations controlled by aspects

All situations are covered by tests

# Tips & Tricks
**1.** First part of magic is done with `aop.xml`:
```xml
<aspectj>
    <aspects>
        <aspect name="com.github.akazver.poc.aop.ServiceAspect"/>
        <aspect name="com.github.akazver.poc.aop.AnnotationAspect"/>
    </aspects>
</aspectj>
```
Second part is in the `build.gradle`:
```groovy
[compileJava, compileTestJava].each {
    it.ajc.options.compilerArgs += ['-xmlConfigured', "${projectDir}/src/main/resources/aop.xml"]
}
```
The `xmlConfigured` AJC key is undocumented, but you can find all information you need on StackOverflow 
and other similar resources. This file tells AJC which aspect should be processed by it, other aspects 
will be on the Spring AOP side

**2.** If you have classes in unit-tests that need to be processed by aspects, then you have to 
explicitly point AJC to apply them in unit-test phase by adding this in `build.gradle`:
```groovy
compileTestJava.ajc.options.aspectpath.from = sourceSets.main.output
```
This is also valid for integration-tests created with `unbroken-dome/gradle-testsets-plugin`, 
just change `compileTestJava` to `compileIntegrationTestJava`

**3.** You can't use required-args-constructor in non-Spring aspects to inject beans because they 
are singletons created by AspectJ with no-args-constructor and accessed via static methods.
Inject dependencies with `@Autowired` and use Spring factory-methods to create beans like this:
```java
@Log4j2
@Configuration
public class ApplicationConfig {

    @Bean
    public ServiceAspect serviceAspect() {
        return Aspects.aspectOf(ServiceAspect.class);
    }

}
```
Another way is to use `@Configurable` on aspect class + `@EnableSpringConfigured` on configuration to tell 
Spring that it needs to inject beans into unmanaged objects, but I couldn't make it work

**4.** There are plenty of other useful AJC options in the 
[official documentation](https://www.eclipse.org/aspectj/doc/next/devguide/ajc-ref.html)