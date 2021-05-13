package com.politecnico.masterchef_pmdm_albajonathan;

import android.provider.BaseColumns;

public final class Contract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private Contract() {}

    /* Inner class that defines the table contents */
    public static class Votaciones implements BaseColumns {
        public static final String TABLE_NAME = "votaciones";
        public static final String COLUMN_NAME_PRESENTACION = "Presentacion";
        public static final String COLUMN_NAME_SERVICIO = "Servicio";
        public static final String COLUMN_NAME_SABOR = "Sabor";
        public static final String COLUMN_NAME_IMAGEN = "Imagen";
        public static final String COLUMN_NAME_TRIPTICO = "Triptico";
        public static final String COLUMN_NAME_JUEZ = "Juez";
        public static final String COLUMN_NAME_EVENTO = "Evento";
        public static final String COLUMN_NAME_EQUIPO = "Equipo";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Votaciones.TABLE_NAME + " (" +
                    Votaciones.COLUMN_NAME_PRESENTACION + " TEXT, " +
                    Votaciones.COLUMN_NAME_SERVICIO + " TEXT, " +
                    Votaciones.COLUMN_NAME_SABOR + " TEXT, " +
                    Votaciones.COLUMN_NAME_IMAGEN + " TEXT, " +
                    Votaciones.COLUMN_NAME_TRIPTICO + " TEXT, " +
                    Votaciones.COLUMN_NAME_JUEZ + " TEXT, " +
                    Votaciones.COLUMN_NAME_EVENTO + " TEXT, " +
                    Votaciones.COLUMN_NAME_EQUIPO + " TEXT " +
                    ")";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Votaciones.TABLE_NAME;

}
