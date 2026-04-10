package barbearia;

public class Cliente extends Thread {
  private Barbearia barbearia;

  public Cliente(String name, Barbearia barbearia) {
    super(name);
    this.barbearia = barbearia;
  }

  public void run() {
    barbearia.entrar(this);
  }
}
