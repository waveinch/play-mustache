package renderers.utils

import java.io.{Reader, StringReader}

import com.github.mustachejava.MustacheResolver

class InMemoryMustacheResolver(components:Map[String,String]) extends MustacheResolver {
  override def getReader(resourceName: String): Reader = {
    new StringReader(components(resourceName.replaceAll(".mustache","")))
  }
}
