package app.control

import org.scaloid.common._
import android.os.Bundle
import java.io.File

class CheckRoot extends SActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    contentView = new SFrameLayout {
      this += new SVerticalLayout {
        STextView("Enter binary name")
        val binaryName = SEditText("su")
        SButton("Enter binary name", toast(if (findBinary(binaryName.text.toString)) {
          "File exists"
        } else {
          "File doesn't exist"
        }))
      }
    }
  }

  def findBinary(name: String) = {
    val possibleLocations = List("/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/",
      "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/")
    val fileExists = if (possibleLocations.exists(loc =>
      new File(loc + "/" + name).exists()
    )) {
      true
    }
    else {
      false
    }
    fileExists
  }
}
