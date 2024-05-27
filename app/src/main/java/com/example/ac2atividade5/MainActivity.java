package com.example.ac2atividade5;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ac2atividade5.adapter.AlunoAdapter;
import com.example.ac2atividade5.api.AlunoService;
import com.example.ac2atividade5.api.ApiClient;
import com.example.ac2atividade5.model.Aluno;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerUsuario;
    AlunoAdapter alunoAdapter;
    AlunoService apiService;
    List<Aluno> listaAlunos;

    FloatingActionButton btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerUsuario = (RecyclerView) findViewById(R.id.recyclerUsuario);
        listaAlunos = new ArrayList<>();
        btnAdd = (FloatingActionButton)findViewById(R.id.btnAdd);
        apiService = ApiClient.getUsuarioService();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,cadastro_aluno.class);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        obterUsuarios();
    }
    private void configurarRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerUsuario.setLayoutManager(layoutManager);
        alunoAdapter = new AlunoAdapter(listaAlunos, this);
        recyclerUsuario.setAdapter(alunoAdapter);
        recyclerUsuario.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void obterUsuarios() {
        retrofit2.Call<List<Aluno>> call = apiService.getAlunos();
        call.enqueue(new Callback<List<Aluno>>() {
            @Override
            public void onResponse(Call<List<Aluno>> call, Response<List<Aluno>>
                    response) {
                listaAlunos = response.body();
                configurarRecycler();
            }
            @Override
            public void onFailure(Call<List<Aluno>> call, Throwable t) {
                Log.e("TESTE", "Erro ao obter os contatos: " + t.getMessage());
            }
        });
    }

}