package cse.darkcorners

import org.scalatest.path

/**
  * Created by dnwiebe on 4/26/17.
  */
class LocallyTest extends path.FunSpec {

  describe ("The locally function is defined in Predef.scala, so you get it for free:") {
    it ("It introduces a temporary bubble of alternate reality.") {
      val mood = "I hate my life.  Everything sucks."

      assert (mood === "I hate my life.  Everything sucks.")

      locally {
        val mood = "Ooh!  Coffee!  Much better."

        assert (mood == "Ooh!  Coffee!  Much better.")
      }

      assert (mood === "I hate my life.  Everything sucks.")
    }

    it ("It also returns a value, so you can use it like an inlined method call to reduce namespace pollution.") {
      val x = someComplicatedExpressionThatReturns (72)
      val y = someComplicatedExpressionThatReturns (83)
      val z = (x + y) - locally {
        val x = someComplicatedExpressionThatReturns (1462)
        val y = someComplicatedExpressionThatReturns (1522)
        y - x
      }

      assert (z === 95)
    }
  }

  def someComplicatedExpressionThatReturns (n: Int): Int = n
}
