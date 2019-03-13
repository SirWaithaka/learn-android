package com.example.downloader

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
   private val TAG = "DMainActivity"
//   private val downloadData by lazy { DownloadData(this, xmlListView) }
   private var downloadData: DownloadData? = null

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)

      downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
   }

   private fun downloadUrl(url: String) {
      Log.d(TAG, "onCreate: called")
      downloadData = DownloadData(this, xmlListView)
      downloadData?.execute(url)
      Log.d(TAG, "onCreate: done")
   }
   override fun onCreateOptionsMenu(menu: Menu?): Boolean {
      menuInflater.inflate(R.menu.feeds_menu, menu)
      return true
   }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {

      val feedUrl: String = when (item.itemId) {
         R.id.menuFree -> "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"
         R.id.menuPaid -> "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=10/xml"
         R.id.menuSongs ->"http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml"
         else -> "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"
      }

      downloadUrl(feedUrl)
      return true
   }

   override fun onDestroy() {
      super.onDestroy()
      downloadData?.cancel(true)
   }

   companion object {
      private class DownloadData(context: Context, listView: ListView) : AsyncTask<String, Void, String>() {
         private val TAG = "DownloadData"

         var propContext: Context by Delegates.notNull()
         var propListView: ListView by Delegates.notNull()

         init {
            propContext = context
            propListView = listView
         }

         override fun doInBackground(vararg params: String?): String {
            Log.d(TAG, "doInBackground: starts with ${params[0]}")

            val rssFeed: String = downloadXML(params[0])
            if (rssFeed.isEmpty()) {
               Log.e(TAG, "doInBackground: Error downloading")
            }
            return rssFeed
         }

         override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parseApplications = ParseApplications()
            parseApplications.parse(result)

//            var arrayAdapter = ArrayAdapter<FeedEntry>(propContext, R.layout.list_item, parseApplications.applications)
//            propListView.adapter = arrayAdapter

            val feedAdapter = FeedAdapter(propContext, R.layout.list_record, parseApplications.applications)
            propListView.adapter = feedAdapter
         }

         private fun downloadXML(urlPath: String?): String {
            val xmlResult = StringBuilder()

            try {
               val url = URL(urlPath)
               val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
               val response = connection.responseCode

               Log.d(TAG, "downloadXML: response code was $response")

               /*
               val inputStream = connection.inputStream
               val inputStreamReader = InputStreamReader(inputStream)
               val reader = BufferedReader(inputStreamReader)
               *
               * Can be reduced to the following line
               */
//               val reader = BufferedReader(InputStreamReader(connection.inputStream))
//
//               val inputBuffer = CharArray(500)
//               var charsRead = 0
//               while (charsRead >= 0) {
//                  charsRead = reader.read(inputBuffer)
//                  if (charsRead > 0) {
//                     xmlResult.append(String(inputBuffer, 0, charsRead))
//                  }
//               }
//               reader.close()

               /*
               * Kotlin idiomatic way to read buffer stream
               *
               */
               val stream = connection.inputStream
               stream.buffered().reader().use { reader ->
                  xmlResult.append(reader.readText())
               }

               Log.d(TAG, "downloadXML: Received ${xmlResult.length} bytes")
               return xmlResult.toString()
//            } catch(e: MalformedURLException) {
//               Log.e(TAG, "downloadXML: Invalid URL: ${e.message}")
//            } catch(e: IOException) {
//               Log.e(TAG, "downloadXML: IO exception reading data: ${e.message}")
//            } catch (e: SecurityException) {
//               Log.e(TAG, "downloadXML: Security exception needs permissions. ${e.message}")
//            } catch(e: Exception) {
//               Log.e(TAG, "downloadXML: Unknown error: ${e.message}")
//            }
            } catch(e: Exception) {
               when(e) {
                  is MalformedURLException -> "downloadXML: Invalid URL: ${e.message}"
                  is IOException -> "downloadXML: IO exception reading data: ${e.message}"
                  is SecurityException -> {
                     e.printStackTrace()
                     "downloadXML: Security exception needs permissions. ${e.message}"
                  }
                  else -> "downloadXML: Unknown error: ${e.message}"
               }
            }
            return ""
         }
      }
   }
}
