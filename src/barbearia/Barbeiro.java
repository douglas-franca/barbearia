package barbearia;

public class Barbeiro extends Thread {
  private static final int TEMPO_CORTE_MIN = 5000;
  private static final int VARIACAO_TEMPO_CORTE = 10000;
  private static final int TEMPO_PAGAMENTO_MIN = 2000;
  private static final int VARIACAO_TEMPO_PAGAMENTO = 3000;

  private Barbearia barbearia;
  private String status = "Disponível";
  private Cliente clienteAtual;

  public Barbeiro(String name, Barbearia barbearia) {
    super(name);
    this.barbearia = barbearia;
  }

  private void cortarCabelo() {
    this.status = "Cortando";
    this.barbearia.imprimirBarbearia();

    try {
      Thread.sleep((long) (Math.random() * VARIACAO_TEMPO_CORTE + TEMPO_CORTE_MIN));
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

      Thread.sleep((long) (Math.random() * VARIACAO_TEMPO_PAGAMENTO + TEMPO_PAGAMENTO_MIN));
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
