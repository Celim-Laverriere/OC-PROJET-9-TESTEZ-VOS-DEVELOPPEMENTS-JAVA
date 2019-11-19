package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;


public class ComptabiliteManagerImplTest {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();


    @Test
    public void checkEcritureComptableUnit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2019/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(1234)));
        manager.checkEcritureComptableUnit(vEcritureComptable);

    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(0),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null,  null,
                                                                                 new BigDecimal(0)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG5CodeJournal() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AG-2019/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                null, new BigDecimal(123),
                                                                                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                null, null,
                                                                                        new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG5Date() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2018/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                null, new BigDecimal(123),
                                                                                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                null, null,
                                                                                        new BigDecimal(123)));

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }


    @Mock
    private ComptabiliteDao comptabiliteDaoMock;
    @Mock
    private DaoProxy daoProxyMock;
    @Mock
    private TransactionManager transactionManagerMock;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        when(this.daoProxyMock.getComptabiliteDao()).thenReturn(this.comptabiliteDaoMock);
        AbstractBusinessManager.configure(null, this.daoProxyMock, this.transactionManagerMock);
    }

    @Test
    public void checkUpdateAddReferenceNominal() throws Exception {

        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(-1);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                null, new BigDecimal(123),
                                                                                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                null, null,
                                                                                        new BigDecimal(123)));

        SequenceEcritureComptable rSequenceEcritureComptable = new SequenceEcritureComptable();
        rSequenceEcritureComptable.setJournalCode("AC");
        rSequenceEcritureComptable.setAnnee(2019);
        rSequenceEcritureComptable.setDerniereValeur(78);

        when(this.comptabiliteDaoMock.getLastValueSequenceEcritureComptableforYear("AC", 2019))
                .thenReturn(rSequenceEcritureComptable);

        manager.addReference(vEcritureComptable);
        Assertions.assertThat(rSequenceEcritureComptable.getDerniereValeur()).isEqualTo(79);
        Assertions.assertThat(rSequenceEcritureComptable.getJournalCode()).isEqualTo("AC");
        Assertions.assertThat(rSequenceEcritureComptable.getAnnee()).isEqualTo(2019);
        Assertions.assertThat(vEcritureComptable.getReference()).isEqualTo("AC-2019/00079");

    }

    @Test(expected = NotFoundException.class)
    public void checkUpdateAddReference() throws Exception {

        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(-1);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));

        SequenceEcritureComptable rSequenceEcritureComptable = new SequenceEcritureComptable();
        rSequenceEcritureComptable.setJournalCode("AC");
        rSequenceEcritureComptable.setAnnee(2019);
        rSequenceEcritureComptable.setDerniereValeur(78);

        when(this.comptabiliteDaoMock.getLastValueSequenceEcritureComptableforYear("BQ", 2019))
                .thenReturn(rSequenceEcritureComptable);

        manager.addReference(vEcritureComptable);
    }

    @Test
    public void checkInsertAddReferenceNominal() throws Exception {

        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("BQ", "Banque"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Paiement Facture C120003");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(600),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(600)));

        SequenceEcritureComptable rSequenceEcritureComptable = new SequenceEcritureComptable();
        rSequenceEcritureComptable.setJournalCode("BQ");
        rSequenceEcritureComptable.setAnnee(2019);
        rSequenceEcritureComptable.setDerniereValeur(1);

        when(this.comptabiliteDaoMock.getLastValueSequenceEcritureComptableforYear("AC", 2019))
                .thenReturn(rSequenceEcritureComptable);

        manager.addReference(vEcritureComptable);
        Assertions.assertThat(rSequenceEcritureComptable.getDerniereValeur()).isEqualTo(1);
        Assertions.assertThat(rSequenceEcritureComptable.getJournalCode()).isEqualTo("BQ");
        Assertions.assertThat(rSequenceEcritureComptable.getAnnee()).isEqualTo(2019);
        Assertions.assertThat(vEcritureComptable.getReference()).isEqualTo("BQ-2019/00001");
    }

    @Test(expected = FunctionalException.class)
    public void checkInsertAddReference() throws Exception {

        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("BQ", "Banque"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Paiement Facture C120003");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(600),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(600)));

        SequenceEcritureComptable rSequenceEcritureComptable = new SequenceEcritureComptable();
        rSequenceEcritureComptable.setJournalCode("BQ");
        rSequenceEcritureComptable.setAnnee(2019);
        rSequenceEcritureComptable.setDerniereValeur(1);

        when(this.comptabiliteDaoMock.getLastValueSequenceEcritureComptableforYear("BQ", 2019))
                .thenReturn(rSequenceEcritureComptable);

        manager.addReference(vEcritureComptable);

    }

}
