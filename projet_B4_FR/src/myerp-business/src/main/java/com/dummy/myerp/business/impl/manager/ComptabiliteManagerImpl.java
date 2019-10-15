package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================


    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    /**
     * {@inheritDoc}
     */
    // TODO à tester
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) {

        // TODO à implémenter
        // Bien se réferer à la JavaDoc de cette méthode !
        /* Le principe : Remonter depuis la persistance la dernière valeur de la séquence du journal pour l'année
        de l'écriture (table sequence_ecriture_comptable)*/

         /* S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
            Utiliser le numéro 1 sinon utiliser la dernière valeur + 1 */

        // Apple la méthode extractCurrentYear() pour extraire année de la date passé en paramètre
        Integer currentYear = extractCurrentYear(pEcritureComptable);

       try {

           // Remonte depuis la persistance la dernière valeur de la séquence du journal pour l'année de l'écriture
           SequenceEcritureComptable sequenceEcritureComptable = getDaoProxy().getComptabiliteDao()
                   .getLastValueSequenceEcritureComptableforYear(
                           pEcritureComptable.getJournal().getCode(), currentYear);

           // Mise à jour de la dernière valeur augmentée de 1 d'écriture Comptable
           sequenceEcritureComptable.setDerniereValeur(sequenceEcritureComptable.getDerniereValeur() + 1);
           // Enregistrer (update) la valeur de la séquence en persitance (table sequence_ecriture_comptable)
           getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(sequenceEcritureComptable);

            // Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)
           pEcritureComptable.getJournal().setCode(pEcritureComptable.getJournal().getCode());

           // Appel de la classe formatageReference() pour formater la référence selon le RG_Compta_5
           pEcritureComptable.setReference(formatageReference(pEcritureComptable));
           pEcritureComptable.setDate(pEcritureComptable.getDate());
           pEcritureComptable.setLibelle(pEcritureComptable.getLibelle());

           System.out.println(pEcritureComptable.getReference() + " /ID Ecriture Comptable : " + pEcritureComptable.getId());

           getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);

       } catch (NotFoundException vEX) {
           // Création d'une nouvelle séquence d'écriture comptable
           SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
           sequenceEcritureComptable.setJournalCode(pEcritureComptable.getJournal().getCode());
           sequenceEcritureComptable.setAnnee(currentYear);
           sequenceEcritureComptable.setDerniereValeur(1);

          // Enregistrer (insert) la valeur de la séquence en persitance (table sequence_ecriture_comptable)
           getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(sequenceEcritureComptable);

           pEcritureComptable.getJournal().setCode(pEcritureComptable.getJournal().getCode());

           // Appel la classe formatageReference() pour formater la référence selon le RG_Compta_5
           pEcritureComptable.setReference(formatageReference(pEcritureComptable));

           pEcritureComptable.setDate(pEcritureComptable.getDate());
           pEcritureComptable.setLibelle(pEcritureComptable.getLibelle());

           System.out.println(pEcritureComptable.getReference() + " /ID Ecriture Comptable : " + pEcritureComptable.getId());

           // Insert de la référence de l'écriture avec la référence calculée (RG_Compta_5)
           getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
       }

    }

    /**
     * {@inheritDoc}
     */
    // TODO à tester
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    // TODO tests à compléter
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture

        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
                                          new ConstraintViolationException(
                                              "L'écriture comptable ne respecte pas les contraintes de validation",
                                              vViolations));
        }

        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
        }

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
            || vNbrCredit < 1
            || vNbrDebit < 1) {
            throw new FunctionalException(
                "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }

        // TODO ===== RG_Compta_5 : Format et contenu de la référence
        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
//        addReference(pEcritureComptable);
        pEcritureComptable.setReference(formatageReference(pEcritureComptable));

        Integer currentYear = extractCurrentYear(pEcritureComptable);

        String[] referenceSplitCodeJournal = pEcritureComptable.getReference().split("-", 2);
        String[] referenceSplitDate = referenceSplitCodeJournal[1].split("/", 5);

        if(referenceSplitCodeJournal[0] == pEcritureComptable.getJournal().getCode()){
            throw new FunctionalException("L'année dans la référence ne correspond pas à la date de l'écriture.");
        }

        if (referenceSplitDate[0].equals(currentYear)) {
            throw new FunctionalException("Le code journal ne correspond pas au code journal de l'écriture.");
        }

    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                    pEcritureComptable.getReference());

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                    || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * Formatage de la référence RG_Compta_5
     * @param pEcritureComptable
     * @return
     */
    public String formatageReference(EcritureComptable pEcritureComptable){

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(pEcritureComptable.getDate());
        Integer currentYear = calendar.get(Calendar.YEAR);

        // Remonter depuis la persistance la dernière écriture comptable
        EcritureComptable ecritureComptable = getDaoProxy().getComptabiliteDao().getLastOneEcritureComptable();
        // Extraction du numéro de séquence de la référence
        String[] referenceSplit = ecritureComptable.getReference().split("/", 7);
        // Conversion du numéro de séquence en Integer
        Integer numeroSequence = Integer.valueOf(referenceSplit[1]) + 1;
        // Extraction du nombre de zéros a conservé devant le numéro de séquence
        String[] zerosInFront = referenceSplit[1].split(Integer.valueOf(referenceSplit[1]).toString(),
                referenceSplit[1].length() - numeroSequence.toString().length());

        String reference = pEcritureComptable.getJournal().getCode() + "-" + currentYear
                + "/" + zerosInFront[0] + numeroSequence.toString();
        return reference;
    }

    /**
     * Extrait l'année de la date
     * @param pEcritureComptable
     * @return currentYear
     */
    public Integer extractCurrentYear(EcritureComptable pEcritureComptable) {

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(pEcritureComptable.getDate());
        Integer currentYear = calendar.get(Calendar.YEAR);

        return currentYear;
    }
}
