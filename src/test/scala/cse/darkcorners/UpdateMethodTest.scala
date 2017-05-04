package cse.darkcorners

import org.scalatest.path

/**
  * Created by dnwiebe on 4/28/17.
  */
class UpdateMethodTest extends path.FunSpec {
  describe ("") {it ("") {fail ()}}

  describe ("Methods with one parameter") {
    class Example (val value: Int) {
      def plus (augend: Int): Int = value + augend
    }
    val addend = new Example (34)

    describe ("when called in the classical style") {

      val sum = addend.plus (43)

      it ("work fine") {
        assert (sum === 77)
      }
    }

    describe ("when called in the infix style") {

      val sum = addend plus 43

      it ("also work fine") {
        assert (sum === 77)
      }
    }
  }

  fail ("Can also be used with types")
}
