package com.lucasvieira.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture[] passaro;
    private Texture fundo;
    private Texture canoBaixo;
    private Texture canoTopo;
    private Texture gameOver;
    private Circle passaroCirculo;
    private Rectangle retanguloCanoTopo;
    private Rectangle retanguloCanoBaixo;
//    private ShapeRenderer shape;

    //atributos de configuração
    private float larguraDispositivo;
    private float alturaDispositivo;
    private int pontuacao = 0;
    private int estadoJogo = 0; //0 = Jogo Parado, 1 = Jogo em Andamento, 2 = Game Over
    private Random numeroRandomico;
    private BitmapFont fonte;
    private BitmapFont mensagem;

    private float variacao = 0;
    private float velocidadeQueda = 0;
    private float velocidadeCano = 300;
    private float posicaoInicialVertical;
    private float posicaoMovimentoCanoHorizontal;
    private float espacoEntreCanos;
    private float deltaTime;
    private float alturaAleatoria;

    //Câmera
    private OrthographicCamera camera;
    private Viewport viewport;
    private final float VIRTUAL_WIDTH = 768;
    private final float VIRTUAL_HEIGHT = 1024;

    @Override
    public void create() {

        batch = new SpriteBatch();
        numeroRandomico = new Random();
        passaroCirculo = new Circle();
//        retanguloCanoBaixo = new Rectangle();
//        retanguloCanoTopo = new Rectangle();
//        shape = new ShapeRenderer();

        fonte = new BitmapFont();
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(3);

        mensagem = new BitmapFont();
        mensagem.setColor(Color.WHITE);
        mensagem.getData().setScale(3);

        passaro = new Texture[4];
        passaro[0] = new Texture("passaro1.png");
        passaro[1] = new Texture("passaro2.png");
        passaro[2] = new Texture("passaro3.png");
        passaro[3] = new Texture("passaro2.png");

        //config da camera
        camera = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH/2, VIRTUAL_HEIGHT/2, 0);
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        fundo = new Texture("fundo.png");
        gameOver = new Texture("game_over.png");
        canoBaixo = new Texture("cano_baixo_maior.png");
        canoTopo = new Texture("cano_topo_maior.png");

        larguraDispositivo = VIRTUAL_WIDTH;
        alturaDispositivo = VIRTUAL_HEIGHT;

        posicaoInicialVertical = alturaDispositivo / 2;
        posicaoMovimentoCanoHorizontal = larguraDispositivo;
        espacoEntreCanos = 230;
    }

    @Override
    public void render() {

        //renderização no celular
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        deltaTime = Gdx.graphics.getDeltaTime();
        variacao += deltaTime * 15;
        //Sprite variando
        if (variacao > 4) variacao = 0;

        if (estadoJogo == 0) {
            if (Gdx.input.justTouched()) {
                estadoJogo = 1;
            }

        } if (estadoJogo == 1) {
            velocidadeQueda++;
            posicaoMovimentoCanoHorizontal -= deltaTime * velocidadeCano;

            //toque
            if (Gdx.input.justTouched()) {
                velocidadeQueda = -15;
            }

            //gravidade
            if (posicaoInicialVertical > 0 || velocidadeQueda < 0) {
                posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;
            }

            //movimentação dos canos
            if (posicaoMovimentoCanoHorizontal <- canoTopo.getWidth()) {
                posicaoMovimentoCanoHorizontal = larguraDispositivo;
                alturaAleatoria = numeroRandomico.nextInt(600) - 300;
                velocidadeCano += 30;
                pontuacao++;
            }

            //desenhar formas - (comentado para teste visual se as formas estavam em cima dos sprites)
//            shape.begin(ShapeRenderer.ShapeType.Filled);
//            shape.circle(passaroCirculo.x, passaroCirculo.y, passaroCirculo.radius);
//            shape.rect(retanguloCanoBaixo.x, retanguloCanoBaixo.y, retanguloCanoBaixo.width, retanguloCanoBaixo.height);
//            shape.rect(retanguloCanoTopo.x, retanguloCanoTopo.y, retanguloCanoTopo.width, retanguloCanoTopo.height);
//            shape.setColor(Color.RED);
//            shape.end();
        }

        // configurar dados de projeção da camera
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(passaro[(int) variacao], 100, posicaoInicialVertical);
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaAleatoria);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaAleatoria);
        fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo/2, alturaDispositivo - 100);

        if (estadoJogo == 2) { // Game Over
            batch.draw(gameOver, larguraDispositivo/2 - gameOver.getWidth()/2, alturaDispositivo/2);
            fonte.draw(batch, "Toque para reiniciar!", larguraDispositivo/2 - 200, alturaDispositivo / 2 - gameOver.getHeight() / 2);

            if (Gdx.input.justTouched()){
                estadoJogo = 0;
                pontuacao = 0;
                velocidadeQueda = 0;
                posicaoInicialVertical = alturaDispositivo / 2;
                posicaoMovimentoCanoHorizontal = larguraDispositivo;
            }
        }

        batch.end();

        //lógica das formas
        passaroCirculo.set(100 + passaro[0].getWidth() / 2 , posicaoInicialVertical + 15, passaro[0].getWidth() / 2);
        retanguloCanoBaixo = new Rectangle(
                posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaAleatoria,
                canoBaixo.getWidth(), canoBaixo.getHeight()
        );

        retanguloCanoTopo = new Rectangle(
                posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaAleatoria,
                canoTopo.getWidth(), canoTopo.getHeight()
        );

        //teste de colisão
        if (Intersector.overlaps(passaroCirculo, retanguloCanoBaixo) || Intersector.overlaps(passaroCirculo, retanguloCanoTopo) || posicaoInicialVertical <= 0 || posicaoInicialVertical >= alturaDispositivo){
            estadoJogo = 2;
            posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;
            velocidadeQueda++;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
