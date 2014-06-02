package services

import entities.SessionInfo
import scala.concurrent.Future

import repositories.Sessions


/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-02
 * Time: 9:04 AM
 * To change this template use File | Settings | File Templates.
 */
trait SessionSvc {
  val sessionService:SessionSvcContract

  /**
   * Service contract
   */
  trait SessionSvcContract extends Sessions{
    def createSession(sessionInfo:SessionInfo):Future[Boolean]
    def retrieveSession(sessionId:String):Future[Option[SessionInfo]]
    def deleteSession(sessionId:String):Future[Boolean]
  }

  /**
   * Concrete implementation
   */
  class SessionSvcImpl extends SessionSvcContract {
    override def createSession(sessionInfo: SessionInfo): Future[Boolean] = {
      repository.create(sessionInfo)
    }

    override def retrieveSession(sessionId: String): Future[Option[SessionInfo]] = ???

    override def deleteSession(sessionId: String): Future[Boolean] = ???

    /**
     * Default implementation uses Anrom
     */
    override val repository: SessionsRepoContract = new SessionsRepoAnorm
  }
}
