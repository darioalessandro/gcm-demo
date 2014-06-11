package services

import entities.Message
import play.Logger
import play.api.libs.json.Json
import play.api.libs.ws.WS

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import play.mvc.Http.Status._


/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-11
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
class MessagingException(reason:String, body:String) extends Exception
trait MessagingSvc {
  val messagingService:MessagingSvcContract
  /**
   * The service contract
   */
  trait MessagingSvcContract {
    def sendMessage(id: String,msg: Message): Future[Try[Boolean]]
  }

  /**
   * An implementation (GCM)
   */
  class GcmMessagingSvc(apiKey:String) extends MessagingSvcContract {
    import play.api.Play.current
    import scala.concurrent.ExecutionContext.Implicits.global
    override def sendMessage(id:String, msg: Message): Future[Try[Boolean]] = {
      val body = Json.obj(
      "registration_ids" -> Json.arr(id),
      "data" -> Json.obj(
          "content" -> msg.content
        )
      )
      Logger.info(body.toString())
      WS.url("https://android.googleapis.com/gcm/send")
        .withHeaders(
          "Authorization" -> s"key=$apiKey",
          "Content-type" -> "application/json"
        )
        .post(body).map { response =>
          response.status match {
            case OK =>
              Logger.info(response.body)
              Success(true)
            case other => Failure(new MessagingException(s"GCM message send failed. Response status: $other",response.body))
          }
        }

    }
  }
}


