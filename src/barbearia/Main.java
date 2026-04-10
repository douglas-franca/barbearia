package barbearia;

import java.util.Random;

public class Main {
  public static void main(String[] args) {
    Barbearia barbearia = new Barbearia();
    final int[] contador = {1};

    Random random = new Random();

    Thread gerador = new Thread(() -> {
      while (true) {
        try {
          Cliente cliente = new Cliente("Cliente-" + contador[0]++, barbearia);
          cliente.start();

          Thread.sleep(500 + random.nextInt(1000));

        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          // break;
        }
      }
    });
    gerador.start();
  }
}
