package app.control

import java.io._

import android.content.Context
import android.os.{Build, Bundle}
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicHeader
import org.apache.http.params.{HttpConnectionParams, BasicHttpParams}
import org.apache.http.protocol.HTTP
import org.apache.http.util.EntityUtils
import org.scaloid.common._
/**
 * Created by arek on 10/19/14.
 */
class Registration extends SActivity with SContext {

  def register(username: String, password: String): Unit = {

    val file = new File(getApplicationContext.getFilesDir, "user_details.txt")

    if(!file.exists()) {
      val os = new FileOutputStream(file)
      os.write((username + "," + password).getBytes)
      os.close()

      val json = "{" +
        "\"name\": " + "\"" + username + "\"" + "," +
        "\"password\": " + "\"" + password + "\"" +
        "}"

      val TIMEOUT_MILLISEC = 10000;  // = 10 seconds
      val httpParams = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
      HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
      val client = new DefaultHttpClient(httpParams);
      val request = new HttpPost("http://192.168.0.105:9000/register");
      val se = new StringEntity(json)
      se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"))
      request.setEntity(se)
      val response = client.execute(request);
      toast(EntityUtils.toString(response.getEntity))
    }
    else {
      toast("Already registered, removing")
      val file = new File(getApplicationContext.getFilesDir, "user_details.txt")
      file.delete()
    }
  }
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    contentView = new SFrameLayout {
      this += new SVerticalLayout {
        STextView("Username:")
        val username = SEditText()
        STextView("Password:")
        val password = SEditText() inputType TEXT_PASSWORD
        SButton("Register").onClick(
          register(username.getText.toString, password.getText.toString)
        )
      }.<<.fill.>>
    }
  }
}
