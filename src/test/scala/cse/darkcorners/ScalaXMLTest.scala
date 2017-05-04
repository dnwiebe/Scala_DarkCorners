package cse.darkcorners

import org.scalatest.path

/**
  * Created by dnwiebe on 4/26/17.
  */
class ScalaXMLTest extends path.FunSpec {

  describe ("xml") {
    val tag =
      <parent attribute="value">
        <child>
          text
        </child>
      </parent>

    it ("prints") {
      pending
      assert (tag.toString === "blah")
    }
  }
}
