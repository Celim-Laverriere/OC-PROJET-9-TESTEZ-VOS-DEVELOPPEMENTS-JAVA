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

    /**
     * Test si la méthode renvoie bien la liste des comptes comptables
     * @see ComptabiliteDaoImpl#getListCompteComptable()
     */
    @Test
    public void checkGetListCompteComptable() {
        List<CompteComptable> compteComptableList = comptabiliteDao.getListCompteComptable();
        Assertions.assertThat(compteComptableList).isNotEmpty();
    }

    /**
     * Test si la méthode renvoie bien la liste des journals comptable
     * @see ComptabiliteDaoImpl#getListJournalComptable()
     */
    @Test
    public void checkGetListJournalComptable() {
        List<JournalComptable> journalComptableList = comptabiliteDao.getListJournalComptable();
        Assertions.assertThat(journalComptableList.isEmpty());
    }

    /**
     * Test si la méthode renvoie bien la liste des écritures comptables
     * @see ComptabiliteDaoImpl#getListEcritureComptable()
     */
    @Test
    public void checkGetListEcritureComptable() {
        List<EcritureComptable>  ecritureComptableList = comptabiliteDao.getListEcritureComptable();
        Assertions.assertThat(ecritureComptableList.isEmpty());
    }

    /**
     * Test si la méthode renvoie bien l'écriture comptable pour l'identifiant passé en paramètre
     * @see ComptabiliteDaoImpl#getEcritureComptable(Integer) 
     * @throws NotFoundException
     */
    @Test
    public void checkGetEcritureComptable() throws NotFoundException {
       EcritureComptable ecritureComptable = comptabiliteDao.getEcritureComptable(-4);
       Assertions.assertThat(ecritureComptable.getId()).isEqualTo(-4);
       Assertions.assertThat(ecritureComptable.getJournal().getCode()).isEqualTo("VE");
       Assertions.assertThat(ecritureComptable.getReference()).isEqualTo("VE-2016/00004");
       Assertions.assertThat(ecritureComptable.getDate()).isEqualTo("2016-12-28 00:00:00.000000");
       Assertions.assertThat(ecritureComptable.getLibelle()).isEqualTo("TMA Appli Yyy");
    }

    /**
     * TTest si la méthode renvoie bien l'écriture comptable pour la référence passée en paramètre
     * @see ComptabiliteDaoImpl#getEcritureComptableByRef(String)
     * @throws NotFoundException
     */
    @Test
    public void checkGetEcritureComptableByRefNominal() throws NotFoundException {
        EcritureComptable vEcritureComptable = comptabiliteDao.getEcritureComptableByRef("BQ-2016/00005");
        Assertions.assertThat(vEcritureComptable.getId()).isEqualTo(-5);
        Assertions.assertThat(vEcritureComptable.getListLigneEcriture()).isNotEmpty();
        Assertions.assertThat(vEcritureComptable.getListLigneEcriture().size() > 1);
    }

    /**
     * Test le déclenchement de l'exception quand aucune référence n'a été trouvé pour une écriture comptable
     * @throws NotFoundException
     */
    @Test(expected = NotFoundException.class)
    public void checkGetEcritureComptableByRef() throws NotFoundException {
        EcritureComptable vEcritureComptable = comptabiliteDao.getEcritureComptableByRef("DE-2016/00005");
    }

    /**
     * Test l'insertion d'une écriture comptable dans la base de données
     * @see ComptabiliteDaoImpl#insertEcritureComptable(EcritureComptable)
     */
    @Test
    public void checkInsertEcritureComptable() {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setReference("AC-2019/00007");
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
        Assertions.assertThat(vEcritureComptable.getListLigneEcriture().size() > 1);
    }

    /**
     * Test la mise à jour d'une écriture comptable dans la base de données
     * @see ComptabiliteDaoImpl#updateEcritureComptable(EcritureComptable)
     * @throws NotFoundException
     */
    @Test
    public void checkUpdateEcritureComptable() throws NotFoundException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(-1);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setReference("AC-2019/00006");
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Portable");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(512),
                                                                            "Clients", new BigDecimal(800),
                                                                            null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                                                                                        "Banque", null,
                                                                                        new BigDecimal(800)));
        comptabiliteDao.updateEcritureComptable(vEcritureComptable);

        EcritureComptable v2EcritureComptable = comptabiliteDao.getEcritureComptable(-1);
        Assertions.assertThat(v2EcritureComptable.getLibelle()).isEqualTo("Portable");
        Assertions.assertThat(vEcritureComptable.getListLigneEcriture()).isNotEmpty();
        Assertions.assertThat(vEcritureComptable.getListLigneEcriture().size() > 1);
        Assertions.assertThat(v2EcritureComptable.getListLigneEcriture().contains(800));
    }

    /**
     * Test si la méthode renvoie bien séquence de l'écriture comptable pour le code journal et
     * l'année passé en paramètre
     * @see ComptabiliteDaoImpl#getLastValueSequenceEcritureComptableforYear(String, Integer) 
     * @throws NotFoundException
     */
    @Test
    public void checkGetLastValueSequenceEcritureComptableforYearNominal() throws NotFoundException {
        SequenceEcritureComptable sequenceEcritureComptable = comptabiliteDao.
                getLastValueSequenceEcritureComptableforYear("AC", 2016);

        Assertions.assertThat(sequenceEcritureComptable.getDerniereValeur()).isEqualTo(2);
    }

    /**
     * Test le déclenchement de l'exception si la séquence de l'écriture comptable n'a pas été trouvé
     * @see ComptabiliteDaoImpl#getLastValueSequenceEcritureComptableforYear(String, Integer)
     * @throws NotFoundException
     */
    @Test(expected = NotFoundException.class)
    public void checkGetLastValueSequenceEcritureComptableforYear() throws NotFoundException {
        SequenceEcritureComptable sequenceEcritureComptable = comptabiliteDao.
                getLastValueSequenceEcritureComptableforYear("AC", 2020);

    }

    /**
     * Test l'insertion d'une séquence d'une écriture comptable dans la base de données
     * @see ComptabiliteDaoImpl#insertSequenceEcritureComptable(SequenceEcritureComptable)
     */
    @Test
    public void checkInsertSequenceEcritureComptableNominal() throws NotFoundException {
        SequenceEcritureComptable vSequenceEcritureComptable = new SequenceEcritureComptable();
        vSequenceEcritureComptable.setJournalCode("AC");
        vSequenceEcritureComptable.setAnnee(2019);
        vSequenceEcritureComptable.setDerniereValeur(1);

        comptabiliteDao.insertSequenceEcritureComptable(vSequenceEcritureComptable);

        SequenceEcritureComptable v2SequenceEcritureComptable = comptabiliteDao.
                getLastValueSequenceEcritureComptableforYear("AC", 2019);
        Assertions.assertThat(v2SequenceEcritureComptable.getDerniereValeur()).isEqualTo(1);
    }

    /**
     * Test le déclenchement de l'exception si la séquence de l'écriture comptable existe déjà
     * @see ComptabiliteDaoImpl#insertSequenceEcritureComptable(SequenceEcritureComptable)
     * @throws NotFoundException
     */
    @Test(expected = NotFoundException.class)
    public void checkInsertSequenceEcritureComptable() throws NotFoundException{
        SequenceEcritureComptable vSequenceEcritureComptable = new SequenceEcritureComptable();
        vSequenceEcritureComptable.setJournalCode("AC");
        vSequenceEcritureComptable.setAnnee(2016);
        vSequenceEcritureComptable.setDerniereValeur(1);

        comptabiliteDao.insertSequenceEcritureComptable(vSequenceEcritureComptable);
    }

    /**
     * Test la mise à jour d'une séquence d'une écriture comptable dans la base de données
     * @see ComptabiliteDaoImpl#updateSequenceEcritureComptable(SequenceEcritureComptable)
     * @throws NotFoundException
     */
    @Test
    public void checkUpdateSequenceEcritureComptableNominal() throws NotFoundException {
        SequenceEcritureComptable vSequenceEcritureComptable = new SequenceEcritureComptable();
        vSequenceEcritureComptable.setJournalCode("AC");
        vSequenceEcritureComptable.setAnnee(2016);
        vSequenceEcritureComptable.setDerniereValeur(2);

        comptabiliteDao.updateSequenceEcritureComptable(vSequenceEcritureComptable);
        SequenceEcritureComptable v2SequenceEcritureComptable = comptabiliteDao.
                getLastValueSequenceEcritureComptableforYear("AC", 2016);
        Assertions.assertThat(v2SequenceEcritureComptable.getDerniereValeur()).isEqualTo(2);

    }

    /**
     * Test le déclenchement de l'exception si la séquence de l'écriture comptable existe pas
     * @see ComptabiliteDaoImpl#updateSequenceEcritureComptable(SequenceEcritureComptable)
     * @throws NotFoundException
     */
    @Test(expected = NotFoundException.class)
    public void checkUpdateSequenceEcritureComptable() throws NotFoundException {
        SequenceEcritureComptable vSequenceEcritureComptable = new SequenceEcritureComptable();
        vSequenceEcritureComptable.setJournalCode("AC");
        vSequenceEcritureComptable.setAnnee(2020);
        vSequenceEcritureComptable.setDerniereValeur(2);

        comptabiliteDao.updateSequenceEcritureComptable(vSequenceEcritureComptable);
    }

    /**
     * Test la méthode qui renvoie la dernière écriture comptable persistée
     * @see ComptabiliteDaoImpl#getLastOneEcritureComptable()
     */
    @Test
    public void checkGetLastOneEcritureComptable() {
        EcritureComptable vEcritureComptable = comptabiliteDao.getLastOneEcritureComptable();
        Assertions.assertThat(vEcritureComptable.getId()).isEqualTo(-5);

        BigDecimal vRetour = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        vRetour = vRetour.add(new BigDecimal(3000));

        for (LigneEcritureComptable ligneEcritureComptable : vEcritureComptable.getListLigneEcriture()) {
            if (ligneEcritureComptable.getDebit() != null) {
                Assertions.assertThat(ligneEcritureComptable.getDebit()).isEqualTo(vRetour);
            }
            if (ligneEcritureComptable.getCredit() != null) {
                Assertions.assertThat(ligneEcritureComptable.getCredit()).isEqualTo(vRetour);
            }
        }
    }

    /**
     * Test la suppression d'une écriture comptable 
     * @see ComptabiliteDaoImpl#deleteEcritureComptable(Integer) 
     * @throws NotFoundException
     */
   @Test(expected = NotFoundException.class)
    public void checkDeleteEcritureComptable() throws NotFoundException {
        Integer pId = 2;
        comptabiliteDao.deleteEcritureComptable(pId);
        Assertions.assertThat(comptabiliteDao.getEcritureComptable(2)).isEqualTo(null);
    }
}
