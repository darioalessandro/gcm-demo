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
class SessionSpec extends org.specs2.mutable.Specification {

  "POST /session/:id" should {
    "create a new session" in new test.App  {
      val sessionId = "foo"
      val header = FakeRequest(POST,s"/session/$sessionId")
      val body = Json.obj(
        "gcm_registration_id" -> "some gcm id",
        "os_version" -> "an os version",
        "app_version" -> "an app version"
      )
      val result = route(header,body).get
      status(result) must equalTo(201)
    }

  }
}
