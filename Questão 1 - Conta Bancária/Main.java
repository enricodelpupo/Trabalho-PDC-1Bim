import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Conta {
    private int numeroConta;
    private String titularConta;
    private double saldo;
    private final Lock lock = new ReentrantLock();

    public Conta(int numeroConta, String titularConta, double saldoInicial) {
        this.numeroConta = numeroConta;
        this.titularConta = titularConta;
        this.saldo = saldoInicial;
    }

    public int getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(int numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getTitularConta() {
        return titularConta;
    }

    public void setTitularConta(String titularConta) {
        this.titularConta = titularConta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void deposito(double valor) {
        lock.lock();
        try {
            saldo += valor;
            System.out.println("Depósito de R$" + valor + " realizado. Saldo atual: R$" + saldo);
        } finally {
            lock.unlock();
        }
    }

    public void saque(double valor) {
        lock.lock();
        try {
            if (saldo >= valor) {
                saldo -= valor;
                System.out.println("Saque de R$" + valor + " realizado. Saldo atual: R$" + saldo);
            } else {
                System.out.println("Saldo insuficiente para o saque de R$" + valor);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Conta{" +
                "numeroConta=" + numeroConta +
                ", titularConta='" + titularConta + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}

class AGastadora extends Thread {
    private final Conta conta;
    private int saquesRealizados = 0;
    private double valorTotalSacado = 0;

    public AGastadora(Conta conta) {
        this.conta = conta;
    }

    @Override
    public void run() {
        while (conta.getSaldo() > 0) {
            try {
                Thread.sleep(3000);
                conta.saque(10);
                saquesRealizados++;
                valorTotalSacado += 10;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getSaquesRealizados() {
        return saquesRealizados;
    }

    public double getValorTotalSacado() {
        return valorTotalSacado;
    }
}

class AEsperta extends Thread {
    private final Conta conta;
    private int saquesRealizados = 0;
    private double valorTotalSacado = 0;

    public AEsperta(Conta conta) {
        this.conta = conta;
    }

    @Override
    public void run() {
        while (conta.getSaldo() > 0) {
            try {
                Thread.sleep(6000);
                conta.saque(50);
                saquesRealizados++;
                valorTotalSacado += 50;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getSaquesRealizados() {
        return saquesRealizados;
    }

    public double getValorTotalSacado() {
        return valorTotalSacado;
    }
}

class AEconomica extends Thread {
    private final Conta conta;
    private int saquesRealizados = 0;
    private double valorTotalSacado = 0;

    public AEconomica(Conta conta) {
        this.conta = conta;
    }

    @Override
    public void run() {
        while (conta.getSaldo() > 0) {
            try {
                Thread.sleep(12000);
                conta.saque(5);
                saquesRealizados++;
                valorTotalSacado += 5;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getSaquesRealizados() {
        return saquesRealizados;
    }

    public double getValorTotalSacado() {
        return valorTotalSacado;
    }
}

class APatrocinadora extends Thread {
    private final Conta conta;

    public APatrocinadora(Conta conta) {
        this.conta = conta;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); 
                if (conta.getSaldo() == 0) {
                    conta.deposito(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Conta conta = new Conta(123456, "João", 1000.0);

        AGastadora gastadora = new AGastadora(conta);
        AEsperta esperta = new AEsperta(conta);
        AEconomica economica = new AEconomica(conta);
        APatrocinadora patrocinadora = new APatrocinadora(conta);

        gastadora.start();
        esperta.start();
        economica.start();
        patrocinadora.start();

        while (conta.getSaldo() > 0) {
            try {
                Thread.sleep(20000);
                System.out.println("Saques realizados pela AGastadora: " + gastadora.getSaquesRealizados());
                System.out.println("Saques realizados pela AEsperta: " + esperta.getSaquesRealizados());
                System.out.println("Saques realizados pela AEconomica: " + economica.getSaquesRealizados());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
}
