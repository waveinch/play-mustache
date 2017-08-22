package renderers

import play.api.libs.json.JsValue

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by mattia on 26/05/16.
  */
trait MustacheRenderer {

  /**
    * Use this method for templates in resources
    *
    * @param template file located in resources to be rendered
    * @param scope    variables to be inserted in the template
    * @return rendered html template
    */
  def renderFile(template: String, scope: Object)(implicit ex:ExecutionContext): Future[String]

  /**
    * Use this method for templates in memory (ex. coming from DB)
    *
    * @param template the whole template in mustache
    * @param scope    variables to be inserted in the template
    * @return rendered html template
    */
  def render(template: String, scope: Object)(implicit ex:ExecutionContext): Future[String]

  /**
    * Use this method for templates in memory (ex. coming from DB)
    *
    * @param template the whole template in mustache
    * @param scope    variables to be inserted in the template
    * @return rendered html template
    */
  def renderWithComponents(template: String, components:Map[String,String],scope: Object)(implicit ex:ExecutionContext): Future[String]

  def renderJson(template: String, scope: JsValue)(implicit ex: ExecutionContext): Future[String]
  def renderJsonWithComponents(template: String, components: Map[String, String], scope: JsValue)(implicit ex: ExecutionContext): Future[String]
  def renderJsonWithFile(template: String, scope: JsValue)(implicit ex: ExecutionContext): Future[String]

}
