Since i am more familiar with Scala and Python than java, done the assignment using scala.
fyi.. I have opened the port 8080 on windows 10 local machine to execute the web url from the browser
Instrunction to execute the scala program
  1. Execute from IntelliJ IDEA
      i. Open Main.scala program under Drtest Package and execute the run command
      
      ii. OPen the Browser

          multiple medallion with cache

          http://localhost:8080/query?usecache=true&medallion=91EDB0A9EEFD7D9169432897F12E4E8C,DF645342E669D093292A1DEF248355A9&pickup_datetime=2013-12-30

          single medallion with cache

          http://localhost:8080/query?usecache=true&medallion=91EDB0A9EEFD7D9169432897F12E4E8C&pickup_datetime=2013-12-30

          single medallion with out cache

          http://localhost:8080/query?usecache=false&medallion=91EDB0A9EEFD7D9169432897F12E4E8C&pickup_datetime=2013-12-30
    
  2. Exeucte from  Scala Shell
      i. Go to the Scala Shell and execute the Main.sacal as  > Scalac Main.scala
      
      ii. OPen the Browser

          multiple medallion with cache

          http://localhost:8080/query?usecache=true&medallion=91EDB0A9EEFD7D9169432897F12E4E8C,DF645342E669D093292A1DEF248355A9&pickup_datetime=2013-12-30

          single medallion with cache

          http://localhost:8080/query?usecache=true&medallion=91EDB0A9EEFD7D9169432897F12E4E8C&pickup_datetime=2013-12-30

          single medallion with out cache

          http://localhost:8080/query?usecache=false&medallion=91EDB0A9EEFD7D9169432897F12E4E8C&pickup_datetime=2013-12-30      

   3. Execute from other Terminal(where java is installed and path variables set) other than IntelliJ and Scala shell
       i. Build the Jar file from IntelliJ
            Execute SBT Clean 
            Execute SBT Package
       ii. Once SBT packaging is done get the Jar file from path C:\Users\Anil\IdeaProjects\Drtest\target\scala-2.11
           Jar file is Drtest_2.11-0.1.jar     

       Note: The package name is Drtest
       
       iii. Go to the terminal and execute the below
            java -cp C:\Users\Anil\IdeaProjects\Drtest\target\scala-2.11\Drtest_2.11-0.1.jar Main
             
         Note: AS mentioned i am not familiar with java, i haven't executed this program from the java terminal and i am giving the steps to execute the scala jar fiel from j               java terminal with my limited knowledge on java
         
Instrunctions to execute the Python job
1. Go to the python terminal and execute the below as 
         Single medallion
     i.  python nyccabdata.py --cab_id "91EDB0A9EEFD7D9169432897F12E4E8C,DF645342E669D093292A1DEF248355A9" --pickup_date "2013-12-30"
         More than onehow medallion
     ii. python nyccabdata.py --cab_id "91EDB0A9EEFD7D9169432897F12E4E8C" --pickup_date "2013-12-30"
