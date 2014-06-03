package repositories

import org.specs2.mutable.Specification
import scala.concurrent.Await
import scala.concurrent.duration._
import entities.SessionInfo
import scala.concurrent.{Future,ExecutionContext,Await}
import ExecutionContext.Implicits.global
import services.SessionNotFound
import scala.util.Failure
import services.SessionNotFound
import java.util.UUID

/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-03
 * Time: 9:03 AM
 * To change this template use File | Settings | File Templates.
 */
class SessionRepoSpec extends Specification
  with Sessions
  with org.specs2.mock.Mockito {
  //we'll be integration testing the Anorm implementation
  override val repository: SessionsRepoContract = new SessionsRepoAnorm

  "get" should {
    "return a session if it exists" in new test.App {
      val sessionInfo = Await.result(repository.get("session_1"),1000 milli)
      sessionInfo must beASuccessfulTry[SessionInfo]
    }
    "return failure if it doesn't exist" in new test.App {
      val sessionInfo = Await.result(repository.get("session_foo"),1000 milli)
      sessionInfo must beAFailedTry[SessionInfo]
    }
  }

  "create" should {
    "return success" in new test.App {
      val data = SessionInfo(UUID.randomUUID().toString,"reg_id","os_version","app_version")
      val result = Await.result(repository.create(data),1000 milli)
      result must beASuccessfulTry[Boolean]
    }
    "return failure if already exists" in new test.App {
      val data = SessionInfo("session_1","reg_id","os_version","app_version")
      val result = Await.result(repository.create(data),1000 milli)
      result must beAFailedTry[Boolean]
    }
  }

  "delete" should {
    "return success" in new test.App {
      val sessionId: String = UUID.randomUUID().toString
      val data = SessionInfo(sessionId, "reg_id", "os_version", "app_version")

      val createResult = Await.result(repository.create(data),1000 milli)
      createResult must beASuccessfulTry[Boolean]
      val result = Await.result(repository.delete(sessionId),1000 milli)
      result must beASuccessfulTry[Boolean]
    }
    "return failure" in new test.App {
      val sessionId: String = UUID.randomUUID().toString
      val data = SessionInfo(sessionId, "reg_id", "os_version", "app_version")

      val result = Await.result(repository.delete(sessionId),1000 milli)
      result must beAFailedTry[Boolean]
    }
  }
}
