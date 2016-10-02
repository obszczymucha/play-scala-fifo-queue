package services

import java.time.Instant
import java.time.temporal.ChronoUnit

import org.scalatest.{FunSpec, Matchers}

import scala.language.postfixOps

class CacheSpec extends FunSpec with Matchers {

  describe("peek") {
    it("should return None if cache is empty") {
      // When
      val result = newCache.peekLast

      // Then
      result shouldBe None
    }

    it("should return last item that was added") {
      // Given
      val cache = newCache
      val item = Item("dust", Instant.now())
      val item2 = Item("particles", Instant.now())
      cache.pushLast(item)
      cache.pushLast(item2)

      // When
      val result = cache.peekLast

      // Then
      result shouldBe Some(item2)
    }
  }

  def newCache: Cache[String] = {
    new Cache[String]() {}
  }

  describe("put") {
    it("should add item to the cache") {
      // Given
      val cache = newCache
      val item = Item("vodka", Instant.now())
      cache.pushLast(item)

      // When
      val result = cache.peekLast

      // Then
      result shouldBe Some(item)
    }
  }

  describe("popIfExpiredAfter") {
    it("should return none if last item has not expired") {
      // Given
      val cache = newCache
      cache.pushLast(Item("Princess", Instant.now().plus(5, ChronoUnit.MINUTES)))
      cache.pushLast(Item("Kenny", Instant.now().plus(5, ChronoUnit.MINUTES)))

      // When
      val result = cache.popFirstIf(itemHasExpired)

      // Then
      result shouldBe None
    }

    it("should return pop each expired item individually") {
      // Given
      val cache = newCache
      val item = Item("The Spice", Instant.now().minus(5, ChronoUnit.MINUTES))
      val item2 = Item("extends life", Instant.now().minus(5, ChronoUnit.MINUTES))
      val item3 = Item("Active item", Instant.now().plus(5, ChronoUnit.MINUTES))
      cache.pushLast(item)
      cache.pushLast(item2)
      cache.pushLast(item3)

      // When
      val lastResult = cache.popFirstIf(itemHasExpired)
      val secondLastResult = cache.popFirstIf(itemHasExpired)

      // Then
      lastResult shouldBe Some(item)
      secondLastResult shouldBe Some(item2)
      cache.peekLast shouldBe Some(item3)
    }
  }

  private def itemHasExpired: (Item[String]) => Boolean = {
    item => item.expiration.isBefore(Instant.now())
  }
}
