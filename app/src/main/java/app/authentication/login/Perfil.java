package app.authentication.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Perfil extends AppCompatActivity {

    TextView txt_NomeUsuario, txt_emailUsuario;
    Button btn_deslogar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getSupportActionBar().hide();

        StartPerfilScreen();

        btn_deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Perfil.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //Get the firebaseAuth instance and a current user with their ID
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    txt_NomeUsuario.setText(documentSnapshot.getString("nome"));
                    txt_emailUsuario.setText(email);
                }
            }
        });
    }

    private void StartPerfilScreen() {

        txt_NomeUsuario = findViewById(R.id.txt_NomeUsuario);
        txt_emailUsuario = findViewById(R.id.txt_emailUsuario);
        btn_deslogar = findViewById(R.id.btn_deslogar);
    }
}