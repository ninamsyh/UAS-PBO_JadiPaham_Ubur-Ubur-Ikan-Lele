package Model;

public class MateriSkor {
    private final String namaMateri;
    private final double skor;

    public MateriSkor(String namaMateri, double skor) {
        this.namaMateri = namaMateri;
        this.skor = skor;
    }

    public String getNamaMateri() {
        return namaMateri;
    }

    public double getSkor() {
        return skor;
    }
}