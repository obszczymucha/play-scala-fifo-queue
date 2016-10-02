package services.jobs

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

import akka.actor.Actor
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import services.Item

@Singleton
class ItemCreationActor @Inject()(cache: StringCache) extends Actor {
  val logger = Logger(this.getClass)

  override def receive: Receive = {
    case "createItem" => createItem()
  }

  def createItem(): Unit = {
    logger.info(s"Creating up cache ($cache)")
    cache.peekLast.fold({
      logger.info(s"No items found, creating one.")
      cache.pushLast(Item(UUID.randomUUID().toString, Instant.now().plus(10, ChronoUnit.SECONDS)))
    }) {
      item =>
        if (item.expiration.minus(3, ChronoUnit.SECONDS).isBefore(Instant.now())) {
          logger.info(s"Last item is about to expire, creating new one.")
          cache.pushLast(Item(UUID.randomUUID().toString, Instant.now().plus(10, ChronoUnit.SECONDS)))
        }
    }
  }
}