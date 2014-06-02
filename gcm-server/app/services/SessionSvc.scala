package services

import entities._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import repositories.Sessions
import scala.Some
import entities.SessionCreationFailed
import entities.SessionCreated


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
    def createSession(sessionInfo:SessionInfo):Future[SessionServiceResult]
    def retrieveSession(sessionId:String):Future[SessionServiceResult]
    def deleteSession(sessionId:String):Future[SessionServiceResult]
  }

  /**
   * Concrete implementation
   */
  class SessionSvcImpl extends SessionSvcContract {
    override def createSession(sessionInfo: SessionInfo): Future[SessionServiceResult] =
      retrieveSession(sessionInfo.id).flatMap[SessionServiceResult] {
        case SessionNotFound() => repository.create(sessionInfo).map[SessionServiceResult] {
          case true => SessionCreated()
          case false => SessionCreationFailed()
        }
        case SessionRetrieved(session) => Future{SessionDuplicated()}
      }

    override def retrieveSession(sessionId: String): Future[SessionServiceResult] =
      repository.get(sessionId).map {
        case Some(session) => SessionRetrieved(session)
        case None => SessionNotFound()
      }

    override def deleteSession(sessionId: String): Future[SessionServiceResult] =
      retrieveSession(sessionId).flatMap[SessionServiceResult] {
        case SessionRetrieved(session) => repository.delete(sessionId).map[SessionServiceResult] {
          case true => SessionDeleted()
          case false => SessionDeletionFailed()
        }
        case t:SessionServiceResult => Future{t}
      }

    /**
     * Default implementation uses Anrom
     */
    override val repository: SessionsRepoContract = new SessionsRepoAnorm
  }
}
