package com.example.game;





import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String tip, name;
    Button Okay;
    List<Character> characters = new ArrayList<>();
    List<String> playernames = new ArrayList<>();
    List<String> desc = new ArrayList<>();
    List<String> imgs = new ArrayList<>();



    int start_count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GameView(this));




        //setContentView(R.layout.activity_main);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-obstacle-dodge.vercel.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<ApiResponse> call = api.getTip();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiController = response.body();
                    tip = apiController.getTip();
                    showDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "Response failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                //Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                t.printStackTrace(); // Print the error stack trace for debugging
            }
        });


        String json = "{\"type\":\"player\"}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        Api apiService = retrofit.create(Api.class);
        call = apiService.getCharacters(requestBody);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse charactersResponse = response.body();
                    List<Character> characters = charactersResponse.getCharacters();

                    for (Character character : characters) {
                        playernames.add(character.getName());
                        desc.add(character.getDescription());
                        imgs.add(character.getImageUrl());
                    }
                    showcharacters_Dialog();


                } else {
                    Toast.makeText(getApplicationContext(), "Response failed", Toast.LENGTH_LONG).show();
                }

            }


            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });




        /*Call<ApiResponse> call2 = api.getScores();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();

                    ApiResponse apiController2 = response.body();
                    List<Score> scores = apiController2.getScores();

                    for (Score score : scores) {
                        String name = score.getName();
                        int scoreValue = score.getScore();
                        playernames.add(name);
                        playerscores.add(scoreValue);
                    }
                    showscores_Dialog();
                } else {
                    // Handle error response
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                //Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                t.printStackTrace(); // Print the error stack trace for debugging
            }
        }); */

    }


    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Tip")
                .setMessage(tip)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        start_count+=1;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        start_count+=1;
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showcharacters_Dialog() {
        Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.character_dialog);
        TextView p_1 = dialog1.findViewById(R.id.info_1);
        TextView p_2 = dialog1.findViewById(R.id.info_2);
        TextView p_3 = dialog1.findViewById(R.id.info_3);
        ImageView i_1 = dialog1.findViewById(R.id.image_1);
        ImageView i_2 = dialog1.findViewById(R.id.image_2);
        ImageView i_3 = dialog1.findViewById(R.id.image_3);
        p_1.append("Name: " + playernames.get(0) + "\n" + "Description:" + desc.get(0));
        p_2.append("Name: " + playernames.get(1) + "\n" + "Description:" + desc.get(1));
        p_3.append("Name: " + playernames.get(2) + "\n" + "Description:" + desc.get(2));
        String imageUrl_1 = imgs.get(0);
        String imageUrl_2 = imgs.get(1);
        String imageUrl_3 = imgs.get(2);
        Picasso.get().load(imageUrl_1).into(i_1);
        Picasso.get().load(imageUrl_2).into(i_2);
        Picasso.get().load(imageUrl_3).into(i_3);
        dialog1.setCancelable(false);
        dialog1.show();
        Okay = (Button) dialog1.findViewById(R.id.Okbutton2);
        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                start_count+=1;
            }
        });




    }






}
