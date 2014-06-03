package controllers

import play.api.test._
import play.api.libs.json._
import play.api.test.Helpers._

/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-02
 * Time: 9:19 AM
 * To change this template use File | Settings | File Templates.
 */
class SessionFunctionalSpec extends org.specs2.mutable.Specification with org.specs2.mock.Mockito{

  "POST /sessions/:id" should {
    "create a new session" in new test.App  {
      val sessionId = "foo"
      val header = FakeRequest(POST,s"/sessions/$sessionId")
      val body = Json.obj(
        "gcm_id" -> "some gcm id",
        "os_version" -> "an os version",
        "app_version" -> "an app version"
      )
      val result = route(header,body).get
      status(result) must equalTo(CREATED)
    }
    "return bad request if body is incorrect" in new test.App {
      val sessionId = "foo"
      val header = FakeRequest(POST,s"/sessions/$sessionId")
      val body = Json.obj(
        "bad_param" -> "some gcm id",
        "os_version" -> "an os version",
        "app_version" -> "an app version"
      )
      val result = route(header,body).get
      status(result) must equalTo(BAD_REQUEST)
    }


  }
}
