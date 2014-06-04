package app.control

import android.app.ListActivity
import android.os.Bundle
import org.scaloid.common._
import scala.collection.JavaConverters._

class RunningApplications extends ListActivity with SContext {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val runningProcesses = activityManager.getRunningAppProcesses.asScala.toList
    val processesNames = for (process <- runningProcesses) yield {
      process.processName
    }
    setListAdapter(SArrayAdapter(android.R.layout.simple_list_item_1, processesNames.toArray))
  }

}
