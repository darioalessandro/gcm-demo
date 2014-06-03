package controllers

import play.api.mvc.{Action, Controller}
import forms.CreateSessionForm
import play.api.data.Form
import entities.SessionInfo
import scala.util.{Success,Failure}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Logger
import scala.concurrent.Future
import services.DuplicateSession

/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-02
 * Time: 9:02 AM
 * To change this template use File | Settings | File Templates.
 */
trait Session {
  this: Controller with services.SessionSvc =>
  def post(id:String) = Action.async { implicit request =>
    forms.CreateSessionForm.getForm.bindFromRequest match {
      case f:Form[CreateSessionForm] if f.hasErrors =>
        Future {BadRequest(f.errorsAsJson)}
      case f:Form[CreateSessionForm] => {
        val data = f.get
        val session = SessionInfo(id,data.gcmId,data.osVersion,data.appVersion)
        sessionService.createSession(session).map {
          case Success(v:SessionInfo) => Created(v.toJson)
          case Failure(dupErr:DuplicateSession) => ???
          case Failure(t:Throwable) =>
            Logger.error(t.getMessage,t)
            ???
        }
      }
    }
  }
  def get(id:String) = Action.async { implicit request => ??? }
  def delete(id:String) = Action.async { implicit request => ???}
}
object Session extends Controller with Session with services.SessionSvc{
  //Concrete implementation
  val sessionService = new SessionSvcImpl
}
