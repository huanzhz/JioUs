package com.jious.EventActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.jious.Model.Event;
import com.jious.Model.User;
import com.jious.R;

import java.util.HashMap;

public class EventDetailView extends AppCompatActivity {

    TextView eName,eDes,eLocation,eDate,eTime;
    DatabaseReference databaseEventRef,databaseUserRef;
    String EventID,User_ID;
    FirebaseUser fuser;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    StorageReference storageReference;
    private StorageTask uploadTask;

    ImageView image_Event;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_view);
        Intent i = this.getIntent();
        EventID = i.getExtras().getString("EventID");
        eName = (TextView) findViewById(R.id.tv_eventName);
        eDes = (TextView) findViewById(R.id.tv_eventDesc);
        eLocation = (TextView) findViewById(R.id.tv_eventVenue);
        eDate = (TextView) findViewById(R.id.tv_eventDate);
        eTime = (TextView) findViewById(R.id.tv_eventTime);
        databaseEventRef = FirebaseDatabase.getInstance().getReference("Event");
        databaseUserRef = FirebaseDatabase.getInstance().getReference("Users");

        image_Event = (ImageView) findViewById(R.id.eventImage);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
    }

    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = EventDetailView.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(EventDetailView.this);
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        // Update Picture storage
                        databaseEventRef = databaseEventRef.child(event.geteID());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        databaseEventRef.updateChildren(map);

                        pd.dismiss();
                    } else {
                        Toast.makeText(EventDetailView.this, "Failed", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EventDetailView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(EventDetailView.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(EventDetailView.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseEventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot eventSS: dataSnapshot.getChildren()){
                    event = eventSS.getValue(Event.class);
                    String check = event.geteID();
                    if(EventID.equals(check)) {
                        eName.setText("Name ; " + event.geteName());
                        eDes.setText("Description : " + event.geteDes());
                        eLocation.setText("Venue : " + event.geteLocation());
                        eDate.setText("Date : " + event.getsDate() + " - " + event.geteDate());
                        eTime.setText(("Time : " + event.getsTime() + " - " + event.geteTime()));
                        // Get the image
                        if (event.getImageURL().equals("default")) {
                            image_Event.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Glide.with(EventDetailView.this).load(event.getImageURL()).into(image_Event);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fuser = FirebaseAuth.getInstance().getCurrentUser();
                User_ID = fuser.getUid();
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    User cUser = user.getValue(User.class);
                    String userID = cUser.getId();
                    Long eCreator = cUser.geteCreator();
                    if(userID.equals(User_ID) && eCreator == 1){
                        //If creator then able to change picture
                        image_Event.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openImage();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
