package barbearia;

public class Barbeiro extends Thread {
  private Barbearia barbearia;
  private String status = "Disponível";

  // private int tempoCorte = (int)(Math.random() * 10000) + 5000;

  private Cliente clienteAtual;

  public Barbeiro(String name, Barbearia barbearia) {
    super(name);
    this.barbearia = barbearia;
  }

  private void cortarCabelo() {
    this.status = "Cortando";
    this.barbearia.imprimirBarbearia();

    try {
      Thread.sleep((long) (Math.random() * 10000 + 5000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void receberPagamento() {
    this.status = "Esperando pagamento";
    this.barbearia.imprimirBarbearia();

    try {
      barbearia.getCaixa().acquire();

      this.status = "Recebendo pagamento";
      this.barbearia.setBarbeiroCaixa(this);
      this.barbearia.imprimirBarbearia();

      Thread.sleep((long) (Math.random() * 5000 + 2000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      barbearia.setBarbeiroCaixa(null);
      barbearia.getCaixa().release();
      barbearia.sair();
      this.barbearia.imprimirBarbearia();
    }
  }

  public void run() {
    while (true) {
      clienteAtual = null;
      this.status = "Disponível";
      this.barbearia.imprimirBarbearia();

      synchronized (barbearia) {
        try {
          while ((clienteAtual = barbearia.atenderCliente()) == null) {
            barbearia.wait();
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }

      // Start cutting hair
      cortarCabelo();

      // After cutting hair, receive payment
      receberPagamento();
    }
  }

  public String getStatus() {
    return status;
  }

  public Cliente getClienteAtual() {
    return clienteAtual;
  }
}
