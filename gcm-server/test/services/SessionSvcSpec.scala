package services

import scala.concurrent.{Future,ExecutionContext,Await}
import ExecutionContext.Implicits.global
import entities.SessionInfo
import scala.concurrent.duration._

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
    "return true if persisted" in {
      val data = SessionInfo("anId","gcmid","os","app")
      //stub out the database
      mockDb.create(data).returns(Future{true})
      val result = Await.result(sessionService.createSession(data),1000 milli)
      result must beTrue
    }
    "return false if persistence fails" in {
      val data = SessionInfo("anId","gcmid","os","app")
      //stub out the database
      mockDb.create(data).returns(Future{false})
      val result = Await.result(sessionService.createSession(data),1000 milli)
      result must beFalse
    }
  }

  "SessionService.retrieve" should {
    "return some if exists" in {
      val data = "someId"
      mockDb.get(data).returns(Future{Some(SessionInfo("anId","gcmid","os","app"))})
      val result = Await.result(sessionService.retrieveSession(data),1000 milli)
      result must beSome
    }
    "return none if none exists" in {
      val data = "someId"
      mockDb.get(data).returns(Future{None})
      val result = Await.result(sessionService.retrieveSession(data),1000 milli)
      result must beNone
    }
  }

}
