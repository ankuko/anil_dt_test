import java.sql.DriverManager
import java.sql.Connection
import spark.Spark._
import spark.{Request, Response, Route}
import scala.collection.mutable._


object main {

  val cacheResult = Map[String, String]()


  def querySQL(
                strSQL:String
              ): String ={

    var result:String  =  null
	val url = "jdbc:redshift://data-demo-test-cluster.c6dkqedsa123.ap-southeast-2.redshift.amazonaws.com:1111/dbname"
    val driver = "com.amazon.redshift.jdbc42.Driver"
    val username = "root"
    val password = "root"
    var connection:Connection = _
    try {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
        val statement = connection.createStatement
        val rs = statement.executeQuery(strSQL)
        while (rs.next) {
            val medallion = rs.getString("medallion")
            val cnt = rs.getString("cnt")
            result = result "\n" + medallion +"," + cnt
        }
    } catch {
        case e: Exception => e.printStackTrace
    }
    connection.close

    result
  }


  def queryResult(
                   useCache:String,
                   medallion:String,
                   pickup_datetime:String
                 ): String ={

    var result:String = null

    val strSQL =
      """
        |SELECT medallion,
        |count(medallion) AS cnt
        |FROM
        | public.Transaction
        |WHERE
        |medallion in (medallion)
        |AND date_part(pickup_datetime) = pickup_datetime
		|group by medallion
        |""".stripMargin
        .replace("medallion", medallion)
        .replace("pickup_datetime", pickup_datetime)


    val cacheKey = "medallion=" + medallion + ",pickup_datetime=" + pickup_datetime
    var cacheExists = false

    if(useCache.trim.toLowerCase == "true"){
      if(cacheResult.exists{entry => entry._1 == cacheKey}){
        cacheExists = true
      }

      if(cacheExists == true){
        result = "medallion:" + medallion + "<br/>pickup_datetime:" + pickup_datetime + "<br/>useCache:" + useCache + "<br/><br/>Cache result is found!" + "<br/><br/>Cache result:" + cacheResult(cacheKey)
      }
      else {
        val querySQLResult = querySQL(strSQL)
        result = "medallion:" + medallion + "<br/>pickup_datetime:" + pickup_datetime + "<br/>useCache:" + useCache + "<br/><br/>Cache result is not found!" + "<br/><br/>SQL:<br/>" + strSQL + "<br/><br/>DB result:" + querySQLResult
        cacheResult(cacheKey) = querySQLResult
      }
    }
    else {
      val querySQLResult = querySQL(strSQL)
      result = "medallion:" + medallion + "<br/>pickup_datetime:" + pickup_datetime + "<br/>useCache:" + useCache + "<br/><br/>SQL:<br/>" + strSQL + "<br/><br/>DB result:" + querySQLResult
      cacheResult(cacheKey) = querySQLResult
    }

    result
  }


  def main(args : Array[String]) {

    val route = (req: Request, resp: Response) => {
      val useCache = req.queryMap().get("useCache").value()
      val medallion = req.queryMap().get("medallion").value()
      val pickup_datetime = req.queryMap().get("pickup_datetime").value()

      queryResult(
        useCache,
        medallion,
        pickup_datetime
      )
    }

    port(8080)
    get("/query", new Route() {
      override def handle(request: Request, response: Response): AnyRef = {
        route(request, response)
      }
    })

    println("Server started!")
  }
}

multiple medallion with cache
http://localhost:8080/query?usecache=true&medallion=D7D598CD99978BD012A87A76A7C891B7,67C2B72EB78E36BF7A24B7D432686767&pickup_datetime=2020-11-24
single medallion with cache
http://localhost:8080/query?usecache=true&medallion=D7D598CD99978BD012A87A76A7C891B7&pickup_datetime=2020-11-24
single medallion with out cache
http://localhost:8080/query?usecache=false&medallion=D7D598CD99978BD012A87A76A7C891B7&pickup_datetime=2020-11-24