package controllers

import play.api.test.{FakeHeaders, FakeRequest, PlaySpecification}
import play.api.mvc.{Controller, Results}
import play.api.test.Helpers._
import play.api.libs.json.Json
import entities.SessionInfo
import services.DuplicateSession
import scala.util.{Success, Failure}
import scala.concurrent.Future


/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-03
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
class SessionSpec extends org.specs2.mutable.Specification with org.specs2.mock.Mockito with Results{

  val controller = new Controller with Session with services.SessionSvc{
    override val sessionService = mock[SessionSvcContract]
  }
  val mockService = controller.sessionService
  "POST /sessions/:id" should {
    "return 201" in {
      mockService.createSession(any[SessionInfo]).returns(Future{Success(SessionInfo("id","gcm_id","os","app"))})
      val body = Json.obj(
        "gcm_id" -> "some gcm id",
        "os_version" -> "an os version",
        "app_version" -> "an app version"
      )
      val request = FakeRequest(POST,"/sessions/id")
        .withJsonBody(body)

      val result = controller.post("id")(request)
      status(result) must equalTo(CREATED)
    }
    "return conflict if duplicate" in  {
      mockService.createSession(any[SessionInfo]).returns(Future{Failure(new DuplicateSession)})
      val body = Json.obj(
        "gcm_id" -> "some gcm id",
        "os_version" -> "an os version",
        "app_version" -> "an app version"
      )
      val request = FakeRequest(POST,"/sessions/foo")
                      .withJsonBody(body)

      val result = controller.post("foo")(request)
      status(result) must equalTo(CONFLICT)
    }
    "return server error" in  {
      mockService.createSession(any[SessionInfo]).returns(Future{Failure(new Exception("something went wrong!"))})
      val body = Json.obj(
        "gcm_id" -> "some gcm id",
        "os_version" -> "an os version",
        "app_version" -> "an app version"
      )
      val request = FakeRequest(POST,"/sessions/foo")
        .withJsonBody(body)

      val result = controller.post("foo")(request)
      status(result) must equalTo(INTERNAL_SERVER_ERROR)
    }
  }




}

