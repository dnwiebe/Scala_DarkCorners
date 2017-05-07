package cse.darkcorners

import org.scalatest.path

/**
  * Created by dnwiebe on 4/26/17.
  */
class M_Regex extends path.FunSpec {

  describe ("Regular expressions in Scala have unapply extractors.") {
    val regex = """(\d\d)/(\d\d)/(\d\d\d\d)""".r

    describe ("You can use the extractor to declare variables") {
      val regex(month, day, year) = "10/14/2006"

      it ("and find the values you expect in them") {
        assert (month === "10")
        assert (day === "14")
        assert (year === "2006")
      }
    }

    it ("If your string doesn't match the regex, you get a MatchError") {
      try {
        val regex (_, _, _) = "booga"
        fail ()
      }
      catch {
        case e: MatchError => assert (e.getMessage === "booga (of class java.lang.String)")
      }
    }

    describe ("You can also use it in match expressions") {
      def mmddyyyy2yyyymmdd (mmddyyyy: String): Option[String] = {
        mmddyyyy match {
          case regex (mm, dd, yyyy) => Some (s"$yyyy/$mm/$dd")
          case _ => None
        }
      }

      it ("where it handles matches") {
        assert (mmddyyyy2yyyymmdd ("10/14/2006") === Some ("2006/10/14"))
      }

      it ("where it handles mismatches") {
        assert (mmddyyyy2yyyymmdd ("booga") === None)
      }
    }
  }
}
