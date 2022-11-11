package net.andimiller.datagenerator

import cats.implicits._
import com.monovore.decline.{Command, Opts}

import scala.concurrent.duration.{DurationInt, FiniteDuration}

object CLI {

  val seed = Opts.option[Long]("seed", "Seed for the random number generator").orNone

  val stream: Opts[Mode.Stream] = Opts.subcommand("stream", "Generate an infinite stream of items", helpFlag = true)(
    (
      Opts
        .option[FiniteDuration]("every", "How often to generate an item, default 1 second")
        .withDefault(1.second),
      seed
    ).mapN(Mode.Stream)
  )

  val generate: Opts[Mode.Generate] = Opts.subcommand("generate", "Generate a set number of items", helpFlag = true)(
    (
      Opts.argument[Long]("count"),
      seed
    ).mapN(Mode.Generate)
  )

  def withName(name: String): Command[Mode] =
    Command(name, "A generator program to generate data", helpFlag = true)(stream.orElse(generate))

}
