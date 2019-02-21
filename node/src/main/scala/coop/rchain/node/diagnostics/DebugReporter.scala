package coop.rchain.node.diagnostics

import java.util.concurrent.atomic.AtomicReference

import cats.implicits._

import com.typesafe.config.Config
import com.typesafe.scalalogging.Logger
import kamon.MetricReporter
import kamon.metric.PeriodSnapshot

class DebugReporter extends MetricReporter {

  private val logger = Logger(this.getClass)
  private val ref    = new AtomicReference(Map.empty[String, Long])
  private val nl     = System.lineSeparator()

  def reportPeriodSnapshot(snapshot: PeriodSnapshot): Unit = {
    val counters = snapshot.metrics.counters.map(c => (c.name, c.value)).toMap
    val current  = ref.updateAndGet(_ |+| counters)
    logger.info(current.map { case (k, v) => s"$k: $v" }.mkString(nl, nl, ""))
  }

  def start(): Unit = {}

  def stop(): Unit = {}

  def reconfigure(config: Config): Unit = {}
}
