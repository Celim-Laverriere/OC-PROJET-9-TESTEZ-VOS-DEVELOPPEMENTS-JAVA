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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ComptabiliteManagerImplIntegrationTest extends BusinessTestCase {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    /**
     * Test si la méthode renvoie bien la liste des comptes comptables
     * @see ComptabiliteManagerImpl#getListCompteComptable()
     */
    @Test
    public void checkGetListCompteComptable() {
        List<CompteComptable> compteComptableList = manager.getListCompteComptable();
        Assertions.assertThat(compteComptableList).isNotEmpty();
    }

    /**
     * Test si la méthode renvoie bien la liste des journals comptable
     * @see ComptabiliteManagerImpl#getListJournalComptable() 
      */
    @Test
    public void checkGetListJournalComptable() {
        List<JournalComptable> journalComptableList = manager.getListJournalComptable();
        Assertions.assertThat(journalComptableList).isNotEmpty();
    }

    /**
     * Test si la méthode renvoie bien la liste des écritures comptables
     * @see ComptabiliteManagerImpl#getListEcritureComptable() 
     */
    @Test
    public void checkGetListEcritureComptable() {
        List<EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();
        Assertions.assertThat(ecritureComptableList).isNotEmpty();
    }

    /**
     * Test l'insertion d'une écriture comptable dans la base de données
     * @see ComptabiliteManagerImpl#insertEcritureComptable(EcritureComptable) 
     * @throws FunctionalException
     */
    @Test
    public void checkInsertEcritureComptableNominal() throws FunctionalException {
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

        List<EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();

        BigDecimal vRetour = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        vRetour = vRetour.add(new BigDecimal(123));

        for (EcritureComptable ecritureComptable : ecritureComptableList) {
            if (ecritureComptable.getReference().equals("VE-2019/00006")) {
                Assertions.assertThat(ecritureComptable.getLibelle()).isEqualTo("TMA Appli Yyy");

                for (LigneEcritureComptable ligneEcritureComptable : ecritureComptable.getListLigneEcriture()) {
                    if (ligneEcritureComptable.getCredit() != null){
                        Assertions.assertThat(ligneEcritureComptable.getCredit()).isEqualTo(vRetour);
                    }
                    if (ligneEcritureComptable.getDebit() != null){
                        Assertions.assertThat(ligneEcritureComptable.getDebit()).isEqualTo(vRetour);
                    }
                }
            }
        }
    }

    /**
     * Test le déclenchement de l'exception lors de l'insertion d'une écriture comptable qui exite déjà
     * @see ComptabiliteManagerImpl#insertEcritureComptable(EcritureComptable) 
     * @throws FunctionalException
     */
    @Test(expected = FunctionalException.class)
    public void checkInsertEcritureComptable() throws FunctionalException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
        String date = "2016-12-29";

        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("BQ", "Banque"));
        vEcritureComptable.setReference("BQ-2016/00003");
        try {
            vEcritureComptable.setDate(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        vEcritureComptable.setLibelle("Paiement Facture F110001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(706),
                null, null,
                new BigDecimal(123)));
        manager.insertEcritureComptable(vEcritureComptable);
    }

    /**
     * Test la mise à jour d'une écriture comptable dans la base de données
     * @see ComptabiliteManagerImpl#updateEcritureComptable(EcritureComptable) 
     * @throws FunctionalException
     */
    @Test
    public void checkUpdateEcritureComptableNominal() throws FunctionalException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
        String date = "2016-12-30";

        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(-2);
        vEcritureComptable.setJournal(new JournalComptable("VE", "Vente"));
        try {
            vEcritureComptable.setDate(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        vEcritureComptable.setReference("VE-2016/00002");
        vEcritureComptable.setLibelle("Imprimante");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, new BigDecimal(250),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(706),
                null, null,
                new BigDecimal(250)));

        manager.updateEcritureComptable(vEcritureComptable);
        List<EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();

        BigDecimal vRetour = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        vRetour = vRetour.add(new BigDecimal(250));

        for (EcritureComptable ecritureComptable : ecritureComptableList) {

            if (ecritureComptable.getId().equals(-2)) {
                Assertions.assertThat(ecritureComptable.getLibelle()).isEqualTo("Imprimante");
                Assertions.assertThat(ecritureComptable.getReference()).isEqualTo("VE-2016/00002");

                for (LigneEcritureComptable ligneEcritureComptable : ecritureComptable.getListLigneEcriture()) {
                    if (ligneEcritureComptable.getCredit() != null){
                        Assertions.assertThat(ligneEcritureComptable.getCredit()).isEqualTo(vRetour);
                    }
                    if (ligneEcritureComptable.getDebit() != null){
                        Assertions.assertThat(ligneEcritureComptable.getDebit()).isEqualTo(vRetour);
                    }
                }
            }
        }
    }

    /**
     * Test le déclenchement de l'exception lors de la mise à jour d'une écriture comptable
     * si Aucune écriture comptable existe pour la référence
     * @see ComptabiliteManagerImpl#updateEcritureComptable(EcritureComptable) 
     * @throws FunctionalException
     */
    @Test(expected = FunctionalException.class)
    public void checkUpdateEcritureComptable() throws FunctionalException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(-1);
        vEcritureComptable.setJournal(new JournalComptable("AR", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setReference("AR-2019/00001");
        vEcritureComptable.setLibelle("Imprimante");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                                                                                null, new BigDecimal(250),
                                                                                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(706),
                                                                                            null, null,
                                                                                            new BigDecimal(250)));

        manager.updateEcritureComptable(vEcritureComptable);
    }

    /**
     * Test la suppression d'une écriture comptable
     * @see ComptabiliteManagerImpl#deleteEcritureComptable(Integer) 
     */
    @Test
    public void checkdeleteEcritureComptable() {
        Integer pId = 1;
        manager.deleteEcritureComptable(pId);

        List<EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();

        for (EcritureComptable ecritureComptable : ecritureComptableList) {
            Assertions.assertThat(ecritureComptable.getId()).isNotEqualTo(1);
        }
    }

}
