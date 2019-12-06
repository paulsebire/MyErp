package com.dummy.myerp.consumer.db.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;


/**
 * Classe utilitaire travaillant sur les ResultSet
 */
public abstract class ResultSetHelper {

    // ==================== Constructeurs ====================
    /**
     * Constructeur.
     */
    protected ResultSetHelper() {
        super();
    }


    // ==================== Méthodes ====================
    /**
     * Renvoie la valeur de la colonne pColName dans un <code>Integer</code>.
     * Si la colonne vaut <code>null</code>, la méthode renvoie <code>null</code>
     *
     * @param pRS : Le ResultSet à intéroger
     * @param pColName : Le nom de la colonne dans le retour de la requête SQL
     * @return <code>Integer</code> ou <code>null</code>
     * @throws SQLException sur erreur SQL
     */
    public static Integer getInteger(ResultSet pRS, String pColName) throws SQLException {
        return  pRS.getInt( pColName );
    }

    /**
     * Renvoie la valeur de la colonne pColName dans un {@link Date} en faisant un truncate de l'heure.
     * Si la colonne vaut <code>null</code>, la méthode renvoie <code>null</code>.
     *
     * @param pRS : Le ResultSet à intéroger
     * @param pColName : Le nom de la colonne dans le retour de la requête SQL
     * @return {@link Date} ou <code>null</code>
     * @throws SQLException sur erreur SQL
     */
    public static Date getDate(ResultSet pRS, String pColName) throws SQLException {
        return DateUtils.truncate( pRS.getDate(pColName) , Calendar.DATE);
    }


}
