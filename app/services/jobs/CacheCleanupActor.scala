package services.jobs

import java.time.Instant

import akka.actor.Actor
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import services.Item

@Singleton
class CacheCleanupActor @Inject()(cache: StringCache) extends Actor {
  val logger = Logger(this.getClass)

  override def receive: Receive = {
    case "cleanupCache" => cleanupCache()
  }

  def cleanupCache(): Unit = {
    def itemHasExpired: (Item[String]) => Boolean = {
      item => item.expiration.isBefore(Instant.now())
    }

    logger.info(s"Cleaning up cache ($cache)")
    val item = cache.popFirstIf(itemHasExpired)
    item.foreach(item => logger.info(s"Item $item has been removed from the cache."))
  }
}
