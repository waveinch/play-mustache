import org.scalatest.FlatSpec
import org.scalatest.concurrent.ScalaFutures
import renderers.MustacheRendererImpl
import org.scalatest._
import Matchers._

import scala.concurrent.ExecutionContext.Implicits.global

class CompnentSpec extends FlatSpec with ScalaFutures {



  "A template with component" should "be rendered" in {
    val mustacheRenderer = new MustacheRendererImpl

    val page ="test {{> component }}"

    val component = "ok"

    whenReady(mustacheRenderer.renderWithComponents(page,Map("component" -> component),null)) { result =>
      println(s"result:$result, template: $page" )
      result shouldBe "test ok"
    }


  }

}
