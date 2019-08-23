package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso;

public class FlussoModificaPwd implements Flusso {

   TipoNuovaPwd tipoNuovaPwd;

    public FlussoModificaPwd() {
        this.tipoNuovaPwd = TipoNuovaPwd.NUOVA;
    }

    public FlussoModificaPwd(int stepCorrente) {
        this.tipoNuovaPwd = TipoNuovaPwd.getEnumTipoNuovaPwd(stepCorrente);
    }

    public TipoNuovaPwd getTipoNuovaPwd() {
        return tipoNuovaPwd;
    }

    public void setTipoNuovaPwd(TipoNuovaPwd tipoNuovaPwd) {
        this.tipoNuovaPwd = tipoNuovaPwd;
    }

    @Override
    public void goNextStep(int currentStep) {
        currentStep++;
        tipoNuovaPwd = TipoNuovaPwd.getEnumTipoNuovaPwd(currentStep);
    }

    @Override
    public int getCurrentStep() {
        return tipoNuovaPwd.getStep();
    }
}
