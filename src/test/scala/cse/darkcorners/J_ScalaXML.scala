package cse.darkcorners

import org.scalatest.path

import scala.xml.NodeSeq

/**
  * Created by dnwiebe on 4/26/17.
  */
class J_ScalaXML extends path.FunSpec {

  describe ("If you include scala-xml_2.12 in your build") {
    describe ("then XML becomes part of the syntax of Scala.") {
      val document =
        <parent>
          <child age="7" hair="blonde">Sara</child>
          <child age="5" hair="brunette">Amanda</child>
        </parent>

      it ("You can .toString it, even if the formatting is a little wonky") {
        assert (document.toString ===
                  """<parent>
            |          <child age="7" hair="blonde">Sara</child>
            |          <child age="5" hair="brunette">Amanda</child>
            |        </parent>""".stripMargin
        )
      }

      it ("You can use a jackass form of XPath to query it") {
        val children = document \ "child"
        assert (children.toList === List (
          <child hair="blonde" age="7">Sara</child>,
          <child hair="brunette" age="5">Amanda</child>
        ))
      }

      it ("You can isolate attributes") {
        val children = document \ "child"
        assert (children (1) \@ "hair" === "brunette")
        assert (children (1) \@ "freckles" === "")
      }

      it ("You can get node text") {
        val children = document \ "child"
        assert (children (1).text === "Amanda")
      }

      it ("You can add nodes") {
        val children = document \ "child"
        val moreChildren = children :+ <child age="3" hair="redhead">Betty</child>

        assert (moreChildren.toList === List (
          <child hair="blonde" age="7">Sara</child>,
          <child hair="brunette" age="5">Amanda</child>,
          <child hair="redhead" age="3">Betty</child>
        ))
      }

      it ("You can use all the standard collection methods and dynamically create nodes") {
        val modifiedChildren = (document \ "child").map {child =>
          <child age={child \@ "age"} hair={child \@ "hair"} sex="female">{child text}</child>
        }

        assert (modifiedChildren.toList === List (
          <child hair="blonde" age="7" sex="female">Sara</child>,
          <child hair="brunette" age="5" sex="female">Amanda</child>
        ))
      }
    }
  }
}
