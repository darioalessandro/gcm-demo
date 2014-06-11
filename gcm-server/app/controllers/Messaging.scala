package controllers

import entities.{SessionInfo, Message}
import forms.SubmitMessageForm
import play.api.mvc.{Action, Controller}
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
        sessionService.retrieveSession(id) map {
          case Success(s:SessionInfo) =>
            messagingService.sendMessage(s.gcmRegistrationId,Message(f.get.content)) match {
              case b => Accepted(s"Message sent")
            }
          case Failure(t:SessionNotFound) => NotFound(s"Session '$id' not found")
          case Failure(t:Throwable) => ???
        }
      }
      case _ => ???
    }
  }

}
object Messaging extends Controller with Messaging with MessagingSvc with SessionSvc{
  override val messagingService = new GcmMessagingSvc
  override val sessionService = new SessionSvcImpl
}

