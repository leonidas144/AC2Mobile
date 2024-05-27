package com.example.ac2atividade5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ac2atividade5.model.Aluno;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ActivityAluno extends AppCompatActivity {

    private EditText editTextNome, editTextRA, editTextCEP;
    private TextView textViewLogradouro, textViewComplemento, textViewBairro, textViewCidade, textViewUF;
    private Button buttonCadastrar;
    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/%s/json/";
    private static final String MOCKAPI_URL = "https://mockapi.io/api/v1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);
    }
}