import java.util.concurrent.CompletableFuture;

class EquipeLanches {
    public CompletableFuture<String> getPipoca() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Pipoca Pronta";
        });
    }

    public CompletableFuture<String> getRefrigerante() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Refrigerante Pronto";
        });
    }
}

public class Cinema {
    private EquipeLanches equipe = new EquipeLanches();

    public CompletableFuture<Void> pedirLanche() {
        CompletableFuture<String> pipocaFuture = equipe.getPipoca();
        CompletableFuture<String> refrigeranteFuture = equipe.getRefrigerante();

        return pipocaFuture.thenCombine(refrigeranteFuture, (pipoca, refrigerante) -> {
            System.out.println(pipoca);
            System.out.println(refrigerante);
            return null;
        });
    }

    public static void main(String[] args) {
        Cinema cinema = new Cinema();

        CompletableFuture<Void> pedidoLanche = cinema.pedirLanche();

        pedidoLanche.join();

        System.out.println("Lanche pronto, aproveite o filme!");
    }
}
