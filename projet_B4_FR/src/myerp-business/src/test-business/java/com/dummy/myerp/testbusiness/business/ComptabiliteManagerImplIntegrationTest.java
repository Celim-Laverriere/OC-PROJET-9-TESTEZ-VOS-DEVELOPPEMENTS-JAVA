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


    @Test
    public void checkGetListCompteComptable() {
        List<CompteComptable> compteComptableList = manager.getListCompteComptable();
        Assertions.assertThat(compteComptableList).isNotEmpty();
    }

    @Test
    public void checkGetListJournalComptable() {
        List<JournalComptable> journalComptableList = manager.getListJournalComptable();
        Assertions.assertThat(journalComptableList).isNotEmpty();
    }

    @Test
    public void checkGetListEcritureComptable() {
        List<EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();
        Assertions.assertThat(ecritureComptableList).isNotEmpty();
    }

    @Test
    public void checkEcritureComptable() throws FunctionalException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Vente"));
        vEcritureComptable.setReference("AC-2019/00006");
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("TMA Appli Yyy");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                                                                                null, new BigDecimal(123),
                                                                                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(706),
                                                                                            null, null,
                                                                                            new BigDecimal(123)));
        manager.checkEcritureComptable(vEcritureComptable);
    }

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

    @Test
    public void checkUpdateEcritureComptableNominal() throws FunctionalException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(-1);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setReference("AC-2019/00001");
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

            if (ecritureComptable.getId().equals(-1)) {
                Assertions.assertThat(ecritureComptable.getLibelle()).isEqualTo("Imprimante");
                Assertions.assertThat(ecritureComptable.getReference()).isEqualTo("AC-2019/00001");

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

    @Test
    public void checkdeleteEcritureComptable() {
        Integer pId = 17;
        manager.deleteEcritureComptable(pId);
    }


}
