package forms

import play.api.data.Form
import play.api.data.Forms._

/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-11
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
case class SubmitMessageForm(content:String)
object SubmitMessageForm {
  def getForm = Form(
    mapping(
      "content" -> text
    )(SubmitMessageForm.apply)(SubmitMessageForm.unapply)
  )
}
