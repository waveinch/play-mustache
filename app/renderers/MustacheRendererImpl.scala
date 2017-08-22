package renderers

import java.io.{StringReader, StringWriter}
import javax.inject.Singleton

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mustachejava.DefaultMustacheFactory
import play.api.libs.json.{JsValue, Json}
import renderers.utils.{InMemoryMustacheResolver, ScalaObjectHandler}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

/**
  * Created by mattia on 26/05/16.
  */
@Singleton
class MustacheRendererImpl extends MustacheRenderer {
  override def render(template: String, scope: Object)(implicit ex:ExecutionContext): Future[String] = {
    renderWithComponents(template, Map(), scope)
  }

  override def renderFile(template: String, scope: Object)(implicit ex:ExecutionContext): Future[String] = Future{
    val file = this.getClass.getClassLoader.getResource(template).getPath
    val mf = new DefaultMustacheFactory()
    mf.setObjectHandler(new ScalaObjectHandler())
    val mustache = mf.compile(file)
    val writer = new StringWriter()

    mustache.execute(writer, scope).flush()
    val result = writer.toString
    writer.close()

    result

  }

  override def renderJson(template: String, scope: JsValue)(implicit ex: ExecutionContext): Future[String] = render(template,jsValue2Object(scope))
  override def renderJsonWithComponents(template: String, components: Map[String, String], scope: JsValue)(implicit ex: ExecutionContext): Future[String] = renderWithComponents(template,components,jsValue2Object(scope))
  override def renderJsonWithFile(template: String, scope: JsValue)(implicit ex: ExecutionContext): Future[String] = renderFile(template,jsValue2Object(scope))


  override def renderWithComponents(template: String, components: Map[String, String], scope: Object)(implicit ex: ExecutionContext): Future[String] = Future{
    val reader = new StringReader(template)
    val mf = new DefaultMustacheFactory(new InMemoryMustacheResolver(components))
    mf.setObjectHandler(new ScalaObjectHandler())

    val mustache = mf.compile(reader, "mustache-tmpl-" + Random.nextInt() + ".mustache")

    val writer = new StringWriter()

    mustache.execute(writer, scope).flush()
    val result = writer.toString
    writer.close()

    result
  }


  private def toMustacheParsableObj(data: String): java.util.Map[String, Object] = {
    val mapper = new ObjectMapper()
    mapper.readValue(data, classOf[java.util.Map[String, Object]])
  }

  def jsValue2Object(json:JsValue):Object = {
    toMustacheParsableObj(Json.stringify(json))
  }

}

