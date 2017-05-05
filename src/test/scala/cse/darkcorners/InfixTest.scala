package cse.darkcorners

import org.scalatest.path

/**
  * Created by dnwiebe on 4/26/17.
  */
class InfixTest extends path.FunSpec {

  describe ("Methods with one parameter") {
    class InfixDemo (p: Int) {
      def a (q: Int): Int = p + q
      def s (q: Int): Int = p - q
    }

    val subject = new InfixDemo (10)

    describe ("can be called with dot notation and parentheses, of course") {
      val result = subject.a (5)

      it ("and get what you're expecting") {
        assert (result === 15)
      }
    }

    describe ("can be called with no dot and no parentheses: in other words, infix") {
      val result = subject s 7

      it ("and also get what you're expecting") {
        assert (result === 3)
      }
    }
  }

  describe ("But that's not all the infix Scala can do.") {
    class Converter[F, T] {
      def convert (input: F): T = {
        // no idea what real-world code would go here
        throw new UnsupportedOperationException ()
      }
    }

    describe ("You can declare variables with types in standard notation") {
      val stringToInt: Converter[String, Int] = null
    }

    describe ("Or you can declare them in infix notation") {
      val intToString: Int Converter String = null
    }
  }
}
