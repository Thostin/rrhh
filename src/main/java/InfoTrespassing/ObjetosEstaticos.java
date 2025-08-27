/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfoTrespassing;

import clases.DbCurso;
import clases.DbEspecialidad;
import clases.DbFuncionario;
import clases.DbOcupacion;
import clases.DbSala;
import clases.DetalleFuncionario;

/**
 *
 * @author thotstin
 */
public class ObjetosEstaticos {

    public static boolean confirmado = false;
    public static DbFuncionario funcionario = new DbFuncionario();
    public static DbFuncionario reloadFuncionario = null;

    public static String nombreSala;

    public static DbSala sala = new DbSala();
    public static DbSala reloadSala = null;
    public static DbSala salaSeleccion = null;

    public static DbCurso curso = new DbCurso();
    public static DbCurso reloadCurso = null;
    public static DbCurso cursoSeleccion = null;

    public static DbEspecialidad especialidad = new DbEspecialidad();
    public static DbEspecialidad reloadEspecialidad = null;
    public static DbEspecialidad especialidadSeleccion = null;

    // Para cuando se quiera modificar un curso, este se usa como objeto intermediario
    public static DbEspecialidad espeModificarCurso = new DbEspecialidad();

    public static DbFuncionario funcionarioSeleccion;

    /* Para el detalle de mostrar el horario del funcionario en el FXML detalleFuncionario */
    public static int idFuncionario;

    /* CUANDO SE QUIERA MODIFICAR EL HORARIO */
    public static boolean seModificaHorario = false;
    public static DbOcupacion ocupacionModificar = null;
    public static DetalleFuncionario detalleOcupacionModificar; // Este es exclusivamente para pasar la hora en formato adecuado (que ya se tenia pues de la tabla donde se muestran los horarios)

    /* ES PARA INDICAR SI ES NECESARIO BUSCAR EL FUNCIONARIO EN cargarHorario.fxml */
    public static DbFuncionario funcionarioCargarHorario = null;
    public static boolean tieneFuncionario = false;

}
