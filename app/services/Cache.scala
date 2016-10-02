package services

import java.time.Instant
import java.util.concurrent.ConcurrentLinkedDeque

import play.api.Logger

case class Item[A](value: A, expiration: Instant)

class Cache[A] {
  val logger = Logger(this.getClass)

  private val cache: ConcurrentLinkedDeque[Item[A]] = new ConcurrentLinkedDeque

  def peekLast: Option[Item[A]] = {
    if (cache.isEmpty) None else Some(cache.peekLast)
  }

  def pushLast(item: Item[A]): Unit = {
    cache.addLast(item)
  }

  def popFirstIf(f: Item[A] => Boolean): Option[Item[A]] = {
    val item = cache.peekFirst()

    if (item == null || !f(item)) {
      None
    } else {
      Some(cache.removeFirst())
    }
  }
}
