package com.example.johnmack.facebookintregration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Provider;

public class PostActivity extends AppCompatActivity {

    private ImageView ivImage;
    private TextView tvName,tvId,tvUrl,tvEmail,tvGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        tvId = (TextView) findViewById(R.id.tvId);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUrl = (TextView) findViewById(R.id.tvProfileUrl);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvEmail = (TextView) findViewById(R.id.tvEmail);

        Log.d("Profile Activity : ","Here");

        Profile profile = Profile.getCurrentProfile();
        /*if(profile!=null){

            tvName.setText("Name : "+profile.getName());
            tvId.setText("ID : "+profile.getId());
            tvUrl.setText("Profile URL : "+profile.getLinkUri());
            Glide.with(this).load(profile.getProfilePictureUri(200,200)).into(ivImage);
        }*/
        getData();


    }

    public void getData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("Graph API Request : ",object.toString());
                        try {
                            String email_id=object.getString("email");
                            String gender=object.getString("gender");
                            String first_name = object
                                    .getString("first_name");
                            String l_name = object.getString("last_name");
                            String id = object.getString("id");
                            tvId.setText(id);
                            String link = object.getString("link");
                            tvUrl.setText(link);
                            try {
                                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                                Glide.with(getApplicationContext()).load(profile_pic).into(ivImage);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            tvName.setText(first_name+" "+l_name);
                            tvEmail.setText(email_id);
                            tvGender.setText(gender);
                            //Log.d("Email : ",email_id);
                            Log.d("Object  : ",object.toString());
                            //Log.d("Gender : ",gender);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,link");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
