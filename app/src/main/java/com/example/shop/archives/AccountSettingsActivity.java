package com.example.shop.archives;


/**
 *                                 לא בשימוש
 */


public class AccountSettingsActivity extends BasicActivity {

/*

    private Button btnSelect, btnUpload,btnDownload;

    // view for image view
    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ;
    StorageReference storageReference = storage.getReference();;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        // initialise views
        btnDownload= findViewById(R.id.btnDownload);
        btnSelect = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imgView);


        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });

        // on pressing btnUpload uploadImage() is called
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                uploadImage();
            }
        });

        btnDownload.setOnClickListener(this);
    }

    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }





    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            String uuid =UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("productsImg/" + uuid);

            // adding listeners on upload
            // or failure of image
            UploadTask uploadTask = (UploadTask) ref.putFile(filePath);
            uploadTask.addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    Toast.makeText(AccountSettingsActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
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
                        Toast.makeText(AccountSettingsActivity.this, downloadUri.toString(), Toast.LENGTH_SHORT).show();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        HashMap<String, String> hm =new HashMap<String,String>();
                        hm.put(uuid,downloadUri.toString());
                        db.collection("products").document("for_sale").set(hm);

                    }
                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnDownload){
            StorageReference storageRef = storage.getReference();
            StorageReference spaceRef = storageRef.child("test/yone.jpg");
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("test/yone.jpg");
            //Toast.makeText(AccountSettingsActivity.this, "" + storageReference.getName().toString(), Toast.LENGTH_SHORT).show();
            */
/*Object o = storageReference.get*//*

            String str = "https://firebasestorage.googleapis.com/v0/b/shop-d4e6c.appspot.com/o/test%2Fyone.jpg?alt=media&token=99cf99a3-9d19-4076-95e1-10df67e88bc3";
           // Glide.with(AccountSettingsActivity.this).load(str).into(imageView);
            spaceRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(AccountSettingsActivity.this).load(uri).into(imageView);
                    Log.d("img:" , uri.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("img:" , exception.toString() + "adad");
                }
            });

            Log.d("img:" , "end");


        }
    }
*/


/*    ImageButton imb_product, imb_cart, imb_history, imb_account;
    Person person_main = MainActivity.p, person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
            person_main = returnConnectedPerson();


        imb_product = findViewById(R.id.imb_product);
        imb_cart = findViewById(R.id.imb_cart);
        imb_history = findViewById(R.id.imb_history);
        imb_account = findViewById(R.id.imb_account);

        imb_product.setOnClickListener(this);
        imb_cart.setOnClickListener(this);
        imb_history.setOnClickListener(this);
        imb_account.setOnClickListener(this);
    }


    private void openAccInfoDialog() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Dialog builder = new Dialog(AccountSettingsActivity.this);
        builder.setContentView(R.layout.dialog_update_info);
        builder.setCancelable(true);

        Integer[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        Integer[] years = new Integer[12];
        int this_year = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 0; i < years.length; i++) {
            years[i] = this_year + i;
        }

        Button btn_update = builder.findViewById(R.id.btn_update);
        btn_cancel = builder.findViewById(R.id.btn_cancel);
        et_firstName = builder.findViewById(R.id.et_fistName);
        et_lastName = builder.findViewById(R.id.et_lastName);
        et_password = builder.findViewById(R.id.et_password);
        et_email = builder.findViewById(R.id.et_email);
        EditText et_zip = builder.findViewById(R.id.et_zip);
        EditText et_cardNumber = builder.findViewById(R.id.et_cardNumber);
        EditText et_cvc = builder.findViewById(R.id.et_cvc);
        EditText et_current_password = builder.findViewById(R.id.et_current_password);


        Spinner spinner_mm = builder.findViewById(R.id.spinner_mm);
        Spinner spinner_yy = builder.findViewById(R.id.spinner_yy);

        ArrayAdapter ad = new ArrayAdapter(AccountSettingsActivity.this, android.R.layout.simple_spinner_item, months);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mm.setPrompt("mm");  // doesnt work
        spinner_mm.setAdapter(ad);

        ad = new ArrayAdapter(AccountSettingsActivity.this, android.R.layout.simple_spinner_item, years);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_yy.setPrompt("yy");  // doesnt work
        spinner_yy.setAdapter(ad);

        person=person_main;
        et_firstName.setText(person.getFirstName());
        et_lastName.setText(person.getLastName());
        et_password.setText(person.getPassword());
        et_email.setText(person.getEmail());

        et_email.setEnabled(false);
        et_email.setInputType(InputType.TYPE_NULL);

        if(person instanceof Partner){
            int zip=((Partner)person).getZip();
            et_zip.setText(zip+"");
            int card_number=((Partner)person).getCard().getNumber();
            et_cardNumber.setText(card_number+"");
            int cvc=((Partner)person).getCard().getCvc();
            et_cvc.setText(cvc+"");
            int month=((Partner)person).getCard().getValidation().getMonth();
            spinner_mm.setSelection(month-1);
            int year=((Partner)person).getCard().getValidation().getYear();
            spinner_yy.setSelection(year-this_year);
        }


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_firstName = et_firstName.getText().toString();
                str_lastName = et_lastName.getText().toString();
                str_password = et_password.getText().toString();
                str_email = et_email.getText().toString();
                String str_zip = et_zip.getText().toString();
                String str_cardNumber = et_cardNumber.getText().toString();
                String str_cvc = et_cvc.getText().toString();

                //check if et is empty
                if (str_zip.equals("")) {
                    et_zip.setError("ENTER ZIP");
                }
                if (str_cardNumber.equals("")) {
                    et_cardNumber.setError("ENTER CARD NUMBER");
                }
                if (str_cvc.equals("")) {
                    et_cvc.setError("ENTER CVC");
                }


                if (str_firstName.equals("")) {
                    et_firstName.setError("ENTER FIRST NAME");
                }

                if (str_lastName.equals("")) {
                    et_lastName.setError("ENTER LAST NAME");
                }
                if (str_email.equals("") || !isValidEmailAddress(str_email)) {
                    et_email.setError("ENTER A VALID EMAIL ");

                }
                if (str_password.equals("") || str_password.length() < 6) {
                    et_password.setError("ENTER PASSWORD (6 chars)");
                }
                Boolean validInfo = str_password.length() >= 6 && isValidEmailAddress(str_email) && !str_firstName.equals("") && !str_lastName.equals("") && !str_email.equals("") && !str_password.equals("") && !str_zip.equals("") && !str_cardNumber.equals("") && !str_cvc.equals("");
                if (validInfo) {//info is valid

                    int int_zip = Integer.parseInt(str_zip);
                    int int_cardNumber = Integer.parseInt(str_cardNumber);
                    int int_cvc = Integer.parseInt(str_cvc);


                    if (et_current_password.getText().toString().equals(person.getPassword())) {


                        int spinner_pos = spinner_mm.getSelectedItemPosition();
                        int int_month = months[spinner_pos];
                        spinner_pos = spinner_yy.getSelectedItemPosition();
                        int int_year = years[spinner_pos];

                        Date date = new Date(int_month, int_year);
                        db.collection("users").document(str_email).set(new Partner(str_firstName, str_lastName, str_email, str_password, (new CreditCard(int_cvc, int_cardNumber, date)), int_zip));

                        if (!str_password.equals(person.getPassword())) {


                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            // Get auth credentials from the user for re-authentication
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(str_email, person.getPassword()); // Current Login Credentials \\
                            // Prompt the user to re-provide their sign-in credentials
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG, "User re-authenticated.");
                                            //Now change your email address \\
                                            //----------------Code for Changing Email Address----------\\
                                            user.updatePassword(str_password)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("change password tag", "User password address updated.");
                                                            }
                                                        }
                                                    });
                                            //----------------------------------------------------------\\
                                        }
                                    });
                        }


                        builder.cancel();
                        Toast.makeText(AccountSettingsActivity.this, "info saved", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(AccountSettingsActivity.this, "pls enter the password", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        builder.create();
        builder.show();
    }


    @Override
    public void onClick(View view) {
        if (view == imb_account) {
            openAccInfoDialog();
        }

    }*/
}