package services

import entities.Message

import scala.concurrent.Future
import scala.util.Try

/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-11
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
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
  class GcmMessagingSvc extends MessagingSvcContract {
    override def sendMessage(id:String, msg: Message): Future[Try[Boolean]] = ???
  }
}


