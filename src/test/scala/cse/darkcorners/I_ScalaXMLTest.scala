package cse.darkcorners

import org.scalatest.path

import scala.xml.NodeSeq

/**
  * Created by dnwiebe on 4/26/17.
  */
class I_ScalaXMLTest extends path.FunSpec {

  describe ("If you include scala-xml_2.12 in your build") {
    describe ("then XML becomes part of the syntax of Scala.") {
      val document =
        <parent>
          <child ordinal="1" hair="blonde">Brenda</child>
          <child ordinal="2" hair="brunette">Taryn</child>
        </parent>

      it ("You can .toString it, even if the result is a little wonky") {
        assert (document.toString ===
                  """<parent>
            |          <child ordinal="1" hair="blonde">Brenda</child>
            |          <child ordinal="2" hair="brunette">Taryn</child>
            |        </parent>""".stripMargin
        )
      }

      it ("You can use a jackass form of XPath to query it") {
        val children = document \ "child"
        assert (children.toList === List (
          <child hair="blonde" ordinal="1">Brenda</child>,
          <child hair="brunette" ordinal="2">Taryn</child>
        ))
      }

      it ("You can isolate attributes") {
        val children = document \ "child"
        assert (children (1) \@ "hair" === "brunette")
        assert (children (1) \@ "freckles" === "")
      }

      it ("You can add nodes") {
        val children = document \ "child"
        val moreChildren = children :+ <child ordinal="3" hair="redhead">Betty</child>

        assert (moreChildren.toList === List (
          <child hair="blonde" ordinal="1">Brenda</child>,
          <child hair="brunette" ordinal="2">Taryn</child>,
          <child hair="redhead" ordinal="3">Betty</child>
        ))
      }

      it ("You can use all the standard collection methods and dynamically create nodes") {
        val modifiedChildren = (document \ "child").map {child =>
          <child ordinal={child \@ "ordinal"} hair={child \@ "hair"} sex="female">{child text}</child>
        }

        assert (modifiedChildren.toList === List (
          <child hair="blonde" ordinal="1" sex="female">Brenda</child>,
          <child hair="brunette" ordinal="2" sex="female">Taryn</child>
        ))
      }
    }
  }
}
