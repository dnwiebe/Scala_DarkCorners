package cse.darkcorners

import org.scalatest.path

/**
  * Created by dnwiebe on 4/26/17.
  */
class E_DSL extends path.FunSpec {

  describe ("With multiple parameter lists, you can define your own language 'keywords'.") {
    def myWhile (cond: => Boolean) (code: => Unit): Unit = {
      if (cond) {
        code
        myWhile (cond) (code)
      }
    }

    it ("It just works") {
      var oddNumbers: List[Int] = Nil
      var count = 15

      myWhile (count > 0) {
        oddNumbers = count :: oddNumbers
        count -= 2
      }

      assert (oddNumbers === List (1, 3, 5, 7, 9, 11, 13, 15))
    }
  }

  describe ("You can even add break and continue.") {
    class BreakException extends RuntimeException
    class ContinueException extends RuntimeException
    def myBreak: Nothing = throw new BreakException
    def myContinue: Nothing = throw new ContinueException
    def myWhile (cond: => Boolean) (code: => Unit): Unit = {
      try {
        if (cond) {
          try {
            code
          }
          catch {
            case _: ContinueException =>
          }
          myWhile (cond) (code)
        }
      }
      catch {
        case _: BreakException =>
      }
    }

    it ("It still works without break or continue") {
      var oddNumbers: List[Int] = Nil
      var count = 15

      myWhile (count > 0) {
        oddNumbers = count :: oddNumbers
        count -= 2
      }

      assert (oddNumbers === List (1, 3, 5, 7, 9, 11, 13, 15))
    }

    it ("You can break out") {
      var oddNumbers: List[Int] = Nil
      var count = 15

      myWhile (count > 0) {
        if (count < 5) myBreak
        oddNumbers = count :: oddNumbers
        count -= 2
      }

      assert (oddNumbers === List (5, 7, 9, 11, 13, 15))
    }

    it ("You can continue") {
      var oddNumbers: List[Int] = Nil
      var count = 15

      myWhile (count > 0) {
        count -= 2
        if (((count + 2) % 5) == 0) myContinue
        oddNumbers = count + 2 :: oddNumbers
      }

      assert (oddNumbers === List (1, 3, 7, 9, 11, 13))
    }

    it ("Benefit of this method: no infinite loops!") {
      try {
        myWhile (true) {
          // forever? Well...no.
        }
        fail ()
      }
      catch {
        case _ : StackOverflowError =>
      }
    }
  }

  describe ("We can go a little further, too...") {
    def dont (code: => Unit) = new DontCommand (code)
    class DontCommand (code: => Unit) {
      def unless (cond: => Boolean): Unit = if (cond) code
      def until (cond: => Boolean): Unit = {while (!cond) {}; code}
    }

    describe ("Here's a backwards if statement.") {
      var flag = false
      def tryMeWith (param: Int): Unit = {
        dont {flag = true} unless (param > 10)
      }

      describe ("If the parameter isn't greater than 10") {
        tryMeWith (5)

        it ("it doesn't set the flag") {
          assert (flag === false)
        }
      }

      describe ("If the parameter is greater than 10") {
        tryMeWith (15)

        it ("it does set the flag") {
          assert (flag === true)
        }
      }

      it ("Let's break that down a little.") {
        def code = {flag = true}
        val dontCommand = dont (code)

        dontCommand.unless (false)

        assert (flag === false)

        dontCommand.unless (true)

        assert (flag === true)
      }

      it ("Let's break it down a little less.") {
        def code = {flag = true}

        dont (code).unless (false)

        assert (flag === false)

        dont (code).unless (true)

        assert (flag === true)
      }

      it ("Aaand...back to the original, almost.") {
        dont {flag = true} unless false

        assert (flag === false)

        dont {flag = true} unless true

        assert (flag === true)
      }
    }

    describe ("Here's an inside-out while statement") {
      var counter = 0
      var flag = false

      dont {flag = true} until {counter = counter + 1; counter >= 5}

      it ("that executes its condition repeatedly until it's true, then its body once") {
        assert (flag === true)
        assert (counter === 5)
      }
    }
  }
}
