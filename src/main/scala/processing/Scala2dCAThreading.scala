package processing

import processing.core._
import processing.model.CellularAutomata._

import scala.util.Random

object Scala2dCAThreading {
  def main(args: Array[String]): Unit = {
    PApplet.main(Array("--present", "processing.RunCAThreading"))
  }
}

private class RunCAThreading extends PApplet {

  var gen, rule = Seq[Boolean]()
  var screen = Seq[Boolean]()
  var screenOffset = 0
  def screenCellCount = (width * height) / (res*res)
  def CAThread = new Thread("CA") {
    override def run {
      while (true) {
        if (screen.length < screenCellCount) {
            gen = computeCA(gen, rule)
            screen = screen ++ gen
        }
      }
    }
  }
  var t = CAThread
  val res = 3

  def initVars {
    try {
      t.stop()
    }
    gen = 1 to width / res map (_ => Random.nextBoolean())
    rule = 1 to 8 map (_ => Random.nextBoolean())
    screen = Seq[Boolean]()
    screenOffset = 0
    t = CAThread
    t.start()
  }

  override def settings {
    size(2560, 1440)
    fullScreen
    initVars
  }

  override def setup {
    frameRate(10)
    noStroke()
    noSmooth()
  }

  override def keyPressed {
    initVars
  }

  override def draw {

    val todraw = screen.slice(screenOffset, screen.length)
    todraw.zipWithIndex.foreach {
      case (cell, index) => {
        val curline = (screenOffset + index) / (width / res)
        val curcol = (screenOffset + index) % (width / res)
        if (cell) fill(0) else fill(255)
        //println(curcol + "/" + curline)
        rect(curcol * res, curline * res, res, res)
      }
    }

    screenOffset = screenOffset + todraw.length
    if (screenOffset > screenCellCount) initVars

  }


}