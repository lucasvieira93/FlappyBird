package com.lucasvieira.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture[] passaro;
    private Texture fundo;
    private Texture canoBaixo;
    private Texture canoTopo;


    //atributos de configuração
    private int larguraDispositivo;
    private int alturaDispositivo;
    private int pontuacao = 0;
    private boolean estadoJogo = false;
    private Random numeroRandomico;

    private float variacao = 0;
    private float velocidadeQueda = 0;
    private float velocidadeCano = 300;
    private float posicaoInicialVertical;
    private float posicaoMovimentoCanoHorizontal;
    private float espacoEntreCanos;
    private float deltaTime;
    private float alturaAleatoria;

    @Override
    public void create() {

        batch = new SpriteBatch();
        passaro = new Texture[3];
        numeroRandomico = new Random();
        passaro[0] = new Texture("passaro1.png");
        passaro[1] = new Texture("passaro2.png");
        passaro[2] = new Texture("passaro3.png");

        fundo = new Texture("fundo.png");
        canoBaixo = new Texture("cano_baixo_maior.png");
        canoTopo = new Texture("cano_topo_maior.png");

        larguraDispositivo = Gdx.graphics.getWidth();
        alturaDispositivo = Gdx.graphics.getHeight();

        posicaoInicialVertical = alturaDispositivo / 2;
        posicaoMovimentoCanoHorizontal = larguraDispositivo;
        espacoEntreCanos = 350;
    }

    @Override
    public void render() {
        //renderização no celular
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        deltaTime = Gdx.graphics.getDeltaTime();
        variacao += deltaTime * 10;

        if (estadoJogo == false) {
            if (Gdx.input.justTouched()) {
                estadoJogo = true;
            }

        } else {

            velocidadeQueda++;
            posicaoMovimentoCanoHorizontal -= deltaTime * velocidadeCano;

            //Sprite variando
            if (variacao > 3) variacao = 0;

            //toque
            if (Gdx.input.justTouched()) {
                velocidadeQueda = -20;
            }

            //gravidade
            if (posicaoInicialVertical > 0 || velocidadeQueda < 0) {
                posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;
            }

            //movimentação dos canos
            if (posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()) {
                posicaoMovimentoCanoHorizontal = larguraDispositivo;
                alturaAleatoria = numeroRandomico.nextInt(400) - 200;
                velocidadeCano += 30;
                pontuacao++;
            }

            batch.begin();

            batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
            batch.draw(passaro[(int) variacao], 100, posicaoInicialVertical);
            batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaAleatoria);
            batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaAleatoria);

            batch.end();
        }

    }

}
