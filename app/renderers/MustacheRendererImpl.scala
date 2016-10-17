package renderers

import java.io.{Writer, Reader, StringReader, StringWriter}
import java.lang.reflect.{Field, Method}
import java.util
import java.util.Map.Entry
import javax.inject.Singleton

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Iteration
import com.github.mustachejava.MustacheFactory
import com.github.mustachejava.reflect.ReflectionObjectHandler
import com.github.mustachejava.{MustacheFactory, Iteration, DefaultMustacheFactory}
import com.github.mustachejava.reflect.ReflectionObjectHandler

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag
import scala.runtime.BoxedUnit
import scala.util.{Try, Random}

/**
  * Created by mattia on 26/05/16.
  */
@Singleton
class MustacheRendererImpl extends MustacheRenderer {
  override def render(template: String, scope: Object)(implicit ex:ExecutionContext): Future[String] = {
    render(new StringReader(template), scope)
  }

  override def renderFile(template: String, scope: Object)(implicit ex:ExecutionContext): Future[String] = Future{
    val file = this.getClass.getClassLoader.getResource(template).getPath
    val mf = mustacheFactory()
    val mustache = mf.compile(file)
    val writer = new StringWriter()

    mustache.execute(writer, scope).flush()
    val result = writer.toString
    writer.close()

    result

  }

  protected def render(reader: Reader, scope: Object, tag: Option[String] = None)(implicit ex:ExecutionContext): Future[String] = Future{
    val mf = mustacheFactory()
    val mustache = mf.compile(reader, tag.getOrElse("mustache-tmpl-" + Random.nextInt() + ".mustache"))
    val writer = new StringWriter()

    mustache.execute(writer, scope).flush()
    val result = writer.toString
    writer.close()

    result
  }

  def mustacheFactory(): MustacheFactory = {
    val mf = new DefaultMustacheFactory()
    mf.setObjectHandler(new ScalaObjectHandler())
    mf
  }
}


class ScalaObjectHandler extends ReflectionObjectHandler {

  // Allow any method or field
  override def checkMethod(member: Method) {}

  override def checkField(member: Field) {}

  override def coerce(value: AnyRef) = {
    value match {
      case m: collection.Map[_, _] => mapAsJavaMap(m)
      case u: BoxedUnit => null
      case s: Seq[Any] => new ArrayMap(s)
      case Some(some: AnyRef) => coerce(some)
      case None => null
      case _ => value
    }
  }

  override def iterate(iteration: Iteration, writer: Writer, value: AnyRef, scopes: java.util.List[AnyRef]) = {
    value match {
      case TraversableAnyRef(t) => {
        var newWriter = writer
        t foreach {
          next =>
            newWriter = iteration.next(newWriter, coerce(next), scopes)
        }
        newWriter
      }
      case n: Number => if (n.intValue() == 0) writer else iteration.next(writer, coerce(value), scopes)
      case _ => super.iterate(iteration, writer, value, scopes)
    }
  }

  override def falsey(iteration: Iteration, writer: Writer, value: AnyRef, scopes: java.util.List[AnyRef]) = {
    value match {
      case TraversableAnyRef(t) => {
        if (t.isEmpty) {
          iteration.next(writer, value, scopes)
        } else {
          writer
        }
      }
      case n: Number => if (n.intValue() == 0) iteration.next(writer, coerce(value), scopes) else writer
      case _ => super.falsey(iteration, writer, value, scopes)
    }
  }

  val TraversableAnyRef = new Def[Traversable[AnyRef]]

  class Def[C: ClassTag] {
    def unapply[X: ClassTag](x: X): Option[C] = {
      x match {
        case c: C => Some(c)
        case _ => None
      }
    }
  }

}

class ArrayMap(val obj: Seq[Any]) extends java.util.AbstractMap[Any, Any] with java.lang.Iterable[Any] {

  override def isEmpty() = size == 0

  override def get(key: Any): Any = Try {
    val index = Integer.parseInt(key.toString)
    return obj(index)
  }.toOption.orNull

  override def containsKey(key: Any): Boolean = get(key) != null

  override def entrySet(): util.Set[Entry[Any, Any]] = throw new UnsupportedOperationException()

  override def iterator(): util.Iterator[Any] = new util.Iterator[Any] {
    var index = 0
    val length = obj.length

    override def hasNext: Boolean = index < length

    override def next(): Any = {
      val nextObj = obj.get(index)
      index += 1
      nextObj
    }
  }
}
