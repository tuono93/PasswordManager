package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso;

public enum TipoStatoPwd {
    OK("OK"),
    ERR("ERR");

    private String tipoStato;

    TipoStatoPwd(String tipoStato) {
        this.tipoStato = tipoStato;
    }

    public String getTipoStato() {
        return tipoStato;
    }

    public static TipoStatoPwd getEnumStatoGestione(String tipoGestione) {
        TipoStatoPwd tipoGestionePwd = null;
        switch (tipoGestione) {
            case "OK":
                tipoGestionePwd = TipoStatoPwd.OK;
                break;
            case "ERR":
                tipoGestionePwd = TipoStatoPwd.ERR;
                break;
        }
        return tipoGestionePwd;
    }
}
