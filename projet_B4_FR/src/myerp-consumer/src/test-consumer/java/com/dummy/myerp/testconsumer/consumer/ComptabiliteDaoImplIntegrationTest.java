package com.dummy.myerp.testconsumer.consumer;

import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ComptabiliteDaoImplIntegrationTest extends ConsumerTestCase {

    private ComptabiliteDaoImpl comptabiliteDao = ComptabiliteDaoImpl.getInstance();


    @Test
    public void checkGetListCompteComptable() {
        List<CompteComptable> compteComptableList = comptabiliteDao.getListCompteComptable();
        Assertions.assertThat(compteComptableList).isNotEmpty();
    }

    @Test
    public void checkGetListJournalComptable() {
        List<JournalComptable> journalComptableList = comptabiliteDao.getListJournalComptable();
        Assertions.assertThat(journalComptableList.isEmpty());
    }

    @Test
    public void checkGetListEcritureComptable() {
        List<EcritureComptable>  ecritureComptableList = comptabiliteDao.getListEcritureComptable();
        Assertions.assertThat(ecritureComptableList.isEmpty());
    }

    @Test
    public void checkGetEcritureComptablee() throws NotFoundException {
       EcritureComptable ecritureComptable = comptabiliteDao.getEcritureComptable(-1);
       Assertions.assertThat(ecritureComptable.getId()).isEqualTo(-1);
       Assertions.assertThat(ecritureComptable.getJournal().getCode()).isEqualTo("AC");
       Assertions.assertThat(ecritureComptable.getReference()).isEqualTo("AC-2016/00001");
       Assertions.assertThat(ecritureComptable.getDate()).isEqualTo("2019-10-28 00:00:00.000000");
       Assertions.assertThat(ecritureComptable.getLibelle()).isEqualTo("Cartouches d’imprimante");
    }

    @Test
    public void checkGetEcritureComptableByRef() throws NotFoundException {
        EcritureComptable vEcritureComptable = comptabiliteDao.getEcritureComptableByRef("BQ-2016/00005");
        Assertions.assertThat(vEcritureComptable.getId()).isEqualTo(-5);
        Assertions.assertThat(vEcritureComptable.getListLigneEcriture()).isNotEmpty();
    }

    @Test
    public void checkInsertEcritureComptable() {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setReference("AC-2019/00006");
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Ordinateurs");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(512),
                                                                            "Clients", new BigDecimal(500),
                                                                            null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                                                                                        "Banque", null,
                                                                                        new BigDecimal(500)));

        comptabiliteDao.insertEcritureComptable(vEcritureComptable);
        Assertions.assertThat(vEcritureComptable.getId()).isEqualTo(vEcritureComptable.getId());
        Assertions.assertThat(vEcritureComptable.getListLigneEcriture()).isNotEmpty();
    }

    @Test
    public void checkUpdateEcritureComptable() {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(28);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setReference("AC-2019/00007");
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Ordinateurs");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(512),
                                                                            "Clients", new BigDecimal(550),
                                                                            null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                                                                                        "Banque", null,
                                                                                        new BigDecimal(550)));

        comptabiliteDao.updateEcritureComptable(vEcritureComptable);
    }

    @Test(expected = NotFoundException.class)
    public void checkDeleteEcritureComptable() throws NotFoundException {
        Integer pId = 28;

        comptabiliteDao.deleteEcritureComptable(pId);
        Assertions.assertThat( comptabiliteDao.getEcritureComptable(28)).isEqualTo(null);
    }

    @Test
    public void checkGetLastValueSequenceEcritureComptableforYear() throws NotFoundException {
        SequenceEcritureComptable sequenceEcritureComptable = comptabiliteDao.
                getLastValueSequenceEcritureComptableforYear("AC", 2016);

        Assertions.assertThat(sequenceEcritureComptable.getDerniereValeur()).isEqualTo(40);
    }
}
