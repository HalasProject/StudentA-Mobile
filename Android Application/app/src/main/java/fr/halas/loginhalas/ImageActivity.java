package fr.halas.loginhalas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ImageActivity extends BaseActivity {

    Button Download;
    String imageUrl;
    String module;
    DocumentReference AffichageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        showProgressDialog();

        Intent intent = getIntent();
        String AffichageID = intent.getStringExtra("AffichageID");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference AffichageRef = db.collection("affichages").document(AffichageID);

        AffichageRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               DocumentSnapshot document = task.getResult();
                 imageUrl = document.getString("Image");
                 module = document.getString("Module");

                Download = findViewById(R.id.buttonDownload);


                Button textModule = findViewById(R.id.textModule);
                textModule.setText(module);


                final ImageView imageView = findViewById(R.id.imageView);

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                storageRef.child(imageUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {


                    @Override
                    public void onSuccess(final Uri uri) {
                        Picasso.with(getBaseContext()).load(uri).into(imageView);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ImageActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
                //Picasso
                hideProgressDialog();

                PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);//PhotoView
            }
        });



    }

}
