package app.control

import org.scaloid.common._
import android.app.ExpandableListActivity
import android.os.Bundle
import android.widget.ExpandableListAdapter
import android.widget.SimpleExpandableListAdapter
import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map
import scala.collection.JavaConverters._
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException

class ApplicationsPermissions extends ExpandableListActivity with SContext {

  var mAdapter: ExpandableListAdapter = _
  val NAME = "NAME"
  val IS_EVEN = "IS_EVEN"

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    val runningProcesses = activityManager.getRunningAppProcesses.asScala.toList

    val processesNames = for (process <- runningProcesses) yield {
      process.processName
    }

    val perms = for (i <- 0 until processesNames.length) yield {
      getAppPermisions(processesNames(i))
    }

    val groupData = new ArrayList[Map[String, String]]()
    val childData = new ArrayList[List[Map[String, String]]]()

    for (i <- 0 until processesNames.length) {
      val curGroupMap = new HashMap[String, String]()
      groupData.add(curGroupMap)
      curGroupMap.put(NAME, processesNames(i))
      val children = new ArrayList[Map[String, String]]()


      for (j <- 0 until perms(i).length) {
        val curChildMap = new HashMap[String, String]()
        children.add(curChildMap)
        curChildMap.put(NAME, perms(i)(j))
        curChildMap.put(IS_EVEN, "Permission desc.")
      }
      childData.add(children)
    }
    mAdapter = new SimpleExpandableListAdapter(this, groupData, android.R.layout.simple_expandable_list_item_1,
      Array(NAME, IS_EVEN), Array(android.R.id.text1, android.R.id.text2), childData, android.R.layout.simple_expandable_list_item_2,
      Array(NAME, IS_EVEN), Array(android.R.id.text1, android.R.id.text2))
    setListAdapter(mAdapter)
  }

  def packageExists(name: String): Boolean = {
    val pm: PackageManager = getPackageManager
    try {
      val info = pm.getPackageInfo(name, PackageManager.GET_META_DATA)
    }
    catch {
      case ex: NameNotFoundException => false
    }
    true
  }

  def getAppPermisions(name: String): Array[String] = {
    var perms = Array("lol")
    val pm = getPackageManager
    try {
      perms = pm.getPackageInfo(name, PackageManager.GET_PERMISSIONS).requestedPermissions
      if (perms == null) {
        perms = Array("Null answer received")
      }
      perms
    }
    catch {
      case ex: NameNotFoundException =>
        perms = Array("No permissions requested")
        perms
    }

  }
}
