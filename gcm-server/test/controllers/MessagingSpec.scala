package controllers

import entities.{Message, SessionInfo}
import play.api.libs.json.Json
import play.api.mvc.Results
import play.api.mvc.Controller
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.{SessionNotFound, SessionSvc, MessagingSvc}

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-11
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */
class MessagingSpec extends org.specs2.mutable.Specification with org.specs2.mock.Mockito with Results{
  val controller = new Controller with Messaging with MessagingSvc with SessionSvc {
    override val messagingService = mock[MessagingSvcContract]
    override val sessionService = mock[SessionSvcContract]
  }
  val mockMessagingService = controller.messagingService
  val mockSessionService = controller.sessionService

  "POST /sessions/:id/message" should {
    "return 202 if session exists and messaging is successful"  in {
      mockSessionService.retrieveSession(anyString).returns(Future{Success(SessionInfo("foo","bar","bar","bar"))})
      mockMessagingService.sendMessage(anyString, any[Message]).returns(Future{Success(true)})
      val body = Json.obj(
        "content" -> "some content"
      )
      val request = FakeRequest(POST,"/sessions/id/message")
        .withJsonBody(body)

      val result = controller.post("id")(request)
      status(result) must equalTo(ACCEPTED)
    }
    "return 404 if the session doesn't exist" in {
      mockSessionService.retrieveSession(anyString).returns(Future{Failure(new SessionNotFound)})
      mockMessagingService.sendMessage(anyString,any[Message]).returns(Future{Success(true)})
      val body = Json.obj(
        "content" -> "some content"
      )
      val request = FakeRequest(POST,"/sessions/id/message")
        .withJsonBody(body)

      val result = controller.post("id")(request)
      status(result) must equalTo(NOT_FOUND)
    }
    "return 400 (BAD REQUEST) if request body is borked" in {
      mockSessionService.retrieveSession(anyString).returns(Future{Failure(new SessionNotFound)})
      mockMessagingService.sendMessage(anyString,any[Message]).returns(Future{Success(true)})
      val body = Json.obj(
        "borked" -> "some content"
      )
      val request = FakeRequest(POST,"/sessions/id/message")
        .withJsonBody(body)

      val result = controller.post("id")(request)
      status(result) must equalTo(BAD_REQUEST)
    }

  }
}
