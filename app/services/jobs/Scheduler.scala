package services.jobs

import akka.actor.{ActorRef, ActorSystem}
import com.google.inject.Inject
import com.google.inject.name.Named

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class Scheduler @Inject()(val system: ActorSystem,
                          @Named("cache-cleanup-actor") val cacheCleanupActor: ActorRef,
                          @Named("item-creation-actor") val itemCreationActor: ActorRef) {
  val scheduledJobContext: ExecutionContext = system.dispatchers.lookup("contexts.scheduled-jobs")
  system.scheduler.schedule(0.seconds, 2.seconds, itemCreationActor, "createItem")(scheduledJobContext)
  system.scheduler.schedule(0.seconds, 1.seconds, cacheCleanupActor, "cleanupCache")(scheduledJobContext)
}
