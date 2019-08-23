package it.passwordmanager.simonederozeris.passwordmanager;

import org.junit.Test;

import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.Flusso;
import it.passwordmanager.simonederozeris.passwordmanager.it.passwordmanager.simonederozeris.passwordmanager.flusso.FlussoModificaPwd;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FlussoUnitTest {
    @Test
    public void flussoimpl_isCorrect() {

        Flusso flusso = new FlussoModificaPwd();
        assertEquals(flusso.getClass().getName(), FlussoModificaPwd.class.getName());
    }
}