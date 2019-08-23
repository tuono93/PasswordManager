package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso;

public class FlussoCheckPwd implements Flusso {

    TipoCheckPwd tipoCheckPwd;

    public FlussoCheckPwd() {
        this.tipoCheckPwd = TipoCheckPwd.CHECK;
    }

    public FlussoCheckPwd(int stepCorrente) {
        this.tipoCheckPwd = TipoCheckPwd.getEnumTipoCheckPwd(stepCorrente);
    }

    public TipoCheckPwd getTipoCheckPwd() {
        return tipoCheckPwd;
    }

    public void setTipoCheckPwd(TipoCheckPwd tipoCheckPwd) {
        this.tipoCheckPwd = tipoCheckPwd;
    }

    @Override
    public void goNextStep(int currentStep) {
        currentStep++;
        tipoCheckPwd = TipoCheckPwd.getEnumTipoCheckPwd(currentStep);
    }
    @Override
    public int getCurrentStep() {
        return tipoCheckPwd.getStep();
    }
}
