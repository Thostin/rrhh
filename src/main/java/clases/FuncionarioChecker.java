/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thotstin
 */
public class FuncionarioChecker {

    private static record Par(int inicio, int fin) {

    }

    // Reduce a los minutos que pasaron (redondeado hacia abajo)
    // de los minutos que pasaron desde el inicio de la 
    // semana de esa fecha
    private static int toMinute(LocalDateTime dateTime) {
        int dia = dateTime.getDayOfWeek().getValue();

        return (dia - 1) * 24 * 60 + dateTime.getHour() * 60 + dateTime.getMinute();
    }

    // Devuelve todos los bloques de horarios no compensatorios del horario del funcionario 
    private static ArrayList<Par> getHorasFijas(int idFuncionario) {
        ArrayList<Par> horasNoCompensadas = new ArrayList();
        String sql = "SELECT horaInicio, horaFin FROM Ocupacion WHERE idFuncionario=" + idFuncionario + " AND compensatorio=0 ORDER BY horaInicio ASC";

        try (Connection con = ConnectionPool.getConnection()) {
            Statement stm = con.createStatement();
            ResultSet resultado = stm.executeQuery(sql);

            Par par;
            while (resultado.next()) {
                par = new Par(resultado.getInt("horaInicio"), resultado.getInt("horaFin"));
                horasNoCompensadas.add(par);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return horasNoCompensadas;

    }

    private static ArrayList<Par> getHorario(int idFuncionario) {
        ArrayList<Par> horasNoCompensadas = new ArrayList();
        String sql = "SELECT horaInicio, horaFin FROM Ocupacion WHERE idFuncionario=" + idFuncionario + " ORDER BY horaInicio ASC";

        try (Connection con = ConnectionPool.getConnection()) {
            Statement stm = con.createStatement();
            ResultSet resultado = stm.executeQuery(sql);

            Par par;
            while (resultado.next()) {
                par = new Par(resultado.getInt("horaInicio"), resultado.getInt("horaFin"));
                horasNoCompensadas.add(par);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return horasNoCompensadas;
    }

    private static ArrayList<Par> getHorasNoFijas(int idFuncionario) {
        ArrayList<Par> horasNoCompensadas = new ArrayList();
        String sql = "SELECT horaInicio, horaFin FROM Ocupacion WHERE idFuncionario=" + idFuncionario + " AND compensatorio=1 ORDER BY horaInicio ASC";

        try (Connection con = ConnectionPool.getConnection()) {
            Statement stm = con.createStatement();
            ResultSet resultado = stm.executeQuery(sql);

            Par par;
            while (resultado.next()) {
                par = new Par(resultado.getInt("horaInicio"), resultado.getInt("horaFin"));
                horasNoCompensadas.add(par);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return horasNoCompensadas;

    }

    private static int semiMu(int x) {
        return x < 0 ? 0 : x;
    }

    private static record ParDateTime(LocalDateTime inicio, LocalDateTime fin) {

    }

    private static ArrayList<ParDateTime> getRegistros(int idFuncionario, LocalDate inicio, LocalDate fin) {
        ArrayList<ParDateTime> registros = new ArrayList();
        //String sql = "SELECT hora FROM REGISTROS WHERE idFuncionario=" + idFuncionario + " ORDER BY hora ASC;" + idFuncionario;

        // Este comando fue primariamente redactado por chaepete
        // ! CHATGPT
        String sql
                = """
WITH numbered AS (
    SELECT *,
           ROW_NUMBER() OVER (PARTITION BY DATE(hora)
                              ORDER BY hora) AS rn
    FROM REGISTROS
    WHERE hora BETWEEN ?
                                 AND ?
                                 AND idFuncionario=?
)
SELECT n1.hora AS entrada,
       n2.hora AS salida
FROM numbered n1
JOIN numbered n2
  ON n1.rn % 2 = 1                 -- odd row = first in pair
 AND n2.rn = n1.rn + 1              -- next row = second in pair
 AND DATE(n1.hora) = DATE(n2.hora)  -- same day
ORDER BY n1.hora;
""";

        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setObject(1, inicio.atStartOfDay());
            stm.setObject(2, fin.plusDays(1).atStartOfDay());
            stm.setInt(3, idFuncionario);

            ResultSet resultado = stm.executeQuery();
            while (resultado.next()) {
                registros.add(new ParDateTime(resultado.getObject("entrada", LocalDateTime.class),
                        resultado.getObject("salida", LocalDateTime.class)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return registros;
    }

    //
    private static int totalMinutos(ArrayList<Par> horario, LocalDate inicio, LocalDate fin) {
        int total = 0;
        for (Par intervalo : horario) {
            total += diasEnMedio(1 + intervalo.inicio / 1440, inicio, fin) * (intervalo.fin - intervalo.inicio);
        }

        return total;
    }

    // dia varia de 1 a 7
    private static int diasEnMedio(int dia, LocalDate inicio, LocalDate fin) {
        int shift = (dia - inicio.getDayOfWeek().getValue() + 7) % 7;

        LocalDate reInicio = inicio.plusDays(shift);

        if (reInicio.isAfter(fin)) {
            return 0;
        }

        int cantidadDias = (int) ChronoUnit.DAYS.between(reInicio, fin);
        return 1 + cantidadDias / 7;
    }

    // En vez de LocalDateTime, se puede usar solamente Date, porque solo miro la fecha
    // El sistema es bastante flexible, no se es necesario comparar fecha por fecha, pero
    // si hubo o no solapamiento y cuanto suman esos solapamientos
    // Despues hay que saber cuantas veces hay esa ocupacion en ese intervalo de fechas simplemente
    public static void checkHorarioFuncionario(DbFuncionario funcionario, LocalDate inicio, LocalDate fin) {
        int idFuncionario = funcionario.getId();

        ArrayList<Par> paresHorarioFijos = getHorasFijas(idFuncionario);
        ArrayList<ParDateTime> paresRegistrosDirecto = getRegistros(idFuncionario, inicio, fin);

        ArrayList<Par> paresRegistros = new ArrayList();

        for (ParDateTime intervalo : paresRegistrosDirecto) {
            paresRegistros.add(new Par(toMinute(intervalo.inicio), toMinute(intervalo.fin)));
        }

        int horaInicioHorario = 0, horaInicioRegistro = 0, horaFinHorario = 0, horaFinRegistro = 0;
        int sumaAporte = 0;
        int sumaAporteTotal = 0;
        int sumaFaltas = 0;
        int falta;
        int diaX = 0;
        int cantidadDias = 0;
        for (Par intervaloHorario : paresHorarioFijos) {
            diaX = 1 + intervaloHorario.inicio / 1440;
            cantidadDias = diasEnMedio(diaX, inicio, fin);
            if (0 == cantidadDias) {
                continue;
            }

            for (Par intervaloRegistro : paresRegistros) {
                horaInicioHorario = intervaloHorario.inicio;
                horaInicioRegistro = intervaloRegistro.inicio;

                horaFinHorario = intervaloHorario.fin;
                horaFinRegistro = intervaloRegistro.fin;

                falta
                        = semiMu(horaInicioRegistro - horaInicioHorario)
                        + semiMu(horaFinHorario - horaFinRegistro);
                sumaAporte += semiMu(horaFinHorario - horaInicioHorario - falta);
            }
            sumaFaltas += cantidadDias * (horaFinHorario - horaInicioHorario) - sumaAporte;
            sumaAporteTotal += sumaAporte;
            sumaAporte = 0;
        }

        int horas = sumaFaltas / 60;

        System.out.println(
                "La cantidad de minutos que se fallaron en el intervalo"
                + "de fechas: " + sumaFaltas + " (en cristiano: " + horas + " horas y " + sumaFaltas % 60 + " minutos)");

        // En minutos
        int compensado = 0;
        int sumaHoraTotal = 0;
        for (Par intervalo : paresRegistros) {
            sumaHoraTotal += intervalo.fin - intervalo.inicio;
        }

        System.out.println("Total de tiempo compensado: " + (sumaHoraTotal - sumaAporteTotal));
    }
}
