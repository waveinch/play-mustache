import renderers.MustacheRendererImpl
import org.scalatest.flatspec.{AsyncFlatSpec}
import org.scalatest.matchers.should.Matchers._


class CompnentSpec extends AsyncFlatSpec  {


  "A template with map" should "be rendered" in {
    val mustacheRenderer = new MustacheRendererImpl

    val page ="test {{test.obj}}"



    mustacheRenderer.render(page,Map("test" -> Map("obj" -> "ok"))).map{ result =>
      println(s"result:$result, template: $page" )
      result shouldBe "test ok"
    }


  }

  "A template with component" should "be rendered" in {
    val mustacheRenderer = new MustacheRendererImpl

    val page ="test {{> component }}"

    val component = "ok"

    mustacheRenderer.renderWithComponents(page,Map("component" -> component),null).map{ result =>
      println(s"result:$result, template: $page" )
      result shouldBe "test ok"
    }


  }

}
