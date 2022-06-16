gradle clean
gradle botJar
#java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar build/lib/reactive-flashcards-1.0.0.jar
java -jar build/lib/reactive-flashcards-1.0.0.jar