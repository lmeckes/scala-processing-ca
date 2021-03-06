package com.lms.ca

object CAModel {

  def computeGeneration(gen: => Seq[Boolean], rule: Seq[Boolean], complexity: Int): Seq[Boolean] = {

    def child(siblings: Seq[Boolean]): Boolean = {
      implicit def b2i(b: Boolean) = if (b) 1 else 0
      var n = 0
      siblings.foreach { case (s) => n = (n << 1) + s }
      rule(n)
    }

    def find(genIndex: Int): List[Boolean] = {
      def cycle(offset: Int) = {
        val d = genIndex + offset
        if (d > gen.length - 1) d - gen.length else if (d < 0) gen.length + d else math.abs(d)
      }
      (1 to complexity).map(offset => gen(cycle(offset))).toList
    }

    gen.zipWithIndex.map {
      case (c, i) => child(find(-i) ::: List(c) ::: find(i))
    }

  }

}