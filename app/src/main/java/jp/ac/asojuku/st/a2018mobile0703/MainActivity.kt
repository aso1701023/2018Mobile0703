package jp.ac.asojuku.st.a2018mobile0703

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Color.MAGENTA
import android.graphics.Paint
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.Button
import jp.ac.asojuku.st.a2018mobile0703.R.attr.height
import jp.ac.asojuku.st.a2018mobile0703.R.id.text
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),SensorEventListener,SurfaceHolder.Callback {
    override fun surfaceCreated(holder: SurfaceHolder?) {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(
                this,accSensor,
                        SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        surfaceWidth = width
        surfaceHeight = height
        ballX = (width/2).toFloat()
        ballY = (height/2).toFloat()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager;
        sensorManager.unregisterListener(this)
    }

    private var surfaceWidth:Int =0;
    private var surfaceHeight:Int = 0;
    private val radius = 30.0f;
    private var coef = 1000.0f;
    private val Goal1 = 100;
    private val Goal2 = 100;
    private val Goal3 = 200;
    private val Goal4 = 300;
    private val BLOCK1 = 800;
    private val BLOCK2 = 700;
    private val BLOCK3 = 900;
    private val BLOCK4 = 300;
    private val BLOCK5 = 600;
    private val BLOCK6 = 500;
    private val BLOCK7 = 200;
    private val BLOCK8 = 200;

    private var ballX:Float = 0f;
    private var ballY:Float = 0f;
    private var vx:Float = 0f;
    private var  vy:Float = 0f;
    private var time:Long = 0L;
    private var Flg = 0;

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    override fun onResume(){
        super.onResume()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager;
        val accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(
                this,accSensor,
                SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null) return
        if(time==0L)time = System.currentTimeMillis()
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            val x = -event.values[0]
            val y = event.values[1]

            var t = (System.currentTimeMillis() - time).toFloat();
            time = System.currentTimeMillis()
            t /= 2000.0f

            val dx = vx*t+x*t*t/2.0f
            val dy = vy*t+y*t*t/2.0f
            ballX += dx * coef
            ballY += dy * coef
            vx += x * t
            vy += y * t
            if(ballX - radius<0 && vx<0){
                vx = -vx/1.5f
                ballX = radius
            }else if(ballX + radius > surfaceWidth && vx > 0){
                vx = -vx/1.5f
                ballX = surfaceWidth - radius
            }
            if(ballY - radius<0 && vy <0){
                vy = -vy/1.5f
                ballY = radius
            }else if(ballY + radius > surfaceHeight && vy>0) {
                vy = -vy / 1.5f
                ballY = surfaceHeight - radius
            }
            if(Flg == 0 && ballY +radius >= 100 && ballX +radius >= 100 && ballY - radius <= 300 && ballX - radius <= 200){
                            resultText.setText(R.string.clear)
                            imageView.setImageResource(R.drawable.saccessed)
                            vx = 0f;
                            vy = 0f;
                            coef = 0f;
                            Flg = 1;
                }
            if(Flg == 0 && ballY -radius <= 700 && ballX +radius >= 800 && ballY + radius >= 300 && ballX - radius <= 900){
                            resultText.setText(R.string.failed)
                            imageView.setImageResource(R.drawable.failed)
                            vx = 0f;
                            vy = 0f;
                            coef = 0f;
                            Flg = 1;
            }
            if(Flg == 0 && ballY -radius <= 500 && ballX +radius >= 200 && ballY + radius >= 200 && ballX - radius <= 600){
                            resultText.setText(R.string.failed)
                            imageView.setImageResource(R.drawable.failed)
                            vx = 0f;
                            vy = 0f;
                            coef = 0f;
                            Flg = 1;
            }
            drawCanvas()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        ResetButton.setOnClickListener{
            ballX = (surfaceWidth/2).toFloat()
            ballY = (surfaceHeight/2).toFloat()
            imageView.setImageResource(R.drawable.th)
            resultText.setText("青いゴールにボールを入れろ")
            vx = 0f
            vy = 0f
            coef = 1000.0f
            Flg = 0;
        }
        val holder = surfaceView.holder
        holder.addCallback(this)
    }
    private fun drawCanvas(){
        val canvas = surfaceView.holder.lockCanvas()
        canvas.drawColor(Color.YELLOW)
        canvas.drawCircle(ballX,ballY,radius, Paint().apply{
            color = Color.MAGENTA
        })
        val GOAL = Rect(Goal1,Goal2,Goal3,Goal4);
        canvas.drawRect(GOAL,Paint().apply {
            color = Color.BLUE
        })
        val BLOCKa = Rect(BLOCK1,BLOCK2,BLOCK3,BLOCK4)
        canvas.drawRect(BLOCKa,Paint().apply {
            color = Color.BLACK
        })
        val BLOCKb = Rect(BLOCK5,BLOCK6,BLOCK7,BLOCK8)
        canvas.drawRect(BLOCKb,Paint().apply {
            color = Color.BLACK
        })
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }
}
