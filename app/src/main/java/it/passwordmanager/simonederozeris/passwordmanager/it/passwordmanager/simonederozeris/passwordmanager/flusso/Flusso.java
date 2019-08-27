package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso;

public interface Flusso {
    public void goNextStep(int currentStep);
    public int getCurrentStep();
    public String getValue();
    public void setValue(String value);

}
