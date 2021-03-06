package com.dummy.myerp.model.bean.comptabilite;


/**
 * Bean représentant une séquence pour les références d'écriture comptable
 */
public class SequenceEcritureComptable {

    // ==================== Attributs ====================
    /** L'année */
    private Integer annee;
    /** La dernière valeur utilisée */
    private Integer derniereValeur;
    /** Le journal_code */
    private String journalCode;

    // ==================== Constructeurs ====================
    /**
     * Constructeur
     */
    public SequenceEcritureComptable() {
    }

    /**
     * Constructeur
     *
     * @param pJournalCode
     * @param pAnnee -
     * @param pDerniereValeur -
     */
    public SequenceEcritureComptable(String pJournalCode, Integer pAnnee, Integer pDerniereValeur) {
        journalCode = pJournalCode;
        annee = pAnnee;
        derniereValeur = pDerniereValeur;
    }


    // ==================== Getters/Setters ====================
    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer pAnnee) {
        annee = pAnnee;
    }

    public Integer getDerniereValeur() {
        return derniereValeur;
    }

    public void setDerniereValeur(Integer pDerniereValeur) {
        derniereValeur = pDerniereValeur;
    }

    public String getJournalCode() {
        return journalCode;
    }

    public void setJournalCode(String pJournalCode) {
        this.journalCode = pJournalCode;
    }

    // ==================== Méthodes ====================
    @Override
    public String toString() {
        final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
        final String vSEP = ", ";
        vStB.append("{")
            .append("annee=").append(annee)
            .append(vSEP).append("derniereValeur=").append(derniereValeur)
            .append("}");
        return vStB.toString();
    }
}
