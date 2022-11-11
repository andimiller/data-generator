package net.andimiller.datagenerator

import cats.Monad
import cats.effect.kernel.Async
import cats.effect.std.Random
import cats.effect.{ExitCode, IO, IOApp}
import org.scalacheck.{Arbitrary, Gen}
import io.circe.Encoder
import io.circe.syntax._
import fs2._
import org.scalacheck.rng.Seed

import scala.concurrent.duration.FiniteDuration
import scala.jdk.CollectionConverters.MapHasAsScala

class DataGeneratorApp[T: Arbitrary: Encoder](programName: String) extends IOApp {

  def items[F[_]: Async: Random]: Stream[F, String] =
    for {
      seed <- Stream.eval(Random[F].nextLong)
      item <- Stream.eval(Async[F].delay { implicitly[Arbitrary[T]].arbitrary.pureApply(Gen.Parameters.default, Seed(seed)) })
      str   = item.asJson.noSpaces
    } yield str

  def generate[F[_]: Async: Random](n: Long): F[Unit] =
    items[F]
      .take(n)
      .through(fs2.text.utf8.encode)
      .through(fs2.io.stdout)
      .compile
      .drain

  def stream[F[_]: Async: Random](every: FiniteDuration): F[Unit] =
    Stream
      .awakeEvery(every)
      .zip(items[F])
      .map(_._2)
      .through(fs2.text.utf8.encode)
      .through(fs2.io.stdout)
      .compile
      .drain

  def random(seed: Option[Long]): IO[Random[IO]] = seed.fold(Random.scalaUtilRandom[IO]) { Random.scalaUtilRandomSeedLong[IO](_) }

  override def run(args: List[String]): IO[ExitCode] =
    CLI.withName(programName).parse(args, System.getenv().asScala.toMap) match {
      case Left(value)  => IO.println(value).as(ExitCode.Success)
      case Right(value) =>
        value match {
          case Mode.Generate(count, seed) =>
            for {
              implicit0(r: Random[IO]) <- random(seed)
              _                        <- generate[IO](count)
            } yield ExitCode.Success
          case Mode.Stream(every, seed)   =>
            for {
              implicit0(r: Random[IO]) <- random(seed)
              _                        <- stream[IO](every)
            } yield ExitCode.Success

        }
    }

}
