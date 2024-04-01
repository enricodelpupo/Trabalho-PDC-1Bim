import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Aluno {
    private String matricula;
    private String nome;
    private String curso;
    private String flag;

    public Aluno(String matricula, String nome, String curso, String flag) {
        this.matricula = matricula;
        this.nome = nome;
        this.curso = curso;
        this.flag = flag;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNome() {
        return nome;
    }

    public String getCurso() {
        return curso;
    }

    public String getFlag() {
        return flag;
    }
}

class ProcessadorArquivo implements Runnable {
    private File arquivo;

    public ProcessadorArquivo(File arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(arquivo));
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length == 4 && partes[3].trim().equals("CONCLUÍDO")) {
                    System.out.println("Aluno formando: " + partes[1] + " - Curso: " + partes[2]);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        File pastaCursos = new File("C:\Documentos");
        File[] arquivosCursos = pastaCursos.listFiles();
        List<Runnable> processadores = new ArrayList<>();

        if (arquivosCursos != null) {
            for (File arquivo : arquivosCursos) {
                processadores.add(new ProcessadorArquivo(arquivo));
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (Runnable processador : processadores) {
            executor.execute(processador);
        }

        executor.shutdown();
    }
}
