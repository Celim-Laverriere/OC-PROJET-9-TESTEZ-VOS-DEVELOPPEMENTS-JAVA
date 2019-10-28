package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ComptabiliteManagerImplIntegrationTest extends BusinessTestCase {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();


    @Test
    public void checkGetListJournalComptable() {
        List<JournalComptable> journalComptableList = manager.getListJournalComptable();

        Assertions.assertThat(!journalComptableList.isEmpty());
    }

    @Test
    public void checkInsertEcritureComptable() throws FunctionalException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("VE", "Vente"));
        vEcritureComptable.setReference("VE-2019/00006");
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("TMA Appli Yyy");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                                                                                null, new BigDecimal(123),
                                                                                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(706),
                                                                                            null, null,
                                                                                            new BigDecimal(123)));

        manager.insertEcritureComptable(vEcritureComptable);
    }

    @Test
    public void checkUpdateEcritureComptable() throws FunctionalException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(-1);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setReference("AC-2016/00001");
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Cartouches d’imprimante");

        manager.updateEcritureComptable(vEcritureComptable);
    }

    @Test
    public void checkdeleteEcritureComptable() {
        Integer pId = 12;
        manager.deleteEcritureComptable(pId);
    }


}
