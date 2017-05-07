package cse.darkcorners

import java.io.File

import org.scalatest.path

/**
  * Created by dnwiebe on 4/26/17.
  */
class F_EarlyInitialization extends path.FunSpec {

  describe ("Given a trait that requires quick initialization") {
    trait ToStringCacher[T] {
      val item: T
      val cachedString: String = item.toString
    }

    describe ("if we just try to extend it") {
      class Extension extends ToStringCacher[File] {
        override val item = new File ("/usr/local/dict")
      }

      it ("we'll get an exception on instantiation") {
        try {
          new Extension ()
          fail ()
        }
        catch {
          case _: NullPointerException => // Tried to calculate cachedString before item had a value
        }
      }
    }

    describe ("we could put the early initialization in the constructor") {
      class Extension (override val item: File) extends ToStringCacher[File]

      it ("which would work fine") {
        val extension = new Extension (new File ("/usr/local/dict"))

        assert (extension.cachedString === "/usr/local/dict")
      }
    }

    describe ("we could even default the constructor parameter") {
      class Extension (override val item: File = new File ("/usr/local/dict")) extends ToStringCacher[File]

      it ("which would also work fine") {
        val extension = new Extension ()

        assert (extension.cachedString === "/usr/local/dict")
      }
    }

    describe ("but both of those require a new named class. Let's use explicit early optimization instead.") {
      // The block below can only only only contain initializations.
      val extension = new {val item = new File ("/usr/local/dict")} with ToStringCacher[File]

      it ("Now we have an anonymous concrete class that works as expected.") {
        assert (extension.cachedString === "/usr/local/dict")
      }
    }
  }
}
