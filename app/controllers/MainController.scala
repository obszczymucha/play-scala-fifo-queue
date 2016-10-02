package controllers

import javax.inject._

import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import services.jobs.StringCache

import scala.concurrent.Future

@Singleton
class MainController @Inject()(cache: StringCache) extends Controller {
  val logger = Logger(this.getClass)

  def main = Action.async {
    cache.peekLast.fold({
      logger.info("The cache is empty!")
      Future(ServiceUnavailable(""))
    }) { item =>
      val message = s"Found in cache: ${item.value}"
      logger.info(message)
      Future(Ok(message))
    }
  }
}
