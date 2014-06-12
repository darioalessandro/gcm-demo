package services

import scala.concurrent.{Future,ExecutionContext,Await}
import ExecutionContext.Implicits.global
import entities._
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-02
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
class SessionSvcSpec extends org.specs2.mutable.Specification with SessionSvc with org.specs2.mock.Mockito{
  override val sessionService: SessionSvcContract = spy(new SessionSvcImpl)
  //mock the repository
  val mockDb = mock[sessionService.SessionsRepoContract]
  //when the session asks for the session repo, return our mock
  org.mockito.Mockito.doReturn(mockDb).when(sessionService).repository

  "SessionService.create" should {
    "return created if persisted" in {
      val data = SessionInfo("anId","gcmid","os","app")
      //stub out the database
      mockDb.create(data).returns(Future{Success(true)})
      mockDb.get(anyString).returns(Future{Failure(new SessionNotFound)})
      val result = Await.result(sessionService.createSession(data),1000 milli)
      result must beLike {
        case Success(_) => ok
      }
    }
    "return failed if persistence fails" in {
      val data = SessionInfo("anId","gcmid","os","app")
      //stub out the database
      mockDb.create(data).returns(Future{Failure(new PersistenceException("persistence error"))})
      mockDb.get(anyString).returns(Future{Failure(new SessionNotFound)})
      val result = Await.result(sessionService.createSession(data),1000 milli)
      result must beLike {
        case Failure(e:PersistenceException) => ok
      }
    }
    "return duplicate result if session exists already" in {
      val data = SessionInfo("anId","gcmid","os","app")
      //stub out the database
      mockDb.get(anyString).returns(Future{Success(SessionInfo("anId","gcmid","os","app"))})
      mockDb.create(data).returns(Future{Success(true)})
      val result = Await.result(sessionService.createSession(data),1000 milli)
      result must beLike {
        case Failure(e:DuplicateSession) => ok
      }
    }
  }

  "SessionService.retrieve" should {
    "return session retrieved if exists" in {
      val data = "someId"
      mockDb.get(data).returns(Future{Success(SessionInfo("anId","gcmid","os","app"))})
      val result = Await.result(sessionService.retrieveSession(data),1000 milli)
      result must beLike {
        case Success(session) => ok
      }
    }
    "return session not found if none exists" in {
      val data = "someId"
      mockDb.get(data).returns(Future{Failure(new SessionNotFound)})
      val result = Await.result(sessionService.retrieveSession(data),1000 milli)
      result must beLike {
        case Failure(e:SessionNotFound) => ok
      }
    }
  }

  "SessionService.delete" should {
    "return session deleted if exists" in {
      val data = "someId"
      mockDb.delete(anyString).returns(Future{Success(true)})
      val result = Await.result(sessionService.deleteSession(data),1000 milli)
      result must beLike {
        case Success(_) => ok
      }
    }
    "return not found if session doesn't exist" in {
      val data = "someId"
      mockDb.delete(anyString).returns(Future{Failure(new SessionNotFound)})
      val result = Await.result(sessionService.deleteSession(data),1000 milli)
      result must beLike {
        case Failure(e:SessionNotFound) => ok
      }
    }
    "return failed if error deleting" in {
      val data = "someId"
      mockDb.delete(anyString).returns(Future{Failure(new PersistenceException("persistence error"))})
      val result = Await.result(sessionService.deleteSession(data),1000 milli)
      result must beLike {
        case Failure(e:PersistenceException) => ok
      }
    }
  }

}
