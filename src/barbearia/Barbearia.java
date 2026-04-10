package barbearia;

import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.ArrayDeque;

public class Barbearia {
  private Queue<Cliente> filaSofa = new ArrayDeque<>();
  private Queue<Cliente> filaEmPe = new ArrayDeque<>();

  private Semaphore caixa = new Semaphore(1, true);
  private Barbeiro barbeiroCaixa = null;

  private Barbeiro[] barbeiros = new Barbeiro[3];

  private int capacidadeSofa = 4;
  private int clientesDentro = 0;
  private int capacidadeTotal = 20;

  public Barbearia() {
    for (int i = 0; i < barbeiros.length; i++) {
      barbeiros[i] = new Barbeiro("B " + (i + 1), this);
      barbeiros[i].start();
    }
  }

  public synchronized boolean entrar(Cliente cliente) {
    if (clientesDentro >= capacidadeTotal) {
      return false;
    }

    clientesDentro++;

    if (filaSofa.size() < capacidadeSofa) {
      filaSofa.add(cliente);
    } else {
      filaEmPe.add(cliente);
    }

    this.imprimirBarbearia();
    notify();

    return true;
  }

  public synchronized void sair() {
    clientesDentro--;
    this.imprimirBarbearia();
  }

  public synchronized Cliente atenderCliente() {
    if (filaSofa.isEmpty()) {
      return null;
    }

    Cliente cliente = filaSofa.poll();

    Cliente clienteParaSofa = filaEmPe.poll();

    if (clienteParaSofa != null) {
      filaSofa.add(clienteParaSofa);
    }

    return cliente;
  }

  public Semaphore getCaixa() {
    return caixa;
  }

  public void setBarbeiroCaixa(Barbeiro barbeiro) {
    this.barbeiroCaixa = barbeiro;
  }


  // ==== Helpers ====

  public void cleanConsole() {
    try {
      final String os = System.getProperty("os.name");

      if (os.contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        new ProcessBuilder("clear").inheritIO().start().waitFor();
      }
    } catch (final Exception e) {
      System.out.print("\033[H\033[2J");
      System.out.flush();
    }
  }

  // Imprimir barbearia
  public synchronized void imprimirBarbearia() {
    cleanConsole();
    System.out.println("========= 💈 BARBEARIA HILZER 💈 =========");
    System.out.println("   Lotação: [" + clientesDentro + "/" + capacidadeTotal + "]"); 
    System.out.println("==========================================\n");

    System.out.print("🪑 Sofá: ");
    for (Cliente c : filaSofa) {
      System.out.print("[" + c.getName() + "]");
    }
    for (int i = filaSofa.size(); i < 4; i++) {
      System.out.print("[  ]");
    }
    System.out.println("\n");

    System.out.print("🧍 Em pé: ");
    for (Cliente c : filaEmPe) {
      System.out.print("[" + c.getName() + "]");
    }
    System.out.println("\n");

    System.out.println("✂️ Barbeiros:");
    for (Barbeiro b : barbeiros) {
      if (this.barbeiroCaixa != b) {
        System.out.println(b.getName() + " - " + b.getStatus() + (b.getClienteAtual() != null ? " (" + b.getClienteAtual().getName() + ")" : ""));
      }
    }

    System.out.println("\n💰 Caixa:");
    if (barbeiroCaixa != null) {
      System.out.println(barbeiroCaixa.getName() + " - " + barbeiroCaixa.getStatus() + (barbeiroCaixa.getClienteAtual() != null ? " (" + barbeiroCaixa.getClienteAtual().getName() + ")" : ""));
    } else {
      System.out.println("Caixa vazio");
    }

    System.out.println("\n=========================================");
  }
}
