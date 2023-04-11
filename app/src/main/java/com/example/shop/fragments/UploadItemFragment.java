package com.example.shop.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop.R;
import com.example.shop.functions.Functions;
import com.example.shop.objects.Partner;
import com.example.shop.objects.Product;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.UUID;


public class UploadItemFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private static final int CAMERA_REQUEST = 1888, GALLERY_REQUEST = 1889;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_GALLERY_PERMISSION_CODE = 123;

    private ImageButton ib_upload;
    private EditText ed_name, ed_category, ed_price, ed_description;
    private ImageView iv_img;
    private Bitmap bitM_upload = null;
    TextView tv_upload;
    ImageView iv_cloud;
    TextView textView_upload;


    public UploadItemFragment() {
    }


    public static UploadItemFragment newInstance(String param1, String param2) {
        UploadItemFragment fragment = new UploadItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_item, container, false);
        ed_name = view.findViewById(R.id.ed_name);
        ed_category = view.findViewById(R.id.ed_category);
        ed_price = view.findViewById(R.id.ed_price);
        ed_description = view.findViewById(R.id.ed_description);
        ib_upload = view.findViewById(R.id.ib_upload);
        iv_img = view.findViewById(R.id.iv_img);
        tv_upload = view.findViewById(R.id.tv_upload);
        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout galleryView = new LinearLayout(getContext());
                createGalleryView(galleryView);
                LinearLayout cameraView = new LinearLayout(getContext());
                createCameraView(cameraView);
                // now i have 2 options() camera & gallery.
                createImgDialog( galleryView, cameraView );
            }
        });


        ib_upload.setOnClickListener(this);
        iv_cloud = view.findViewById(R.id.iv_cloud);
        textView_upload = view.findViewById(R.id.textView_upload);

        Animation popInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.sslide_up_title);

        iv_cloud.startAnimation(popInAnimation);
        textView_upload.startAnimation(popInAnimation);

        return view;
    }

    // create an alertDialog builder with the gallery and camera options, the functions return the dialog.
    private AlertDialog createImgDialogBuilder(LinearLayout galleryView,LinearLayout cameraView ){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Image");
        builder.setNegativeButton("Cancel", null);
        builder.setView(galleryView);
        builder.setView(cameraView);
        return  builder.create();
    }

    // create the alertDialog that let you chose the img source.
    private void createImgDialog(LinearLayout galleryView,LinearLayout cameraView ){
        AlertDialog dialog =createImgDialogBuilder(galleryView,cameraView);

        // Set the click listeners for the custom views
        galleryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectImageFromGallery();
            }
        });

        cameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectImageFromCamera();
            }
        });

        // Add both views to the dialog
        LinearLayout optionsLayout = new LinearLayout(getContext());
        optionsLayout.setOrientation(LinearLayout.VERTICAL);
        optionsLayout.addView(galleryView);
        optionsLayout.addView(cameraView);
        dialog.setView(optionsLayout);

        dialog.show();
    }



    // Create a view for the camera option
    private void createCameraView(LinearLayout cameraView) {
        cameraView.setOrientation(LinearLayout.HORIZONTAL);
        cameraView.setGravity(Gravity.CENTER_VERTICAL);
        cameraView.setPadding(16, 16, 16, 16);
        ImageView cameraIcon = new ImageView(getContext());
        cameraIcon.setImageResource(R.drawable.icon_camera);
        cameraView.addView(cameraIcon);
        TextView cameraText = new TextView(getContext());
        cameraText.setText("Camera");
        cameraText.setTextSize(18);
        cameraView.addView(cameraText);

    }

    // Create a view for the gallery option
    private void createGalleryView(LinearLayout galleryView) {
        galleryView.setOrientation(LinearLayout.HORIZONTAL);
        galleryView.setGravity(Gravity.CENTER_VERTICAL);
        galleryView.setPadding(16, 16, 16, 16);
        ImageView galleryIcon = new ImageView(getContext());
        galleryIcon.setImageResource(R.drawable.icon_gallery);
        galleryView.addView(galleryIcon);
        TextView galleryText = new TextView(getContext());
        galleryText.setText("Gallery");
        galleryText.setTextSize(18);
        galleryView.addView(galleryText);
    }


    @Override
    public void onClick(View view) {
        if (view == ib_upload) {
            String str_name = ed_name.getText().toString();
            String str_category = ed_category.getText().toString();
            String str_price = ed_price.getText().toString();
            if (valid_info(str_name, str_category, str_price)) {  // the info is valid' it can be uploaded to the fire base
                iv_img.setDrawingCacheEnabled(true);
                iv_img.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) iv_img.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                String str_description = ed_description.getText().toString();
                uploadProduct(bitmap, str_name, str_category, str_price, str_description);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////


    // take a picture from the camera
    private void selectImageFromCamera() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            Toast.makeText(getContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra("requestCode", CAMERA_REQUEST);
            takePicture.launch(cameraIntent);
        }
    }

    // select an image from the gallery
    private void selectImageFromGallery() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_GALLERY_PERMISSION_CODE);
            return;
        }

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra("requestCode", GALLERY_REQUEST);
        pickImage.launch(galleryIntent);
    }

    // the new on activity result for the camera

    ActivityResultLauncher<Intent> takePicture = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");
                            bitM_upload = photo;
                            iv_img.setImageBitmap(photo);
                        }
                    }
                }
            });

    // the new on activity result for the gallery
    ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            cursor.close();

                            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                            bitM_upload = bitmap;
                            iv_img.setImageBitmap(bitmap);
                        }
                    }
                }
            });


    // upload the product to the fire-store and the img to the storage
    private void uploadProduct(Bitmap bitmap, String name, String category, String price, String description) {

        //showing progressDialog while uploading
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Defining the child of storageReference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        String uuid = UUID.randomUUID().toString();
        StorageReference ref = storageReference.child("productsImg/" + uuid);

        // adding listeners on upload or failure of image
        iv_img.setDrawingCacheEnabled(true);
        iv_img.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = (UploadTask) ref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "Product Uploaded!!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Functions.setPerson();
                ed_name.setText("");
                ed_category.setText("");
                ed_price.setText("");
                ed_description.setText("");
                iv_img.setImageDrawable(null);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int) progress + "%");
            }
        });

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put(uuid, downloadUri.toString());
                    db.collection("users").document(Functions.generalConnectedPerson.getEmail())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Partner p = documentSnapshot.toObject(Partner.class);
                                    Product product_created = createProduct(description, name, category, price, uuid, downloadUri.toString());
                                    p.getItems().add(product_created);
                                    AccountFragment.uploadedProducts.add(product_created);
                                    db.collection("users").document(p.getEmail()).set(p);
                                    db.collection("products").document(uuid).set(product_created);

                                }
                            });

                }
            }
        });
    }


    // check if the product's info is valid and return it
    private boolean valid_info(String str_name, String str_category, String str_price) {
        boolean valid_info = true;
        if (iv_img.getDrawable() == null) {
            Toast.makeText(getContext(), "choose an image", Toast.LENGTH_SHORT).show();
            valid_info = false;
        }
        if (str_name.equals("")) {
            ed_name.setError("enter a name");
            valid_info = false;
        }
        if (str_category.equals("")) {
            ed_category.setError("enter a category");
            valid_info = false;
        }
        if (str_price.length() >= 7) {
            ed_price.setError("price must be less than 1M");
            valid_info = false;
        }
        if (str_price.equals("")) {
            ed_price.setError("enter a price");
            valid_info = false;
        }
        return valid_info;
    }

    // create a new products based on the info and return it
    private Product createProduct(String str_description, String str_name, String str_category, String str_price, String productId, String imgUrl) {
        if (str_description.equals("")) {
            return new Product(str_name.trim(), str_category.trim(), imgUrl, Integer.parseInt(str_price), productId, Functions.generalConnectedPerson.getEmail());
        }
        return new Product(str_name.trim(), str_category.trim(), imgUrl, Integer.parseInt(str_price), productId, str_description, Functions.generalConnectedPerson.getEmail());
    }


}