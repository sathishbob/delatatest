call mvn clean install package
call mvn exec:java@DriverScript
call mvn exec:java@TestReport
