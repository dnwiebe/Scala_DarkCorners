package cse.darkcorners

import org.scalatest.path

/**
  * Created by dnwiebe on 4/26/17.
  */
class RequireTest extends path.FunSpec {

  describe ("In Predef.h, which means you have access to it with no import, is the require function.") {

    describe ("You can use it very simply to make runtime assertions.") {
      def choosyMethod (parameter: Int): Int = {
        require (parameter > 0)
        require (parameter < 10)
        parameter
      }

      it ("If you don't fall afoul of it, you'll never know it's there") {
        assert (choosyMethod (3) === 3)
      }

      it ("If you do, you'll get a handy runtime exception") {
        try {
          choosyMethod (11)
          fail ()
        }
        catch {
          case e: IllegalArgumentException => assert (e.getMessage === "requirement failed")
        }
      }
    }

    describe ("You can supply a message, if you like.") {
      def choosyMethod (parameter: Int): Int = {
        require (parameter > 0, s"$parameter is too small; must be > 0")
        require (parameter < 10, s"$parameter is too big; must be < 10")
        parameter
      }

      it ("Now the exception message is a little better.") {
        try {
          choosyMethod (11)
          fail ()
        }
        catch {
          case e: IllegalArgumentException =>
            assert (e.getMessage === "requirement failed: 11 is too big; must be < 10")
        }
      }
    }

    describe ("The message is call-by-name, so you can do other things with it too.") {
      var errorCount = 0;

      def choosyMethod (parameter: Int): Int = {
        require (parameter > 0, {errorCount += 1; s"$parameter is too small; must be > 0"})
        require (parameter < 10, {errorCount += 1; s"$parameter is too big; must be < 10"})
        parameter
      }

      it ("Now the exception message is a little better.") {
        assert (errorCount === 0)

        try {
          choosyMethod (11)
          fail ()
        }
        catch {
          case e: IllegalArgumentException =>
            assert (e.getMessage === "requirement failed: 11 is too big; must be < 10")
        }

        assert (errorCount === 1)
      }
    }
  }
}
