package fr.halas.loginhalas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.halas.loginhalas.helper.getImage;

public class AddingActivity extends BaseActivity {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    //BUTTONS
    Button buttonChoose;
    Button buttonUpload;
    @ServerTimestamp
    Date time;
    //EDIT TEXT
    EditText ETModule;
    EditText ETSections;
    EditText ETGroupe;
    TextView Notice;

    // IMAGE & OTHER
    ImageView imageView;
    Bitmap bitmap, imageUPLOADED;

    // FOR SPINNER
    Spinner ETYears;
    String YearArray[] = {"Licence 1", "Licence 2", "Licence 3", "Master 1", "Master 2"};
    ArrayAdapter<String> Adapter;

    // FOR IMAGE UPLOAD
    int PICK_IMAGE_REQUEST = 1;
//FIREBASE

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user1 = mAuth.getCurrentUser();


    // FOR DATABASE MYSQL & PHP & JSON
    private static final String TAG = AddingActivity.class.getSimpleName();

    private String KEY_CREATOR = "UserID";
    private String KEY_CREATOR_NAME = "CreatorName";
    private String KEY_MODULO = "Module";
    private String KEY_YEARS = "Year";
    private String KEY_SECTIONS = "Section";
    private String KEY_GROUPE = "Groupe";
    private String KEY_IMAGE = "Image";
    private String KEY_DATE = "Date";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef;
    SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);


        Notice = findViewById(R.id.textNotice);
        buttonChoose = findViewById(R.id.buttonChoose);
        buttonUpload = findViewById(R.id.buttonUpload);
        ETModule = findViewById(R.id.ETModule);
        ETYears = findViewById(R.id.ETYears);
        ETSections = findViewById(R.id.ETSections);
        ETGroupe = findViewById(R.id.ETGroupe);
        imageView = findViewById(R.id.imageView);



        ETModule.setVisibility(View.GONE);
        ETYears.setVisibility(View.GONE);
        ETSections.setVisibility(View.GONE);
        ETGroupe.setVisibility(View.GONE);
        buttonUpload.setVisibility(View.GONE);


        Adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, YearArray);
        ETYears.setAdapter(Adapter);

        // ADD IMAGE
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        // UPLOAD DATA
        buttonUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int ok = 0;
                if (isEmpty(ETModule)) {
                    ETModule.setError("Veuillez taper le module !");
                } else ok++;

                if (isEmpty(ETSections)) {
                    ETSections.setError("Veuillez taper la section !");
                } else ok++;

                if (isEmpty(ETGroupe)) {
                    ETGroupe.setError("Veuillez taper le groupe !");
                } else ok++;

                if (ok == 3) uploadImage();

            }
        });


    }

    private void uploadImage() {

        showProgressDialog();



        final String module = ETModule.getText().toString().trim();
        final String year = ETYears.getSelectedItem().toString().trim();
        final String section = ETSections.getText().toString().trim();
        final String groupe = ETGroupe.getText().toString().trim();
        final String imageNAME = randomAlphaNumeric(15);


        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference mountainImagesRef = storageRef.child("images/" + imageNAME);


        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(AddingActivity.this, "Erreur ! ", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Map<String, Object> user = new HashMap<>();
                user.put(KEY_CREATOR, user1.getUid());
                user.put(KEY_CREATOR_NAME, user1.getDisplayName());
                user.put(KEY_MODULO, module);
                user.put(KEY_YEARS, year);
                user.put(KEY_SECTIONS, section);
                user.put(KEY_GROUPE, groupe);
                user.put(KEY_DATE, FieldValue.serverTimestamp());
                user.put(KEY_IMAGE, ("images/" + imageNAME));


                db.collection("affichages").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        hideProgressDialog();
                        Toast.makeText(AddingActivity.this, "Document added with success !", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddingActivity.this, "Erreur ! ", Toast.LENGTH_LONG).show();
                                hideProgressDialog();
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                startActivity(new Intent(AddingActivity.this, UserAreaActivity.class));
            }
        });


        init();
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageUPLOADED = getImage.setToImageView(getImage.getResizedBitmap(bitmap, 512));
                imageView.setImageBitmap(imageUPLOADED);
                ETModule.setVisibility(View.VISIBLE);
                ETYears.setVisibility(View.VISIBLE);
                ETSections.setVisibility(View.VISIBLE);
                ETGroupe.setVisibility(View.VISIBLE);
                buttonUpload.setVisibility(View.VISIBLE);
                Notice.setVisibility(View.GONE);
                buttonChoose.setText("Modifier la photo !");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void init() {
        imageView.setImageResource(0);
        ETModule.setText(null);
        ETSections.setText(null);
        ETGroupe.setText(null);
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;
        else
            return true;
    }

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


}