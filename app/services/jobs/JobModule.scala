package services.jobs

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import services.Cache

class StringCache extends Cache[String]

class JobModule extends AbstractModule with AkkaGuiceSupport {
  override def configure() = {
    bind(classOf[StringCache]).asEagerSingleton()
    bindActor[CacheCleanupActor]("cache-cleanup-actor")
    bindActor[ItemCreationActor]("item-creation-actor")
    bind(classOf[Scheduler]).asEagerSingleton()
  }
}
