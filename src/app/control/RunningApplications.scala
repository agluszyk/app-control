package app.control

import java.io.{FileReader, BufferedReader, File}

import android.accounts.{Account, AccountManager}
import android.app.ListActivity
import android.os.{Build, Bundle}
import android.view._
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicHeader
import org.apache.http.params.{HttpConnectionParams, BasicHttpParams}
import org.apache.http.protocol.HTTP
import org.apache.http.util.EntityUtils
import org.scaloid.common._
import scala.collection.JavaConverters._

class RunningApplications extends ListActivity with SContext{

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val runningProcesses = activityManager.getRunningAppProcesses.asScala.toList
    val processesNames = for (process <- runningProcesses) yield {
      process.processName
    }
    setListAdapter(SArrayAdapter(android.R.layout.simple_list_item_1, processesNames.toArray))

    }

  override def onCreateOptionsMenu(menu: android.view.Menu): Boolean = {

    val item = menu.add("Upload")
    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {


    val userString = """{"name": "Guillaume"}"""

    val runningProcesses = activityManager.getRunningAppProcesses.asScala.toList
    val processesNames = for (process <- runningProcesses) yield {
      process.processName
    }


    val processedString = "{\"processess\":[\"" + processesNames.mkString("\",\"") + "\"]" + "," +
    "\"model\": " + "\"" + Build.MODEL + "\"" +
      "}"
    //toast(processedString)


    val file = new File(getApplicationContext.getFilesDir, "user_details.txt")

    if(!file.exists()){
      toast("Register first")
      return false
    }
    val user_details = new BufferedReader(new FileReader(file)).readLine().split(",")

    val json = "{" +
    "\"owner\": " + "\"" + user_details(0) + "\"" + "," +
    "\"model\": " + "\"" + Build.MODEL + "\"" + "," +
    "\"manufacturer\": " + "\"" + Build.MANUFACTURER + "\"" + "," +
    "\"osVersion\": " + "\"" + Build.VERSION.RELEASE + "\"" + "," +
    "\"build\": " + "\"" + Build.FINGERPRINT + "\"" + "," +
    "\"processess\":" + "[\"" + processesNames.mkString("\",\"") + "\"]" +
    "}"

    //toast(json)


    val TIMEOUT_MILLISEC = 10000;  // = 10 seconds
    val httpParams = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
    HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
    val client = new DefaultHttpClient(httpParams);

    val request = new HttpPost("http://192.168.0.105:9000/addService");
    val se = new StringEntity(json)
    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"))
    request.setEntity(se)

    val response = client.execute(request);


    toast(EntityUtils.toString(response.getEntity))
    true
  }

}
