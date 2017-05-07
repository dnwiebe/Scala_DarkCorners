package cse.darkcorners

import org.scalatest.path

/**
  * Created by dnwiebe on 4/26/17.
  */
class C_MoreAboutCaseClasses extends path.FunSpec {

  case class Car (make: String, model: String, year: Int, electric: Boolean)

  describe ("Case classes come with a .copy() method.") {

    val original = Car ("Ford", "Focus", 2003, false)

    describe ("If you use it without any parameters") {
      val copy = original.copy ()

      it ("you get a direct copy") {
        assert (copy ne original)
        assert (copy === original)
      }
    }

    describe ("If you override the default on some of the parameters") {
      val modified = original.copy (year = 2013, electric = true)

      it ("you get a modified copy") {
        assert (modified.make === original.make)
        assert (modified.model === original.model)
        assert (modified.year === 2013)
        assert (modified.electric === true)
      }
    }
  }

  describe ("Case classes also extend the Product trait.") {
    val car = Car ("Toyota", "Tacoma", 1995, false)

    describe ("If you ask what the arity is") {
      val result = car.productArity

      it ("you get the number of fields in the class") {
        assert (result === 4)
      }
    }

    describe ("You can find field values non-reflectively") {
      val make = car.productElement (0)
      val year = car.productElement (2)

      it ("and their values are what you expect, although your variables are Anys") {
        assert (make === car.make)
        assert (year === car.year)
      }
    }

    describe ("You can iterate through field values") {
      val list = car.productIterator.toList

      it ("and treat them as a collection.") {
        assert (list === List ("Toyota", "Tacoma", 1995, false))
      }
    }

    describe ("You can ask for the class name") {
      val name = car.productPrefix

      it ("and get basically the same thing as getSimpleName on the class") {
        assert (name === car.getClass.getSimpleName)
      }
    }
  }
}
