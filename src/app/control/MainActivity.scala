package app.control

import org.scaloid.common._
import android.os.Bundle

class MainActivity extends SActivity with SContext {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    contentView = new SFrameLayout {
      this += new SVerticalLayout {
        SButton("Running Applications").onClick(
          startActivity(SIntent[RunningApplications])
        )
        SButton("Running Processess").onClick(
          startActivity(SIntent[RunningProcessess])
        )
        SButton("Check root access").onClick(
          startActivity(SIntent[CheckRoot])
        )
        SButton("Applications permissions").onClick(
          startActivity(SIntent[ApplicationsPermissions])
        )

      }.<<.fill.>>
    }
  }

}
