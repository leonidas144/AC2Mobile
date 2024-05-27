package com.example.ac2atividade5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ac2atividade5.api.AlunoService;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class cadastro_aluno extends AppCompatActivity {

    AlunoService apiService;
    private EditText editTextNome, editTextRA, editTextCEP, editTextLogradouro, editTextComplemento, editTextBairro, editTextCidade, editTextUF;
    private Button buttonCadastrar;
    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/%s/json/";
    private static final String MOCKAPI_URL = "https://664291f53d66a67b3437a949.mockapi.io/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_aluno);

        editTextNome = findViewById(R.id.editTextNome);
        editTextRA = findViewById(R.id.editTextRA);
        editTextCEP = findViewById(R.id.editTextCEP);
        editTextLogradouro = findViewById(R.id.editTextLogradouro);
        editTextComplemento = findViewById(R.id.editTextComplemento);
        editTextBairro = findViewById(R.id.editTextBairro);
        editTextCidade = findViewById(R.id.editTextCidade);
        editTextUF = findViewById(R.id.editTextUF);

        buttonCadastrar = findViewById(R.id.buttonCadastrar);
        /*buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Aluno aluno = new Aluno();
                aluno.setRa(editTextRA.getText().toString());
                aluno.setNome(editTextNome.getText().toString());
                aluno.setCep(editTextCEP.getText().toString());
                aluno.setComplemento(editTextComplemento.toString());
                aluno.setBairro(editTextBairro.toString());
                aluno.setCidade(editTextCidade.toString());
                aluno.setUf(editTextUF.toString());
                inserirAluno(aluno);

            }
        });*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOCKAPI_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(AlunoService.class);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarAluno();
            }
        });

        // Listener para buscar o endereço ao preencher o CEP
        editTextCEP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    buscarEnderecoPorCEP();
                }
            }
        });
    }

    private void buscarEnderecoPorCEP() {
        String cep = editTextCEP.getText().toString().trim();
        if (cep.length() == 8) { // O CEP no Brasil tem 8 dígitos
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            new BuscarEnderecoTask().execute(url);
        } else {
            Toast.makeText(this, "CEP inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void cadastrarAluno() {
        // Obter dados do formulário
        String ra = editTextRA.getText().toString();
        String nome = editTextNome.getText().toString();
        String cep = editTextCEP.getText().toString();
        String logradouro = editTextLogradouro.getText().toString();
        String complemento = editTextComplemento.getText().toString();
        String bairro = editTextBairro.getText().toString();
        String cidade = editTextCidade.getText().toString();
        String uf = editTextUF.getText().toString();

        // Criar objeto Aluno
        Aluno aluno = new Aluno(ra, nome, cep, logradouro, complemento, bairro, cidade, uf);

        Call<Aluno> call = apiService.postAluno(aluno);
        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if(response.isSuccessful()){
                    Toast.makeText(cadastro_aluno.this,"Aluno cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Log.e("Inserir", "Erro ao criar: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                Log.e("Inserir", "Erro ao criar: " + t.getMessage());
            }
        });
    }

    private void inserirAluno(Aluno aluno) {
        Call<Aluno> call = apiService.postAluno(aluno);
        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {
                    // A solicitação foi bem-sucedida
                    Aluno createdPost = response.body();
                    Toast.makeText(cadastro_aluno.this, "Inserido com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // A solicitação falhou
                    Log.e("Inserir", "Erro ao criar: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                // Ocorreu um erro ao fazer a solicitação
                Log.e("Inserir", "Erro ao criar: " + t.getMessage());
            }
        });
    }

    // AsyncTask para buscar endereço por CEP
    private class BuscarEnderecoTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                //InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    editTextLogradouro.setText(jsonObject.getString("logradouro"));
                    editTextComplemento.setText(jsonObject.getString("complemento"));
                    editTextBairro.setText(jsonObject.getString("bairro"));
                    editTextCidade.setText(jsonObject.getString("localidade"));
                    editTextUF.setText(jsonObject.getString("uf"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(cadastro_aluno.this, "Erro ao buscar endereço", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // AsyncTask para salvar aluno
    private static void cadastrarAluno(Aluno aluno) throws IOException {
        String urlStr = MOCKAPI_URL + "aluno";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        // Converter aluno para JSON
        JSONObject alunoJson = new JSONObject((Map) aluno);

        conn.getOutputStream().write(alunoJson.toString().getBytes());

        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new RuntimeException("Falha ao cadastrar aluno: Código de erro HTTP " + conn.getResponseCode());
        }

        System.out.println("Aluno cadastrado com sucesso!");
        conn.disconnect();
    }
}
