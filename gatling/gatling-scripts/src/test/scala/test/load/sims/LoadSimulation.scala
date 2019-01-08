package test.load.sims

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import java.util.Random
import scala.concurrent.duration._

class LoadSimulation extends Simulation {

  // 从系统变量读取 baseUrl、path和模拟的用户数
  val baseUrl = System.getProperty("base.url")
  val testPath = System.getProperty("test.path")
   val testData = System.getProperty("test.data")
  val sim_users = System.getProperty("sim.users").toInt

  val httpConf = http.baseURL(baseUrl)
 //注意这里,设置提交内容type
  val headers_json = Map("Content-Type" -> "application/json")
  val contentType = Map("Content-Type" -> "application/x-www-form-urlencoded")
  val contentTypeStream = Map("Content-Type" -> "text/event-stream")
  // 定义模拟的请求，重复30次
  val helloRequest = repeat(30) {
    // 自定义测试名称
    exec(http("hello-with-latency")
      // 执行get请求
    //  .get(testPath+"?q=java"))
  //  .get(testPath+"?value=秦时明月"))
    .post(testPath)
      .headers(headers_json)  //设置body数据格式
      //将json参数用StringBody包起,并作为参数传递给function body()
      .body(StringBody("{\"commonInfo\":{\"version\":\"csjy_1.0.4\",\"mac\":\"38A28CEBD3E7\",\"typeCode\":\"004\",\"sp\":4,\"region\":-1,\"area\":-1},\"playInfo\":{\"bufferInfos\":[],\"vodInfo\":{\"mediaName\":\"明月几时\",\"columnName\":\"电影\",\"contentName\":\"第1集\",\"contentId\":137569,\"definition\":1,\"mediaId\":10096,\"columnId\":3,\"showTimeLength\":7829},\"liveInfo\":null,\"playType\":2,\"playTimeLength\":5,\"chargeType\":2}}")).asJSON)
  //	 .body(StringBody("{\"hashTag\":\"java\",\"queue\":\"twitter-track-hashtag\"}")).asJSON)
  //  .body(StringBody("{\"value\":\"秦时明月\"}")).asJSON)
	//	 .formParam("value", "秦时明月"))
      // 模拟用户思考时间，随机1~2秒钟
     .pause(1 second, 2 seconds)
    //10秒钟约一次请求
    // .pause(4 second, 6 seconds)
  }

  // 定义模拟的场景
  val scn = scenario("hello")
    // 该场景执行上边定义的请求
    .exec(helloRequest)

  // 配置并发用户的数量在30秒内均匀提高至sim_users指定的数量
  setUp(scn.inject(rampUsers(sim_users).over(30 seconds)).protocols(httpConf))
}
