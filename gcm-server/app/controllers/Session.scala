package controllers

import play.api.mvc.{Action, Controller}

/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-02
 * Time: 9:02 AM
 * To change this template use File | Settings | File Templates.
 */
object Session extends Controller with services.SessionSvc{
  //Concrete implementation
  val sessionService = new SessionSvcImpl

  def post(id:String) = Action.async { implicit request =>
    ???
  }
  def get(id:String) = Action.async { implicit request => ??? }
  def delete(id:String) = Action.async { implicit request => ???}
}
