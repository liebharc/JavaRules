# JavaRules

An area to experiment with Rule engines. The main topic is to gain better understanding about things I see in a productive system.

## Discussion

From https://groups.google.com/forum/#!searchin/drools-usage/liebharc%7Csort:date/drools-usage/V8ypP1o5SwM/tEaLk8SlAwAJ :

I would like to summarize our findings for anyone who followed or found this thread. In our case we have three issues:

1. Our application frequently receives new information which must be interpreted and for that it also creates frequently new Drools sessions
2. In the Drools code of the application control facts are used as primary method to avoid infinite loops
3. Most rules used the MVEL dialect for consequences

First benchmarks indicate that if we address those issues Drools performs (in terms of memory footprint and execution time) as well as our hand written Java comparison code in a test scenario which is worst case for Drools (fast LHS and consequences and a small number of rules,
14 rules to be precise). So the overhead Drools adds seems to be small, while you of course get a performance boost as your number of rules increase.

With that in mind these are the actions we are considering to improve the performance in our application:

The first issue has been addressed by the Drools team with the addition of session pools in Drools 7.13. This clearly improves the memory footprint of our application as we could measure with micro-benchmarks and macro-benchmarks. Throughput in the macro-benchmark roughly doubled.

The second issue requires work on our side. Here is a blog article on different ways how to prevent infinite loops in Drools: [1] As of of now the majority of our rules use control facts (our control facts are called tokens). The issue here is that the control facts are inserted into the Drools session which causes a few things which can hit
performance: The control fact needs to be created and garbage collected. In addition to the control fact which is created by the rule, Drools itself will create some internal objects to manage the new fact.

Drools offers alternatives to control facts as the blog article describes as well. So wherever possible we will replace the control fact with one of the following strategies:

lock-on-active: The LHS of a rule is only evaluated once when the agenda group becomes active. So if a rule in the same agenda group inserts or modifies a fact then the LHS of any rule with lock-on-active set will never be reevaluated. In our case I think there are a lot of cases where this alone is enough to prevent infinite loops without having to rely on control facts. The reason for that is that many rules react to an event fact (a custom class) which is never modified or inserted by any rule.

property reactivity: We have property reactivity disabled by now as two of our classes don't follow simple bean convention (there methods are more complicated than simple getters and setters). We will therefore apply "@Modifies" annotations as described in the section "Fine grained property change listeners" of the Drools documentation [2] or rely on traits are also described in the Drools documentation [3] and discussed for maps and map like structures in more details here [4, 5, 6].

In addition almost all of our rules used the MVEL dialect. Changing to the default Java dialect also improved the performance.

Micro-benchmarks indicate another considerable performance improvement if this is done. In our real application the improvement was roughly a factor of 4.

[1]
https://ilesteban.wordpress.com/2012/11/16/about-drools-and-infinite-execution-loops/
[2]
https://docs.jboss.org/drools/release/7.13.0.Final/drools-docs/html_single/#_left_hand_side_when_syntax
[3]
https://docs.jboss.org/drools/release/7.13.0.Final/drools-docs/html_single/#_traits

[4] groups.google.com/forum/#!topic/drools-usage/G7rlz3oI1HQ
[5]
https://github.com/kiegroup/drools/blob/master/drools-compiler/src/test/java/org/drools/compiler/factmodel/traits/TraitMapCoreTest.java
[6]
http://blog.athico.com/2011/12/dynamic-typing-in-rules-traits-part-2.html

## Build

```
mvn clean install
```