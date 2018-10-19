package group2.seshealthpatient.Fragments;


import android.app.Activity;
import android.app.AuthenticationRequiredException;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import group2.seshealthpatient.Activities.ViewUploadActivity;
import group2.seshealthpatient.Model.Upload;
import group2.seshealthpatient.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendFileFragment extends Fragment {
    private static final String TAG = "SendFileFragment";


    @BindView(R.id.FileNameTextView)
    TextView FileName;


    Uri Uri1;

    ProgressDialog progressDialog;



    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;


    public SendFileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        reference = database.getReference();
        getActivity().setTitle("Send File");
    }



    @OnClick(R.id.UploadBtn)
    public void UploadFile(){
        if(Uri1 != null)
            upload(Uri1);
        else{
            Toast.makeText(getContext(), "Please selected a file.",Toast.LENGTH_LONG).show();
        }
    }




    public void upload(final Uri uri3){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        //获得storage 的 reference
        firebaseUser = firebaseAuth.getCurrentUser();
        final StorageReference storageReference = storage.getReference();
        final UploadTask uploadTask = storageReference.child("Uploads").child(getFormat(uri3)).child(getFileName(uri3)).putFile(uri3);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            //成功的情况
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return storageReference.child("Uploads").child(getFormat(uri3)).child(getFileName(uri3)).getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String downloadUrl = task.getResult().toString();
                            String name = getFileName(uri3);
                            Upload upload = new Upload(name,getFormat(uri3),downloadUrl);


                            //储存在realtime database中
                            reference.child("Users").child(firebaseUser.getUid()).child("UploadFiles").child(name).setValue(upload)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getContext(),"File is successfully uploaded",Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();}
                                            else
                                                Toast.makeText(getContext(),"Upload failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Upload failed",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);

            }
        });

    }

    @OnClick(R.id.ChoosePDFBtn)
    public void ChoosePDF(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,99);
    }
    @OnClick(R.id.ChooseDocBtn)
    public void ChooseDoc(){
        Intent intent = new Intent();
        intent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,88);
    }
    @OnClick(R.id.ChooseImageBtn)
    public void ChoosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,77);
    }
    @OnClick(R.id.ChooseVideoBtn)
    public void Choosevideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,66);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==99 && resultCode == RESULT_OK && data!=null){
            Uri1=data.getData();


            String file1 = getFileAbsolutePath(getActivity(),Uri1);
            String file = file1.substring(file1.lastIndexOf("/") + 1, file1.length());
            FileName.setText(getString(R.string.selected_file,file));

        }
        else if(requestCode==88 && resultCode == RESULT_OK && data!=null){

            Uri1=data.getData();

            String file1 = getFileAbsolutePath(getActivity(),Uri1);
            String file = file1.substring(file1.lastIndexOf("/") + 1, file1.length());

            FileName.setText(getString(R.string.selected_file,file));
        }
        else if(requestCode==77 && resultCode == RESULT_OK && data!=null){
            Uri1=data.getData();

            String file1 = getFileAbsolutePath(getActivity(),Uri1);
            String file = file1.substring(file1.lastIndexOf("/") + 1, file1.length());

            FileName.setText(getString(R.string.selected_file,file));

        }
        else if(requestCode==66 && resultCode == RESULT_OK && data!=null){
            Uri1=data.getData();
            String file1 = getFileAbsolutePath(getActivity(),Uri1);
            String file = file1.substring(file1.lastIndexOf("/") + 1, file1.length());
            FileName.setText(getString(R.string.selected_file,file));

        }

        else
            Toast.makeText(getContext(),"Please select a file",Toast.LENGTH_LONG).show();
    }



    private String getFileName(Uri uri1){
        String file = getFileAbsolutePath(getActivity(),uri1);
        String name = file.substring(file.lastIndexOf("/") + 1, file.length());
        return name.substring(0,name.lastIndexOf("."));
    }
    private String getFormat(Uri uri2){
        String file = getFileAbsolutePath(getActivity(),uri2);
        String name = file.substring(file.lastIndexOf("/") + 1, file.length());
        return name.substring(name.lastIndexOf(".")+1);}



    public static String getFileAbsolutePath(Activity context, Uri fileUri) {
        if (context == null || fileUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, fileUri)) {
            if (isExternalStorageDocument(fileUri)) {
                String docId = DocumentsContract.getDocumentId(fileUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(fileUri)) {
                String id = DocumentsContract.getDocumentId(fileUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(fileUri)) {
                String docId = DocumentsContract.getDocumentId(fileUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(fileUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(fileUri))
                return fileUri.getLastPathSegment();
            return getDataColumn(context, fileUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(fileUri.getScheme())) {
            return fileUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    @OnClick(R.id.ViewUploadBtn)
    public void viewUploadFile(){
        Intent intent = new Intent(getActivity(), ViewUploadActivity.class);
        startActivity(intent);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_send_file, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Now that the view has been created, we can use butter knife functionality
    }


}
