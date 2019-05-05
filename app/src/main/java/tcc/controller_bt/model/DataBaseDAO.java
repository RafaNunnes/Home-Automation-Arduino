package tcc.controller_bt.model;

import java.util.ArrayList;

/**
 * Interface para implementação do CRUD (Create-Read-Update-Delete) do Banco de Dados
 * através do padrão de projeto Data Access Object (DAO)
 */
public interface DataBaseDAO {

    // Create
    public void createTable();


    // Read
    public ArrayList<DeviceControlButton> getControlButtons();

    public DeviceControlButton getControlButtonById(long id);


    // Update
    public long addControlButton(String name, int control_type, int logical_port, String infrared_code, int format_type, int num_bits);

    public boolean updateControlButton(DeviceControlButton control_button);


    // Delete
    public void removeTable();

    public boolean removeControlButton(DeviceControlButton control_button);
}
