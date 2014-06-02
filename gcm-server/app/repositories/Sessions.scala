package repositories

import entities.SessionInfo
import scala.concurrent.Future

/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-02
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
trait Sessions {
  val repository:SessionsRepoContract

  trait SessionsRepoContract {
    def create(session:SessionInfo):Future[Boolean]
    def get(id:String):Future[Option[SessionInfo]]
    def delete(id:String):Future[Boolean]
  }

  /**
   * Anorm implementation of the repo
   */
  class SessionsRepoAnorm extends SessionsRepoContract {
    override def create(session: SessionInfo): Future[Boolean] = ???

    override def get(id: String): Future[Option[SessionInfo]] = ???

    override def delete(id: String): Future[Boolean] = ???
  }
}
