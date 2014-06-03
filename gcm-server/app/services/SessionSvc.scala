package services

import entities._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import repositories.Sessions

import scala.util.{Success, Try, Failure}


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
    override def createSession(sessionInfo: SessionInfo): Future[Try[SessionInfo]] =
      retrieveSession(sessionInfo.id).flatMap[Try[SessionInfo]]{
        case Success(s) => Future{Failure(new DuplicateSession)}
        case Failure(t:SessionNotFound) =>repository.create(sessionInfo).map[Try[SessionInfo]] {
          case Success(_) => Success(sessionInfo)
          case Failure(e) => Failure(e)
        }
        case Failure(t) => Future{Failure(t)}
      }
    override def retrieveSession(sessionId: String): Future[Try[SessionInfo]] = repository.get(sessionId)

    override def deleteSession(sessionId: String): Future[Try[Boolean]] = repository.delete(sessionId)


    /**
     * Default implementation uses Anrom
     */
    override val repository: SessionsRepoContract = new SessionsRepoAnorm
  }
}
