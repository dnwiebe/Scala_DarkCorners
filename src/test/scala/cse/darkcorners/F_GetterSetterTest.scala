package cse.darkcorners

import org.scalatest.path

import scala.beans.BeanProperty
import scala.collection.mutable.ListBuffer

/**
  * Created by dnwiebe on 4/26/17.
  */
class F_GetterSetterTest extends path.FunSpec {

  describe ("If you need to write Scala code that interops with Java, you might need getters and setters.") {
    class Person {
      var firstName: String = _
      var lastName: String = _
      var birthYear: Int = _

      def getFirstName: String = firstName
      def setFirstName (firstName: String): Unit = {this.firstName = firstName}
      def getLastName: String = lastName
      def setLastName (lastName: String): Unit = {this.lastName = lastName}
      def getBirthYear: Int = birthYear
      def setBirthYear (birthYear: Int): Unit = {this.birthYear = birthYear}
    }

    describe ("Then you can pretend it's a JavaBean") {
      val person = new Person

      it ("because it works like one") {
        assert (person.getFirstName === null)
        assert (person.getLastName === null)
        assert (person.getBirthYear === 0)

        person.setFirstName ("Throckmorton")
        person.setLastName ("Willoughby")
        person.setBirthYear (1966)

        assert (person.getFirstName === "Throckmorton")
        assert (person.getLastName === "Willoughby")
        assert (person.getBirthYear === 1966)
      }
    }
  }

  describe ("But there's an easier way.") {
    class Person {
      @BeanProperty var firstName: String = _
      @BeanProperty var lastName: String = _
      @BeanProperty var birthYear: Int = _
    }

    describe ("Then you can pretend it's a JavaBean") {
      val person = new Person

      it ("because it works like one") {
        assert (person.getFirstName === null)
        assert (person.getLastName === null)
        assert (person.getBirthYear === 0)

        person.setFirstName ("Throckmorton")
        person.setLastName ("Willoughby")
        person.setBirthYear (1966)

        assert (person.getFirstName === "Throckmorton")
        assert (person.getLastName === "Willoughby")
        assert (person.getBirthYear === 1966)
      }
    }
  }

  describe ("And while we're at it, what about native Scala getters and setters?") {

    class Coordinates (val windowX: Int, val windowY: Int) {
      private var viewportOffsetX = 0
      private var viewportOffsetY = 0

      def voX_= (x: Int): Unit = {viewportOffsetX = x}
      def voX: Int = viewportOffsetX

      def voY_= (y: Int): Unit = {viewportOffsetY = y}
      def voY: Int = viewportOffsetY

      def vX: Int = windowX - viewportOffsetX
      def vY: Int = windowY - viewportOffsetY
    }

    describe ("If we make a Coordinates in window space") {
      val subject = new Coordinates (651, 812)

      describe ("and then move the viewport down and left") {
        subject.voX = 51
        subject.voY = 12

        it ("the viewport coordinates of that window point will have changed") {
          assert (subject.vX === 600)
          assert (subject.vY === 800)
        }

        describe ("we can adjust it as well with += and -=") {
          subject.voX += 9
          subject.voY -= 2

          it ("and use the getters to check the adjustments") {
            assert (subject.voX === 60)
            assert (subject.voY === 10)
          }
        }
      }
    }
  }

  describe ("And just before we go, a little something about apply/update.") {
    class StarMap {
      private val stars = ListBuffer[(Double, Double, Double, String)] ()

      // find star nearest to coordinates
      def apply (x: Double, y: Double, z: Double): String = {
        stars.foldLeft (("None", Double.MaxValue)) {(soFar, elem) =>
          val (_, minSoFar) = soFar
          val (ex, ey, ez, name) = elem
          val distance = pythagoras3d ((ex, ey, ez), (x, y, z))
          distance - minSoFar match {
            case d if d < 0 => (name, distance)
            case _ => soFar
          }
        }._1
      }

      // find coordinates of named star
      def apply (name: String): Option[(Double, Double, Double)] = {
        stars.find {_._4 == name} match {
          case None => None
          case Some ((x, y, z, _)) => Some ((x, y, z))
        }
      }

      // add star
      def update (x: Double, y: Double, z: Double, name: String): Unit = {
        stars += ((x, y, z, name))
      }

      private def pythagoras3d (a: (Double, Double, Double), b: (Double, Double, Double)): Double = {
        val dx = a._1 - b._1
        val dy = a._2 - b._2
        val dz = a._3 - b._3
        (dx * dx) + (dy * dy) + (dz * dz)
      }
    }

    describe ("Given an empty StarMap") {
      val subject = new StarMap ()

      describe ("that is then filled with a few stars") {
        subject (134.2, -172.3, 14.3) = "Spica"
        subject (-7.3, 4.8, 2.0) = "Epsilon Eridani"
        subject (0.4, -3.1, -65.3) = "Aldebaran"

        describe ("and asked which star is closest to a far-south point") {
          val result = subject (0.0, 0.0, -100.0)

          it ("says Aldebaran") {
            assert (result === "Aldebaran")
          }
        }

        describe ("and asked for the coordinates of Betelgeuse") {
          val result = subject ("Betelgeuse")

          it ("doesn't know") {
            assert (result === None)
          }
        }
      }
    }
  }
}
