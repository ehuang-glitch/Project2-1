package theActivites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.canvasclone_3.databinding.ActivityLoginBinding;

import database.CanvasCloneRepository;
import database.entites.User;


public class loginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private CanvasCloneRepository repository;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        repository = CanvasCloneRepository.getReposoitory(getApplication());


        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });

        binding.SignUpButton.setOnClickListener(v -> {
            Intent intent = registerActivity.registerIntentFactory(loginActivity.this);

            startActivity(intent);
        });
    }



    private void verifyUser(){
        String username = binding.userNameLoginEditText.getText().toString();

        if(username.isEmpty()){
            toastMaker("Username may not be blank");
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if(user != null){
                String password = binding.passwordLoginEditText.getText().toString();
                if(password.equals(user.getPassword())){

                    startActivity((MainActivity.mainActivityIntentFactory(getApplicationContext(), user.getId())));
                }else{
                    toastMaker("Invalid Password");
                    binding.passwordLoginEditText.setSelection(0);
                }
            }else{
                toastMaker(String.format("%s is not a valid username"));
                binding.userNameLoginEditText.setSelection(0);
            }

        });

    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context){
        return new Intent(context, loginActivity.class);


    }
}