package repositories

import entities.SessionInfo
import scala.concurrent.Future
import scala.util.{Success,Failure, Try}
import services.{PersistenceException, SessionNotFound}


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

    import anorm.SQL
    import play.api.db.DB
    import play.api.Play.current
    import scala.concurrent.ExecutionContext.Implicits.global
    val fields =
      """
        |session_id,
        |gcm_registration_id,
        |os_version,
        |app_version
      """.stripMargin

    override def create(session: SessionInfo): Future[Try[Boolean]] = {
      Future {
        DB.withConnection { implicit connection =>
          SQL(s"INSERT INTO SESSION($fields) VALUES({session_id},{gcm_id},{os_version},{app_version});")
            .on(
              'session_id -> session.id,
              'gcm_id -> session.gcmRegistrationId,
              'os_version -> session.osVersion,
              'app_version -> session.appVersion
            ).executeInsert()
        }
      }.map {
        case None => Success(true)
        case Some(_) => ???
      }.recover {
        case e:Exception => Failure(new PersistenceException(e.getMessage))
      }
  }

    override def get(id: String): Future[Try[SessionInfo]] = Future {
      val option: Option[SessionInfo] = DB.withConnection { implicit connection =>
        SQL( s"""SELECT $fields FROM SESSION WHERE session_id = {session_id}""")
          .on(
            'session_id -> id
          )
          .as(SessionInfo.fromDb.singleOpt)
      }
      //we may or may not get something from the db…
      option match {
        case Some(v) => Success(v)
        case None => Failure(new SessionNotFound)
      }
    }


    override def delete(id: String): Future[Try[Boolean]] =
      Future {
        DB.withConnection(implicit connection =>
          SQL(s"DELETE FROM SESSION WHERE session_id={session_id}")
            .on(
              'session_id -> id
            )
            .executeUpdate()
        )
      }.map {
        case 0 => Failure(new SessionNotFound)
        case count:Int => Success(true)
      }

  }
}
