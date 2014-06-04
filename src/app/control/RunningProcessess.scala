package app.control

import org.scaloid.common._
import scala.io.Source._
import android.os.Bundle
import android.app.ListActivity

class RunningProcessess extends ListActivity with SContext {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val proc = Runtime.getRuntime.exec("ps")
    val lines = fromInputStream(proc.getInputStream).getLines()
    val psNames = for (line <- lines) yield {
      val splitted = line.split(" ")
      splitted.last
    }
    setListAdapter(SArrayAdapter(android.R.layout.simple_list_item_1, psNames.toArray.drop(1)))
  }
}
