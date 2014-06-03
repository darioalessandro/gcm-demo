package repositories

import entities.SessionInfo
import scala.concurrent.Future
import scala.util.Try

/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-02
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
trait Sessions {
  val repository:SessionsRepoContract

  trait SessionsRepoContract {
    def create(session:SessionInfo):Future[Try[Boolean]]
    def get(id:String):Future[Try[SessionInfo]]
    def delete(id:String):Future[Try[Boolean]]
  }

  /**
   * Anorm implementation of the repo
   */
  class SessionsRepoAnorm extends SessionsRepoContract {
    override def create(session: SessionInfo): Future[Try[Boolean]] = ???

    override def get(id: String): Future[Try[SessionInfo]] = ???

    override def delete(id: String): Future[Try[Boolean]] = ???
  }
}
