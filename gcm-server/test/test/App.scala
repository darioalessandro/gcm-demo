package test

import play.api.test.{WithApplication, FakeApplication}
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import org.specs2.execute.{AsResult,Result}
/**
 * Created with IntelliJ IDEA.
 * Date: 2014-06-02
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
abstract class App(app:FakeApplication = App.app) extends WithApplication(app)
{
  override def around[T: AsResult](t: => T):Result = super.around{
    //do anything you need to do before
    t
  }

}

object App {
  val app = FakeApplication()
}

