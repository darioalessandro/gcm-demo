package forms

import play.api.data.Form
import play.api.data.Forms._
/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-03
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */
case class CreateSessionForm(gcmId:String,osVersion:String,appVersion:String)

object CreateSessionForm {
  def getForm = Form(
    mapping(
      "gcm_id" -> text,
      "os_version" -> text,
      "app_version" -> text
    )(forms.CreateSessionForm.apply)(forms.CreateSessionForm.unapply)
  )
}
