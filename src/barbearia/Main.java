package barbearia;

import java.util.Random;

public class Main {
  private static final int TEMPO_GERACAO_MIN = 500;
  private static final int TEMPO_GERACAO_MAX = 1500;

  public static void main(String[] args) {

    Barbearia barbearia = new Barbearia();
    final int[] contador = {1};

    Random random = new Random();

    Thread gerador = new Thread(() -> {
      while (true) {
        try {
          Cliente cliente = new Cliente("Cliente-" + contador[0]++, barbearia);
          cliente.start();

          Thread.sleep(TEMPO_GERACAO_MIN + random.nextInt(TEMPO_GERACAO_MAX - TEMPO_GERACAO_MIN));

        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    });
    gerador.start();
  }
}
