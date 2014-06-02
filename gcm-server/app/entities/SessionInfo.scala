package entities

import play.api.libs.json._
import anorm._
import anorm.SqlParser._
/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-02
 * Time: 9:06 AM
 * To change this template use File | Settings | File Templates.
 */
case class SessionInfo(id:String,gcmRegistrationId:String,osVersion:String,appVersion:String) {
  def toJson:JsValue = SessionInfo.toJson(this)
}
object SessionInfo extends ((String,String,String,String) => SessionInfo) {
  implicit val jsonFormat = Json.format[SessionInfo]
  //sql stuff
  val sqlResult =
    get[String]("id") ~
    get[String]("gcm_registration_id") ~
    get[String]("os_version") ~
    get[String]("app_version")

  val fromDb = sqlResult.map {
    case id~gcm_registration_id~os_version~app_version => SessionInfo(id,gcm_registration_id,os_version,app_version)
  }

  //json
  def toJson(session:SessionInfo):JsValue = Json.toJson[SessionInfo](session)
  def fromJson(session:JsValue):Option[SessionInfo] = {
    try {
      Option(Json.fromJson(session).get)
    } catch {
      case e:Exception => None
    }

  }
}



