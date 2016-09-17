# Versions
* [0.0.1](https://github.com/teivah/TIBreview/releases/tag/0.0.1): Initial version

# TIBreview: Quality code review for TIBCO BusinessWorks 6

##What is TIBReview?

TIBReview is an automated tool for analyzing the quality of a TIBCO BusinessWorks 6 project. It provides:
* An extensible and configurable engine for implementing automated rules
* A output mechanism for generating a quality report either in a CSV or a PMD format (directly integrable in Jenkins or Sonar).
* A mechanism for configuring easily the rules processing (for example: *“I want to change this parameter on this environment”* or *“On this environment, I want to disable this very rule”*)

##What is TIBReview able to analyze?

So far the process and resource assets.

In terms of process quality check:
* Check forbidden activities: Checkpoint, Copy File etc…
* Global conventions: process description missing, default namespace used, process size, number of activities per process, missing Catch All activity etc…
* Transitions: missing label on a conditioned transition, missing Empty activity after a transition parallelization, conditioned transitions without a no matching one
* Critical sections: make sure a blocking activity is not called within a critical section scope
* Process calls infinite cycles: for example Process A --> Process B --> Process A

In terms of resource quality check, TIBReview is able to detect hardcoded values (HTTP Client maxTotalConnections hardcoded, FTP Connection username hardcoded, JDBC Connection dbURL hardcoded etc…).

In total, TIBReview provides about 50 automated checks for the first version (and we keep working on it to improve the quality coverage).

##How to run TIBReview?

The JAR syntax is the following:

*$java -Dlog4j.configuration=file:"<Log4jFile>" -jar tibreview-0.0.1.jar -r <TIBRuleFile> -c <ConfigFile> -i {project|process|resource} -s <InputPath> -o {csv|pmd} -t <TargetPath>*

* -r: tibrules.xml file
* -c: config.properties file
* -i: input type, use *project* to analyze a whole project, *process* to analyze a single process, *resource* to analyze a single resource
* -s: input path, if –i was set to project you must reference a BW6 project
* -o: output type (CSV or PMD file)
* -t: target path

For example:

*$java -Dlog4j.configuration=file:"log4.properties" -jar tibreview-0.0.1.jar -r tibrules.xml -c config.properties -i project -s C:/tibco/workspace/myProject -o pmd -t C:/output*

##How can I implement my own rules?

For a process, you can implement two types of rules: XPath or Java.

An XPath rule can be pretty straightforward to implement and as you can see most of TIBReview rules are based on this mechanism.
At runtime, each XPath request will replace each %property% (referenced in the config.properties file) and each * xpathFunction * by the XPath functions defined. An * xpathFunction * is a simple way to keep readable your tibrules file without having to duplicate an XPath statement.

Yet, it is possible for complex rules implementation (for example cycle check) to do it directly in Java.
You must create a new class *in com.tibco.exchange.tibreview.processor.processrule.java* extending the *PRJava* abstract class. For a concrete example, take a look at *com.tibco.exchange.tibreview.processor.processrule.java.PRCycleCheck*.

It is also possible to add complex conditions management. For example: *"If this is a top process, I want to check this XPath rule but if this is a sub process, I want to check that Java rule"*.

For resources, you simply need to add a new resource type or new property to check whether it has been managed using a property.

##How to configure TIBReview?

You must use the config.properties file.

* rules.disable: a list of disabled rule name separated by a *;* character
* property.< myProperty >: allows to set a value for the <myProperty> property. This property will be used in the tibrules.xml file if an implementation uses %myProperty%. For a concrete example, you should take a look at the *Process size: width* rule.

##How to build TIBReview?

After having installed Maven, you just need to run a *$mvn package*. A fat JAR will be packaged in the /target folder.

##Can I participate to the project?

It would be with pleasure :) Please feel free to contact me if you would like to work on the project or for any questions / remarks. 
