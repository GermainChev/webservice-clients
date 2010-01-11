Java Clients
============

A set of sample EBI Web Services clients developed in Java.

See http://www.ebi.ac.uk/Tools/webservices/

Building Clients
----------------

An Apache ant (http://ant.apache.org/) build file (build.xml) is used to 
perform the build, with Apache Ivy (http://ant.apache.org/ivy/) being used to 
manage library dependencies (see ivy.xml).

1. Generate the stubs from the WSDLs.

Generate stubs from service description documents (WSDL) for all the SOAP 
toolkits:

  ant stubs

Generate stubs for a specific SOAP library, e.g. Apache Axis 1.x:

  ant axis1-stubs

Generate stubs for a specific service for a specific library, e.g. WSWUBlast:

  ant axis1-stubs-wublast

Note: since ant does not support remote dependency checking, and many of the 
WSDLs are dynamic documents, the stubs are re-generated each time these 
targets are called.

2. Compile

  ant

or

  ant compile

To compile just the code for one of the SOAP libraries, e.g. Axis 1.x:

  ant axis1-compile

3. Compile and package into jars:

  ant jar

To package just the code for one of the SOAP libraries, e.g. Axis 1.x:

  ant axis1-jar

4. Package the dependencies downloaded by Ivy:

  ant package-dependencies

Running Clients
---------------

To run a client the required jars (lib/) need to be added to the classpath. A
simple way to do this is to set the java.ext.dirs property to include the
directory containing the jars. For example:

  java -Djava.ext.dirs=lib/ -jar bin/WSDbfetch.jar

For JAX-WS under Java 5, the JAX-WS libraries also need to be included in the
path list specified for java.ext.dirs, since these are not provided as part 
of the Java installation. The JAX-WS libraries can be obtained from 
https://jax-ws.dev.java.net/.

Support
-------

If you have problems with the clients or any suggestions for our Web Services
then please contact us via the Support form http://www.ebi.ac.uk/support/
