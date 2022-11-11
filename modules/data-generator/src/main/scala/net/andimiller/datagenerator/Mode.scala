package net.andimiller.datagenerator

import scala.concurrent.duration.FiniteDuration

sealed trait Mode
object Mode {
  case class Generate(count: Long, seed: Option[Long])         extends Mode
  case class Stream(every: FiniteDuration, seed: Option[Long]) extends Mode
}
