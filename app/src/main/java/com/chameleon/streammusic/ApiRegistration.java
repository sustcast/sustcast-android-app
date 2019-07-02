package com.chameleon.streammusic;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.Toast;

        import com.chameleon.streammusic.data.model.signupResponse;
        import com.chameleon.streammusic.data.remote.ApiUtils;
        import com.chameleon.streammusic.data.remote.UserClient;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

public class ApiRegistration extends AppCompatActivity {
    private Button mTestButton;

    private EditText mNameEntry;
    private EditText mEmailEntry;
    private EditText mPasswordEntry;
    private EditText mCpasswordEntry;
    private EditText mContact;
    private EditText mRegNo;
    private EditText mDept;

    private UserClient mAPIService;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_registration);

        mNameEntry = (EditText) findViewById(R.id.entryName);
        mEmailEntry = (EditText) findViewById(R.id.entryEmail);
        mPasswordEntry = (EditText) findViewById(R.id.entryPassword);
        mCpasswordEntry = (EditText) findViewById(R.id.entryCpassword);
        mContact = (EditText) findViewById(R.id.entryContact);
        mRegNo = (EditText) findViewById(R.id.entryRegNo);
        mDept = (EditText) findViewById(R.id.entryDept);

        mAPIService = ApiUtils.getAPIService();
        mTestButton = (Button) findViewById(R.id.testButton);
        //spinner = findViewById(R.id.pBarreg);

        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEntry.getText().toString().trim();
                String email = mEmailEntry.getText().toString().trim();
                String password = mPasswordEntry.getText().toString().trim();
                String cpassword = mCpasswordEntry.getText().toString().trim();
                String registration_number = mRegNo.getText().toString().trim();
                String contact = mContact.getText().toString().trim();
                String department = mDept.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(registration_number) && !TextUtils.isEmpty(contact) && !TextUtils.isEmpty(department)) {
                    spinner.setVisibility(View.VISIBLE);
                    startRegistration(name, email, password, cpassword, registration_number, contact, department);
                }
            }
        });


    }


    private void startRegistration(String name, String email, String password, String c_password, String registration_number, String contact_number, String department) {
        spinner.setVisibility(View.VISIBLE);
        mAPIService.registerUser(name, email, password, c_password, registration_number, department, contact_number).enqueue(new Callback<signupResponse>() {
            @Override
            public void onResponse(Call<signupResponse> call, Response<signupResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ApiRegistration.this, "Verification mail sent", Toast.LENGTH_LONG).show();
                    spinner.setVisibility(View.GONE);
                    Intent intent = new Intent(ApiRegistration.this, ApiLogin.class);
                    startActivity(intent);
                } else {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(ApiRegistration.this, "Response Unsuccessful!", Toast.LENGTH_LONG).show();
                }

                //System.out.println("JSON => " + new GsonBuilder().setPrettyPrinting().create().toJson(response));
            }

            @Override
            public void onFailure(Call<signupResponse> call, Throwable t) {
                spinner.setVisibility(View.GONE);
                Toast.makeText(ApiRegistration.this, "Unable to complete registration", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }


}
