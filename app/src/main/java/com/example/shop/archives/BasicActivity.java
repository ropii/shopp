package com.example.shop.archives;

import androidx.appcompat.app.AppCompatActivity;

public class BasicActivity extends AppCompatActivity {

    /**
     *                                 לא בשימוש
     */

    /*Button btn_cancel, btn_signUp,btn_logIn , btn_disconnect;
    EditText et_firstName, et_lastName, et_password, et_email;
    private FirebaseAuth mAuth;
    String str_firstName;
    String str_lastName;
    String str_password;
    String str_email;
    static Menu globalMenu = null;
    MenuItem menu_signUp,menu_disconnect,menu_signIn,menu_accSettings;
    FirebaseFirestore db = FirebaseFirestore.getInstance();





    //מזמן MENU
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){



        getMenuInflater().inflate(R.menu.menu, menu);
        globalMenu = menu;
        return true;
    }




    //מעדכן את נראות הITEMS בMENU
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu_disconnect = globalMenu.findItem(R.id.menu_disconnect);
        menu_disconnect.setVisible(isSignIn());

        menu_signIn = globalMenu.findItem(R.id.menu_signIn);
        menu_signIn.setVisible(!isSignIn());

        menu_signUp = globalMenu.findItem(R.id.menu_products);
        menu_signUp.setVisible(!isSignIn());

        menu_accSettings = globalMenu.findItem(R.id.menu_accSettings);
        menu_disconnect.setVisible(isSignIn());

        return super.onPrepareOptionsMenu(menu);
    }

    // בתוך MENU מציאת הITEM הנלחץ
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_products) {
            openSignUpDialog();
        }
        if (id == R.id.menu_signIn) {
            openSignInDialog();
        }
        if (id == R.id.menu_disconnect) {
            openDisconnectDialog();
        }
        if (id == R.id.menu_accSettings) {
            startActivity(new Intent(this, AccountSettingsActivity.class));
        }
        return true;
    }







    //דיאלוג של התנתקות
    public  void openDisconnectDialog() {

        Dialog builder = new Dialog(BasicActivity.this);
        builder.setContentView(R.layout.dialog_disconnect);
        builder.setCancelable(true);


        btn_cancel = builder.findViewById(R.id.btn_cancel);
        btn_disconnect = builder.findViewById(R.id.btn_disconnect);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });

        btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // מנתק
                FirebaseAuth.getInstance().signOut();
                builder.cancel();
            }
        });

        builder.create();
        builder.show();


    }


    //דיאלוג של התחברות
    void openSignInDialog() {


        Dialog builder = new Dialog(BasicActivity.this);
        builder.setContentView(R.layout.dialog_sign_in);
        builder.setCancelable(true);


        et_password = builder.findViewById(R.id.et_password);
        btn_cancel = builder.findViewById(R.id.btn_cancel);
        et_email = builder.findViewById(R.id.et_email);
        btn_logIn = builder.findViewById(R.id.btn_logIn);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });

        btn_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_password = et_password.getText().toString();
                str_email = et_email.getText().toString();

                //check if et is empty
                if (str_email.equals("") || !isValidEmailAddress(str_email)) {
                    et_email.setError("ENTER A VALID EMAIL ");

                }
                if (str_password.equals("") ||str_password.length()<6) {
                    et_password.setError("ENTER PASSWORD (6 chars)");
                }
                Boolean validInfo = str_password.length()>=6 && isValidEmailAddress(str_email)&& !str_email.equals("") &&  !str_password.equals("");
                if (validInfo) {   //info is valid

                    builder.cancel();
                    logIn(str_email,str_password);

                }


            }
        });

        builder.create();
        builder.show();


    }


    // דיאלוג של הרשמה
    void openSignUpDialog() {

        Dialog builder = new Dialog(this);
        builder.setContentView(R.layout.dialog_sign_up);
        builder.setCancelable(true);


        btn_signUp = builder.findViewById(R.id.btn_signUp);
        btn_cancel = builder.findViewById(R.id.btn_cancel);
        et_firstName = builder.findViewById(R.id.et_fistName);
        et_lastName = builder.findViewById(R.id.et_lastName);
        et_password = builder.findViewById(R.id.et_password);
        et_email = builder.findViewById(R.id.et_email);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_firstName = et_firstName.getText().toString();
                str_lastName = et_lastName.getText().toString();
                str_password = et_password.getText().toString();
                str_email = et_email.getText().toString();

                //check if et is empty
                if (str_firstName.equals("")) {
                    et_firstName.setError("ENTER FIRST NAME");
                }

                if (str_lastName.equals("")) {
                    et_lastName.setError("ENTER LAST NAME");
                }
                if (str_email.equals("") || !isValidEmailAddress(str_email)) {
                    et_email.setError("ENTER A VALID EMAIL ");

                }
                if (str_password.equals("") ||str_password.length()<6) {
                    et_password.setError("ENTER PASSWORD (6 chars)");
                }
                Boolean validInfo = str_password.length()>=6 && isValidEmailAddress(str_email)&& !str_firstName.equals("") &&  !str_lastName.equals("") &&  !str_email.equals("") &&  !str_password.equals("");
                if (validInfo) {   //info is valid

                    builder.cancel();
                    Person p = new Person(str_firstName, str_lastName, str_email, str_password);
                    registerUser(p);

                }


            }
        });

        builder.create();
        builder.show();
    }






                                                                     // פעולות

    // מקבל אימייל וסיסמא ומתחבר לחשבון
    public void logIn(String email, String password){

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(BasicActivity.this, "logged in.",Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(BasicActivity.this, "logged in failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    // שליחת אימייל וסיסמא לפייר-בייס (הרשמה)
    public void registerUser(Person p) {

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(str_email, str_password).addOnCompleteListener(BasicActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    db.collection("users").document(p.getEmail()+"").set(p);          // הוספת הפרטים למשתמש
                    Toast.makeText(BasicActivity.this, "SIGN UP SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(BasicActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });







    }*/
}

