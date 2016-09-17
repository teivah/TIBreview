# Versions
* [0.0.1](https://github.com/teivah/TIBreview/releases/tag/0.0.1): Initial version

# TIBreview: Quality code review for TIBCO BusinessWorks 6

##What is TIBReview?

TIBReview is an **automated tool** for analyzing the **quality** of a **TIBCO BusinessWorks 6 project**. It provides:
* An extensible and configurable engine for implementing **automated rules in XPath or Java**.
* A output mechanism for generating a **quality report** either in a **CSV** or a **PMD** format (directly then integrable in Jenkins or Sonar).
* A mechanism for managing the rules processing based on a **configuration file** (this way on an environment you can choose to modify property values or to disable only certain rules).

##What is TIBReview able to analyze?

So far the **process** and **resource** assets.

In terms of process quality check:
* **Check forbidden activities**: Checkpoint, Copy File etc…
* **Global conventions**: process description missing, default namespace used, process size, number of activities per process, missing Catch All activity etc…
* **Transitions**: missing label on a conditioned transition, missing Empty activity after a transition parallelization, conditioned transitions without a no matching one
* **Critical sections**: make sure a blocking activity is not called within a critical section scope
* **Process calls infinite cycles**: for example Process A --> Process B --> Process A
* Etc...

In terms of resource quality check, TIBReview is able to detect **hardcoded** values (HTTP Client maxTotalConnections, FTP Connection username, JDBC Connection dbURL hardcoded etc…).

In total, TIBReview provides about **50 automated checks** for the first version (and we keep working on it to improve the quality coverage).

##How can I implement my own rules?

Yes you can and you should ;) Because each context is **unique**, it was important to have an **extensible engine** to be able to easily and quickly implement new quality rules. The rules logic is defined in the **tibrules.xml** file.

For a process, you can implement two types of rules: **XPath** or **Java**.

An XPath rule can be pretty straightforward to implement and most of TIBReview rules are based on this mechanism.
At runtime, the XPath request will be analyzed to replace each %property% by the value defined in the configuration file (see hereunder for more details) and each * xpathFunction * by the value defined in the tibrules.xml. An * xpathFunction * is a simple way to keep readable your tibrules file without having to duplicate an XPath statement.

Yet, it is also possible for **complex rules implementation** to do it directly in Java (for example cycle analysis).
You must create a new class *in com.tibco.exchange.tibreview.processor.processrule.java* extending the *PRJava* abstract class. For a concrete example, take a look at *com.tibco.exchange.tibreview.processor.processrule.java.PRCycleCheck*.

It is also possible to add complex conditions management and to use **if / elseif / else statements**. For example: *"If this is a top process, I want to check this XPath rule but if this is a sub process, I want to check that Java rule"*.

For resources, you can easily extend the tibrules.xml by adding new resource type or new property. Each property will be then checked to make sure it has been managed using a property.

##How to configure TIBReview?

You can use the config.properties file:

* rules.disable: a list of disabled rule name separated by a *;* character.
* property.< myProperty >: allows to set a value for the <myProperty> property. This property will be used in the tibrules.xml file if an implementation uses %myProperty%. For a concrete example, you should take a look at the *Process size: width* rule.

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

##How to build TIBReview?

Using Maven, you just have to run a *$mvn package*. A fat JAR will be packaged in the /target folder.

##Can I participate to the project?

It would be with **pleasure** :) Please feel free to contact me if you would like to work on the project or for any questions / remarks. 
