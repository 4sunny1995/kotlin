package com.example.nckh

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

val apiURL="http://localhost:8000/api/"
class MainActivity : AppCompatActivity() {
    private val client=OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnLogin.setOnClickListener {
            val  map:HashMap<String,String> = hashMapOf("email" to  txtUsername.text.toString(),"password" to txtPassword.text.toString())

            POST(apiURL+"user",map,object :Callback{

                override fun onResponse(call: Call?, response: Response) {
                    print("ok")
                    val responseData=response.body()?.string()
                    runOnUiThread {

                        try {
                            var json=JSONObject(responseData)
                            println("Request Successful!!")

                        }
                        catch (e:JSONException)
                        {
                            print('1')
                            e.printStackTrace()
                        }
                    }
                }
                override fun onFailure(call: Call?, e: IOException) {
                    println("Request faild")
                }
            })
        }
        btnExit.setOnClickListener {
            finish()
        }
    }
    fun goHome(){
        startActivity(Intent(this,MainActivity::class.java))
    }
    fun POST(url:String,parameters:HashMap<String,String>,callback: Callback):Call{
//        showDialog(url)
        val  builder = FormBody.Builder()
        val it=parameters.entries.iterator()
        while (it.hasNext()) {
            val pair = it.next() as Map.Entry<*, *>
            builder.add(pair.key.toString(), pair.value.toString())
        }
        val formBody=builder.build()
//        println(formBody.toString())

        val request=Request.Builder().url(url).post(formBody).build()

        val call=client.newCall(request)
//        showDialog(request.toString())
        call.enqueue(callback)
        return call
    }
    public fun showDialog(data : String){
        val builder=android.app.AlertDialog.Builder(this)
        builder.setTitle("Thông báo")
        builder.setMessage(data)
        builder.setPositiveButton("OK",{dialog: DialogInterface?, which: Int ->goHome()  }).show()
    }
}
