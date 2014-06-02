package services

import entities._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import repositories.Sessions
import scala.Some
import entities.SessionCreationFailed
import entities.SessionCreated
import scala.util.Try


/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-02
 * Time: 9:04 AM
 * To change this template use File | Settings | File Templates.
 */
class SessionNotFound extends Exception
class DuplicateSession extends Exception
class PersistenceException extends Exception

trait SessionSvc {
  val sessionService:SessionSvcContract

  /**
   * Service contract
   */
  trait SessionSvcContract extends Sessions{
    def createSession(sessionInfo:SessionInfo):Future[Try[SessionInfo]]
    def retrieveSession(sessionId:String):Future[Try[SessionInfo]]
    def deleteSession(sessionId:String):Future[Try[Boolean]]
  }

  /**
   * Concrete implementation
   */
  class SessionSvcImpl extends SessionSvcContract {
    override def createSession(sessionInfo: SessionInfo): Future[Try[SessionInfo]] = ???
    override def retrieveSession(sessionId: String): Future[Try[SessionInfo]] = ???
    override def deleteSession(sessionId: String): Future[Try[Boolean]] = ???

    /**
     * Default implementation uses Anrom
     */
    override val repository: SessionsRepoContract = new SessionsRepoAnorm
  }
}
