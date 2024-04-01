import java.util.concurrent.atomic.AtomicInteger;

class SomaThread implements Runnable {
    private int[] vetor;
    private int inicio;
    private int fim;
    private AtomicInteger resultadoParcial;

    public SomaThread(int[] vetor, int inicio, int fim, AtomicInteger resultadoParcial) {
        this.vetor = vetor;
        this.inicio = inicio;
        this.fim = fim;
        this.resultadoParcial = resultadoParcial;
    }

    @Override
    public void run() {
        int somaParcial = 0;
        for (int i = inicio; i < fim; i++) {
            somaParcial += vetor[i];
        }
        resultadoParcial.addAndGet(somaParcial);
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int tamanho = 100;
        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = i + 1; 
        }

        int numThreads = 4;

        int tamanhoParte = tamanho / numThreads;
        AtomicInteger resultadoParcial = new AtomicInteger(0);

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            int inicio = i * tamanhoParte;
            int fim = (i == numThreads - 1) ? tamanho : (i + 1) * tamanhoParte;
            threads[i] = new Thread(new SomaThread(vetor, inicio, fim, resultadoParcial));
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        System.out.println("Soma total: " + resultadoParcial.get());
    }
}
