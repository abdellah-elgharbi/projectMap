package abdellah.project.myapplication;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Animation extends AppCompatActivity {


    private ProgressBar progressBar;
    public void first_thread(){
        Thread thread= new Thread(){
            @Override
            public void run(){
                try {
                    sleep(4000);
                    Intent intent=new Intent(Animation.this,MainActivity.class);
                    startActivity(intent);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        thread.start();
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        first_thread();
        progressBar=findViewById(R.id.progressBar);
        ObjectAnimator progressAnimator=ObjectAnimator.ofInt(progressBar,"progress",0,100);
        progressAnimator.setDuration(4000);
        progressAnimator.start();

    }
}