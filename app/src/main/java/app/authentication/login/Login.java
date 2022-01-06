package app.authentication.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.DragStartHelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    TextView txt_tela_cadastro;
    EditText edit_email, edit_password;
    Button btn_entrar;
    ProgressBar progressbar;

    String[] mensagens = {"Preencha todos os campo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        startLoginScreen();

        txt_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
            }
        });

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edit_email.getText().toString();
                String senha = edit_password.getText().toString();

                if (email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else {
                    AuthenticateUser(view);
                }
            }
        });
    }

    private void AuthenticateUser(View view){
        String email = edit_email.getText().toString();
        String senha = edit_password.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    progressbar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PerfilScreen();
                        }
                    },3000);
                }else {
                    String error;

                    try {
                        throw task.getException();
                    }catch (Exception e){
                        error = "Error ao logar";
                    }
                    Snackbar snackbar = Snackbar.make(view, error, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser =FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null){
            PerfilScreen();
        }
    }

    private void PerfilScreen(){
       Intent intent = new Intent(Login.this, Perfil.class);
       startActivity(intent);
       finish();
    }

    private void startLoginScreen(){
        txt_tela_cadastro = findViewById(R.id.txt_tela_cadastro);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        btn_entrar = findViewById(R.id.btn_entrar);
        progressbar = findViewById(R.id.progressbar);
    }
}