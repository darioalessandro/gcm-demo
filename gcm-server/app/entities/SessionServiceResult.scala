package entities

/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-02
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
abstract class SessionServiceResult
case class SessionRetrieved(session:SessionInfo) extends SessionServiceResult
case class SessionNotFound() extends SessionServiceResult
case class SessionCreated() extends SessionServiceResult
case class SessionDeleted() extends SessionServiceResult
case class SessionDeletionFailed() extends SessionServiceResult
case class SessionCreationFailed() extends SessionServiceResult
case class SessionDuplicated() extends SessionServiceResult
