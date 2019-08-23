package it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso;

public enum TipoCheckPwd {
        CHECK(1),
        FINISH(2);

        private int step;

    TipoCheckPwd(int step) {
            this.step = step;
        }

        public int getStep() {
            return step;
        }

        public static TipoCheckPwd getEnumTipoCheckPwd(int step) {
            TipoCheckPwd tipoNuovaPwd = null;
            switch (step) {
                case 1:
                    tipoNuovaPwd = TipoCheckPwd.CHECK;
                    break;
                case 2:
                    tipoNuovaPwd = TipoCheckPwd.FINISH;
                    break;
            }
            return tipoNuovaPwd;
        }
 }
