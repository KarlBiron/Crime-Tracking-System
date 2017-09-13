package ae.ac.ud.ceit.cts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import ae.ac.ud.ceit.cts.login.LoginActivity;

/**
 * Created by atalla on 24/02/17.
 */

public class SplashScreen extends Activity {    //(extends=inherit)Activity[Super-class] is part of Android support library

    /**
     * The thread to process splash screen events
     */

    private Thread mSplashThread;               //A thread is a thread of execution in a program

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_layout); // Splash screen view (splash_layout is a PNG image)

        //"this" is a reference to the current object
        // the object whose method or constructor is being called
        final SplashScreen sPlashScreen = this;

        // The thread to wait for splash screen events
        mSplashThread =  new Thread(){
            @Override
            public void run(){      // configure the thread (mSplashThread)
                try {
                    //synchronized(this): used to keep variables or methods thread-safe
                    //If you wrap a variable in a synchronized block like so: synchronized(this)
                    //Then any attempts to modify the value of (this) from another thread while the logic inside the synchronized block is running
                    // will wait until the block has finished execution.
                    // It ensures that the value going into the block will be the same through the lifecycle of that block.
                    synchronized(this){
                        // Wait given period of time or exit on touch
                        wait(5000);
                    }
                }
                catch(InterruptedException ex){
                }

                finish();

                // Run next activity
                Intent intent = new Intent();                           //setup new Intent
                intent.setClass(sPlashScreen, LoginActivity.class);     //Set LoginActivity as next page
                startActivity(intent);                                  //Start LoginActivity
               // stop();
            }
        };

        mSplashThread.start();      //starts the actual splash activity thread
    }

    /**
     * Processes splash screen touch events
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt)            //Setting a trigger for the Thread
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)      //[ACTION_DOWN] = A pressed gesture has finished
        {
            //synchronized(mSplashThread): used to keep variables or methods thread-safe
            //If you wrap a variable in a synchronized block like so: synchronized(mSplashThread)
            //Then any attempts to modify the value of (mSplashActivity) from another thread while the logic inside the synchronized block is running
            // will wait until the block has finished execution.
            // It ensures that the value going into the block will be the same through the lifecycle of that block.
            synchronized(mSplashThread){
                //notifyAll() makes the main thread finish pausing the app
                //notifyAll() = Returns a string containing a concise, human-readable description of this object(mSplashThread)
                mSplashThread.notifyAll();
            }
        }
        return true;
    }
}
