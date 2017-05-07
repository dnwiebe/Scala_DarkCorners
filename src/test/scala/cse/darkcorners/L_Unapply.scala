package cse.darkcorners

import org.scalatest.path

/**
  * Created by dnwiebe on 4/26/17.
  */
class L_Unapply extends path.FunSpec {
  describe ("You probably know that tuples in Scala have extractor functionality.") {

    describe ("Given a tuple") {
      val tuple = ("Ford", "Focus", 2013, true)

      it ("you can extract it with a match statement and get the values out") {
        tuple match {
          case (make, model, year, electric) => {
            assert (make === tuple._1)
            assert (model === tuple._2)
            assert (year === tuple._3)
            assert (electric === tuple._4)
          }
          case _ => fail ()
        }
      }
    }
  }

  describe ("You may know that case classes have it too.") {

    describe ("Given a case class instance") {
      case class Car (make: String, model: String, year: Int, electric: Boolean)
      val subject = Car ("Ford", "Focus", 2013, true)

      it ("you can extract it with a match statement and get the values out") {
        subject match {
          case Car (make, model, year, electric) => {
            assert (make === subject.make)
            assert (model === subject.model)
            assert (year === subject.year)
            assert (electric === subject.electric)
          }
          case _ => fail ()
        }
      }
    }
  }

  describe ("But did you know you can give your own classes this extractor capability?") {
    class Car (val make: String, val model: String, val year: Int, val electric: Boolean)

    describe ("Given a singleton with an unapply and a class instance") {
      object CarAll {
        def unapply (car: Car): Option[(String, String, Int, Boolean)] = {
          Some ((car.make, car.model, car.year, car.electric))
        }
      }
      val subject = new Car ("Ford", "Focus", 2013, true)

      it ("you can extract it with val and get the values out") {
        val CarAll (make, model, year, electric) = subject
        assert (make === subject.make)
        assert (model === subject.model)
        assert (year === subject.year)
        assert (electric === subject.electric)
      }

      it ("you can extract it with a match statement and get the values out") {
        subject match {
          case CarAll (make, model, year, electric) => {
            assert (make === subject.make)
            assert (model === subject.model)
            assert (year === subject.year)
            assert (electric === subject.electric)
          }
          case _ => fail ()
        }
      }
    }

    describe ("Given a singleton with a different unapply that works only for non-electric cars and a couple of instances") {
      object CarAllNonElectric {
        def unapply (car: Car): Option[(String, String, Int)] = {
          if (car.electric) {
            None
          }
          else {
            Some ((car.make, car.model, car.year))
          }
        }
      }
      val gas = new Car ("Ford", "Focus", 2013, false)
      val electric = new Car ("Toyota", "Prius", 2015, true)

      it ("you can extract the non-electric with val and get out the values") {
        val CarAllNonElectric (make, model, year) = gas
        assert (make === gas.make)
        assert (model === gas.model)
        assert (year === gas.year)
      }

      it ("you can extract the non-electric with a match statement and get out the values") {
        gas match {
          case CarAllNonElectric (make, model, year) => {
            assert (make === gas.make)
            assert (model === gas.model)
            assert (year === gas.year)
          }
          case _ => fail ()
        }
      }

      it ("you cannot extract the electric with a val") {
        try {
          val CarAllNonElectric (_, _, _) = electric
        }
        catch {
          case e: MatchError => // success
        }
      }

      it ("you cannot extract the electric with a match statement") {
        electric match {
          case CarAllNonElectric (_, _, _) => fail ()
          case _ => // succeed
        }
      }
    }

    describe ("Unapply doesn't always have to return an Option[TupleNN]") {
      object CarIsElectric {
        def unapply (car: Car): Boolean = car.electric
      }
      val gas = new Car ("Ford", "Focus", 2013, false)
      val electric = new Car ("Toyota", "Prius", 2015, true)

      it ("the gas car doesn't match") {
        gas match {
          case CarIsElectric () => fail ()
          case _ => // succeed
        }
      }

      it ("the electric car does match") {
        electric match {
          case CarIsElectric () => // succeed
          case _ => fail ()
        }
      }
    }

    describe ("If it returns only one value, it's just an Option[T], not an Option[TupleNN[T, ...]]") {
      object CarMake {
        def unapply (car: Car): Option[String] = if (car.electric) Some (car.make) else None
      }
      val gas = new Car ("Ford", "Focus", 2013, false)
      val electric = new Car ("Toyota", "Prius", 2015, true)

      it ("the gas car doesn't match in a val") {
        try {
          val CarMake (_) = gas
        }
        catch {
          case e: MatchError => // success
        }
      }

      it ("the gas car doesn't match in a match") {
        gas match {
          case CarMake (_: String) => fail ()
          case _ => // succeed
        }
      }

      it ("the electric car does match in a val") {
        val CarMake (make) = electric
        assert (make === electric.make)
      }

      it ("the electric car does match in a match") {
        electric match {
          case CarMake (make: String) => assert (make === electric.make)
          case _ => fail ()
        }
      }
    }

    describe ("unapplySeq can handle extractions that result in an unpredictable number of pieces.") {
      object FullName {
        def unapplySeq (fullName: String): Option[List[String]] = {
          if (fullName == null) {return None}
          fullName.split (" ").toList match {
            case Nil => None
            case names => Some (names)
          }
        }
      }

      def analyze (fullName: String): String = {
        fullName match {
          case FullName (last) => s"Single name: $last"
          case FullName (first, last) => s"First and last names: $first $last"
          case FullName (first, middle, last) => s"First, middle, and last names: $first $middle $last"
          case _ => s"Unrecognized name format: $fullName"
        }
      }

      it ("Null name is not recognized") {
        assert (analyze (null) === "Unrecognized name format: null")
      }

      it ("Single name is recognized") {
        assert (analyze ("Cher") === "Single name: Cher")
      }

      it ("Double name is recognized") {
        assert (analyze ("Cyndi Lauper") === "First and last names: Cyndi Lauper")
      }

      it ("First, middle, and last names are recognized") {
        assert (analyze ("Kirsten Joy Weiss") === "First, middle, and last names: Kirsten Joy Weiss")
      }

      it ("More names are not") {
        assert (analyze ("Esteban Julio Ricardo Montoya de la Rosa Ramirez") ===
          "Unrecognized name format: Esteban Julio Ricardo Montoya de la Rosa Ramirez")
      }
    }
  }
}
