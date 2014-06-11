package controllers

import entities.{SessionInfo, Message}
import forms.SubmitMessageForm
import play.api.mvc.{SimpleResult, Action, Controller}
import services.{SessionNotFound, SessionSvc, MessagingSvc}
import play.api.data.Form
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-11
 * Time: 9:38 AM
 * To change this template use File | Settings | File Templates.
 */
trait Messaging {
  this: Controller with MessagingSvc with SessionSvc=>
  def post(id:String) = Action.async { implicit request =>
    forms.SubmitMessageForm.getForm.bindFromRequest() match {
      case f:Form[SubmitMessageForm] if f.hasErrors => Future{BadRequest(s"${f.errorsAsJson}")}
      case f:Form[SubmitMessageForm] => {
        sessionService.retrieveSession(id) flatMap  {
          case Success(s:SessionInfo) =>
            messagingService.sendMessage(s.gcmRegistrationId,Message(f.get.content)) map {
              case Success(b:Boolean) => Accepted("Message sent")
              case Failure(t:Throwable) => InternalServerError(s"Error while sending message: ${t.getMessage}")
            }
          case Failure(t:SessionNotFound) => Future{NotFound(s"Session '$id' not found")}
          case Failure(t:Throwable) => Future{InternalServerError(s"Error while sending message: ${t.getMessage}")}
        }
      }
    }
  }

}
object Messaging extends Controller with Messaging with MessagingSvc with SessionSvc{
  override val messagingService = new GcmMessagingSvc
  override val sessionService = new SessionSvcImpl
}

