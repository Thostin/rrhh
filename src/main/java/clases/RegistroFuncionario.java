/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package clases;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author thotstin
 */
public record RegistroFuncionario(LocalDate getFecha, String getHoraEntrada, String getHoraSalida, String getMarcaEntrada, 
        String getMarcaSalida, String getTardanza, String getSalidaAnticipada, String getFalta)
{
}
