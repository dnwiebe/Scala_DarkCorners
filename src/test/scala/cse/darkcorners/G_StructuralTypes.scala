package cse.darkcorners

import java.io.{ByteArrayInputStream, StringWriter}

import org.mockito.Mockito._
import org.scalatest.path

/**
  * Created by dnwiebe on 4/26/17.
  */
class G_StructuralTypes extends path.FunSpec {

  describe ("Structural types are Scala's answer to duck typing in dynamically-typed languages.") {
    def andThenClose[T] (closeable: {def close (): Unit}) (code: => T): T = {
      try {
        code
      }
      finally {
        closeable.close ()
      }
    }

    describe ("Given an InputStream") {
      val inputStream = spy (new ByteArrayInputStream("I was gratified to be able to answer promptly, and so I did.".getBytes ()))

      val string = andThenClose (inputStream) {
        val array = new Array[Byte] (100)
        val len = inputStream.read (array, 0, array.length)
        new String (array, 0, len)
      }

      it ("it can read out the data") {
        assert (string === "I was gratified to be able to answer promptly, and so I did.")
      }

      it ("it closes the stream") {
        verify (inputStream).close ()
      }
    }

    describe ("Given a Writer") {
      val writer = spy (new StringWriter ())

      andThenClose (writer) {
        writer.write ("I said I didn't know.")
      }

      it ("it writes the data") {
        assert (writer.toString === "I said I didn't know.")
      }

      it ("it closes the Writer") {
        verify (writer).close ()
      }
    }

    it ("But something without a close () won't compile.") {
      assertDoesNotCompile("""andThenClose ("Strings have no close () method") {}""")
    }
  }
}
