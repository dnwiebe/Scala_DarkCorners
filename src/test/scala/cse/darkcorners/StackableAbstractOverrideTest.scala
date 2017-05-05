package cse.darkcorners

import org.scalatest.path

import scala.collection.mutable.ListBuffer

/**
  * Created by dnwiebe on 4/26/17.
  */
class StackableAbstractOverrideTest extends path.FunSpec {
  describe ("You can use abstract and super keywords in stackable traits") {
    val log = new ListBuffer[String]

    trait Reverser {
      def reverse (s : String) : String
    }

    trait TimingReverser extends Reverser {
      abstract override def reverse (s : String): String = {
        val start = System.currentTimeMillis
        val result = super.reverse (s)
        val dur = System.currentTimeMillis-start
        log += "Executed reverse in %s ms".format (dur)
        result
      }
    }

    trait ParameterPrintingReverser extends Reverser {
      abstract override def reverse (s : String): String = {
        log += "Called reverse with s=%s".format (s)
        super.reverse (s)
      }
    }

    trait ImplementingReverser extends Reverser {
      def reverse (s: String): String = s.reverse
    }

    describe ("When the traits are stacked together") {
      val subject = new ImplementingReverser with ParameterPrintingReverser with TimingReverser

      val result = subject.reverse ("booga")

      it ("the string is reversed properly") {
        assert (result === "agoob")
      }

      it ("all the stacked traits are called") {
        assert (log (0) === "Called reverse with s=booga")
        assert (log (1).startsWith ("Executed reverse in "))
      }
    }
  }
}
