package com.example.game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Locale;
import java.util.Random;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    public MainThread thread;

    private Random random;
    private long startTime;

    private float purpleshortX, purpleshortY, purpletallX, purpletallY, chaserX, chaserY, playerX, playerY, short_height, tall_height, short_width, tall_width;
    private float shortSpeedY, tallSpeedY, chaserSpeedX, playerSpeedX;
    private RectF ob_purpleshort, ob_purpletall;


    private boolean touch = false, gameOver = false, isStopwatchRunning, allowjump = true, fall = false, collide=false;

    private Sound sound;

    private int count = 0, score, totalcount=0;

    private SharedPreferences sharedPreferences;
    int width,height;




    public GameView(Context context) {
        super(context);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        getHolder().addCallback(this);
        random = new Random(System.currentTimeMillis());
        sharedPreferences= context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        sound = new Sound(this.getContext());
        startTime = System.currentTimeMillis();
        isStopwatchRunning = true;
        width = getWidth();
        height = getHeight();
        purpleshortX = 50;



        ob_purpleshort = new RectF(50, 600, 200, 700);
        ob_purpletall = new RectF(50, 1500, 400, 1600);

        purpleshortY = 600;
        purpletallX = 50;
        purpletallY = 1500;
        short_height = 250;
        tall_height = 350;
        short_width = 100;
        tall_width = 100;




        chaserX = 150;
        chaserY = 150;
        playerX = 110;
        playerY = 500;
        shortSpeedY = -0.8f;
        tallSpeedY = -0.8f;



    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

            thread.setRunning(true);
            thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }

    public void update() {

        endofscreen();

        shortSpeedY -= 0.0099f;
        tallSpeedY -= 0.0099f;


       if (chaserX >= 500) {
            chaserSpeedX = 0;
            fall = true;
        }

        else if (fall==true && chaserX>=150) {
            chaserSpeedX -= 0.1f;
            chaserX += chaserSpeedX;
        }

        else {
            chaserSpeedX = 0;
            fall = false;
        }


        if (checkCollision_obtall(playerX, playerY, 80, purpletallX, purpletallY, purpletallY + tall_width, purpletallX + tall_height) || checkCollision_ch(playerX, playerY, 80, chaserX, chaserY, 120)) {
            sound.playHitSound();
            gameOver = true;
        }



        if (checkCollision_obshort(playerX, playerY, 80, purpleshortX, purpleshortY, purpleshortY + short_width, purpleshortX + short_height)) {
            sound.playHitSound();
            collide=true;
        }

        else {
            collide=false;
            count=0;
        }



        if (collide==true && count==0)
        {
            count+=1;
            playerY -= 0.1;
            totalcount+=1;
        }


        if (checkCollision_obshort(playerX, playerY, 80, purpleshortX, purpleshortY, purpleshortY + short_width, purpleshortX + short_height) && totalcount==2) {
            sound.playHitSound();
            gameOver = true;
}
}







    @Override

    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        purpleshortY += shortSpeedY;
        purpletallY += tallSpeedY;


        if (isStopwatchRunning) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            int seconds = (int) (elapsedTime / 1000);
            int minutes = seconds / 60;
            seconds %= 60;
            score = seconds;

            float rotationAngle = 90f;
            canvas.save();
            canvas.rotate(rotationAngle, 700, 50);


            String stopwatchText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            Paint textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(40);
            canvas.drawText(stopwatchText, 700, 50, textPaint);
            canvas.restore();
        }






        if (checkchaser_obshort(chaserX, chaserY, 120, purpleshortX, purpleshortY, purpleshortY + short_width, purpleshortX + short_height) || checkchaser_obtall(chaserX, chaserY, 120, purpletallX, purpletallY, purpletallY + tall_width, purpletallX + tall_height)) {
            if (fall==false) {
                while (chaserX < 500) {
                    chaserSpeedX += 0.1f;
                    chaserX += chaserSpeedX;
                }
            }
            fall=true;
        }

        if (fall==true && chaserX>150) {
            chaserSpeedX -= 0.1f;
            chaserX += chaserSpeedX;
        }


        if (touch==true && allowjump==true) {

            allowjump=false;

            while (playerX<500)
            {
                playerSpeedX += 0.1f;
                playerX += playerSpeedX;
                sound.playJumpSound();
            }

            }

        else if(touch==true && allowjump==false) {
            if (playerX > 110) {
                playerSpeedX -= 0.2f;
                playerX += playerSpeedX;
            } else {
                allowjump = true;
                playerSpeedX = 0;
            }
        }

        else if (touch==false) {

            if (playerX > 110)
            {
                playerSpeedX -= 0.2f;
                playerX += playerSpeedX;
            }
            else {
                allowjump=true;
                playerSpeedX = 0;
            }


        }


        Paint chaser_p = new Paint();
        chaser_p.setColor(Color.rgb(250, 0, 0));
        canvas.drawCircle(chaserX, chaserY, 120, chaser_p);

        Paint player_p = new Paint();
        player_p.setColor(Color.rgb(0, 0, 250));
        canvas.drawCircle(playerX, playerY, 80, player_p);

            Paint purpleshort_p = new Paint();
            purpleshort_p.setColor(Color.rgb(128, 0, 128));
            canvas.drawRect(purpleshortX, purpleshortY, purpleshortX + ob_purpleshort.width(), purpleshortY + ob_purpleshort.height(), purpleshort_p);

            Paint purpletall_p = new Paint();
            purpletall_p.setColor(Color.rgb(128, 0, 128));
            canvas.drawRect(purpletallX, purpletallY, purpletallX + ob_purpletall.width(), purpletallY + ob_purpletall.height(), purpletall_p);


        invalidate();


        int highScore = sharedPreferences.getInt("highScore", 0);

        if (score>highScore)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highScore", score);
            editor.apply();
        }



        if (gameOver==true) {
            stopStopwatch();
            shortSpeedY = 0;
            tallSpeedY = 0;
            float centerX = getWidth() / 2f;
            float centerY = getHeight() / 2f;
            float rightmost = getWidth();
            float topmost = getHeight();

            Paint gameOverTextPaint = new Paint();
            gameOverTextPaint.setColor(Color.RED);
            gameOverTextPaint.setTextSize(50);
            gameOverTextPaint.setTextAlign(Paint.Align.CENTER);

            String gameOverText = "Game Over";
            Rect textBounds1 = new Rect();
            gameOverTextPaint.getTextBounds(gameOverText, 0, gameOverText.length(), textBounds1);


            int textWidth1 = textBounds1.width();
            int textHeight1 = textBounds1.height();

            float padding = 500f;

            Bitmap rotatedBitmap1 = Bitmap.createBitmap((int) (textWidth1 + padding * 2), (int) (textHeight1 + padding * 2), Bitmap.Config.ARGB_8888);
            Canvas rotatedCanvas1 = new Canvas(rotatedBitmap1);
            rotatedCanvas1.rotate(90, (textWidth1 + padding * 2) / 2f, (textHeight1 + padding * 2) / 2f);
            rotatedCanvas1.drawText(gameOverText, padding, textHeight1 + padding, gameOverTextPaint);

            float left2 = centerX - (textWidth1 + padding * 2) / 2f;
            float top2 = centerY - (textHeight1 + padding * 2) / 2f;
            canvas.drawBitmap(rotatedBitmap1, left2, top2, null);



            Paint scoreTextPaint = new Paint();
            scoreTextPaint.setColor(Color.BLACK);
            scoreTextPaint.setTextSize(30);
            scoreTextPaint.setTextAlign(Paint.Align.CENTER);

            String scoreText = "High Score:"+highScore+"Score:"+score;
            Rect textBounds2 = new Rect();
            scoreTextPaint.getTextBounds(scoreText, 0, scoreText.length(), textBounds2);


            int textWidth2 = textBounds2.width();
            int textHeight2 = textBounds2.height();

            Bitmap rotatedBitmap2 = Bitmap.createBitmap((int) (textWidth2 + padding * 2), (int) (textHeight2 + padding * 2), Bitmap.Config.ARGB_8888);
            Canvas rotatedCanvas2 = new Canvas(rotatedBitmap2);
            rotatedCanvas2.rotate(90, (textWidth2 + padding * 2) / 2f, (textHeight2 + padding * 2) / 2f);
            rotatedCanvas2.drawText(scoreText, padding, textHeight2 + padding, scoreTextPaint);


            float right = rightmost -50 - (textWidth2 + padding * 2) / 2f;
            float top = topmost - 50 - (textHeight2 + padding * 2) / 2f;
            canvas.drawBitmap(rotatedBitmap2, right, top, null);

        }

        invalidate();


    }

    public void stopStopwatch() {
        isStopwatchRunning = false;
    }


    private void endofscreen() {

      if (purpleshortY + ob_purpleshort.height() < 0) {
            purpleshortY = getHeight() + random.nextInt(1800) ;
        }

        if (purpletallY + ob_purpletall.height() < 0) {
            purpletallY = getHeight() + random.nextInt(1800);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (gameOver==true) {
            return false;
        }

        else if (action == MotionEvent.ACTION_DOWN) {
                touch = true;
            }
        else if (action == MotionEvent.ACTION_UP) {
                touch = false;

            }
            return true;

    }


    public boolean checkCollision_obshort(float playerX, float playerY, float circleRadius,
                                          float purpleshortX, float purpleshortY, float shortRight, float shortBottom) {
        if (playerX <= 350) {
            float closestX = Math.max(purpleshortX, Math.min(playerX, shortRight));
            float closestY = Math.max(purpleshortY, Math.min(playerY, shortBottom));

            float distance = (float) Math.sqrt(Math.pow(playerX - closestX, 2) + Math.pow(playerY - closestY, 2));

            return distance <= circleRadius;
        }
        else {
            return false;
        }
    }

    public boolean checkCollision_obtall(float playerX, float playerY, float circleRadius,
                                         float purpletallX, float purpletallY, float tallRight, float tallBottom) {

        if (playerX <= 350) {
            float closestX = Math.max(purpletallX, Math.min(playerX, tallRight));
            float closestY = Math.max(purpletallY, Math.min(playerY, tallBottom));

            float distance = (float) Math.sqrt(Math.pow(playerX - closestX, 2) + Math.pow(playerY - closestY, 2));

            return distance <= circleRadius;
        }
        else {
            return false;
        }
    }


    public boolean checkchaser_obshort(float chaserX, float chaserY, float circleRadius,
                                          float purpleshortX, float purpleshortY, float shortRight, float shortBottom) {
        if (chaserX <= 200) {
            float closestX = Math.max(purpleshortX, Math.min(chaserX, shortRight));
            float closestY = Math.max(purpleshortY, Math.min(chaserY, shortBottom));

            float distance = (float) Math.sqrt(Math.pow(chaserX - closestX, 2) + (Math.pow(chaserY - closestY, 2)));

            if (distance <= circleRadius+30) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public boolean checkchaser_obtall(float chaserX, float chaserY, float circleRadius,
                                         float purpletallX, float purpletallY, float tallRight, float tallBottom) {

        if (chaserX <= 400) {
            float closestX = Math.max(purpletallX, Math.min(chaserX, tallRight));
            float closestY = Math.max(purpletallY, Math.min(chaserY, tallBottom));

            float distance = (float) Math.sqrt(Math.pow(chaserX - closestX, 2) + Math.pow(chaserY - closestY, 2));

            if (distance <= circleRadius+30) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }




    public boolean checkCollision_ch(float playerX, float playerY, float radius1,
                                  float chaserX, float chaserY, float radius2) {
        float distance = (float) Math.sqrt(Math.pow(chaserX - playerX, 2) + Math.pow(chaserY - playerY, 2));
        if (distance <= radius1 + radius2)
        {
            return true;
        }
        else {
            return false;
        }
    }

}



