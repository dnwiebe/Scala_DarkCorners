package cse.darkcorners

import org.scalatest.path

import scala.collection.mutable.ListBuffer

/**
  * Created by dnwiebe on 4/26/17.
  */
class H_CallByName extends path.FunSpec {
  describe ("Regarding call-by-name") {
    val log = new ListBuffer[String] ()

    def add (a: Int, b: Int): Int = {
      val c = a + b
      log += s"$a + $b == $c"
      c
    }

    describe ("a call to a function with a call-by-value parameter") {
      def callByValue (param: Int): Unit = { // Note parameter type of "Int"
        log += "function called"
        log += s"parameter: $param"
      }

      log += "before"
      callByValue (add (10, 15))
      log += "after"

      it ("records the expected sequence of events") {
        assert (log === ListBuffer (
          "before",
          "10 + 15 == 25",   // add is called to compute a value for the parameter
          "function called", // then the function is called
          "parameter: 25",   // with that parameter
          "after"
        ))
      }
    }

    describe ("a call to a function with a call-by-name parameter") {
      def callByName (param: => Int): Unit = { // Note parameter type of "=> Int"
        log += "function called"
        log += s"parameter: $param"
      }

      log += "before"
      callByName (add (10, 15))
      log += "after"

      it ("records a different sequence of events") {
        assert (log === ListBuffer (
          "before",
          "function called", // the function is called with the name of the add function
          "10 + 15 == 25",   // _then_ the add function is called to get the value to interpolate
          "parameter: 25",   // and the value is interpolated
          "after"
        ))
      }
    }
  }

  describe ("One quite reasonable use of call-by-name is forensic logging.") {
    val messages = new ListBuffer[String] ()
    var currentLogFilter = 3

    def log (message: => String, level: Int): Unit = { // call by name
      if (level > currentLogFilter) {
        messages += message
      }
    }

    def complicatedFormatter (message: String): String = {
      Thread.sleep (50) // Expensive formatting that you only want to do if you're actually going to log
      message
    }

    describe ("When the log isn't important enough to match the current logging filter") {
      currentLogFilter = 5

      val before = System.currentTimeMillis ()
      log (complicatedFormatter ("Nobody listens to a word I say"), 3)
      val after = System.currentTimeMillis ()

      it ("the expensive formatting isn't done") {
        assert (after - before < 50)
      }
    }

    describe ("Only when the log is important enough") {
      currentLogFilter = 1

      val before = System.currentTimeMillis ()
      log (complicatedFormatter ("Now they're a little more interested"), 3)
      val after = System.currentTimeMillis ()

      it ("do we do the expensive formatting") {
        assert (after - before >= 50)
      }
    }
  }

  describe ("Call-by-name isn't the same as lazy vals, although it's similar") {
    val log = new ListBuffer[String] ()

    def add (a: Int, b: Int): Int = {
      val c = a + b
      log += s"$a + $b == $c"
      c
    }

    describe ("A non-lazy val") {
      log += "before declaration"
      val nonLazyVal = add (4, 5)
      log += "after declaration"
      log += s"result: $nonLazyVal"

      it ("acts just as expected") {
        assert (log === ListBuffer (
          "before declaration",
          "4 + 5 == 9",
          "after declaration",
          "result: 9"
        ))
      }
    }

    describe ("A lazy val") {
      log += "before declaration"
      lazy val lazyVal = add (4, 5);
      log += "after declaration"
      log += s"result: $lazyVal"

      it ("acts a little differently") {
        assert (log === ListBuffer (
          "before declaration",
          "after declaration",
          "4 + 5 == 9",
          "result: 9"
        ))
      }
    }
  }
}
