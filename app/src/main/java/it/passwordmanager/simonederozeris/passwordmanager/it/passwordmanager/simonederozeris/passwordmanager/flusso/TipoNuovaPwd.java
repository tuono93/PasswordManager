package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso;

public enum TipoNuovaPwd {
    NUOVA(1),
    CONFERMA(2),
    FINISH(3);

    private int step;

    TipoNuovaPwd(int step) {
        this.step = step;
    }

    public int getStep() {
        return step;
    }

    public static TipoNuovaPwd getEnumTipoNuovaPwd(int step) {
        TipoNuovaPwd tipoNuovaPwd = null;
        switch (step) {

            case 1:
                tipoNuovaPwd = TipoNuovaPwd.NUOVA;
                break;
            case 2:
                tipoNuovaPwd = TipoNuovaPwd.CONFERMA;
                break;

            case 3:
                tipoNuovaPwd = TipoNuovaPwd.FINISH;
                break;
        }
        return tipoNuovaPwd;
    }
}
