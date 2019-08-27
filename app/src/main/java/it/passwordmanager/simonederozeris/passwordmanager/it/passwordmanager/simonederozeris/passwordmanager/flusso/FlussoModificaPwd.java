package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso;

public class FlussoModificaPwd implements Flusso {

   TipoNuovaPwd tipoNuovaPwd;
   String value = "";

    public FlussoModificaPwd() {
        this.tipoNuovaPwd = TipoNuovaPwd.NUOVA;
    }

    public FlussoModificaPwd(int stepCorrente,String value) {
        this.tipoNuovaPwd = TipoNuovaPwd.getEnumTipoNuovaPwd(stepCorrente);
        this.value = value;
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

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
